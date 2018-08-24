package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;

public class SearchQuery {
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	
	public SearchQuery()
	{
		
	}
	
	public static String normalize_string(String s)
	{
		return s.replaceAll("[^a-zA-Z0-9 ]", "_").trim();
	}
	
	public static String get_id_from_movie_title(String title)
	{
		String query = "SELECT id \r\n" + 
				"FROM movies\r\n" + 
				"WHERE movies.title =  \"" + title + "\";";
		
			ArrayList<String[]> res_table = get_arrayList_from_query(query, 1);
			if(res_table.size() <= 0)
				return "";
		return res_table.get(0)[0];
	}
	
	private static String fetch_ft_string(String s)
	{
		String[] hold = s.split("\\s+");
		String result = "";
		
		for(int i = 0; i < hold.length; i++)
			result += "+" + hold[i].trim() + "* ";
			
		return result;
	}
	
	private static String fetch_fuzzy_string(String s, String att, int d)
	{
		String dist = Integer.toString(d);
		String result = "";
		// levenshtein
		result += "levenshtein(\"" + s + "\", title)<" + dist + " ";
			
		return result;
	}
	
	private String fetch_inputs(String[] s, char choice)
	{
		String result = "";
		if(!s[0].equals(""))
			switch(choice)
			{
				case 't':
				case 'T':
					result += "title LIKE \"%" + s[0] + "%\"";
					for(int i = 1; i < s.length; i++)
						result += " OR title LIKE \"%" + s[i] + "%\"";
					break;
				case 'y':
				case 'Y':
					result += "year = " + s[0];
					for(int i = 1; i < s.length; i++)
						result += " OR year = " + s[i];
					break;
				case 'd':
				case 'D':
					result += "director LIKE \"%" + s[0] + "%\"";
					for(int i = 1; i < s.length; i++)
						result += " OR director LIKE \"%" + s[i] + "%\"";
					break;
				case 's':
				case 'S':
					result += "AND (name LIKE \"%" + s[0] + "%\"";
					for(int i = 1; i < s.length; i++)
						result += " OR name LIKE \"%" + s[i] + "%\"";
					result += ")";
					break;
				default:
					break;
			}
		return result;
	}
	
	private String build_search_string(String movie_title, String movie_year, String movie_director)
	{
		String search_string = movie_title;
		
		if(search_string.equals("") || movie_year.equals(""))
			search_string += movie_year;
		else
			search_string += " AND (" + movie_year + ")";
		
		if(search_string.equals("") || movie_director.equals(""))
			search_string += movie_director;
		else
			search_string += " AND (" + movie_director + ")";
		
		if(!search_string.equals(""))
			search_string = "WHERE " + search_string + "\r\n";
		
		return search_string;
	}
	
	public String build_query_from_request(HttpServletRequest request)
	{
		// Get search inputs
		String[] titles = request.getParameter("movie_title").split(";");
		String[] years = request.getParameter("movie_year").split(";");
		String[] directors = request.getParameter("movie_director").split(";");
		String[] stars = request.getParameter("movie_star").split(";");

		String movie_title = fetch_inputs(titles, 't');
		String movie_year = fetch_inputs(years, 'y');
		String movie_director = fetch_inputs(directors, 'd');
		String movie_star = fetch_inputs(stars, 's');

		String search_string = build_search_string(movie_title, movie_year, movie_director);

		String query = "SELECT M.id, M.title, M.year, M.director, list_of_genres, list_of_stars\r\n" + 
				"FROM\r\n" + 
				"(\r\n" + 
				"SELECT id, title,year,director\r\n" + 
				"FROM movies\r\n" + 
				search_string + 
				"GROUP BY id\r\n" + 
				") M,\r\n" + 
				"(\r\n" + 
				"SELECT movies.id\r\n" + 
				"FROM movies, stars_in_movies as sim, stars\r\n" + 
				"WHERE sim.movieId = movies.id AND stars.id = sim.starId " + movie_star +"\r\n" + 
				"GROUP BY movies.id\r\n" + 
				") S,\r\n" + 
				"(\r\n" + 
				"SELECT movies.id, GROUP_CONCAT(genres.name ORDER BY genres.name ASC SEPARATOR ', ') as list_of_genres\r\n" + 
				"FROM movies, genres_in_movies as gim, genres\r\n" + 
				"WHERE gim.movieId = movies.id AND genres.id = gim.genreId\r\n" + 
				"GROUP BY movies.id\r\n" + 
				") G,\r\n" + 
				"(\r\n" + 
				"SELECT movies.id, GROUP_CONCAT(stars.name ORDER BY stars.name ASC SEPARATOR ', ') as list_of_stars\r\n" + 
				"FROM movies, stars_in_movies as sim, stars\r\n" + 
				"WHERE sim.movieId = movies.id AND stars.id = sim.starId\r\n" + 
				"GROUP BY movies.id\r\n" + 
				") SS\r\n" + 
				"WHERE M.id = G.id AND M.id = S.id AND M.id = SS.id\r\n" + 
				"GROUP BY M.id";
		
		return query;
	}
	
	private static ArrayList<String[]> get_arrayList_from_query(String query, int numb_of_att)
	{
		ArrayList<String[]> res_table = new ArrayList<>();
		
		try {
			// Incorporate mySQL driver
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			// Connect to the test database
			Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/moviedb?autoReconnect=true&useSSL=false&useUnicode=true&characterEncoding=utf8", "root", "112968Met@");
			// Create an execute an SQL statement to select all of table"Stars" records
			Statement select = connection.createStatement();
			
			ResultSet result = select.executeQuery(query);
			while(result.next())
			{
				String hold[] = new String[numb_of_att];
				for(int i = 0; i < numb_of_att; i++)
					hold[i] = result.getString(i+1);

				res_table.add(hold);
			}
			connection.close();
		} catch (Exception e) {
			  e.printStackTrace();
		}
		return res_table;
	}
	
	public static ArrayList<String[]> get_arrayList_from_search_query(String query, String att_order, String order,int start, int display_count)
	{
		String order_string = "\r\nORDER BY M." + att_order + " " + order;
		String limit = "\r\nLIMIT "+ Integer.toString(start) + ", " + Integer.toString(display_count);
		
		query += order_string + limit;
		
		ArrayList<String[]> res_table = get_arrayList_from_query(query, 6);
		
		return res_table;
	}
	
	/**
	 * Get result of a query searching for a star full name 
	 * @param star: star full name
	 * @return An arrayList that contain the star:
	 * 	full name, birth date, list_of_movies
	 */
	public static ArrayList<String[]> get_arrayList_from_star(String star)
	{
		String query = "SELECT name, IFNULL(birthYear, \"-\"), GROUP_CONCAT(movies.title ORDER BY movies.title ASC SEPARATOR ', ') as list_of_movies\r\n" + 
				"FROM movies, stars_in_movies as sim, stars\r\n" + 
				"WHERE sim.movieId = movies.id AND stars.id = sim.starId AND name LIKE \"" + star + "\"\r\n" + 
				"GROUP BY name, birthYear";
		ArrayList<String[]> res_table = get_arrayList_from_query(query, 3);
		
		return res_table;
	}
	
	/**
	 * Get result of a query searching for a movie title 
	 * @param movie:  movie title
	 * @return An arrayList that contain the movie:
	 * 	id, title, year, director, list_of_genres, list_of_stars
	 */
	public static ArrayList<String[]> get_arrayList_from_movie(String movie_id)
	{
		String query = "SELECT S.id, S.title, S.year, S.director, list_of_genres, list_of_stars\r\n" + 
				"FROM\r\n" + 
				"(\r\n" + 
				"SELECT movies.id, movies.title,movies.year,movies.director\r\n" + 
				"FROM movies, stars_in_movies as sim, stars\r\n" + 
				"WHERE sim.movieId = movies.id AND stars.id = sim.starId\r\n" + 
				"GROUP BY movies.id\r\n" + 
				") S,\r\n" + 
				"(\r\n" + 
				"SELECT movies.id, GROUP_CONCAT(genres.name ORDER BY genres.name ASC SEPARATOR ', ') as list_of_genres\r\n" + 
				"FROM movies, genres_in_movies as gim, genres\r\n" + 
				"WHERE gim.movieId = movies.id AND genres.id = gim.genreId\r\n" + 
				"GROUP BY movies.id\r\n" + 
				") G,\r\n" + 
				"(\r\n" + 
				"SELECT movies.id, GROUP_CONCAT(stars.name ORDER BY stars.name ASC SEPARATOR ', ') as list_of_stars\r\n" + 
				"FROM movies, stars_in_movies as sim, stars\r\n" + 
				"WHERE sim.movieId = movies.id AND stars.id = sim.starId\r\n" + 
				"GROUP BY movies.id\r\n" + 
				") SS\r\n" + 
				"WHERE S.id = G.id AND S.id = SS.id AND S.id = \"" + movie_id +"\"";
		ArrayList<String[]> res_table = get_arrayList_from_query(query, 6);
		
		return res_table;
	}
	
	public static ArrayList<String[]> get_arrayList_of_genres()
	{
		String query = "SELECT name \r\n" + 
				"FROM genres\r\n" + 
				"GROUP BY name";
		ArrayList<String[]> res_table = get_arrayList_from_query(query, 1);
		
		return res_table;
	}
	
	public static ArrayList<String[]> get_arrayList_from_genre(String genre,String att_order, String order,int start, int display_count)
	{
		String query = "SELECT S.id, S.title, S.year, S.director, list_of_genres, list_of_stars\r\n" + 
				"FROM \r\n" + 
				"(\r\n" + 
				"SELECT movies.id, movies.title,movies.year,movies.director\r\n" + 
				"FROM movies, stars_in_movies as sim, stars\r\n" + 
				"WHERE sim.movieId = movies.id AND stars.id = sim.starId\r\n" + 
				"GROUP BY movies.id\r\n" + 
				") S,\r\n" + 
				"(\r\n" + 
				"SELECT movies.id\r\n" + 
				"FROM movies, genres_in_movies as gim, genres \r\n" + 
				"WHERE gim.movieId = movies.id AND genres.id = gim.genreId AND genres.name LIKE \"" + genre + "\"\r\n" + 
				"GROUP BY movies.id\r\n" + 
				") G,\r\n" + 
				"(\r\n" + 
				"SELECT movies.id, GROUP_CONCAT(stars.name ORDER BY stars.name ASC SEPARATOR ', ') as list_of_stars\r\n" + 
				"FROM movies, stars_in_movies as sim, stars\r\n" + 
				"WHERE sim.movieId = movies.id AND stars.id = sim.starId\r\n" + 
				"GROUP BY movies.id \r\n" + 
				") SS,\r\n" + 
				"(\r\n" + 
				"SELECT movies.id, GROUP_CONCAT(genres.name ORDER BY genres.name ASC SEPARATOR ', ') as list_of_genres\r\n" + 
				"FROM movies, genres_in_movies as gim, genres \r\n" + 
				"WHERE gim.movieId = movies.id AND genres.id = gim.genreId\r\n" + 
				"GROUP BY movies.id\r\n" + 
				") GG\r\n" + 
				"WHERE S.id = G.id AND S.id = SS.id AND S.id = GG.id";
		
		String order_string = "\r\nORDER BY S." + att_order + " " + order;
		String limit = "\r\nLIMIT "+ Integer.toString(start) + ", " + Integer.toString(display_count);
		
		query += order_string + limit;
		ArrayList<String[]> res_table = get_arrayList_from_query(query, 6);
		
		return res_table;
	}
	
	public static ArrayList<String[]> get_arrayList_from_alpha(String alpha,String att_order, String order,int start, int display_count)
	{
		String query = "SELECT S.id, S.title, S.year, S.director, list_of_genres, list_of_stars\r\n" + 
				"FROM \r\n" + 
				"(\r\n" + 
				"SELECT movies.id, movies.title,movies.year,movies.director\r\n" + 
				"FROM movies, stars_in_movies as sim, stars\r\n" + 
				"WHERE sim.movieId = movies.id AND stars.id = sim.starId AND movies.title LIKE \"" + alpha + "%\"\r\n" + 
				"GROUP BY movies.id\r\n" + 
				") S,\r\n" + 
				"(\r\n" + 
				"SELECT movies.id,  GROUP_CONCAT(genres.name ORDER BY genres.name ASC SEPARATOR ', ') as list_of_genres\r\n" + 
				"FROM movies, genres_in_movies as gim, genres \r\n" + 
				"WHERE gim.movieId = movies.id AND genres.id = gim.genreId\r\n" + 
				"GROUP BY movies.id\r\n" + 
				") G,\r\n" + 
				"(\r\n" + 
				"SELECT movies.id, GROUP_CONCAT(stars.name ORDER BY stars.name ASC SEPARATOR ', ') as list_of_stars\r\n" + 
				"FROM movies, stars_in_movies as sim, stars\r\n" + 
				"WHERE sim.movieId = movies.id AND stars.id = sim.starId\r\n" + 
				"GROUP BY movies.id \r\n" + 
				") SS\r\n" + 
				"WHERE S.id = G.id AND S.id = SS.id";
		
		String order_string = "\r\nORDER BY S." + att_order + " " + order;
		String limit = "\r\nLIMIT "+ Integer.toString(start) + ", " + Integer.toString(display_count);
		
		query += order_string + limit;
		ArrayList<String[]> res_table = get_arrayList_from_query(query, 6);
		
		return res_table;
	}
	
	public static ArrayList<String[]> get_arrayList_of_tables()
	{
		String query = "SELECT t.table_name\r\n" + 
				"FROM information_schema.tables as t, information_schema.columns as c\r\n" + 
				"WHERE t.table_schema = \"moviedb\" AND t.table_name = c.table_name\r\n" + 
				"GROUP BY t.table_name;";
		
		ArrayList<String[]> res_table = get_arrayList_from_query(query, 1);
		
		return res_table;
	}
	
	public static ArrayList<String[]> get_arrayList_of_table_name(String table_name)
	{
		String query = "SELECT t.table_name, GROUP_CONCAT(c.column_name SEPARATOR ', ') as attribute, GROUP_CONCAT(c.data_type SEPARATOR ', ') as type\r\n" + 
				"FROM information_schema.tables as t, information_schema.columns as c\r\n" + 
				"WHERE t.table_schema = \"moviedb\" AND t.table_name = c.table_name AND t.table_name = \"" + table_name + "\"\r\n" + 
				"GROUP BY t.table_name;";
		
		ArrayList<String[]> res_table = get_arrayList_from_query(query, 3);
		
		return res_table;
	}
	
	public static ArrayList<String[]> get_arrayList_from_star_prefix(String star_name, int limit)
	{
		String ft_str = fetch_ft_string(star_name);
		String query = "SELECT entryId, entry\r\n" + 
				"FROM ft_stars\r\n" + 
				"WHERE MATCH (entry) AGAINST (\"" + ft_str + "\" IN BOOLEAN MODE)\r\n" + 
				"ORDER BY entry ASC\r\n" + 
				"LIMIT " + Integer.toString(limit) + ";";
		ArrayList<String[]> res_table = get_arrayList_from_query(query, 2);
		
		return res_table;
	}
	
	public static ArrayList<String[]> get_arrayList_from_star_fuzz(String star_name, int limit, int d)
	{
		String query = "SELECT entryId, entry\r\n" + 
				"FROM ft_stars\r\n" + 
				"WHERE levenshtein(\"" + star_name + "\", entry)<" + Integer.toString(d) + "\r\n" + 
				"ORDER BY entry ASC\r\n" + 
				"LIMIT " + Integer.toString(limit) + ";";
		ArrayList<String[]> res_table = get_arrayList_from_query(query, 2);
		
		return res_table;
	}
	
	public static ArrayList<String[]> get_arrayList_from_movie_prefix(String movie_title, int limit)
	{
		String ft_str = fetch_ft_string(movie_title);
		String query = "SELECT entryId, entry\r\n" + 
				"FROM ft_movies\r\n" + 
				"WHERE MATCH (entry) AGAINST (\"" + ft_str + "\" IN BOOLEAN MODE)\r\n" + 
				"ORDER BY entry ASC\r\n" + 
				"LIMIT " + Integer.toString(limit) + ";";
		ArrayList<String[]> res_table = get_arrayList_from_query(query, 2);
		
		return res_table;
	}
	
	public static ArrayList<String[]> get_arrayList_from_movie_fuzz(String movie_title, int limit, int d)
	{
		String query = "SELECT entryId, entry\r\n" + 
				"FROM ft_movies\r\n" + 
				"WHERE levenshtein(\"" + movie_title + "\", entry)<" + Integer.toString(d) + "\r\n" + 
				"ORDER BY entry ASC\r\n" + 
				"LIMIT " + Integer.toString(limit) + ";";
		ArrayList<String[]> res_table = get_arrayList_from_query(query, 2);
		
		return res_table;
	}
	
	public static String get_arrayList_for_ftfuzz_search(String ft_string, int d)
	{
		String ft_str = fetch_ft_string(ft_string);
		String fuzz_str = fetch_fuzzy_string(ft_string, "title", d);
		String query = "SELECT M.id, M.title, M.year, M.director, list_of_genres, list_of_stars\r\n" + 
				"FROM\r\n" + 
				"((\r\n" + 
				"SELECT id\r\n" + 
				"FROM movies\r\n" + 
				"WHERE " + fuzz_str + "\r\n" + 
				") UNION\r\n" + 
				"(\r\n" + 
				"SELECT entryId as id\r\n" + 
				"FROM ft_movies\r\n" + 
				"WHERE MATCH (entry) AGAINST (\"" + ft_str + "\")\r\n" + 
				")) MM,\r\n" + 
				"(\r\n" + 
				"SELECT id, title,year,director\r\n" + 
				"FROM movies\r\n" + 
				"GROUP BY id\r\n" + 
				") M,\r\n" + 
				"(\r\n" + 
				"SELECT movies.id, GROUP_CONCAT(genres.name SEPARATOR ', ') as list_of_genres\r\n" + 
				"FROM movies, genres_in_movies as gim, genres\r\n" + 
				"WHERE gim.movieId = movies.id AND genres.id = gim.genreId\r\n" + 
				"GROUP BY movies.id\r\n" + 
				") G, \r\n" + 
				"(\r\n" + 
				"SELECT movies.id, GROUP_CONCAT(stars.name SEPARATOR ', ') as list_of_stars\r\n" + 
				"FROM movies, stars_in_movies as sim, stars\r\n" + 
				"WHERE sim.movieId = movies.id AND stars.id = sim.starId\r\n" + 
				"GROUP BY movies.id\r\n" + 
				") S\r\n" + 
				"WHERE MM.id = M.id AND MM.id = G.id AND MM.id = S.id\r\n" + 
				"GROUP BY M.id";
		
		return query;
	}
	
	public static String get_arrayList_for_ft_search(String ft_string)
	{
		String ft_str = fetch_ft_string(ft_string);
		String query = "SELECT M.id, M.title, M.year, M.director, list_of_genres, list_of_stars\r\n" + 
				"FROM\r\n" + 
				"(\r\n" + 
				"SELECT entryId as id\r\n" + 
				"FROM ft_movies\r\n" + 
				"WHERE MATCH (entry) AGAINST (\"" + ft_str + "\")\r\n" + 
				") MM,\r\n" + 
				"(\r\n" + 
				"SELECT id, title,year,director\r\n" + 
				"FROM movies\r\n" + 
				"GROUP BY id\r\n" + 
				") M,\r\n" + 
				"(\r\n" + 
				"SELECT movies.id, GROUP_CONCAT(genres.name SEPARATOR ', ') as list_of_genres\r\n" + 
				"FROM movies, genres_in_movies as gim, genres\r\n" + 
				"WHERE gim.movieId = movies.id AND genres.id = gim.genreId\r\n" + 
				"GROUP BY movies.id\r\n" + 
				") G, \r\n" + 
				"(\r\n" + 
				"SELECT movies.id, GROUP_CONCAT(stars.name SEPARATOR ', ') as list_of_stars\r\n" + 
				"FROM movies, stars_in_movies as sim, stars\r\n" + 
				"WHERE sim.movieId = movies.id AND stars.id = sim.starId\r\n" + 
				"GROUP BY movies.id\r\n" + 
				") S\r\n" + 
				"WHERE MM.id = M.id AND MM.id = G.id AND MM.id = S.id\r\n" + 
				"GROUP BY M.id";
		
		return query;
	}
	
	public static String get_arrayList_for_fuzzy_search(String ft_string, int d)
	{
		String fuzz_str = fetch_fuzzy_string(ft_string, "title", d);
		String query = "SELECT M.id, M.title, M.year, M.director, list_of_genres, list_of_stars\r\n" + 
				"FROM\r\n" + 
				"(\r\n" + 
				"SELECT id\r\n" + 
				"FROM movies\r\n" + 
				"WHERE " + fuzz_str + "\r\n" + 
				") MM,\r\n" + 
				"(\r\n" + 
				"SELECT id, title,year,director\r\n" + 
				"FROM movies\r\n" + 
				"GROUP BY id\r\n" + 
				") M,\r\n" + 
				"(\r\n" + 
				"SELECT movies.id, GROUP_CONCAT(genres.name SEPARATOR ', ') as list_of_genres\r\n" + 
				"FROM movies, genres_in_movies as gim, genres\r\n" + 
				"WHERE gim.movieId = movies.id AND genres.id = gim.genreId\r\n" + 
				"GROUP BY movies.id\r\n" + 
				") G, \r\n" + 
				"(\r\n" + 
				"SELECT movies.id, GROUP_CONCAT(stars.name SEPARATOR ', ') as list_of_stars\r\n" + 
				"FROM movies, stars_in_movies as sim, stars\r\n" + 
				"WHERE sim.movieId = movies.id AND stars.id = sim.starId\r\n" + 
				"GROUP BY movies.id\r\n" + 
				") S\r\n" + 
				"WHERE MM.id = M.id AND MM.id = G.id AND MM.id = S.id\r\n" + 
				"GROUP BY M.id";
		
		return query;
	}
}

package pages;


import java.io.IOException;
import java.io.OutputStream;
import java.sql.*;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.Movie;
/**
 * Servlet implementation class Hello
 */
@WebServlet("/Top")
public class Top extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
	public ResultSet getTop20() throws Exception
    {

            // Incorporate mySQL driver
            Class.forName("com.mysql.jdbc.Driver").newInstance();

             // Connect to the test database
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/moviedb?autoReconnect=true&useSSL=false","mytestuser", "mypassword");

            // Create an execute an SQL statement to select all of table"Stars" records
            Statement select = connection.createStatement();
            String statement= "SELECT T.id, T.title, T.year, director, list_of_genres, list_of_stars, AVG(rating) as rating\r\n" + 
            		"FROM\r\n" + 
            		"(\r\n" + 
            		"SELECT id,title, year, director, ROUND(AVG(rating),3) as rating\r\n" + 
            		"FROM movies, ratings\r\n" + 
            		"WHERE ratings.movieId = movies.id\r\n" + 
            		"GROUP BY movies.id\r\n" + 
            		"ORDER BY rating DESC\r\n" + 
            		"LIMIT 20\r\n" + 
            		") T,\r\n" + 
            		"(\r\n" + 
            		"SELECT title, GROUP_CONCAT(stars.name SEPARATOR ', ') as list_of_stars\r\n" + 
            		"FROM movies, stars_in_movies as sim, stars\r\n" + 
            		"WHERE sim.movieId = movies.id AND stars.id = sim.starId\r\n" + 
            		"GROUP BY movies.id\r\n" + 
            		") S,\r\n" + 
            		"(\r\n" + 
            		"SELECT title, GROUP_CONCAT(genres.name SEPARATOR ', ') as list_of_genres\r\n" + 
            		"FROM movies, genres_in_movies as gim, genres\r\n" + 
            		"WHERE gim.movieId = movies.id AND genres.id = gim.genreId\r\n" + 
            		"GROUP BY movies.id\r\n" + 
            		") G\r\n" + 
            		"WHERE T.title = S.title AND T.title = G.title\r\n" + 
            		"GROUP BY T.title, T.year, T.director, G.list_of_genres, S.list_of_stars\r\n" + 
            		"ORDER BY rating DESC;";
            ResultSet result = select.executeQuery(statement);
           
            return result;
            
            // Get metatdata from stars; print # of attributes in table
            /*System.out.println("The results of the query");
            ResultSetMetaData metadata = result.getMetaData();
            System.out.println("There are " + metadata.getColumnCount() + " columns");

            // Print type of each attribute
          

            // print table's contents, field by field
            while (result.next())
            {
                    
                    System.out.println("title = " + result.getString(1));
                    System.out.println("year = " + result.getInt(2));
                    System.out.println("direcetor = " + result.getString(3));
                    System.out.println("rating = "+result.getFloat(4));
					   System.out.println();
            }*/
    }
	
	public ArrayList<Movie> movieArrayList()
	{
		Movie m = null;
		ArrayList<Movie> movieList = new ArrayList<Movie>();
		try
		{
		ResultSet r = getTop20();
		while(r.next())
		{
			String movieId = r.getString(1);
			String title = r.getString(2);
			int year = r.getInt(3);
			String director = r.getString(4);
			String genres = r.getString(5);
			String stars = r.getString(6);
			double rating = r.getDouble(7);
			
			m = new Movie(title,year,director,rating,genres,stars,movieId);
			movieList.add(m);
		}
		}catch(Exception e){
			System.out.println(e);
		}
		
		try {
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return movieList;
	}
    public Top() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		/*String test = "[{\"name\":\"Quan Ho\",\"age\":23},{\"name\":\"dog\"}]";
		response.setContentType("application/json");
		response.getWriter().write(test);*/
		//ArrayList<Integer> result = new ArrayList<Integer>();
		/*try {
			result = getTop20();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
		ArrayList<Movie> resultList = movieArrayList();
		request.setAttribute("resultSet", resultList);
		RequestDispatcher requestDispatcher;
		requestDispatcher = request.getRequestDispatcher("/top.jsp");
		requestDispatcher.forward(request,response);
		
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}

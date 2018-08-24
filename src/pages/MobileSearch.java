package pages;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Servlet implementation class MobileSearch
 */
@WebServlet("/MobileSearch")
public class MobileSearch extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private static final int ITEMS_PER_PAGE = 10;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MobileSearch() {
        super();
        // TODO Auto-generated constructor stub
    }

    //search helper
    private ArrayList<String> searchFor(String searchItem, String queryNorm) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException
    {
    	   // Incorporate mySQL driver
        Class.forName("com.mysql.jdbc.Driver").newInstance();

         // Connect to the test database
        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/moviedb?autoReconnect=true&useSSL=false","root", "112968Met@");
        boolean valid=false;
        // Create an execute an SQL statement to select all of table"Stars" records
        Statement select = connection.createStatement();
        String statement = String.format("SELECT matched_search.id,matched_search.title,matched_search.year,matched_search.director,GROUP_CONCAT(DISTINCT IFNULL(full_genres.name,'N/A')) as genres,GROUP_CONCAT(DISTINCT IFNULL(full_stars.name,'N/A')) as stars from  (SELECT * from movies WHERE MATCH(title) AGAINST('%s') UNION SELECT * from movies WHERE levenshtein(title,'%s')<4) as matched_search LEFT JOIN (SELECT * from genres,genres_in_movies WHERE genres_in_movies.genreId = genres.id) as full_genres ON full_genres.movieId = matched_search.id LEFT JOIN (SELECT * from stars,stars_in_movies WHERE stars.id = stars_in_movies.starId) as full_stars ON full_stars.movieId = matched_search.id GROUP BY matched_search.id;", searchItem,queryNorm);
        System.out.println("Executing "+statement);
        ResultSet result = select.executeQuery(statement);
        int retrievedId=-1;
        ArrayList<String> resultList=new ArrayList<>();
        while(result.next())
        {
        	
        	String id = result.getString(1);
        	String title =result.getString(2);
        	String year = result.getString(3);
        	String director = result.getString(4);
        	String genres = result.getString(5);
        	String stars = result.getString(6);
        	String movieString = String.format("id:%s\nTitle: %s\nYear:%s\nDirector:%s\nStars:%s\nGenres:%s", id,title,year,director,stars,genres);
        	resultList.add(movieString);
        	
        }
        connection.close();
       return resultList;
    }
    //search helper
    private ArrayList<String> searchForPaged(String searchItem,String page,String queryNorm) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException
    {
    	   // Incorporate mySQL driver
        Class.forName("com.mysql.jdbc.Driver").newInstance();

         // Connect to the test database
        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/moviedb?autoReconnect=true&useSSL=false","root", "112968Met@");
        boolean valid=false;
	int fuck = new Integer(page).intValue()-1;
        // Create an execute an SQL statement to select all of table"Stars" records
         String statement = String.format("SELECT matched_search.id,matched_search.title,matched_search.year,matched_search.director,GROUP_CONCAT(DISTINCT IFNULL(full_genres.name,'N/A')) as genres,GROUP_CONCAT(DISTINCT IFNULL(full_stars.name,'N/A')) as stars from  (SELECT * from movies WHERE MATCH(title) AGAINST('%s') UNION SELECT * from movies WHERE levenshtein(title,'%s')<4) as matched_search LEFT JOIN (SELECT * from genres,genres_in_movies WHERE genres_in_movies.genreId = genres.id) as full_genres ON full_genres.movieId = matched_search.id LEFT JOIN (SELECT * from stars,stars_in_movies WHERE stars.id = stars_in_movies.starId) as full_stars ON full_stars.movieId = matched_search.id GROUP BY matched_search.id LIMIT %d,%d", searchItem,queryNorm,fuck,ITEMS_PER_PAGE);
 
	Statement select = connection.createStatement();
        
	System.out.println("Executing "+statement);
        ResultSet result = select.executeQuery(statement);
        int retrievedId=-1;
        ArrayList<String> resultList=new ArrayList<>();
        while(result.next())
        {
        	
        	String id = result.getString(1);
        	String title =result.getString(2);
        	String year = result.getString(3);
        	String director = result.getString(4);
		String genres = result.getString(5);
        	String stars = result.getString(6);

        	String movieString = String.format("id:%s\nTitle: %s\nYear:%s\nDirector:%s\nStars:%s\nGenres:%s", id,title,year,director,stars,genres);
		resultList.add(movieString);
        	
        }
        connection.close();
       return resultList;
    }
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String searchQuery = request.getParameter("searchQuery");
		String page = request.getParameter("p");
		String queryNorm = request.getParameter("searchQueryNorm");
		System.out.println("Searched for "+searchQuery);
		Gson gsonBuilder = new GsonBuilder().create();
		ArrayList<String>result = null;
		
		try {
			if(page==null)
				result = searchFor(searchQuery,queryNorm);
			else
				result = searchForPaged(searchQuery,page,queryNorm);
			
			System.out.println("The size is "+result.size());
			String jsonResult = gsonBuilder.toJson((List<String>)result);
			System.out.println("Result json: "+jsonResult);
			response.setContentType("application/json");
			response.getWriter().write(jsonResult);
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}

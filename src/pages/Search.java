package pages;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.SearchQuery;

/**
 * Servlet implementation class Search
 */
@WebServlet("/Search")
public class Search extends HttpServlet 
{
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		int display_count = Integer.parseInt(request.getParameter("display_count"));
		try {
				SearchQuery search_query = new SearchQuery();
				String query = search_query.build_query_from_request(request);
				
				request.getSession().setAttribute("search_query", query);
				
				RequestDispatcher view = request.getRequestDispatcher("/search_result.jsp?att_order=title&order=ASC&current_page=1&ddisplay_count=" + Integer.toString(display_count));
				view.forward(request, response);
			
		} catch (Exception e) {
			  e.printStackTrace();
		}
		
	}
}

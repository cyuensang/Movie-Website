package pages;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mysql.cj.core.util.StringUtils;

import model.DashboardProc;

/**
 * Servlet implementation class Dashboard_result
 */
@WebServlet("/Dashboard_result")
public class Dashboard_result extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		String table_name = request.getParameter("Table");
		String update_name = request.getParameter("Update");
		String movie_id = request.getParameter("movie_id");
		String movie_title = request.getParameter("movie_title");
		String movie_year = request.getParameter("movie_year");
		String movie_director = request.getParameter("movie_director");
		String star_name = request.getParameter("star_name");
		String star_birthdate = request.getParameter("star_birthdate");
		String genre_name = request.getParameter("genre_name");
		
		ArrayList<String[]> logs = null;
		String hold_logs = (String) request.getSession().getAttribute("log");
		
		try {
				if(table_name == null)
					table_name = (String) request.getSession().getAttribute("view");
				request.getSession().setAttribute("view", table_name);
				
				if(update_name == null)
					update_name = (String) request.getSession().getAttribute("update_view");
				request.getSession().setAttribute("update_view", update_name);
				
				if(movie_id != null || movie_title != null || movie_year != null || 
						movie_director != null || star_name != null || star_birthdate != null || genre_name != null)
				{
					if(update_name.equals("update_movie") && (movie_id == null || movie_id.length() <= 0))
					{
						hold_logs += "Update movie error: movie id missing., ";
						request.getSession().setAttribute("log", hold_logs);
					}
					else if(update_name.equals("add_star") && (star_name == null || star_name.length() <= 0))
					{
						hold_logs += "Add star error: star name missing., ";
						request.getSession().setAttribute("log", hold_logs);
					}
					else if(update_name.equals("add_genre") && (genre_name == null || genre_name.length() <= 0))
					{
						hold_logs += "Add genre error: genre name missing., ";
						request.getSession().setAttribute("log", hold_logs);
					}
					else if((update_name.equals("update_movie") || update_name.equals("add_movie")) && movie_year != null && movie_year.length() > 0 && !StringUtils.isStrictlyNumeric(movie_year))
					{
						hold_logs += "General error: movie year is not numeric., ";
						request.getSession().setAttribute("log", hold_logs);
					}
					else if(update_name.equals("add_star") && star_birthdate != null && star_birthdate.length() > 0 && !StringUtils.isStrictlyNumeric(star_birthdate))
					{
						hold_logs += "General error: star birthdate is not numeric., ";
						request.getSession().setAttribute("log", hold_logs);	
					}
					else
					{
						logs = DashboardProc.get_arrayList_from_add_movie(movie_id, movie_title, movie_year, movie_director, 
								star_name, star_birthdate, genre_name);
						
						hold_logs += logs.get(0)[0] + ", ";
						request.getSession().setAttribute("log", hold_logs);
					}
				}
				
				RequestDispatcher view = request.getRequestDispatcher("/_dashboard.jsp?table_to_display=" + table_name + "&update_to_display="+ update_name);
				view.forward(request, response);
			
		} catch (Exception e) {
			  e.printStackTrace();
		}
		
	}

}

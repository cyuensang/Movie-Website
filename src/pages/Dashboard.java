package pages;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.Cart;
import model.VerifyUtils;

/**
 * Servlet implementation class Dashboard
 */
@WebServlet("/Dashboard")
public class Dashboard extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Dashboard() {
        super();
        // TODO Auto-generated constructor stub
    }

    private String isValidEmployee(String emp_username, String emp_password) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException
    {
    	String fullname = "";
    	
    	// Incorporate mySQL driver
        Class.forName("com.mysql.jdbc.Driver").newInstance();
        // Connect to the test database
        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/moviedb?autoReconnect=true&useSSL=false","root", "112968Met@");
        // Create an execute an SQL statement to select all of table "Stars" records
        Statement select = connection.createStatement();
        
        String statement = 	"SELECT fullname\r\n" + 
        					"FROM employees\r\n" + 
        					"WHERE email = \""+ emp_username + "\" AND password = \"" + emp_password + "\";";
        
        ResultSet result = select.executeQuery(statement);
        
        if(result.next())
        	fullname += result.getString(1);
       
        return fullname;
    }
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if(request.getSession().getAttribute("Emp_logged_in") != null)
		{
			System.out.println("Employee Logout");
			request.getSession().removeAttribute("log");
			request.getSession().removeAttribute("view");
			request.getSession().removeAttribute("update_view");
			request.getSession().removeAttribute("Emp_logged_in");
			request.getSession().removeAttribute("Emp_fullname");
			response.sendRedirect("_dashboard.jsp");
		}
		else
		{
			response.sendRedirect("_dashboard.jsp");
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
				String emp_username = request.getParameter("Emp_username");
				String emp_password = request.getParameter("Emp_password");
				String fullname = "";
				
				try {
						// Recaptcha
						/*
						String gRecaptchaResponse = request.getParameter("g-recaptcha-response");
						System.out.println("gRecaptchaResponse=" + gRecaptchaResponse);
						// Verify CAPTCHA.
						boolean valid = VerifyUtils.verify(gRecaptchaResponse);
						if (!valid)
						{
							RequestDispatcher view = request.getRequestDispatcher("/login.jsp");
							view.forward(request, response);
						}
						*/
						fullname = isValidEmployee(emp_username, emp_password);
						if(fullname.length() > 0)
						{
							request.getSession().setAttribute("Emp_fullname", fullname);
							request.getSession().setAttribute("log", "");
							request.getSession().setAttribute("view", "creditcards");
							request.getSession().setAttribute("update_view", "add_movie");
							request.getSession().setAttribute("Emp_logged_in", true);
							response.getWriter().write("{\"username\":"+"\"" + fullname + "\""+",\"success\":\"true\"}");
						}
						else
						{
							response.getWriter().write("{\"success\":\"false\"}");
						}
						
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

}

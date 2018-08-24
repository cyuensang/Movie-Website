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
 * Servlet implementation class Login
 */
@WebServlet("/Login")
public class Login extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Login() {
        super();
        // TODO Auto-generated constructor stub
    }
    private int isValidLogin(String username, String password) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException
    {
    	   // Incorporate mySQL driver
        Class.forName("com.mysql.jdbc.Driver").newInstance();

         // Connect to the test database
        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/moviedb?autoReconnect=true&useSSL=false","root", "112968Met@");
        boolean valid=false;
        // Create an execute an SQL statement to select all of table"Stars" records
        Statement select = connection.createStatement();
        String statement = "SELECT email,password,id from customers WHERE email"+"=\""+username+"\";";
        ResultSet result = select.executeQuery(statement);
        int retrievedId=-1;
        if(result.next())
        {
        	
        	String retrievedUsername = result.getString(1);
        	String retrievedPassword =result.getString(2);
        	retrievedId = result.getInt(3);
        	valid=retrievedPassword.equals(password);
        }
        connection.close();
        if(valid)
        {
        	return retrievedId;
        	
        }
        
    	return -1;
    }
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if(request.getSession().getAttribute("logged_in")!=null)
		{
			System.out.println("Checking here!");
			request.getSession().removeAttribute("logged_in");
			request.getSession().removeAttribute("username");
			request.getSession().removeAttribute("cart");
			response.sendRedirect(request.getHeader("referer"));
		}
		else
		{
			response.sendRedirect("login.jsp");
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
				String username = request.getParameter("username");
				String password = request.getParameter("password");
				int id;
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
					if((id=isValidLogin(username,password))!=-1)
					{
						request.getSession().setAttribute("username", username);
						request.getSession().setAttribute("logged_in", true);
						request.getSession().setAttribute("customer_id", id);
						response.getWriter().write("{\"username\":"+"\""+username+"\""+",\"success\":\"true\"}");
						if(request.getSession().getAttribute("cart")==null)
								request.getSession().setAttribute("cart", new Cart());
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

package pages;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.Cart;
import model.CartItem;

/**
 * Servlet implementation class Checkout
 */
@WebServlet("/Checkout")
public class Checkout extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Checkout() {
        super();
        // TODO Auto-generated constructor stub
    }
    private boolean isValidInfo(String fname, String lname,String cc_no,String exp_mo,String exp_day,String exp_year) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException
    {
    	   // Incorporate mySQL driver
        Class.forName("com.mysql.jdbc.Driver").newInstance();

         // Connect to the test database
        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/moviedb?autoReconnect=true&useSSL=false","root", "112968Met@");
        boolean isValid=false;
        // Create an execute an SQL statement to select all of table"Stars" records
        Statement select = connection.createStatement();
        String statement = "SELECT * from customers,creditcards where ccId=\""+cc_no+"\" and creditcards.id = customers.ccId and customers.firstName =\""+fname+"\"and customers.lastName =\""+lname+"\" and expiration ='"+exp_year+"-"+exp_mo+"-"+exp_day+"';";
        ResultSet result = select.executeQuery(statement);
        isValid=result.next();
       
        connection.close();
    	return isValid;
    }
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}
	private void buyShit(String customerId,Cart myCart) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException
	{
		HashMap<String,CartItem> cartList = myCart.getItemsMap();
		Class.forName("com.mysql.jdbc.Driver").newInstance();
		Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/moviedb?autoReconnect=true&useSSL=false","root", "112968Met@");
		Statement insert = connection.createStatement();
		String statement;
		for(String id: cartList.keySet())
		{
			for(int i=0;i<cartList.get(id).getQty();i++)
			{
				statement = "INSERT INTO sales(customerId,movieId,saleDate) VALUES("+customerId+",'"+id+"',DATE(NOW()));";
				insert.executeUpdate(statement);
			}
			
			
		}
		
		connection.close();
	}
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String fname = request.getParameter("fname");
		String lname = request.getParameter("lname");
		String cc_number = request.getParameter("cc_number");
		String cc_day = request.getParameter("cc_day");
		String cc_month = request.getParameter("cc_month");
		String cc_year = request.getParameter("cc_year");
		String customerId = (new Integer((int) request.getSession(false).getAttribute("customer_id")).toString());
		try {
			if(request.getSession().getAttribute("username")==null)
			{
				response.sendRedirect("/Fabflix/login.jsp");
			}
			if(isValidInfo(fname, lname,cc_number,cc_month,cc_day,cc_year))
			{
				buyShit(customerId,(Cart)request.getSession(false).getAttribute("cart"));
				response.getWriter().write("{\"success\":\"true\"}");
			}
			else
			{
				response.getWriter().write("{\"success\":\"false\"}");
			}
			System.out.println("Received info First Name:"+fname+" Last Name:"+lname+" Credit Card Number:"+cc_number+" Exp Month:"+cc_month+" Exp Year:"+cc_year+" Exp day:"+cc_day);
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}

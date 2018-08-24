package pages;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.Movie;
import model.Cart;
import model.CartItem;
/**
 * Servlet implementation class ShoppingCart
 */
@WebServlet("/ShoppingCart")
public class ShoppingCart extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ShoppingCart() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.sendRedirect("cart.jsp");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	
	private void addToCart(String title, String movieId,Cart myCart)
	{
		CartItem i = new CartItem(title,movieId);
		
		if(myCart.doesItemExist(i))
		{
			myCart.increaseByOne(i.getMovieId());
		}
		else
		{
			myCart.addNewItem(i);
		}
	}
	private void removeFromCart(String movieId,Cart myCart)
	{
			
		
			System.out.println("Removing "+movieId);
			myCart.removeItem(movieId);
		
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String movieId = request.getParameter("movieId");
		String title = request.getParameter("title");
		String whatDo = request.getParameter("do");
		String qty = request.getParameter("qty");
		Cart myCart = (Cart) request.getSession().getAttribute("cart");
		if(myCart==null)
		{
			myCart = new Cart();
			request.getSession().setAttribute("cart",myCart);
		}
		if(whatDo.equals("add") && myCart!=null)
			addToCart(title,movieId,myCart);
		else if(whatDo.equals("remove"))
		{
			removeFromCart(movieId,myCart);
		}
		else if(whatDo.equals("up"))
			myCart.justUpdate(movieId, new Integer(qty).intValue());
		response.setStatus(200);
		response.getWriter().write("{\"success\":\"true\"}");
	}

}

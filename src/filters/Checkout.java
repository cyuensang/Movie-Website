package filters;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.Cart;
import model.CartItem;

/**
 * Servlet Filter implementation class Checkout
 */
@WebFilter("/user_info.jsp")
public class Checkout implements Filter {

    /**
     * Default constructor. 
     */
    public Checkout() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
		// TODO Auto-generated method stub
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		// TODO Auto-generated method stub
		// place your code here

		// pass the request along the filter chain
		System.out.println("Here's the shit!");
		System.out.println("Filtering!");
		HttpServletRequest httpRequest = ((HttpServletRequest) request);
		String user = (String) ((HttpServletRequest) request).getSession().getAttribute("username");
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		Cart myCart = (Cart) ((HttpServletRequest) request).getSession().getAttribute("cart");
		if(user!=null && myCart!=null && myCart.getItemsMap().size()!=0)
		{
			System.out.println("Fuck this!"+"username: "+user	);
			RequestDispatcher r = httpRequest.getRequestDispatcher("user_info.jsp");
			r.forward(request, response);
		}
		else if(user==null)
		{
			httpResponse.sendRedirect("login.jsp");
		}
		else
		{
			RequestDispatcher r = httpRequest.getRequestDispatcher("index.jsp");
			r.forward(request, response);
		}
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		// TODO Auto-generated method stub
	}

}

<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link rel="stylesheet" type="text/css" href="css/style.css">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<%@page import="java.util.ArrayList" %>
<%@page import="model.Movie" %>
<%@page import="model.Cart" %>
<%@page import="model.CartItem" %>
<title>Fabflix</title>
</head>
<body>
<div class="logo">
		Fabflix
		<ul>
		<li><a href="index.jsp">Home</a></li>
		<li><a href="search.jsp">Advanced Search</a></li>
		<li><a href="login.jsp">
			<%  
			if(request.getSession().getAttribute("logged_in")!=null)
				out.print("Sign out");
			else
				out.print("Sign in");
		%>
		</a></li>
		<li><a href="cart.jsp">My Cart</a></li>
	</ul>
		<div class="search-bar">
			<input type="text" name="n_search" id="autocomplete"/>
			<button  type="button" onclick="clickNormalSearch()">Search</button>
		</div>
</div>
<div class="menu">
</div>


<div class="section">
<table id="shopping-cart">
	<tr>
		<th><h1>Shopping Cart</h1></th>
	</tr>
	
	<%
		Cart current = (Cart)request.getSession().getAttribute("cart");
		if(current!=null)
			for(CartItem i:current.getItemsMap().values())
			{
				out.println(i.toHtmlRow());
			}
		out.println("<tr><td>");
		
		
		if(current!=null && current.getItemsMap().size()!=0)
		{
			out.println("<button id=\"checkout-btn\" button onclick=transfer_to_checkout()>");
			out.println("Checkout");
			out.println("</button>");
		}
		else
		{
			out.println("No items!");
		}
		out.println("</td></tr>");
		%>
	
	
	
	
		
	</tr>
</table>
</div>

	<!-- Using jQuery -->
    <script src="js/jquery-3.3.1.js"></script>
    <!-- include jquery autocomplete JS  -->
    <script src="js/jquery.autocomplete.min.js"></script>
    <!-- website JS  -->
	<script src="js/normal_search.js"></script>
	<script src="js/add_cart.js"></script>

</body>
</html>
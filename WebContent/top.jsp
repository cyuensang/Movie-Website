<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link rel="stylesheet" type="text/css" href="css/style.css">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<%@page import="java.util.ArrayList" %>
<%@page import="model.Movie" %>

<title>Fabflix</title>
</head>
<body>
<div class="logo">
	Fabflix
	<ul>
		<li><a href="index.jsp">Home</a></li>
		<li><a href="search.jsp">Advanced Search</a></li>
		<li><a href="../Fabflix/Login">
			<%  
			if(request.getSession().getAttribute("logged_in")!=null)
				out.print("Sign out");
			else
				out.print("Sign in");
		%>
		</a></li>
		<li><a href="../Fabflix/cart.jsp">My Cart</a></li>
	</ul>
		<div class="search-bar">
			<input type="text" name="n_search" id="autocomplete"/>
			<button  type="button" onclick="clickNormalSearch()">Search</button>
		</div>
</div>
<div class="menu">
	
</div>


<div class="section">
<ul>
<li><h1 style="color:black">Top 20 movies</h1><li>
<%
	ArrayList<Movie> result = (ArrayList<Movie>)request.getAttribute("resultSet");
	
	for(int i=0;i<result.size();i++)
	{	
		Movie m = result.get(i);
		out.print("<div style=\"color:white;\">Top "+(i+1)+"</div>");
		out.println("<li class=\"movied\">");
		out.println(m);
		out.println("<br>");
		out.println("<button type=\"button\" onclick=\"addToCart('"+m.getMovieId()+"','"+m.getTitle()+"')\">Add to Cart</button>");
		out.println("</li><br>");
	}
	
%>
</ul>
</div>
<script src="jquery-3.3.1.js"></script>
<script src="js/add_cart.js"></script>

</body>
</html>
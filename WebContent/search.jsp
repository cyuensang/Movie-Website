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
		<ul>
			<li><h1 style="color:black">Search</h1><li>
			<b>(use ';' for multiple entries)</b>
			
			<form method="post" name="frm" action="Search">
				<table border="0" width="300" align="center" bgcolor="#343434">
					<tr><td colspan=2 style="font-size:12pt;color:#00000;" align="center">
					
					<tr><td><b>Title: </b></td>
					<td><input  type="text" name="movie_title" id="movie_title"></td></tr>
					<tr><td><b>Year: </b></td>
					<td><input  type="text" name="movie_year" id="movie_year"></td></tr>
					<tr><td><b>Director: </b></td>
					<td><input  type="text" name="movie_director" id="movie_director"></td></tr>
					<tr><td><b>Star's name: </b></td>
					<td><input  type="text" name="movie_star" id="movie_star"></td></tr>
					
					<tr><td colspan=1><b>Display: </b></td>
						<td><select name="display_count">
							<option value="10">10 per page</option>
							<option value="25">25 per page</option>
							<option value="50">50 per page</option>
							<option value="100">100 per page</option>
						</select>
					</td></tr>
					
					<tr><td colspan=2 align="center">
					<input  type="submit" name="submit" value="Search"></td></tr>
					
				</table>
			</form>
		</ul>
	</div>
	
	<!-- Using jQuery -->
    <script src="js/jquery-3.3.1.js"></script>
    <!-- include jquery autocomplete JS  -->
    <script src="js/jquery.autocomplete.min.js"></script>
    <!-- website JS  -->
	<script src="js/normal_search.js"></script>
	
</body>
</html>
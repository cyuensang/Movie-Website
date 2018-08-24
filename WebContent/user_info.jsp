<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link rel="stylesheet" type="text/css" href="css/style.css">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<%@page import="java.util.ArrayList" %>
<%@page import="model.Movie" %>
<%@page session="true" %>
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

	




<div class="login-section">
	<div id="inner-login">
	<h1>Check out</h1>
	<br>
	<form action="#" method="post" id="info_form">
		<label>First Name</label>
		<br>
		<input type="text" placeholder="John" name="fname">
		<br>
		<label>Last Name</label>
		<br>
		<input type="text" placeholder="Doe" name="lname">
		<br>
		<label>Credit Card Number</label>
		<br>
		<input type="text" placeholder="12345679" name="cc_number">
		<br>
		<label>Expiration month</label>
		<br>
		<input type="text" placeholder="05" name="cc_month">
		<br>
		<label>Expiration day</label>
		<br>
		<input type="text" placeholder="29" name="cc_day">
		<br>
		<label>Expiration year</label>
		<br>
		<input type="text" placeholder="29" name="cc_year">
		<br>
		
		<input type="submit" value="checkout">
	</form>
	<div id="info-error">
	</div>
	</div>
</div>

	<!-- Using jQuery -->
    <script src="js/jquery-3.3.1.js"></script>
    <!-- include jquery autocomplete JS  -->
    <script src="js/jquery.autocomplete.min.js"></script>
    <!-- website JS  -->
	<script src="js/normal_search.js"></script>
	<script src="js/checkout.js"></script>

</body>
</html>
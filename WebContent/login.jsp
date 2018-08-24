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

<script src='https://www.google.com/recaptcha/api.js'></script>

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
	<h1>Login</h1>
	<br>
	<form action="#" method="post" id="login_form">
		<input type="text" placeholder="Username" name="username">
		<br>
		<input type="password" placeholder="password" name="password">
		<br>
		<input type="submit" value="login">
		<!-- Recaptcha --><!--
		<div class="g-recaptcha" data-sitekey="6LfBGEgUAAAAAABt66fizd2bI8eCW7TNJkpH2VGg"></div>
		-->
	</form>
	<div id="login-error">
	</div>
	</div>
</div>

	<!-- Using jQuery -->
    <script src="js/jquery-3.3.1.js"></script>
    <!-- include jquery autocomplete JS  -->
    <script src="js/jquery.autocomplete.min.js"></script>
    <!-- website JS  -->
	<script src="js/normal_search.js"></script>
	<script src="js/login.js"></script>

</body>
</html>
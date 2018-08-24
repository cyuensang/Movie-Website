<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link rel="stylesheet" type="text/css" href="css/style.css">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<%@page import="java.util.*" %>
<%@page import="model.Movie" %>
<%@ page import="model.SearchQuery" %>
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

	<%
		// Not logged
		if(request.getSession().getAttribute("Emp_logged_in")==null)
		{
	%>

		<div class="login-section">
			<div id="inner-login">
			<h1>Employee Login</h1>
			<br>
			<form action="#" method="post" id="emp_login_form">
				<input type="text" placeholder="email" name="Emp_username">
				<br>
				<input type="password" placeholder="password" name="Emp_password">
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

	<%
		}
		// Employee logged in
		else
		{
			String table_to_display = request.getParameter("table_to_display");
			if(table_to_display == null)
				table_to_display = "creditcards";
			
			String update_to_display = request.getParameter("update_to_display");
			if(update_to_display == null)
				update_to_display = "add_movie";
			
			ArrayList<String[]> table_content = SearchQuery.get_arrayList_of_table_name(table_to_display);
			ArrayList<String[]> tables = SearchQuery.get_arrayList_of_tables();
			String logs = (String) request.getSession().getAttribute("log");
	%>
	
		<div class="section">
			<ul>
			
			<li><h1 align="center" style="color:black">Dash board&nbsp;&nbsp;<a href="../Fabflix/Dashboard">
					<b style="font-size:12pt;">sign out</b></a>
			</h1></li>
			
			<table border="0" width="600" align="left" bgcolor="#343434">
				<tr>
					<td valign="top" style="border: 2px solid #000000;"><form method="post" name="frm" action="Dashboard_result">
					<table border="0" width="280" align="center" bgcolor="#343434">
						<tr><td colspan=2 style="font-size:12pt;color:#00000;" align="center">
							<b>View table: </b></td>
							<td colspan=2><select name="Table">
										<option selected=<%=table_to_display%>><%=table_to_display%></option>
								<%
									for(int i = 0; i < tables.size(); i++)
									{
										String table_name = tables.get(i)[0];
										if(!table_name.equals(table_to_display))
										{
								%>
											<option value=<%=table_name%>><%=table_name%></option>
								<%
										}
									}
								%>
							</select></td>
							<td><input  type="submit" name="submit" value="View"></td>
						</tr>
							
						<tr><td colspan=5>
							<table width="250" align="center" style="border: 1px solid #000000;">
								<tr>
									<td colspan=5 align="center" style="background-color: teal"><b><%=table_to_display%></b></td>
								</tr>
						
								<tr style="background-color: lightgrey;">
									<td style="border: 1px solid #000000;" align="center"><b>Attribute</b></td>
									<td style="border: 1px solid #000000;" align="center"><b>Type</b></td>
								</tr>
						
								<%
									if(table_content.size() > 0)
									{
										for (int i = 0; i < table_content.size(); i++) 
										{
											String info[] = table_content.get(i);
											String[] hold_att = info[1].split(",");
											String[] hold_type = info[2].split(",");
											for(int j = 0; j < hold_att.length; j++)
											{
										%>
											<tr style="background-color: lightgrey;" align="center"
											style="border:2px solid #000000;">
											<td style="border: 1px solid #000000;">
											<%=hold_att[j]%></td>
											<td style="border: 1px solid #000000;">
											<%=hold_type[j]%></td></tr>
										<%
											}
										}
									}
									else if(table_content.size() == 0)
									{
								%>
								<tr><td>
										<%
										out.println("No result");
									}
										%>
								</td></tr>
							</table>
						</td></tr>
					</table>
					</form></td>
				
					<td valign="top">
					<table border="0" width="280" align="center" bgcolor="#343434">
						<tr>
							<form method="post" name="frm" action="Dashboard_result">
							<td align="left"><b>Choose: </b></td>
							<td align="center"><select name="Update">
										<option selected=<%=update_to_display%>><%=update_to_display%></option>
								<%
									String[] update_tables = {"add_movie", "add_star", "add_genre", "update_movie"}; 
									for(int i = 0; i < update_tables.length; i++)
									{
										if(!update_tables[i].equals(update_to_display))
										{
								%>
											<option value=<%=update_tables[i]%>><%=update_tables[i]%></option>
								<%
										}
									}
								%>
							</select></td>
							<td align="left"><input  type="submit" name="submit" value="submit"></td>
							</form>
						</tr>
						<tr><td colspan=2 align="center"><b>(* are required field)</b></td></tr>
						<form method="post" name="frm" action="Dashboard_result">
						
						<%
							if(update_to_display.equals("add_movie"))
							{
						%>
								<tr><td colspan=2 align="left"><b><u>Movie attributes:</u></b></td></tr>
								<tr><td><b>Title*: </b></td>
								<td><input  type="text" name="movie_title" id="movie_title"></td></tr>
								<tr><td><b>Year*: </b></td>
								<td><input  type="text" name="movie_year" id="movie_year"></td></tr>
								<tr><td><b>Director*: </b></td>
								<td><input  type="text" name="movie_director" id="movie_director"></td></tr>
						<%
							}
							else if(update_to_display.equals("add_star"))
							{
						%>
								<tr><td colspan=2 align="left"><br><b><u>Star attributes:</u></b></td></tr>
								<tr><td><b>Name*: </b></td>
								<td><input  type="text" name="star_name" id="star_name"></td></tr>
								<tr><td><b>Birthdate: </b></td>
								<td><input  type="text" name="star_birthdate" id="star_birthdate"></td></tr>
						<%
							}
							else if(update_to_display.equals("add_genre"))
							{
						%>
								<tr><td colspan=2 align="left"><br><b><u>Genre attributes:</u></b></td></tr>
								<tr><td><b>Name*: </b></td>
								<td><input  type="text" name="genre_name" id="genre_name"></td></tr>
						<%
							}
							else if(update_to_display.equals("update_movie"))
							{
						%>
								<tr><td colspan=2 align="left"><b><u>Movie attributes:</u></b></td></tr>
								<tr><td><b>Id*: </b></td>
								<td><input  type="text" name="movie_id" id="movie_id"></td></tr>
								<tr><td><b>Title: </b></td>
								<td><input  type="text" name="movie_title" id="movie_title"></td></tr>
								<tr><td><b>Year: </b></td>
								<td><input  type="text" name="movie_year" id="movie_year"></td></tr>
								<tr><td><b>Director: </b></td>
								<td><input  type="text" name="movie_director" id="movie_director"></td></tr>
								
								<tr><td colspan=2 align="left"><br><b><u>Star attributes:</u></b></td></tr>
								<tr><td><b>Name: </b></td>
								<td><input  type="text" name="star_name" id="star_name"></td></tr>
								
								<tr><td colspan=2 align="left"><br><b><u>Genre attributes:</u></b></td></tr>
								<tr><td><b>Name: </b></td>
								<td><input  type="text" name="genre_name" id="genre_name"></td></tr>
						<%
							}
						%>
						
						<tr><td colspan=3 align="center"><br>
						<input  type="submit" name="submit" value="Update"></td></tr>
						</form>
					</table>
					</td>
				</tr>
			</table>
			
			</ul>
		</div>
		
		<div class="section">
			<ul>
			<table border="0" width="600" align="left" bgcolor="#343434">
				<tr>
					<td colspan=5 align="center" style="background-color: teal"><b>logs</b></td>
				</tr>
			<%
				String[] hold_logs = logs.split(",");
				if(hold_logs != null)
				{
					for(int i = 0; i < hold_logs.length; i++)
					{
			%>
				<tr style="background-color: lightgrey;">
					<td colspan=5 style="border: 1px solid #000000;" align="left"><b><%=hold_logs[i]%></b></td>
				</tr>
			<%
					}
				}
			%>
			</table>
			</ul>
		</div>
	<%
		}
	%>

	<!-- Using jQuery -->
    <script src="js/jquery-3.3.1.js"></script>
    <!-- include jquery autocomplete JS  -->
    <script src="js/jquery.autocomplete.min.js"></script>
    <!-- website JS  -->
	<script src="js/normal_search.js"></script>
	<script src="js/dashboard.js"></script>
	
</body>
</html>
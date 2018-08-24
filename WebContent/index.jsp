<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<head>
	<link rel="stylesheet" type="text/css" href="css/style.css">
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<%@page import="java.util.*" %>
	<%@ page import="model.SearchQuery" %>
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

	<%
		ArrayList<String[]> result = SearchQuery.get_arrayList_of_genres();
	%>
	
	<div class="section">
		<table width="400" align="center" style="border: 2px solid #000000;">
			
			<tr>
				<td colspan=6 align="center" style="background-color: teal"><b>List of Genres</b></td>
			</tr>

			<tr align="center" style="background-color: lightgrey;">
				<td style="border: 1px solid #000000;">
					<%
						for(int i = 0; i < result.size(); i++)
						{
							String genres[] = result.get(i);
							String normalized = SearchQuery.normalize_string(genres[0]);
					%>
							<a href="movie_by_genre.jsp?genre=<%=normalized%>&att_order=title&order=ASC&current_page=1&display_count=10"><%=genres[0]%></a>
							<%out.print(" | ");%>
					<%
						}
					%>
				</td>
			</tr>
		</table>
		
		<table width="400" align="center" style="border: 2px solid #000000;">
			
			<tr>
				<td colspan=6 align="center" style="background-color: teal"><b>List of Movie by Alphabet</b></td>
			</tr>

			<tr align="center" style="background-color: lightgrey;">
				<td style="border: 1px solid #000000;">
					<%
						int num = 48;
						for(int i = 0; i < 10; i++)
						{
							char n = (char)(num+i);
					%>
							<a href="movie_by_alpha.jsp?alpha=<%=n%>&att_order=title&order=ASC&current_page=1&display_count=10"><%=n%></a>
							<%out.print("  ");%>
					<%
						}
					%>
				</td>
			</tr>
			<tr align="center" style="background-color: lightgrey;">
				<td style="border: 1px solid #000000;">
					<%
						int alp = 65;
						for(int i = 0; i < 26; i++)
						{
							char a = (char)(alp+i);
					%>
							<a href="movie_by_alpha.jsp?alpha=<%=a%>&att_order=title&order=ASC&current_page=1&display_count=10"><%=a%></a>
							<%out.print("  ");%>
					<%
						}
					%>
				</td>
			</tr>
		</table>
		
	</div>
	
	<!-- Using jQuery -->
    <script src="js/jquery-3.3.1.js"></script>
    <!-- include jquery autocomplete JS  -->
    <script src="js/jquery.autocomplete.min.js"></script>
    <!-- website JS  -->
	<script src="js/normal_search.js"></script>
	
</body>
</html>
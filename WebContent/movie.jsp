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
		String movie_id = request.getParameter("movie_id");
		ArrayList<String[]> result = SearchQuery.get_arrayList_from_movie(movie_id);
	%>
	
	<div class="section">
		<table width="700" align="center" style="border: 2px solid #000000;">
		
			<tr>
				<td colspan=6 align="center" style="background-color: teal"><b>Movie</b></td>
			</tr>

			<tr style="background-color: lightgrey;">
				<td style="border: 1px solid #000000;"><b>Id</b></td>
				<td style="border: 1px solid #000000;"><b>Title</b></td>
				<td style="border: 1px solid #000000;"><b>Year</b></td>
				<td style="border: 1px solid #000000;"><b>Director</b></td>
				<td style="border: 1px solid #000000;"><b>List of Genres</b></td>
				<td style="border: 1px solid #000000;"><b>List of Stars</b></td>
			</tr>

			<%
				if(result.size() > 0)
				{
					String info[] = result.get(0);
			%>
			
			<tr style="background-color: lightgrey;" align="center"
				style="border:2px solid #000000;">
				<td style="border: 1px solid #000000;">
					<%out.println(info[0]);%>
				</td>
				<td style="border: 1px solid #000000;">
					<%out.println(info[1]);%>
				</td>
				<td style="border: 1px solid #000000;">
					<%out.println(info[2]);%>
				</td>
				<td style="border: 1px solid #000000;">
					<%out.println(info[3]);%>
				</td>
				<td style="border: 1px solid #000000;">
					<%
						String[] genres = info[4].split(",");
						for(int j = 0; j < genres.length; j++)
						{
							
							String normalized = SearchQuery.normalize_string(genres[j]);
					%>
							<a href="movie_by_genre.jsp?genre=<%=normalized%>&att_order=title&order=ASC&current_page=1&display_count=10"><%=genres[j]%></a>
							<%out.print(", ");%>
					<%
						}
					%>
				</td>
				<td style="border: 1px solid #000000;">
					<%
						String[] hold = info[5].split(",");
						for(int j = 0; j < hold.length; j++)
						{
							
							String normalized = SearchQuery.normalize_string(hold[j]);
					%>
							<a href="star.jsp?star=<%=normalized%>"><%=hold[j]%></a>
							<%out.print(", ");%>
					<%
						}
					%>
				</td>
			</tr>
			<%
				}
				else
				{
			%>
				<tr><td>
					<%
					out.println("No result");
				}
					%>
				</td></tr>
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
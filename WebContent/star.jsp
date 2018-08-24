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
		String star = (String)request.getParameter("star");
		ArrayList<String[]> result = SearchQuery.get_arrayList_from_star(star);
	%>
	
	<div class="section">
		<table width="700" align="center" style="border: 2px solid #000000;">
		
			<tr>
				<td colspan=6 align="center" style="background-color: teal"><b>Star</b></td>
			</tr>

			<tr style="background-color: lightgrey;">
				<td style="border: 1px solid #000000;"><b>Name</b></td>
				<td style="border: 1px solid #000000;"><b>Birth date</b></td>
				<td style="border: 1px solid #000000;"><b>List of Movies</b></td>
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
					<%
						String[] hold = info[2].split(",");
						for(int j = 0; j < hold.length; j++)
						{
							hold[j] = hold[j].trim();
							String movie_id = SearchQuery.get_id_from_movie_title(hold[j]);
							if(movie_id.length() > 0)
							{
					%>
								<a href="movie.jsp?movie_id=<%=movie_id%>"><%=hold[j]%></a>
								<%out.print(", ");%>
					<%
							}
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
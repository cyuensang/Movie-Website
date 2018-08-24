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
		int display_count = Integer.parseInt(request.getParameter("display_count"));
		int current_page = Integer.parseInt(request.getParameter("current_page"));
		
		String att_order = (String)request.getParameter("att_order");
		String order = (String)request.getParameter("order");
		String normalized = "";
		
		String query = (String)request.getParameter("query");
		
		// Perform combined ft and fuzzy search
		String search_query = SearchQuery.get_arrayList_for_ftfuzz_search(query, 4);
		ArrayList<String[]> result = SearchQuery.get_arrayList_from_search_query(search_query, att_order, order, (current_page-1)*display_count, display_count+1);
		/*
		// Perform fulltext search first
		String search_query = SearchQuery.get_arrayList_for_ft_search(query);
		ArrayList<String[]> result = SearchQuery.get_arrayList_from_search_query(search_query, att_order, order, (current_page-1)*display_count, display_count+1);
		
		// Perform fulltext fuzzy first
		if(result.size() < display_count)
		{
			int remain = display_count - result.size();
			search_query = SearchQuery.get_arrayList_for_fuzzy_search(query, 5);
			result.addAll(SearchQuery.get_arrayList_from_search_query(search_query, att_order, order, (current_page-1)*remain, remain+1));
		}
		*/
		
		int next = current_page+1;
		int back = current_page-1;
		
		int result_size = result.size()>display_count? result.size()-1: result.size();
		String back_cond = current_page-1 <= 0? "disabled": "";
		String next_cond = result.size() <= display_count? "disabled": "";
	%>
	
	<div class="section">
		<table width="700" align="center" style="border: 2px solid #000000;">
			<tr>
				<td><b>Displaying <%=result_size%> results</b></td>
				<td><a href="ft_search_result.jsp?query=<%=query%>&att_order=<%=att_order%>&order=<%=order%>&current_page=1&display_count=10">10</a></td>
				<td><a href="ft_search_result.jsp?query=<%=query%>&att_order=<%=att_order%>&order=<%=order%>&current_page=1&display_count=25">25</a></td>
				<td><a href="ft_search_result.jsp?query=<%=query%>&att_order=<%=att_order%>&order=<%=order%>&current_page=1&display_count=50">50</a></td>
				<td><a href="ft_search_result.jsp?query=<%=query%>&att_order=<%=att_order%>&order=<%=order%>&current_page=1&display_count=100">100</a></td>
				<td><b>Title: </b></td>
				<td><a href="ft_search_result.jsp?query=<%=query%>&att_order=title&order=ASC&current_page=1&display_count=<%=display_count%>">ASC</a></td>
				<td><a href="ft_search_result.jsp?query=<%=query%>&att_order=title&order=DESC&current_page=1&display_count=<%=display_count%>">DESC</a></td>
				<td><b>Year: </b></td>
				<td><a href="ft_search_result.jsp?query=<%=query%>&att_order=year&order=ASC&current_page=1&display_count=<%=display_count%>">ASC</a></td>
				<td><a href="ft_search_result.jsp?query=<%=query%>&att_order=year&order=DESC&current_page=1&display_count=<%=display_count%>">DESC</a></td>
			</tr>
		</table>
	</div>
	
	<div class="section">
		<table width="700" align="center" style="border: 2px solid #000000;">
		
			<tr>
				<td colspan=6 align="center" style="background-color: teal"><b>Search Result</b></td>
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
					for (int i = 0; i < display_count && i < result.size(); i++) 
					{
						String info[] = result.get(i);
			%>

			<tr style="background-color: lightgrey;" align="center"
				style="border:2px solid #000000;">
				<td style="border: 1px solid #000000;">
					<%out.println(info[0]);%>
				</td>
				<td style="border: 1px solid #000000;">
					<a href="movie.jsp?movie_id=<%=info[0]%>"><%=info[1]%></a>
				</td>
				<td style="border: 1px solid #000000;">
					<%out.println(info[2]);%>
				</td>
				<td style="border: 1px solid #000000;">
					<%out.println(info[3]);%>
				</td>
				<td style="border: 1px solid #000000;">
					<%out.println(info[4]);%>
				</td>
				<td style="border: 1px solid #000000;">
					<%
						String[] hold = info[5].split(",");
						for(int j = 0; j < hold.length; j++)
						{
							
							normalized = SearchQuery.normalize_string(hold[j]);
					%>
							<a href="star.jsp?star=<%=normalized%>"><%=hold[j]%></a>
							<%out.print(", ");%>
					<%
						}
					%>
				</td>
				
				<td>
					<button type="button" onclick="addToCart('<%=info[0]%>','<%=info[1]%>')">Add to Cart</button>
				</td>
			</tr>
			<%
					}
				}
				else if(current_page == 1 && result.size() == 0)
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
	
	<div class="section">
		<a href="ft_search_result.jsp?query=<%=query%>&att_order=<%=att_order%>&order=<%=order%>&current_page=<%=back%>&display_count=<%=display_count%>"><input type="button" value="Back" <%=back_cond%>/></a>
		<a href="ft_search_result.jsp?query=<%=query%>&att_order=<%=att_order%>&order=<%=order%>&current_page=<%=next%>&display_count=<%=display_count%>"><input type="button" value="Next" <%=next_cond%>/></a>
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
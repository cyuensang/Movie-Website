package pages;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import model.SearchQuery;

/**
 * Servlet implementation class Suggestion
 */
@WebServlet("/Suggestion")
public class Suggestion extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Suggestion() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	@SuppressWarnings("unlikely-arg-type")
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			// setup the response json arrray
			JsonArray jsonArray = new JsonArray();
			JsonArray jsonMovie= new JsonArray();
			JsonArray jsonStar = new JsonArray();
			
			// get the query string from parameter
			String query = request.getParameter("query");
			
			// return the empty json array if query is null or empty
			if (query == null || query.trim().isEmpty()) {
				response.getWriter().write(jsonArray.toString());
				return;
			}	
			
			// search on marvel heros and DC heros and add the results to JSON Array
			// this example only does a substring match
			// TODO: in project 4, you should do full text search with MySQL to find the matches on movies and stars
			
			int count = 10;
			// Get Result for movies with ft prefix
			ArrayList<String[]> result = SearchQuery.get_arrayList_from_movie_prefix(query, count);
			for(int i = 0; i < result.size() && count > 0; i++, count--)
			{
				String[] hold = result.get(i);
				jsonMovie.add(generateJsonObject(hold[0], hold[1], "movie")); // id, title. "movie"
			}
			
			// Get Result for stars with ft prefix
			result = SearchQuery.get_arrayList_from_star_prefix(query, count);
			for(int j = 0; j < result.size() && count > 0; j++, count--)
			{
				String[] hold = result.get(j);
				jsonStar.add(generateJsonObject(hold[0], hold[1], "star")); // id, star_name. "star"
			}
			
			// Get Result for movies with fuzzy
			result = SearchQuery.get_arrayList_from_movie_fuzz(query, count, 4);
			for(int i = 0; i < result.size() && count > 0; i++, count--)
			{
				String[] hold = result.get(i);
				if(!jsonMovie.contains(generateJsonObject(hold[0], hold[1], "movie")) )
						jsonMovie.add(generateJsonObject(hold[0], hold[1], "movie")); // id, title. "movie"
			}
			
			// Get Result for stars with fuzzy
			result = SearchQuery.get_arrayList_from_star_fuzz(query, count, 4);
			for(int j = 0; j < result.size() && count > 0; j++, count--)
			{
				String[] hold = result.get(j);
				if(!jsonStar.contains(generateJsonObject(hold[0], hold[1], "star")) )
					jsonStar.add(generateJsonObject(hold[0], hold[1], "star")); // id, star_name. "star"
			}
			
			jsonArray.addAll(jsonMovie);
			jsonArray.addAll(jsonStar);
			response.setCharacterEncoding("UTF-8");
			response.getWriter().write(jsonArray.toString());
			return;
		} catch (Exception e) {
			System.out.println(e);
			response.sendError(500, e.getMessage());
		}
	}
	
	/*
	 * Generate the JSON Object from hero and category to be like this format:
	 * {
	 *   "value": "Iron Man",
	 *   "data": { "category": "marvel", "heroID": 11 }
	 * }
	 * 
	 */
	private static JsonObject generateJsonObject(String id, String value, String category) {
		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("value", value);
		
		JsonObject additionalDataJsonObject = new JsonObject();
		additionalDataJsonObject.addProperty("category", category);
		additionalDataJsonObject.addProperty("id", id);
		
		jsonObject.add("data", additionalDataJsonObject);
		return jsonObject;
	}

}

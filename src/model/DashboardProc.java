package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class DashboardProc {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	
	private static ArrayList<String[]> get_arrayList_from_query(String query, int numb_of_att)
	{
		ArrayList<String[]> res_table = new ArrayList<>();
		
		try {
			// Incorporate mySQL driver
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			// Connect to the test database
			Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/moviedb?autoReconnect=true&useSSL=false","root", "112968Met@");
			// Create an execute an SQL statement to select all of table"Stars" records
			Statement select = connection.createStatement();
			
			ResultSet result = select.executeQuery(query);
			while(result.next())
			{
				String hold[] = new String[numb_of_att];
				for(int i = 0; i < numb_of_att; i++)
					hold[i] = result.getString(i+1);
				res_table.add(hold);
			}
			connection.close();
		} catch (Exception e) {
			  e.printStackTrace();
		}
		return res_table;
	}
	
	public static ArrayList<String[]> get_arrayList_from_add_movie(String movie_id, String movie_title, 
			String movie_year, String movie_director, String star_name, String star_birthdate, 
			String genre_name)
	{
		String[] s = {movie_id, movie_title, movie_year, movie_director, star_name, star_birthdate, genre_name};
		
		for(int i =0; i < s.length; i++)
			if(s[i] == null || s[i].length() <= 0)
				s[i] = "NULL";
			else if(i != 2 && i != 5)
				s[i] = "\"" + s[i] + "\"";
		
		String query = "CALL add_movie(" + s[0] + ", " + s[1] + ", " + s[2] + ", " + s[3] + ", " + s[4] + ", " + s[5] + ", " + s[6] + ");";
		
		ArrayList<String[]> res_table = get_arrayList_from_query(query, 1);
		
		return res_table;
	}

}

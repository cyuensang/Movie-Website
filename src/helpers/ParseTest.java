package helpers;
import java.sql.SQLException;

import helpers.DomParser;
public class ParseTest {
	public static void main(String [] args)
	{
		DomParser p = new DomParser();
		
	
		
		//p.storeCastMembers();
		try {
			//parsing stuff
			p.retrieveStarSet();
			p.retrieveMovieSet();
			p.parseXML("actors63.xml",'a');
			p.parseXML("mains243.xml", 'm');
			p.parseXML("casts124.xml", 'c');
			
			p.storeMovies();
			p.insertGenres();
			p.storeActors();
			p.insertStars();
			p.storeCastMembers();
			
			p.naiveRetrieveGenresInMovies();
			//insert into genres tables
			
			
			//insert into movies table
			p.insertMovies();
			//insert into genres_in_movies_table
			p.insertMoviesGenres();
			p.naiveRetrieveStarsInMovies();
			p.insertStarsInMovies();
			
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//p.printMovies();
		
	}


}

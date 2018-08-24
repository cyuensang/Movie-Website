package helpers;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;



import model.BufferedMovie;
import model.Movie;
import model.Star;

import java.util.Random;
public class DomParser {
	HashSet<String> genres;
	HashSet<String> stars;
	HashSet<String> movies;
	Connection currentConnection;
	Document castDom;
	Document actorsDom;
	Document moviesDom;
	HashMap<String,Star> starBuffer;
	HashMap<String,Movie>  moviesBuffer;
	HashMap<String, String> starsInMoviesBuffer;
	//hack map for movie -> actors
	HashMap<String, HashSet<String> > movieToStars;
	HashMap<String, String> genresInMoviesBuffer;
	public DomParser()
	{
		genres = new HashSet<String>();
		this.currentConnection = null;
		this.castDom = null;
		this.actorsDom=null;
		this.starBuffer = new HashMap<String,Star>();
		this.moviesBuffer = new HashMap<String,Movie>();
		this.stars = new HashSet<String>();
		this.movies = new HashSet<String>();
		this.movieToStars = new HashMap<String, HashSet<String> >();
		this.genresInMoviesBuffer = new HashMap<String,String>();
		this.starsInMoviesBuffer = new HashMap<String,String>();
	}
	public void normalizeGenres()
	{
		BufferedMovie c;
		for(String movieName: moviesBuffer.keySet())
		{
			c=(BufferedMovie) moviesBuffer.get(movieName);
		}
	}
	public String[] splitGenres(BufferedMovie m)
	{
		
		
		m.getGenres().split(":");
		return m.getGenres().split(":");
	}
	public Connection establishConnection() throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException
	{
		Class.forName("com.mysql.jdbc.Driver").newInstance();

        // Connect to the test database
       Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/moviedb?autoReconnect=true&useSSL=false","root", "112968Met@");
       this.currentConnection=connection;
       System.out.println("Created connection!");
       return connection;
	}
	public boolean closeConnection() throws SQLException
	{
		if(currentConnection==null)
			return false;
		
		this.currentConnection.close();
		this.currentConnection = null;
		return true;
	}
	//badly designed shit
	private Document parseXMLHelper(char xmlInitial,Document d)
	{
		Document chosenDocument = null;
		switch(xmlInitial)
		{
			case 'c':
				chosenDocument = this.castDom = d;
			break;
			case 'a':
				chosenDocument = this.actorsDom = d;
			case 'm':
				chosenDocument = this.moviesDom = d;
			break;
			default:
				System.out.println("Invalid file name");
			break;
				
		}
		
		
		return chosenDocument;
	}
	
	public void parseXML(String fileName, char xmlInitial)
	{
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		
		DocumentBuilder db;
		try {
			db = dbf.newDocumentBuilder();
			parseXMLHelper(xmlInitial,db.parse("../xml/stanford-movies/"+fileName));
			
		} catch (ParserConfigurationException  | SAXException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Finished parsing "+fileName);
		
	}
	
	public void retrieveGenreSet() throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException
	{
		Class.forName("com.mysql.jdbc.Driver").newInstance();

         // Connect to the test database
		if(this.currentConnection==null)
			establishConnection();
        Statement select = this.currentConnection.createStatement();
        String statement = "SELECT name from genres;";
        // Create an execute an SQL statement to select all of table"Stars" records
        ResultSet genreResultSet = select.executeQuery(statement);
        
        while(genreResultSet.next())
        {
        	genres.add(genreResultSet.getString(1));
        }
        closeConnection();
	}
	public void retrieveMovieSet() throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException
	{
		Class.forName("com.mysql.jdbc.Driver").newInstance();

         // Connect to the test database
		if(this.currentConnection==null)
			establishConnection();
        Statement select = this.currentConnection.createStatement();
        String statement = "SELECT title from movies;";
        // Create an execute an SQL statement to select all of table"Stars" records
        ResultSet movieResultSet = select.executeQuery(statement);
        
        while(movieResultSet.next())
        {
        	movies.add(movieResultSet.getString(1).toLowerCase());
        }
        closeConnection();
	}
	public void retrieveStarSet() throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException
	{
		Class.forName("com.mysql.jdbc.Driver").newInstance();

         // Connect to the test database
		if(this.currentConnection==null)
			establishConnection();
        Statement select = this.currentConnection.createStatement();
        String statement = "SELECT name from stars;";
        // Create an execute an SQL statement to select all of table"Stars" records
        ResultSet genreResultSet = select.executeQuery(statement);
        
        while(genreResultSet.next())
        {
        	stars.add(genreResultSet.getString(1));
        	System.out.println(genreResultSet.getString(1));
        }
        closeConnection();
	}
	public String generateStarId()
	{
		final int KEY_LEN = 10;
		String [] possible_values = {"a","b","c","d","e","f","g","h","i","j","k",
							"l","m","n","o","p","q","r","s","t","u","v",
							"w","x","y","z","0","1","2","3","4","5","6","7","8","9"};
		StringBuilder result= new StringBuilder("");
		Random rnd = new Random();
		for(int i=0;i<KEY_LEN;i++)
		{
				result.append(possible_values[rnd.nextInt(possible_values.length)]);
		}
		
		
		return result.toString();
		
	}
	public void printCurrentGenres()
	{
		for(String g: genres)
		{
			System.out.println(g);
		}
	}
	//returns null if the genre wasn't found in the query
	private String whatIsThisGenre(String genre) throws SQLException
	{
		
		if(genre.length()<2)
			return null;
		
		Statement select = this.currentConnection.createStatement();
	    String statement = "SELECT name from genres where name like \""+ genre.substring(0, genre.length()-1)+"%\";";
	    ResultSet genreResultSet = select.executeQuery(statement);
	    while(genreResultSet.next())
	    {
	    	return genreResultSet.getString(1);
	    }
	   
	    return null;
	}
	private boolean isThisMovieAlreadyHere(String title)
	{
		
		return movies.contains(title.toLowerCase());
	}
	private String fixGenre(String g)
	{
		String fixed = GreatOldDictionary.myBigDict.get(g);
		if(fixed == null)
			return null;
		else
			return fixed;
	}
	private boolean processGenres(BufferedMovie m, NodeList genreList) throws DOMException, SQLException
	{
			String currentGenre;
			String fixedGenre;
			
				
				if(genreList!=null && genreList.getLength()>0)
				{
					for(int i=0;i<genreList.getLength();i++)
					{
						
						fixedGenre = fixGenre(genreList.item(i).getTextContent());
						if(fixedGenre != null)
							currentGenre = whatIsThisGenre(fixedGenre);
						else
							currentGenre = whatIsThisGenre(genreList.item(i).getTextContent());//whatIsThisGenre(genreList.item(i).getTextContent());
						
						if(fixedGenre!=null && currentGenre == null)
						{
							m.appendGenre(fixedGenre);
							genres.add(fixedGenre);
						}
						else
						{
							return false;
						}
							
					}
					return true;
				}
				return false;
			
		
			
			
	}
	public void insertStarsMoviesHelper(String movieId,ArrayList<String> g, PreparedStatement ps2) throws SQLException
	{
		for(int i=0;i<g.size();i++)
		{
			if(starsInMoviesBuffer.get(g.get(i))==null)
				continue;
			ps2.setString(2, movieId);
			ps2.setString(1, starsInMoviesBuffer.get(g.get(i)));
			ps2.addBatch();
		}
	}
	public void insertMovieGenresHelper(String movieId,ArrayList<String> g, PreparedStatement ps2) throws SQLException
	{
		for(int i=0;i<g.size();i++)
		{
			if(genresInMoviesBuffer.get(g.get(i))==null)
				continue;
			ps2.setString(2, movieId);
			ps2.setString(1, genresInMoviesBuffer.get(g.get(i)));
			ps2.addBatch();
		}
	}
	public void insertMoviesGenres() throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException
	{
		this.establishConnection();
		
		PreparedStatement ps = this.currentConnection.prepareStatement("INSERT INTO genres_in_movies(genreId,movieId) VALUES (?,?)");
		this.currentConnection.setAutoCommit(false);
		int [] numInserted = null;
		BufferedMovie m;
		System.out.println("Inserting genres_in_movies");
		for(String g: moviesBuffer.keySet())
		{	
			m = (BufferedMovie) moviesBuffer.get(g);
		
			insertMovieGenresHelper(m.getMovieId(),m.getGenreAsList(),ps);
			
			
		}
		numInserted = ps.executeBatch();
		this.currentConnection.commit();
		this.closeConnection();
	}
	public void insertMovies() throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException
	{
		this.establishConnection();
		PreparedStatement ps = this.currentConnection.prepareStatement("INSERT INTO movies(id,title,year,director) VALUES (?,?,?,?)");
		
		this.currentConnection.setAutoCommit(false);
		int [] numInserted = null;
		BufferedMovie m;
		System.out.println("Inserting movies");
		for(String g: moviesBuffer.keySet())
		{	
			m = (BufferedMovie) moviesBuffer.get(g);
			ps.setString(1, m.getMovieId());
			ps.setString(2, m.getTitle());
			ps.setInt(3, m.getYear());
			
			
			
			ps.setString(4, m.getDirector());
			ps.addBatch();
		}
		numInserted = ps.executeBatch();
	
		this.currentConnection.commit();
		this.closeConnection();
	}
	public void naiveGenreHelper(ArrayList<String> genreList) throws SQLException
	{
		PreparedStatement ps = this.currentConnection.prepareStatement("SELECT id,name from genres WHERE name = ? ");
		
		for(int i=0;i<genreList.size();i++)
		{
			if(this.genresInMoviesBuffer.keySet().contains(genreList.get(i)))
				continue;
			ps.setString(1,genreList.get(i));
			
			ResultSet r = ps.executeQuery();
			String curId,curName;
			while(r.next())
			{
				curId=r.getString(1);
				curName=r.getString(2);
				this.genresInMoviesBuffer.put(curName,curId);
			}
		}
		
	}
	
	public void printGenreInMoviesBuffer()
	{
		
		System.out.println("Len "+genresInMoviesBuffer.keySet().size());
		for(String s: genresInMoviesBuffer.keySet())
		{
			System.out.println("Name "+s+" and Id: "+genresInMoviesBuffer.get(s));
		}
	}
	public void naiveRetrieveGenresInMovies() throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException
	{
		this.establishConnection();
		System.out.println("Caching genres");
		for(String movieTitle: moviesBuffer.keySet())
		{
			BufferedMovie m = (BufferedMovie) moviesBuffer.get(movieTitle);
			ArrayList<String> s = m.getGenreAsList();
			naiveGenreHelper(s);
		}
		this.closeConnection();
	}
	public void naiveRetrieveStarsInMovies() throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException
	{
		this.establishConnection();
		StringBuilder superStatement = new StringBuilder("SELECT id,name from stars WHERE name in (");
		PreparedStatement ps;
		String curId,curName;
		System.out.println("Caching stars in movies");
		int counter = 1;
		for(String starName: stars)
		{
			superStatement.append("?,");
			
			
		}
		superStatement.setLength(superStatement.length()-1);
		superStatement.append(")");
		ps = this.currentConnection.prepareStatement(superStatement.toString());
		
		for(String starName: stars)
		{
			
			ps.setString(counter, starName);
			counter++;
		}
		ResultSet r = ps.executeQuery();
		
		while(r.next())
		{
			curId=r.getString(1);
			curName=r.getString(2);
			this.starsInMoviesBuffer.put(curName,curId);
		}
		this.closeConnection();
	}
	public void printStarsInMovies()
	{
		for(String s: starsInMoviesBuffer.keySet())
		{
			System.out.println(s+" "+starsInMoviesBuffer.get(s));
		}
	}
	public void insertGenres() throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException
	{
		this.establishConnection();
		PreparedStatement ps = this.currentConnection.prepareStatement("INSERT INTO genres(name) VALUES (?)");
		this.currentConnection.setAutoCommit(false);
		int [] numInserted = null;
		for(String g: genres)
		{
			ps.setString(1, g);
			ps.addBatch();
		}
		numInserted = ps.executeBatch();
		this.currentConnection.commit();
		this.closeConnection();
	}
	public void insertStars() throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException
	{
		this.establishConnection();
		PreparedStatement ps = this.currentConnection.prepareStatement("INSERT INTO stars(id,name,birthYear)VALUES (?,?,?)");
		this.currentConnection.setAutoCommit(false);
		int [] numInserted = null;
		Star currentStar;
		for(String g: starBuffer.keySet())
		{
			currentStar = starBuffer.get(g);
			
			ps.setString(1, currentStar.getStarId());
			ps.setString(2,currentStar.getStarName());
			System.out.println("Inserted "+currentStar.getStarName());
			if(currentStar.getDob()!=null)
				ps.setInt(3, currentStar.getDob());
			else
				ps.setNull(3, java.sql.Types.INTEGER);
			ps.addBatch();
		}
		numInserted = ps.executeBatch();
		this.currentConnection.commit();
		this.closeConnection();
	}
	public void storeCasts()
	{
		
	}
	public void storeMoviesHelper(Node currentMovie, String dirName) throws DOMException, SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException
	{
		NodeList movieName = ((Element) currentMovie).getElementsByTagName("t");
		NodeList directorsList=((Element) currentMovie).getElementsByTagName("dirs");
		NodeList currentYear = ((Element) currentMovie).getElementsByTagName("year");
		NodeList genres = ((Element) currentMovie).getElementsByTagName("cats");
		BufferedMovie movieToBeAdded = new BufferedMovie();
		
		if(movieName!=null && movieName.getLength()>0 && isThisMovieAlreadyHere(movieName.item(0).getTextContent()))
		{
			return;
		}
		else if(movieName!=null && movieName.getLength()>0)
		{
			movieToBeAdded.setTitle(movieName.item(0).getTextContent());
		}
		else
		{
			System.err.println("No title for this movie");
			return;
		}
		
		if(currentYear!=null && currentYear.getLength()>0 && currentYear.item(0).getTextContent().matches("[0-9]+"))
		{
			movieToBeAdded.setYear(Integer.parseInt(currentYear.item(0).getTextContent()));
		}
		else
		{
			System.err.println("No valid year for movie: "+movieName.item(0).getTextContent());
			return;
		}
		
		if(dirName!=null)
		{
			
			movieToBeAdded.appendDirector(dirName);
			
			
		}
		else
		{
			System.err.println("No director for this movie");
			return;
		}
		
		if(genres!=null && genres.getLength()>0)
		{
			NodeList genresList = ((Element) genres.item(0)).getElementsByTagName("cat");
			processGenres(movieToBeAdded,genresList);
		}
		movieToBeAdded.setId(generateStarId());
		moviesBuffer.put(movieToBeAdded.getTitle(), movieToBeAdded);
		
	}
	public void printMovies()
	{
		for(String t: moviesBuffer.keySet())
		{
			System.out.println(moviesBuffer.get(t));
		}
	}
	public void storeMovies()
	{
		Element moviesRoot = this.moviesDom.getDocumentElement();
		NodeList directorRoot = moviesRoot.getElementsByTagName("directorfilms");
		String dirName=null;
		NodeList currentMovieList = null;
		Node currentMovie;
		 //open for genre processing
		
		try 
		{
			this.establishConnection();
			if(directorRoot!=null)
			{
				for(int i=0;i<directorRoot.getLength();i++)
				{
					currentMovieList = ((Element) directorRoot.item(i)).getElementsByTagName("film");
					NodeList dirList = ((Element) directorRoot.item(i)).getElementsByTagName("dirname");
					if(dirList!=null && dirList.getLength()>0)
						dirName = dirList.item(0).getTextContent();
					if(currentMovieList!=null)
						for(int j=0;j<currentMovieList.getLength();j++)
							storeMoviesHelper(currentMovieList.item(j),dirName);
				
				}
			}
			this.closeConnection();//finish processing
		} 
		catch (DOMException | SQLException | InstantiationException | IllegalAccessException | ClassNotFoundException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public void storeActors() throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException
	{
		if(this.actorsDom == null)
			return;
		
		Element actorRoot = this.actorsDom.getDocumentElement();
		NodeList nl = actorRoot.getElementsByTagName("actor");
		Node currentActor;
		//
		
		if(nl!=null)
		{
			for(int i=0;i<nl.getLength();i++)
			{
				currentActor = nl.item(i);
				if(currentActor!=null)
				{
					NodeList actorDOB = ((Element) currentActor).getElementsByTagName("dob");
					NodeList actorName = ((Element) currentActor).getElementsByTagName("stagename");
					Star starToBeInserted = new Star();
					
					if(actorName!=null && actorName.item(0)!=null && stars.contains(actorName.item(0).getTextContent()))
					{
						continue;
					}
					else if(actorName!=null && actorName.item(0)!=null)
					{
						
						starToBeInserted.setStarName(actorName.item(0).getTextContent());
					}
					else
					{
						//an actor without a full name is useless, so just skip
						System.err.println("No full name for this Actor!");
						continue;
					}
					if(actorDOB!=null && actorDOB.item(0)!=null && !actorDOB.item(0).getTextContent().isEmpty())
					{
						//damn dirty hack
						String dob = actorDOB.item(0).getTextContent();
						if(dob.matches("[0-9]+"))
							starToBeInserted.setDob(new Integer(Integer.parseInt(actorDOB.item(0).getTextContent().toString())));
						else
							System.err.println("Invalid DOB format!");
					}
					else
					{
						starToBeInserted.setDob(null);
					}
					
					System.out.println("Inserting into Star Buffer");
					starToBeInserted.setStarId(this.generateStarId());
					starBuffer.put(starToBeInserted.getStarName().toLowerCase(), starToBeInserted);
				}
			}
		}
		
	}
	
	public void printActorsMap()
	{
		for(String id: starBuffer.keySet())
		{
			System.out.println(starBuffer.get(id));
		}
	}
	public void printStarSet()
	{
		for(String s: stars)
		{
			System.out.println("Hey "+s);
		}
	}
	public void printCastMembers()
	{
		Element castRoot = castDom.getDocumentElement();
		String currentMovie;
		
		
		NodeList nl = castRoot.getElementsByTagName("filmc");
		if(nl!=null)
		{
			for(int i=0;i<nl.getLength();i++)
			{
				NodeList castList= ((Element) nl.item(i)).getElementsByTagName("m");
				if(castList!=null)
				{
					for(int j=0;j<castList.getLength();j++)
					{
						if(castList.item(i)!=null)
						{
							NodeList children = castList.item(i).getChildNodes();
							if(children!=null)
								System.out.println("Title: "+children.item(1).getTextContent()+"\nActress\\Actor:"+children.item(2).getTextContent()+"\n\n\n");
						}
						}
				}
				
			}
		}
		
	}
	public void storeCastMembers()
	{
		Element castRoot = castDom.getDocumentElement();
		String currentMovie;
		
		
		NodeList n1 = castRoot.getElementsByTagName("filmc");
		
		for(int i=0;i<n1.getLength();i++)
		{
			NodeList nl = ((Element)n1.item(i)).getElementsByTagName("m");
			if(nl!=null)
				for(int j=0;j<nl.getLength();j++)
				{
					NodeList movieNodeList=((Element) nl.item(j)).getElementsByTagName("t");
					NodeList actorNodeList=((Element) nl.item(j)).getElementsByTagName("a");
					Node movieNode=null,actorNode=null;
					String movieName=null,actorName=null;
					if(movieNodeList!=null && movieNodeList.getLength()>0) movieNode = movieNodeList.item(0);
					if(actorNodeList!=null && actorNodeList.getLength()>0) actorNode = actorNodeList.item(0);
					if(movieNode!=null)movieName = movieNode.getTextContent();
					if(actorNode!=null)actorName = actorNode.getTextContent();
			
					if(movieNode==null)
						continue;
					if(moviesBuffer.containsKey(movieName))
					{
						System.out.println("Inserting "+movieName);
						((BufferedMovie)moviesBuffer.get(movieName)).appendStar(actorName);
						stars.add(actorName);
					}
					
				}
		}
		
		
		
	}
	public void insertStarsInMovies() throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException
	{
		this.establishConnection();
		System.out.println("Inserting stars_in_movies");
		PreparedStatement ps = this.currentConnection.prepareStatement("INSERT INTO stars_in_movies(starId,movieId) VALUES (?,?)");
		this.currentConnection.setAutoCommit(false);
		int [] numInserted = null;
		BufferedMovie m;
		
		for(String g: moviesBuffer.keySet())
		{	
			m = (BufferedMovie) moviesBuffer.get(g);
		
			insertStarsMoviesHelper(m.getMovieId(),m.getStarsAsList(),ps);
			
			
		}
		numInserted = ps.executeBatch();
		this.currentConnection.commit();
		this.closeConnection();
	}
	
	/*private void printCastMember(Element castMember)
	{
		String movieName = castMember.getText
	}*/

}

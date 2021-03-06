package model;

import java.util.ArrayList;

public class BufferedMovie extends Movie {

	private StringBuilder genreBuffer;
	private ArrayList<String> genreBufferList;
	private StringBuilder starsListBuffer;
	private ArrayList<String> starsArray;
	private StringBuilder directorsBuffer;
	
	public BufferedMovie()
	{
		super();
		this.genreBuffer = new StringBuilder();
		this.starsListBuffer = new StringBuilder();
		this.directorsBuffer = new StringBuilder();
		this.genreBufferList = new ArrayList<String>();
		this.starsArray = new ArrayList<String>();
		this.movieId = null;
	}
	public BufferedMovie(String title, int year,String director,
			double ranking,String genres, String starsList,String movieId)
	{
		super(title,year,director,ranking,genres,starsList,movieId);
		
		this.genreBuffer = new StringBuilder(starsList);
		this.starsListBuffer = new StringBuilder(genres);
		this.directorsBuffer = new StringBuilder(director);
	}
	public String setId(String newId)
	{
		this.movieId = newId;
		return this.movieId;
	}
	@Override
	public String getGenres()
	{
		return this.genreBuffer.toString();
	}
	public ArrayList<String> getGenreAsList()
	{
		return this.genreBufferList;
	}
	@Override
	public String getStars()
	{
		return this.starsListBuffer.toString();
	}
	public ArrayList<String> getStarsAsList()
	{
		return this.starsArray;
	}
	@Override
	public String getDirector()
	{
		return this.directorsBuffer.toString();
	}
	public String appendGenre(String newGenre)
	{
		this.genreBuffer.append(newGenre+":");
		this.genreBufferList.add(newGenre);
		this.genres = genreBuffer.toString();
		
		return this.genres;
	}
	
	public String appendStar(String newStar)
	{
		if(this.starsListBuffer.length()>0)
			this.starsListBuffer.append(":");
		this.starsListBuffer.append(newStar);
		this.starsArray.add(newStar);
		this.starsList = starsListBuffer.toString();
		
		return this.starsList;
	}
	public String appendDirector(String newDirector)
	{
		if(this.directorsBuffer.length()>0)
			this.directorsBuffer.append(":");
		this.directorsBuffer.append(newDirector);
		
		this.director = this.directorsBuffer.toString();
		
		return this.director;
	}
	
	@Override
	public String toString()
	{
		StringBuilder movieInfo = new StringBuilder();
		
		movieInfo.append("**********************\n");
		movieInfo.append("Id: "+this.getMovieId()+"\n");
		movieInfo.append("Title "+this.getTitle()+"\n");
		movieInfo.append("Year: "+this.getYear()+"\n");
		movieInfo.append("Directors: "+this.directorsBuffer.toString()+"\n");
		movieInfo.append("Genre: "+genreBuffer.toString()+"\n");
		movieInfo.append("*****************\n\n\n");
		
		
		return movieInfo.toString();
	}
}

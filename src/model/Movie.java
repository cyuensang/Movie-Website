package model;

public class Movie {
	private String title;
	private int year;
	protected String director;
	private Double ranking;
	protected String genres;
	protected String starsList;
	protected String movieId;
	
	public Movie()
	{
		this.title = null;
		this.year = -1;
		this.director = "";
		this.ranking = -1.0;
		this.genres = null;
		this.starsList=null;
		this.movieId=null;
		
	}
	
	public Movie(String title, int year,String director,double ranking,String genres, String starsList,String movieId)
	{
		this.title = title;
		this.year = year;
		this.director = director;
		this.ranking = ranking;
		this.genres = genres;
		this.starsList = starsList;
		this.movieId = movieId;
	}
	public String getMovieId()
	{
		return this.movieId;
	}
	
	public double getRanking()
	{
		return this.ranking;
	}
	public String getTitle()
	{
		return this.title;
	}
	public String setTitle(String newTitle)
	{
		this.title = newTitle;
		
		return this.title;
	}
	public String getDirector()
	{
		return this.director;
	}
	public String getGenres()
	{
		return this.genres;
	}
	public String getStars()
	{
		return this.starsList;
	}
	public int getYear()
	{
		return this.year;
	}
	public int setYear(int newYear)
	{
		this.year = newYear;
		
		return this.year;
	}
	@Override
	public String toString()
	{
		String info = "Title: "+this.title+"<br>"+"Year: "+Integer.toString(this.year)+"<br>"
						+"Director: "+this.director+"<br>"+"Rating: "+Double.toString(this.ranking);
		StringBuilder temp = new StringBuilder(info);
		temp.append("<br>"+"Genre: "+genres);
		temp.append("<br>"+"Starring: "+starsList);
		return temp.toString();
	}
	
}

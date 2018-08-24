package model;

public class CartItem extends Movie {

	private int qty;
	private double price;
	
	public CartItem(String title,String movieId)
	{
		super(title,0,"",0.0,"","",movieId);
		this.qty=1;
		this.price=0;
		System.out.println("Created "+title+" "+movieId);
	}
	public CartItem(Movie m, double price)
	{
		super(m.getTitle(),m.getYear(),m.getDirector(),m.getRanking(),m.getGenres(),m.getStars(),m.getMovieId());
		this.qty = qty;
		this.price = price;
		
	}
	public CartItem(String title, int year,String director,
			double ranking,String genres, 
			String starsList,int qty,double price,String movieId)
	{
		super(title,year,director,ranking,genres,starsList,movieId);
		this.qty = qty;
		this.price = price;
		
	}
	
	public int changeQuantity(int newQty)
	{
		this.qty = newQty;
		
		return newQty;
	}
	@Override
	public int hashCode()
	{
		return this.getMovieId().hashCode();
	}
	public int getQty()
	{
		return this.qty;
	}
	public double getPrice()
	{
		return this.price;
	}
	
	public double calculateMultipledPrice()
	{
		return this.qty*this.price;
	}
	public String toString()
	{
		return this.getTitle();
	}
	public String toHtmlRow()
	{
		String result = "<tr id=\""+getMovieId()+"-row\">\r\n" + 
				"		<td>\r\n" + 
							getTitle() + 
				"		</td>\r\n" + 
				"	<td>\r\n" + 
				"		<input type=\"text\" name=\"qty\" class=\"qty-box\" value= \""+getQty()+"\" id=\""+getMovieId()+"-qty\">\r\n" + 
				"	</td>\r\n" + 
				"	<td>\r\n" + 
				"		<button onclick=update_cart_item(\""+getMovieId()+"\")>\r\n" + 
				"			update\r\n" + 
				"		</button>\r\n" + 
				"	</td>\r\n" + 
				"	<td>\r\n" + 
				"		<button onclick=remove_cart_item(\""+getMovieId()+"\")>\r\n" + 
				"			remove\r\n" + 
				"		</button>\r\n" + 
				"	</td>\r\n" + 
				"</tr>";
		
		return result;
	}
	public String toHtmlRowSuccess()
	{
		String result = "<tr id=\""+getMovieId()+"-row\">\r\n" + 
				"		<td>\r\n" + 
							getTitle() + 
				"		</td>\r\n" + 
				"	<td>\r\n" + 
				getQty()+ 
				"	</td>\r\n" + 
				"</tr>";
		
		return result;
	}
}

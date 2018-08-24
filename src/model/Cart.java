package model;


import java.util.HashMap;


import model.CartItem;
public class Cart {
	HashMap<String,CartItem> items;
	double totalPrice;
	public Cart()
	{
		items = new HashMap<String,CartItem>();
		this.totalPrice = 0;
	}
	
	public double getTotalPrice(double newPrice)
	{
		return this.totalPrice;
	}
	public double setTotalPrice(double newPrice)
	{
		this.totalPrice = newPrice;
		
		return this.totalPrice;
	}
	public boolean doesItemExist(CartItem item)
	{
		return items.containsKey(item.getMovieId());
	}
	public CartItem addNewItem(CartItem newItem)
	{
		items.put(newItem.getMovieId(), newItem);
		
		return newItem;
		
	}
	public CartItem removeItem(CartItem item)
	{
		return items.remove(item.getMovieId());
	}
	public CartItem removeItem(String movieId)
	{
		return items.remove(movieId);
	}
	public double increaseByOne(String itemId)
	{
		CartItem itemToBeUpdated = items.get(itemId);
		return updateTotalBasedOnNewItemQuantity(itemId,itemToBeUpdated.getQty()+1);
	}
	public double updateTotalBasedOnNewItemQuantity(String itemId,int newQty)
	{
		double newTotalPrice = this.totalPrice;
		CartItem itemToBeUpdated = items.get(itemId);
		newTotalPrice = newTotalPrice - itemToBeUpdated.calculateMultipledPrice();
		itemToBeUpdated.changeQuantity(newQty);
		newTotalPrice += itemToBeUpdated.calculateMultipledPrice();
		this.totalPrice = newTotalPrice;
		return this.totalPrice;
	}
	public void justUpdate(String itemId,int newQty)
	{
		CartItem i=items.get(itemId);
		System.out.println("Updating");
		if(i!=null)
		{
			i.changeQuantity(newQty);
			System.out.println("New quantity for "+itemId+" is "+newQty);
		}
	}
	public HashMap<String,CartItem> getItemsMap()
	{
		return this.items;
	}
	@Override
	public String toString()
	{
		return "Number of items in Cart: "+items.size();
	}
}

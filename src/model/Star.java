package model;

public class Star {

	String starId;
	String starName;
	Integer dob;
	public Star()
	{
		this.starId = null;
		this.starName = null;
		this.dob = null;
	}
	public Star(String id, String starName, Integer dob)
	{
		this.starId = id;
		this.starName = starName;
		this.dob = dob;
	}
	
	public String getStarId()
	{
		return this.starId;
	}
	public String getStarName()
	{
		return this.starName;
	}
	public Integer getDob()
	{
		return this.dob;
	}
	
	public String setStarId(String id)
	{
		this.starId = id;
		
		return this.starId;
	}
	
	public String setStarName(String name)
	{
		this.starName = name;
		
		return this.starName;
	}
	
	public Integer setDob(Integer dob)
	{
		this.dob = dob;
		
		return this.dob;
	}
	@Override
	public String toString()
	{
		StringBuilder result = new StringBuilder();
		
		if(this.starId!=null)
			result.append("Star ID: "+this.starId+"\n");
		if(this.starName!=null)
			result.append("Star Name: "+this.starName+"\n");
		if(this.dob!=null)
			result.append("DOB: "+this.dob.toString());
		
		return result.toString();
	}
}

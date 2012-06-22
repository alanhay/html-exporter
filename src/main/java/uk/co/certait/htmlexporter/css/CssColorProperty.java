package uk.co.certait.htmlexporter.css;

public enum CssColorProperty
{
	COLOR("color"), 
	BACKGROUND("background"), 
	BORDER_COLOR("border-color");
	
	private String property;
	
	private CssColorProperty(String property)
	{
		this.property = property;
	}
	
	public String getProperty()
	{
		return property;
	}
}

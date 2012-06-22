package uk.co.certait.htmlexporter.css;

public enum CssIntegerProperty
{
	FONT_SIZE("font-size"),
	WIDTH("width"),
	BORDER_WIDTH("border-width");
	
	private String property;
	
	private CssIntegerProperty(String property)
	{
		this.property = property;
	}
	
	public String getProperty()
	{
		return property;
	}
}

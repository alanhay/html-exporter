package uk.co.certait.htmlexporter.css;

public enum CssStringProperty
{
	FONT_FAMILY("font-family"),
	FONT_WEIGHT("font-weight"),
	FONT_STYLE("font-style"),
	TEXT_DECORATION("text-decoration"),
	TEXT_ALIGN("text-align");
	
	private String property;
	
	private CssStringProperty(String property)
	{
		this.property = property;
	}
	
	public String getProperty()
	{
		return property;
	}
}

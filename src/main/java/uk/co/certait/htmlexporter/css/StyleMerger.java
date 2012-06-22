package uk.co.certait.htmlexporter.css;


/**
 * 
 * @author alanhay
 *
 */
public class StyleMerger
{
	public static Style mergeStyles(Style... styles)
	{
		Style merged = new Style();
		
		for(Style style : styles)
		{
			for(CssStringProperty p : style.getStringProperties().keySet())
			{
				merged.addProperty(p, style.getProperty(p));
			}
			
			for(CssIntegerProperty p : style.getIntegerProperties().keySet())
			{
				merged.addProperty(p, style.getProperty(p));
			}
			
			for(CssColorProperty p : style.getColorProperties().keySet())
			{
				merged.addProperty(p, style.getProperty(p));
			}
		}
		
		return merged;
	}
}

package uk.co.certait.htmlexporter.css;

import java.awt.Color;

import com.osbcp.cssparser.PropertyValue;
import com.osbcp.cssparser.Rule;
import com.osbcp.cssparser.Selector;

public class StyleGenerator
{
	private static final String PX = "px";
	
	protected Style createStyle(Rule rule, Selector selector)
	{
		Style style = new Style();

		populateIntegerProperties(rule, selector, style);
		populateStringProperties(rule, selector, style);
		populateColorProperties(rule, selector, style);

		return style;
	}
	
	protected void populateIntegerProperties(Rule rule, Selector selector, Style style)
	{
		for (PropertyValue pv : rule.getPropertyValues())
		{
			for(CssIntegerProperty p : CssIntegerProperty.values())
			{
				if(p.getProperty().equals(pv.getProperty()))
				{
					style.addProperty(p, Integer.parseInt(pv.getValue().replaceAll(PX, "").trim()));
				}
			}
		}
	}
	
	protected void populateStringProperties(Rule rule, Selector selector, Style style)
	{
		for (PropertyValue pv : rule.getPropertyValues())
		{
			for(CssStringProperty p : CssStringProperty.values())
			{
				if(p.getProperty().equals(pv.getProperty()))
				{
					style.addProperty(p, pv.getValue().trim());
				}
			}
		}
	}
	
	protected void populateColorProperties(Rule rule, Selector selector, Style style)
	{
		for (PropertyValue pv : rule.getPropertyValues())
		{
			for(CssColorProperty p : CssColorProperty.values())
			{
				if(p.getProperty().equals(pv.getProperty()))
				{
					style.addProperty(p, createColor(pv.getValue().trim()));
				}
			}
		}
	}
	
	private Color createColor(String hex)
	{
		hex = hex.toUpperCase();

		return Color.decode(hex);
	}
}

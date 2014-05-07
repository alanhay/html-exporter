/**
 * Copyright (C) 2012 alanhay <alanhay99@hotmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package uk.co.certait.htmlexporter.css;

import java.awt.Color;

import com.osbcp.cssparser.PropertyValue;
import com.osbcp.cssparser.Rule;
import com.osbcp.cssparser.Selector;

public class StyleGenerator
{
	private static final String PX = "px";
	private static final String PERCENTAGE = "%";
	
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
					if(! pv.getValue().contains(PERCENTAGE)){
						double value = Double.parseDouble(pv.getValue().replaceAll(PX, "").trim());
						
						if(value < 1){
							value = 1;
						}
						
						style.addProperty(p, (int)value);
					}
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

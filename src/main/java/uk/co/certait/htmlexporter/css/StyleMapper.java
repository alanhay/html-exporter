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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.jsoup.nodes.Element;

import com.osbcp.cssparser.CSSParser;
import com.osbcp.cssparser.Rule;

/**
 * 
 * @author alanhay
 *
 */
public class StyleMapper
{
	private static final String CLASS_PREFIX = ".";
	
	Map<String, Style> styles;
	private StyleGenerator generator;
	
	public StyleMapper(Map<String, Style> styles)
	{
		this.styles = styles;
		generator = new StyleGenerator();
	}
	
	public Style getStyleForElement(Element element)
	{
		Style style = new Style();

		if(getStyleForTag(element) != null)
		{
			style = StyleMerger.mergeStyles(style, getStyleForTag(element));
		}
		
		if(! getStylesForClass(element).isEmpty())
		{
			List<Style> classStyles = getStylesForClass(element);
			
			for(Style classStyle : classStyles)
			{
				style = StyleMerger.mergeStyles(style, classStyle);
			}
		}
		
		if(getInlineStyle(element) != null)
		{
			style = StyleMerger.mergeStyles(style, getInlineStyle(element));
		}

		return style;
	}

	private Style getStyleForTag(Element element)
	{
		return styles.get(element.tagName());
	}

	private List<Style> getStylesForClass(Element element)
	{
		List<Style> classStyles = new ArrayList<Style>();

		if (StringUtils.isNotEmpty(element.className()))
		{
			String [] classNames = element.className().split(" ");

			for(String className : classNames)
			{
				String qualifiedClassName = CLASS_PREFIX + className.trim();
		
				if (styles.containsKey(qualifiedClassName))
				{	
					classStyles.add(styles.get(qualifiedClassName));
				}
			}
		}

		return classStyles;
	}

	private Style getInlineStyle(Element element)
	{
		Style style = null;

		if (element.hasAttr("style"))
		{
			List<Rule> inlineRules;
			try
			{
				inlineRules = CSSParser.parse("x{" + element.attr("style") + "}");
			}
			catch (Exception e)
			{
				throw new RuntimeException("Error parsing inline style for element " + element.tagName());
			}
			
			style = generator.createStyle(inlineRules.get(0), inlineRules.get(0).getSelectors().get(0));
		}

		return style;
	}
}

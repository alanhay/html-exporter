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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.osbcp.cssparser.CSSParser;
import com.osbcp.cssparser.Rule;
import com.osbcp.cssparser.Selector;

/**
 * 
 * @author alanhay
 * 
 */
public class StyleParser
{
	public static final String HREF_ATTRIBUTE_KEY = "href";

	private StyleGenerator generator;
	private ExternalStyleSheetLoader externalStyleSheetLoader;
	Map<String, Style> styles = null;

	public StyleParser(ExternalStyleSheetLoader externalStyleSheetLoader)
	{
		generator = new StyleGenerator();
		this.externalStyleSheetLoader = externalStyleSheetLoader;

		styles = new HashMap<String, Style>();
	}

	/**
	 * 
	 * @param embeddedStyleSheets
	 * @param linkedStyleSheets
	 */
	public Map<String, Style> parseStyles(Elements externalStyleSheets, Elements embeddedStyleSheets)
	{
		processEmbeddedStyles(embeddedStyleSheets);
		//processExternalStyles(externalStyleSheets);

		return styles;
	}

	/**
	 * 
	 * @param embeddedStyleSheets
	 */
	protected void processEmbeddedStyles(Elements embeddedStyleSheets)
	{
		for (Element element : embeddedStyleSheets)
		{
			try
			{
				List<Rule> rules = CSSParser.parse(element.data());
				mapStyles(rules);
			}
			catch (Exception e)
			{
				throw new RuntimeException(e);
			}
		}
	}

	/**
	 * 
	 * @param externalStyleSheets
	 */
	protected void processExternalStyles(Elements externalStyleSheets)
	{
		for (Element element : externalStyleSheets)
		{
			try
			{
				String styleSheet = externalStyleSheetLoader.loadStyleSheet(getUrl(element));
				List<Rule> rules = CSSParser.parse(styleSheet);
				mapStyles(rules);
			}
			catch (Exception e)
			{
				throw new RuntimeException(e);
			}
		}
	}

	/**
	 * 
	 * @param rules
	 */
	protected void mapStyles(List<Rule> rules)
	{
		for (Rule rule : rules)
		{
			for (Selector selector : rule.getSelectors())
			{
				Style style = generator.createStyle(rule, selector);

				if (styles.containsKey(selector.toString()))
				{
					Style existing = styles.get(selector.toString());
					Style merged = StyleMerger.mergeStyles(existing, style);
					styles.put(selector.toString(), merged);
				}
				else
				{
					styles.put(selector.toString(), style);
				}
			}
		}
	}
	
	/**
	 * 
	 * @param element
	 * @return
	 */
	protected String getUrl(Element element)
	{
		return element.attr(HREF_ATTRIBUTE_KEY);
	}
}

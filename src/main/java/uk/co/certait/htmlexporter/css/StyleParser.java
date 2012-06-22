package uk.co.certait.htmlexporter.css;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.osbcp.cssparser.CSSParser;
import com.osbcp.cssparser.Rule;
import com.osbcp.cssparser.Selector;

public class StyleParser
{
	private StyleGenerator generator;
	
	public StyleParser()
	{
		generator = new StyleGenerator();
	}
	
	public Map<String, Style> parseStyles(Elements elements)
	{
		Map<String, Style> styles = null;
		
		for (Element element : elements)
		{
			try
			{
				List<Rule> rules = CSSParser.parse(element.data());
				styles = mapStyles(rules);
			}
			catch (Exception e)
			{
				throw new RuntimeException(e);
			}
		}
		
		return styles;
	}
	
	protected Map<String, Style> mapStyles(List<Rule> rules)
	{
		Map<String, Style> styles = new HashMap<String, Style>();
		
		for (Rule rule : rules)
		{
			for (Selector selector : rule.getSelectors())
			{
				Style style = generator.createStyle(rule, selector);

				if (styles.containsKey(selector.toString()))
				{
					Style merged = StyleMerger.mergeStyles(styles.get(selector.toString()), style);
					styles.put(selector.toString(), merged);
				}
				else
				{
					styles.put(selector.toString(), style);
				}
			}
		}

		return styles;
	}
}

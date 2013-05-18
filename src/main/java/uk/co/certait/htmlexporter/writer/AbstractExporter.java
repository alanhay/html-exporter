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
package uk.co.certait.htmlexporter.writer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import uk.co.certait.htmlexporter.css.DefaultExternalStyleSheetLoader;
import uk.co.certait.htmlexporter.css.StyleMap;
import uk.co.certait.htmlexporter.css.StyleParser;

public abstract class AbstractExporter implements Exporter
{
	private static final String MEDIA_ATTRIBUTE_KEY = "media";
	private static final String TYPE_ATTRIBUTE_KEY = "type";
	private static final String SCREEN_MEDIA_TYPE = "screen";
	private static final String MEDIA_TYPE_ALL = "all";
	private static final String TEXT_CSS_TYPE = "text/css";
	private static final String TABLE_ELEMENT = "table";
	private static final String STYLESHEET_ELEMENT = "style";
	private static final String LINK_ELEMENT = "link";

	public byte[] exportHtml(String html) throws IOException
	{
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		exportHtml(html, out);

		return out.toByteArray();
	}

	public void exportHtml(String html, File outputFile) throws IOException
	{
		FileOutputStream out = new FileOutputStream(outputFile);
		exportHtml(html, out);
	}

	protected Elements getTables(String html)
	{
		Document document = Jsoup.parse(html);

		return document.getElementsByTag(TABLE_ELEMENT);
	}

	protected StyleMap getStyleMapper(String html)
	{
		Document document = Jsoup.parse(html);
		Elements embeddedStyleSheets = document.getElementsByTag(STYLESHEET_ELEMENT);
		
		Elements externalResources = document.getElementsByTag(LINK_ELEMENT);
		Elements externalStyleSheets = new Elements();
		
		for(Element externalResource : externalResources)
		{
			if(isStyleSheetLink(externalResource))
			{
				externalStyleSheets.add(externalResource);
			}
		} 

		StyleParser parser = new StyleParser(new DefaultExternalStyleSheetLoader(""));
		StyleMap mapper = new StyleMap(parser.parseStyles(externalStyleSheets, embeddedStyleSheets));

		return mapper;
	}

	/**
	 * 
	 * @param element
	 * 
	 * @return
	 */
	protected boolean isStyleSheetLink(Element element)
	{
		boolean isStyleSheetLink = false;

		if (element.hasAttr(TYPE_ATTRIBUTE_KEY) && element.attr(TYPE_ATTRIBUTE_KEY).toLowerCase().equals(TEXT_CSS_TYPE))
		{
			isStyleSheetLink = styleSheetAppliesToScreen(element);
		}

		return isStyleSheetLink;
	}
	
	/**
	 * 
	 * @param element
	 * 
	 * @return
	 */
	protected boolean styleSheetAppliesToScreen(Element element)
	{
		//boolean screenMedia = false;
		
		if (element.hasAttr(MEDIA_ATTRIBUTE_KEY))
		{
			String [] mediaTypes = element.attr(MEDIA_ATTRIBUTE_KEY).toLowerCase().split(",");
			
			for(String mediaType : mediaTypes)
			{
				if(mediaType.equals(SCREEN_MEDIA_TYPE) || mediaType.equals(MEDIA_TYPE_ALL))
				{
					//screenMedia = true;
					break;
				}
			}
		}	
		
		//return screenMedia;
		return true;
	}

	public abstract void exportHtml(String html, OutputStream out) throws IOException;
}

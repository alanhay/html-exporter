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

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.jsoup.Jsoup;
import org.jsoup.nodes.DataNode;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import uk.co.certait.htmlexporter.css.StyleMap;
import uk.co.certait.htmlexporter.css.StyleParser;

public abstract class AbstractExporter implements Exporter {
	protected String datePattern = "yyyy-MM-dd";

	private static final String DATA_NEW_SHEET_ATTRIBUTE = "data-new-sheet";
	private static final String DATA_SHEET_NAME_ATTRIBUTE = "data-sheet-name";

	// Optional resource loader to load external CSS files
	protected IResourceLoader resourceLoader;

	protected Document document;

	protected Document parse(String html) {
		document = Jsoup.parse(html);
		return document;
	}

	protected Document getDocument() {
		return document;
	}

	public byte[] exportHtml(String html) throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		exportHtml(html, out);

		return out.toByteArray();
	}

	public void exportHtml(String html, File outputFile) throws IOException {
		FileOutputStream out = new FileOutputStream(outputFile);
		exportHtml(html, out);
	}

	public void setDatePattern(String datePattern) {
		this.datePattern = datePattern;
	}

	public String getDatePattern() {
		return this.datePattern;
	}

	protected Elements getTables() {
		Document document = getDocument();
		return document.getElementsByTag("table");
	}

	protected StyleMap getStyleMapper() {
		Document document = getDocument();
		Elements styles = document.getElementsByTag("style");

		// Load any external .css files specified in the <head> section as <link .. > tags
		// and add the classes to the styles collection
		// NOTE: we ignore any external CSS links not defined in the <head> section.
		// This will add the styles from any external .css files as children of the styles collection.
		Elements headElements = document.getElementsByTag("head");

		// The linked stylesheets are in the head and are assumed to fall prior to any inline style elements
		// so they are inserted at the start in the order they appear in
		int index = 0;

		for(Element head: headElements)
		{
			for (Element link : head.getElementsByTag("link"))
			{
				String rel = link.attr("rel");
				if (rel != null && rel.equals("stylesheet"))
				{
					if (resourceLoader == null)
						throw new RuntimeException("Attempt to load contents of external .css file but no resource loader configured");

					Element styleElement = new Element("style");

					String uri = link.attr("href");
					String cssContent = resourceLoader.loadResourceContent(uri);
					DataNode styleContent = new DataNode(cssContent);
					styleElement.appendChild(styleContent);
					styles.add(index++, styleElement);
				}
			}
		}

		StyleParser parser = new StyleParser();
		StyleMap mapper = new StyleMap(parser.parseStyleSheets(styles));

		return mapper;
	}

	protected boolean isNewSheet(Element element) {
		return Boolean.valueOf(element.attr(DATA_NEW_SHEET_ATTRIBUTE));
	}

	protected String getSheetName(Element element) {
		return element.attr(DATA_SHEET_NAME_ATTRIBUTE);
	}

	public abstract void exportHtml(String html, OutputStream out) throws IOException;

	public void setResourceLoader(IResourceLoader iResourceLoader) {
		resourceLoader = iResourceLoader;
	}
}

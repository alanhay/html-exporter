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
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import uk.co.certait.htmlexporter.css.StyleMap;
import uk.co.certait.htmlexporter.css.StyleParser;

public abstract class AbstractExporter implements Exporter {

	private static final String DATA_NEW_SHEET_ATTRIBUTE = "data-new-sheet";
	private static final String DATA_SHEET_NAME_ATTRIBUTE = "data-sheet-name";

	public byte[] exportHtml(String html) throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		exportHtml(html, out);

		return out.toByteArray();
	}

	public void exportHtml(String html, File outputFile) throws IOException {
		FileOutputStream out = new FileOutputStream(outputFile);
		exportHtml(html, out);
	}

	protected Elements getTables(String html) {
		Document document = Jsoup.parse(html);// FIXME parsing twice

		return document.getElementsByTag("table");
	}

	protected StyleMap getStyleMapper(String html) {
		Document document = Jsoup.parse(html);
		Elements styles = document.getElementsByTag("style");// FIXME parsing
																// twice

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
}

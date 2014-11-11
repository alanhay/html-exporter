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
package uk.co.certait.htmlexporter.demo;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.net.URL;
import java.util.Properties;

import org.apache.commons.io.IOUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import uk.co.certait.htmlexporter.writer.excel.ExcelExporter;
import uk.co.certait.htmlexporter.writer.ods.OdsExporter;

public class DemoTwo {
	public DemoTwo() throws Exception {
		Document document = Jsoup.parse(new URL("http://news.bbc.co.uk/sport1/hi/football/eng_prem/table/8102708.stm"),
				10000);
		Elements elements = document.getElementsByClass("fulltable");

		String table = null;

		for (Element element : elements) {
			table = element.toString();
		}

		String html = generateHTML(table);
		saveFile("league.html", html.getBytes());

		new ExcelExporter().exportHtml(html, new File("./league.xlsx"));
		new OdsExporter().exportHtml(html, new File("./league.ods"));
	}

	public static void main(String[] args) throws Exception {
		new DemoTwo();

		System.exit(0);
	}

	public String generateHTML(String content) {
		Properties props = new Properties();
		props.put("resource.loader", "class");
		props.put("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");

		Velocity.init(props);
		Template template = Velocity.getTemplate("league.vm");

		VelocityContext context = new VelocityContext();
		context.put("content", content);
		Writer writer = new StringWriter();
		template.merge(context, writer);

		return writer.toString();
	}

	public void saveFile(String fileName, byte[] data) throws IOException {
		File file = new File(fileName);
		FileOutputStream out = new FileOutputStream(file);
		IOUtils.write(data, out);
	}
}

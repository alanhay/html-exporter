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
import java.io.IOException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.io.IOUtils;

import uk.co.certait.htmlexporter.writer.excel.ExcelExporter;
import uk.co.certait.htmlexporter.writer.ods.OdsExporter;

public class ExternalGenerator
{
	public ExternalGenerator() throws Exception
	{
		String html = loadHTML("http://www.bbc.co.uk/sport/football/tables");

		new ExcelExporter().exportHtml(html, new File("./league.xlsx"));
		new OdsExporter().exportHtml(html, new File("./league.ods"));
		// new PdfExporter().exportHtml(html, new File("./report.pdf"));
	}

	public static void main(String[] args) throws Exception
	{
		new ExternalGenerator();

		System.exit(0);
	}

	public String loadHTML(String url) throws IOException
	{
		HttpClient client = new HttpClient();
		HttpMethod method = new GetMethod(url);
		client.executeMethod(method);
		String html = IOUtils.toString(method.getResponseBodyAsStream());
		method.releaseConnection();

		return html;
	}
}

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

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.RandomUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import uk.co.certait.htmlexporter.demo.domain.*;
import uk.co.certait.htmlexporter.pdf.PdfExporter;
import uk.co.certait.htmlexporter.writer.IResourceLoader;
import uk.co.certait.htmlexporter.writer.excel.ExcelExporter;
import uk.co.certait.htmlexporter.writer.ods.OdsExporter;

import java.io.*;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Properties;

public class ReportGeneratorExternalCss
{
	public ReportGeneratorExternalCss() throws Exception {
		File directory = new File(System.getProperty("user.home") + "/html-exporter");

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yy-HH-mm-ss");
		String timestamp = LocalDateTime.now().format(formatter);

		if (!directory.exists()) {
			directory.mkdirs();
		}

		LocalDateTime.now();

		String html = generateHTML("report-external-css.vm");
		saveFile(directory, "report-external-css" + timestamp + ".html", html.getBytes());

		String relativeResourceBase = "src/main/resources";
		String resourceBase = "/my/css/file/directory/styles";  // This could be something absolute like a repository of css files

		ExcelExporter excelExporter = new ExcelExporter();

		IResourceLoader resourceLoader = new IResourceLoader()
		{
			@Override
			public String loadResourceContent(String uri)
			{
				String filename;
				if (uri.startsWith("/"))
					filename = resourceBase + File.separatorChar + uri;
				else
					filename = relativeResourceBase + File.separatorChar + uri;

				try (FileReader fileReader = new FileReader(filename))
				{
					int bufSize = 1024;
					char[] buf = new char[bufSize];
					StringBuilder sb = new StringBuilder();
					int offset = 0;


					int charsRead = 0;
					while (charsRead != -1)
					{
						charsRead = fileReader.read(buf, offset, bufSize);
						if (charsRead != -1)
							sb.append(buf, 0, charsRead);
					}
					return sb.toString();
				}
				catch (IOException ioe)
				{
					throw new RuntimeException("loadResourceContent failed: " + ioe);
				}
			}
		};

		excelExporter.setResourceLoader(resourceLoader);

		excelExporter.exportHtml(html, new File(directory, "report-external-css" + timestamp + ".xlsx"));

//		new PdfExporter().exportHtml(html, new File(directory, "report-" + timestamp + ".pdf"));
//		new OdsExporter().exportHtml(html, new File(directory, "report-" + timestamp + ".ods"));
	}

	public static void main(String[] args) throws Exception {
		new ReportGeneratorExternalCss();

		System.exit(0);
	}

	public String generateHTML(String templateName) {
		Properties props = new Properties();
		props.put("resource.loader", "class");
		props.put("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");

		Velocity.init(props);
		Template template = Velocity.getTemplate(templateName);

		VelocityContext context = new VelocityContext();
		context.put("numberFormatter", NumberFormat.getInstance(Locale.UK));
		context.put("data", generateData());
		context.put("productGroups", ProductGroup.values());
		Writer writer = new StringWriter();
		template.merge(context, writer);

		return writer.toString();
	}

	public SalesReportData generateData() {
		SalesReportData data = new SalesReportData();

		String[] areaNames = { "North", "South", "East", "West" };
		String[][] regionNames = { { "Grampian", "Highland" }, { "Borders", "Dumfries" },
				{ "Fife", "Lothian", "Tayside" }, { "Argyll", "Ayrshire", "Glasgow" } };

		for (int i = 0; i < areaNames.length; ++i) {
			Area area = new Area(i, areaNames[i]);

			int storeCount = RandomUtils.nextInt(1, 2) + 2;

			for (int j = 0; j < regionNames[i].length; ++j) {
				Region region = new Region(i + "_" + j, regionNames[i][j]);
				area.addRegion(region);

				for (int k = 0; k < storeCount; ++k) {
					Store store = new Store(region.getName() + "_" + (k + 1), region.getName() + " Store " + (k + 1));
					region.addStore(store);

					for (ProductGroup group : ProductGroup.values()) {
						int saleCount = RandomUtils.nextInt(1, 50);

						for (int m = 0; m < saleCount; ++m) {
							int value = RandomUtils.nextInt(1, 100) + 10;
							store.addSale(new Sale(group, new BigDecimal(Integer.toString(value))));
						}
					}
				}
			}

			data.addArea(area);
		}

		return data;
	}

	public void saveFile(File directory, String fileName, byte[] data) throws IOException {
		File file = new File(directory, fileName);
		FileOutputStream out = new FileOutputStream(file);
		IOUtils.write(data, out);
		out.flush();
		out.close();
	}
}

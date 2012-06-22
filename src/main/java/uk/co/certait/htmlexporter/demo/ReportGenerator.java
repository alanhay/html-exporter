package uk.co.certait.htmlexporter.demo;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.math.BigDecimal;
import java.util.Properties;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

import uk.co.certait.htmlexporter.demo.domain.Area;
import uk.co.certait.htmlexporter.demo.domain.ProductGroup;
import uk.co.certait.htmlexporter.demo.domain.Sale;
import uk.co.certait.htmlexporter.demo.domain.SalesReportData;
import uk.co.certait.htmlexporter.demo.domain.Store;
import uk.co.certait.htmlexporter.writer.excel.ExcelExporter;
import uk.co.certait.htmlexporter.writer.ods.OdsExporter;

public class ReportGenerator
{
	public ReportGenerator() throws Exception
	{
		String html = generateHTML();
		saveFile("report.html", html.getBytes());
		
		new ExcelExporter().exportHtml(html, new File("./report.xlsx"));
		new OdsExporter().exportHtml(html, new File("./report.ods"));
	}

	public static void main(String[] args) throws Exception
	{
		new ReportGenerator();

		System.exit(0);
	}

	public String generateHTML()
	{
		Properties props = new Properties();
		props.put("resource.loader", "class");
		props.put("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");

		Velocity.init(props);
		Template template = Velocity.getTemplate("report.vm");

		VelocityContext context = new VelocityContext();
		context.put("data", generateData());
		context.put("productGroups", ProductGroup.values());
		Writer writer = new StringWriter();
		template.merge(context, writer);

		return writer.toString();
	}

	public SalesReportData generateData()
	{
		SalesReportData data = new SalesReportData();

		String[] areaNames = { "North", "South", "East", "West" };

		for (int i = 0; i < areaNames.length; ++i)
		{
			Area area = new Area(i, areaNames[i]);

			int storeCount = RandomUtils.nextInt(8) + 2;

			for (int j = 0; j < storeCount; ++j)
			{
				Store store = new Store(area.getName() + "_" + (j + 1), area.getName() + " Store " + (j + 1));
				area.addStore(store);

				for (ProductGroup group : ProductGroup.values())
				{
					int saleCount = RandomUtils.nextInt(50);

					for (int k = 0; k < saleCount; ++k)
					{
						int value = RandomUtils.nextInt(100) + 10;
						store.addSale(new Sale(group, new BigDecimal(Integer.toString(value))));
					}
				}
			}

			data.addArea(area);
		}

		return data;
	}

	public void saveFile(String fileName, byte[] data) throws IOException
	{
		File file = new File(fileName);
		FileOutputStream out = new FileOutputStream(file);
		IOUtils.write(data, out);
	}
}

package uk.co.certait.htmlexporter.writer.excel;

import java.io.IOException;
import java.io.OutputStream;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jsoup.nodes.Element;

import uk.co.certait.htmlexporter.css.StyleMapper;
import uk.co.certait.htmlexporter.writer.AbstractExporter;
import uk.co.certait.htmlexporter.writer.TableWriter;

public class ExcelExporter extends AbstractExporter
{	
	public void exportHtml(String html, OutputStream out) throws IOException
	{
		Workbook workbook = new XSSFWorkbook();
		Sheet sheet = workbook.createSheet();
		
		StyleMapper styleMapper = getStyleMapper(html);
		
		TableWriter writer = new ExcelTableWriter(new ExcelTableRowWriter(sheet, new ExcelTableCellWriter(sheet, styleMapper)));

		int startRow = 0;
		
		for(Element element : getTables(html))
		{
			startRow += writer.writeTable(element, styleMapper, startRow) + 1;
			sheet.createRow(startRow);
		}

		workbook.write(out);
		out.flush();
		out.close();
	}
}

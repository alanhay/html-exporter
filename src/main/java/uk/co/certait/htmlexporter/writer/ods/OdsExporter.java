package uk.co.certait.htmlexporter.writer.ods;

import java.io.IOException;
import java.io.OutputStream;

import org.jsoup.nodes.Element;
import org.odftoolkit.simple.SpreadsheetDocument;
import org.odftoolkit.simple.table.Table;

import uk.co.certait.htmlexporter.css.StyleMapper;
import uk.co.certait.htmlexporter.writer.AbstractExporter;
import uk.co.certait.htmlexporter.writer.TableWriter;

public class OdsExporter extends AbstractExporter
{
	public void exportHtml(String html, OutputStream out) throws IOException
	{
		SpreadsheetDocument spreadsheet = null;

		try
		{
			spreadsheet = SpreadsheetDocument.newSpreadsheetDocument();
			Table table = spreadsheet.getSheetByIndex(0);

			StyleMapper styleMapper = getStyleMapper(html);

			TableWriter writer = new OdsTableWriter(new OdsTableRowWriter(table, new OdsTableCellWriter(table,
					styleMapper)));

			int startRow = 0;

			for (Element element : getTables(html))
			{
				startRow += writer.writeTable(element, styleMapper, startRow) + 1;
				table.appendRow();
			}

			spreadsheet.save(out);

		}
		catch (Exception ex)
		{
			throw new IOException(ex);
		}
	}
}

package uk.co.certait.htmlexporter.writer.excel;

import org.jsoup.nodes.Element;

import uk.co.certait.htmlexporter.writer.AbstractTableWriter;
import uk.co.certait.htmlexporter.writer.TableRowWriter;

public class ExcelTableWriter extends AbstractTableWriter
{
	public ExcelTableWriter(TableRowWriter rowWriter)
	{
		super(rowWriter);
	}
	
	public void renderTable(Element table)
	{
		//nothing required for Excel
	}
}

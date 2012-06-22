package uk.co.certait.htmlexporter.writer.ods;

import org.jsoup.nodes.Element;
import org.odftoolkit.simple.table.Table;

import uk.co.certait.htmlexporter.writer.AbstractTableRowWriter;
import uk.co.certait.htmlexporter.writer.TableCellWriter;

public class OdsTableRowWriter extends AbstractTableRowWriter
{
	private Table table;
	
	public OdsTableRowWriter(Table table, TableCellWriter cellWriter)
	{
		super(cellWriter);
		
		this.table = table;
	}

	@Override
	public void renderRow(Element row, int rowIndex)
	{
		table.appendRow();
	}
}

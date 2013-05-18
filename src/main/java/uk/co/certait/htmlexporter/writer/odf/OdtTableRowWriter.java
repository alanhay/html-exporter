package uk.co.certait.htmlexporter.writer.odf;

import org.jsoup.nodes.Element;
import org.odftoolkit.simple.table.Table;

import uk.co.certait.htmlexporter.writer.AbstractTableRowWriter;
import uk.co.certait.htmlexporter.writer.TableCellWriter;

public class OdtTableRowWriter extends AbstractTableRowWriter
{
	private Table table;
	
	public OdtTableRowWriter(Table table, TableCellWriter cellWriter)
	{
		super(cellWriter);
		
		this.table = table;
	}

	@Override
	public void renderRow(Element row, int rowIndex)
	{
		if(table.getRowCount() < rowIndex)
		{
			table.appendRow();
		}
	}

	@Override
	public void doMerge(int rowIndex, int columnIndex, int rowSpan, int columnSpan)
	{
		org.odftoolkit.simple.table.CellRange cr = table.getCellRangeByPosition(columnIndex,
				rowIndex, columnIndex + columnSpan - 1, rowIndex + rowSpan - 1);
		
		cr.merge();
		
	}
}

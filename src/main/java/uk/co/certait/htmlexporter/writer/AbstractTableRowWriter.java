package uk.co.certait.htmlexporter.writer;

import org.jsoup.nodes.Element;

public abstract class AbstractTableRowWriter implements TableRowWriter
{
	private TableCellWriter cellWriter;
	
	public AbstractTableRowWriter(TableCellWriter cellWriter)
	{
		this.cellWriter = cellWriter;
	}
	
	public void writeRow(Element row, int rowIndex)
	{
		renderRow(row, rowIndex);
		
		int columnIndex = 0;
		
		for(Element element : row.getAllElements())
		{
			if(element.tag().getName().equals(TD_TAG) || element.tag().getName().equals(TH_TAG))
			{
				columnIndex += cellWriter.writeCell(element, rowIndex, columnIndex);
			}
		}
	}
	
	public abstract void renderRow(Element row, int rowIndex);
}

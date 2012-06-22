package uk.co.certait.htmlexporter.writer;

import org.jsoup.nodes.Element;

import uk.co.certait.htmlexporter.css.StyleMapper;

public abstract class AbstractTableWriter implements TableWriter
{
	private TableRowWriter rowWriter;
	
	public AbstractTableWriter(TableRowWriter rowWriter)
	{
		this.rowWriter = rowWriter;
	}
	
	@Override
	public int writeTable(Element table, StyleMapper styleMapper, int startRow)
	{
		renderTable(table);
		
		int rowIndex = startRow;
		
		for(Element element : table.getElementsByTag(TABLE_ROW_ELEMENT_NAME))
		{
			rowWriter.writeRow(element, rowIndex);
			++ rowIndex;
		}
		
		return rowIndex;
	}
	
	public abstract void renderTable(Element table);
}

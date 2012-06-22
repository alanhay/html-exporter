package uk.co.certait.htmlexporter.writer;

import org.jsoup.nodes.Element;

public interface TableCellWriter
{
	public static final String COLUMN_SPAN_ATTRIBUTE = "colspan";
	public static final String DATA_GROUP_ATTRIBUTE = "data-group";
	public static final String DATA_GROUP_OUTPUT_ATTRIBUTE = "data-group-output";
	
	public int writeCell(Element cell, int row, int column);
}

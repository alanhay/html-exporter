package uk.co.certait.htmlexporter.writer;

import org.jsoup.nodes.Element;

import uk.co.certait.htmlexporter.css.StyleMapper;

public interface TableWriter
{
	public static final String TABLE_ROW_ELEMENT_NAME = "tr";
	
	public int writeTable(Element table, StyleMapper styleMapper, int startRow);
}

package uk.co.certait.htmlexporter.writer;

import org.jsoup.nodes.Element;

public interface TableRowWriter
{
	public static final String TD_TAG = "td";
	public static final String TH_TAG = "th";
	
	public void writeRow(Element element, int row);
}

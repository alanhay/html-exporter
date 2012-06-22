package uk.co.certait.htmlexporter.writer.ods;

import org.jsoup.nodes.Element;

import uk.co.certait.htmlexporter.writer.AbstractTableWriter;
import uk.co.certait.htmlexporter.writer.TableRowWriter;

public class OdsTableWriter extends AbstractTableWriter
{
	public OdsTableWriter(TableRowWriter rowWriter)
	{
		super(rowWriter);
	}

	@Override
	public void renderTable(Element table)
	{
		// nothing required
	}
}

package uk.co.certait.htmlexporter.writer.ods;

import uk.co.certait.htmlexporter.writer.AbstractCellRangeResolver;

public class OdsCellRangeResolver extends AbstractCellRangeResolver
{
	@Override
	public String getCellSeparator()
	{
		return ";";
	}
}

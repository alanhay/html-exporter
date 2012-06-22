package uk.co.certait.htmlexporter.writer.excel;

import uk.co.certait.htmlexporter.writer.AbstractCellRangeResolver;

public class ExcelCellRangeResolver extends AbstractCellRangeResolver
{
	@Override
	public String getCellSeparator()
	{
		return ",";
	}
}

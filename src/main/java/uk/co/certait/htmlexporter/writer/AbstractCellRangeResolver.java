package uk.co.certait.htmlexporter.writer;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.util.CellReference;

import uk.co.certait.htmlexporter.ss.CellRange;
import uk.co.certait.htmlexporter.ss.CellRangeResolver;
import uk.co.certait.htmlexporter.ss.CellRangeRow;
import uk.co.certait.htmlexporter.ss.TableCellReference;

public abstract class AbstractCellRangeResolver implements CellRangeResolver
{
	public String getRangeString(CellRange range)
	{
		String rangeString;
		
		if(range.isContiguous())
		{
			rangeString = buildContiguousString(range);
		}
		else
		{
			rangeString = buildNonContigousString(range);
		}
		
		return rangeString;
	}
	
	private String buildContiguousString(CellRange range)
	{
		TableCellReference firstCell = range.getFirstCell();
		CellReference first = new CellReference(firstCell.getRowIndex(), firstCell.getColumnIndex());
		
		TableCellReference lastCell = range.getLastCell();
		CellReference last = new CellReference(lastCell.getRowIndex(), lastCell.getColumnIndex());
		
		return first.formatAsString() + ":" + last.formatAsString();
	}
	
	private String buildNonContigousString(CellRange range)
	{
		StringBuilder builder = new StringBuilder();
		
		for(CellRangeRow row : range.getRows())
		{
			for(TableCellReference cell : row.getCells())
			{
				if(cell != null)
				{
					builder.append(new CellReference(cell.getRowIndex(), cell.getColumnIndex()).formatAsString()).append(getCellSeparator());
				}
			}
		}
		
		return StringUtils.removeEnd(builder.toString(), getCellSeparator());
	}
	
	public abstract String getCellSeparator();
}

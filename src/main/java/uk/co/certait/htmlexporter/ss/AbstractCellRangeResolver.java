/**
 * Copyright (C) 2012 alanhay <alanhay99@hotmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package uk.co.certait.htmlexporter.ss;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.util.CellReference;


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

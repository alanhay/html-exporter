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
package uk.co.certait.htmlexporter.writer.ods;

import org.jsoup.nodes.Element;
import org.odftoolkit.simple.table.Table;

import uk.co.certait.htmlexporter.writer.AbstractTableRowWriter;
import uk.co.certait.htmlexporter.writer.TableCellWriter;

public class OdsTableRowWriter extends AbstractTableRowWriter
{
	private Table table;
	
	public OdsTableRowWriter(Table table, TableCellWriter cellWriter)
	{
		super(cellWriter);
		
		this.table = table;
	}

	@Override
	public void renderRow(Element row, int rowIndex)
	{
		//actually nothing required for ods
	}

	@Override
	public void doMerge(int rowIndex, int columnIndex, int rowSpan, int columnSpan)
	{
		org.odftoolkit.simple.table.CellRange cr = table.getCellRangeByPosition(columnIndex,
				rowIndex, columnIndex + columnSpan - 1, rowIndex + rowSpan - 1);
		
		cr.merge();
	}
}

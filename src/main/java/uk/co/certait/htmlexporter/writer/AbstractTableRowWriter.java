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
package uk.co.certait.htmlexporter.writer;

import org.jsoup.nodes.Element;

public abstract class AbstractTableRowWriter implements TableRowWriter
{
	private TableCellWriter cellWriter;
	
	public AbstractTableRowWriter(TableCellWriter cellWriter)
	{
		this.cellWriter = cellWriter;
	}
	
	public void writeRow(Element row, int rowIndex)
	{
		renderRow(row, rowIndex);
		
		int columnIndex = 0;
		
		for(Element element : row.getAllElements())
		{
			if(element.tag().getName().equals(TD_TAG) || element.tag().getName().equals(TH_TAG))
			{
				columnIndex += cellWriter.writeCell(element, rowIndex, columnIndex);
			}
		}
	}
	
	public abstract void renderRow(Element row, int rowIndex);
}

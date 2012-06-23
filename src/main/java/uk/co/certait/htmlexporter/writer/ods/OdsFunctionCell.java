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


import org.odftoolkit.simple.table.Cell;

import uk.co.certait.htmlexporter.ss.CellRange;
import uk.co.certait.htmlexporter.ss.CellRangeObserver;
import uk.co.certait.htmlexporter.ss.CellRangeResolver;
import uk.co.certait.htmlexporter.ss.Function;

public class OdsFunctionCell implements CellRangeObserver
{
	private Cell cell;
	private CellRangeResolver resolver;
	private Function function;

	public OdsFunctionCell(Cell cell, CellRange range, CellRangeResolver resolver, Function function)
	{
		this.cell = cell;
		this.resolver = resolver;
		this.function = function;
		
		range.addCellRangeObserver(this);
		
		cellRangeUpdated(range);
	}

	public void cellRangeUpdated(CellRange range)
	{
		cell.setFormula("=" + function.toString()+ "(" + resolver.getRangeString(range) + ")");
	}
}

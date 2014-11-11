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
import org.odftoolkit.simple.table.Cell;
import org.odftoolkit.simple.table.Table;

import uk.co.certait.htmlexporter.css.Style;
import uk.co.certait.htmlexporter.css.StyleMap;
import uk.co.certait.htmlexporter.ss.CellRange;
import uk.co.certait.htmlexporter.ss.Function;
import uk.co.certait.htmlexporter.writer.AbstractTableCellWriter;

public class OdsTableCellWriter extends AbstractTableCellWriter {
	private Table table;
	private StyleMap styleMapper;
	private OdsStyleGenerator styleGenerator;

	public OdsTableCellWriter(Table table, StyleMap styleMapper) {
		this.table = table;
		this.styleMapper = styleMapper;
		styleGenerator = new OdsStyleGenerator();
	}

	@Override
	public void renderCell(Element element, int rowIndex, int columnIndex) {
		Cell cell = table.getCellByPosition(columnIndex, rowIndex);

		Double numericValue = null;

		if ((numericValue = getNumericValue(element)) != null) {
			cell.setDoubleValue(numericValue);
		} else {
			cell.setStringValue(getElementText(element));
		}

		Style style = styleMapper.getStyleForElement(element);
		styleGenerator.styleCell(cell, style);

		String commentText;

		if ((commentText = getCellCommentText(element)) != null) {
			cell.setNoteText(commentText);
		}
	}

	@Override
	public void addFunctionCell(int rowIndex, int columnIndex, CellRange range, Function function) {
		Cell cell = table.getCellByPosition(columnIndex, rowIndex);

		new OdsFunctionCell(cell, range, new OdsCellRangeResolver(), function);
	}
}

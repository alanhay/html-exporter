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
package uk.co.certait.htmlexporter.writer.excel;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.jsoup.nodes.Element;

import uk.co.certait.htmlexporter.css.Style;
import uk.co.certait.htmlexporter.css.StyleMap;
import uk.co.certait.htmlexporter.writer.AbstractTableRowWriter;
import uk.co.certait.htmlexporter.writer.TableCellWriter;

public class ExcelTableRowWriter extends AbstractTableRowWriter {
	private Sheet sheet;

	public ExcelTableRowWriter(Sheet sheet, TableCellWriter cellWriter) {
		super(cellWriter);

		this.sheet = sheet;
	}

	@Override
	public void renderRow(Element row, int rowIndex) {
		// row may already have been created as a result of a merge operation.
		if (sheet.getRow(rowIndex) == null) {
			sheet.createRow(rowIndex);
		}
	}

	public void doMerge(int rowIndex, int columnIndex, int rowSpan, int columnSpan) {
		XSSFCell cell = (XSSFCell) sheet.getRow(rowIndex).getCell(columnIndex);
		CellRangeAddress range = new CellRangeAddress(rowIndex, rowIndex + rowSpan - 1, columnIndex,
				columnIndex + columnSpan - 1);

		// The code here previously relied on RegionUtils.(...) to set the border for
		// the merged range however that does not seem to have been updated to handle
		// the new XssfColor model and borders in merged regions are therefore always
		// rendered as black.
		// In order to have the borders for a merged region correctly rendered, it looks
		// like we first have to set the border on each individual cell before calling
		// the merge operation which seems to be broadly what RegionUtils was doing.
		// Here we just copy the entire style object from the first cell to the other
		// cells as there does not seem to be a case when that is not what we would
		// want.

		for (int row = rowIndex; row < rowIndex + rowSpan; ++row) {
			if (sheet.getRow(row) == null) {
				sheet.createRow(row);
			}

			for (int col = columnIndex; col < columnIndex + columnSpan; ++col) {
				XSSFCell nextCell = (XSSFCell) sheet.getRow(row).getCell(col);

				if (nextCell == null) {
					nextCell = (XSSFCell) sheet.getRow(row).createCell(col);
				}
				nextCell.setCellStyle(cell.getCellStyle());
			}
		}

		sheet.addMergedRegion(range);
	}
}

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

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
import org.jsoup.nodes.Element;

import uk.co.certait.htmlexporter.css.Style;
import uk.co.certait.htmlexporter.css.StyleMapper;
import uk.co.certait.htmlexporter.ss.CellRange;
import uk.co.certait.htmlexporter.ss.Function;
import uk.co.certait.htmlexporter.writer.AbstractTableCellWriter;

public class ExcelTableCellWriter extends AbstractTableCellWriter
{
	private Sheet sheet;
	private StyleMapper styleMapper;
	private ExcelStyleGenerator styleGenerator;

	public ExcelTableCellWriter(Sheet sheet, StyleMapper styleMapper)
	{
		this.sheet = sheet;
		this.styleMapper = styleMapper;

		styleGenerator = new ExcelStyleGenerator();
	}

	@Override
	public int renderCell(Element element, int rowIndex, int columnIndex)
	{
		int cellsWritten = 1;

		Cell cell = sheet.getRow(rowIndex).createCell(columnIndex);

		Double numericValue;

		if ((numericValue = getNumericValue(element)) != null)
		{
			cell.setCellValue(numericValue);
		}
		else
		{
			cell.setCellValue(getElementText(element));
		}

		Style style = styleMapper.getStyleForElement(element);
		cell.setCellStyle(styleGenerator.getStyle(cell, style));

		if (spansMultipleColumns(element))
		{
			int columnCount = getMergedColumnCount(element) - 1;
			doMerge(sheet, cell, columnCount);

			cellsWritten += columnCount;
		}

		return cellsWritten;
	}

	protected void doMerge(Sheet sheet, Cell cell, int columnCount)
	{
		CellRangeAddress range = new CellRangeAddress(cell.getRowIndex(), cell.getRowIndex(), cell.getColumnIndex(),
				cell.getColumnIndex() + columnCount);

		sheet.addMergedRegion(range);

		RegionUtil.setBorderBottom(cell.getCellStyle().getBorderBottom(), range, sheet, sheet.getWorkbook());
		RegionUtil.setBorderTop(cell.getCellStyle().getBorderTop(), range, sheet, sheet.getWorkbook());
		RegionUtil.setBorderLeft(cell.getCellStyle().getBorderLeft(), range, sheet, sheet.getWorkbook());
		RegionUtil.setBorderRight(cell.getCellStyle().getBorderRight(), range, sheet, sheet.getWorkbook());

		RegionUtil.setBottomBorderColor(cell.getCellStyle().getBottomBorderColor(), range, sheet, sheet.getWorkbook());
		RegionUtil.setTopBorderColor(cell.getCellStyle().getTopBorderColor(), range, sheet, sheet.getWorkbook());
		RegionUtil.setLeftBorderColor(cell.getCellStyle().getLeftBorderColor(), range, sheet, sheet.getWorkbook());
		RegionUtil.setRightBorderColor(cell.getCellStyle().getRightBorderColor(), range, sheet, sheet.getWorkbook());
	}

	public void addFunctionCell(int rowIndex, int columnIndex, CellRange range, Function function)
	{
		Cell cell = sheet.getRow(rowIndex).getCell(columnIndex);

		new ExcelFunctionCell(cell, range, new ExcelCellRangeResolver(), function);
	}
}

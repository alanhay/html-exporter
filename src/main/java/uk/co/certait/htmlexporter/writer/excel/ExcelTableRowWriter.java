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

import uk.co.certait.htmlexporter.writer.AbstractTableRowWriter;
import uk.co.certait.htmlexporter.writer.TableCellWriter;

public class ExcelTableRowWriter extends AbstractTableRowWriter
{
	private Sheet sheet;

	public ExcelTableRowWriter(Sheet sheet, TableCellWriter cellWriter)
	{
		super(cellWriter);
		
		this.sheet = sheet;
	}

	@Override
	public void renderRow(Element row, int rowIndex)
	{
		if(sheet.getRow(rowIndex) == null)
		{
			sheet.createRow(rowIndex);
		}
	}
	
	public void doMerge(int rowIndex, int columnIndex, int rowSpan, int columnSpan)
	{
		Cell cell = sheet.getRow(rowIndex).getCell(columnIndex);
		CellRangeAddress range = new CellRangeAddress(rowIndex, rowIndex + rowSpan - 1, columnIndex, columnIndex + columnSpan - 1);

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
}

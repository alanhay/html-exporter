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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Sheet;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.co.certait.htmlexporter.css.Style;
import uk.co.certait.htmlexporter.css.StyleMap;
import uk.co.certait.htmlexporter.ss.CellRange;
import uk.co.certait.htmlexporter.ss.Function;
import uk.co.certait.htmlexporter.writer.AbstractTableCellWriter;

public class ExcelTableCellWriter extends AbstractTableCellWriter {

	private static final Logger logger = LoggerFactory.getLogger(ExcelTableCellWriter.class);

	private Sheet sheet;
	private StyleMap styleMapper;
	private ExcelStyleGenerator styleGenerator;

	public ExcelTableCellWriter(Sheet sheet, StyleMap styleMapper) {
		this.sheet = sheet;
		this.styleMapper = styleMapper;

		styleGenerator = new ExcelStyleGenerator();
	}

	@Override
	public void renderCell(Element element, int rowIndex, int columnIndex) {
		Cell cell = sheet.getRow(rowIndex).createCell(columnIndex);

		Double numericValue = null;
		String format = null;

		if (isDateCell(element)) {
			DateFormat df = new SimpleDateFormat(getDateCellFormat(element));

			try {
				cell.setCellValue(df.parse(getElementText(element)));
			} catch (ParseException pex) {
				logger.error("Error processing date cell with format specified as {} and value {}",
						getDateCellFormat(element), getElementText(element), pex);
			}
			format = getDateCellFormat(element);
		} else if ((numericValue = getNumericValue(element)) != null) {
			cell.setCellValue(numericValue);
			if (getNumericCellFormat(element) != null) {
				format = getNumericCellFormat(element);
				if (getNumericCellFormat(element).contains("%")) {
					cell.setCellValue(numericValue / 100); // fix wrong excel interpretation of percent formatted values
				}
			}
		} else {
			cell = sheet.getRow(rowIndex).createCell(columnIndex, CellType.STRING);
			cell.setCellValue(getElementText(element));
		}

		Style style = styleMapper.getStyleForElement(element);
		cell.setCellStyle(styleGenerator.getStyle(cell, style,format));

		String commentText;

		if ((commentText = getCellCommentText(element)) != null) {
			ExcelCellCommentGenerator.addCellComment(cell, commentText, getCellCommentDimension(element));
		}

		if (definesFreezePane(element)) {
			sheet.createFreezePane(columnIndex, rowIndex);
		}
	}

	public void addFunctionCell(int rowIndex, int columnIndex, CellRange range, Function function) {
		Cell cell = sheet.getRow(rowIndex).getCell(columnIndex);

		new ExcelFunctionCell(cell, range, new ExcelCellRangeResolver(), function);
	}
	
}

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
import java.util.Date;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Sheet;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.co.certait.htmlexporter.css.*;
import uk.co.certait.htmlexporter.ss.CellRange;
import uk.co.certait.htmlexporter.ss.Function;
import uk.co.certait.htmlexporter.writer.AbstractTableCellWriter;

public class ExcelTableCellWriter extends AbstractTableCellWriter {

	private static final Logger logger = LoggerFactory.getLogger(ExcelTableCellWriter.class);

	private Sheet sheet;
	private StyleMap styleMapper;
	private ExcelStyleGenerator styleGenerator;
	private ExcelStyleGenerator rowStyleGenerator;

	public ExcelTableCellWriter(Sheet sheet, StyleMap styleMapper) {
		this.sheet = sheet;
		this.styleMapper = styleMapper;

		styleGenerator = new ExcelStyleGenerator();
	}

	public void setRowStyleGenerator(ExcelStyleGenerator rowStyleGenerator) {
		this.rowStyleGenerator = rowStyleGenerator;
	}

	public ExcelStyleGenerator getRowStyleGenerator() {
		return rowStyleGenerator;
	}

	@Override
	public void renderCell(Element element, int rowIndex, int columnIndex) {
		Cell cell = sheet.getRow(rowIndex).createCell(columnIndex);

		Double numericValue;
		Date dateValue = null;

		String datePattern = null;
		boolean wrapText = false;
		if (isDateCell(element)) {
			String dateCellFormat = getDateCellFormat(element);
			DateFormat df = new SimpleDateFormat(getDateCellFormat(element));

			try {
				dateValue = df.parse(getElementText(element));
				cell.setCellValue(dateValue);
				datePattern = dateCellFormat;
			} catch(ParseException pex) {
				logger.error("Error processing date cell with format specified as {} and value {}", getDateCellFormat(element), getElementText(element), pex);
			}
		} else if(isPossibleDateCell(element) && (dateValue = getDateValue(element, getDatePattern())) != null) {
			cell.setCellValue(dateValue);
			datePattern = getDatePattern();
		} else if(isPossibleDateTimeCell(element) && (dateValue = getDateTimeValue(element, getDateTimePattern())) != null) {
			cell.setCellValue(dateValue);
			datePattern = toExcelDateTimePattern(getDateTimePattern());
		} else if ((numericValue = getNumericValue(element)) != null) {
			if (isPercentageCell(element)) {
				cell.setCellValue(numericValue / 100);
			} else {
				cell.setCellValue(numericValue);
			}
		} else {
			cell = sheet.getRow(rowIndex).createCell(columnIndex, CellType.STRING);

			String text = getElementText(element);
			cell.setCellValue(text);

			wrapText = (text.contains("\n") || text.length() > ExcelExporter.MAX_COLUMN_WIDTH);
		}

		Style cellStyle = dateValue != null ? styleMapper.getDateStyleForElement(element, datePattern) :
						  styleMapper.getStyleForElement(element);

		if(wrapText) {
			cellStyle.addProperty(CssStringProperty.WRAP_TEXT, "true");
		}

		cell.setCellStyle(styleGenerator.getStyle(element, cell, cellStyle));

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

	/**
	 * Converts a date pattern (e.g. from Java) to one that can be used by Excel.
	 *
	 * @param dateTimePattern
	 * @return
	 */
	private String toExcelDateTimePattern(String dateTimePattern) {
		if(dateTimePattern.matches("^.*a$")) { // 1. Replace 'a' at the end of the pattern with AM/PM
			return dateTimePattern.substring(0, dateTimePattern.lastIndexOf("a")) + "AM/PM";
		}
		return dateTimePattern;
	}

}
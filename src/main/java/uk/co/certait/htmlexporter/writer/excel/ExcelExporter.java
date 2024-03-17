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

import java.io.IOException;
import java.io.OutputStream;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xssf.usermodel.helpers.ColumnHelper;
import org.jsoup.nodes.Element;

import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCol;
import uk.co.certait.htmlexporter.css.StyleMap;
import uk.co.certait.htmlexporter.writer.AbstractExporter;
import uk.co.certait.htmlexporter.writer.TableWriter;

public class ExcelExporter extends AbstractExporter {
	//Make 150 max column width. For reference, POI max column width is 255 characters.
	public final static int MAX_COLUMN_WIDTH = 150;

	public void exportHtml(String html, OutputStream out) throws IOException {
		Workbook workbook = new XSSFWorkbook();

		parse(html);

		StyleMap styleMapper = getStyleMapper();
		Sheet sheet = null;
		int startRow = 0;

		for (Element element : getTables()) {
			if (workbook.getNumberOfSheets() == 0) {
				String sheetName = getSheetName(element);

				if (StringUtils.isNotEmpty(sheetName)) {
					sheet = workbook.createSheet(sheetName);
				} else {
					sheet = workbook.createSheet();
				}
			} else if (isNewSheet(element)) {
				String sheetName = getSheetName(element);

				if (StringUtils.isNotEmpty(sheetName))
					sheet = workbook.createSheet(sheetName);
				else {
					sheet = workbook.createSheet();
				}

				startRow = 0;
			}

			ExcelTableCellWriter cellWriter = new ExcelTableCellWriter(sheet, styleMapper);
			cellWriter.setDatePattern(getDatePattern());

			ExcelTableRowWriter rowWriter = new ExcelTableRowWriter(sheet, cellWriter);
			TableWriter writer = new ExcelTableWriter(rowWriter);

			startRow += writer.writeTable(element, styleMapper, startRow) + 1;
			Row row = sheet.createRow(startRow);
			row.setHeight((short) -1);
		}

		for (int i = 0; i < workbook.getNumberOfSheets(); ++i) {
			formatSheet(workbook.getSheetAt(i));
		}

		workbook.write(out);
		out.flush();
		out.close();
		workbook.close();
	}

	protected void formatSheet(Sheet sheet) {
		int lastRowWithData = 0;
		for (int i = sheet.getLastRowNum(); i >= 0; --i) {
			if (sheet.getRow(i) != null &&  sheet.getRow(i).getPhysicalNumberOfCells() > 0) {
				lastRowWithData = i;
				break;
			}
		}

		int defaultColumnWidth = sheet.getDefaultColumnWidth() * 256;

		ColumnHelper columnHelper = ((XSSFSheet) sheet).getColumnHelper();
		for (int i = 0; i < sheet.getRow(lastRowWithData).getPhysicalNumberOfCells(); ++i) {
			CTCol ctCol = columnHelper.getColumn(i, true);

			int columnWidth = sheet.getColumnWidth(i);
			boolean hasCustomWidth = ctCol != null && ctCol.isSetCustomWidth();
			boolean doAutoSizeColumn = !hasCustomWidth || columnWidth == 600;

			if(doAutoSizeColumn) {
				sheet.autoSizeColumn(i);
			}
		}

		for (int i = 0; i < sheet.getRow(sheet.getLastRowNum()).getPhysicalNumberOfCells(); ++i) {
			int columnWidth = (int) (sheet.getColumnWidth(i) * 1.2);
			sheet.setColumnWidth(i, (int)(columnWidth));
		}
	}
}

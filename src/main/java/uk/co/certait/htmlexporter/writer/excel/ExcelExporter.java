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
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jsoup.nodes.Element;

import uk.co.certait.htmlexporter.css.StyleMap;
import uk.co.certait.htmlexporter.writer.AbstractExporter;
import uk.co.certait.htmlexporter.writer.TableWriter;

public class ExcelExporter extends AbstractExporter {
	
	private boolean autoSizeMergedCells;
	private boolean ignoreNumberAsTextWarning;
	
	private int tableDistanceInRows;
	
	public ExcelExporter(boolean autoSizeMergedCells, int tableDistanceInRows, boolean ignoreNumberAsTextWarning) {
		super();
		this.autoSizeMergedCells = autoSizeMergedCells;
		this.tableDistanceInRows = tableDistanceInRows;
		this.ignoreNumberAsTextWarning = ignoreNumberAsTextWarning;
	}
	
	public ExcelExporter() {
		super();
		this.autoSizeMergedCells = false;
		this.tableDistanceInRows = 1;
		this.ignoreNumberAsTextWarning = false;
	}

	public void exportHtml(String html, OutputStream out) throws IOException {
		Workbook workbook = new XSSFWorkbook();

		StyleMap styleMapper = getStyleMapper(html);
		Sheet sheet = null;
		int startRow = 0;

		for (Element element : getTables(html)) {

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

			TableWriter writer = new ExcelTableWriter(
					new ExcelTableRowWriter(sheet, new ExcelTableCellWriter(sheet, styleMapper, ignoreNumberAsTextWarning)));

			startRow += writer.writeTable(element, styleMapper, startRow) + tableDistanceInRows;
			sheet.createRow(startRow);
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
			if (sheet.getRow(i) != null && sheet.getRow(i).getPhysicalNumberOfCells() > 0) {
				lastRowWithData = i;
				break;
			}
		}

		for (int i = 0; i < sheet.getRow(lastRowWithData).getPhysicalNumberOfCells(); ++i) {
			sheet.autoSizeColumn(i,autoSizeMergedCells);
		}

		for (int i = 0; i < sheet.getRow(sheet.getLastRowNum()).getPhysicalNumberOfCells(); ++i) {
			sheet.setColumnWidth(i, (int) (sheet.getColumnWidth(i) * 1.2));
		}
	}
	
	
	public boolean isAutoSizeMergedCells() {
		return autoSizeMergedCells;
	}

	public void setAutoSizeMergedCells(boolean autoSizeMergedCells) {
		this.autoSizeMergedCells = autoSizeMergedCells;
	}

	public boolean isIgnoreNumberAsTextWarning() {
		return ignoreNumberAsTextWarning;
	}

	public void setIgnoreNumberAsTextWarning(boolean ignoreNumberAsTextWarning) {
		this.ignoreNumberAsTextWarning = ignoreNumberAsTextWarning;
	}

	public int getTableDistanceInRows() {
		return tableDistanceInRows;
	}

	public void setTableDistanceInRows(int tableDistanceInRows) {
		this.tableDistanceInRows = tableDistanceInRows;
	}


}

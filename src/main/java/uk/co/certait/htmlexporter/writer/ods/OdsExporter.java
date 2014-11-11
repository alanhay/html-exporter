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

import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;

import org.apache.commons.lang.StringUtils;
import org.jsoup.nodes.Element;
import org.odftoolkit.simple.SpreadsheetDocument;
import org.odftoolkit.simple.table.Column;
import org.odftoolkit.simple.table.Table;

import uk.co.certait.htmlexporter.css.StyleMap;
import uk.co.certait.htmlexporter.writer.AbstractExporter;
import uk.co.certait.htmlexporter.writer.TableWriter;

public class OdsExporter extends AbstractExporter {
	public void exportHtml(String html, OutputStream out) throws IOException {
		SpreadsheetDocument spreadsheet = null;

		try {
			spreadsheet = SpreadsheetDocument.newSpreadsheetDocument();
			Table table = spreadsheet.getSheetByIndex(0);;
			StyleMap styleMapper = getStyleMapper(html);
			int startRow = 0;
			boolean firstLoop = true;

			for (Element element : getTables(html)) {

				if (firstLoop) {
					String sheetName = getSheetName(element);

					if (StringUtils.isNotEmpty(sheetName)) {
						table.setTableName(sheetName);
					}

				} else if (isNewSheet(element)) {
					String sheetName = getSheetName(element);

					if (StringUtils.isNotEmpty(sheetName))
						table = spreadsheet.appendSheet(sheetName);

					else {
						table = spreadsheet.appendSheet("Sheet " + (spreadsheet.getSheetCount() + 1));
					}

					startRow = 0;

					if (spreadsheet.getSheetByIndex(spreadsheet.getSheetCount() - 1).getRowCount() == 0) {
						spreadsheet.removeSheet(spreadsheet.getSheetCount() - 1);
					}
				}

				TableWriter writer = new OdsTableWriter(new OdsTableRowWriter(table, new OdsTableCellWriter(table,
						styleMapper)));

				startRow += writer.writeTable(element, styleMapper, startRow) + 1;
				firstLoop = false;
			}

			for (int i = 0; i < spreadsheet.getSheetCount(); ++i) {
				formatSheet(spreadsheet.getSheetByIndex(i));
			}

			spreadsheet.save(out);

		} catch (Exception ex) {
			throw new IOException(ex);
		}
	}

	protected void formatSheet(Table sheet) {
		for (Iterator<Column> columns = sheet.getColumnIterator(); columns.hasNext();) {
			columns.next().setUseOptimalWidth(true);
		}
	}
}

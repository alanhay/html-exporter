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

import org.jsoup.nodes.Element;
import org.odftoolkit.simple.SpreadsheetDocument;
import org.odftoolkit.simple.table.Table;

import uk.co.certait.htmlexporter.css.StyleMap;
import uk.co.certait.htmlexporter.writer.AbstractExporter;
import uk.co.certait.htmlexporter.writer.TableWriter;

public class OdsExporter extends AbstractExporter
{
	public void exportHtml(String html, OutputStream out) throws IOException
	{
		SpreadsheetDocument spreadsheet = null;

		try
		{
			spreadsheet = SpreadsheetDocument.newSpreadsheetDocument();
			Table table = spreadsheet.getSheetByIndex(0);

			StyleMap styleMapper = getStyleMapper(html);

			TableWriter writer = new OdsTableWriter(new OdsTableRowWriter(table, new OdsTableCellWriter(table,
					styleMapper)));

			int startRow = 0;

			for (Element element : getTables(html))
			{
				startRow += writer.writeTable(element, styleMapper, startRow) + 1;
			}

			spreadsheet.save(out);

		}
		catch (Exception ex)
		{
			throw new IOException(ex);
		}
	}
}

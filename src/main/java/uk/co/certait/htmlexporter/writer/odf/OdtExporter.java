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
package uk.co.certait.htmlexporter.writer.odf;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.jsoup.nodes.Element;
import org.odftoolkit.simple.TextDocument;
import org.odftoolkit.simple.table.Table;

import uk.co.certait.htmlexporter.css.StyleMap;
import uk.co.certait.htmlexporter.writer.AbstractExporter;
import uk.co.certait.htmlexporter.writer.TableWriter;

public class OdtExporter extends AbstractExporter
{
	public void exportHtml(String html, OutputStream out) throws IOException
	{
		TextDocument document;

		try
		{
			InputStream in = this.getClass().getResourceAsStream("/template.odt");
			document = TextDocument.loadDocument(in);
	
			StyleMap styleMap= getStyleMapper(html);

			for (Element element : getTables(html))
			{
				Table table = document.addTable();
				//table.removeRowsByIndex(1, 1);
				
				TableWriter writer = new OdtTableWriter(new OdtTableRowWriter(table, new OdtTableCellWriter(table,styleMap)));
				
				writer.writeTable(element, styleMap, 0);
				document.addParagraph("");
				
				//if(table.getRowCount() > 30){
					for(int i = 0; i < table.getColumnCount(); ++ i)
					{
						if(i < 3)
						{
							table.getColumnByIndex(i).setWidth((table.getWidth()/100)*20);
						}
						//else
						//{
						//	table.getColumnByIndex(i).setWidth((table.getWidth()/100)*3);
						//}
					}
				
				for(int i = 0; i < table.getColumnCount(); ++ i)
				{
					System.out.println("width=" + table.getColumnByIndex(i).getWidth());
				}
				}
			//}
			
			document.save(out);
		}
		catch (Exception ex)
		{
			throw new IOException(ex);
		}
	}
}

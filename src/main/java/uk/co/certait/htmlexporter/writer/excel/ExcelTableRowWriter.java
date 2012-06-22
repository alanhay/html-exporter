package uk.co.certait.htmlexporter.writer.excel;

import org.apache.poi.ss.usermodel.Sheet;
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
		sheet.createRow(rowIndex);
	}
}

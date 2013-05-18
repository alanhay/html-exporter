package uk.co.certait.htmlexporter.writer.odf;

import org.apache.commons.lang.StringUtils;
import org.jsoup.nodes.Element;
import org.odftoolkit.simple.table.Cell;
import org.odftoolkit.simple.table.Table;

import uk.co.certait.htmlexporter.css.Style;
import uk.co.certait.htmlexporter.css.StyleMap;
import uk.co.certait.htmlexporter.ss.CellRange;
import uk.co.certait.htmlexporter.ss.Function;
import uk.co.certait.htmlexporter.writer.AbstractTableCellWriter;
import uk.co.certait.htmlexporter.writer.ods.OdsStyleGenerator;

public class OdtTableCellWriter extends AbstractTableCellWriter
{
	private Table table;
	private StyleMap styleMap;
	private OdsStyleGenerator styleGenerator;

	public OdtTableCellWriter(Table table, StyleMap styleMap)
	{
		this.table = table;
		this.styleMap = styleMap;
		styleGenerator = new OdsStyleGenerator();
	}

	@Override
	public void renderCell(Element element, int rowIndex, int columnIndex)
	{
		Cell cell = table.getCellByPosition(columnIndex, rowIndex);

		Double numericValue = null;

		if ((numericValue = getNumericValue(element)) != null)
		{
			cell.setDoubleValue(numericValue);
		}
		else
		{
			String text = getElementText(element).replaceAll("\u00A0", "");

			if (StringUtils.isNotEmpty(text))
			{
				cell.setStringValue(getElementText(element));
			}
		}
		
		Style style = styleMap.getStyleForElement(element);
		styleGenerator.styleCell(cell, style);
	}

	@Override
	public void addFunctionCell(int rowIndex, int columnIndex, CellRange range, Function function)
	{
		// TODO Auto-generated method stub

	}
}

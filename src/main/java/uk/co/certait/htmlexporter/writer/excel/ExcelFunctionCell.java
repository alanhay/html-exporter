package uk.co.certait.htmlexporter.writer.excel;

import org.apache.poi.ss.usermodel.Cell;

import uk.co.certait.htmlexporter.ss.CellRange;
import uk.co.certait.htmlexporter.ss.CellRangeObserver;
import uk.co.certait.htmlexporter.ss.CellRangeResolver;
import uk.co.certait.htmlexporter.ss.Function;

public class ExcelFunctionCell implements CellRangeObserver
{
	private Cell cell;
	private CellRangeResolver resolver;
	private Function function;

	public ExcelFunctionCell(Cell cell, CellRange range, CellRangeResolver resolver, Function function)
	{
		this.cell = cell;
		this.resolver = resolver;
		this.function = function;
		
		range.addCellRangeListener(this);
		
		cellRangeUpdated(range);
	}

	public void cellRangeUpdated(CellRange range)
	{
		cell.setCellFormula(function.toString()+ "(" + resolver.getRangeString(range) + ")");
	}
}

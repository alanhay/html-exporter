package uk.co.certait.htmlexporter.ss;

public class DefaultTableCellReference implements TableCellReference
{
	private int rowIndex;
	private int columnIndex;

	public DefaultTableCellReference(int rowIndex, int columnIndex)
	{
		this.rowIndex = rowIndex;
		this.columnIndex = columnIndex;
	}

	@Override
	public int getRowIndex()
	{
		return rowIndex;
	}

	@Override
	public int getColumnIndex()
	{
		return columnIndex;
	}
}

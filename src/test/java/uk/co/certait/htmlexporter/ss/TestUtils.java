package uk.co.certait.htmlexporter.ss;

import org.easymock.EasyMock;

import uk.co.certait.htmlexporter.ss.TableCellReference;

public class TestUtils
{
	public static TableCellReference createCell(int rowIndex, int columnIndex)
	{
		TableCellReference cell = EasyMock.createMock(TableCellReference.class);
		EasyMock.expect(cell.getRowIndex()).andReturn(rowIndex).anyTimes();
		EasyMock.expect(cell.getColumnIndex()).andReturn(columnIndex).anyTimes();

		EasyMock.replay(cell);

		return cell;
	}
}

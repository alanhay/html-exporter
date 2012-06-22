package uk.co.certait.htmlexporter.ss;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import uk.co.certait.htmlexporter.ss.CellRange;
import uk.co.certait.htmlexporter.ss.TableCellReference;

public class CellRangeTest
{
	private CellRange range;

	@Before
	public void onSetup()
	{
		range = new CellRange();
	}

	@Test
	public void testAddCell()
	{
		TableCellReference cell1 = createCell(0, 5);
		range.addCell(cell1);

		Assert.assertFalse(range.isEmpty());
		Assert.assertEquals(1, range.getWidth());
		Assert.assertEquals(1, range.getHeight());
		
		TableCellReference cell2 = createCell(0, 6);
		range.addCell(cell2);

		Assert.assertEquals(2, range.getWidth());
		Assert.assertEquals(1, range.getHeight());

		TableCellReference cell3 = createCell(1, 3);
		range.addCell(cell3);

		Assert.assertEquals(4, range.getWidth());
		Assert.assertEquals(2, range.getHeight());
		
		TableCellReference cell4 = createCell(3, 8);
		range.addCell(cell4);

		Assert.assertEquals(6, range.getWidth());
		Assert.assertEquals(4, range.getHeight());
		
		TableCellReference cell5 = createCell(6, 1);
		range.addCell(cell5);

		Assert.assertEquals(8, range.getWidth());
		Assert.assertEquals(7, range.getHeight());
	}

	@Test
	public void testIsNewRowRequired()
	{
		TableCellReference cell1 = createCell(0, 0);
		Assert.assertTrue(range.isCellInNewRow(cell1));
		range.addCell(cell1);

		TableCellReference cell2 = createCell(0, 5);
		Assert.assertFalse(range.isCellInNewRow(cell2));
		range.addCell(cell2);

		TableCellReference cell3 = createCell(1, 2);
		Assert.assertTrue(range.isCellInNewRow(cell3));
	}

	@Test
	public void testIsEmpty()
	{
		Assert.assertTrue(range.isEmpty());

		Assert.assertEquals(0, range.getWidth());
		Assert.assertEquals(0, range.getHeight());
	}
	
	@Test
	public void testIsContiguous()
	{
		range.addCell(createCell(1, 1));
		range.addCell(createCell(1, 2));
		Assert.assertTrue(range.isContiguous());
		
		range.addCell(createCell(1, 4));
		Assert.assertFalse(range.isContiguous());
		
		range = new CellRange();
		range.addCell(createCell(1, 1));
		range.addCell(createCell(1, 2));
		range.addCell(createCell(2, 1));
		range.addCell(createCell(2, 2));
		Assert.assertTrue(range.isContiguous());
		
		range = new CellRange();
		range.addCell(createCell(1, 1));
		range.addCell(createCell(1, 2));
		range.addCell(createCell(3, 1));
		range.addCell(createCell(3, 2));
		Assert.assertFalse(range.isContiguous());
	}

	private TableCellReference createCell(int rowIndex, int columnIndex)
	{
		return TestUtils.createCell(rowIndex, columnIndex);
	}
}

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
package uk.co.certait.htmlexporter.ss;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

public class CellRangeRowTest
{
	private CellRangeRow row;

	@Before
	public void onSetup()
	{
		row = new CellRangeRow(0);
	}

	@Test
	public void testAddCell()
	{
		try
		{
			row.addCell(null);
			Assert.fail();
		}
		catch (IllegalArgumentException ex)
		{

		}

		TableCellReference ref = createCell(1, 5);
		row.addCell(ref);
		Assert.assertSame(row.getCells().get(5), ref);
		Assert.assertEquals(1, row.getWidth());

		TableCellReference ref2 = createCell(1, 5);
		row.addCell(ref2);
		Assert.assertSame(row.getCells().get(5), ref2);
		Assert.assertEquals(1, row.getWidth());
	}

	@Test
	public void testIsContiguous()
	{
		Assert.assertFalse(row.isContiguous());// ??

		row.addCell(createCell(1, 1));
		Assert.assertTrue(row.isContiguous());

		row.addCell(createCell(1, 2));
		Assert.assertTrue(row.isContiguous());

		row.addCell(createCell(1, 3));
		Assert.assertTrue(row.isContiguous());

		row.addCell(createCell(1, 5));
		Assert.assertFalse(row.isContiguous());

		// fill the gap
		row.addCell(createCell(1, 4));
		Assert.assertTrue(row.isContiguous());
	}

	@Test
	public void testGetFirstPopulatedColumn()
	{
		Assert.assertEquals(-1, row.getFirstPopulatedColumn());
		row.addCell(createCell(3, 4));
		row.addCell(createCell(3, 5));
		row.addCell(createCell(3, 6));

		Assert.assertEquals(4, row.getFirstPopulatedColumn());
		
		row.addCell(createCell(3, 2));
		Assert.assertEquals(2, row.getFirstPopulatedColumn());
	}

	@Test
	public void testGetLastPopulatedColumn()
	{
		Assert.assertEquals(-1, row.getLastPopulatedColumn());

		row.addCell(createCell(1, 1));

		row.addCell(createCell(1, 2));
		row.addCell(createCell(1, 5));
		row.addCell(createCell(1, 9));
		Assert.assertEquals(9, row.getLastPopulatedColumn());
	}

	@Test
	public void testGetWidth()
	{
		Assert.assertEquals(0, row.getWidth());

		row.addCell(createCell(2, 1));
		Assert.assertEquals(1, row.getWidth());

		row.addCell(createCell(2, 2));
		Assert.assertEquals(2, row.getWidth());

		row.addCell(createCell(2, 4));
		Assert.assertEquals(4, row.getWidth());
		
		//overwrite
		row.addCell(createCell(2, 4));
		Assert.assertEquals(4, row.getWidth());

		row.addCell(createCell(2, 0));
		Assert.assertEquals(5, row.getWidth());
	}

	private TableCellReference createCell(int rowIndex, int columnIndex)
	{
		return TestUtils.createCell(rowIndex, columnIndex);
	}
}

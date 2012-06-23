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

import uk.co.certait.htmlexporter.ss.CellRangeRow;
import uk.co.certait.htmlexporter.ss.TableCellReference;

public class CellRangeRowTest
{
	private CellRangeRow row;
	
	@Before
	public void onSetup()
	{
		row = new CellRangeRow(0);
	}
	@Test
	public void testIsContiguous()
	{
		Assert.assertFalse(row.isContiguous());//?? 
		
		row.addCell(createCell(1, 1));
		Assert.assertTrue(row.isContiguous());
		
		row.addCell(createCell(1, 2));
		Assert.assertTrue(row.isContiguous());
		
		row.addCell( createCell(1, 3));
		Assert.assertTrue(row.isContiguous());
		
		row.addCell(createCell(1, 5));
		Assert.assertFalse(row.isContiguous());
	}
	
	@Test
	public void testGetFirstPopulatedColumnForRow()
	{
		row.addCell(createCell(3, 1));
		row.addCell(createCell(3, 2));
		row.addCell(createCell(3, 3));

		Assert.assertEquals(3, row.getLastPopulatedColumn());
	}
	
	@Test
	public void testGetLastPopulatedColumnForRow()
	{
		row.addCell(createCell(1, 1));

		row.addCell(createCell(1, 2));
		row.addCell(createCell(1, 5));
		row.addCell(createCell(1, 9));
		Assert.assertEquals(9, row.getLastPopulatedColumn());
	}
	
	private TableCellReference createCell(int rowIndex, int columnIndex)
	{
		return TestUtils.createCell(rowIndex, columnIndex);
	}
}

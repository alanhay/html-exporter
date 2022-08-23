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

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

public class CellRangeRowTest {
	private CellRangeRow row;

	@Before
	public void onSetup() {
		row = new CellRangeRow(0);
	}

	@Test
	public void testAddCell() {
		try {
			row.addCell(null);
			fail();
		} catch (IllegalArgumentException ex) {

		}

		TableCellReference ref = createCell(1, 5);
		row.addCell(ref);
		assertThat(row.getCells().get(5)).isSameAs(ref);
		assertThat(row.getWidth()).isEqualTo(1);

		TableCellReference ref2 = createCell(1, 5);
		row.addCell(ref2);
		assertThat(row.getCells().get(5)).isSameAs(ref2);
		assertThat(row.getWidth()).isEqualTo(1);
	}

	@Test
	public void testIsContiguous() {
		assertThat(row.isContiguous()).isFalse();

		row.addCell(createCell(1, 1));
		assertThat(row.isContiguous()).isTrue();

		row.addCell(createCell(1, 2));
		assertThat(row.isContiguous()).isTrue();

		row.addCell(createCell(1, 3));
		assertThat(row.isContiguous()).isTrue();

		row.addCell(createCell(1, 5));
		assertThat(row.isContiguous()).isFalse();

		// fill the gap
		row.addCell(createCell(1, 4));
		assertThat(row.isContiguous()).isTrue();
	}

	@Test
	public void testGetFirstPopulatedColumn() {
		assertThat(row.getFirstPopulatedColumn()).isEqualTo(-1);
		row.addCell(createCell(3, 4));
		row.addCell(createCell(3, 5));
		row.addCell(createCell(3, 6));

		assertThat(row.getFirstPopulatedColumn()).isEqualTo(4);

		row.addCell(createCell(3, 2));
		assertThat(row.getFirstPopulatedColumn()).isEqualTo(2);
	}

	@Test
	public void testGetLastPopulatedColumn() {
		assertThat(row.getLastPopulatedColumn()).isEqualTo(-1);

		row.addCell(createCell(1, 1));

		row.addCell(createCell(1, 2));
		row.addCell(createCell(1, 5));
		row.addCell(createCell(1, 9));
		assertThat(row.getLastPopulatedColumn()).isEqualTo(9);
	}

	@Test
	public void testGetWidth() {
		assertThat(row.getWidth()).isEqualTo(0);

		row.addCell(createCell(2, 1));
		assertThat(row.getWidth()).isEqualTo(1);

		row.addCell(createCell(2, 2));
		assertThat(row.getWidth()).isEqualTo(2);

		row.addCell(createCell(2, 4));
		assertThat(row.getWidth()).isEqualTo(4);

		// overwrite
		row.addCell(createCell(2, 4));
		assertThat(row.getWidth()).isEqualTo(4);

		row.addCell(createCell(2, 0));
		assertThat(row.getWidth()).isEqualTo(5);
	}

	private TableCellReference createCell(int rowIndex, int columnIndex) {
		return TestUtils.createCell(rowIndex, columnIndex);
	}
}

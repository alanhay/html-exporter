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

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;

public class CellRangeTest {
	private CellRange range;

	@Before
	public void onSetup() {
		range = new CellRange();
	}

	@Test
	public void testAddCell() {
		TableCellReference cell1 = createCell(0, 5);
		range.addCell(cell1);

		assertThat(range.isEmpty()).isFalse();
		assertThat(range.getHeight()).isEqualTo(1);

		TableCellReference cell2 = createCell(0, 6);
		range.addCell(cell2);
		assertThat(range.getHeight()).isEqualTo(1);

		TableCellReference cell3 = createCell(1, 3);
		range.addCell(cell3);
		assertThat(range.getHeight()).isEqualTo(2);

		TableCellReference cell4 = createCell(3, 8);
		range.addCell(cell4);
		assertThat(range.getHeight()).isEqualTo(4);

		TableCellReference cell5 = createCell(6, 1);
		range.addCell(cell5);
		assertThat(range.getHeight()).isEqualTo(7);
	}

	@Test
	public void testIsNewRowRequired() {
		TableCellReference cell1 = createCell(0, 0);
		assertThat(range.isCellInNewRow(cell1)).isTrue();
		range.addCell(cell1);

		TableCellReference cell2 = createCell(0, 5);
		assertThat(range.isCellInNewRow(cell2)).isFalse();
		range.addCell(cell2);

		TableCellReference cell3 = createCell(1, 2);
		assertThat(range.isCellInNewRow(cell3)).isTrue();
	}

	@Test
	public void testIsEmpty() {
		assertThat(range.isEmpty()).isTrue();
		assertThat(range.getHeight()).isEqualTo(0);
	}

	@Test
	public void testIsContiguous() {
		range.addCell(createCell(1, 1));
		range.addCell(createCell(1, 2));
		assertThat(range.isContiguous()).isTrue();

		range.addCell(createCell(1, 4));
		assertThat(range.isContiguous()).isFalse();

		range = new CellRange();
		range.addCell(createCell(1, 1));
		range.addCell(createCell(1, 2));
		range.addCell(createCell(2, 1));
		range.addCell(createCell(2, 2));
		assertThat(range.isContiguous()).isTrue();

		// false: rows differ in size
		range = new CellRange();
		range.addCell(createCell(1, 1));
		range.addCell(createCell(1, 2));
		range.addCell(createCell(2, 1));
		range.addCell(createCell(2, 2));
		range.addCell(createCell(2, 3));
		assertThat(range.isContiguous()).isFalse();

		// false:
		range = new CellRange();
		range.addCell(createCell(1, 1));
		range.addCell(createCell(1, 2));
		range.addCell(createCell(3, 1));
		range.addCell(createCell(3, 2));
		assertThat(range.isContiguous()).isFalse();
	}

	@Test
	public void testAddCellRangeListener() {
		// verify that registered observers called on cell added.
		CellRangeObserver observer = createCellRangeObserver(range);
		range.addCellRangeObserver(observer);
		range.addCell(createCell(1, 3));

		EasyMock.verify(observer);
	}

	private TableCellReference createCell(int rowIndex, int columnIndex) {
		return TestUtils.createCell(rowIndex, columnIndex);
	}

	private CellRangeObserver createCellRangeObserver(CellRange range) {
		CellRangeObserver observer = EasyMock.createMock(CellRangeObserver.class);
		observer.cellRangeUpdated(range);
		EasyMock.expectLastCall();

		EasyMock.replay(observer);

		return observer;
	}
}

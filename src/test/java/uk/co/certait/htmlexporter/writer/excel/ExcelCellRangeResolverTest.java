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
package uk.co.certait.htmlexporter.writer.excel;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Before;
import org.junit.Test;

import uk.co.certait.htmlexporter.ss.CellRange;
import uk.co.certait.htmlexporter.ss.CellRangeResolver;
import uk.co.certait.htmlexporter.ss.TableCellReference;
import uk.co.certait.htmlexporter.ss.TestUtils;

public class ExcelCellRangeResolverTest {
	private CellRange range;
	private CellRangeResolver resolver;

	@Before
	public void onSetUp() {
		range = new CellRange();
		resolver = new ExcelCellRangeResolver();
	}

	@Test
	public void testGetFormulaString() {
		range.addCell(createCell(0, 0));
		range.addCell(createCell(0, 1));

		assertThat(resolver.getRangeString(range)).isEqualTo("A1:B1");

		range.addCell(createCell(0, 2));
		assertThat(resolver.getRangeString(range)).isEqualTo("A1:C1");

		range.addCell(createCell(1, 0));
		range.addCell(createCell(1, 1));
		range.addCell(createCell(1, 2));

		assertThat(resolver.getRangeString(range)).isEqualTo("A1:C2");

		range = new CellRange();
		range.addCell(createCell(3, 4));
		range.addCell(createCell(3, 5));
		range.addCell(createCell(3, 6));
		range.addCell(createCell(4, 4));
		range.addCell(createCell(4, 5));
		range.addCell(createCell(4, 6));
		range.addCell(createCell(5, 4));
		range.addCell(createCell(5, 5));
		range.addCell(createCell(5, 6));

		assertThat(resolver.getRangeString(range)).isEqualTo("E4:G6");
	}

	public TableCellReference createCell(int rowIndex, int columnIndex) {
		return TestUtils.createCell(rowIndex, columnIndex);
	}
}

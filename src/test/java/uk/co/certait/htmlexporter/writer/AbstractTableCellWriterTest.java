package uk.co.certait.htmlexporter.writer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;

import org.easymock.EasyMock;
import org.jsoup.nodes.Element;
import org.junit.Test;

import uk.co.certait.htmlexporter.writer.excel.ExcelTableCellWriter;

public class AbstractTableCellWriterTest {

	@Test
	public void testGetNumericValue_when_percentage() {
		Element e = createMock(Element.class);
		expect(e.hasAttr(TableCellWriter.DATA_TEXT_CELL)).andReturn(false);
		expect(e.ownText()).andReturn("23.54%");
		EasyMock.replay(e);

		ExcelTableCellWriter writer = new ExcelTableCellWriter(null, null);
		assertThat(writer.getNumericValue(e)).isEqualTo(23.54);
	}

	@Test
	public void testIsPercentageCell_when_numeric_percentage_cell() {
		Element e = createMock(Element.class);
		expect(e.hasAttr(TableCellWriter.DATA_TEXT_CELL)).andReturn(false);
		expect(e.ownText()).andReturn("23.54%").anyTimes();
		EasyMock.replay(e);

		ExcelTableCellWriter writer = new ExcelTableCellWriter(null, null);
		assertThat(writer.isPercentageCell(e)).isTrue();
	}

	@Test
	public void testIsPercentageCell_when_non_numeric_percentage_cell() {
		Element e = createMock(Element.class);
		expect(e.hasAttr(TableCellWriter.DATA_TEXT_CELL)).andReturn(false);
		expect(e.ownText()).andReturn("some text%").anyTimes();
		EasyMock.replay(e);

		ExcelTableCellWriter writer = new ExcelTableCellWriter(null, null);
		assertThat(writer.isPercentageCell(e)).isFalse();
	}
	
	@Test
	public void testIsPercentageCell_when_numeric_non_percentage_cell() {
		Element e = createMock(Element.class);
		expect(e.hasAttr(TableCellWriter.DATA_TEXT_CELL)).andReturn(false);
		expect(e.ownText()).andReturn("23").anyTimes();
		EasyMock.replay(e);

		ExcelTableCellWriter writer = new ExcelTableCellWriter(null, null);
		assertThat(writer.isPercentageCell(e)).isFalse();
	}

	@Test
	public void testGetElementtext() {

	}

	@Test
	public void testSpansMultipleColumns() {

	}

	@Test
	public void testGetMergedColumnCount() {

	}

	@Test
	public void testIsFunctionGroupCell() {

	}

	@Test
	public void testGetFunctionGroupReferences() {

	}
}

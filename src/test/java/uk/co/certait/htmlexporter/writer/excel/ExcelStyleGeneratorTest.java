package uk.co.certait.htmlexporter.writer.excel;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static uk.co.certait.htmlexporter.css.CssStringProperty.BORDER_STYLE;
import static uk.co.certait.htmlexporter.css.CssStringProperty.BORDER_TOP_STYLE;
import static uk.co.certait.htmlexporter.css.CssStringProperty.BORDER_TOP_WIDTH;
import static uk.co.certait.htmlexporter.css.CssStringProperty.BORDER_WIDTH;
import static uk.co.certait.htmlexporter.writer.TableCellWriter.DATA_NUMERIC_CELL_FORMAT_ATTRIBUTE;
import static uk.co.certait.htmlexporter.writer.TableCellWriter.DATE_CELL_ATTRIBUTE;

import java.awt.Color;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.jsoup.nodes.Element;
import org.junit.Test;

import uk.co.certait.htmlexporter.css.CssColorProperty;
import uk.co.certait.htmlexporter.css.CssStringProperty;
import uk.co.certait.htmlexporter.css.Style;

public class ExcelStyleGeneratorTest {

	@Test
	public void testGetStyle() {
		ExcelStyleGenerator generator = new ExcelStyleGenerator();
		Style style = new Style();
		style.addProperty(CssColorProperty.BACKGROUND_COLOR, Color.RED);
		style.addProperty(CssStringProperty.BORDER_STYLE, "thick");

		Font font = mock(Font.class);
		XSSFCellStyle cellStyle = mock(XSSFCellStyle.class);
		Workbook workbook = mock(Workbook.class);
		Sheet sheet = mock(Sheet.class);
		Cell cell = mock(Cell.class);
		CreationHelper creationHelper = mock(CreationHelper.class);
		DataFormat dataFormat = mock(DataFormat.class);

		when(workbook.getCreationHelper()).thenReturn(creationHelper);
		when(workbook.createFont()).thenReturn(font);
		when(workbook.createCellStyle()).thenReturn(cellStyle);
		when(sheet.getWorkbook()).thenReturn(workbook);
		when(cell.getSheet()).thenReturn(sheet);
		when(creationHelper.createDataFormat()).thenReturn(dataFormat);
		when(dataFormat.getFormat("dd/MM/yy")).thenReturn((short) 1);
		when(dataFormat.getFormat("dd/MM/yyyy")).thenReturn((short) 2);
		when(dataFormat.getFormat("##0.00")).thenReturn((short) 3);

		Element e1 = mock(Element.class);
		when(e1.hasAttr(DATE_CELL_ATTRIBUTE)).thenReturn(true);
		when(e1.attr(DATE_CELL_ATTRIBUTE)).thenReturn("dd/MM/yy");

		Element e2 = mock(Element.class);
		when(e2.hasAttr(DATE_CELL_ATTRIBUTE)).thenReturn(true);
		when(e2.attr(DATE_CELL_ATTRIBUTE)).thenReturn("dd/MM/yy");

		Element e3 = mock(Element.class);
		when(e3.hasAttr(DATE_CELL_ATTRIBUTE)).thenReturn(true);
		when(e3.attr(DATE_CELL_ATTRIBUTE)).thenReturn("dd/MM/yyyy");

		Element e4 = mock(Element.class);
		when(e4.hasAttr(DATE_CELL_ATTRIBUTE)).thenReturn(true);
		when(e4.attr(DATE_CELL_ATTRIBUTE)).thenReturn(null);

		Element e5 = mock(Element.class);
		when(e5.hasAttr(DATA_NUMERIC_CELL_FORMAT_ATTRIBUTE)).thenReturn(true);
		when(e5.attr(DATA_NUMERIC_CELL_FORMAT_ATTRIBUTE)).thenReturn("##0.00");

		generator.getStyle(e1, cell, style);
		generator.getStyle(e2, cell, style);
		generator.getStyle(e3, cell, style);
		generator.getStyle(e4, cell, style);
		generator.getStyle(e5, cell, style);

		// verify the cache works:
		// should be 4 styles created as e1 and e2 share a style and format
		verify(workbook, times(4)).createCellStyle();
	}

	@Test
	public void testGetBorderStyle_when_solid() {
		ExcelStyleGenerator generator = new ExcelStyleGenerator();
		Style style = new Style();
		style.addProperty(BORDER_STYLE, "solid");

		assertThat(generator.getBorderStyle(style, BORDER_STYLE, BORDER_WIDTH)).isEqualTo(BorderStyle.THIN);
	}

	@Test
	public void testGetBorderStyle_when_dotted() {
		ExcelStyleGenerator generator = new ExcelStyleGenerator();
		Style style = new Style();
		style.addProperty(BORDER_STYLE, "dotted");

		assertThat(generator.getBorderStyle(style, BORDER_STYLE, BORDER_WIDTH)).isEqualTo(BorderStyle.DOTTED);
	}

	@Test
	public void testGetBorderStyle_when_dashed() {
		ExcelStyleGenerator generator = new ExcelStyleGenerator();
		Style style = new Style();
		style.addProperty(BORDER_STYLE, "dashed");

		assertThat(generator.getBorderStyle(style, BORDER_STYLE, BORDER_WIDTH)).isEqualTo(BorderStyle.DASHED);
	}

	@Test
	public void testGetBorderStyle_when_double() {
		ExcelStyleGenerator generator = new ExcelStyleGenerator();
		Style style = new Style();
		style.addProperty(BORDER_STYLE, "double");

		assertThat(generator.getBorderStyle(style, BORDER_STYLE, BORDER_WIDTH)).isEqualTo(BorderStyle.DOUBLE);
	}

	@Test
	public void testGetBorderStyle_when_unsopported() {
		ExcelStyleGenerator generator = new ExcelStyleGenerator();
		Style style = new Style();
		style.addProperty(BORDER_STYLE, "ridge");

		assertThat(generator.getBorderStyle(style, BORDER_STYLE, BORDER_WIDTH)).isNull();
	}

	@Test
	public void testGetBorderStyle_when_solid_thin() {
		ExcelStyleGenerator generator = new ExcelStyleGenerator();
		Style style = new Style();
		style.addProperty(BORDER_STYLE, "solid");
		style.addProperty(BORDER_WIDTH, "thin");

		assertThat(generator.getBorderStyle(style, BORDER_STYLE, BORDER_WIDTH)).isEqualTo(BorderStyle.THIN);
	}

	@Test
	public void testGetBorderStyle_when_solid_medium() {
		ExcelStyleGenerator generator = new ExcelStyleGenerator();
		Style style = new Style();
		style.addProperty(BORDER_STYLE, "solid");
		style.addProperty(BORDER_WIDTH, "medium");

		assertThat(generator.getBorderStyle(style, BORDER_STYLE, BORDER_WIDTH)).isEqualTo(BorderStyle.MEDIUM);
	}

	@Test
	public void testGetBorderStyle_when_solid_thick() {
		ExcelStyleGenerator generator = new ExcelStyleGenerator();
		Style style = new Style();
		style.addProperty(BORDER_STYLE, "solid");
		style.addProperty(BORDER_WIDTH, "thick");

		assertThat(generator.getBorderStyle(style, BORDER_STYLE, BORDER_WIDTH)).isEqualTo(BorderStyle.THICK);
	}

	@Test
	public void testGetBorderStyleTop_when_explict_solid_wth_general_width() {
		ExcelStyleGenerator generator = new ExcelStyleGenerator();
		Style style = new Style();
		style.addProperty(BORDER_TOP_STYLE, "solid");
		style.addProperty(BORDER_WIDTH, "medium");

		assertThat(generator.getBorderStyle(style, BORDER_TOP_STYLE, BORDER_TOP_WIDTH)).isEqualTo(BorderStyle.MEDIUM);
	}

	@Test
	public void testGetBorderStyleTop_when_implicit_solid_with_specific_width() {
		ExcelStyleGenerator generator = new ExcelStyleGenerator();
		Style style = new Style();
		style.addProperty(BORDER_STYLE, "solid");
		style.addProperty(BORDER_WIDTH, "thin");
		style.addProperty(BORDER_TOP_WIDTH, "thick");
		assertThat(generator.getBorderStyle(style, BORDER_TOP_STYLE, BORDER_TOP_WIDTH)).isEqualTo(BorderStyle.THICK);
	}
}

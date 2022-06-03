package uk.co.certait.htmlexporter.writer.excel;

import static org.assertj.core.api.Assertions.assertThat;
import static uk.co.certait.htmlexporter.css.CssStringProperty.BORDER_STYLE;
import static uk.co.certait.htmlexporter.css.CssStringProperty.BORDER_TOP_STYLE;
import static uk.co.certait.htmlexporter.css.CssStringProperty.BORDER_TOP_WIDTH;
import static uk.co.certait.htmlexporter.css.CssStringProperty.BORDER_WIDTH;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.junit.Test;

import uk.co.certait.htmlexporter.css.Style;

public class ExcelStyleGeneratorTest {

	@Test
	public void testGetBorderStyle_when_solid() {
		ExcelStyleGenerator generator = new ExcelStyleGenerator();
		Style style = new Style();
		style.addProperty(BORDER_STYLE, "solid");

		assertThat(generator.getBorderStyle(style, BORDER_STYLE, BORDER_WIDTH)).isEqualTo(BorderStyle.HAIR);
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

		assertThat(generator.getBorderStyle(style, BORDER_STYLE, BORDER_WIDTH)).isEqualTo(BorderStyle.HAIR);
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

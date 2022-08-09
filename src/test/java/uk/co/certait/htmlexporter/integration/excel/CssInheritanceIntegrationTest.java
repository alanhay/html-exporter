package uk.co.certait.htmlexporter.integration.excel;

import static org.assertj.core.api.Assertions.assertThat;

import java.awt.Color;

import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;

public class CssInheritanceIntegrationTest extends BaseIntegrationTest {

	/**
	 * Tests that the correct basic formatting is correctly applied to the table
	 * cells
	 */
	@Test
	public void testDataFormat() {
		XSSFWorkbook workbook = createWorkbook("/integration/css-inheritance.html");
		XSSFSheet sheet = workbook.getSheetAt(0);
		XSSFRow row = sheet.getRow(0);

		// row 0 has no explicit <tr/> or <td/> styling so should inherit from table
		// background
		assertThat(row.getCell(0).getCellStyle().getFillForegroundXSSFColor())
				.isEqualTo(new XSSFColor(Color.YELLOW, null));
		assertThat(row.getCell(1).getCellStyle().getFillForegroundXSSFColor())
				.isEqualTo(new XSSFColor(Color.YELLOW, null));
		assertThat(row.getCell(2).getCellStyle().getFillForegroundXSSFColor())
				.isEqualTo(new XSSFColor(Color.YELLOW, null));

		row = sheet.getRow(1);

		// row 1 has explicit <tr/> styling but no <td/> styling so should inherit from
		// <tr/> background
		assertThat(row.getCell(0).getCellStyle().getFillForegroundXSSFColor())
				.isEqualTo(new XSSFColor(Color.PINK, null));
		assertThat(row.getCell(1).getCellStyle().getFillForegroundXSSFColor())
				.isEqualTo(new XSSFColor(Color.PINK, null));
		assertThat(row.getCell(2).getCellStyle().getFillForegroundXSSFColor())
				.isEqualTo(new XSSFColor(Color.PINK, null));

		row = sheet.getRow(2);

		// row 2 has explicit <tr/> styling but no <td/> styling so should inherit from
		// <tr/> background
		assertThat(row.getCell(0).getCellStyle().getFillForegroundXSSFColor())
				.isEqualTo(new XSSFColor(Color.BLUE, null));
		assertThat(row.getCell(1).getCellStyle().getFillForegroundXSSFColor())
				.isEqualTo(new XSSFColor(Color.BLUE, null));
		assertThat(row.getCell(2).getCellStyle().getFillForegroundXSSFColor())
				.isEqualTo(new XSSFColor(Color.BLUE, null));

		row = sheet.getRow(3);

		// row 3 has explicit <tr/> styling and <td/> styling for some cells
		assertThat(row.getCell(0).getCellStyle().getFillForegroundXSSFColor())
				.isEqualTo(new XSSFColor(Color.RED, null));
		assertThat(row.getCell(1).getCellStyle().getFillForegroundXSSFColor())
				.isEqualTo(new XSSFColor(Color.ORANGE, null));
		assertThat(row.getCell(2).getCellStyle().getFillForegroundXSSFColor())
				.isEqualTo(new XSSFColor(Color.CYAN, null));
	}
}

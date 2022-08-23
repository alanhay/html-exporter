package uk.co.certait.htmlexporter.integration.excel;

import static org.assertj.core.api.Assertions.assertThat;

import java.awt.Color;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xssf.usermodel.extensions.XSSFCellBorder.BorderSide;
import org.junit.Test;

public class CellBorderIntegrationTest extends BaseIntegrationTest {

	/**
	 * Tests that the correct border formatting is correctly applied to the table
	 * cells
	 */
	@Test
	public void testCellBorders() {
		XSSFWorkbook workbook = createWorkbook("/integration/cell-borders.html");
		XSSFSheet sheet = workbook.getSheetAt(0);
		XSSFRow row = sheet.getRow(0);

		assertThat(row.getCell(0).getCellStyle().getBorderTop()).isEqualTo(BorderStyle.THIN);
		assertThat(row.getCell(0).getCellStyle().getBorderColor(BorderSide.TOP))
				.isEqualTo(new XSSFColor(Color.BLACK, null));

		assertThat(row.getCell(0).getCellStyle().getBorderBottom()).isEqualTo(BorderStyle.THIN);
		assertThat(row.getCell(0).getCellStyle().getBorderColor(BorderSide.BOTTOM))
				.isEqualTo(new XSSFColor(Color.BLACK, null));

		assertThat(row.getCell(0).getCellStyle().getBorderLeft()).isEqualTo(BorderStyle.THIN);
		assertThat(row.getCell(0).getCellStyle().getBorderColor(BorderSide.LEFT))
				.isEqualTo(new XSSFColor(Color.BLACK, null));

		assertThat(row.getCell(0).getCellStyle().getBorderRight()).isEqualTo(BorderStyle.THIN);
		assertThat(row.getCell(0).getCellStyle().getBorderColor(BorderSide.RIGHT))
				.isEqualTo(new XSSFColor(Color.BLACK, null));

		row = sheet.getRow(2);

		assertThat(row.getCell(0).getCellStyle().getBorderTop()).isEqualTo(BorderStyle.THIN);
		assertThat(row.getCell(0).getCellStyle().getBorderColor(BorderSide.TOP))
				.isEqualTo(new XSSFColor(Color.BLACK, null));

		assertThat(row.getCell(0).getCellStyle().getBorderBottom()).isEqualTo(BorderStyle.THIN);
		assertThat(row.getCell(0).getCellStyle().getBorderColor(BorderSide.BOTTOM))
				.isEqualTo(new XSSFColor(Color.BLACK, null));

		assertThat(row.getCell(0).getCellStyle().getBorderLeft()).isEqualTo(BorderStyle.THIN);
		assertThat(row.getCell(0).getCellStyle().getBorderColor(BorderSide.LEFT))
				.isEqualTo(new XSSFColor(Color.BLACK, null));

		assertThat(row.getCell(0).getCellStyle().getBorderRight()).isEqualTo(BorderStyle.THIN);
		assertThat(row.getCell(0).getCellStyle().getBorderColor(BorderSide.RIGHT))
				.isEqualTo(new XSSFColor(Color.RED, null));

		row = sheet.getRow(4);

		assertThat(row.getCell(0).getCellStyle().getBorderTop()).isEqualTo(BorderStyle.MEDIUM);
		assertThat(row.getCell(0).getCellStyle().getBorderColor(BorderSide.TOP))
				.isEqualTo(new XSSFColor(Color.BLACK, null));

		assertThat(row.getCell(0).getCellStyle().getBorderBottom()).isEqualTo(BorderStyle.MEDIUM);
		assertThat(row.getCell(0).getCellStyle().getBorderColor(BorderSide.BOTTOM))
				.isEqualTo(new XSSFColor(Color.BLACK, null));

		assertThat(row.getCell(0).getCellStyle().getBorderLeft()).isEqualTo(BorderStyle.MEDIUM);
		assertThat(row.getCell(0).getCellStyle().getBorderColor(BorderSide.LEFT))
				.isEqualTo(new XSSFColor(Color.BLACK, null));

		assertThat(row.getCell(0).getCellStyle().getBorderRight()).isEqualTo(BorderStyle.THICK);
		assertThat(row.getCell(0).getCellStyle().getBorderColor(BorderSide.RIGHT))
				.isEqualTo(new XSSFColor(Color.ORANGE, null));

		row = sheet.getRow(6);

		assertThat(row.getCell(0).getCellStyle().getBorderTop()).isEqualTo(BorderStyle.DASHED);
		assertThat(row.getCell(0).getCellStyle().getBorderColor(BorderSide.TOP))
				.isEqualTo(new XSSFColor(Color.BLACK, null));

		assertThat(row.getCell(0).getCellStyle().getBorderBottom()).isEqualTo(BorderStyle.DASHED);
		assertThat(row.getCell(0).getCellStyle().getBorderColor(BorderSide.BOTTOM))
				.isEqualTo(new XSSFColor(Color.BLACK, null));

		assertThat(row.getCell(0).getCellStyle().getBorderLeft()).isEqualTo(BorderStyle.DASHED);
		assertThat(row.getCell(0).getCellStyle().getBorderColor(BorderSide.LEFT))
				.isEqualTo(new XSSFColor(Color.RED, null));

		assertThat(row.getCell(0).getCellStyle().getBorderRight()).isEqualTo(BorderStyle.DASHED);
		assertThat(row.getCell(0).getCellStyle().getBorderColor(BorderSide.RIGHT))
				.isEqualTo(new XSSFColor(Color.BLACK, null));

		row = sheet.getRow(8);

		assertThat(row.getCell(0).getCellStyle().getBorderTop()).isEqualTo(BorderStyle.DASHED);
		assertThat(row.getCell(0).getCellStyle().getBorderColor(BorderSide.TOP))
				.isEqualTo(new XSSFColor(Color.BLACK, null));

		assertThat(row.getCell(0).getCellStyle().getBorderBottom()).isEqualTo(BorderStyle.DASHED);
		assertThat(row.getCell(0).getCellStyle().getBorderColor(BorderSide.BOTTOM))
				.isEqualTo(new XSSFColor(Color.BLACK, null));

		assertThat(row.getCell(0).getCellStyle().getBorderLeft()).isEqualTo(BorderStyle.DOTTED);
		assertThat(row.getCell(0).getCellStyle().getBorderColor(BorderSide.LEFT))
				.isEqualTo(new XSSFColor(Color.BLACK, null));

		assertThat(row.getCell(0).getCellStyle().getBorderRight()).isEqualTo(BorderStyle.DOTTED);
		assertThat(row.getCell(0).getCellStyle().getBorderColor(BorderSide.RIGHT))
				.isEqualTo(new XSSFColor(Color.BLACK, null));

		row = sheet.getRow(10);

		assertThat(row.getCell(0).getCellStyle().getBorderTop()).isEqualTo(BorderStyle.DOUBLE);
		assertThat(row.getCell(0).getCellStyle().getBorderColor(BorderSide.TOP))
				.isEqualTo(new XSSFColor(Color.BLACK, null));

		assertThat(row.getCell(0).getCellStyle().getBorderBottom()).isEqualTo(BorderStyle.DOUBLE);
		assertThat(row.getCell(0).getCellStyle().getBorderColor(BorderSide.BOTTOM))
				.isEqualTo(new XSSFColor(Color.BLUE, null));

		assertThat(row.getCell(0).getCellStyle().getBorderLeft()).isEqualTo(BorderStyle.DOUBLE);
		assertThat(row.getCell(0).getCellStyle().getBorderColor(BorderSide.LEFT))
				.isEqualTo(new XSSFColor(Color.BLACK, null));

		assertThat(row.getCell(0).getCellStyle().getBorderRight()).isEqualTo(BorderStyle.DOUBLE);
		assertThat(row.getCell(0).getCellStyle().getBorderColor(BorderSide.RIGHT))
				.isEqualTo(new XSSFColor(Color.BLACK, null));

		row = sheet.getRow(12);

		assertThat(row.getCell(0).getCellStyle().getBorderTop()).isEqualTo(BorderStyle.THICK);
		assertThat(row.getCell(0).getCellStyle().getBorderColor(BorderSide.TOP))
				.isEqualTo(new XSSFColor(Color.RED, null));

		assertThat(row.getCell(0).getCellStyle().getBorderBottom()).isEqualTo(BorderStyle.THICK);
		assertThat(row.getCell(0).getCellStyle().getBorderColor(BorderSide.BOTTOM))
				.isEqualTo(new XSSFColor(Color.RED, null));

		assertThat(row.getCell(0).getCellStyle().getBorderLeft()).isEqualTo(BorderStyle.THICK);
		assertThat(row.getCell(0).getCellStyle().getBorderColor(BorderSide.LEFT))
				.isEqualTo(new XSSFColor(Color.RED, null));

		assertThat(row.getCell(0).getCellStyle().getBorderRight()).isEqualTo(BorderStyle.THICK);
		assertThat(row.getCell(0).getCellStyle().getBorderColor(BorderSide.RIGHT))
				.isEqualTo(new XSSFColor(Color.RED, null));
	}
}

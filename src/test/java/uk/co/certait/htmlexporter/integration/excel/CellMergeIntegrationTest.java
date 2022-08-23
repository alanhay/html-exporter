package uk.co.certait.htmlexporter.integration.excel;

import static org.assertj.core.api.Assertions.assertThat;

import java.awt.Color;

import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;

public class CellMergeIntegrationTest extends BaseIntegrationTest {

	/**
	 * Tests that the expected row and column spans are applied to the table cells
	 */
	@Test
	public void testCellBorders() {
		XSSFWorkbook workbook = createWorkbook("/integration/cell-merge.html");
		XSSFSheet sheet = workbook.getSheetAt(0);

		assertThat(sheet.getNumMergedRegions()).isEqualTo(2);
		CellRangeAddress mergedRegion1 = new CellRangeAddress(0, 1, 0, 0);
		CellRangeAddress mergedRegion2 = new CellRangeAddress(2, 3, 0, 1);

		assertThat(sheet.getMergedRegions()).contains(mergedRegion1);
		assertThat(sheet.getMergedRegions()).contains(mergedRegion2);

		assertThat(sheet.getMergedRegion(0).getNumberOfCells()).isEqualTo(2);
		assertThat(sheet.getMergedRegion(0)).contains(sheet.getRow(0).getCell(0).getAddress());
		assertThat(sheet.getMergedRegion(0)).contains(sheet.getRow(1).getCell(0).getAddress());

		assertThat(sheet.getMergedRegion(1).getNumberOfCells()).isEqualTo(4);
		assertThat(sheet.getMergedRegion(1)).contains(sheet.getRow(2).getCell(0).getAddress());
		assertThat(sheet.getMergedRegion(1)).contains(sheet.getRow(2).getCell(1).getAddress());
		assertThat(sheet.getMergedRegion(1)).contains(sheet.getRow(3).getCell(0).getAddress());
		assertThat(sheet.getMergedRegion(1)).contains(sheet.getRow(3).getCell(1).getAddress());

		assertThat(sheet.getRow(0).getCell(0).getStringCellValue()).isEqualTo("cell a");

		assertThat(sheet.getRow(0).getCell(0).getCellStyle().getFillForegroundXSSFColor())
				.isEqualTo(new XSSFColor(Color.YELLOW, null));

		assertThat(sheet.getRow(1).getCell(0).getCellStyle().getFillForegroundXSSFColor())
				.isEqualTo(new XSSFColor(Color.YELLOW, null));

		assertThat(sheet.getRow(2).getCell(0).getStringCellValue()).isEqualTo("cell f");

		assertThat(sheet.getRow(2).getCell(0).getCellStyle().getFillForegroundXSSFColor())
				.isEqualTo(new XSSFColor(Color.CYAN, null));

		assertThat(sheet.getRow(2).getCell(1).getCellStyle().getFillForegroundXSSFColor())
				.isEqualTo(new XSSFColor(Color.CYAN, null));

		assertThat(sheet.getRow(3).getCell(0).getCellStyle().getFillForegroundXSSFColor())
				.isEqualTo(new XSSFColor(Color.CYAN, null));

		assertThat(sheet.getRow(3).getCell(1).getCellStyle().getFillForegroundXSSFColor())
				.isEqualTo(new XSSFColor(Color.CYAN, null));
	}
}

package uk.co.certait.htmlexporter.integration.excel;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;

public class DataFormatIntegrationTest extends BaseIntegrationTest {

	@Test
	public void testDataFormat() {
		XSSFWorkbook workbook = createWorkbook("/integration/data-format.html");
		XSSFSheet sheet = workbook.getSheetAt(0);
		XSSFRow row = sheet.getRow(0);

		DataFormatter formatter = new DataFormatter();
		
		assertThat(row.getCell(0).getStringCellValue()).isEqualTo("some text");

		assertThat(row.getCell(1).getLocalDateTimeCellValue())
				.isEqualTo(LocalDateTime.of(LocalDate.of(2022, 8, 1), LocalTime.of(12, 54, 43)));

		assertThat(row.getCell(1).getCellStyle().getDataFormatString()).isEqualTo("dd-MM-yyyy HH:mm:ss");
		assertThat(formatter.formatCellValue(row.getCell(1))).isEqualTo("01-08-2022 12:54:43");

		assertThat(row.getCell(2).getLocalDateTimeCellValue())
				.isEqualTo(LocalDateTime.of(LocalDate.of(2022, 8, 2), LocalTime.of(12, 34)));

		assertThat(row.getCell(2).getCellStyle().getDataFormatString()).isEqualTo("dd-MM-yy HH:mm");
		assertThat(formatter.formatCellValue(row.getCell(2))).isEqualTo("02-08-22 12:34");
		
		assertThat(row.getCell(3).getLocalDateTimeCellValue())
				.isEqualTo(LocalDateTime.of(LocalDate.of(2022, 8, 3), LocalTime.of(0, 0)));

		assertThat(row.getCell(3).getCellStyle().getDataFormatString()).isEqualTo("dd/MM/yy");
		assertThat(formatter.formatCellValue(row.getCell(3))).isEqualTo("03/08/22");

		assertThat(row.getCell(4).getNumericCellValue()).isEqualTo(354);
		assertThat(row.getCell(4).getCellStyle().getDataFormatString()).isEqualTo("General");

		assertThat(row.getCell(5).getNumericCellValue()).isEqualTo(3.54);
		assertThat(row.getCell(5).getCellStyle().getDataFormatString()).isEqualTo("General");
		
		assertThat(row.getCell(6).getNumericCellValue()).isEqualTo(1234567);
		assertThat(row.getCell(6).getCellStyle().getDataFormatString()).isEqualTo("#,##0");
		assertThat(formatter.formatCellValue(row.getCell(6))).isEqualTo("1,234,567");
		
		assertThat(row.getCell(7).getNumericCellValue()).isEqualTo(1.23456);
		assertThat(row.getCell(7).getCellStyle().getDataFormatString()).isEqualTo("#,##0.000");
		assertThat(formatter.formatCellValue(row.getCell(7))).isEqualTo("1.235");
		
		assertThat(row.getCell(8).getNumericCellValue()).isEqualTo(1234925.7746);
		assertThat(row.getCell(8).getCellStyle().getDataFormatString()).isEqualTo("#,##0.0000");
		
		assertThat(row.getCell(9).getNumericCellValue()).isEqualTo(0.2354);
		assertThat(row.getCell(9).getCellStyle().getDataFormatString()).isEqualTo("0.00%");
		assertThat(formatter.formatCellValue(row.getCell(9))).isEqualTo("23.54%");
		
		assertThat(row.getCell(10).getNumericCellValue()).isEqualTo(0.2356561);
		assertThat(row.getCell(10).getCellStyle().getDataFormatString()).isEqualTo("0.000%");
		assertThat(formatter.formatCellValue(row.getCell(10))).isEqualTo("23.566%");
		
	}
}

package uk.co.certait.htmlexporter.integration.excel;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.co.certait.htmlexporter.writer.excel.ExcelExporter;

public class BaseIntegrationTest {
	private static final Logger logger = LoggerFactory.getLogger(BaseIntegrationTest.class);

	protected XSSFWorkbook createWorkbook(String sourceHtmlPath) {
		ExcelExporter exporter = new ExcelExporter();
		File temp = null;

		try (InputStream in = this.getClass().getResourceAsStream(sourceHtmlPath)) {
			String html = IOUtils.toString(in, "UTF-8");
			temp = File.createTempFile("html-exporter-integration-test", ".xlsx");
			exporter.exportHtml(html, temp);
			XSSFWorkbook workbook = new XSSFWorkbook(temp);
			workbook.close();
			temp.delete();
			in.close();
			return workbook;
		} catch (IOException ioex) {
			logger.error("Error generating Excel workbook", ioex);
		} catch (InvalidFormatException ife) {
			logger.error("Error generating Excel workbook", ife);
		}

		return null;
	}
}

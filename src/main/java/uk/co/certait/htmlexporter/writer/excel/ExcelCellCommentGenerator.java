package uk.co.certait.htmlexporter.writer.excel;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.Comment;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.RichTextString;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import uk.co.certait.htmlexporter.ss.Dimension;

public class ExcelCellCommentGenerator {

	public static void addCellComment(Cell cell, String commentText, Dimension d) {

		Sheet sheet = cell.getSheet();
		Workbook book = sheet.getWorkbook();
		Row row = cell.getRow();

		Drawing drawing = sheet.createDrawingPatriarch();
		CreationHelper factory = book.getCreationHelper();

		ClientAnchor anchor = factory.createClientAnchor();
		anchor.setCol1(cell.getColumnIndex());
		anchor.setCol2(cell.getColumnIndex() + d.getWidth());
		anchor.setRow1(row.getRowNum());
		anchor.setRow2(row.getRowNum() + d.getHeight());

		Comment comment = drawing.createCellComment(anchor);
		RichTextString str = factory.createRichTextString(commentText);
		comment.setString(str);

		cell.setCellComment(comment);
	}
}

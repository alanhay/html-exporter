package uk.co.certait.htmlexporter.writer.excel;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;

import uk.co.certait.htmlexporter.css.CssColorProperty;
import uk.co.certait.htmlexporter.css.CssIntegerProperty;
import uk.co.certait.htmlexporter.css.CssStringProperty;
import uk.co.certait.htmlexporter.css.Style;

public class ExcelStyleGenerator
{
	private Map<Style, XSSFCellStyle> styles;

	public ExcelStyleGenerator()
	{
		styles = new HashMap<Style, XSSFCellStyle>();
	}

	public CellStyle getStyle(Cell cell, Style style)
	{
		XSSFCellStyle cellStyle;

		if (styles.containsKey(style))
		{
			cellStyle = styles.get(style);
		}
		else
		{
			cellStyle = (XSSFCellStyle) cell.getSheet().getWorkbook().createCellStyle();
			
			applyBackground(style, cellStyle);
			applyBorders(style, cellStyle);
			applyFont(cell, style, cellStyle);
			applyAlignment(style, cellStyle);
			applyWidth(cell, style);
			
			styles.put(style, cellStyle);
		}
		
		return cellStyle;
	}

	protected void applyBackground(Style style, XSSFCellStyle cellStyle)
	{
		if (style.isBackgroundSet())
		{
			cellStyle.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
			cellStyle.setFillForegroundColor(new XSSFColor(style.getProperty(CssColorProperty.BACKGROUND)));
		}
	}

	protected void applyBorders(Style style, XSSFCellStyle cellStyle)
	{
		if (style.isBorderWidthSet())
		{
			short width = (short) style.getProperty(CssIntegerProperty.BORDER_WIDTH);

			Color color = style.getProperty(CssColorProperty.BORDER_COLOR) != null ? style
					.getProperty(CssColorProperty.BORDER_COLOR) : Color.BLACK;

			cellStyle.setBorderBottom(BorderStyle.THICK);
			cellStyle.setBorderBottom(width);
			cellStyle.setBottomBorderColor(new XSSFColor(color));

			cellStyle.setBorderTop(BorderStyle.THICK);
			cellStyle.setBorderTop(width);
			cellStyle.setTopBorderColor(new XSSFColor(color));

			cellStyle.setBorderLeft(BorderStyle.THICK);
			cellStyle.setBorderLeft(width);
			cellStyle.setLeftBorderColor(new XSSFColor(color));

			cellStyle.setBorderRight(BorderStyle.THICK);
			cellStyle.setBorderRight(width);
			cellStyle.setRightBorderColor(new XSSFColor(color));
		}
	}

	protected void applyFont(Cell cell, Style style, XSSFCellStyle cellStyle)
	{
		Font font = createFont(cell.getSheet().getWorkbook(), style);
		cellStyle.setFont(font);
	}

	protected void applyAlignment(Style style, XSSFCellStyle cellStyle)
	{
		if (style.isLeftAligned())
		{
			cellStyle.setAlignment(HorizontalAlignment.LEFT);
		}
		else if (style.isRightAligned())
		{
			cellStyle.setAlignment(HorizontalAlignment.RIGHT);
		}
		else if (style.isCenterAligned())
		{
			cellStyle.setAlignment(HorizontalAlignment.CENTER);
		}
	}

	protected void applyWidth(Cell cell, Style style)
	{
		if (style.getProperty(CssIntegerProperty.WIDTH) > 0)
		{
			cell.getSheet().setColumnWidth(cell.getColumnIndex(),
					style.getProperty(CssIntegerProperty.WIDTH) * 50);
		}
	}

	public Font createFont(Workbook workbook, Style style)
	{
		Font font = workbook.createFont();

		if (style.isFontNameSet())
		{
			font.setFontName(style.getProperty(CssStringProperty.FONT_FAMILY));
		}

		if (style.isFontSizeSet())
		{
			font.setFontHeightInPoints((short) style.getProperty(CssIntegerProperty.FONT_SIZE));
		}

		if (style.isColorSet())
		{
			Color color = style.getProperty(CssColorProperty.COLOR);
			
			//if(! color.equals(Color.WHITE)) // POI Bug
			//{
				((XSSFFont) font).setColor(new XSSFColor(color));
			//}
		}

		if (style.isFontBold())
		{
			font.setBoldweight(Font.BOLDWEIGHT_BOLD);
		}

		font.setItalic(style.isFontItalic());

		if (style.isTextUnderlined())
		{
			font.setUnderline(Font.U_SINGLE);
		}

		return font;
	}
}

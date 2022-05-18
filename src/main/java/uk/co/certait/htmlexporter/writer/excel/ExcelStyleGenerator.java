/**
 * Copyright (C) 2012 alanhay <alanhay99@hotmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package uk.co.certait.htmlexporter.writer.excel;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;

import uk.co.certait.htmlexporter.css.CssColorProperty;
import uk.co.certait.htmlexporter.css.CssIntegerProperty;
import uk.co.certait.htmlexporter.css.CssStringProperty;
import uk.co.certait.htmlexporter.css.Style;

public class ExcelStyleGenerator {
	private Map<Style, XSSFCellStyle> styles;

	public ExcelStyleGenerator() {
		styles = new HashMap<Style, XSSFCellStyle>();
	}

	public CellStyle getStyle(Cell cell, Style style) {
		XSSFCellStyle cellStyle;

		if (styles.containsKey(style)) {
			cellStyle = styles.get(style);
		} else {
			cellStyle = (XSSFCellStyle) cell.getSheet().getWorkbook().createCellStyle();

			applyBackground(style, cellStyle);
			applyBorders(style, cellStyle);
			applyFont(cell, style, cellStyle);
			applyHorizontalAlignment(style, cellStyle);
			applyverticalAlignment(style, cellStyle);
			applyWidth(cell, style);

			styles.put(style, cellStyle);
		}

		return cellStyle;
	}

	protected void applyBackground(Style style, XSSFCellStyle cellStyle) {
		if (style.isBackgroundSet()) {
			cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			cellStyle.setFillForegroundColor(
					new XSSFColor(style.getProperty(CssColorProperty.BACKGROUND_COLOR).get(), null));
		}
	}

	protected void applyBorders(Style style, XSSFCellStyle cellStyle) {
		if (style.isBorderWidthSet()) {
			String solidBorder = "solid";

			Color borderColor = style.getProperty(CssColorProperty.BORDER_COLOR).orElse(Color.BLACK);
			Color topBorderColor = style.getProperty(CssColorProperty.BORDER_TOP_COLOR).orElse(borderColor);
			Color bottomBorderColor = style.getProperty(CssColorProperty.BORDER_BOTTOM_COLOR).orElse(borderColor);
			Color leftBorderColor = style.getProperty(CssColorProperty.BORDER_LEFT_COLOR).orElse(borderColor);
			Color rightBorderColor = style.getProperty(CssColorProperty.BORDER_RIGHT_COLOR).orElse(borderColor);

			String borderStyle = style.getProperty(CssStringProperty.BORDER_STYLE).orElse(solidBorder);
			String topBorderStyle = style.getProperty(CssStringProperty.BORDER_TOP_STYLE).orElse(borderStyle);
			String bottomBorderStyle = style.getProperty(CssStringProperty.BORDER_BOTTOM_STYLE).orElse(borderStyle);
			String leftBorderStyle = style.getProperty(CssStringProperty.BORDER_LEFT_STYLE).orElse(borderStyle);
			String rightBorderStyle = style.getProperty(CssStringProperty.BORDER_RIGHT_STYLE).orElse(borderStyle);

			cellStyle.setBorderTop(topBorderStyle.equals(solidBorder) ? BorderStyle.THIN
					: BorderStyle.valueOf(topBorderStyle.toUpperCase()));
			cellStyle.setTopBorderColor(new XSSFColor(topBorderColor, null));

			cellStyle.setBorderBottom(bottomBorderStyle.equals(solidBorder) ? BorderStyle.THIN
					: BorderStyle.valueOf(bottomBorderStyle.toUpperCase()));
			cellStyle.setBottomBorderColor(new XSSFColor(bottomBorderColor, null));

			cellStyle.setBorderLeft(leftBorderStyle.equals(solidBorder) ? BorderStyle.THIN
					: BorderStyle.valueOf(leftBorderStyle.toUpperCase()));
			cellStyle.setLeftBorderColor(new XSSFColor(leftBorderColor, null));

			cellStyle.setBorderRight(rightBorderStyle.equals(solidBorder) ? BorderStyle.THIN
					: BorderStyle.valueOf(rightBorderStyle.toUpperCase()));
			cellStyle.setRightBorderColor(new XSSFColor(rightBorderColor, null));
		}
	}

	protected void applyFont(Cell cell, Style style, XSSFCellStyle cellStyle) {
		Font font = createFont(cell.getSheet().getWorkbook(), style);
		cellStyle.setFont(font);
	}

	protected void applyHorizontalAlignment(Style style, XSSFCellStyle cellStyle) {
		if (style.isHorizontallyAlignedLeft()) {
			cellStyle.setAlignment(HorizontalAlignment.LEFT);
		} else if (style.isHorizontallyAlignedRight()) {
			cellStyle.setAlignment(HorizontalAlignment.RIGHT);
		} else if (style.isHorizontallyAlignedCenter()) {
			cellStyle.setAlignment(HorizontalAlignment.CENTER);
		}
	}

	protected void applyverticalAlignment(Style style, XSSFCellStyle cellStyle) {
		if (style.isVerticallyAlignedTop()) {
			cellStyle.setVerticalAlignment(VerticalAlignment.TOP);
		} else if (style.isVerticallyAlignedBottom()) {
			cellStyle.setVerticalAlignment(VerticalAlignment.BOTTOM);
		} else if (style.isVerticallyAlignedMiddle()) {
			cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
		}
	}

	protected void applyWidth(Cell cell, Style style) {
		if (style.getProperty(CssIntegerProperty.WIDTH).isPresent()) {
			cell.getSheet().setColumnWidth(cell.getColumnIndex(),
					style.getProperty(CssIntegerProperty.WIDTH).get() * 50);
		}
	}

	public Font createFont(Workbook workbook, Style style) {
		Font font = workbook.createFont();

		if (style.isFontNameSet()) {
			font.setFontName(style.getProperty(CssStringProperty.FONT_FAMILY).get());
		}

		if (style.isFontSizeSet()) {
			font.setFontHeightInPoints((short) style.getProperty(CssIntegerProperty.FONT_SIZE).get().intValue());
		}

		if (style.isColorSet()) {
			Color color = style.getProperty(CssColorProperty.COLOR).get();

			// if(! color.equals(Color.WHITE)) // POI Bug
			// {
			((XSSFFont) font).setColor(new XSSFColor(color, null));
			// }
		}

		font.setBold(style.isFontBold());
		font.setItalic(style.isFontItalic());

		if (style.isTextUnderlined()) {
			font.setUnderline(Font.U_SINGLE);
		}

		return font;
	}
}

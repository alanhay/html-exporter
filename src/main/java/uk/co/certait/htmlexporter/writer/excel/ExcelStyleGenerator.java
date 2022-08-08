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

import static uk.co.certait.htmlexporter.writer.TableCellWriter.DATA_NUMERIC_CELL_FORMAT_ATTRIBUTE;
import static uk.co.certait.htmlexporter.writer.TableCellWriter.DATE_CELL_ATTRIBUTE;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellUtil;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.jsoup.nodes.Element;

import uk.co.certait.htmlexporter.css.CssColorProperty;
import uk.co.certait.htmlexporter.css.CssIntegerProperty;
import uk.co.certait.htmlexporter.css.CssStringProperty;
import uk.co.certait.htmlexporter.css.Style;

public class ExcelStyleGenerator {

	private static final Map<BorderMappingKey, BorderStyle> BORDER_STYLE_MAP = new HashMap<>();

	static {
		BORDER_STYLE_MAP.put(new BorderMappingKey("solid", null), BorderStyle.THIN);
		BORDER_STYLE_MAP.put(new BorderMappingKey("solid", "thin"), BorderStyle.THIN);
		BORDER_STYLE_MAP.put(new BorderMappingKey("solid", "medium"), BorderStyle.MEDIUM);
		BORDER_STYLE_MAP.put(new BorderMappingKey("solid", "thick"), BorderStyle.THICK);
	}

	private Map<StyleCacheKey, XSSFCellStyle> styles;

	public ExcelStyleGenerator() {
		styles = new HashMap<StyleCacheKey, XSSFCellStyle>();
	}

	/**
	 * Creates and caches an Excel {@link CellStyle} object based on the specified
	 * {@link Style}.
	 * 
	 * Note that when we cache the style we need to take into account any data
	 * format specified for the cell due to a POI defect:
	 * https://bz.apache.org/bugzilla/show_bug.cgi?id=59442
	 * 
	 * Which means that we cannot use the utility method
	 * {@link CellUtil#setCellStyleProperty(Cell, String, Object)} that would allow
	 * us to modify the format only for that cell.
	 * 
	 * We cannot do the following as that changes the data format for all cells with
	 * that style i.e. Excel will attempt to display a numeric cell as a date for
	 * any numeric cell sharing the same style as date cell and where we have set
	 * the format as below.
	 * 
	 * <code>cell.getCellStyle().setDataFormat(createHelper.createDataFormat().getFormat(getDateCellFormat(element)));</code>
	 * 
	 * Essentially, then, if we want to cache styles (and we must) then our cache
	 * must reflect both the style and the format.
	 * 
	 * @param element The HTML element being processed
	 * @param cell    The Excel cell being generated
	 * @param style   The Style object computed from the CSS
	 * @return A {@link CellStyle} that can be applied to the Excel style
	 */
	public CellStyle getStyle(Element element, Cell cell, Style style) {
		XSSFCellStyle cellStyle;
		CreationHelper createHelper = cell.getSheet().getWorkbook().getCreationHelper();
		short dataFormat = -1;

		if (element.hasAttr(DATE_CELL_ATTRIBUTE)) {
			dataFormat = createHelper.createDataFormat().getFormat(element.attr(DATE_CELL_ATTRIBUTE));
		} else if (element.hasAttr(DATA_NUMERIC_CELL_FORMAT_ATTRIBUTE)) {
			dataFormat = createHelper.createDataFormat().getFormat(element.attr(DATA_NUMERIC_CELL_FORMAT_ATTRIBUTE));
		}

		StyleCacheKey styleCacheKey = new StyleCacheKey(style, dataFormat);

		if (styles.containsKey(styleCacheKey)) {
			cellStyle = styles.get(styleCacheKey);
		} else {
			cellStyle = (XSSFCellStyle) cell.getSheet().getWorkbook().createCellStyle();

			applyBackground(style, cellStyle);
			applyBorders(style, cellStyle);
			applyFont(cell, style, cellStyle);
			applyHorizontalAlignment(style, cellStyle);
			applyverticalAlignment(style, cellStyle);
			applyWidth(cell, style);

			if (dataFormat > -1) {
				cellStyle.setDataFormat(dataFormat);
			}

			styles.put(styleCacheKey, cellStyle);
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

		Color borderColor = style.getProperty(CssColorProperty.BORDER_COLOR).orElse(Color.BLACK);

		BorderStyle topBorderStyle = getBorderStyle(style, CssStringProperty.BORDER_TOP_STYLE,
				CssStringProperty.BORDER_TOP_WIDTH);

		if (topBorderStyle != null) {
			cellStyle.setBorderTop(topBorderStyle);
			Color topBorderColor = style.getProperty(CssColorProperty.BORDER_TOP_COLOR).orElse(borderColor);
			cellStyle.setTopBorderColor(new XSSFColor(topBorderColor, null));
		}

		BorderStyle bottomBorderStyle = getBorderStyle(style, CssStringProperty.BORDER_BOTTOM_STYLE,
				CssStringProperty.BORDER_BOTTOM_WIDTH);

		if (bottomBorderStyle != null) {
			cellStyle.setBorderBottom(bottomBorderStyle);
			Color bottomBorderColor = style.getProperty(CssColorProperty.BORDER_BOTTOM_COLOR).orElse(borderColor);
			cellStyle.setBottomBorderColor(new XSSFColor(bottomBorderColor, null));
		}

		BorderStyle leftBorderStyle = getBorderStyle(style, CssStringProperty.BORDER_LEFT_STYLE,
				CssStringProperty.BORDER_LEFT_WIDTH);

		if (leftBorderStyle != null) {
			cellStyle.setBorderLeft(leftBorderStyle);
			Color leftBorderColor = style.getProperty(CssColorProperty.BORDER_LEFT_COLOR).orElse(borderColor);
			cellStyle.setLeftBorderColor(new XSSFColor(leftBorderColor, null));
		}

		BorderStyle rightBorderStyle = getBorderStyle(style, CssStringProperty.BORDER_RIGHT_STYLE,
				CssStringProperty.BORDER_RIGHT_WIDTH);

		if (rightBorderStyle != null) {
			cellStyle.setBorderRight(rightBorderStyle);
			Color rightBorderColor = style.getProperty(CssColorProperty.BORDER_RIGHT_COLOR).orElse(borderColor);
			cellStyle.setRightBorderColor(new XSSFColor(rightBorderColor, null));
		}
	}

	protected BorderStyle getBorderStyle(Style style, CssStringProperty borderStyleProperty,
			CssStringProperty borderWidthProperty) {
		BorderStyle excelBorderStyle = null;

		String cssBorderStyle = style.getProperty(borderStyleProperty)
				.orElse(style.getProperty(CssStringProperty.BORDER_STYLE).orElse(null));

		String cssBorderWidth = style.getProperty(borderWidthProperty)
				.orElse(style.getProperty(CssStringProperty.BORDER_WIDTH).orElse(null));

		excelBorderStyle = BORDER_STYLE_MAP.get(new BorderMappingKey(cssBorderStyle, cssBorderWidth));

		if (excelBorderStyle == null) {
			try {
				excelBorderStyle = BorderStyle.valueOf(cssBorderStyle.toUpperCase());
			} catch (IllegalArgumentException ex) {
				// ignore unsupported border style
			}
		}

		return excelBorderStyle;
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
			String fontName = style.getProperty(CssStringProperty.FONT_FAMILY).get().split(",")[0].trim()
					.replaceAll("\"", "");
			font.setFontName(fontName);
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

	static class BorderMappingKey {
		private String borderStyle;
		private String borderWidth;

		public BorderMappingKey(String borderStyle, String borderWidth) {
			this.borderStyle = borderStyle;
			this.borderWidth = borderWidth;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj) {
				return true;
			} else if (obj instanceof BorderMappingKey) {
				BorderMappingKey other = (BorderMappingKey) obj;
				return new EqualsBuilder().append(this.borderStyle, other.borderStyle)
						.append(this.borderWidth, other.borderWidth).isEquals();
			}

			return false;

		}

		@Override
		public int hashCode() {
			return new HashCodeBuilder().append(this.borderStyle).append(this.borderWidth).toHashCode();
		}
	}

	static class StyleCacheKey {
		private Style style;
		private Short format;

		private StyleCacheKey(Style style, Short format) {
			this.style = style;
			this.format = format;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj) {
				return true;
			} else if (obj instanceof StyleCacheKey) {
				StyleCacheKey other = (StyleCacheKey) obj;
				return new EqualsBuilder().append(this.style, other.style).append(this.format, other.format).isEquals();
			}

			return false;

		}

		@Override
		public int hashCode() {
			return new HashCodeBuilder().append(this.style).append(this.format).toHashCode();
		}
	}
}

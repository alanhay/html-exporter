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
package uk.co.certait.htmlexporter.writer;

import javax.swing.text.DateFormatter;
import java.text.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.*;

import uk.co.certait.htmlexporter.ss.CellRange;
import uk.co.certait.htmlexporter.ss.DefaultTableCellReference;
import uk.co.certait.htmlexporter.ss.Dimension;
import uk.co.certait.htmlexporter.ss.Function;
import uk.co.certait.htmlexporter.ss.RangeReferenceTracker;

/**
 * 
 * @author alanhay
 * 
 */
public abstract class AbstractTableCellWriter implements TableCellWriter {
	private RangeReferenceTracker tracker;

	private static final String DEFAULT_DATE_PATTERN = "yyyy-MM-dd";
	private static final String DEFAULT_DATE_TIME_PATTERN = DEFAULT_DATE_PATTERN + " hh:mm a";

	private String datePattern;
	private String dateTimePattern;

	private static final Pattern pBracketed = Pattern.compile("^\\((.*)\\)$");
	private static final Pattern pNumeric = Pattern.compile("^(-?\\d+\\.?\\d+\\s*%?)$");
	private static final Pattern pDate = Pattern.compile("^(\\d{1,2}/\\d{1,2}/\\d{4})$|^(\\d{4}-\\d{1,2}-\\d{1,2})$");
	private static final Pattern pDateTime = Pattern.compile("^(\\d{1,2}/\\d{1,2}/\\d{4})[\\s-]+(\\d{1,2}:\\d{2})\\s*([APap][Mm])$|" +
			"^(\\d{1,2}/\\d{1,2}/\\d{4})[\\s-]+(\\d{1,2}:\\d{2})$");

	private final Map<String, DateFormat> dateFormatterCache = new HashMap<>();

	public AbstractTableCellWriter() {
		tracker = new RangeReferenceTracker();
	}

	public void writeCell(Element element, int rowIndex, int columnIndex) {
		renderCell(element, rowIndex, columnIndex);

		if (isFunctionGroupCell(element)) {
			for (String rangeName : getFunctionGroupReferences(element)) {
				tracker.addCelltoRange(rangeName, new DefaultTableCellReference(rowIndex, columnIndex));
			}
		}

		if (isFunctionOutputCell(element)) {
			String rangeName = getFunctionOutputReference(element);
			addFunctionCell(rowIndex, columnIndex, tracker.getCellRange(rangeName), Function.SUM);
		}
	}

	/**
	 * Returns the actual text of the innermost child element for this cell.
	 * 
	 * @param element
	 * 
	 * @return The text to be output for this Cell.
	 */
	public String getElementText(Element element) {
		String text = "";
		for (Node child : element.childNodes()) {
			if(child instanceof TextNode) {
				text += ((TextNode) child).text();
			} else if(child instanceof Element) {
				Element childElement = (Element) child;
				// do more of these for p or other tags you want a new line for
				if (childElement.tag().getName().equalsIgnoreCase("br")) {
					text += "\n";
				}
				text += getElementText(childElement);
			}
		}

		return text;
	}

	@Deprecated
	public String oldGetElementText(Element element) {
		String text = element.ownText();

		for (Element child : element.children()) {
			text = child.ownText();
		}

		return text;
	}


	/**
	 * Checks the for the presence of the 'colspan' attribute on the cell and
	 * returns the value if this attribute if present, otherwise 1.
	 * 
	 * @param element
	 * 
	 * @return True if the this Element spans multiple columns (has the 'colspan'
	 *         attribute defined, otherwise false.
	 */
	protected boolean spansMultipleColumns(Element element) {
		boolean spansMultipleColumns = false;

		if (element.hasAttr(COLUMN_SPAN_ATTRIBUTE)) {
			int columnCount = Integer.parseInt(element.attr(COLUMN_SPAN_ATTRIBUTE));

			spansMultipleColumns = columnCount > 1;
		}

		return spansMultipleColumns;
	}

	protected boolean definesFreezePane(Element element) {
		boolean definesFreezePane = false;

		if (element.hasAttr(DATA_FREEZE_PANE_CELL)) {
			if (Boolean.parseBoolean(element.attr(DATA_FREEZE_PANE_CELL))) {
				definesFreezePane = true;
			}
		}

		return definesFreezePane;
	}

	/**
	 * Checks the for the presence of the 'colspan' attribute on the cell and
	 * returns the value if this attribute if present, otherwise 1.
	 * 
	 * @param element
	 * 
	 * @return The number of columns this cell should span.
	 */
	protected int getMergedColumnCount(Element element) {
		int columnCount = 1;

		if (spansMultipleColumns(element)) {
			columnCount = Integer.parseInt(element.attr(COLUMN_SPAN_ATTRIBUTE));
		}

		return columnCount;
	}

	public void setDatePattern(String datePattern) {
		if(this.datePattern != null && this.dateTimePattern != null) {
			// update dateTimePattern with the new datePattern if it matches the default dateTimePattern
			if((this.datePattern + " hh:mm a").equals(this.dateTimePattern)) {
				setDateTimePattern(datePattern + " hh:mm a");
			}
		}
		this.datePattern = datePattern;
	}

	public String getDatePattern() {
		if(this.datePattern == null || this.datePattern.isEmpty()) {
			setDatePattern(DEFAULT_DATE_PATTERN);
		}
		return this.datePattern;
	}

	public void setDateTimePattern(String dateTimePattern) {
		this.dateTimePattern = dateTimePattern;
	}

	public String getDateTimePattern() {
		if(dateTimePattern == null || dateTimePattern.isEmpty()) {
			String datePattern = getDatePattern();
			setDateTimePattern(datePattern + " hh:mm a");
		}
		return this.dateTimePattern;
	}

	/**
	 * 
	 * @param element
	 * 
	 * @return
	 */
	protected boolean isFunctionGroupCell(Element element) {
		return element.hasAttr(DATA_GROUP_ATTRIBUTE);
	}

	protected boolean isDateCell(Element element) {
		return element.hasAttr(DATE_CELL_ATTRIBUTE);
	}

	protected String getDateCellFormat(Element element) {
		return element.attr(DATE_CELL_ATTRIBUTE);
	}

	protected boolean isPossibleDateCell(Element element) {
		if (!element.hasAttr(DATA_TEXT_CELL)) {
			String value = element.ownText();
			if(pDate.matcher(value).matches()) {
				return true;
			}
		}
		return false;
	}

	protected boolean isPossibleDateTimeCell(Element element) {
		if (!element.hasAttr(DATA_TEXT_CELL)) {
			String value = element.ownText();
			if(pDateTime.matcher(value).matches()) {
				return true;
			}
		}
		return false;
	}

	protected Date getDateValue(Element element, String datePattern) {
		DateFormat dateFormatter = getDateFormatter(datePattern);
		try {
			String value = element.ownText();
			return dateFormatter.parse(value);
		} catch(ParseException e) {
			return null;
		}
	}

	protected Date getDateTimeValue(Element element, String dateTimePattern) {
		String value = element.ownText();
		DateFormat dateFormatter = getDateFormatter(dateTimePattern);
		try {
			value = value.replaceAll("\\s+-+\\s+", " "); // replace the dash between date and time with a space (this is unique to our reporting module only)
			return dateFormatter.parse(value);
		} catch(ParseException e) {
			return null;
		}
	}

	protected DateFormat getDateFormatter(String pattern) {
		DateFormat dateFormatter = (DateFormat)dateFormatterCache.get(pattern);
		if(dateFormatter == null) {
			dateFormatter = new SimpleDateFormat(pattern);
			dateFormatterCache.put(pattern, dateFormatter);
		}
		return dateFormatter;
	}
	
	protected boolean isNumericCell(Element element) {
		return getNumericValue(element) != null;
	}

	protected boolean isPercentageCell(Element element) {
		return getNumericValue(element) != null && element.ownText().trim().endsWith("%");
	}

	/**
	 * 
	 * @param element
	 * 
	 * @return
	 */
	protected String[] getFunctionGroupReferences(Element element) {
		return getAttributeValues(element, DATA_GROUP_ATTRIBUTE);
	}

	/**
	 * 
	 * @param element
	 * 
	 * @return
	 */
	protected boolean isFunctionOutputCell(Element element) {
		boolean functionOutputCell = false;

		for (Attribute attribute : element.attributes()) {
			if (attribute.getKey().equalsIgnoreCase(DATA_GROUP_OUTPUT_ATTRIBUTE)) {
				functionOutputCell = true;
				break;
			}
		}

		return functionOutputCell;
	}

	/**
	 * 
	 * @param element
	 * 
	 * @return
	 */
	protected String getFunctionOutputReference(Element element) {
		String functionOutputGroup = null;

		for (Attribute attribute : element.attributes()) {
			if (attribute.getKey().equalsIgnoreCase(DATA_GROUP_OUTPUT_ATTRIBUTE)) {
				functionOutputGroup = attribute.getValue();
				break;
			}
		}

		return functionOutputGroup;
	}

	/**
	 * 
	 * @param element
	 * @return
	 */
	protected String getCellCommentText(Element element) {
		String commentText = null;

		for (Attribute attribute : element.attributes()) {
			if (attribute.getKey().equalsIgnoreCase(DATA_CELL_COMMENT_ATTRIBUTE)) {
				commentText = attribute.getValue();
				break;
			}
		}

		return StringUtils.trimToNull(commentText);
	}

	/**
	 * Return the Dimension for the cell comment. Return a Dimension of 3,1 if the
	 * dimension attribute is not present or has an invalid value.
	 * 
	 * @return
	 */
	protected Dimension getCellCommentDimension(Element element) {
		Dimension dimension = null;

		for (Attribute attribute : element.attributes()) {
			if (attribute.getKey().equalsIgnoreCase(DATA_CELL_COMMENT_DIMENSION_ATTRIBUTE)) {
				try {
					dimension = new Dimension(attribute.getValue());
				} catch (IllegalArgumentException ex) {
					dimension = new Dimension(3, 1);
				}
			}
		}

		return dimension != null ? dimension : new Dimension(3, 1);
	}

	/**
	 * 
	 * @param element
	 * @param attributeName
	 * 
	 * @return
	 */
	protected String[] getAttributeValues(Element element, String attributeName) {
		String values[] = null;

		if (element.hasAttr(attributeName)) {
			values = element.attr(attributeName).toLowerCase().split(",");

			for (String value : values) {
				value = value.trim().toLowerCase();
			}
		}

		return values;
	}

	/**
	 * Returns the numeric value of the cell if it is numeric, otherwise null.
	 *
	 * @param element The Element representing the cell.
	 * 
	 * @return The numeric value of the cell if it is numeric, otherwise null.
	 */
	public Double getNumericValue(Element element) {
		Double numericValue = null;

		if (!element.hasAttr(DATA_TEXT_CELL)) {
			try {
				String value = element.ownText();
				value = value.replaceAll(",", "");

				Matcher mBracketed = pBracketed.matcher(value);
				boolean isBracketed = false;
				if(mBracketed.matches()) {
					value = mBracketed.group(1);
					isBracketed = true;
				}

				if(pNumeric.matcher(value).matches()) {
					numericValue = NumberFormat.getInstance().parse(value).doubleValue();
					if(isBracketed) {
						numericValue = -numericValue;
					}
				}
			} catch (ParseException e) {
				return null;
			}
		}

		return numericValue;
	}

	public abstract void renderCell(Element element, int rowIndex, int columnIndex);

	public abstract void addFunctionCell(int rowIndex, int columnIndex, CellRange range, Function function);
}

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

import java.text.NumberFormat;
import java.text.ParseException;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Element;

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
	 * Returns the actual text of the element for this cell.
	 * 
	 * @param element
	 * 
	 * @return The text to be output for this Cell.
	 */
	public String getElementText(Element element) {
		String text = element.text();

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
			int columnCount = Integer.parseInt(element.attr(COLUMN_SPAN_ATTRIBUTE).replaceAll("\\s",""));

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
			columnCount = Integer.parseInt(element.attr(COLUMN_SPAN_ATTRIBUTE).replaceAll("\\s",""));
		}

		return columnCount;
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
	
	protected boolean isNumericCell(Element element) {
		return (!element.hasAttr(DATA_TEXT_CELL) && getNumericValue(element) != null);
	}
	
	protected String getNumericCellFormat(Element element) {
		return element.attr(NUMBER_CELL_ATTRIBUTE);
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
	 * 
	 * @param element
	 * 
	 * @return
	 */
	public Double getNumericValue(Element element) {
		Double numericValue = null;

		try {
			numericValue = NumberFormat.getInstance().parse(element.ownText()).doubleValue();
		} catch (ParseException e) {

		}

		return numericValue;
	}
	

	public abstract void renderCell(Element element, int rowIndex, int columnIndex);

	public abstract void addFunctionCell(int rowIndex, int columnIndex, CellRange range, Function function);
}

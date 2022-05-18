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
package uk.co.certait.htmlexporter.ss;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.list.GrowthList;

/**
 * A CellRangeRow represents a row of data within a CellRange.
 * 
 * 
 * @author alanhay
 * 
 */
public class CellRangeRow {
	private int index;
	private List<TableCellReference> cells;

	@SuppressWarnings("unchecked")
	protected CellRangeRow(int index) {
		this.index = index;
		cells = GrowthList.decorate(new ArrayList<TableCellReference>());
	}

	/**
	 * Adds a reference to the specified cell to this row. The cell will be inserted
	 * at the position corresponding to the cell's column index.
	 * 
	 * @throws IllegalArgumentException if the cell reference is null.
	 * 
	 * @param cell
	 */
	public void addCell(TableCellReference cell) {
		if (cell == null) {
			throw new IllegalArgumentException();
		}

		// allows cells to be added in any order.
		if (cells.size() > cell.getColumnIndex()) {
			cells.remove(cell.getColumnIndex());
		}

		cells.add(cell.getColumnIndex(), cell);
	}

	/**
	 * A row is regarded as contiguous if there there are no gaps in the cell
	 * references if refers to. Thus for a row with cell references in columns 2,3
	 * and 4, [-][-][O][O][O], this would be true. Add a cell reference at column 6
	 * and this would be false [-][-][O][O][O][-][O].
	 * 
	 * @return True if this row is contiguous, otherwise false.
	 */
	protected boolean isContiguous() {
		boolean isContiguous = true;

		if (!cells.isEmpty()) {
			int firstPopulatedColumn = getFirstPopulatedColumn();
			int lastPopulatedColumn = getLastPopulatedColumn();

			for (int i = firstPopulatedColumn; i < lastPopulatedColumn; ++i) {
				if (cells.get(i) == null) {
					isContiguous = false;
					break;
				}
			}
		} else {
			isContiguous = false;
		}

		return isContiguous;
	}

	/**
	 * 
	 * @return The index of the first populated column in this row. Returns -1 if
	 *         the row holds no cell references.
	 */
	protected int getFirstPopulatedColumn() {
		int firstColumn = -1;

		for (int i = 0; i < cells.size(); ++i) {
			if (cells.get(i) != null) {
				firstColumn = i;
				break;
			}
		}

		return firstColumn;
	}

	/**
	 * Returns the index of the last populated column in this row. As null
	 * references cannot be added to a row this should always be the same as the
	 * size of the Collection holding the cell references - 1.
	 * 
	 * @return The index of the last populated column. Returns -1 if this row is
	 *         Empty.
	 */
	protected int getLastPopulatedColumn() {
		int lastColumn = -1;

		for (int i = cells.size() - 1; i > -1; --i) {
			if (cells.get(i) != null) {
				lastColumn = i;
				break;
			}
		}

		return lastColumn;
	}

	/**
	 * 
	 * @return True if this row contains no cell references, otherwise false.
	 */
	public boolean isEmpty() {
		boolean isEmpty = true;

		for (TableCellReference cell : cells) {
			if (cell != null) {
				isEmpty = false;
				break;
			}
		}

		return isEmpty;
	}

	/**
	 * Returns the width of this row. The width represents the difference between
	 * the first non-null column and the last non-null column. Thus, for example,
	 * for a row holding references in columns 3, 4, and 6, [-][-][-][O][O][-][O]
	 * the width would be 4.
	 * 
	 * @return The width of this row.
	 */
	public int getWidth() {
		int width = 0;

		if (!isEmpty()) {
			width = getLastPopulatedColumn() - getFirstPopulatedColumn() + 1;
		}

		return width;
	}

	/**
	 * 
	 * @return A list of the cell references held by this row.
	 */
	public List<TableCellReference> getCells() {
		return cells;
	}

	/**
	 * 
	 * @return The first cell referenced by this row.
	 */
	public TableCellReference getFirstCell() {
		return cells.get(getFirstPopulatedColumn());
	}

	/**
	 * 
	 * @return The last cell referenced by this row.
	 */
	public TableCellReference getLastCell() {
		return cells.get(getLastPopulatedColumn());
	}

	/**
	 * 
	 * @return The underlying index of this row.
	 */
	public int getIndex() {
		return index;
	}

	public String toString() {
		StringBuilder builder = new StringBuilder();

		for (TableCellReference cell : cells) {
			if (cell != null) {
				builder.append("[").append(cell.getRowIndex()).append(",").append(cell.getColumnIndex()).append("]");
			} else {
				builder.append("[x]");
			}
		}

		return builder.toString();
	}
}

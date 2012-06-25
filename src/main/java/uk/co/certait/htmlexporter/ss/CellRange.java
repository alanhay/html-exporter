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

/**
 * A CellRange represents a range of cells. The range may or may not be
 * contiguous. A CellRange is considered contiguous when traversing the range
 * form the top left to bottom right there are no gaps found.
 * 
 * @author alanhay
 * 
 */
public class CellRange
{
	private List<CellRangeRow> rows;
	private List<CellRangeObserver> listeners;

	public CellRange()
	{
		rows = new ArrayList<CellRangeRow>();
		listeners = new ArrayList<CellRangeObserver>();
	}

	/**
	 * 
	 * @param cell
	 */
	public void addCell(TableCellReference cell)
	{
		if (rows.size() == 0)
		{
			rows.add(new CellRangeRow(cell.getRowIndex()));
		}
		else if (isCellInNewRow(cell))
		{
			int lastRowIndex = rows.get(rows.size() - 1).getIndex();

			for (int i = lastRowIndex; i < cell.getRowIndex(); ++i)
			{
				rows.add(new CellRangeRow(i + 1));
			}
		}

		rows.get(rows.size() - 1).addCell(cell);

		notifyObservers();
	}

	/**
	 * 
	 * @param listener
	 */
	public void addCellRangeObserver(CellRangeObserver listener)
	{
		listeners.add(listener);
	}

	/**
	 * Notify any registered observers that a cell has been added.
	 */
	private void notifyObservers()
	{
		for (CellRangeObserver listener : listeners)
		{
			listener.cellRangeUpdated(this);
		}
	}

	/**
	 * 
	 * @param cell
	 *            The cell to be added.
	 * 
	 * @return True if this cell references a previously unreferenced row,
	 *         otherwise false.
	 */
	protected boolean isCellInNewRow(TableCellReference cell)
	{
		boolean isNewRow = true;

		for (CellRangeRow row : rows)
		{
			if (cell.getRowIndex() == row.getIndex())
			{
				isNewRow = false;
			}
		}

		return isNewRow;
	}

	/**
	 * A CellRange is considered to be contiguous if all rows within it are
	 * contiguous and the first and last populated columns for all rows have the
	 * same indices.
	 * 
	 * @return True if this CellRange is contiguous, otherwise false.
	 */
	public boolean isContiguous()
	{
		boolean isContiguous = true;

		int firstColumn = -99;
		int lastColumn = -99;

		boolean firstLoop = true;

		for (CellRangeRow row : rows)
		{
			if (!row.isContiguous())
			{
				isContiguous = false;
				break;
			}
			else
			{
				if (!firstLoop)
				{
					if (row.getFirstPopulatedColumn() != firstColumn || row.getLastPopulatedColumn() != lastColumn)
					{
						isContiguous = false;
						break;
					}
				}
				else
				{
					firstColumn = row.getFirstPopulatedColumn();
					lastColumn = row.getLastPopulatedColumn();
					firstLoop = false;
				}
			}
		}

		return isContiguous;
	}

	/**
	 * 
	 * @return
	 */
	public int getHeight()
	{
		return rows.size();
	}

	/**
	 * 
	 * @return
	 */
	public boolean isEmpty()
	{
		boolean isEmpty = true;

		for (CellRangeRow row : rows)
		{
			if (!row.isEmpty())
			{
				isEmpty = false;
				break;
			}
		}

		return isEmpty;
	}

	/**
	 * 
	 * @return The first cell in this range, that is the cell at the top left of
	 *         the range.
	 */
	public TableCellReference getFirstCell()
	{
		return rows.get(0).getFirstCell();
	}

	/**
	 * 
	 * @return The last cell in this range, that is the cell at the bottom right
	 *         of the range.
	 */
	public TableCellReference getLastCell()
	{
		return rows.get(rows.size() - 1).getLastCell();
	}

	/**
	 * 
	 * @return The rows which this range references.
	 */
	public List<CellRangeRow> getRows()
	{
		return rows;
	}

	/**
	 * 
	 */
	public String toString()
	{
		StringBuilder builder = new StringBuilder();

		for (CellRangeRow row : rows)
		{
			builder.append("\nIndex").append(row.getIndex()).append(": ");
			builder.append(row.toString()).append("\n");
		}

		return builder.toString();
	}
}

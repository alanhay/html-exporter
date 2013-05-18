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
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.collections.list.GrowthList;

/**
 * Tracks cells as they are added to a document, taking into account any row and
 * column spans. Clients should call addCell(...) after each cell is added to
 * the document. Clients can then retrieve the next insert point for any row by
 * calling getNextColumnIndexForRow(..).
 * 
 * 
 * @author alanhay
 * 
 */
public class CellTracker
{
	private Map<Integer, List<String>> rows;

	public CellTracker()
	{
		rows = new TreeMap<Integer, List<String>>();
	}

	/**
	 * Notify the tracker that a cell has been added at the specified row and
	 * column index and with the specified row and column span.
	 * 
	 * @param rowIndex
	 * @param columnIndex
	 * @param rowSpan
	 * @param columnSpan
	 */
	public void addCell(int rowIndex, int columnIndex, int rowSpan, int columnSpan)
	{
		updateRows(rowIndex, columnIndex, rowSpan, columnSpan);
	}

	/**
	 * <p>
	 * Updates the internal Map to reflect current state of the document with
	 * regard to those cells which have been filled for all rows.
	 * </p>
	 * <p>
	 * If, for example, cells have been added to row 2 at columns 0, 1 and 2 and
	 * the cell at column 2 has a row span of 2 and a column span of 2, the map
	 * will be populated as follows:
	 * </p>
	 * <p>
	 * 2=[0][0][0][0][x]<br/>
	 * 3=[x][x][0][0][x]
	 * </p>
	 * 
	 * <p>
	 * When row 3 is populated, columns 2 and 3 are already occupied by the
	 * merge from the previous row. After columns 0 and 1 are populated the next
	 * available cell will be at column 5.
	 * </p>
	 * 
	 * @param element
	 * @param rowIndex
	 */
	@SuppressWarnings("unchecked")
	protected void updateRows(int rowIndex, int columnIndex, int rowSpan, int columnSpan)
	{
		for (int i = rowIndex; i < rowIndex + rowSpan; ++i)
		{
			if (!rows.containsKey(i))
			{
				List<String> cells = GrowthList.decorate(new ArrayList<String>());

				rows.put(i, cells);
			}

			for (int j = columnIndex; j < columnIndex + columnSpan; ++j)
			{
				List<String> cells = rows.get(i);

				if (cells.size() > j)
				{
					cells.remove(j);
				}

				// this can be anything but null but add a nice String for
				// debugging.
				String s = "[" + (i) + "," + (j) + "]";
				cells.add((j), s);
			}
		}
	}

	/**
	 * 
	 * @param rowIndex
	 * 
	 * @return The next available column for the specified row.
	 */
	public int getNextColumnIndexForRow(int rowIndex)
	{
		int columnIndex = 0;

		if (rows.get(rowIndex) != null)
		{
			for (String s : rows.get(rowIndex))
			{
				if (s == null)
				{
					break;
				}

				++columnIndex;
			}
		}

		return columnIndex;
	}

	/**
	 * 
	 * @return
	 */
	protected int getTrackedRowCount()
	{
		return rows.size();
	}

	public String toString()
	{
		StringBuilder builder = new StringBuilder();

		for (Integer i : rows.keySet())
		{
			builder.append("Row ").append(i).append(":");

			for (String s : rows.get(i))
			{
				if (s != null)
				{
					builder.append(s);
				}
				else
				{
					builder.append("[x,x]");
				}
			}

			builder.append("\n");
		}

		return builder.toString();
	}
}

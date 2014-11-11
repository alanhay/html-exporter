package uk.co.certait.htmlexporter.writer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.collections.list.GrowthList;
import org.apache.poi.ss.usermodel.Cell;

public class RowTracker {
	private Map<Integer, List<String>> rows;

	public RowTracker() {
		rows = new TreeMap<Integer, List<String>>();
	}

	protected void addCell(int rowIndex, int columnIndex, int rowSpan, int columnSpan) {
		updateRows(rowIndex, columnIndex, rowSpan, columnSpan);
	}

	/**
	 * 
	 * @param element
	 * @param rowIndex
	 */
	@SuppressWarnings("unchecked")
	protected void updateRows(int rowIndex, int columnIndex, int rowSpan, int columnSpan) {
		for (int i = rowIndex; i < rowIndex + rowSpan; ++i) {
			if (!rows.containsKey(i)) {
				List<String> cells = GrowthList.decorate(new ArrayList<Cell>());

				rows.put(i, cells);
			}

			for (int j = columnIndex; j < columnIndex + columnSpan; ++j) {
				List<String> cells = rows.get(i);

				if (cells.size() > j) {
					cells.remove(j);
				}

				String s = "[" + (i) + "," + (j) + "]";
				cells.add((j), s);
			}
		}
	}

	/**
	 * 
	 * @param rowIndex
	 * 
	 * @return
	 */
	public int getNextColumnIndexForRow(int rowIndex) {
		int columnIndex = 0;

		if (rows.get(rowIndex) != null) {
			for (String s : rows.get(rowIndex)) {
				if (s == null) {
					break;
				}

				++columnIndex;
			}
		}

		return columnIndex;
	}

	public int getTrackedRowCount() {
		return rows.size();
	}

	public String toString() {
		StringBuilder builder = new StringBuilder();

		for (Integer i : rows.keySet()) {
			builder.append("Row ").append(i).append(":");

			for (String s : rows.get(i)) {
				if (s != null) {
					builder.append(s);
				} else {
					builder.append("[x,x]");
				}
			}

			builder.append("\n");
		}

		return builder.toString();
	}
}

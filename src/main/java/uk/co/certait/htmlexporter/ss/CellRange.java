package uk.co.certait.htmlexporter.ss;

import java.util.ArrayList;
import java.util.List;

public class CellRange
{
	private List<CellRangeRow> rows;
	private List<CellRangeObserver> listeners;

	public CellRange()
	{
		rows = new ArrayList<CellRangeRow>();
		listeners = new ArrayList<CellRangeObserver>();
	}

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
		
		notifyListeners();
	}
	
	public void addCellRangeListener(CellRangeObserver listener)
	{
		listeners.add(listener);
	}
	
	protected void notifyListeners()
	{
		for(CellRangeObserver listener : listeners)
		{
			listener.cellRangeUpdated(this);
		}
	}

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

	public int getWidth()
	{
		int firstPopulatedColumnForRange = 0;
		int lastPopulatedColumnForRange = 0;

		boolean firstLoop = true;

		for (CellRangeRow row : rows)
		{
			if (!row.isEmpty())
			{
				if (firstLoop)
				{
					firstLoop = false;

					firstPopulatedColumnForRange = row.getFirstPopulatedColumn();
					lastPopulatedColumnForRange = row.getLastPopulatedColumn();
				}
				else
				{
					int firstPopulatedColumnForRow = row.getFirstPopulatedColumn();

					if (firstPopulatedColumnForRow < firstPopulatedColumnForRange)
					{
						firstPopulatedColumnForRange = firstPopulatedColumnForRow;
					}

					int lastPopulatedColumnForRow = row.getLastPopulatedColumn();

					if (lastPopulatedColumnForRow > lastPopulatedColumnForRange)
					{
						lastPopulatedColumnForRange = lastPopulatedColumnForRow;
					}
				}
			}
		}

		return isEmpty() ? 0 : lastPopulatedColumnForRange + 1 - firstPopulatedColumnForRange;
	}

	public int getHeight()
	{
		return rows.size();
	}

	public boolean isEmpty()
	{
		boolean isEmpty = true;

		for (CellRangeRow row : rows)
		{
			if (! row.isEmpty())
			{
				isEmpty = false;
				break;
			}
		}

		return isEmpty;
	}

	public boolean isContiguous()
	{
		boolean isContiguous = true;

		for (CellRangeRow row : rows)
		{
			if (!row.isContiguous())
			{
				isContiguous = false;
				break;
			}
		}

		return isContiguous;
	}

	public String toString()
	{
		StringBuilder builder = new StringBuilder();
		
		for(CellRangeRow row : rows)
		{
			builder.append("\nIndex").append(row.getIndex()).append(": ");
			
			for(TableCellReference cell : row.getCells())
			{
				if(cell != null)
				{
					builder.append("[").append(cell.getRowIndex()).append(",").append(cell.getColumnIndex()).append("]");
				}
				else
				{
					builder.append("[x]");
				}
			}

		}
		
		return builder.toString();
	}
	
	public TableCellReference getFirstCell()
	{
		return rows.get(0).getFirstCell();
	}
	
	public TableCellReference getLastCell()
	{
		return rows.get(rows.size() -1).getLastCell();
	}
	
	public List<CellRangeRow> getRows()
	{
		return rows;
	}
}

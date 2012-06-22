package uk.co.certait.htmlexporter.ss;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.list.GrowthList;
import org.apache.poi.ss.usermodel.Cell;

public class CellRangeRow
{
	private int index;
	private List<TableCellReference> cells;

	@SuppressWarnings("unchecked")
	protected CellRangeRow(int index)
	{
		this.index = index;
		cells = GrowthList.decorate(new ArrayList<Cell>());
	}

	protected boolean isContiguous()
	{
		boolean isContiguous = true;

		if (!cells.isEmpty())
		{
			int firstPopulatedColumn = getFirstPopulatedColumn();
			int lastPopulatedColumn = getLastPopulatedColumn();

			for (int i = firstPopulatedColumn; i < lastPopulatedColumn; ++i)
			{
				if (cells.get(i) == null)
				{
					isContiguous = false;
					break;
				}
			}
		}
		else
		{
			isContiguous = false;
		}

		return isContiguous;
	}

	protected int getFirstPopulatedColumn()
	{
		int firstColumn = 0;

		for (int i = 0; i < cells.size(); ++i)
		{
			if (cells.get(i) != null)
			{
				firstColumn = i;
				break;
			}
		}

		return firstColumn;
	}

	protected int getLastPopulatedColumn()
	{
		int lastColumn = 0;

		for (int i = cells.size() - 1; i > -1; --i)
		{
			if (cells.get(i) != null)
			{
				lastColumn = i;
				break;
			}
		}

		return lastColumn;
	}

	public boolean isEmpty()
	{
		boolean isEmpty = true;

		for (TableCellReference cell : cells)
		{
			if (cell != null)
			{
				isEmpty = false;
				break;
			}
		}

		return isEmpty;
	}

	public void addCell(TableCellReference cell)
	{
		cells.add(cell.getColumnIndex(), cell);
	}

	public List<TableCellReference> getCells()
	{
		return cells;
	}
	
	public TableCellReference getFirstCell()
	{
		return cells.get(getFirstPopulatedColumn());
	}
	
	public TableCellReference getLastCell()
	{
		return cells.get(getLastPopulatedColumn());
	}

	public int getIndex()
	{
		return index;
	}
}

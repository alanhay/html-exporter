package uk.co.certait.htmlexporter.ss;

import java.util.HashMap;
import java.util.Map;


public class RangeReferenceTracker
{
	private Map<String, CellRange> ranges;
	
	public RangeReferenceTracker()
	{
		ranges = new HashMap<String, CellRange>();
	}
	
	public void addCelltoRange(String rangeName, TableCellReference cell)
	{
		rangeName = rangeName.toLowerCase().trim();
		
		if(! ranges.containsKey(rangeName))
		{
			ranges.put(rangeName, new CellRange());
		}
		
		ranges.get(rangeName).addCell(cell);
	}
	
	public CellRange getCellRange(String name)
	{
		return ranges.get(name.toLowerCase());
	}
}

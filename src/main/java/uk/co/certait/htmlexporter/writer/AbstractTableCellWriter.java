package uk.co.certait.htmlexporter.writer;

import java.text.NumberFormat;
import java.text.ParseException;

import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Element;

import uk.co.certait.htmlexporter.ss.CellRange;
import uk.co.certait.htmlexporter.ss.DefaultTableCellReference;
import uk.co.certait.htmlexporter.ss.Function;
import uk.co.certait.htmlexporter.ss.RangeReferenceTracker;

public abstract class AbstractTableCellWriter implements TableCellWriter
{
	private RangeReferenceTracker tracker;
	
	public AbstractTableCellWriter()
	{
		tracker = new RangeReferenceTracker();
	}
	
	@Override
	public int writeCell(Element element, int rowIndex, int columnIndex)
	{
		int cellsWritten = renderCell(element, rowIndex, columnIndex);
		
		if(isFunctionGroupCell(element))
		{
			for(String rangeName : getFunctionGroupReferences(element))
			{
				tracker.addCelltoRange(rangeName, new DefaultTableCellReference(rowIndex, columnIndex));
			}
		}
		if(isFunctionOutputCell(element))
		{
			String rangeName = getFunctionOutputReference(element);
			addFunctionCell(rowIndex, columnIndex, tracker.getCellRange(rangeName), Function.SUM);
		}
		
		return cellsWritten;
	}
	
	public String getElementText(Element element)
	{
		String text = element.ownText();
		
		for(Element child : element.children())
		{
			text = child.ownText();
		}
		
		return text;
	}
	
	protected boolean spansMultipleColumns(Element element)
	{
		boolean spansMultipleColumns = false;
		
		if (element.hasAttr(COLUMN_SPAN_ATTRIBUTE))
		{
			int columnCount = Integer.parseInt(element.attr(COLUMN_SPAN_ATTRIBUTE));
			
			spansMultipleColumns = columnCount > 1;
		}
		
		return spansMultipleColumns;
	}
	
	protected int getMergedColumnCount(Element element)
	{
		int columnCount = 1;
		
		if(spansMultipleColumns(element))
		{
			columnCount = Integer.parseInt(element.attr(COLUMN_SPAN_ATTRIBUTE));	
		}
		
		return columnCount ;
	}
	
	protected boolean isFunctionGroupCell(Element element)
	{
		return element.hasAttr(DATA_GROUP_ATTRIBUTE);
	}
	
	protected String [] getFunctionGroupReferences(Element element)
	{
		return getAttributeValues(element, DATA_GROUP_ATTRIBUTE);
	}
	
	protected boolean isFunctionOutputCell(Element element)
	{
		boolean functionOutputCell = false;
		
		for(Attribute attribute : element.attributes())
		{
			if(attribute.getKey().equalsIgnoreCase(DATA_GROUP_OUTPUT_ATTRIBUTE))
			{
				functionOutputCell = true;
				break;
			}
		}
		
		return functionOutputCell;
	}
	
	protected String getFunctionOutputReference(Element element)
	{
		String functionOutputGroup = null;
		
		for(Attribute attribute : element.attributes())
		{
			if(attribute.getKey().equalsIgnoreCase(DATA_GROUP_OUTPUT_ATTRIBUTE))
			{
				functionOutputGroup = attribute.getValue();
				break;
			}
		}
		
		return functionOutputGroup;
	}
	
	protected String [] getAttributeValues(Element element, String attributeName)
	{
		String values [] = null;
		
		if (element.hasAttr(attributeName))
		{
			values = element.attr(attributeName).toLowerCase().split(",");

			for (String value : values)
			{
				value = value.trim().toLowerCase();
			}
		}
		
		return values;	
	}
	
	public Double getNumericValue(Element element)
	{
		Double numericValue = null;
		
		try
		{
			numericValue = NumberFormat.getInstance().parse(element.ownText()).doubleValue();
		}
		catch(ParseException e)
		{
			
		}
		
		return numericValue;
	}
	
	public abstract int renderCell(Element element, int rowIndex, int columnIndex);
	
	public abstract void addFunctionCell(int rowIndex, int columnIndex, CellRange range, Function function);
}

package uk.co.certait.htmlexporter.demo.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class SalesReportData
{
	private List<Area> areas;

	public SalesReportData()
	{
		areas = new ArrayList<Area>();
	}

	public void addArea(Area area)
	{
		areas.add(area);
	}

	public int getNumberOfSalesForProductGroup(ProductGroup group)
	{
		int total = 0;

		for (Area area : areas)
		{
			total += area.getNumberOfSalesForProductGroup(group);
		}

		return total;
	}

	public BigDecimal getValueOfSalesForProductGroup(ProductGroup group)
	{
		BigDecimal total = new BigDecimal("0");

		for (Area area : areas)
		{
			total = total.add(area.getValueOfSalesForProductGroup(group));
		}
		
		return total;
	}

	public int getNumberOfSales()
	{
		int total = 0;

		for (Area area : areas)
		{
			total += area.getNumberOfSales();
		}

		return total;
	}

	public BigDecimal getValueOfSales()
	{
		BigDecimal total = new BigDecimal("0");

		for (Area area : areas)
		{
			total = total.add(area.getValueOfSales());
		}
		
		return total;
	}
	
	public List<Area> getAreas()
	{
		return areas;
	}
}

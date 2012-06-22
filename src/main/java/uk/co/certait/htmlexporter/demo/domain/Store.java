package uk.co.certait.htmlexporter.demo.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Store
{
	private String id;
	private String name;
	private Map<ProductGroup, List<Sale>> sales;

	public Store(String id, String name)
	{
		this.id = id;
		this.name = name;
		
		sales = new HashMap<ProductGroup, List<Sale>>();
	}

	public String getId()
	{
		return id;
	}

	public String getName()
	{
		return name;
	}

	public Map<ProductGroup, List<Sale>> getSales()
	{
		return sales;
	}

	public void addSale(Sale sale)
	{
		if(! sales.containsKey(sale.getProductGroup()))
		{
			sales.put(sale.getProductGroup(), new ArrayList<Sale>());
		}
		
		sales.get(sale.getProductGroup()).add(sale);
	}

	public int getNumberOfSalesForProductGroup(ProductGroup group)
	{
		return sales.containsKey(group) ? sales.get(group).size() : 0;
	}
	
	public BigDecimal getValueOfSalesForProductGroup(ProductGroup group)
	{
		BigDecimal total = new BigDecimal("0");
		
		if(sales.containsKey(group))
		{
			for(Sale sale : sales.get(group))
			{
				total = total.add(sale.getValue());
			}
		}
	
		return total;
	}
	
	public int getNumberOfSales()
	{
		int total = 0;
		
		for(ProductGroup group : sales.keySet())
		{
			total += sales.get(group).size();
		}
		
		return total;
	}
	
	public BigDecimal getValueOfSales()
	{
		BigDecimal total = new BigDecimal("0");
		
		for(ProductGroup group : sales.keySet())
		{
			total = total.add(getValueOfSalesForProductGroup(group));
		}
		
		return total;
	}
}

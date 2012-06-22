package uk.co.certait.htmlexporter.demo.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Area
{
	private int id;
	private String name;
	private List<Store> stores;
	
	public Area(int id, String name)
	{
		this.id = id;
		this.name = name;
		
		stores = new ArrayList<Store>();
	}

	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public List<Store> getStores()
	{
		return stores;
	}

	public void addStore(Store store)
	{
		stores.add(store);
	}
	
	public int getNumberOfSalesForProductGroup(ProductGroup group)
	{
		int count = 0;
		
		for(Store store : stores)
		{
			count += store.getNumberOfSalesForProductGroup(group);
		}
		return count;
	}
	
	public BigDecimal getValueOfSalesForProductGroup(ProductGroup group)
	{
		BigDecimal total = new BigDecimal("0");
		
		for(Store store : stores)
		{
			total = total.add(store.getValueOfSalesForProductGroup(group));
		}
	
		return total;
	}
	
	public Store getBestPerformingStoreForProductGroup(ProductGroup group)
	{
		//FIXME does not account 2 stores with equal sales
		
		Store store = null;
		BigDecimal maxSales = new BigDecimal("-1");
		
		for(Store s : stores)
		{
			if(s.getValueOfSalesForProductGroup(group).compareTo(maxSales) > 0)
			{
				store = s;
				maxSales = store.getValueOfSalesForProductGroup(group);
			}
		}
		
		return store;
	}
	
	public int getNumberOfSales()
	{
		int total = 0;
		
		for(Store store : stores)
		{
			total += store.getNumberOfSales();
		}
		
		return total;
	}
	
	public BigDecimal getValueOfSales()
	{
		BigDecimal total = new BigDecimal("0");
		
		for(Store store : stores)
		{
			total = total.add(store.getValueOfSales());
		}
		
		return total;
	}
}

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
package uk.co.certait.htmlexporter.demo.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Area
{
	private int id;
	private String name;
	private List<Region> regions;

	public Area(int id, String name)
	{
		this.id = id;
		this.name = name;

		regions = new ArrayList<Region>();
	}

	public int getId()
	{
		return id;
	}

	public String getName()
	{
		return name;
	}
	
	public List<Region> getRegions()
	{
		return regions;
	}

	public void addRegion(Region region)
	{
		regions.add(region);
	}

	public int getNumberOfSalesForProductGroup(ProductGroup group)
	{
		int count = 0;

		for (Region region : regions)
		{
			count += region.getNumberOfSalesForProductGroup(group);
		}
		return count;
	}

	public BigDecimal getValueOfSalesForProductGroup(ProductGroup group)
	{
		BigDecimal total = new BigDecimal("0");

		for (Region region : regions)
		{
			total = total.add(region.getValueOfSalesForProductGroup(group));
		}

		return total;
	}

	public Store getBestPerformingStoreForProductGroup(ProductGroup group)
	{
		// FIXME does not account 2 stores with equal sales

		Store store = null;
		BigDecimal maxSales = new BigDecimal("-1");

		for (Region region : regions)
		{
			for (Store s : region.getStores())
			{
				if (s.getValueOfSalesForProductGroup(group).compareTo(maxSales) > 0)
				{
					store = s;
					maxSales = store.getValueOfSalesForProductGroup(group);
				}
			}
		}

		return store;
	}

	public int getNumberOfSales()
	{
		int total = 0;

		for (Region region : regions)
		{
			total += region.getNumberOfSales();
		}

		return total;
	}

	public BigDecimal getValueOfSales()
	{
		BigDecimal total = new BigDecimal("0");

		for (Region region : regions)
		{
			total = total.add(region.getValueOfSales());
		}

		return total;
	}
	
	public int getNumberOfRegions()
	{
		return regions.size();
	}
	
	public int getNumberOfStores()
	{
		int total = 0;
		
		for(Region region : regions)
		{
			total += region.getNumberOfStores();
		}
		
		return total;
	}
}

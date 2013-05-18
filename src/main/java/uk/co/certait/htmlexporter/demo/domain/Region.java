package uk.co.certait.htmlexporter.demo.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Copyright (C) 2012 alanhay <alanhay99@hotmail.com>
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
public class Region
{
	private String id;
	private String name;
	private List<Store> stores;

	public Region(String id, String name)
	{
		this.id = id;
		this.name = name;

		stores = new ArrayList<Store>();
	}

	public String getId()
	{
		return id;
	}

	public String getName()
	{
		return name;
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

		for (Store store : stores)
		{
			count += store.getNumberOfSalesForProductGroup(group);
		}
		return count;
	}

	public BigDecimal getValueOfSalesForProductGroup(ProductGroup group)
	{
		BigDecimal total = new BigDecimal("0");

		for (Store store : stores)
		{
			total = total.add(store.getValueOfSalesForProductGroup(group));
		}

		return total;
	}

	public int getNumberOfSales()
	{
		int total = 0;

		for (Store store : stores)
		{
			total += store.getNumberOfSales();
		}

		return total;
	}

	public BigDecimal getValueOfSales()
	{
		BigDecimal total = new BigDecimal("0");

		for (Store store : stores)
		{
			total = total.add(store.getValueOfSales());
		}

		return total;
	}

	public int getNumberOfStores()
	{
		return stores.size();
	}
}

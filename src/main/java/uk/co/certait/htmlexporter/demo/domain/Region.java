package uk.co.certait.htmlexporter.demo.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Region {
	private String id;
	private String name;
	private List<Store> stores;

	public Region(String id, String name) {
		this.id = id;
		this.name = name;

		stores = new ArrayList<Store>();
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public List<Store> getStores() {
		return stores;
	}

	public void addStore(Store store) {
		stores.add(store);
	}

	public int getNumberOfSalesForProductGroup(ProductGroup group) {
		int count = 0;

		for (Store store : stores) {
			count += store.getNumberOfSalesForProductGroup(group);
		}
		return count;
	}

	public BigDecimal getValueOfSalesForProductGroup(ProductGroup group) {
		BigDecimal total = new BigDecimal("0");

		for (Store store : stores) {
			total = total.add(store.getValueOfSalesForProductGroup(group));
		}

		return total;
	}

	public int getNumberOfSales() {
		int total = 0;

		for (Store store : stores) {
			total += store.getNumberOfSales();
		}

		return total;
	}

	public BigDecimal getValueOfSales() {
		BigDecimal total = new BigDecimal("0");

		for (Store store : stores) {
			total = total.add(store.getValueOfSales());
		}

		return total;
	}

	public int getNumberOfStores() {
		return stores.size();
	}
}

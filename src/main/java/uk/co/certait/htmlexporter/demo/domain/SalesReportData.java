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

public class SalesReportData {
	private List<Area> areas;

	public SalesReportData() {
		areas = new ArrayList<Area>();
	}

	public void addArea(Area area) {
		areas.add(area);
	}

	public int getNumberOfSalesForProductGroup(ProductGroup group) {
		int total = 0;

		for (Area area : areas) {
			total += area.getNumberOfSalesForProductGroup(group);
		}

		return total;
	}

	public BigDecimal getValueOfSalesForProductGroup(ProductGroup group) {
		BigDecimal total = new BigDecimal("0");

		for (Area area : areas) {
			total = total.add(area.getValueOfSalesForProductGroup(group));
		}

		return total;
	}

	public int getNumberOfSales() {
		int total = 0;

		for (Area area : areas) {
			total += area.getNumberOfSales();
		}

		return total;
	}

	public BigDecimal getValueOfSales() {
		BigDecimal total = new BigDecimal("0");

		for (Area area : areas) {
			total = total.add(area.getValueOfSales());
		}

		return total;
	}

	public List<Area> getAreas() {
		return areas;
	}
}

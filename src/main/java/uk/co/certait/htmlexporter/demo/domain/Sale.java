package uk.co.certait.htmlexporter.demo.domain;

import java.math.BigDecimal;

public class Sale
{
	private ProductGroup productGroup;
	private BigDecimal value;

	public Sale(ProductGroup productGroup, BigDecimal value)
	{
		this.productGroup = productGroup;
		this.value = value;
	}

	public ProductGroup getProductGroup()
	{
		return productGroup;
	}

	public BigDecimal getValue()
	{
		return value;
	}
}

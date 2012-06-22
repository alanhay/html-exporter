package uk.co.certait.htmlexporter.demo.domain;

import org.apache.commons.lang.WordUtils;

public enum ProductGroup
{
	ELECTRICAL(1), SPORTS(2), HOME(3), GARDEN(4), TRAVEL(5), TOYS(6);

	private int id;

	private ProductGroup(int id)
	{
		this.id = id;
	}

	public String toString()
	{
		return WordUtils.capitalizeFully(this.name());
	}

	public int getId()
	{
		return id;
	}
}

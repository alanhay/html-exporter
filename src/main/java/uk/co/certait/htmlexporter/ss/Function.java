package uk.co.certait.htmlexporter.ss;

public enum Function
{
	AVERAGE(1), SUM(9);

	private int code;

	private Function(int code)
	{
		this.code = code;
	}

	public int getCode()
	{
		return code;
	}
}

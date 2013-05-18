package uk.co.certait.htmlexporter.css;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.io.IOUtils;

public class DefaultExternalStyleSheetLoader implements ExternalStyleSheetLoader
{
	private String baseUrl;
	
	public DefaultExternalStyleSheetLoader(String baseUrl)
	{
		this.baseUrl = baseUrl;
	}
	
	public String loadStyleSheet(String url) throws IOException
	{
		return loadAbsoluteStyleSheet(url);
	}
	
	protected String getUrl()
	{
		return "";
	}

	public String loadAbsoluteStyleSheet(String url) throws IOException
	{
		HttpClient client = new HttpClient();
		HttpMethod method = new GetMethod(baseUrl + url);
		client.executeMethod(method);
		String styleSheet = IOUtils.toString(method.getResponseBodyAsStream());
		method.releaseConnection();
        
        System.out.println(styleSheet);
        
        return styleSheet;
	}

	public String loadRelativeStyleSheet(String baseUrl, String path)
	{
		return null;
	}
}

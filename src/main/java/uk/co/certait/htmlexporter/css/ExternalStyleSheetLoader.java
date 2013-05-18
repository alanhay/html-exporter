package uk.co.certait.htmlexporter.css;

import java.io.IOException;


public interface ExternalStyleSheetLoader
{
	public String loadStyleSheet(String url) throws IOException;
}

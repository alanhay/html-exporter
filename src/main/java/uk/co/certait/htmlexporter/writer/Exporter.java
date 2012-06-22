package uk.co.certait.htmlexporter.writer;

import java.io.File;
import java.io.IOException;

public interface Exporter
{
	public void exportHtml(String html, File outputFile) throws IOException;
	
	public byte [] exportHtml(String html) throws IOException;
}

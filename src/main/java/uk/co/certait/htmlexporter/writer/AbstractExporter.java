package uk.co.certait.htmlexporter.writer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import uk.co.certait.htmlexporter.css.StyleMapper;
import uk.co.certait.htmlexporter.css.StyleParser;

public abstract class AbstractExporter implements Exporter
{
	public byte [] exportHtml(String html) throws IOException
	{
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		exportHtml(html, out);
		
		return out.toByteArray();
	}
	
	public void exportHtml(String html, File outputFile) throws IOException
	{
		FileOutputStream out = new FileOutputStream(outputFile);
		exportHtml(html, out);
	}
	
	protected Elements getTables(String html)
	{
		Document document = Jsoup.parse(html);//FIXME parsing twice
		
		return document.getElementsByTag("table");
	}
	
	protected StyleMapper getStyleMapper(String html)
	{
		Document document = Jsoup.parse(html);
		Elements styles = document.getElementsByTag("style");//FIXME parsing twice
		
		StyleParser parser = new StyleParser();
		StyleMapper mapper = new StyleMapper(parser.parseStyles(styles));
		
		return mapper;
	}
	
	public abstract void exportHtml(String html, OutputStream out) throws IOException;
}

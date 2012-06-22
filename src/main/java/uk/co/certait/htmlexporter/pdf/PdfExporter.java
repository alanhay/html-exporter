package uk.co.certait.htmlexporter.pdf;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.xhtmlrenderer.pdf.ITextRenderer;

public class PdfExporter
{
	public byte[] exportHtml(String html) throws Exception
	{
		DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		Document doc = builder.parse(new ByteArrayInputStream(html.replaceAll("&nbsp;", "").getBytes()));
	
		ITextRenderer renderer = new ITextRenderer();
		renderer.setDocument(doc, null);
		
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		renderer.layout();
		renderer.createPDF(out);
		out.flush();
		out.close();
		
		return out.toByteArray();
	}
}

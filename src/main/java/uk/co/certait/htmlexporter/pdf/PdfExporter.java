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
package uk.co.certait.htmlexporter.pdf;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.xhtmlrenderer.pdf.ITextRenderer;

public class PdfExporter {

	protected List<String> fontPaths;

	public void registerFontPath(String path) {
		getFontPaths().add(path);
	}

	public List<String> getFontPaths() {
		if(fontPaths == null) fontPaths = new ArrayList<>();
		return fontPaths;
	}

	public byte[] exportHtml(String html) throws Exception {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		exportHtml(html, out);

		return out.toByteArray();
	}

	public void exportHtml(String html, File file) throws Exception {
		exportHtml(html, new FileOutputStream(file));
	}

	private void exportHtml(String html, OutputStream out) throws Exception {
		DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		Document doc = builder.parse(new ByteArrayInputStream(html.replaceAll("&nbsp;", "").getBytes(StandardCharsets.UTF_8)));

		ITextRenderer renderer = new ITextRenderer();
		renderer.setDocument(doc, null);

		for(String path : getFontPaths()) {
			renderer.getFontResolver().addFont(path, true);
		}

		renderer.layout();
		renderer.createPDF(out);
		out.flush();
		out.close();
	}
}

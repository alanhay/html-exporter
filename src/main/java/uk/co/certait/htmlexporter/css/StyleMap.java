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
package uk.co.certait.htmlexporter.css;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Element;

import com.helger.commons.collection.impl.ICommonsList;
import com.helger.css.ECSSVersion;
import com.helger.css.decl.CSSDeclaration;
import com.helger.css.decl.CSSDeclarationList;
import com.helger.css.reader.CSSReaderDeclarationList;

/**
 * 
 * @author alanhay
 * 
 */
public class StyleMap {
	private static final String CLASS_PREFIX = ".";

	Map<String, Style> styles;
	private StyleGenerator generator;

	public StyleMap(Map<String, Style> styles) {
		this.styles = styles != null ? styles : new HashMap<String, Style>();
		generator = new StyleGenerator();
	}

	public Style getStyleForElement(Element element) {
		return StyleMerger.mergeStyles(getAllStyles(element).toArray(new Style[0]));
	}
	
	protected List<Style> getAllStyles(Element element) {
		List<Style> styles = new ArrayList<>();

		if (getStyleForTag(element) != null) {
			styles.add(getStyleForTag(element));
		}

		if (!getStylesForClass(element).isEmpty()) {
			List<Style> classStyles = getStylesForClass(element);
			styles.addAll(classStyles);
		}

		Optional<Style> inlineStyle = getInlineStyle(element);

		if (inlineStyle.isPresent()) {
			styles.add(inlineStyle.get());
		}
		
		//recursive call for each parent element (closest first)
		//get any applicable styles and insert at start of list to
		//preserve priority
		for (Element parent : element.parents()) {
			styles.addAll(0, getAllStyles(parent));
		}

		return styles;		
	}

	private Style getStyleForTag(Element element) {
		return styles.get(element.tagName());
	}

	private List<Style> getStylesForClass(Element element) {
		List<Style> classStyles = new ArrayList<Style>();

		if (StringUtils.isNotEmpty(element.className())) {
			String[] classNames = element.className().split(" ");

			for (String className : classNames) {
				String qualifiedClassName = CLASS_PREFIX + className.trim();

				if (styles.containsKey(qualifiedClassName)) {
					classStyles.add(styles.get(qualifiedClassName));
				}
			}
		}

		return classStyles;
	}

	protected Optional<Style> getInlineStyle(Element element) {
		List<Style> styles = new ArrayList<>();

		if (element.hasAttr("style")) {
			CSSDeclarationList cssStyles = CSSReaderDeclarationList.readFromString(element.attr("style"),
					ECSSVersion.LATEST);
			ICommonsList<CSSDeclaration> declarations = cssStyles.getAllDeclarations();

			for (CSSDeclaration declaration : declarations) {
				styles.add(generator.createStyle(declaration));
			}
		}

		return Optional.of(StyleMerger.mergeStyles(styles.toArray(new Style[0])));
	}
}

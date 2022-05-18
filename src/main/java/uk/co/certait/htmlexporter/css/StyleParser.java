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

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.helger.commons.collection.impl.ICommonsList;
import com.helger.css.ECSSVersion;
import com.helger.css.decl.CSSDeclaration;
import com.helger.css.decl.CSSSelector;
import com.helger.css.decl.CSSStyleRule;
import com.helger.css.decl.CascadingStyleSheet;
import com.helger.css.decl.ICSSSelectorMember;
import com.helger.css.reader.CSSReader;

public class StyleParser {
	private StyleGenerator generator;

	public StyleParser() {
		generator = new StyleGenerator();
	}

	/**
	 * 
	 * @param elements A collection of HTML <style/> elements. In cases of multiple
	 *                 styles in the same sheet,
	 * @return A Map of Styles extracted from the CSS. Normal order of precedence
	 *         applies: where the same selector is specified in multiple <style/>
	 *         elements, the properties will be merged. For any properties specified
	 *         in both <style/> elements then the latest will overwrite the first.
	 */
	public Map<String, Style> parseStyleSheets(Elements elements) {
		Map<String, Style> mergedStyleMap = new HashMap<>();

		for (Element element : elements) {
			Map<String, Style> styleMap = parseStyleSheet(element);

			for (Entry<String, Style> entry : styleMap.entrySet()) {
				if (mergedStyleMap.containsKey(entry.getKey())) {
					mergedStyleMap.put(entry.getKey(),
							StyleMerger.mergeStyles(mergedStyleMap.get(entry.getKey()), entry.getValue()));
				} else {
					mergedStyleMap.put(entry.getKey(), entry.getValue());
				}
			}
		}

		return mergedStyleMap;
	}

	/**
	 * 
	 * @param element An HTML <style/> element
	 * 
	 * @return A Map of Styles extracted from the CSS
	 */
	public Map<String, Style> parseStyleSheet(Element element) {
		Map<String, Style> styleMap = new HashMap<>();
		CascadingStyleSheet sheet = CSSReader.readFromString(element.data(), ECSSVersion.LATEST);
		ICommonsList<CSSStyleRule> rules = sheet.getAllStyleRules();

		for (CSSStyleRule rule : rules) {
			ICommonsList<CSSSelector> selectors = rule.getAllSelectors();
			ICommonsList<CSSDeclaration> declarations = rule.getAllDeclarations();

			for (CSSDeclaration declaration : declarations) {
				Style style = generator.createStyle(declaration);

				for (CSSSelector selector : selectors) {
					ICommonsList<ICSSSelectorMember> selectorMembers = selector.getAllMembers();

					for (ICSSSelectorMember selectorMember : selectorMembers) {
						String selectorName = selectorMember.getAsCSSString();

						if (styleMap.containsKey(selectorName)) {
							styleMap.put(selectorName, StyleMerger.mergeStyles(styleMap.get(selectorName), style));
						} else {
							styleMap.put(selectorName, style);
						}
					}
				}
			}
		}

		return styleMap;
	}
}

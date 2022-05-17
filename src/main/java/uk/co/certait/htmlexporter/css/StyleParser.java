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
import java.util.List;
import java.util.Map;

import org.jsoup.nodes.Element;

import com.helger.commons.collection.impl.ICommonsList;
import com.helger.css.ECSSVersion;
import com.helger.css.decl.CSSDeclaration;
import com.helger.css.decl.CSSDeclarationList;
import com.helger.css.decl.CSSExpression;
import com.helger.css.decl.CSSExpressionMemberTermSimple;
import com.helger.css.decl.CSSSelector;
import com.helger.css.decl.CSSStyleRule;
import com.helger.css.decl.CascadingStyleSheet;
import com.helger.css.decl.ICSSExpressionMember;
import com.helger.css.decl.ICSSSelectorMember;
import com.helger.css.decl.shorthand.CSSShortHandDescriptor;
import com.helger.css.decl.shorthand.CSSShortHandRegistry;
import com.helger.css.property.ECSSProperty;
import com.helger.css.reader.CSSReader;
import com.helger.css.reader.CSSReaderDeclarationList;

public class StyleParser {
	private StyleGenerator generator;

	public StyleParser() {
		generator = new StyleGenerator();
	}

	/*	*//**
			 * 
			 * @param elements
			 * @return
			 *//*
				 * public Map<String, Style> parseStyles(Elements elements) { Map<String, Style>
				 * styles = new HashMap<>();
				 * 
				 * for (Element element : elements) { try { List<Rule> rules =
				 * CSSParser.parse(element.data()); mapStyles(rules, styles); } catch (Exception
				 * e) { throw new RuntimeException(e); } }
				 * 
				 * return styles; }
				 */

	/**
	 * 
	 * @param elements A collection of <style/> elements
	 * 
	 * @return A Map of the Style elements
	 */
	public Map<String, Style> parseInlineStyle(Element element) {
		Map<String, Style> styleMap = new HashMap<>();

		CSSDeclarationList styles = CSSReaderDeclarationList.readFromString(element.data(), ECSSVersion.LATEST);
		ICommonsList<CSSDeclaration> decs = styles.getAllDeclarations();

		for (CSSDeclaration dec : decs) {
			if (ECSSProperty.getFromNameOrNull(dec.getProperty()) != null) {

				CSSShortHandDescriptor shorthandDescriptor = CSSShortHandRegistry
						.getShortHandDescriptor(ECSSProperty.getFromNameOrNull(dec.getProperty()));

				if (shorthandDescriptor != null) {
					List<CSSDeclaration> splitDeclarations = shorthandDescriptor.getSplitIntoPieces(dec);

					for (CSSDeclaration d1 : splitDeclarations) {
						System.out.println("\t" + d1.getProperty());
						CSSExpression exp = d1.getExpression();
						ICommonsList<ICSSExpressionMember> members = exp.getAllMembers();

						for (ICSSExpressionMember m : members) {
							CSSExpressionMemberTermSimple t = (CSSExpressionMemberTermSimple) m;
							System.out.println("++" + t.getValue());
						}
					}
				} else {
					System.out.println(dec.getProperty());
					CSSExpression exp = dec.getExpression();
					ICommonsList<ICSSExpressionMember> members = exp.getAllMembers();

					for (ICSSExpressionMember m : members) {
						CSSExpressionMemberTermSimple t = (CSSExpressionMemberTermSimple) m;
						System.out.println("--" + t.getValue());
					}
				}
			}
		}

		return styleMap;
	}

	/**
	 * 
	 * @param element An HTML <style/> element
	 * 
	 * @return
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

	/*
	 * protected void mapStyles(List<Rule> rules, Map<String, Style> styles) {
	 * 
	 * for (Rule rule : rules) { for (Selector selector : rule.getSelectors()) {
	 * Style style = generator.createStyle(rule, selector);
	 * 
	 * if (styles.containsKey(selector.toString())) { Style merged =
	 * StyleMerger.mergeStyles(styles.get(selector.toString()), style);
	 * styles.put(selector.toString(), merged); } else {
	 * styles.put(selector.toString(), style); } } }
	 */

	// }

}

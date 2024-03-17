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

import java.awt.Color;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.helger.css.decl.CSSDeclaration;
import com.helger.css.decl.CSSExpressionMemberTermSimple;
import com.helger.css.decl.ICSSExpressionMember;
import com.helger.css.decl.shorthand.CSSShortHandDescriptor;
import com.helger.css.decl.shorthand.CSSShortHandRegistry;
import com.helger.css.property.ECSSProperty;

public class StyleGenerator {
	private static final String PX = "px";
	private static final String PERCENTAGE = "%";
	private static final Pattern pFontSize = Pattern.compile("^[\\s\\t]*([0-9]+?)[\\s\\t]*(px|pt|em|ex|pc|in|cm|mm|%)$");
	private static final Pattern pNumber = Pattern.compile("^[\\s\\t]*(\\d)+[\\s\\t]*$");

	// called by the parser and by the cell writer for an in-line style
	// the common interface is CSSDeclaration - which may need split
	protected Style createStyle(CSSDeclaration declaration) {
		List<Style> styles = new ArrayList<>();
		List<CSSDeclaration> declarations = new ArrayList<>();

		if (ECSSProperty.getFromNameOrNull(declaration.getProperty()) != null) {

			CSSShortHandDescriptor shorthandDescriptor = CSSShortHandRegistry
					.getShortHandDescriptor(ECSSProperty.getFromNameOrNull(declaration.getProperty()));

			if (shorthandDescriptor != null) {
				declarations.addAll(shorthandDescriptor.getSplitIntoPieces(declaration));
			} else {
				declarations.add(declaration);
			}

			for (CSSDeclaration decs : declarations) {
				Style style = new Style();
				populateIntegerProperties(decs, style);
				populateStringProperties(decs, style);
				populateColorProperties(decs, style);
				styles.add(style);
			}
		}

		return StyleMerger.mergeStyles(styles.toArray(new Style[0]));
	}

	protected double toPoint(double sourceValue, String unit) {
		double valueInPt = sourceValue;
		if(unit != null) {
			unit = unit.trim();
			if(!unit.isEmpty()) {
				switch(unit) {
				case "px":
					valueInPt = sourceValue * 0.75;
					break;
				case "pt":
					valueInPt = sourceValue;
					break;
				case "em":
					valueInPt = sourceValue * 12;
					break;
				case "ex":
					valueInPt = sourceValue * 6;
					break;
				case "pc":
					valueInPt = sourceValue * 12;
					break;
				case "in":
					valueInPt = sourceValue * 72;
					break;
				case "cm":
					valueInPt = sourceValue * 28.3464567;
					break;
				case "mm":
					valueInPt = sourceValue * 2.83464567;
					break;
				case "%":
					valueInPt = (sourceValue / 100) * 12;
					break;
				}
			}
		}
		return Math.max(1, round(valueInPt, 1));
	}

	protected static double round(double value, int precision) {
		int scale = (int) Math.pow(10, precision);
		return (double) Math.round(value * scale) / scale;
	}

	protected void populateIntegerProperties(CSSDeclaration rule, Style style) {
		for (CssIntegerProperty p : CssIntegerProperty.values()) {
			if (p.getProperty().equals(rule.getProperty())) {
				ICSSExpressionMember member = rule.getExpression().getMemberAtIndex(0);
				CSSExpressionMemberTermSimple term = (CSSExpressionMemberTermSimple) member;
				if(term != null) {
					String termValue = term.getValue().trim().toLowerCase();
					Matcher mFontSize = pFontSize.matcher(termValue);
					if (mFontSize.matches()) {
						double rawValue = Double.parseDouble(mFontSize.group(1));
						String unit = mFontSize.group(2);
						if(unit != null) {
							unit = unit.trim();
							if(!unit.isEmpty()) {
								double valueInPt = toPoint(rawValue, unit);
								style.addProperty(p, (int) valueInPt);
							}
						}
					} else {
						Matcher mNumber = pNumber.matcher(termValue);
						if(mNumber.matches()) {
							double rawValue = Math.max(1, Double.parseDouble(mNumber.group(1)));
							style.addProperty(p, (int) rawValue); // assume already in pt
						}
					}
				}
			}
		}
	}

	protected void populateStringProperties(CSSDeclaration rule, Style style) {
		for (CssStringProperty p : CssStringProperty.values()) {
			if (p.getProperty().equals(rule.getProperty())) {
				ICSSExpressionMember member = rule.getExpression().getMemberAtIndex(0);
				CSSExpressionMemberTermSimple term = (CSSExpressionMemberTermSimple) member;
				style.addProperty(p, term.getValue().trim());
			}
		}
	}

	protected void populateColorProperties(CSSDeclaration rule, Style style) {
		for (CssColorProperty p : CssColorProperty.values()) {
			if (p.getProperty().equals(rule.getProperty())) {
				ICSSExpressionMember member = rule.getExpression().getMemberAtIndex(0);
				CSSExpressionMemberTermSimple term = (CSSExpressionMemberTermSimple) member;
				style.addProperty(p, createColor(term.getValue().trim()));
			}
		}
	}

	private Color createColor(String cssValue) {
		// FIXME handle short format hex i.e. #fff
		// See also class CSSColorHelper
		try {
			return Color.decode(cssValue.toUpperCase());
		} catch (Exception ex) {
			try {
				Field field = Class.forName("java.awt.Color").getField(cssValue.toUpperCase());
				return (Color) field.get(null);
			} catch (Exception e) {
				return null;
			}
		}
	}
}

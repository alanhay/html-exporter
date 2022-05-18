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

/**
 * 
 * @author alanhay
 * 
 */
public class StyleMerger {
	public static Style mergeStyles(Style... styles) {
		Style merged = new Style();

		for (Style style : styles) {
			for (CssStringProperty p : style.getStringProperties().keySet()) {
				merged.addProperty(p, style.getProperty(p).get());
			}

			for (CssIntegerProperty p : style.getIntegerProperties().keySet()) {
				merged.addProperty(p, style.getProperty(p).get());
			}

			for (CssColorProperty p : style.getColorProperties().keySet()) {
				merged.addProperty(p, style.getProperty(p).get());
			}
		}

		return merged;
	}
}

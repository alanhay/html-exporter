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

public enum CssStringProperty {
	FONT_FAMILY("font-family"), FONT_WEIGHT("font-weight"), FONT_STYLE("font-style"),
	TEXT_DECORATION("text-decoration"), TEXT_ALIGN("text-align"), VERTICAL_ALIGN("vertical-align"),
	BORDER_STYLE("border-style"), BORDER_TOP_STYLE("border-top-style"), BORDER_BOTTOM_STYLE("border-bottom-style"),
	BORDER_LEFT_STYLE("border-left-style"), BORDER_RIGHT_STYLE("border-right-style"), BORDER_WIDTH("border-width"),
	BORDER_TOP_WIDTH("border-top-width"), BORDER_BOTTOM_WIDTH("border-bottom-width"),
	BORDER_LEFT_WIDTH("border-left-width"), BORDER_RIGHT_WIDTH("border-right-width"),
	WRAP_TEXT("wrap-text");

	private String property;

	private CssStringProperty(String property) {
		this.property = property;
	}

	public String getProperty() {
		return property;
	}
}

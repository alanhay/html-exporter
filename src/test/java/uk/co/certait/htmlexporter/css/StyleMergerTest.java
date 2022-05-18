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

import org.junit.Assert;
import org.junit.Test;

public class StyleMergerTest {
	@Test
	public void testMergeStyles() {
		Style tagStyle = new Style();
		Style classStyle = new Style();
		Style inlineStyle = new Style();

		tagStyle.addProperty(CssColorProperty.BACKGROUND_COLOR, Color.RED);
		tagStyle.addProperty(CssStringProperty.FONT_WEIGHT, Style.BOLD_FONT_STYLE);

		classStyle.addProperty(CssIntegerProperty.BORDER_WIDTH, 3);
		classStyle.addProperty(CssStringProperty.FONT_STYLE, Style.TEXT_DECORATION_UNDERLINE);
		classStyle.addProperty(CssColorProperty.BACKGROUND_COLOR, Color.BLUE);// override
																		// tag
																		// style

		inlineStyle.addProperty(CssColorProperty.COLOR, Color.WHITE);
		inlineStyle.addProperty(CssIntegerProperty.BORDER_WIDTH, 4);// override
																	// class
																	// style
		inlineStyle.addProperty(CssIntegerProperty.FONT_SIZE, 12);// override
																	// class
																	// style

		Style style = StyleMerger.mergeStyles(tagStyle, classStyle, inlineStyle);
		Assert.assertEquals(style.getProperty(CssColorProperty.BACKGROUND_COLOR), Color.BLUE);
		Assert.assertEquals(style.getProperty(CssStringProperty.FONT_WEIGHT), Style.BOLD_FONT_STYLE);
		Assert.assertEquals(style.getProperty(CssIntegerProperty.BORDER_WIDTH), 4);
		Assert.assertEquals(style.getProperty(CssStringProperty.FONT_STYLE), Style.TEXT_DECORATION_UNDERLINE);
		Assert.assertEquals(style.getProperty(CssColorProperty.COLOR), Color.WHITE);
		Assert.assertEquals(style.getProperty(CssIntegerProperty.FONT_SIZE), 12);
	}
}

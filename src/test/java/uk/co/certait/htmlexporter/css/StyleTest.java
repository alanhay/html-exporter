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

import junit.framework.Assert;

import org.junit.Test;

public class StyleTest {
	@Test
	public void testEqualsAndHashCode() {
		Style style1 = new Style();
		Style style2 = null;

		Assert.assertFalse(style1.equals(style2));

		style2 = style1;
		Assert.assertEquals(style1, style2);

		style2 = new Style();
		Assert.assertEquals(style1, style2);

		style1.addProperty(CssIntegerProperty.FONT_SIZE, 12);
		Assert.assertFalse(style1.equals(style2));
		style2.addProperty(CssIntegerProperty.FONT_SIZE, 11);
		Assert.assertFalse(style1.equals(style2));
		style2.addProperty(CssIntegerProperty.FONT_SIZE, 12);
		Assert.assertEquals(style1, style2);
		Assert.assertEquals(style1.hashCode(), style2.hashCode());

		style1.addProperty(CssStringProperty.FONT_WEIGHT, Style.BOLD_FONT_STYLE);
		Assert.assertFalse(style1.equals(style2));

		style2.addProperty(CssStringProperty.FONT_WEIGHT, Style.BOLD_FONT_STYLE);
		Assert.assertEquals(style1, style2);
		Assert.assertEquals(style1.hashCode(), style2.hashCode());

		style1.addProperty(CssColorProperty.BACKGROUND_COLOR, Color.GREEN);
		Assert.assertFalse(style1.equals(style2));
		style2.addProperty(CssColorProperty.BACKGROUND_COLOR, Color.RED);
		Assert.assertFalse(style1.equals(style2));
		style2.addProperty(CssColorProperty.BACKGROUND_COLOR, Color.GREEN);
		Assert.assertEquals(style1, style2);
		Assert.assertEquals(style1.hashCode(), style2.hashCode());
	}

	@Test
	public void testIsFontSizeSet() {
		Style style = new Style();
		Assert.assertFalse(style.isFontSizeSet());

		style.addProperty(CssIntegerProperty.FONT_SIZE, 12);

		Assert.assertTrue(style.isFontSizeSet());
	}

	@Test
	public void testIsWidthSet() {
		Style style = new Style();
		Assert.assertFalse(style.isWidthSet());

		style.addProperty(CssIntegerProperty.WIDTH, 100);

		Assert.assertTrue(style.isWidthSet());
	}


	@Test
	public void testIsFontBold() {
		Style style = new Style();
		Assert.assertFalse(style.isFontBold());

		style.addProperty(CssStringProperty.FONT_WEIGHT, Style.BOLD_FONT_STYLE);

		Assert.assertTrue(style.isFontBold());
	}

	@Test
	public void testIsFontItalic() {
		Style style = new Style();
		Assert.assertFalse(style.isFontItalic());

		style.addProperty(CssStringProperty.FONT_STYLE, Style.ITALIC_FONT_STYLE);

		Assert.assertTrue(style.isFontItalic());
	}

	@Test
	public void testIsTextUnderlined() {
		Style style = new Style();
		Assert.assertFalse(style.isTextUnderlined());

		style.addProperty(CssStringProperty.TEXT_DECORATION, Style.TEXT_DECORATION_UNDERLINE);

		Assert.assertTrue(style.isTextUnderlined());
	}

	@Test
	public void testIsLeftAligned() {
		Style style = new Style();
		Assert.assertFalse(style.isHorizontallyAlignedLeft());

		style.addProperty(CssStringProperty.TEXT_ALIGN, Style.LEFT_ALIGN);

		Assert.assertTrue(style.isHorizontallyAlignedLeft());
	}

	@Test
	public void testIsRightAligned() {
		Style style = new Style();
		Assert.assertFalse(style.isHorizontallyAlignedRight());

		style.addProperty(CssStringProperty.TEXT_ALIGN, Style.RIGHT_ALIGN);

		Assert.assertTrue(style.isHorizontallyAlignedRight());
	}

	@Test
	public void testIsCenterAligned() {
		Style style = new Style();
		Assert.assertFalse(style.isHorizontallyAlignedCenter());

		style.addProperty(CssStringProperty.TEXT_ALIGN, Style.CENTER_ALIGN);

		Assert.assertTrue(style.isHorizontallyAlignedCenter());
	}

	@Test
	public void testIsBackgroundSet() {
		Style style = new Style();
		Assert.assertFalse(style.isBackgroundSet());

		style.addProperty(CssColorProperty.BACKGROUND_COLOR, Color.RED);

		Assert.assertTrue(style.isBackgroundSet());
	}

	@Test
	public void testisColorSet() {
		Style style = new Style();
		Assert.assertFalse(style.isColorSet());

		style.addProperty(CssColorProperty.COLOR, Color.RED);

		Assert.assertTrue(style.isColorSet());
	}

	@Test
	public void testIsBorderColorSet() {
		Style style = new Style();
		Assert.assertFalse(style.isBorderColorSet());

		style.addProperty(CssColorProperty.BORDER_COLOR, Color.RED);

		Assert.assertTrue(style.isBorderColorSet());
	}
}

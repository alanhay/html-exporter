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

import static org.assertj.core.api.Assertions.assertThat;

import java.awt.Color;

import org.junit.Test;

public class StyleTest {
	@Test
	public void testEqualsAndHashCode() {
		Style style1 = new Style();
		Style style2 = null;

		assertThat(style1).isNotEqualTo(style2);

		style2 = style1;
		assertThat(style1).isEqualTo(style2);

		style2 = new Style();

		style1.addProperty(CssIntegerProperty.FONT_SIZE, 12);
		assertThat(style1).isNotEqualTo(style2);

		style2.addProperty(CssIntegerProperty.FONT_SIZE, 11);
		assertThat(style1).isNotEqualTo(style2);

		style2.addProperty(CssIntegerProperty.FONT_SIZE, 12);
		assertThat(style1).isEqualTo(style2);
		assertThat(style1.hashCode()).isEqualTo(style2.hashCode());

		style1.addProperty(CssStringProperty.FONT_WEIGHT, Style.BOLD_FONT_STYLE);
		assertThat(style1).isNotEqualTo(style2);

		style2.addProperty(CssStringProperty.FONT_WEIGHT, Style.BOLD_FONT_STYLE);
		assertThat(style1).isEqualTo(style2);
		assertThat(style1.hashCode()).isEqualTo(style2.hashCode());
		;

		style1.addProperty(CssColorProperty.BACKGROUND_COLOR, Color.GREEN);
		assertThat(style1).isNotEqualTo(style2);

		style2.addProperty(CssColorProperty.BACKGROUND_COLOR, Color.RED);
		assertThat(style1).isNotEqualTo(style2);

		style2.addProperty(CssColorProperty.BACKGROUND_COLOR, Color.GREEN);
		assertThat(style1).isEqualTo(style2);
		assertThat(style1.hashCode()).isEqualTo(style2.hashCode());
		;
	}

	@Test
	public void testIsFontSizeSet() {
		Style style = new Style();
		assertThat(style.isFontSizeSet()).isFalse();

		style.addProperty(CssIntegerProperty.FONT_SIZE, 12);
		assertThat(style.isFontSizeSet()).isTrue();
	}

	@Test
	public void testIsWidthSet() {
		Style style = new Style();
		assertThat(style.isWidthSet()).isFalse();

		style.addProperty(CssIntegerProperty.WIDTH, 100);
		assertThat(style.isWidthSet()).isTrue();
	}

	@Test
	public void testIsFontBold() {
		Style style = new Style();
		assertThat(style.isFontBold()).isFalse();

		style.addProperty(CssStringProperty.FONT_WEIGHT, Style.BOLD_FONT_STYLE);
		assertThat(style.isFontBold()).isTrue();
	}

	@Test
	public void testIsFontItalic() {
		Style style = new Style();
		assertThat(style.isFontItalic()).isFalse();

		style.addProperty(CssStringProperty.FONT_STYLE, Style.ITALIC_FONT_STYLE);
		assertThat(style.isFontItalic()).isTrue();
	}

	@Test
	public void testIsTextUnderlined() {
		Style style = new Style();
		assertThat(style.isTextUnderlined()).isFalse();

		style.addProperty(CssStringProperty.TEXT_DECORATION, Style.TEXT_DECORATION_UNDERLINE);
		assertThat(style.isTextUnderlined()).isTrue();
	}

	@Test
	public void testIsLeftAligned() {
		Style style = new Style();
		assertThat(style.isHorizontallyAlignedLeft()).isFalse();

		style.addProperty(CssStringProperty.TEXT_ALIGN, Style.LEFT_ALIGN);
		assertThat(style.isHorizontallyAlignedLeft()).isTrue();
	}

	@Test
	public void testIsRightAligned() {
		Style style = new Style();
		assertThat(style.isHorizontallyAlignedRight()).isFalse();

		style.addProperty(CssStringProperty.TEXT_ALIGN, Style.RIGHT_ALIGN);
		assertThat(style.isHorizontallyAlignedRight()).isTrue();
	}

	@Test
	public void testIsCenterAligned() {
		Style style = new Style();
		assertThat(style.isHorizontallyAlignedCenter()).isFalse();

		style.addProperty(CssStringProperty.TEXT_ALIGN, Style.CENTER_ALIGN);
		assertThat(style.isHorizontallyAlignedCenter()).isTrue();
	}

	@Test
	public void testIsBackgroundSet() {
		Style style = new Style();
		assertThat(style.isBackgroundSet()).isFalse();

		style.addProperty(CssColorProperty.BACKGROUND_COLOR, Color.RED);
		assertThat(style.isBackgroundSet()).isTrue();
	}

	@Test
	public void testisColorSet() {
		Style style = new Style();
		assertThat(style.isColorSet()).isFalse();

		style.addProperty(CssColorProperty.COLOR, Color.RED);
		assertThat(style.isColorSet()).isTrue();
	}

	@Test
	public void testIsBorderColorSet() {
		Style style = new Style();
		assertThat(style.isBorderColorSet()).isFalse();

		style.addProperty(CssColorProperty.BORDER_COLOR, Color.RED);
		assertThat(style.isBorderColorSet()).isTrue();
	}
}

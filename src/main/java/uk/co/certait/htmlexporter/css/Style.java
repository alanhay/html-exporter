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
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * 
 * @author alanhay
 * 
 */
public class Style {
	protected static final String BOLD_FONT_STYLE = "bold";
	protected static final String ITALIC_FONT_STYLE = "italic";
	protected static final String TEXT_DECORATION_UNDERLINE = "underline";
	protected static final String LEFT_ALIGN = "left";
	protected static final String RIGHT_ALIGN = "right";
	protected static final String CENTER_ALIGN = "center";
	protected static final String TOP_ALIGN = "top";
	protected static final String BOTTOM_ALIGN = "bottom";
	protected static final String MIDDLE_ALIGN = "middle";

	private Map<CssIntegerProperty, Integer> integerProperties;
	private Map<CssStringProperty, String> stringProperties;
	private Map<CssColorProperty, Color> colorProperties;

	public Style() {
		integerProperties = new HashMap<CssIntegerProperty, Integer>();
		stringProperties = new HashMap<CssStringProperty, String>();
		colorProperties = new HashMap<CssColorProperty, Color>();
	}

	protected void addProperty(CssIntegerProperty property, Integer value) {
		if (value != null) {
			integerProperties.put(property, value);
		}
	}

	protected void addProperty(CssStringProperty property, String value) {
		if (!StringUtils.isEmpty(value)) {
			stringProperties.put(property, value);
		}
	}

	protected void addProperty(CssColorProperty property, Color color) {
		if (color != null) {
			colorProperties.put(property, color);
		}
	}

	protected Map<CssIntegerProperty, Integer> getIntegerProperties() {
		return integerProperties;
	}

	protected Map<CssStringProperty, String> getStringProperties() {
		return stringProperties;
	}

	protected Map<CssColorProperty, Color> getColorProperties() {
		return colorProperties;
	}

	public int getProperty(CssIntegerProperty property) {
		return integerProperties.get(property) != null ? integerProperties.get(property) : 0;
	}

	public String getProperty(CssStringProperty property) {
		return stringProperties.get(property);
	}

	public Color getProperty(CssColorProperty property) {
		return colorProperties.get(property);
	}

	public boolean isFontSizeSet() {
		return integerProperties.containsKey(CssIntegerProperty.FONT_SIZE);
	}

	public boolean isWidthSet() {
		return integerProperties.containsKey(CssIntegerProperty.WIDTH);
	}

	public boolean isBorderWidthSet() {
		return integerProperties.containsKey(CssIntegerProperty.BORDER_WIDTH);
	}

	public boolean isFontNameSet() {
		return stringProperties.containsKey(CssStringProperty.FONT_FAMILY);
	}

	public boolean isFontBold() {
		return BOLD_FONT_STYLE.equals(stringProperties.get(CssStringProperty.FONT_WEIGHT));
	}

	public boolean isFontItalic() {
		return ITALIC_FONT_STYLE.equals(stringProperties.get(CssStringProperty.FONT_STYLE));
	}

	public boolean isTextUnderlined() {
		return TEXT_DECORATION_UNDERLINE.equals(stringProperties.get(CssStringProperty.TEXT_DECORATION));
	}

	public boolean isHorizontallyAlignedLeft() {
		return LEFT_ALIGN.equals(stringProperties.get(CssStringProperty.TEXT_ALIGN));
	}

	public boolean isHorizontallyAlignedRight() {
		return RIGHT_ALIGN.equals(stringProperties.get(CssStringProperty.TEXT_ALIGN));
	}

	public boolean isHorizontallyAlignedCenter() {
		return CENTER_ALIGN.equals(stringProperties.get(CssStringProperty.TEXT_ALIGN));
	}

	public boolean isVerticallyAlignedTop() {
		return TOP_ALIGN.equals(stringProperties.get(CssStringProperty.VERTICAL_ALIGN));
	}

	public boolean isVerticallyAlignedBottom() {
		return BOTTOM_ALIGN.equals(stringProperties.get(CssStringProperty.VERTICAL_ALIGN));
	}

	public boolean isVerticallyAlignedMiddle() {
		return MIDDLE_ALIGN.equals(stringProperties.get(CssStringProperty.VERTICAL_ALIGN));
	}

	public boolean isBackgroundSet() {
		return colorProperties.containsKey(CssColorProperty.BACKGROUND);
	}

	public boolean isBorderColorSet() {
		return colorProperties.containsKey(CssColorProperty.BORDER_COLOR);
	}

	public boolean isColorSet() {
		return colorProperties.containsKey(CssColorProperty.COLOR);
	}

	@Override
	public boolean equals(Object obj) {
		boolean equals = false;

		if (this == obj) {
			equals = true;
		} else if (obj != null && obj instanceof Style) {
			Style other = (Style) obj;
			equals = new EqualsBuilder().append(this.integerProperties, other.integerProperties)
					.append(this.stringProperties, other.stringProperties)
					.append(this.colorProperties, other.colorProperties).isEquals();
		}

		return equals;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37).append(integerProperties).append(stringProperties).append(colorProperties)
				.toHashCode();
	}
}

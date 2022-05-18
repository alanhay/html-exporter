package uk.co.certait.htmlexporter.css;

import static org.assertj.core.api.Assertions.assertThat;

import java.awt.Color;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;
import org.junit.Test;

public class StyleParserTest {

	private StyleParser parser = new StyleParser();

	@Test
	public void testParseStyleSheet() throws IOException {
		String stylesheet = IOUtils.resourceToString("/stylesheet_1.css", Charset.defaultCharset());

		Element e = new Element(Tag.valueOf("style"), "") {
			public String data() {
				return stylesheet;
			}
		};

		Map<String, Style> styleMap = parser.parseStyleSheet(e);

		assertThat(styleMap).hasSize(6);

		assertThat(styleMap).containsKey("th");
		Style style = styleMap.get("th");
		assertThat(style.getProperty(CssColorProperty.BACKGROUND_COLOR)).isEqualTo(Color.decode("#999999"));
		assertThat(style.getProperty(CssColorProperty.BORDER_COLOR)).isEqualTo(Color.decode("#556677"));
		assertThat(style.getProperty(CssIntegerProperty.BORDER_WIDTH)).isEqualTo(1);
		assertThat(style.getProperty(CssColorProperty.COLOR)).isEqualTo(Color.decode("#ffffff"));
		assertThat(style.getProperty(CssStringProperty.FONT_FAMILY)).isEqualTo("Georgia");
		assertThat(style.getProperty(CssStringProperty.BORDER_TOP_STYLE)).isEqualTo(Style.DASHED_BORDER);
		assertThat(style.getProperty(CssStringProperty.BORDER_BOTTOM_STYLE)).isEqualTo(Style.DASHED_BORDER);
		assertThat(style.getProperty(CssStringProperty.BORDER_LEFT_STYLE)).isEqualTo(Style.DASHED_BORDER);
		assertThat(style.getProperty(CssStringProperty.BORDER_RIGHT_STYLE)).isEqualTo(Style.DASHED_BORDER);
		assertThat(style.isVerticallyAlignedMiddle()).isTrue();
		assertThat(style.isFontBold()).isTrue();
		assertThat(style.isFontItalic()).isTrue();
		assertThat(style.isTextUnderlined()).isTrue();
		assertThat(style.getProperty(CssIntegerProperty.FONT_SIZE)).isEqualTo(12);

		assertThat(styleMap).containsKey("td");
		style = styleMap.get("td");
		assertThat(style.isBackgroundSet()).isTrue();
		assertThat(style.getProperty(CssColorProperty.BACKGROUND_COLOR)).isEqualTo(Color.decode("#999999"));
		assertThat(style.getProperty(CssStringProperty.BORDER_STYLE)).isEqualTo(Style.SOLID_BORDER);
		assertThat(style.getProperty(CssColorProperty.BORDER_TOP_COLOR)).isEqualTo(Color.decode("#656565"));
		assertThat(style.getProperty(CssColorProperty.BORDER_BOTTOM_COLOR)).isEqualTo(Color.decode("#656565"));
		assertThat(style.getProperty(CssColorProperty.BORDER_LEFT_COLOR)).isEqualTo(Color.decode("#656565"));
		assertThat(style.getProperty(CssColorProperty.BORDER_RIGHT_COLOR)).isEqualTo(Color.decode("#656565"));
		assertThat(style.getProperty(CssIntegerProperty.BORDER_WIDTH)).isEqualTo(1);
		assertThat(style.getProperty(CssColorProperty.COLOR)).isEqualTo(Color.decode("#333333"));
		assertThat(style.isVerticallyAlignedMiddle()).isTrue();
		assertThat(style.isHorizontallyAlignedRight()).isTrue();
		assertThat(style.isFontNameSet()).isFalse();
		assertThat(style.isFontBold()).isFalse();
		assertThat(style.isFontItalic()).isFalse();
		assertThat(style.isTextUnderlined()).isFalse();
		assertThat(style.getProperty(CssIntegerProperty.FONT_SIZE)).isEqualTo(10);

		assertThat(styleMap).containsKey(".okay");
		assertThat(styleMap).containsKey(".warning");
		assertThat(styleMap).containsKey(".total");
		assertThat(styleMap).containsKey(".fancy-border");

		style = styleMap.get(".fancy-border");
		assertThat(style.getProperty(CssColorProperty.BORDER_TOP_COLOR)).isEqualTo(Color.RED);
		assertThat(style.getProperty(CssColorProperty.BORDER_RIGHT_COLOR)).isEqualTo(Color.GREEN);
		assertThat(style.getProperty(CssColorProperty.BORDER_BOTTOM_COLOR)).isEqualTo(Color.ORANGE);
		assertThat(style.getProperty(CssColorProperty.BORDER_LEFT_COLOR)).isEqualTo(Color.CYAN);
		assertThat(style.getProperty(CssStringProperty.BORDER_TOP_STYLE)).isEqualTo(Style.DOTTED_BORDER);
		assertThat(style.getProperty(CssStringProperty.BORDER_RIGHT_STYLE)).isEqualTo(Style.SOLID_BORDER);
		assertThat(style.getProperty(CssStringProperty.BORDER_BOTTOM_STYLE)).isEqualTo(Style.DOUBLE_BORDER);
		assertThat(style.getProperty(CssStringProperty.BORDER_LEFT_STYLE)).isEqualTo(Style.DASHED_BORDER);
	}

	@Test
	public void testParseStyleSheets() throws IOException {
		String stylesheet = IOUtils.resourceToString("/stylesheet_1.css", Charset.defaultCharset());

		Elements elements = new Elements();
		Element element1 = new Element(Tag.valueOf("style"), "") {
			public String data() {
				return stylesheet;
			}
		};

		Element element2 = new Element(Tag.valueOf("style"), "") {
			public String data() {
				return ".warning{font-size:20px; color: orange}";
			}
		};

		elements.add(element1);
		elements.add(element2);

		Map<String, Style> styleMap = parser.parseStyleSheets(elements);
		assertThat(styleMap).containsKey(".warning");
		Style style = styleMap.get(".warning");

		// Not specified in 2nd <style/> so inherited from 1st <style/>
		assertThat(style.getProperty(CssColorProperty.BACKGROUND_COLOR)).isEqualTo(Color.decode("#ff0000"));

		// Specified only in 2nd <style/>
		assertThat(style.getProperty(CssIntegerProperty.FONT_SIZE)).isEqualTo(20);

		// Property in 1st <Style/> overwritten in 2nd <style/>
		assertThat(style.getProperty(CssColorProperty.COLOR)).isEqualTo(Color.ORANGE);
	}
}

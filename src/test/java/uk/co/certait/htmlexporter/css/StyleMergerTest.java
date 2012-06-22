package uk.co.certait.htmlexporter.css;

import java.awt.Color;

import org.junit.Assert;
import org.junit.Test;

public class StyleMergerTest
{
	@Test
	public void testMergeStyles()
	{
		Style tagStyle = new Style();
		Style classStyle = new Style();
		Style inlineStyle = new Style();
		
		tagStyle.addProperty(CssColorProperty.BACKGROUND, Color.RED);
		tagStyle.addProperty(CssStringProperty.FONT_WEIGHT, Style.BOLD_FONT_STYLE);
		
		classStyle.addProperty(CssIntegerProperty.BORDER_WIDTH, 3);
		classStyle.addProperty(CssStringProperty.FONT_STYLE, Style.TEXT_DECORATION_UNDERLINE);
		classStyle.addProperty(CssColorProperty.BACKGROUND, Color.BLUE);//override tag style
		
		inlineStyle.addProperty(CssColorProperty.COLOR, Color.WHITE);
		inlineStyle.addProperty(CssIntegerProperty.BORDER_WIDTH, 4);//override class style
		inlineStyle.addProperty(CssIntegerProperty.FONT_SIZE, 12);//override class style
		
		Style style = StyleMerger.mergeStyles(tagStyle, classStyle, inlineStyle);
		Assert.assertEquals(style.getProperty(CssColorProperty.BACKGROUND), Color.BLUE);
		Assert.assertEquals(style.getProperty(CssStringProperty.FONT_WEIGHT), Style.BOLD_FONT_STYLE);
		Assert.assertEquals(style.getProperty(CssIntegerProperty.BORDER_WIDTH), 4);
		Assert.assertEquals(style.getProperty(CssStringProperty.FONT_STYLE), Style.TEXT_DECORATION_UNDERLINE);
		Assert.assertEquals(style.getProperty(CssColorProperty.COLOR), Color.WHITE);
		Assert.assertEquals(style.getProperty(CssIntegerProperty.FONT_SIZE), 12);
	}
}

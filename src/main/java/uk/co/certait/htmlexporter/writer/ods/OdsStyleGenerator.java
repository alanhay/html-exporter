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
package uk.co.certait.htmlexporter.writer.ods;

import org.odftoolkit.odfdom.type.Color;
import org.odftoolkit.simple.style.Border;
import org.odftoolkit.simple.style.Font;
import org.odftoolkit.simple.style.StyleTypeDefinitions;
import org.odftoolkit.simple.style.StyleTypeDefinitions.CellBordersType;
import org.odftoolkit.simple.style.StyleTypeDefinitions.FontStyle;
import org.odftoolkit.simple.style.StyleTypeDefinitions.HorizontalAlignmentType;
import org.odftoolkit.simple.style.StyleTypeDefinitions.VerticalAlignmentType;
import org.odftoolkit.simple.table.Cell;

import uk.co.certait.htmlexporter.css.CssColorProperty;
import uk.co.certait.htmlexporter.css.CssIntegerProperty;
import uk.co.certait.htmlexporter.css.CssStringProperty;
import uk.co.certait.htmlexporter.css.Style;

public class OdsStyleGenerator
{
	public void styleCell(Cell cell, Style style)
	{
		if (style.isBackgroundSet())
		{
			cell.setCellBackgroundColor(new Color(style.getProperty(CssColorProperty.BACKGROUND)));
		}

		applyBorder(cell, style);
		applyHorizontalAlignment(cell, style);
		applyVerticalAlignment(cell, style);
		applyFont(cell, style);
	}
	
	protected void applyBorder(Cell cell, Style style)
	{
		if (style.isBorderWidthSet())
		{
			double borderWidth = style.getProperty(CssIntegerProperty.BORDER_WIDTH);
			Color borderColor;

			if (style.isBorderColorSet())
			{
				borderColor = new Color(style.getProperty(CssColorProperty.BORDER_COLOR));
			}
			else
			{
				borderColor = Color.BLACK;
			}
			
			//ods border too thick. divide by 5 for now
			cell.setBorders(CellBordersType.ALL_FOUR, new Border(borderColor, borderWidth / 5,
					StyleTypeDefinitions.SupportedLinearMeasure.PT));
		}	
	}

	protected void applyHorizontalAlignment(Cell cell, Style style)
	{

		if (style.isHorizontallyAlignedLeft())
		{
			cell.setHorizontalAlignment(HorizontalAlignmentType.LEFT);
		}
		else if (style.isHorizontallyAlignedRight())
		{
			cell.setHorizontalAlignment(HorizontalAlignmentType.RIGHT);
		}
		else if (style.isHorizontallyAlignedCenter())
		{
			cell.setHorizontalAlignment(HorizontalAlignmentType.CENTER);
		}
	}
	
	protected void applyVerticalAlignment(Cell cell, Style style)
	{

		if (style.isVerticallyAlignedTop())
		{
			cell.setVerticalAlignment(VerticalAlignmentType.TOP);
		}
		else if (style.isVerticallyAlignedBottom())
		{
			cell.setVerticalAlignment(VerticalAlignmentType.BOTTOM);
		}
		else if (style.isVerticallyAlignedMiddle())
		{
			cell.setVerticalAlignment(VerticalAlignmentType.MIDDLE);
		}
	}

	protected void applyFont(Cell cell, Style style)
	{
		Font font = cell.getFont();

		if (style.isFontNameSet())
		{
			font.setFamilyName(style.getProperty(CssStringProperty.FONT_FAMILY));
		}

		if (style.isFontBold() && style.isFontItalic())
		{
			font.setFontStyle(FontStyle.BOLDITALIC);
		}
		else if (style.isFontBold())
		{
			font.setFontStyle(FontStyle.BOLD);
		}
		else if (style.isFontItalic())
		{
			font.setFontStyle(FontStyle.ITALIC);
		}

		if (style.isFontSizeSet())
		{
			font.setSize(style.getProperty(CssIntegerProperty.FONT_SIZE));
		}

		if (style.isColorSet())
		{
			font.setColor(new Color(style.getProperty(CssColorProperty.COLOR)));
		}

		cell.setFont(font);
	}
}

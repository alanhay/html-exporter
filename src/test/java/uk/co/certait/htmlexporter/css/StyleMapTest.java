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

import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;
import org.junit.Test;

public class StyleMapTest {

	@Test
	public void testGetInlineStyle() {
		Element e = new Element(Tag.valueOf("td"), "") {
			
			@Override
			public boolean hasAttr(String attributeKey) {
				return true;
			}
			@Override
			public String attr(String attributeKey) {
				return "font-color: red; background: white; border: 1px dashed #678876";
			}
		};

		Style style = new StyleMap(null).getInlineStyle(e);

		// FIXME Use Optional
		assertThat(style).isNotNull();
	}
}

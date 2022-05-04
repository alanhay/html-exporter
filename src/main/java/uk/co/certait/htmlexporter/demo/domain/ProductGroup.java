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
package uk.co.certait.htmlexporter.demo.domain;

import org.apache.commons.text.WordUtils;

public enum ProductGroup {
	ELECTRICAL(1), SPORTS(2), HOME(3), GARDEN(4), TRAVEL(5), TOYS(6);

	private int id;

	private ProductGroup(int id) {
		this.id = id;
	}

	public String toString() {
		return WordUtils.capitalizeFully(this.name());
	}

	public int getId() {
		return id;
	}
}

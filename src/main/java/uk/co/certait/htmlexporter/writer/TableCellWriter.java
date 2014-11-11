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
package uk.co.certait.htmlexporter.writer;

import org.jsoup.nodes.Element;

public interface TableCellWriter {
	public static final String COLUMN_SPAN_ATTRIBUTE = "colspan";
	public static final String DATA_GROUP_ATTRIBUTE = "data-group";
	public static final String DATA_GROUP_OUTPUT_ATTRIBUTE = "data-group-output";
	public static final String DATE_CELL_ATTRIBUTE = "data-date-cell-format";
	public static final String DATA_CELL_COMMENT_ATTRIBUTE = "data-cell-comment";
	public static final String DATA_CELL_COMMENT_DIMENSION_ATTRIBUTE = "data-cell-comment-dimension";
	public static final String DATA_TEXT_CELL = "data-text-cell";
	public static final String DATA_FREEZE_PANE_CELL = "data-freeze-pane-cell";

	public void writeCell(Element cell, int row, int column);
}

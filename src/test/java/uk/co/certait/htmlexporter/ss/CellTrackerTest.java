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
package uk.co.certait.htmlexporter.ss;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import uk.co.certait.htmlexporter.ss.CellTracker;

public class CellTrackerTest
{
	private CellTracker tracker;

	@Before
	public void setUp()
	{
		tracker = new CellTracker();
	}

	@Test
	public void testAddCell()
	{
		tracker.addCell(0, 0, 1, 1);
		Assert.assertEquals(1, tracker.getTrackedRowCount());
		Assert.assertEquals(1, tracker.getNextColumnIndexForRow(0));

		tracker.addCell(0, 1, 1, 1);
		Assert.assertEquals(1, tracker.getTrackedRowCount());
		Assert.assertEquals(2, tracker.getNextColumnIndexForRow(0));

		tracker.addCell(0, 2, 1, 2);// fills columns 2 and 3 in this row
		Assert.assertEquals(1, tracker.getTrackedRowCount());
		Assert.assertEquals(4, tracker.getNextColumnIndexForRow(0));

		tracker.addCell(0, 4, 2, 1);// fills column 4 in rows 0 and 1
		// now tracking additional row
		Assert.assertEquals(2, tracker.getTrackedRowCount());
		Assert.assertEquals(0, tracker.getNextColumnIndexForRow(1));

		tracker.addCell(1, 0, 1, 1);
		Assert.assertEquals(1, tracker.getNextColumnIndexForRow(1));

		tracker.addCell(1, 1, 1, 1);
		Assert.assertEquals(2, tracker.getNextColumnIndexForRow(1));

		tracker.addCell(1, 2, 1, 1);
		Assert.assertEquals(3, tracker.getNextColumnIndexForRow(1));

		tracker.addCell(1, 3, 1, 1);
		// index 4 already filled by merge from row above
		Assert.assertEquals(5, tracker.getNextColumnIndexForRow(1));

		tracker.addCell(2, 0, 1, 1);
		tracker.addCell(2, 1, 3, 2);
		Assert.assertEquals(0, tracker.getNextColumnIndexForRow(3));
		// fill 0 and 1 and 2 filled by merge from above so next should be 3
		tracker.addCell(3, 0, 1, 1);
		Assert.assertEquals(3, tracker.getNextColumnIndexForRow(3));
	}
}

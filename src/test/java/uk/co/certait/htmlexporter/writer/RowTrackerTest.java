package uk.co.certait.htmlexporter.writer;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class RowTrackerTest {
	private RowTracker tracker;

	@Before
	public void setUp() {
		tracker = new RowTracker();
	}

	@Test
	public void testAddCell() {
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

package uk.co.certait.htmlexporter.writer;

import static org.assertj.core.api.Assertions.assertThat;

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
		assertThat(tracker.getTrackedRowCount()).isEqualTo(1);
		assertThat(tracker.getNextColumnIndexForRow(0)).isEqualTo(1);

		tracker.addCell(0, 1, 1, 1);
		assertThat(tracker.getTrackedRowCount()).isEqualTo(1);
		assertThat(tracker.getNextColumnIndexForRow(0)).isEqualTo(2);

		// fills columns 2 and 3 in this row
		tracker.addCell(0, 2, 1, 2);
		assertThat(tracker.getTrackedRowCount()).isEqualTo(1);
		assertThat(tracker.getNextColumnIndexForRow(0)).isEqualTo(4);

		tracker.addCell(0, 4, 2, 1);
		// now tracking additional row
		assertThat(tracker.getTrackedRowCount()).isEqualTo(2);
		assertThat(tracker.getNextColumnIndexForRow(1)).isEqualTo(0);

		tracker.addCell(1, 0, 1, 1);
		assertThat(tracker.getNextColumnIndexForRow(1)).isEqualTo(1);

		tracker.addCell(1, 1, 1, 1);
		assertThat(tracker.getNextColumnIndexForRow(1)).isEqualTo(2);

		tracker.addCell(1, 2, 1, 1);
		assertThat(tracker.getNextColumnIndexForRow(1)).isEqualTo(3);

		tracker.addCell(1, 3, 1, 1);
		// index 4 already filled by merge from row above
		assertThat(tracker.getNextColumnIndexForRow(1)).isEqualTo(5);

		tracker.addCell(2, 0, 1, 1);
		tracker.addCell(2, 1, 3, 2);
		assertThat(tracker.getNextColumnIndexForRow(3)).isEqualTo(0);
		// fill 0 and 1 and 2 filled by merge from above so next should be 3
		tracker.addCell(3, 0, 1, 1);
		assertThat(tracker.getNextColumnIndexForRow(3)).isEqualTo(3);
	}
}

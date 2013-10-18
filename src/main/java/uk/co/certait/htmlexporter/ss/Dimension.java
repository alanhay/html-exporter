package uk.co.certait.htmlexporter.ss;

public class Dimension {

	public int width;
	public int height;

	/**
	 * Converts a String of the format x,y to a width and height.
	 * 
	 * @param dimensions
	 */
	public Dimension(String dimensions) {
		String[] parts = dimensions.split(",");

		try {
			width = Integer.parseInt(parts[0]);
			height = Integer.parseInt(parts[1]);
		} catch (NumberFormatException ex) {
			throw new IllegalArgumentException();
		}
	}

	public Dimension(int width, int height) {
		this.width = width;
		this.height = height;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}
}

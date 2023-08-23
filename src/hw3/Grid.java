package hw3;

import api.Tile;

/**
 * @author Asray Gopa
 * Represents the game's grid.
 */
public class Grid {

	private int width;
	private int height;
	private Tile[][] grid;

	/**
	 * Creates a new grid.
	 * 
	 * @param width  - number of columns
	 * @param height - number of rows
	 */
	public Grid(int width, int height) {
		this.width = width;
		this.height = height;
		grid = new Tile[width][height];
	}

	/**
	 * Get the grid's width.
	 * 
	 * @return width
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * Get the grid's height.
	 * 
	 * @return height
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * Gets the tile for the given column and row.
	 * 
	 * @param x - the column
	 * @param y - the row
	 * @return
	 */
	public api.Tile getTile(int x, int y) {
		if (x < 0 || x >= width || y < 0 || y >= height) { // making sure it's not out of bounds
			throw new IndexOutOfBoundsException(x + ", " + y);
		}
		return grid[x][y];
	}

	/**
	 * Sets the tile for the given column and row. Calls tile.setLocation().
	 * 
	 * @param tile - the tile to set
	 * @param x    - the column
	 * @param y    - the row
	 */
	public void setTile(api.Tile tile, int x, int y) {
		grid[x][y] = tile;
		tile.setLocation(x, y);
	}

	/**
	 * public java.lang.String toString() Overrides: toString in class
	 * java.lang.Object
	 */
	@Override
	public String toString() {
		String str = "";
		for (int y = 0; y < getHeight(); y++) {
			if (y > 0) {
				str += "\n";
			}
			str += "[";
			for (int x = 0; x < getWidth(); x++) {
				if (x > 0) {
					str += ",";
				}
				str += getTile(x, y);
			}
			str += "]";
		}
		return str;
	}

}

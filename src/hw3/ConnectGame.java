package hw3;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import api.ScoreUpdateListener;
import api.ShowDialogListener;
import api.Tile;

/**
 * @author Asray Gopa 
 * Class that models a game.
 */
public class ConnectGame {

	private long score;
	private int minTileLevel;
	private int maxTileLevel;
	private Grid grid;
	private java.util.Random rand;
	private ArrayList<Tile> selectedTiles;
	private ShowDialogListener dialogListener;
	private ScoreUpdateListener scoreListener;

	/**
	 * Constructs a new ConnectGame object with given grid dimensions and minimum
	 * and maximum tile levels.
	 * 
	 * @param width  - grid width
	 * @param height - grid height
	 * @param min    - minimum tile level
	 * @param max    - maximum tile level
	 * @param rand   - random number generator
	 */
	public ConnectGame(int width, int height, int min, int max, java.util.Random rand) {
		this.rand = rand;
		minTileLevel = min;
		maxTileLevel = max;
		grid = new Grid(width, height);
		score = 0;
		selectedTiles = new ArrayList<>();
	}

	/**
	 * Gets a random tile with level between minimum tile level inclusive and
	 * maximum tile level exclusive. For example, if minimum is 1 and maximum is 4,
	 * the random tile can be either 1, 2, or 3.
	 * 
	 * @return tile - a tile with random level between minimum inclusive and maximum
	 *         exclusive
	 */
	public Tile getRandomTile() {
		//generating level
		int randomLevel = (int) (rand.nextInt(maxTileLevel - minTileLevel) + minTileLevel);
		//setting level value to tile
		Tile tile = new Tile(randomLevel); 
		return tile;
	}

	/**
	 * Regenerates the grid with all random tiles produced by getRandomTile().
	 */
	public void radomizeTiles() {
		Tile tile;
		//nested for loop checking each element in the grid
		for (int i = 0; i < grid.getHeight(); i++) { 
			for (int j = 0; j < grid.getWidth(); j++) { 
				tile = getRandomTile(); //getting a random tile
				grid.setTile(tile, j, i); //setting the tile to grid position
			}
		}
	}

	/**
	 * Determines if two tiles are adjacent to each other. The may be next to each
	 * other horizontally, vertically, or diagonally.
	 * 
	 * @param t1 - one of the two tiles
	 * @param t2 - one of the two tiles
	 * @return true if they are next to each other horizontally, vertically, or
	 *         diagonally on the grid, false otherwise
	 */
	public boolean isAdjacent(Tile t1, Tile t2) {
		int x1 = t1.getX();
		int x2 = t2.getX();
		int y1 = t1.getY();
		int y2 = t2.getY();
		//condition statement for checking if two tiles next to each other horizontally, vertically, or diagonally on the grid
		if (Math.abs(x1 - x2) <= 1 && Math.abs(y1 - y2) <= 1) { 
			return true;
		}
		return false;
	}

	/**
	 * Indicates the user is trying to select (clicked on) a tile to start a new
	 * selection of tiles. If a selection of tiles is already in progress, the
	 * method should do nothing and return false. If a selection is not already in
	 * progress (this is the first tile selected), then start a new selection of
	 * tiles and return true.
	 * 
	 * @param x - the column of the tile selected
	 * @param y - the row of the tile selected
	 * @return true if this is the first tile selected, otherwise false
	 */
	public boolean tryFirstSelect(int x, int y) {
		//setting coordinates to tile
		Tile tile = grid.getTile(x, y); 
		//checking if arraylist is not empty
		if (!selectedTiles.isEmpty() || selectedTiles.size() != 0) { 
			return false;
		} else { //if arraylist is not empty, firstSelect is true
			tile.setSelect(true); // setting tile as selected to true
			selectedTiles.add(tile); //adding tile to arraylist
			return true;
		}
	}

	/**
	 * Indicates the user is trying to select (mouse over) a tile to add to the
	 * selected sequence of tiles. The rules of a sequence of tiles are: 1. The
	 * first two tiles must have the same level. 2. After the first two, each tile
	 * must have the same level or one greater than the level of the previous tile.
	 * For example, given the sequence: 1, 1, 2, 2, 2, 3. The next selected tile
	 * could be a 3 or a 4. If the use tries to select an invalid tile, the method
	 * should do nothing. If the user selects a valid tile, the tile should be added
	 * to the list of selected tiles.
	 * 
	 * @param x - the column of the tile selected
	 * @param y - the row of the tile selected
	 */
	public void tryContinueSelect(int x, int y) {
		//checking if arraylist is not empty
		if (selectedTiles.size() != 0) { 
			Tile lastSelectedTile = selectedTiles.get(selectedTiles.size() - 1); //getting the value of the last selected tile by finding out the last element of the arraylist
			Tile continueTile = grid.getTile(x, y); //this tile will be the one to check 
			
			if (continueTile.isSelected()) {
				if (continueTile == selectedTiles.get(selectedTiles.size() - 2)) { // checking the second to last tile value from arraylist to see we didnt go back on the tile
					unselect(lastSelectedTile.getX(), lastSelectedTile.getY()); // unselecting the "continuing tile"
				}
			}
			//checking if there's one tile selected
			if (selectedTiles.size() == 1) { 
				if (lastSelectedTile.getLevel() == continueTile.getLevel() && (!grid.getTile(x, y).isSelected())) { //checking if the "first selection" is of the same level
					selectedTiles.add(continueTile); //adding "continuing tile" to array list
					continueTile.setSelect(true); //setting the tile as selected as true
				}
			} else { //essentially same thing but checking after the first tile in the arraylist. the first two selections have to be the same level but after that it can be of the same level or the next level
				if ((lastSelectedTile.getLevel() + 1) == continueTile.getLevel() && (!grid.getTile(x, y).isSelected()) || lastSelectedTile.getLevel() == continueTile.getLevel()) {
					selectedTiles.add(continueTile);//adding "continuing tile" to array list
					continueTile.setSelect(true);//setting the tile as selected as true
				}
			}
		}
	}

	/**
	 * Indicates the user is trying to finish selecting (click on) a sequence of
	 * tiles. If the method is not called for the last selected tile, it should do
	 * nothing and return false. Otherwise it should do the following: 1. When the
	 * selection contains only 1 tile reset the selection and make sure all tiles
	 * selected is set to false. 2. When the selection contains more than one block:
	 * a. Upgrade the last selected tiles with upgradeLastSelectedTile(). b. Drop
	 * all other selected tiles with dropSelected(). c. Reset the selection and make
	 * sure all tiles selected is set to false.
	 * 
	 * @param x - the column of the tile selected
	 * @param y - the row of the tile selected
	 * @return return false if the tile was not selected, otherwise return true
	 */
	public boolean tryFinishSelection(int x, int y) {
		if (selectedTiles.size() >= 1) { //
			if (selectedTiles.size() == 1) { // if there only one tile in the arraylist and the user is trying to finish, the game should unselect that tile
				unselect(x, y); //unselecting tile
			} else {
				for (int i = 0; i < selectedTiles.size(); i++) {
					score += selectedTiles.get(i).getValue(); //increasing the score by getting all the values from the loop
				}
				scoreListener.updateScore(score); // updating score
				upgradeLastSelectedTile(); //increasing the score of all selected tiles
				dropSelected();
			}
			return true;
		}
		return false;
	}

	/**
	 * Increases the level of the last selected tile by 1 and removes that tile from
	 * the list of selected tiles. The tile itself should be set to unselected.If
	 * the upgrade results in a tile that is greater than the current maximum tile
	 * level, both the minimum and maximum tile level are increased by 1. A message
	 * dialog should also be displayed with the message "New block 32, removing
	 * blocks 2". Not that the message shows tile values and not levels. Display a
	 * message is performed with dialogListener.showDialog("Hello, World!");
	 */
	public void upgradeLastSelectedTile() {
		Tile lastSelectedtile = selectedTiles.get(selectedTiles.size() - 1); //	 Get the last selected tile
		int updateMax = lastSelectedtile.getLevel() + 1; // Increase the level of the tile by 1
		lastSelectedtile.setLevel(updateMax);
		unselect(lastSelectedtile.getX(), lastSelectedtile.getY()); // Unselect the tile
		
		if (updateMax > maxTileLevel) {	// Checking if the updated level is greater than the max tile level in the game
			// Updating the max and min tile levels
			maxTileLevel = maxTileLevel + 1;
			minTileLevel = minTileLevel + 1;
			// Showing a dialog to inform the user about the new block and removed blocks
			dialogListener.showDialog("New block" + lastSelectedtile.getValue() + ", removing blocks " + (int) Math.pow(2, minTileLevel - 1));
			dropLevel(minTileLevel - 1); // Drop the level of the tiles
		}
	}

	/**
	 * Gets the selected tiles in the form of an array. This does not mean selected
	 * tiles must be stored in this class as a array.
	 * 
	 * @return selectedTiles - the selected tiles in the form of an array
	 */

	public Tile[] getSelectedAsArray() {
		Tile[] tile = new Tile[selectedTiles.size()]; 
		return selectedTiles.toArray(tile);
	}

	/**
	 * Removes all tiles of a particular level from the grid. When a tile is
	 * removed, the tiles above it drop down one spot and a new random tile is
	 * placed at the top of the grid.
	 * 
	 * @param level
	 */
	public void dropLevel(int level) {
		for (int col = 0; col < grid.getWidth(); col++) {
		    // Creating an arraylist to store tiles that need to be upgraded
		    ArrayList<Tile> tilesToUpgrade = new ArrayList<>();
		    for (int row = 0; row < grid.getHeight(); row++) {
		        Tile tile = grid.getTile(col, row);
		        if (tile.getLevel() != level) { // Checking if the tile's level is not equal to the specified level
		            tilesToUpgrade.add(tile); // Adding the tile to the list of tiles to be upgraded
		        }
		    }
		    int numNewTiles = grid.getHeight() - tilesToUpgrade.size();
		    for (int row = 0; row < grid.getHeight(); row++) {
		        Tile tile; // Creating a variable to store the current tile
		        if (row < numNewTiles) { // Checking if the row is less than the number of new tiles needed, create a random tile
		            tile = getRandomTile();
		        } else { //else getting the next tile from the list of tiles to upgrade
		            tile = tilesToUpgrade.get(row - numNewTiles);
		        }
		        grid.setTile(tile, col, row); // Setting the tile in the grid at the current row and column

		    }
		}

	}

	/**
	 * Removes all selected tiles from the grid. When a tile is removed, the tiles
	 * above it drop down one spot and a new random tile is placed at the top of the
	 * grid.
	 */
	public void dropSelected() {
		for (int i = 0; i < grid.getWidth(); i++) {
		    // Creating a new list to store tiles that will be dropped
			ArrayList<Tile> tilesToBeDropped = new ArrayList<Tile>();
			for (int j = 0; j < grid.getHeight(); j++) { // Going over each row of the current column
				Tile selectTile = grid.getTile(i, j); //Get the tile at the current row and column
				if (!selectTile.isSelected()) { // If the tile is not selected, add it to the list of tiles to be dropped
					tilesToBeDropped.add(selectTile);
				} else {  // Otherwise, unselect the tile
					unselect(i, j);
				}
			}
			for (int z = 0; z < grid.getHeight(); z++) {  // Going over each row of the current column
				int numberOfNewTiles = grid.getHeight() - tilesToBeDropped.size();  // Calculating the number of new tiles to add to the column
				if (z < numberOfNewTiles) { // If the current row is less than the number of new tiles, a random tile will be added

					grid.setTile(getRandomTile(), i, z);
				} else {  // Otherwise,a tile will  be added from the list of tiles to be dropped
					grid.setTile(tilesToBeDropped.get(z - numberOfNewTiles), i, z);
				}
			}
		}
	}

	/**
	 * Remove the tile from the selected tiles.
	 * 
	 * @param x - column of the tile
	 * @param y - row of the tile
	 */
	public void unselect(int x, int y) {
		grid.getTile(x, y).setSelect(false);
		selectedTiles.remove(grid.getTile(x, y));
	}

	/**
	 * Gets the player's score.
	 * 
	 * @return score - the score
	 */
	public long getScore() {
		return score;
	}

	/**
	 * Gets the game grid.
	 * 
	 * @return grid - the grid
	 */

	public Grid getGrid() {
		return grid;
	}

	/**
	 * Gets the minimum tile level.
	 * 
	 * @return minTileLevel - the minimum tile level
	 */
	public int getMinTileLevel() {
		return minTileLevel;
	}

	/**
	 * Gets the maximum tile level.
	 * 
	 * @return maxTileLevel - the maximum tile level
	 */
	public int getMaxTileLevel() {
		return maxTileLevel;
	}

	/**
	 * Sets the player's score.
	 * 
	 * @param score - number of points
	 */
	public void setScore(long score) {
		this.score = score;
	}

	/**
	 * Sets the game's grid.
	 * 
	 * @param grid - game's grid
	 */
	public void setGrid(Grid grid) {
		this.grid = grid;
	}

	/**
	 * Sets the minimum tile level.
	 * 
	 * @param minTileLevel - the lowest level tile
	 */
	public void setMinTileLevel(int minTileLevel) {
		this.minTileLevel = minTileLevel;
	}

	/**
	 * Sets the maximum tile level.
	 * 
	 * @param maxTileLevel - the highest level tile
	 */
	public void setMaxTileLevel(int maxTileLevel) {
		this.maxTileLevel = maxTileLevel;
	}

	/**
	 * Sets callback listeners for game events.
	 * 
	 * @param dialogListener listener for creating a user dialog
	 * @param scoreListener  listener for updating the player's score
	 */
	public void setListeners(ShowDialogListener dialogListener, ScoreUpdateListener scoreListener) {
		this.dialogListener = dialogListener;
		this.scoreListener = scoreListener;
	}

	/**
	 * Save the game to the given file path.
	 * 
	 * @param filePath location of file to save
	 */
	public void save(String filePath) {
		GameFileUtil.save(filePath, this);
	}

	/**
	 * Load the game from the given file path
	 * 
	 * @param filePath location of file to load
	 * @throws FileNotFoundException
	 */
	public void load(String filePath) {
		GameFileUtil.load(filePath, this);
	}
}

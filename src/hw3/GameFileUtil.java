package hw3;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import api.Tile;

/**
 * @author Asray 
 * Utility class with static methods for saving and loading game files.
 */
public class GameFileUtil {
	
	public GameFileUtil() {

	}

	/**
	 * Saves the current game state to a file at the given file path.
	 * <p>
	 * The format of the file is one line of game data followed by multiple lines of
	 * game grid. The first line contains the: width, height, minimum tile level,
	 * maximum tile level, and score. The grid is represented by tile levels. The
	 * conversion to tile values is 2^level, for example, 1 is 2, 2 is 4, 3 is 8, 4
	 * is 16, etc. The following is an example:
	 * 
	 * 5 8 1 4 100
	 * 1 1 2 3 1
	 * 2 3 3 1 3
	 * 3 3 1 2 2
	 * 3 1 1 3 1
	 * 2 1 3 1 2
	 * 2 1 1 3 1
	 * 4 1 3 1 1
	 * 1 3 3 3 3
	 * 
	 * @param filePath the path of the file to save
	 * @param game     the game to save
	 */
	public static void save(String filePath, ConnectGame game) {
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));
			String gameData = game.getGrid().getWidth() + " " + game.getGrid().getHeight() + " "
					+ game.getMinTileLevel() + " " + game.getMaxTileLevel() + " " + game.getScore();
			writer.write(gameData);
			writer.newLine();
			for (int i = 0; i < game.getGrid().getHeight(); i++) { // Going through each tile in the grid and writing its level to the file
				for (int j = 0; j < game.getGrid().getWidth(); j++) {
					if (j != game.getGrid().getWidth() - 1) { // Checking if the tile is not the last one in the row, writing its level and a space 
						writer.write(game.getGrid().getTile(j, i).getLevel() + " ");
					} else if (i != game.getGrid().getHeight() - 1) {
						writer.write(game.getGrid().getTile(j, i).getLevel() + "\n");// Checking if the tile is the last one in the row but not the last row, writing its level and a newline
					} else {
						writer.write(game.getGrid().getTile(j, i).getLevel() + ""); // Checking if the tile is the last one in the last row, only write its level.

					}
				}
			}
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	/**
	 *  Loads the file at the given file path into the given game object. 
	 * When the method returns the game object has been modified to 
	 * represent the loaded game. See the save() method for the
	 *  specification of the file format.
	 * @param filePath - the path of the file to load
	 * @param game - the game to modify
	 */
	public static void load(java.lang.String filePath, ConnectGame game) {
		try {
			File file = new File(filePath);
			try (Scanner scnr = new Scanner(file)) {
				int width = scnr.nextInt();
				int height = scnr.nextInt();
				int min = scnr.nextInt();
				int max = scnr.nextInt();
				int score = scnr.nextInt();
				Grid grid = new Grid(width, height);

				game.setGrid(grid); //Setting the grid object for the game
				game.setMaxTileLevel(max); //Setting the maximum tile level for the game
				game.setMinTileLevel(min); //Setting the minimum tile level for the game
				game.setScore(score); //Setting the current score for the game
				for (int i = 0; i < height; i++) { //Going over each element in the grid
					for (int j = 0; j < width; j++) {
						Tile tile = new Tile(scnr.nextInt()); //Creating a new Tile object with the level read from the file 
						grid.setTile(tile, j, i); //Setting the Tile object at the current position of the grid
 
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}

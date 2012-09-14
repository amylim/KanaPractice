package com.chasingkytes.kana.practice;

import java.util.Random;

/**
 * Game grid layout for the game. It is a two dimensional array of KanaBubble objects.
 * @author Amy Lim
 */
public class KanaBubbleGrid {
	
	final static int TARGET_HIT = 0;
	final static int TARGET_MISS = 1;
	final static int TARGET_NONE = 2; 
	
	int maxRows;
	int maxColumns;
	final float diameter = 100;
	KanaBubble[][] myGrid;
	KanaBubble target;
	int targetIndex;	
	
	/**
	 * Creates a KanaBubbleGrid. Default constructor for KanaBubbleGrid. 
	 */
	public KanaBubbleGrid() {
		maxRows = 2;
		maxColumns = 3;	
		
		initializeGrid();
		setTarget();
	}
	
	/**
	 * Creates a KanaBubbleGrid. 
	 * @param r - number of rows for KanaBubbleGrid 
	 * @param c - number of columns for KanaBubbleGrid
	 */
	public KanaBubbleGrid(int r, int c) {
		maxRows = r;
		maxColumns = c;
		
		initializeGrid();
		setTarget();
		
	}
	
	/**
	 * Obtain the amount of rows in the KanaBubbleGrid object
	 * @return int - <i>number of rows in KanaBubbleGrid</i>
	 */
	public int getMaxRows() {
		return this.maxRows;
	}
	
	/**
	 * Obtain the amount of columns in the KanaBubbleGrid object
	 * @return int - <i>number of columns in KanaBubbleGrid</i>
	 */
	public int getMaxColumns() {
		return this.maxColumns;
	}
	
	/**
	 * Obtains the KanaBubble in the KanaBubbleGrid.
	 * @param r - <i>row of the KanaBubble</i>
	 * @param c - <i>column of the KanaBubble</i>
	 * @return KanaBubble in row <i>r</i> and column <i>c</i>, 
	 * returns <i>null</i> if it is out of bounds
	 */
	public KanaBubble getKanaBubble(int r, int c) {
		if(r < maxRows && c < maxColumns)
			return myGrid[r][c];
		return null;
	}
	
	/**
	 * Obtains the width of the grid.
	 * @return width of KanaBubbleGrid
	 */
	public float getWidth() {
		return maxColumns * diameter;
	}
	
	/**
	 * Obtains the height of the grid.
	 * @return height of KanaBubbleGrid
	 */
	public float getHeight() {
		return maxRows * diameter;
	}
	
	/**
	 * Checks if the KanaBubble at row <i>r</i> and column <i>c</i>, match the kanaIndex of the target
	 * @param r
	 * @param c
	 * @return
	 */
	public boolean isPointerEqualsTarget(int r, int c) {
		if(myGrid[r][c].getKanaIndex() == targetIndex)
			return true;
		return false;
	}
	
	/**
	 * Obtains the target index of the KanaBubbleGrid
	 * @return int - <i>targetIndex</i>
	 */
	public int getTargetIndex() {
		return this.targetIndex;
	}
	
	/**
	 * Obtains the target KanaBubble object in the KanaBubbleGrid.
	 * @return KanaBubble
	 */
	public KanaBubble getTarget() {
		return this.target;
	}
	
//	public int[] getRowColumnFromXY(float x, float y) {
//		int[] coords = new int[2];
//		float maxX = getWidth();
//		float maxY = getHeight();		
//	
//		if(x < maxX && y < maxY) {
//			coords[0] = (int) (y / diameter);	//row
//			coords[1] = (int) (x / diameter);	//column
//		} else {
//			coords[0] = -1;
//		}
//		
//		return coords;
//	}
	
	/**
	 * Obtains the row and column of the KanaBubble that the user touched from the 
	 * x and y coordinates of the user's touch on the screen. 
	 * @param x - <i>x position of the user's touch</i>
	 * @param y - <i>y position of the user's touch</i>
	 * @param sideMargin - <i>x offset of the game grid</i>
	 * @param topMargin - <i>y offset of the game grid</i>
	 * @return int[2] - <i>the value at index 0 is the row of KanaBubble that the user touched 
	 * (it will be -1 if the user did not touch a KanaBubble on the grid),
	 * the value at index 1 is the column of the KanaBubble that the user touched. </i>
	 */
	public int[] getRowColumnFromXY(float x, float y, float sideMargin, float topMargin) {
		//coordinates that will store the row and column of the user's touch
		int[] coords = new int[2];

		//maximum x and y coordinates of the grid on the screen
		float maxX = getWidth() + sideMargin;
		float maxY = getHeight() + topMargin;		
	
		if(x >= sideMargin && x < maxX && y >= topMargin && y < maxY) {
			//reset the x and y values as if the grid was first drawn from (0,0) instead of the with the offsets (sideMargin, topMargin)
			//makes it easier to calculate the row and column from the user's touch
			x = x - sideMargin;
			y = y - topMargin;
			coords[0] = (int) (y / diameter);	//row
			coords[1] = (int) (x / diameter);	//column
		} else {
			//if the touch is not in the grid, the row position is set to -1
			coords[0] = -1;
		}
		return coords;
	}
	
//	public int processGridTouch(float x, float y) {
//		int[] coords = getRowColumnFromXY(x, y);
//		if(coords[0] == -1) {
//			//do nothing if the grid was not touched
//			return TARGET_NONE;
//		} else {
//			//check if the user selected the target
//			if(isPointerEqualsTarget(coords[0], coords[1])) {
//				//if the user selected the correct target change the color to blue
//				myGrid[coords[0]][coords[1]].setColor(KanaBubble.BLUE);
//				return TARGET_HIT;
//			} else {
//				//if the user did not select the target change the color to red
//				myGrid[coords[0]][coords[1]].setColor(KanaBubble.RED);
//				return TARGET_MISS;
//			}
//		}
//	}
	
	/**
	 * If the user touches the game grid, process whether or not the user 
	 * hit the correct Kana or not. Returns the constant indicating the 
	 * type action from the user.
	 * @param x - <i>x position of the user's touch</i>
	 * @param y - <i>y position of the user's touch</i>
	 * @param sideMargin - <i>x offset of the game grid</i>
	 * @param topMargin - <i>y offset of the game grid</i>
	 * @return <b>TARGET_NONE</b> - user did not touch the grid, 
	 * <b>TARGET_HIT</b> - user hit the correct Kana, 
	 * <b>TARGET_MISS</b> - user hit the wrong Kana
	 */
	public int processGridTouch(float x, float y, float sideMargin, float topMargin) {
		int[] coords = getRowColumnFromXY(x, y, sideMargin, topMargin);
		if(coords[0] == -1) {
			//do nothing if the grid was not touched
			return TARGET_NONE;
		} else {
			//check if the user selected the target
			if(isPointerEqualsTarget(coords[0], coords[1])) {
				//if the user selected the correct target change the color to blue
				myGrid[coords[0]][coords[1]].setColor(KanaBubble.BLUE);
				return TARGET_HIT;
			} else {
				//if the user did not select the target change the color to red
				myGrid[coords[0]][coords[1]].setColor(KanaBubble.RED);
				return TARGET_MISS;
			}
		}
	}
	
	/**
	 * Sets the target Kana that the user will need to find on the game grid.
	 */
	private void setTarget() {
		//randomize the location on the grid
		Random rand = new Random();
		int targetRow = rand.nextInt(maxRows);
		int targetColumn = rand.nextInt(maxColumns);
		
		//randomize the target Kana
		targetIndex = rand.nextInt(70);
		target = new KanaBubble(targetIndex);
		
		removeRepeatTargets();
		
		//set the target Kana on the game grid
		myGrid[targetRow][targetColumn].setKana(targetIndex); 
	}
	
	/**
	 * Removes repeats of the target Kana that the user will be looking 
	 * for on the game grid.
	 */
	private void removeRepeatTargets() {
		for(int i = 0; i < maxRows; i++) {
			for(int j = 0; j < maxColumns; j++) {
				while(myGrid[i][j].getKanaIndex() == targetIndex) {
					myGrid[i][j].randomizeKana();
				}
			}
		}
	}
	
	/**
	 * Initializes the game grid to be filled with random Kana.
	 */
	private void initializeGrid() {
		myGrid = new KanaBubble[maxRows][maxColumns];
		for(int i = 0; i < maxRows; i++) {
			for(int j = 0; j < maxColumns; j++) {
				KanaBubble temp = new KanaBubble();
				temp.setXY(diameter*j, diameter*i);			
				temp.randomizeKana();
				myGrid[i][j] = temp;
			}
		}
	}
}

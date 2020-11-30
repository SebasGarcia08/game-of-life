package model;

import java.util.HashSet;

public class LifeGame implements Game {

	protected class Cell {

		int i;
		int j;

		public Cell(int i, int j) {
			this.i = i;
			this.j = j;
		}

	}

	private boolean[][] grid;

	public LifeGame(int x, int y) {
		grid = new boolean[x][y];
	}

	@Override
	public boolean check(int i, int j) {

		try {
			grid[i][j] = true;
		} catch (IndexOutOfBoundsException e) {
			return false;
		}

		return true;
	}

	@Override
	public boolean uncheck(int i, int j) {
		try {
			grid[i][j] = false;
		} catch (IndexOutOfBoundsException e) {
			return false;
		}

		return true;
	}

	@Override
	public boolean[][] next() {

		boolean[][] newGrid = new boolean[grid.length][grid[0].length];

		for (int i = 0; i < grid.length; i++) {
			for (int j = 0; j < grid[0].length; j++) {

				int aliveNeighbours = aliveNeighbours(i, j);

				// Rule #1: Any dead cell with three live neighbours becomes a live cell.
				if (grid[i][j] == false && aliveNeighbours == 3) {
					newGrid[i][j] = true;

					// Rule #2: Any live cell with two or three live neighbours survives.
				} else if (grid[i][j] == true && aliveNeighbours == 2 || aliveNeighbours == 3) {
					newGrid[i][j] = true;

					// Rule #3: All other live cells die in the next generation. Similarly, all
					// other dead cells stay dead.
				} else {
					newGrid[i][j] = false;
				}

			}
		}

		grid = newGrid;

		return grid;
	}

	public int aliveNeighbours(int i, int j) {

		int counter = 0;

		int r = grid.length;
		int c = grid[0].length;

		if (grid[mod((i - 1), r)][j % c]) // North
			counter++;
		if (grid[mod((i + 1), r)][j % c]) // South
			counter++;
		if (grid[i % r][mod((j + 1), c)]) // East
			counter++;
		if (grid[i % r][mod((j - 1), c)]) // West
			counter++;
		if (grid[mod((i - 1), r)][mod((j + 1), c)]) // North-East
			counter++;
		if (grid[mod((i - 1), r)][mod((j - 1), c)]) // North-West
			counter++;
		if (grid[mod((i + 1), r)][mod((j + 1), c)]) // South-East
			counter++;
		if (grid[mod((i + 1), r)][mod((j - 1), c)]) // South-West
			counter++;

		return counter;
	}

	public int mod(int a, int b) {

		int r = a % b;
		if (r < 0)
			r += b;
		return r;
	}

	@Override
	public boolean[][] getState() {
		return grid;
	}

	@Override
	public boolean reset() {
		this.grid = new boolean[grid.length][grid[0].length];
		return true;
	}

}

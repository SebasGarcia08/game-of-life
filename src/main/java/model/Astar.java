package model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.function.BiFunction;

import org.controlsfx.control.spreadsheet.Grid;

import controller.GameUIController;
import javafx.scene.paint.Color;

public class Astar {

	public static int[] directionI = { -1, 0, 0, 1 };
	public static int[] directionJ = { 0, -1, 1, 0 };

	static class Triplet implements Comparable<Triplet> {
		private double f_score;
		private int count;
		private int[] cellIdxs;

		public Triplet(double index, int count, int[] cellIdxs) {
			this.f_score = index;
			this.count = count;
			this.cellIdxs = cellIdxs;
		}

		@Override
		public int compareTo(Triplet o) {
			if (f_score > o.f_score)
				return 1;
			else if (f_score < o.f_score)
				return -1;
			else if (count < o.count)
				return -1;
			else if (count > o.count)
				return 1;
			else
				return 0;
		}
	}

	public static boolean run(GameUIController controller, Cell start, Cell end, DISTANCE distance) {
		BiFunction<int[], int[], Double> h;
		if (distance == DISTANCE.NONE) {
			h = (p1,p2) -> 0.0;
		} else {			
			h = (distance == DISTANCE.MANHATTAN) ? Astar::L1 : Astar::L2;
		}
		int count = 0;
		Color pathColor = new Color(249 / 250.0, 168 / 250.0, 37 / 250.0, 1.0);

		boolean grid[][] = controller.getGrid();
		PriorityQueue<Triplet> openSet = new PriorityQueue<>();
		openSet.add(new Triplet(0, count, start.getCoords()));

		double[][] gScores = new double[grid.length][grid[0].length];
		double[][] fScores = new double[grid.length][grid[0].length];

		for (double[] row : gScores)
			Arrays.fill(row, Double.MAX_VALUE);

		for (double[] row : fScores)
			Arrays.fill(row, Double.MAX_VALUE);

		gScores[start.getI()][start.getJ()] = 0;
		fScores[start.getI()][start.getJ()] = h.apply(start.getCoords(), end.getCoords());
		openSet.add(new Triplet(0.0, count, start.getCoords()));

		HashMap<int[], int[]> cameFrom = new HashMap<>();

		Set<int[]> openSetHash = new HashSet<>();
		openSetHash.add(start.getCoords());

		while (!openSet.isEmpty()) {
			int[] currentIdxs = openSet.poll().cellIdxs;
			int iCurr = currentIdxs[0];
			int jCurr = currentIdxs[1];

			openSetHash.remove(currentIdxs);

			if (currentIdxs[0] == end.getI() && currentIdxs[1] == end.getJ()) {
				reconstructPath(cameFrom, end.getCoords(), controller, new Color(0, 0, 1, 1));
				return true;
			}

			if (iCurr != start.getI() && jCurr != start.getJ())
				controller.paintCell(iCurr, jCurr, new Color(1, 0, 0, 1));

			for (int[] neighbourIdxs : getNeighbours(grid, currentIdxs)) {
				int iNeighbour = neighbourIdxs[0];
				int jNeighbour = neighbourIdxs[1];

				double tempGScore = gScores[iCurr][jCurr] + 1;

				if (tempGScore < gScores[iNeighbour][jNeighbour]) {
					Cell neighbour = new Cell(iNeighbour, jNeighbour, false);
					if (neighbourIdxs[0] == end.getI() && neighbourIdxs[1] == end.getJ())
						cameFrom.put(end.getCoords(), currentIdxs);
					else
						cameFrom.put(neighbourIdxs, currentIdxs);

					gScores[iNeighbour][jNeighbour] = tempGScore;
					fScores[iNeighbour][jNeighbour] = tempGScore + h.apply(neighbour.getCoords(), end.getCoords());
					if (!openSetHash.contains(neighbourIdxs)) {
						count += 1;
						openSet.add(new Triplet(fScores[iNeighbour][jNeighbour], count, neighbourIdxs));
						openSetHash.add(neighbourIdxs);

						if (iNeighbour != end.getI() && jNeighbour != end.getJ()) {
							controller.paintCell(iNeighbour, jNeighbour, pathColor);
						}
					}
				}
			}

			if (currentIdxs[0] != start.getI() && currentIdxs[1] != start.getJ()) {
				controller.paintCell(iCurr, jCurr, new Color(1, 0, 0, 1));
			}
		}
		return false;
	}

	public static void reconstructPath(HashMap<int[], int[]> cameFrom, int[] current, GameUIController controller,
			Color pathColor) {
		while (cameFrom.containsKey(current)) {
			current = cameFrom.get(current);
			controller.paintCell(current[0], current[1], pathColor);
		}
	}

	public static List<int[]> getNeighbours(boolean[][] grid, int[] cellCoords) {
		List<int[]> neighbours = new ArrayList<>(6);
		int R = grid.length;
		int C = grid[0].length;

		int i = cellCoords[0];
		int j = cellCoords[1];

		// Explore neighbors
		for (int idx = 0; idx < 4; idx++) {
			int r = i + directionI[idx];
			int c = j + directionJ[idx];

			boolean coordsAreOutOfBounds = r < 0 || c < 0 || r >= R || c >= C;
			if (!coordsAreOutOfBounds && !grid[r][c])
				neighbours.add(new int[] { r, c });

		}
		return neighbours;
	}

	public static double L2(int[] p1, int[] p2) {
		return Math.sqrt(Math.pow(p1[0] - p2[0], 2) + Math.pow(p1[1] - p2[1], 2));
	}

	public static double L1(int[] p1, int[] p2) {
		return Math.abs(p1[0] - p2[0]) + Math.abs(p1[1] - p2[1]);
	}

}

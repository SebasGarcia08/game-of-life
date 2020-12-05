package model;

import java.util.Arrays;
import java.util.PriorityQueue;

public class Astar {
	
	static class Triplet{
		private int index;
		private int count;
		private Cell cell;
		
		public Triplet(int index, int count, Cell cell) {
			this.index = index;
			this.count = count;
			this.cell = cell;
		}
	}
	
	public static <T> void run(T[][] grid, Cell start, Cell end) {
		int count = 0;
		PriorityQueue<Triplet> openSet = new PriorityQueue<>();
		openSet.add(new Triplet(0, count, start));
		
		double[][] gScores = new double[grid.length][grid[0].length];
		double[][] fScores = new double[grid.length][grid[0].length];		
		
		Arrays.fill(gScores, Double.MAX_VALUE);
		Arrays.fill(fScores, Double.MAX_VALUE);

		gScores[start.getI()][start.getJ()] = 0;
		fScores[start.getI()][start.getJ()] = manhattanDistance(start.getCoords(), end.getCoords());
	}
	
	public static double manhattanDistance(int[] p1, int[] p2) {
		return Math.abs( p1[0] - p2[0]) + Math.abs(p1[1] - p2[1]);
	}
}

package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShortestPathFinder {
	// There are three kinds of nodes in the grid:
	// 1 normal node
	// 0 obstacle
	// 9 source and destination, only two nodes in the array should be equals 9
	private boolean[][] nodes;

	public ShortestPathFinder(boolean[][] nodes) {
		this.nodes = nodes;
	}

	public List<Cell> shortestPath(Cell start, Cell end) {
		// key node, value parent
		Map<Cell, Cell> parents = new HashMap<>();

		// traverse every node using breadth first search until reaching the destination
		List<Cell> temp = new ArrayList<>();
		temp.add(start);
		parents.put(start, null);

		Cell endFound = null;

		boolean reachDestination = false;
		while (temp.size() > 0 && !reachDestination) {
			Cell currentNode = temp.remove(0);
			List<Cell> children = getChildren(currentNode);
			for (Cell child : children) {
				// Node can only be visited once
				if (!parents.containsKey(child)) {
					parents.put(child, currentNode);
					boolean isActive = child.status();
					if (!isActive) {
						temp.add(child);
					} else if (child.getI() == end.getI() && child.getJ() == end.getJ()) {
						temp.add(child);
						reachDestination = true;
						endFound = child;
						break;
					}
				}
			}
		}

		if (endFound == null) {
			return null;
		}

		// get the shortest path
		Cell node = end;
		List<Cell> path = new ArrayList<>();
		while (node != null) {
			path.add(0, node);
			node = parents.get(node);
		}
		return path;
	}

	private List<Cell> getChildren(Cell parent) {
		List<Cell> children = new ArrayList<>();
		int x = parent.getI();
		int y = parent.getJ();
		if (x - 1 >= 0) {
			Cell child = new Cell(x - 1, y, nodes[x - 1][y]);
			children.add(child);
		}
		if (y - 1 >= 0) {
			Cell child = new Cell(x, y - 1, nodes[x][y - 1]);
			children.add(child);
		}
		if (x + 1 < nodes.length) {
			Cell child = new Cell(x + 1, y, nodes[x + 1][y]);
			children.add(child);
		}
		if (y + 1 < nodes[0].length) {
			Cell child = new Cell(x, y + 1, nodes[x][y + 1]);
			children.add(child);
		}
		return children;
	}
}
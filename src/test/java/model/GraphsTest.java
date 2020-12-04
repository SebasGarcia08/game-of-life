package model;

import static org.junit.Assert.*;

import org.junit.Test;

public class GraphsTest {
	
	private AdjListGraph<Integer> adjListGraph;
	private AdjMatrixGraph<Integer> adjMatrixGraph;
	
	public void emptySetup() {
		adjListGraph = new AdjListGraph<Integer>(4);
		adjMatrixGraph = new AdjMatrixGraph<Integer>(4);
	}
	
	public void nonEmptySetup() {
		// Adjacency List Graph
		adjListGraph = new AdjListGraph<Integer>(5);
		// Vertices
		adjListGraph.addVertex(1);
		adjListGraph.addVertex(2);
		adjListGraph.addVertex(3);
		adjListGraph.addVertex(4);
		adjListGraph.addVertex(5);
		// Edges
		adjListGraph.addEdge(3, 1, 1);
		adjListGraph.addEdge(3, 2, 1);
		adjListGraph.addEdge(4, 5, 1);
		adjListGraph.addEdge(2, 5, 1);
		
		// Adjacency Matrix Graph
		adjMatrixGraph = new AdjMatrixGraph<Integer>(5);
		// Vertices
		adjMatrixGraph.addVertex(1);
		adjMatrixGraph.addVertex(2);
		adjMatrixGraph.addVertex(3);
		adjMatrixGraph.addVertex(4);
		adjMatrixGraph.addVertex(5);
		// Edges
		adjMatrixGraph.addEdge(3, 1, 1);
		adjMatrixGraph.addEdge(3, 2, 1);
		adjMatrixGraph.addEdge(4, 5, 1);
		adjMatrixGraph.addEdge(2, 5, 1);
	}

	@Test
	public void testBFS() {
		nonEmptySetup();
		assertTrue("3 1 2 5 4 ".equals(adjListGraph.bfs(3)));
		assertTrue("3 1 2 5 4 ".equals(adjMatrixGraph.bfs(3)));
		assertTrue("2 3 5 1 4 ".equals(adjListGraph.bfs(2)));
		assertTrue("2 3 5 1 4 ".equals(adjMatrixGraph.bfs(2)));
		emptySetup();
		assertTrue("No vertices in the graph".equals(adjListGraph.bfs(5)));
		adjListGraph.addVertex(32);			adjMatrixGraph.addVertex(32);
		adjListGraph.addVertex(15);			adjMatrixGraph.addVertex(15);
		adjListGraph.addVertex(28);			adjMatrixGraph.addVertex(28);
		adjListGraph.addVertex(7);			adjMatrixGraph.addVertex(7);
		adjListGraph.addEdge(32, 15, 1);	adjMatrixGraph.addEdge(32, 15, 1);
		adjListGraph.addEdge(15, 28, 1);	adjMatrixGraph.addEdge(15, 28, 1);
		adjListGraph.addEdge(32, 7, 1);		adjMatrixGraph.addEdge(32, 7, 1);
		assertTrue("15 32 28 7 ".equals(adjListGraph.bfs(15)));
		assertTrue("15 32 28 7 ".equals(adjMatrixGraph.bfs(15)));
	}
	
	@Test
	public void testDFS() {
		nonEmptySetup();
		assertTrue("3 2 5 4 1 ".equals(adjListGraph.dfs(3)));
		assertTrue("3 2 5 4 1 ".equals(adjMatrixGraph.dfs(3)));
		assertTrue("1 3 2 5 4 ".equals(adjListGraph.dfs(1)));
		assertTrue("1 3 2 5 4 ".equals(adjMatrixGraph.dfs(1)));
		emptySetup();
		assertTrue("No vertices in the graph".equals(adjListGraph.dfs(5)));
		adjListGraph.addVertex(12);			adjMatrixGraph.addVertex(12);
		adjListGraph.addVertex(23);			adjMatrixGraph.addVertex(23);
		adjListGraph.addVertex(41);			adjMatrixGraph.addVertex(41);
		adjListGraph.addVertex(4);			adjMatrixGraph.addVertex(4);
		adjListGraph.addEdge(12, 4, 1);		adjMatrixGraph.addEdge(12, 4, 1);
		adjListGraph.addEdge(23, 41, 1);	adjMatrixGraph.addEdge(23, 41, 1);
		adjListGraph.addEdge(12, 23, 1);	adjMatrixGraph.addEdge(12, 23, 1);
		assertTrue("41 23 12 4 ".equals(adjListGraph.dfs(41)));
		assertTrue("41 23 12 4 ".equals(adjMatrixGraph.dfs(41)));
	}
	
	@Test
	public void testDijkstra() {
		nonEmptySetup();
		assertTrue(3 == adjListGraph.dijkstra(1, 5));
		assertTrue(3 == adjMatrixGraph.dijkstra(1, 5));
		assertTrue(2 == adjListGraph.dijkstra(2, 4));
		assertTrue(2 == adjMatrixGraph.dijkstra(2, 4));
		emptySetup();
		assertTrue(-1 == adjListGraph.dijkstra(1, 5));
		adjListGraph.addVertex(8);			adjMatrixGraph.addVertex(8);
		adjListGraph.addVertex(17);			adjMatrixGraph.addVertex(17);
		adjListGraph.addVertex(21);			adjMatrixGraph.addVertex(21);
		adjListGraph.addVertex(30);			adjMatrixGraph.addVertex(30);
		adjListGraph.addEdge(8, 21, 3);		adjMatrixGraph.addEdge(8, 21, 3);
		adjListGraph.addEdge(21, 30, 2);	adjMatrixGraph.addEdge(21, 30, 2);
		adjListGraph.addEdge(17, 8, 4);		adjMatrixGraph.addEdge(17, 8, 4);
		assertTrue(5 == adjListGraph.dijkstra(8, 30));
		assertTrue(5 == adjMatrixGraph.dijkstra(8, 30));
	}
}

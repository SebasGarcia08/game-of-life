package model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Stack;

public class AdjListGraph<K> {

	// Small epsilon value to comparing double values.
	private static final double EPS = 1e-6;

	// An edge class to represent a directed edge
	// between two nodes with a certain cost.
	public static class Edge {
		double cost;
		int from, to;

		public Edge(int from, int to, double cost) {
			this.from = from;
			this.to = to;
			this.cost = cost;
		}
	}

	// Node class
	public static class Node {
		int id;
		double value;

		public Node(int id, double value) {
			this.id = id;
			this.value = value;
		}
	}

	private int n;
	private double[] dist;
	private Integer[] prev;
	private List<List<Edge>> graph;
	private List<K> vertices;

	private Comparator<Node> comparator =
			new Comparator<Node>() {
		@Override
		public int compare(Node node1, Node node2) {
			if (Math.abs(node1.value - node2.value) < EPS) return 0;
			return (node1.value - node2.value) > 0 ? +1 : -1;
		}
	};

	/**
	 * Constructor for an empty graph
	 *
	 * @param n - The number of nodes in the graph.
	 */
	public AdjListGraph(int n) {
		this.n = n;
		graph = new ArrayList<>(n);
		vertices = new ArrayList<K>();
	}

	/**
	 * Adds a undirected edge to the graph.
	 *
	 * @param v - The first node in the undirected edge.
	 * @param w - The second node in the undirected edge.
	 * @param cost - The cost of the edge.
	 */
	public void addEdge(K v, K w, int cost) {
		int idxV = vertices.indexOf(v);
		int idxW = vertices.indexOf(w);
		graph.get(idxV).add(new Edge(idxV, idxW, cost));
		graph.get(idxW).add(new Edge(idxW, idxV, cost));
	}

	// Run Dijkstra's algorithm on a directed graph to find the shortest path
	// from a starting node to an ending node. If there is no path between the
	// starting node and the destination node the returned value is set to be
	// Double.POSITIVE_INFINITY.
	public double dijkstra(K v_start, K v_end) {
		
		int start = vertices.indexOf(v_start);
		int end = vertices.indexOf(v_end);
		
		// Maintain an array of the minimum distance to each node
		dist = new double[n];
		Arrays.fill(dist, Double.POSITIVE_INFINITY);
		dist[start] = 0;

		// Keep a priority queue of the next most promising node to visit.
		PriorityQueue<Node> pq = new PriorityQueue<>(2 * n, comparator);
		pq.offer(new Node(start, 0));

		// Array used to track which nodes have already been visited.
		boolean[] visited = new boolean[n];
		prev = new Integer[n];

		while (!pq.isEmpty()) {
			Node node = pq.poll();
			visited[node.id] = true;

			// We already found a better path before we got to
			// processing this node so we can ignore it.
			if (dist[node.id] < node.value) continue;

			List<Edge> edges = graph.get(node.id);
			for (int i = 0; i < edges.size(); i++) {
				Edge edge = edges.get(i);

				// You cannot get a shorter path by revisiting
				// a node you have already visited before.
				if (visited[edge.to]) continue;

				// Relax edge by updating minimum cost if applicable.
				double newDist = dist[edge.from] + edge.cost;
				if (newDist < dist[edge.to]) {
					prev[edge.to] = edge.from;
					dist[edge.to] = newDist;
					pq.offer(new Node(edge.to, dist[edge.to]));
				}
			}
			// Once we've visited all the nodes spanning from the end
			// node we know we can return the minimum distance value to
			// the end node because it cannot get any better after this point.
			if (node.id == end) return dist[end];
		}
		// End node is unreachable
		return Double.POSITIVE_INFINITY;
	}

	public void addVertex(K vertex) {
		graph.add(new ArrayList<>());
		vertices.add(vertex);
	}
	
	public void getAdjacencyList() {
    	System.out.println("The Linked List Representation of the graph is: ");

    	for (int i = 0; i < graph.size(); i++) {
    		System.out.print(vertices.get(i) + ": ");
    		List<Edge> edgeList = graph.get(i);
    		for (int j = 1;; j++) {
    			if (j != edgeList.size())
    				System.out.print(vertices.get(edgeList.get(j - 1).to) + " ");
    			else {
    				System.out.print(vertices.get(edgeList.get(j - 1).to));
    				break;
    			}
    		}
    		System.out.println();
    	}
    }
	
	public String bfs(K v_start) { 
		String path = "";
		int start = vertices.indexOf(v_start);
        // Mark all the vertices as not visited(By default 
        // set as false) 
        boolean visited[] = new boolean[n]; 
  
        // Create a queue for BFS 
        Queue<Integer> queue = new LinkedList<Integer>(); 
  
        // Mark the current node as visited and enqueue it 
        visited[start]=true; 
        queue.add(start); 
  
        while (queue.size() != 0) { 
            // Dequeue a vertex from queue and print it 
            start = queue.poll(); 
            path += vertices.get(start) + " "; 
  
            // Get all adjacent vertices of the dequeued vertex s 
            // If a adjacent has not been visited, then mark it 
            // visited and enqueue it 
            Iterator<Edge> i = graph.get(start).listIterator(); 
            while (i.hasNext()) { 
                Edge n = i.next(); 
                if (!visited[n.to]) { 
                    visited[n.to] = true; 
                    queue.add(n.to); 
                } 
            } 
        }
        return path;
    }
	
	public String dfs(K v_start) {
		String path = "";
		int start = vertices.indexOf(v_start);
		
		Stack<Integer> stack = new Stack<Integer>();
		boolean visited[] = new boolean[n]; 
		
		visited[start] = true;
		stack.add(start);
		
		while (!stack.isEmpty()) {	
			Integer element = stack.pop();
			path += vertices.get(element) + " ";
			
			if(!visited[element]) {
				visited[element] = true;
			}
			
			List<Edge> neighbours = graph.get(element);
			for (int i = 0; i < neighbours.size(); i++) {
				Integer n = neighbours.get(i).to;
				if(n!=null && !visited[n]) {
					stack.add(n);
				}
			}
		}
		return path;
	}
	
	// test client
	public static void main(String[] args) {
//		Graph_NodeEdge<Integer> g = new Graph_NodeEdge<Integer>(3);
//		g.addVertex(1);
//		g.addVertex(2);
//		g.addVertex(3);
//		g.addEdge(1, 2, 1);
//		g.addEdge(2, 3, 1);
		
		AdjListGraph<String> g = new AdjListGraph<String>(4);
		g.addVertex("A");
		g.addVertex("B");
		g.addVertex("C");
		g.addEdge("A", "B", 1);
		g.addEdge("B", "C", 1);
		
		g.getAdjacencyList();
		
		System.out.println("BFS: " + g.bfs("A"));
		System.out.println("DFS: " + g.dfs("A"));
		
		//System.out.println(g.dijkstra(1, 3));
		System.out.println(g.dijkstra("A", "C"));
	}
}
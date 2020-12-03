package model;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Stack;

public class AdjMatrixGraph<K> {
    private static final String NEWLINE = System.getProperty("line.separator");
    // Number of Vertices
    private int V;
    // Number of Edges
    private int E;
    // Adjacency Matrix
    private int[][] adjacencyMatrix;
    private List<K> indexToVertex;
    
	private double[] dist;
	private Integer[] prev;
	private Comparator<Integer> comparator =
			new Comparator<Integer>() {
		@Override
		public int compare(Integer node1, Integer node2) {
			if (Math.abs(node1 - node2) < (1e-6)) return 0;
			return (node1 - node2) > 0 ? +1 : -1;
		}
	};
    
    // empty graph with V vertices
    public AdjMatrixGraph(int numVertices) {
        if (numVertices < 0) throw new IllegalArgumentException("Too few vertices");
        this.V = numVertices;
        this.E = 0;
        this.adjacencyMatrix = new int[V][V];
        indexToVertex = new ArrayList<K>();
    }
    
    public boolean addVertex(K vertex) {
    	boolean added = false;
    	for (int i=0; i<adjacencyMatrix.length && !added; i++) {
    		if (!indexToVertex.contains(vertex)) {
    			indexToVertex.add(vertex);
    			added = true;
    		}
    	}
    	return added;
    }

    /**
     * Adds undirected weighted edge between v and w
     * @param v
     * @param w
     */
    public void addEdge(K v, K w, int cost) {
    	int idxV = indexToVertex.indexOf(v);
    	int idxW = indexToVertex.indexOf(w);
    	
    	if (idxV == -1) throw new NoSuchElementException("Vertex " + v + " not present in the graph" );
    	if (idxW == -1) throw new NoSuchElementException("Vertex " + w + " not present in the graph" );
    	
        if (adjacencyMatrix[idxV][idxW] == 0) E++;
        adjacencyMatrix[idxV][idxW] = cost;
        adjacencyMatrix[idxW][idxV] = cost;
    }
    
    /**
     * Adds undirected unweighted edge between v and w
     * @param v
     * @param w
     */
    public void addEdge(K v, K w) {
    	int idxV = indexToVertex.indexOf(v);
    	int idxW = indexToVertex.indexOf(w);
    	
    	if (idxV == -1) throw new NoSuchElementException("Vertex " + v + " not present in the graph" );
    	if (idxW == -1) throw new NoSuchElementException("Vertex " + w + " not present in the graph" );
    	
        if (adjacencyMatrix[idxV][idxW] == 0) E++;
        adjacencyMatrix[idxV][idxW] = 1;
        adjacencyMatrix[idxW][idxV] = 1;
    }

    // does the graph contain the edge v-w?
    public boolean contains(int v, int w) {
        return (adjacencyMatrix[v][w]!=0);
    }

    // return list of neighbors of v
    public List<K> adj(int v) {
        //return new AdjIterator(v);
    	List<K> neighboors = new ArrayList<K>();
        for (int i=0; i<adjacencyMatrix[v].length; i++) {
        	if (adjacencyMatrix[v][i]!=0) { neighboors.add(indexToVertex.get(i)); }
        }
        return neighboors;
    }

    // string representation of Graph - takes quadratic time
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append("Number of vertices: " + V + " - Number of edges: " + E + NEWLINE);
        for (int v = 0; v < indexToVertex.size(); v++) {
            s.append(indexToVertex.get(v) + ": ");
            for (K w : adj(v)) {
                s.append(w + " ");
            }
            s.append(NEWLINE);
        }
        return s.toString();
    }

    public String floydwarshall() {
    	String transitiveClosure = "The Transitive Closure of the Graph\n";
    	
		int distancematrix[][] = new int[indexToVertex.size() + 1][indexToVertex.size() + 1];
		
        for (int source = 1; source <= indexToVertex.size(); source++) {
            for (int destination = 1; destination <= indexToVertex.size(); destination++) {
                distancematrix[source][destination] = adjacencyMatrix[source-1][destination-1];
            }
        }
 
        for (int intermediate = 1; intermediate <= indexToVertex.size(); intermediate++) {
            for (int source = 1; source <= indexToVertex.size(); source++) {
                for (int destination = 1; destination <= indexToVertex.size(); destination++) {
                    if (distancematrix[source][intermediate] + distancematrix[intermediate][destination]
                         < distancematrix[source][destination])
                        distancematrix[source][destination] = distancematrix[source][intermediate] 
                            + distancematrix[intermediate][destination];
                }
            }
        }
 
        for (int source = 0; source < indexToVertex.size(); source++)
        	transitiveClosure += "\t" + indexToVertex.get(source);
 
        transitiveClosure += "\n";
        for (int source = 1; source <= indexToVertex.size(); source++) {
            transitiveClosure += indexToVertex.get(source-1) + "\t";
            for (int destination = 1; destination <= indexToVertex.size(); destination++) {
            	transitiveClosure += distancematrix[source][destination] + "\t";
            }
            transitiveClosure += "\n";
        }
        return transitiveClosure;
    }
    
    public String bfs(K v_start) { 
		String path = "";
		int start = indexToVertex.indexOf(v_start);
        // Mark all the vertices as not visited(By default 
        // set as false) 
        boolean visited[] = new boolean[V]; 
  
        // Create a queue for BFS 
        Queue<Integer> queue = new LinkedList<Integer>(); 
  
        // Mark the current node as visited and enqueue it 
        visited[start]=true; 
        queue.add(start); 
  
        while (queue.size() != 0) { 
            // Dequeue a vertex from queue and print it 
            start = queue.poll(); 
            path += indexToVertex.get(start) + " "; 
  
            // Get all adjacent vertices of the dequeued vertex s 
            // If a adjacent has not been visited, then mark it 
            // visited and enqueue it 
            Iterator<K> i = adj(start).listIterator(); 
            while (i.hasNext()) { 
                K n = i.next(); 
                if (!visited[indexToVertex.indexOf(n)]) { 
                    visited[indexToVertex.indexOf(n)] = true; 
                    queue.add(indexToVertex.indexOf(n)); 
                } 
            } 
        }
        return path;
    }

    public String dfs(K v_start) {
		String path = "";
		int start = indexToVertex.indexOf(v_start);
		
		Stack<Integer> stack = new Stack<Integer>();
		boolean visited[] = new boolean[V]; 
		
		visited[start] = true;
		stack.add(start);
		
		while (!stack.isEmpty()) {	
			Integer element = stack.pop();
			path += indexToVertex.get(element) + " ";
			
			if(!visited[element]) {
				visited[element] = true;
			}
			
			List<K> neighbours = adj(element);
			for (int i = 0; i < neighbours.size(); i++) {
				Integer n = indexToVertex.indexOf(neighbours.get(i));
				if(n!=null && !visited[n]) {
					stack.add(n);
				}
			}
		}
		return path;
	}
    
    public double dijkstra(K v_start, K v_end) {

    	int start = indexToVertex.indexOf(v_start);
    	int end = indexToVertex.indexOf(v_end);

    	// Maintain an array of the minimum distance to each node
    	dist = new double[V];
    	Arrays.fill(dist, Double.POSITIVE_INFINITY);
    	dist[start] = 0;

    	// Keep a priority queue of the next most promising node to visit.
    	PriorityQueue<Integer> pq = new PriorityQueue<>(2 * V, comparator);
    	pq.offer(start);

    	// Array used to track which nodes have already been visited.
    	boolean[] visited = new boolean[V];
    	prev = new Integer[V];

    	while (!pq.isEmpty()) {
    		Integer node = pq.poll();
    		visited[node] = true;

    		List<K> edges = adj(node);
    		for (int i = 0; i < edges.size(); i++) {
    			Integer edge = indexToVertex.indexOf(edges.get(i));

    			// You cannot get a shorter path by revisiting
    			// a node you have already visited before.
    			if (visited[edge]) continue;

    			// Relax edge by updating minimum cost if applicable.
    			double newDist = dist[node] + 1;
    			if (newDist < dist[edge]) {
    				prev[edge] = node;
    				dist[edge] = newDist;
    				pq.offer(edge);
    			}
    		}
    		// Once we've visited all the nodes spanning from the end
    		// node we know we can return the minimum distance value to
    		// the end node because it cannot get any better after this point.
    		if (node == end) return dist[end];
    	}
    	// End node is unreachable
    	return Double.POSITIVE_INFINITY;
    }
    
    public int minKey(int key[], Boolean mstSet[]) { 
        // Initialize min value 
        int min = Integer.MAX_VALUE, min_index = -1; 
  
        for (int v = 0; v < V; v++) {
            if (mstSet[v] == false && key[v] < min) { 
                min = key[v]; 
                min_index = v; 
            } 
        }
        return min_index; 
    } 
  
    // A utility function to print the constructed MST stored in 
    // parent[] 
    public void printMST(int parent[]) { 
        System.out.println("Edge \tWeight"); 
        for (int i = 1; i < indexToVertex.size(); i++) {
        	int weight = adjacencyMatrix[i][parent[i]];
            System.out.println(indexToVertex.get(parent[i]) + " - " + indexToVertex.get(i) + "\t" + weight);
        }
    } 
  
    // Function to construct and print MST for a graph represented 
    // using adjacency matrix representation 
    public void primMST() { 
        // Array to store constructed MST 
        int parent[] = new int[V]; 
  
        // Key values used to pick minimum weight edge in cut 
        int key[] = new int[V]; 
  
        // To represent set of vertices included in MST 
        Boolean mstSet[] = new Boolean[V]; 
  
        // Initialize all keys as INFINITE 
        for (int i = 0; i < V; i++) { 
            key[i] = Integer.MAX_VALUE; 
            mstSet[i] = false; 
        } 
  
        // Always include first 1st vertex in MST. 
        key[0] = 0; // Make key 0 so that this vertex is 
        // picked as first vertex 
        parent[0] = -1; // First node is always root of MST 
  
        // The MST will have V vertices 
        for (int count = 0; count < V - 1; count++) { 
            // Pick thd minimum key vertex from the set of vertices 
            // not yet included in MST 
            int u = minKey(key, mstSet); 
  
            // Add the picked vertex to the MST Set 
            mstSet[u] = true; 
  
            // Update key value and parent index of the adjacent 
            // vertices of the picked vertex. Consider only those 
            // vertices which are not yet included in MST 
            for (int v = 0; v < V; v++) {
  
                // graph[u][v] is non zero only for adjacent vertices of m 
                // mstSet[v] is false for vertices not yet included in MST 
                // Update the key only if graph[u][v] is smaller than key[v] 
            	int graph_uv = adjacencyMatrix[u][v];
                if (graph_uv != 0 && mstSet[v] == false && graph_uv < key[v]) { 
                    parent[v] = u; 
                    key[v] = graph_uv; 
                }
            }
        } 
  
        // print the constructed MST 
        printMST(parent); 
    }
    
    public int find(int i, int[] parent) { 
    	while (parent[i] != i) 
    		i = parent[i]; 
    	return i; 
    } 

    // Does union of i and j. It returns 
    // false if i and j are already in same set. 
    public void union1(int i, int j, int parent[]) { 
    	int a = find(i, parent); 
    	int b = find(j, parent); 
    	parent[a] = b;
    } 

    // Finds MST using Kruskal's algorithm 
    public void kruskalMST() { 
    	int mincost = 0; // Cost of min MST.
    	int[] parent = new int[V]; 
    	int INF = Integer.MAX_VALUE; 

    	// Initialize sets of disjoint sets. 
    	for (int i = 0; i < V; i++) 
    		parent[i] = i; 

    	// Include minimum weight edges one by one 
    	int edge_count = 0; 
    	while (edge_count < V - 1) {
    		int min = INF, a = -1, b = -1; 
    		for (int i = 0; i < V; i++) {
    			for (int j = 0; j < V; j++) {
    				if (find(i, parent) != find(j, parent) && adjacencyMatrix[i][j] < min) {
    					min = adjacencyMatrix[i][j]; 
    					a = i;
    					b = j;
    				}
    			}
    		}

    		union1(a, b, parent); 
    		System.out.printf("Edge %d:(%d, %d) cost:%d \n", 
    				edge_count++, a, b, min); 
    		mincost += min;
    	} 
    	System.out.printf("\nMinimum cost= %d \n", mincost); 
    }
    
    // test client
    public static void main(String[] args) {
        AdjMatrixGraph<String> g = new AdjMatrixGraph<String>(3);
        
        g.addVertex("A");
        g.addVertex("B");
        g.addVertex("C");
        
        g.addEdge("A", "B", 3);
        g.addEdge("B", "C", 2);
        
        System.out.println("BFS: " + g.bfs("A"));
        System.out.println("DFS: " + g.dfs("A"));
        
        System.out.println(g.dijkstra("A", "C"));
        
        System.out.println(g.floydwarshall());
        
        g.primMST();
        g.kruskalMST();
        
        System.out.println(g);
    }

}

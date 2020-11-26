package model;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
 
public class Graph<K> {
    private Map<K, List<K>> adjacencyList;
    
    private ArrayList<K> vertices;
 
    public Graph() {
        adjacencyList = new HashMap<K, List<K>>();
        vertices = new ArrayList<K>();
    }
    
    public void addVertex(K vertex) {
    	adjacencyList.put(vertex, new LinkedList<K>());
    	vertices.add(vertex);
    }
 
    public void setEdge(K to, K from) {
        if (!adjacencyList.containsKey(to) || !adjacencyList.containsKey(from))
            System.out.println("The vertices does not exists");
 
        List<K> sls = adjacencyList.get(to);
        sls.add(from);
        List<K> dls = adjacencyList.get(from);
        dls.add(to);
    }
 
    public List<K> getEdge(K to) {
        if (!adjacencyList.containsKey(to)) {
            System.out.println("The vertices does not exists");
            return null;
        }
        return adjacencyList.get(to);
    }
    
    public int getSize() {
    	return adjacencyList.size();
    }
    
    public void getAdjacencyList() {
    	System.out.println("The Linked List Representation of the graph is: ");

    	for (int i = 0; i < adjacencyList.size(); i++) {
    		System.out.print(vertices.get(i) + "->");
    		List<K> edgeList = getEdge(vertices.get(i));
    		for (int j = 1;; j++) {
    			if (j != edgeList.size())
    				System.out.print(edgeList.get(j - 1) + " -> ");
    			else {
    				System.out.print(edgeList.get(j - 1));
    				break;
    			}
    		}
    		System.out.println();
    	}
    }
}

package profiling.constraint.graph;

import java.util.List;
import java.util.Map.Entry;
import java.util.Vector;

import org.jgrapht.DirectedGraph;
import org.jgrapht.alg.DijkstraShortestPath;
import org.jgrapht.alg.StrongConnectivityInspector;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DirectedSubgraph;

/*
 * JGraphTUtils is a singleton, 
 * we need only one instance of this class anyways.
 */
public class JGraphTUtils {
	
	private static JGraphTUtils uniqueInstance;

	private JGraphTUtils() {
		super();
		uniqueInstance = null;
	}
	
	public static synchronized JGraphTUtils getInstance() {
		if (uniqueInstance == null) {
			uniqueInstance = new JGraphTUtils();
		}
		return uniqueInstance;
	}
	
	public DirectedGraph<String, DefaultEdge> transformIntoDirectedGraph(CFG cfg) {
		// constructs a directed graph with the specified vertices and edges
	    DirectedGraph<String, DefaultEdge> directedGraph =
	        new DefaultDirectedGraph<String, DefaultEdge>
	        (DefaultEdge.class);	    	    
	    
	    for (Entry<String, Node> node : cfg.getNodes().entrySet()) {
	    	directedGraph.addVertex(node.getKey());
	    }
	    
	    for (Entry<String, Edge> edge :cfg.getEdges().entrySet()) {
	    	directedGraph.addEdge(edge.getValue().getSource().getId(), 
	    			edge.getValue().getTarget().getId());
	    }
		return directedGraph;	
	}
	
	public String computeStronglyConnectedNodes(DirectedGraph<String, DefaultEdge> directedGraph) {
        // computes all the strongly connected components of the directed graph
        StrongConnectivityInspector<String, DefaultEdge> sci =
            new StrongConnectivityInspector<String, DefaultEdge>(directedGraph);
        List<DirectedSubgraph<String, DefaultEdge>> stronglyConnectedSubgraphs = sci.stronglyConnectedSubgraphs();

        // builds the String containing the strongly connected components
        StringBuilder sce = new StringBuilder();
        for (int i = 0; i < stronglyConnectedSubgraphs.size(); i++) {
        	sce.append(stronglyConnectedSubgraphs.get(i) + "\n");
        }

        return sce.toString();
	}
	
	public String calculateShortestPath(String source, String target, DirectedGraph<String, DefaultEdge> directedGraph) {
        // Prints the shortest path from vertex i to vertex c. This certainly
        // exists for our particular directed graph.
		StringBuilder shortestPath = new StringBuilder();
        List<DefaultEdge> path =
            DijkstraShortestPath.findPathBetween(directedGraph, source, target);
        shortestPath.append(path);
        
        return shortestPath.toString();
	}
	
	public Vector<String> getShortestPath(String source, String target, DirectedGraph<String, DefaultEdge> directedGraph) {
        // Prints the shortest path from vertex i to vertex c. This certainly
        // exists for our particular directed graph.
		StringBuilder shortestPath = new StringBuilder();
        List<DefaultEdge> path =
            DijkstraShortestPath.findPathBetween(directedGraph, source, target);
        shortestPath.append(path);
        
        Vector<String> strPath = new Vector<String>();
        //System.out.println("Path: " + path);
        if (path!=null && path.size()>0){        	
        	int cont = 0;
            for (DefaultEdge de: path){
            	String strEdge = de.toString();
            	strEdge = strEdge.replace("(", "");
            	strEdge = strEdge.replace(")", "");
            	strEdge = strEdge.replace(" ", "");
            	String[] pieces = strEdge.split(":");
            	if (cont==0)
            		strPath.add(pieces[0]);
            	strPath.add(pieces[1]);
            	cont++;
            }
            return strPath;
        }
        
        else 
        	return null;        
	}
	

}

package profiling.constraint.graph;

import java.io.File;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Set;

import maintenance.Maintenance;

import org.jgrapht.DirectedGraph;
import org.jgrapht.graph.DefaultEdge;

public class GraphUtils {

	/**
	 * @param args
	 */
	public static String produceGraphVizOutput(CFG cfg)
	{
		StringBuilder toPrint = new StringBuilder("");
		
		toPrint.append("digraph G { \n");
		for(Entry<String, Edge> e : cfg.getEdges().entrySet())
			toPrint.append(toStringEdge( e.getValue() )+";\n");
		
		toPrint.append("}");
		
		return toPrint.toString();
	}
	
	public static void produceGraphVizOutputToFile(CFG cfg, String pathToFile)
		throws Exception
	{
		String toPrint = produceGraphVizOutput(cfg);
		File file = new File(pathToFile);
		FileWriter fw = new FileWriter(file);
		fw.write(toPrint);
		fw.close();
	}
	
	private static String toStringEdge(Edge e)
	{
		return e.getSource().getId() + " -> " + e.getTarget().getId(); 
	}
	
	public static void checkValidGraph(CFG cfg)
		throws Exception
	{
		//only one entry node
		int entryNodes = getNumberOfEntryNodes(cfg);
		if(entryNodes != 1)
			throw new Exception("Invalid CFG; there are " + entryNodes + " entry nodes.");
		
		//one or more exit nodes
		int exitNodes = getNumberOfExitNodes(cfg);
		
		Maintenance.gambiarra("Ignorando exceção de tem mais de não ter nó de saída - métodos com loop infinito");
		
		//if(exitNodes < 1)
			//throw new Exception("Invalid CFG; there is no exit node.");
		
		checkNodesReachableFromEntryNode(cfg);

		checkNodesThatReachesExitNodes(cfg);
		
		//graph is planar
		//TODO
	}
	
	public static void checkNodesThatReachesExitNodes(CFG cfg)
		throws Exception
	{
		JGraphTUtils jgraphtutils = JGraphTUtils.getInstance();
		DirectedGraph<String, DefaultEdge> directedGraph = jgraphtutils.transformIntoDirectedGraph(cfg);
		
		Set<String> exitNodes = new HashSet<String>();	
		for(String node : directedGraph.vertexSet())
			if(directedGraph.outDegreeOf(node) == 0) 	//exit node
				exitNodes.add(node);
		
		for(Node tNode : cfg.getNodes().values())
		{
			String consideredNode = tNode.getId();
			
			boolean reachedExitNode = false;
			for(String exitNode : exitNodes)
			{
				String path = jgraphtutils.calculateShortestPath(consideredNode, exitNode, directedGraph);
				if(! path.equals("null")) 
				{
					reachedExitNode = true;
					break;
				}
			}
						
			
			if(! reachedExitNode)
				throw new Exception("Node "+consideredNode+" does not reach an exit node.");
		}
	}

	public static void checkNodesReachableFromEntryNode(CFG cfg)
		throws Exception
	{
		JGraphTUtils jgraphtutils = JGraphTUtils.getInstance();
		DirectedGraph<String, DefaultEdge> directedGraph = jgraphtutils.transformIntoDirectedGraph(cfg);
		String entryNode = cfg.getRoot().getId();
		
		for(Node tNode : cfg.getNodes().values())
		{
			String toReachNode = tNode.getId();
			String path = jgraphtutils.calculateShortestPath(entryNode, toReachNode, directedGraph);
			if(path.equals("null")) 
			{
				throw new Exception("There is no path from entry node to node " + toReachNode);
			}
		}
	}
	
	public static int getNumberOfEntryNodes(CFG cfg)
	{			
		int numberOfEntryNodes = 0;
		
		JGraphTUtils jgraphtutils = JGraphTUtils.getInstance();
		DirectedGraph<String, DefaultEdge> directedGraph = jgraphtutils.transformIntoDirectedGraph(cfg);
		
		for(String node : directedGraph.vertexSet()){
			if(directedGraph.inDegreeOf(node) == 0){ 	//exit node{
				if (cfg.getMethodName().contains("parseScope"))
					System.out.println("Entry node: "+ node);
				numberOfEntryNodes++;
			}
		}
		
		if (numberOfEntryNodes==0){
			if (directedGraph.vertexSet().contains("0"))
				numberOfEntryNodes=1;
		}
		
		return numberOfEntryNodes;
	}
	
	public static int getNumberOfExitNodes(CFG cfg)
	{
		int numberOfExitNodes = 0;
		JGraphTUtils jgraphtutils = JGraphTUtils.getInstance();
		DirectedGraph<String, DefaultEdge> directedGraph = jgraphtutils.transformIntoDirectedGraph(cfg);
		
		for(String node : directedGraph.vertexSet())
			if(directedGraph.outDegreeOf(node) == 0) 	//exit node
				numberOfExitNodes++;
				
		return numberOfExitNodes;
	}
}

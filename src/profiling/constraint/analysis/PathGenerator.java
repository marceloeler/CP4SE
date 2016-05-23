package profiling.constraint.analysis;

import java.awt.List;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;
import java.util.Stack;
import java.util.Vector;

import org.jgrapht.DirectedGraph;
import org.jgrapht.graph.DefaultEdge;

import maintenance.Maintenance;
import profiling.constraint.graph.CFG;
import profiling.constraint.graph.Edge;
import profiling.constraint.graph.JGraphTUtils;
import profiling.constraint.graph.Node;


public class PathGenerator {
	
	private Vector<String> loops;
	private Vector<String> ends;
	private Vector<String> currentPath;
	private boolean hasLoop;
	private int numberOfLoops;
	private Hashtable<String,Vector<Vector<String>>> subPaths;
	private Vector<String> hashPaths;
	private static  Vector<Vector<String>> allPathsStr;
	private CFG cfg;
	private int cont;		
	
	private int loopCount;
	private int loopDepth;
	
	
	public CFG expandCFG(CFG cfg){
		
		Hashtable<String,String> modifiers = new Hashtable<String,String>();
		
		CFG expandedCFG = new CFG();		
		Queue<Node> queue = new LinkedList<Node>();
		
		for (Node nd: cfg.getNodes().values())
			nd.setVisited(false);
		
		for (Edge edge: cfg.getEdges().values())
			edge.setVisited(false);
		
		
		Node root = new Node(cfg.getRoot().getId());
		expandedCFG.addNode(root);
		expandedCFG.setRoot(root);
		
		cfg.getRoot().setVisited(true);
		queue.add(cfg.getRoot());
		
		while (!queue.isEmpty()){
			Node v = queue.poll();			
			Node cpV = expandedCFG.getNode(v.getId());
			//System.out.println("Expand "+v.getId());
			for (Node w: v.getNeighbors()){
				String nid = w.getId();
				if (expandedCFG.getNode(nid)!=null)
				{
					String modifier = "'";
					if (modifiers.get(nid)!=null)					
						modifier+=modifiers.get(nid);
					modifiers.put(nid,modifier);
					nid+=modifier;
					
				}
				Node cpW = new Node(nid);				
				
				expandedCFG.addNode(cpW);
				cpV.addNeighbor(cpW);					
				expandedCFG.addEdge(cpV, cpW);
				if (w.isVisited()==false){
					queue.add(w);
					w.setVisited(true);
				}
				
			}		
			//v.setVisited(true);
		}			
		
		return expandedCFG;
	}
	/*
	public CFG oldExpandCFG(CFG cfg){
		CFG expandedCFG = new CFG();		
		Queue<Node> queue = new LinkedList<Node>();
		
		for (Node nd: cfg.getNodes().values())
			nd.setVisited(false);
		
		for (Edge edge: cfg.getEdges().values())
			edge.setVisited(false);
		
		
		Node root = new Node(cfg.getRoot().getId());
		expandedCFG.addNode(root);
		expandedCFG.setRoot(root);
		
		cfg.getRoot().setVisited(true);
		queue.add(cfg.getRoot());
		
		while (!queue.isEmpty()){
			Node v = queue.poll(); //or peek
			//v.setVisited(true);
			Node cpV = expandedCFG.getNode(v.getId());
			
			for (Node w: v.getNeighbors()){			
				Node cpW = new Node(w.getId());																
				if (w.isVisited()==false){															
					expandedCFG.addNode(cpW);
					cpV.addNeighbor(cpW);					
					expandedCFG.addEdge(cpV, cpW);									
					w.setVisited(true);
					queue.add(w);
				}								
			}					
		}			
		
		return expandedCFG;
	}
	*/
	public Path convertStrPathToPath(CFG cfg, Vector<String> strPath){
		Path path = new Path();
		for (int i=0; i<strPath.size(); i++)
			path.addNode(cfg.getNode(strPath.get(i)));
		return path;
	}
	
	public Vector<Path> generateAllDistinctPaths(CFG cfg){
		//System.out.println("generateAllDistinctPaths");
		Vector<Path> allPaths = new Vector<Path>();
		
		if (cfg.getEdges().size()>0){
			CFG expandedCFG = this.expandCFG(cfg);
			
			//System.out.println("Expanded cfg:");
			//System.out.println(expandedCFG);
			
			JGraphTUtils jgraphtutils = JGraphTUtils.getInstance();
			DirectedGraph<String, DefaultEdge> directedGraph = jgraphtutils.transformIntoDirectedGraph(cfg);
			DirectedGraph<String, DefaultEdge> expandedDirectedGraph = jgraphtutils.transformIntoDirectedGraph(expandedCFG);
			
			
			
			Vector<String> exits = new Vector<String>();
			Vector<String> leaves = new Vector<String>();
			
			for (Node node: expandedCFG.getNodes().values()){
				if (node.getNeighbors().size()==0){					
					leaves.add(node.getId());
				}
			}
			
			for (Node node: cfg.getNodes().values()){
				if (node.hasReturn())
				{
					exits.add(node.getId());					
				}
			}
			
			Node root = expandedCFG.getRoot();				
			
			for (String node: leaves){							
				//elimina os ' do identificador do nó usado para expandir o grafo
				//node = node.replace("'", "");
				Vector<String> strPath = jgraphtutils.getShortestPath(root.getId(), node, expandedDirectedGraph);
				String lastNode = strPath.lastElement();
				//elimina os ' do identificador do nó usado para expandir o grafo
				lastNode = lastNode.replace("'","");
				
				if (!cfg.getNode(lastNode).hasReturn()) //se não é nó de saída, então é folha sem ser exit node
				{
					//precisa achar o menor caminho desta folha até um nó de saída
					//tentar achar um caminho para cada exit node
					int i=0;
					boolean achou=false;
					while (i<exits.size() && !achou){
						String exitNode = exits.get(i);
						//elimina os ' do identificador do nó usado para expandir o grafo
						exitNode = exitNode.replace("'", "");
						
						Vector complementaryPath  = jgraphtutils.getShortestPath(lastNode, exitNode, directedGraph);
						if (complementaryPath!=null){ //se existe o caminho
							achou=true;
							strPath.remove(strPath.size()-1);
							strPath.addAll(complementaryPath);														
							//elimina os ' dos identificadores dos nós do caminho																					
						}
						i++;
					}
				}				
				for (int j=0; j<strPath.size(); j++)
				{
					String st = strPath.get(j);
					st=st.replace("'", "");
					strPath.set(j,st);
				}
				//System.out.println("Path: "+cfg.getMethodName());
				//System.out.println(strPath);
				allPaths.add(this.convertStrPathToPath(cfg, strPath));			
			}		
		}
		else
		{
			Path path = new Path();
			path.addNode(cfg.getRoot());
			allPaths.add(path);
		}
				
		this.calculateLoopCount(cfg, allPaths);
		
		cfg.setLoops(this.loopCount);
		cfg.setLoopDepth(this.loopDepth);
				
		//System.out.println("Loop count: " + this.loopCount);
		//System.out.println("Loop depth: " + this.loopDepth);
		return allPaths;
	}
	
	public void calculateLoopCount(CFG cfg, Vector<Path> allPaths){	
	
		loopCount = 0;
		Vector<String> loopNodes = new Vector<String>();				
		Vector<String> loopExitNodes = new Vector<String>();
		
		for (Node node: cfg.getNodes().values()){
			if (node.isLoop()){
				loopNodes.add(node.getId());
				if (node.isInfiniteLoop()){ //nao tem noh de saida
					loopExitNodes.add(node.getId());
				}
				else
				{
					Vector<Node> neighbors = node.getNeighbors();
					for (Node neighbor: neighbors){
						Edge edge = cfg.getEdge(node, neighbor);
						if (!edge.isFalseBranch()){
							if (!neighbor.isException()&&!neighbor.hasReturn()&&!neighbor.isExceptionHandle()){
								loopExitNodes.add(neighbor.getId());
								if (cfg.getMethodName().equals("getNextToken")){
									//System.out.println("pair: " + edge.getStrEdge());
								}
							}
						}
					}
				}
			}
		}
		
		/*
		if (cfg.getMethodName().equals("getNextToken")){
		//	System.out.println("CFG: ");
		//	System.out.println(cfg);
		//	System.out.println();
			System.out.print("loop nodes: ");
			for (String ns: loopNodes){
				System.out.print(ns+" ");
			}
			System.out.println();
			System.out.print("exit nodes: ");
			for (String ns: loopExitNodes){
				System.out.print(ns+" ");
			}
			System.out.println();
			
			System.out.println();
			for (Path path: allPaths){
				System.out.println(path.toString());
			}
		}*/
				
		loopCount = loopNodes.size();
		
		loopDepth=0;
		
		for (Path path: allPaths){
			
			Stack<String> loopH = new Stack<String>();
			for (int i=0; i<path.getNodes().size(); i++){
				
				Node nd = path.getNodes().get(i);
				
				if (loopExitNodes.contains(nd.getId())&&!loopH.isEmpty()){
					
					int index = loopExitNodes.indexOf(nd.getId());
					//if (index>=4)
					//	System.out.println("method: " + cfg.getClassName()+"."+cfg.getMethodName());
					String loopNode = loopNodes.get(index);
					
					if (!loopH.isEmpty()){
						if (loopH.peek().equals(loopNode))
							loopH.pop();
					}
					
					//System.out.println("    pop: " + pop);
				}
					
				if (loopNodes.contains(nd.getId())){
					if (!loopH.contains(nd.getId())){
						loopH.push(nd.getId());
						//System.out.println("    push: " + nd.getId());
						if (loopDepth<loopH.size() && loopH.size()>1){
						//	System.out.println("    set loopDepth = " + loopH.size());
							loopDepth=loopH.size()-1;
						}
					}
				}
			}
		}
		
	}
	
	public int countElements(Vector<Node> nodes, Node node){
		int cont=0;
		for (int i=0; i<nodes.size(); i++)
			if (nodes.get(i).getId().equals(node.getId()))
				cont++;
		return cont;
	}
	
	
	public Vector<Path> getIterationPaths(CFG cfg, Vector<Path> paths){		
		Vector<Vector<String>> allPathsStr = new Vector<Vector<String>>();
		for (Path path: paths)
			allPathsStr.add(path.getVetNodes());
		
		Vector<Vector<String>> newPathsStr = this.iterateLoopPaths(allPathsStr); 
		
		Vector<Path> newPaths = new Vector<Path>();
		
		for (Vector<String> pathNodes: newPathsStr){
			
			Path path = new Path();
			for (int i=0; i<pathNodes.size(); i++){	
				Node node = cfg.getNode(pathNodes.get(i));
				path.addNode(node);				
			}
			newPaths.add(path);			
		}
		
		return newPaths;
	}
	
	
	private String pathToStr(Vector<String> path){
		String pathStr = "";
		for (int i=0; i<path.size(); i++)
			pathStr+=path.get(i);
		return pathStr;
	}		
	
	public Vector<Path> generateAllPaths(CFG cfg, int iterations){
		
		Vector<Vector<Vector<String>>> newPaths = new Vector<Vector<Vector<String>>>();
		
		Vector<Path> paths = new Vector<Path>();
		
		//1 loop iterations
		Vector<Vector<String>> allPathsStr = this.generateAllStrPaths(cfg);
		
		//pos 0 = allPathsStr
		newPaths.add(allPathsStr);
		
		//2 to N loop iterations
		for (int i=1; i<iterations; i++){
			Vector<Vector<String>> newPathsTemp = this.iterateLoopPaths(newPaths.get(i-1)); 
			newPaths.add(newPathsTemp);
		}
		
		for (int i=1; i<newPaths.size(); i++)
			allPathsStr.addAll(newPaths.get(i));		
		
		for (Vector<String> pathNodes: allPathsStr){
			
			Path path = new Path();
			for (int i=0; i<pathNodes.size(); i++){	
				Node node = cfg.getNode(pathNodes.get(i));
				path.addNode(node);				
			}
			paths.add(path);			
		}
		
		return paths;
	}
	
	private Vector<Vector<String>> generateAllStrPaths(CFG cfg){			
		if (cfg.getMethodName().contains("placeOrder")){
			Scanner scan = new Scanner(System.in);
			scan.next();
		}
		cont = 0;
		this.cfg = cfg;
		allPathsStr = new Vector<Vector<String>>();
		hashPaths = new Vector<String>();
		subPaths = new  Hashtable<String,Vector<Vector<String>>>();
		loops = new Vector<String>();
		ends = new Vector<String>();
		currentPath=new Vector<String>();
		
		visit(cfg.getRoot());						 						
		if (loops.size()>0)
			hasLoop=true;
		else
			hasLoop=false;
		numberOfLoops=loops.size();
		cfg.setLoops(numberOfLoops);
		return allPathsStr;			
	}
	
	private void visit(Node vNode){
		boolean isLoop=false;
	   	
		if (currentPath.contains(vNode.getId())){
	   		 isLoop=true; 
	   	}
	   	 		
	   	currentPath.add(vNode.getId()); 
		 	   	
	   	if (vNode.getNeighbors().size()>0){ //tem vizinhos
			
			if (isLoop){ //se repetiu é loop 
				loops.add(vNode.getId());
				//verifica qual foi o nó seguinte ao loop e visita o nó contrário
				int index = currentPath.indexOf(vNode.getId());
				String nextNode = currentPath.get(index+1);
				//System.out.println("Next: "+nextNode);
				if (vNode.getNeighbors().get(0).getId().equals(nextNode)) //se por aqui foi o caminho anterior
				{
					Maintenance.gambiarra("org.databene.jdbacl.sql.parser.SQLLexer.mIDENTIFIER");
					if (vNode.getNeighbors().size()>1)
						visit(vNode.getNeighbors().get(1));					
				}
				else
				{					
					visit(vNode.getNeighbors().get(0));
				}
			}
			else{
				for (int i=0; i<vNode.getNeighbors().size(); i++){
					visit(vNode.getNeighbors().get(i));				
				}
			}
								
		}							
		else
		{	ends.add(vNode.getId());								
			allPathsStr.add((Vector<String>)currentPath.clone());				
			hashPaths.add(pathToStr((Vector<String>)currentPath.clone()));
			cont++;
		}		
		currentPath.remove(currentPath.size()-1);
	}
	
	public Vector<Path> generateAllPaths(CFG cfg){
		
		Vector<Vector<Vector<String>>> newPaths = new Vector<Vector<Vector<String>>>();
		
		Vector<Path> paths = new Vector<Path>();
		
		Vector<Vector<String>> allPathsStr = this.generateAllStrPaths(cfg);

		newPaths.add(allPathsStr);				
		
		for (int i=1; i<newPaths.size(); i++)
			allPathsStr.addAll(newPaths.get(i));		
		
		for (Vector<String> pathNodes: allPathsStr){
			
			Path path = new Path();
			for (int i=0; i<pathNodes.size(); i++){	
				Node node = cfg.getNode(pathNodes.get(i));
				path.addNode(node);				
			}
			paths.add(path);			
		}
		
		return paths;
	}
	
	//mult
    private Vector<Vector<String>> iterateLoopPaths(Vector<Vector<String>> allPaths){
     int loopCount=1;
   	 Vector<String> subPathsHash = new Vector<String>();
   	 
   	 if (subPaths.size()==0){
   		//seleciona trechos em que há loop
       	 for (Vector<String> path: allPaths){
       		 
       		 for (int i=0; i<path.size(); i++){
       			 String currentNode = path.get(i);
       			 int firstIndex = path.indexOf(currentNode);
       			 int lastIndex = path.lastIndexOf(currentNode);

       			 if (firstIndex!=lastIndex && i!=lastIndex){ //tem duas ocorrências = loop
       				 Vector<Vector<String>> vet = subPaths.get(currentNode);
       				 if (vet==null)
       				 {
       					 vet = new Vector<Vector<String>>();
       					 subPaths.put(currentNode, vet);
       				 }
       				 
       				 Vector<String> subPath = new Vector<String>();
       				 for (int j=firstIndex; j<lastIndex; j++){
       					 subPath.add(path.get(j));
       				 }    				 
       				 
       				 if (!subPathsHash.contains(pathToStr(subPath))){
       					 subPathsHash.add(pathToStr(subPath));
           				 vet.add(subPath);
       				 }    				 
       			 }    			 
       		 }    		    		
       	 }     
   	 }   	 	   	  
   	     	
   	 
   	Vector<Vector<String>> newPaths = new Vector<Vector<String>>();
   	for (Vector<String> path: allPaths){
   		
			 Enumeration<String> nodeLoops = subPaths.keys();
	    	 while (nodeLoops.hasMoreElements()){
	    		 
	    		 String nodeLoop = nodeLoops.nextElement();
	    		 
	    		 if (path.contains(nodeLoop)){
	    			 Vector<Vector<String>> portions = subPaths.get(nodeLoop);
  				 
  	    		 int lastIndex = path.lastIndexOf(nodeLoop);
  	    		 for (int p=0; p<portions.size(); p++){   	    		
  	    		
      				 for (int lc=1; lc<=loopCount; lc++)
      				 {
      					Vector<String> cpPath = (Vector<String>) path.clone();
      					
      					 for (int ii=0; ii<lc; ii++)       						 
      						 cpPath.addAll(lastIndex, portions.get(p));
      					 
      					String hashPath = pathToStr(cpPath);       				 
         				 
         				if (!hashPaths.contains(hashPath)){       					 
         				 newPaths.add(cpPath);
         					 hashPaths.add(hashPath);
         				 }   
      				 }       				        				      				         				   				   
	    		 }
  				 		    	
	    	}    		
	    }
   }

   return newPaths;
 }
	public int getLoopCount() {
		return loopCount;
	}
	public void setLoopCount(int loopCount) {
		this.loopCount = loopCount;
	}
	public int getLoopDepth() {
		return loopDepth;
	}
	public void setLoopDepth(int loopDepth) {
		this.loopDepth = loopDepth;
	}
    
    
    

}

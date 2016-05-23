package profiling.constraint.graph;

import java.util.Hashtable;
import java.util.Vector;

import profiling.constraint.analysis.stack.Variable;

//import eler.marcelo.graph.Constraint;

public class CFG {
	
	protected String className;
	protected String methodName;
	//protected String projectName;
	protected Node root;
	protected Hashtable<String, Node> nodes;
	protected Hashtable<String, Edge> edges;
	protected Hashtable<String, Variable> variables;
	protected Vector<String> stedges;
	protected int loops;
	public boolean isAbstract;
	private String methodSignature;
	private int loopDepth;
	protected int LOB;
	
	protected int arguments;
	protected int linesOfCode;

	//PARAMETERS
	
	public CFG(){
		isAbstract=false;
		nodes = new Hashtable<String,Node>();
		stedges = new Vector<String>();
		edges = new Hashtable<String, Edge>();
		variables = new Hashtable<String,Variable>();		
	}
	
	public String getMethodName() {
		return methodName;
	}
	
	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}
	
	public Hashtable<String, Node> getNodes() {
		return nodes;
	}
	
	public void setNodes(Hashtable<String, Node> nodes) {
		this.nodes = nodes;
	}
	
	public Hashtable<String, Edge> getEdges() {
		return edges;
	}
	
	public void setEdges(Hashtable<String, Edge> edges) {
		this.edges = edges;
	}
	
	public Node getRoot() {
		return root;
	}
	
	public void setRoot(Node root) {		
		root.setRoot(true);
		this.root = root;
	}
	
	
	
	public int getLoopDepth() {
		return loopDepth;
	}

	public void setLoopDepth(int loopDepth) {
		this.loopDepth = loopDepth;
	}

	public Hashtable<String, Variable> getVariables() {
		return variables;
	}
	
	public void setVariables(Hashtable<String, Variable> variables) {
		this.variables = variables;
	}
	
	public Vector<String> getStedges() {
		return stedges;
	}
	
	public void setStedges(Vector<String> stedges) {
		this.stedges = stedges;
	}
	
	public Node getNode(String id){
		return nodes.get(id);
	}
	
	public void addNode(Node node){
		nodes.put(node.getId(),node);
	}
	
	public Edge getEdge(String stedge){
		return edges.get(stedge);
	}
	
	public Edge getEdge(Node source, Node target){
		String st = source.getId()+"-"+target.getId();
		return this.getEdge(st);
	}
	
	public void addEdge(Edge edge){
		//if (!edge.getSource().getNeighbors().contains(edge.getTarget()))
		//	edge.getSource().addNeighbor(edge.getTarget());
		edges.put(edge.getStrEdge(), edge);
	}
	
	public Edge addEdge(Node source, Node target){
		Edge edge = new Edge(source,target);
		edges.put(edge.getStrEdge(), edge);
		return edge;
	}
	

	public Variable getVariable(String varName){
		return variables.get(varName);
	}
	
	public void addVariable(Variable variable){
		variables.put(variable.getValue(), variable);
	}
	
	public void addStedge(String stedge){
		stedges.add(stedge);
	}

	public int getLoops() {
		return loops;
	}

	public void setLoops(int loops) {
		this.loops = loops;
	}
	
	
	
	public int getArguments() {
		return arguments;
	}

	public void setArguments(int arguments) {
		this.arguments = arguments;
	}

	public int getLinesOfCode() {
		return linesOfCode;
	}

	public void setLinesOfCode(int linesOfCode) {
		this.linesOfCode = linesOfCode;
	}

	public int getLOB() {
		return LOB;
	}

	public void setLOB(int lOB) {
		LOB = lOB;
	}

	public String toString(){
		String ret = "";
		/*
		for (String st: stedges){
			ret+=st+"\n";
		}*/
		
		for (Edge edge: edges.values()){
			ret+=edge.getStrEdge()+"\n";
		}
		
		return ret;
	}
	
	public Vector<String> getStrNodes(){
		Vector<String> strNodes = new Vector<String>();
		for (Node node: nodes.values()){
			strNodes.add(node.getId());
		}
		return strNodes;
	}
	
	public CFG copy(){
		CFG copy = new CFG();
		if (this.root!=null){			
			copy.setLoops(this.loops);
			copy.setMethodName(this.methodName);			
			copy.setClassName(this.className);
			copy.setLoopDepth(this.loopDepth);
			
			for (Variable var: variables.values())
				copy.addVariable((Variable)var.copy());
			
			for (String st: stedges){
				copy.addStedge(new String(st));
			}
			
			for (Node node: nodes.values()){
				Node copyNode = node.copy();
				copy.addNode(copyNode);
				if (copyNode.isRoot())
					copy.setRoot(copyNode);
			}
			
			for (Edge edge: edges.values()){
				Node source = copy.getNode(edge.getSource().getId());
				Node target = copy.getNode(edge.getTarget().getId());
				source.addNeighbor(target);
				copy.addEdge(edge.copy(source, target));
			}
		}				
		return copy;
	}
	
	public void changeNodeLabels(){
		Hashtable<String, Node> otherNodes = new Hashtable<String,Node>();
		for (String key: nodes.keySet()){
			Node node = nodes.get(key);
			node.setId(this.methodName+"::"+node.getId());			
			otherNodes.put(node.getId(), node);
		}
		this.nodes = otherNodes;
		
		this.changeEdgeLabels();
	}
	
	public void changeEdgeLabels(){
		Hashtable<String, Edge> otherEdges = new Hashtable<String, Edge>();
		stedges.clear();
		for (Edge edge: edges.values())
		{
			otherEdges.put(edge.getStrEdge(), edge);
			stedges.add(edge.getStrEdge());
		}
		this.edges = otherEdges;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}
	
	public int getConstraintCount(){
		int count = 0;
		for (Edge edge: edges.values()){
			if (edge.isConstrained())
				count++;
			if (edge.isMultConstrained())
				count+=edge.getConstraints().size();
		}
		return count;
	}

	public String getMethodSignature() {
		return methodSignature;
	}

	public void setMethodSignature(String methodSignature) {
		this.methodSignature = methodSignature;
	}
	
	/*
	 * 	
	protected Hashtable<String, Edge> edges;	
	 */
	
	public int getLoopCount(){
		int loopCount = 0;
		
		
		
		return loopCount;
	}
		
	public Vector<Edge> getEdgesFromNode(Node node){
		Vector<Edge> vetEdges = new Vector<Edge>();
		for (Edge singleEdge: edges.values()){
			if (singleEdge.getSource().getId().equals(node.getId()))
				vetEdges.add(singleEdge);
		}
		
		return vetEdges;
	}
	
	public String getPlanEdges(){
		String plan = "";
		Vector<Edge> orderedEdges = new Vector<Edge>();
		
		for (Edge edgreal : edges.values())
		{
			String vlsrc = edgreal.getSource().getId();
			String vltgt = edgreal.getTarget().getId();
			Edge edg = new Edge(new Node(vlsrc), new Node(vltgt));
		//	System.out.println("new edge: " + edg.getStrEdge());
			//System.out.println("Vector configuration: " );
			//for (Edge e: orderedEdges)
			//	System.out.println(e.getStrEdge());
			int srcnew = Integer.valueOf(edg.getSource().getId());
			int tgtnew = Integer.valueOf(edg.getTarget().getId());
			
			int i=0;
			boolean foundPos=false;
			while (i<orderedEdges.size() && !foundPos){
							
				int src = Integer.valueOf(orderedEdges.get(i).getSource().getId());
				
				if (srcnew<=src)
					foundPos=true;
				else
					i++;
			}
			
			if (foundPos){

				int src = Integer.valueOf(orderedEdges.get(i).getSource().getId());				
				//System.out.println("found: srcnew = "+ srcnew+ " srcvet: " + src + " index: " + i);
				//System.out.println();
				if (srcnew==src){
					foundPos=false;
					while (i<orderedEdges.size() && !foundPos){
						src = Integer.valueOf(orderedEdges.get(i).getSource().getId());
						int tgt = Integer.valueOf(orderedEdges.get(i).getTarget().getId());
						
						if (tgtnew<tgt || srcnew!=src){
							foundPos=true;
						}
						else
							i++;
					}
				}
				orderedEdges.add(i, edg);
			}
			else{
				orderedEdges.add(edg);
				//System.out.println();
			}
		}
		
		
		
		//for (int i=0; i<orderedEdges.size(); i++)
			//plan+=orderedEdges.get(i).getSource().getId()+"->"+orderedEdges.get(i).getTarget().getId()+"\n";
		
		//renomear nós
		Vector<String> newNames = new Vector<String>();
		for (int i=0; i<orderedEdges.size(); i++){
			String srcId = orderedEdges.get(i).getSource().getId();
			if (!newNames.contains(srcId)){
				newNames.add(srcId);
			}
		}
		
		for (int i=0; i<orderedEdges.size(); i++){
			String tgtId = orderedEdges.get(i).getTarget().getId();
			
			if (!newNames.contains(tgtId)){
				int tgtIdi = Integer.valueOf(tgtId);
				
				boolean found=false;
				int j=0;
				while (!found && j<newNames.size()){
					int nameInt = Integer.valueOf(newNames.get(j));
					if (tgtIdi < nameInt)
						found = true;
					else
						j++;
				}
				if (found)
					newNames.add(j,tgtId);
				else
					newNames.add(tgtId);
			}
			
		}
		
		//for (int i=0; i<newNames.size(); i++)
			//System.out.println(newNames.get(i));
		//System.out.println();
		
		for (int i=0; i<orderedEdges.size(); i++)
		{
			String src = orderedEdges.get(i).getSource().getId();
			String tgt = orderedEdges.get(i).getTarget().getId();
			
			int indSrc = newNames.indexOf(src);
			int indTgt = newNames.indexOf(tgt);
			
			orderedEdges.get(i).getSource().setId("n"+indSrc);
			orderedEdges.get(i).getTarget().setId("n"+indTgt);
		}
		
		for (int i=0; i<orderedEdges.size(); i++)
			plan+=orderedEdges.get(i).getSource().getId()+"->"+orderedEdges.get(i).getTarget().getId()+":";
		
		return plan;
	}
	
	public static void main(String args[]){
		CFG cfg = new CFG();
		
		cfg.addEdge(new Node("13"),new Node("5"));
		cfg.addEdge(new Node("5"),new Node("7"));
		cfg.addEdge(new Node("7"),new Node("9"));
		cfg.addEdge(new Node("3"),new Node("9"));
		cfg.addEdge(new Node("19"),new Node("25"));
		cfg.addEdge(new Node("25"),new Node("30"));
		cfg.addEdge(new Node("0"),new Node("3"));
		cfg.addEdge(new Node("0"),new Node("1"));
		cfg.addEdge(new Node("0"),new Node("4"));
		cfg.addEdge(new Node("7"),new Node("19"));
		cfg.addEdge(new Node("19"),new Node("15"));
		cfg.addEdge(new Node("3"),new Node("7"));
		cfg.addEdge(new Node("19"),new Node("3"));
		cfg.addEdge(new Node("13"),new Node("3"));
		
		String plan = cfg.getPlanEdges();
		System.out.println(plan);
	}
		
}

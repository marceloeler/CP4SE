package profiling.constraint.graph;

import java.util.Hashtable;
import java.util.Vector;

import profiling.constraint.analysis.stack.CodeElement;
import profiling.constraint.analysis.stack.MethodCall;
import profiling.constraint.project.Project;
import profiling.constraint.symbolic.AbstractUpdate;
import profiling.constraint.symbolic.AssignmentUpdate;

public class CombinedCFG {
	
	private CFG cfg;
	private Hashtable<String, CFG> cfgs;
	
	public void addCFG(CFG cfg){		
		cfg.changeNodeLabels();
		cfgs.put(cfg.getMethodName(),cfg);
	}
	
	
	public CombinedCFG(CFG cfg){		
		this.cfg = cfg.copy();
		this.cfg.changeNodeLabels();		
		cfgs = new Hashtable<String, CFG>();
	}
	
	/*
	public void combineCFGs(){
		
		//Vector<Node> integrationNodes = new Vector<Node>();
		Hashtable<String,Node> cfgNodes = (Hashtable<String,Node>) cfg.getNodes().clone();
		for (Node node: cfgNodes.values())
		{
			for (int i=0; i<node.getVarUpdates().size(); i++){
				AbstractUpdate update = node.getVarUpdates().get(i);
				if (update instanceof AssignmentUpdate){
					AssignmentUpdate assignment = (AssignmentUpdate) update;
					CodeElement updatedValue = assignment.getUpdatedValue();
					if (updatedValue instanceof MethodCall){
						//integra CFGS						
						MethodCall methodCall = (MethodCall) updatedValue;						
						CFG calledCfg = Project.getCFG(methodCall.getClassName(),methodCall.getMethodName());
						if (calledCfg!=null){							
							calledCfg = calledCfg.copy();
							this.addCFG(calledCfg);
							this.joinCFGs(node, i, calledCfg);
						}
					}
				}				
			}
		}
				
	}*/
	
	private void joinCFGs(Node nodeA, int updateIndex, CFG calledCfg){
					
		//split node
		//updates 1, 2, 3, i, 5, 6
		//NodeA (node) fica com updates de 1 a 3
		//NodeB fica com updates de i a 6 (i é onde está a chamada ao método)		
		
		for (int i=updateIndex; i<nodeA.getVarUpdates().size(); i++){
			nodeA.getVarUpdates().remove(i--);
		}
				
		Node nodeB = nodeA.copy();		
		for (int i=updateIndex-1; i>=0; i--){
			nodeB.getVarUpdates().remove(i);
		}		
		nodeB.setId(nodeB.getId()+"b");
		cfg.addNode(nodeB);
				
		System.out.println("NodeA neighbors: " + nodeA.getNeighbors().size());
		//elimina as arestas do nodeA
		for (int i=0; i<nodeA.getNeighbors().size(); i++){
			Node neighbor = nodeA.getNeighbors().get(i);
			String stEdge = nodeA.getId()+"-"+neighbor.getId();
			Edge edge = cfg.getEdges().remove(stEdge);
			if (edge!=null)
			{
				cfg.getEdges().remove(edge.getStrEdge());
				edge.setSource(nodeB);
				cfg.addEdge(edge);
				cfg.addStedge(edge.getStrEdge());
				nodeB.addNeighbor(edge.getTarget());
			}					
		}		
		nodeA.getNeighbors().clear();
		nodeA.setId(nodeA.getId()+"a");
		
		//inclui aresta do nodeA para o root do CFG
		System.out.println("ROOT: " + calledCfg.getRoot().getId());
		nodeA.addNeighbor(calledCfg.getRoot());
		Edge edge = new Edge(nodeA,calledCfg.getRoot());
		edge.setConstrained(false);
		edge.setMultConstrained(false);
		edge.setFalseBranch(false);
		cfg.addEdge(edge);
		cfg.addStedge(edge.getStrEdge());
		
		//inclui aresta dos exit nodes do calledCfg para o nodeB
		for (Node nd: calledCfg.getNodes().values()){
			if (nd.hasReturn()){
				nd.addNeighbor(nodeB);
				Edge returnEdge = new Edge(nd,nodeB);
				returnEdge.setConstrained(false);
				returnEdge.setFalseBranch(false);
				returnEdge.setMultConstrained(false);
				cfg.addEdge(returnEdge);
				cfg.addStedge(returnEdge.getStrEdge());
			}
		}	
		
		System.out.println("CFG nodes: ");
		for (Node nd: cfg.getNodes().values())
		{
			System.out.println(nd.getId()+" neighbors: "+nd.getNeighbors().size());
		}
	}


	public CFG getCfg() {
		return this.cfg;
	}


	public void setCfg(CFG cfg) {
		this.cfg = cfg;
	}


	public Hashtable<String, CFG> getCfgs() {
		return this.cfgs;
	}


	public void setCfgs(Hashtable<String, CFG> cfgs) {
		this.cfgs = cfgs;
	}

	
	
}

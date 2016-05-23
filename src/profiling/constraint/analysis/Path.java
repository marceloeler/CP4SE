package profiling.constraint.analysis;

import java.util.Vector;

import profiling.constraint.analysis.info.PathMetric;
import profiling.constraint.graph.CFG;
import profiling.constraint.graph.Node;

public class Path {

	private Vector<Node> nodes;
	private Vector<Constraint> constraints;
	private boolean unsolvable;
	private PathMetric pathMetric;
	private boolean nonLinear;
	
	public Path(){
		nodes = new Vector<Node>();
		constraints = new Vector<Constraint>();
		nonLinear=false;
	}
	
	public void addNode(Node node){
		nodes.add(node);
	}
	
	public Vector<Node> getNodes(){
		return nodes;
	}
	
	public Vector<Constraint> getConstraints() {
		return constraints;
	}

	public void setConstraints(Vector<Constraint> constraints) {
		this.constraints = constraints;
	}

	public boolean isUnsolvable() {
		return unsolvable;
	}

	public void setUnsolvable(boolean unsolvable) {
		this.unsolvable = unsolvable;
	}

	public PathMetric getPathMetric() {
		return pathMetric;
	}

	public void setPathMetric(PathMetric pathMetric) {
		this.pathMetric = pathMetric;
	}
	
	

	public boolean isNonLinear() {
		return nonLinear;
	}

	public void setNonLinear(boolean nonLinear) {
		this.nonLinear = nonLinear;
	}

	public String toString(){
		String pathStr = "";
		for (Node node: nodes){
			pathStr+=node.getId()+" ";
		}
		
		return pathStr;
	}
	
	public Vector<String> getVetNodes(){
		Vector<String> vetNodes = new Vector<String>();
		for (Node nd: nodes){
			vetNodes.add(nd.getId());
		}
		return vetNodes;
	}
	
	public boolean containsNode(Vector<String> elements){
		Vector<String> vetNodes = this.getVetNodes();
		for (String element: elements)
			if (vetNodes.contains(element))
				return true;
		return false;		
	}
	
	public Vector<String> getVetEdges(){
		Vector<String> vetNodes = this.getVetNodes();
		Vector<String> vetEdges  = new Vector<String>();
		for (int i=0; i<vetNodes.size()-1; i++){
			String stEdge = vetNodes.get(i)+"-"+vetNodes.get(i+1);
			vetEdges.add(stEdge);
		}
		return vetEdges;
	}
	
	public boolean containsEdge(Vector<String> elements){
		
		Vector<String> vetEdges = this.getVetEdges();
		for (String element: elements){
			if (vetEdges.contains(element))
				return true;
		}
				
		return false;
	}
	
	public void calculateMetrics(CFG cfg){
		
		Info.constraintsequences++;
		
		ConstraintSequenceGenerator seqGen = new ConstraintSequenceGenerator();
		//System.out.println("before getconstraintsequence elements");
		constraints = seqGen.getConstraintSequenceElements(cfg,this);
		//System.out.println("after getconstraintsequence elements");
		checkForEnsuredUnsolvability();
		if (this.unsolvable)
			Info.unsolvablecs++;
		
		checkForLinearity();
		if (this.isNonLinear()){
			Info.path_nonLinear++;
		}
		
		
		//System.out.println("before new PathMetric()");
		pathMetric = new PathMetric(this.constraints);
		//System.out.println("after new PathMetric()");
	}
	
	private static int cont = 0;
	
	public void checkForLinearity(){
		//int contcon=0;
		for (Constraint constraint: constraints){
			if (constraint.hasNonLinearExpression()){
				this.nonLinear=true;
				//contcon++;
			}
		}
		//if (contcon>0)
			//System.out.println("Same path: " + contcon +" non-linear constraints");
	}
	
	public void checkForEnsuredUnsolvability(){
		
		int size = constraints.size();
		for (int i=0; i<size-1; i++){
			
			for (int j=i; j<size-1; j++){
				
				Constraint constraintX = constraints.get(j);
				if (constraintX.isUnsolvable())
					this.unsolvable=true;			
				Constraint constraintY = constraints.get(j+1);
				
				if (constraintX.negate(constraintY))
					this.unsolvable=true;		
			}			
		}		
	}
	
	
}
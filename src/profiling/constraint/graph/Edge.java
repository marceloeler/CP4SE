package profiling.constraint.graph;

import java.util.Vector;
import profiling.constraint.analysis.Constraint;

public class Edge {

	private boolean falseBranch;
	private Node source;
	private Node target;
	private boolean covered;
	private boolean constrained;
	private boolean multConstrained;
	private boolean visited;
	
	private Constraint constraint;
	private Vector<Constraint> constraints;

	public Edge(Node source, Node target){
		constraints = new Vector<Constraint>();
		this.source = source;
		this.target = target;
	}

	public Node getSource() {
		return source;
	}

	public void setSource(Node source) {
		this.source = source;
	}

	public Node getTarget() {
		return target;
	}

	public void setTarget(Node target) {
		this.target = target;
	}

	public boolean isCovered() {
		return covered;
	}

	public void setCovered(boolean covered) {
		this.covered = covered;
	}
	
	public String getStrEdge(){
		return source.getId()+"-"+target.getId();
	}

	public Constraint getConstraint() {
		return constraint;
	}

	public void setConstraint(Constraint constraint) {
		this.constraint = constraint;
		//System.out.println(constraint);
	}

	public boolean isConstrained() {
		return constrained;
	}

	public void setConstrained(boolean constrained) {
		this.constrained = constrained;
	}

	public boolean isMultConstrained() {
		return multConstrained;
	}

	public void setMultConstrained(boolean multConstrained) {
		this.multConstrained = multConstrained;
	}

	public Vector<Constraint> getConstraints() {
		return constraints;
	}

	public void setConstraints(Vector<Constraint> constraints) {
		this.constraints = constraints;
	}

	public void addMultConstraint(Constraint constraint){
		constraints.add(constraint);
		//System.out.println(constraint);
	}

	public boolean isFalseBranch() {
		return falseBranch;
	}

	public void setFalseBranch(boolean falseBranch) {
		this.falseBranch = falseBranch;
	}
	
	
	
	public boolean isVisited() {
		return visited;
	}

	public void setVisited(boolean visited) {
		this.visited = visited;
	}

	public Edge copy(Node source, Node target){
		Edge copy = new Edge(source, target);
		copy.setFalseBranch(this.falseBranch);
		copy.setCovered(this.covered);
		copy.setConstrained(this.constrained);
		copy.setMultConstrained(this.multConstrained);
		copy.setConstraint(constraint.copy());
		copy.setVisited(visited);
		for (Constraint cnstr: constraints)
			copy.addMultConstraint(cnstr.copy());
		return copy;		
	}
	
	
}

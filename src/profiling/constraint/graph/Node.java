package profiling.constraint.graph;

import java.util.Vector;

import profiling.constraint.symbolic.AbstractUpdate;

public class Node {
	
	private String id;
	private Vector<Node> neighbors;
	
	private int firstInstruction;
	private int lastInstruction;
	
	private boolean visited;
	private boolean hasGoto;
	private boolean hasReturn;
	private boolean hasConstraint;
	private boolean loop;
	private boolean exception;
	private boolean root;
	private boolean exceptionHandle;
	private boolean infiniteLoop;
	
	private Vector<AbstractUpdate> varUpdates;
	
	private profiling.constraint.analysis.Constraint elementConstraint;
	
	public Node(){				
		neighbors = new Vector<Node>();		
		varUpdates = new Vector<AbstractUpdate>();
	}
	
	public Node(String id){		
		this.id = id;		
		neighbors = new Vector<Node>();		
		varUpdates = new Vector<AbstractUpdate>();
	}		

	public String getId(){
		return id;
	}
	
	public void setId(String id){		
		this.id = id;
	}
		
	
	public boolean hasGoto(){
		return hasGoto;
	}
	
	public void setHasGoto(boolean hasGoto){
		this.hasGoto = hasGoto;
	}
	
	public boolean hasReturn(){
		return hasReturn;
	}
	
	public void setHasReturn(boolean hasReturn){
		this.hasReturn = hasReturn;
	}
	
	public void addNeighbor(Node node){
		neighbors.add(node);
	}
	
	public void setHasConstraint(boolean hasConstraint){
		this.hasConstraint = hasConstraint;
	}
	
	public boolean hasConstraint(){
		return hasConstraint;
	}

	public boolean isLoop() {
		return loop;
	}

	public void setLoop(boolean loop) {
		this.loop = loop;
	}
			 	
	public Vector<AbstractUpdate> getVarUpdates() {
		return varUpdates;
	}

	public void setVarUpdates(Vector<AbstractUpdate> varUpdates) {
		this.varUpdates = varUpdates;
	}
	
	public void addVarUpdate(AbstractUpdate update){
		varUpdates.add(update);
	}

	public void setNeighbors(Vector<Node> neighbors) {
		this.neighbors = neighbors;
	}

	public Vector<Node> getNeighbors() {
		return neighbors;
	}	
	
	public profiling.constraint.analysis.Constraint getElementConstraint() {
		return elementConstraint;
	}

	public void setElementConstraint(profiling.constraint.analysis.Constraint elementConstraint) {
		this.elementConstraint = elementConstraint;
	}

	public boolean isException() {
		return exception;
	}

	public void setException(boolean exception) {
		this.exception = exception;
	}		

	public int getFirstInstruction() {
		return firstInstruction;
	}

	public void setFirstInstruction(int firstInstruction) {
		this.firstInstruction = firstInstruction;
	}

	public int getLastInstruction() {
		return lastInstruction;
	}

	public void setLastInstruction(int lastInstruction) {
		this.lastInstruction = lastInstruction;
	}

	public boolean isExceptionHandle() {
		return exceptionHandle;
	}

	public void setExceptionHandle(boolean exceptionHandle) {
		this.exceptionHandle = exceptionHandle;
	}
	
	

	public boolean isInfiniteLoop() {
		return infiniteLoop;
	}

	public void setInfiniteLoop(boolean infiniteLoop) {
		this.infiniteLoop = infiniteLoop;
	}

	public Node copy(){
		Node copy = new Node(this.id);
		copy.setHasConstraint(this.hasConstraint);
		copy.setLoop(this.loop);
		copy.setException(this.exception);
		copy.setExceptionHandle(this.exceptionHandle);
		copy.setHasReturn(this.hasReturn);
		copy.setHasGoto(this.hasGoto);
		copy.setRoot(this.root);
		copy.setVisited(this.visited);
		copy.setFirstInstruction(this.firstInstruction);
		copy.setLastInstruction(this.lastInstruction);
		copy.setInfiniteLoop(this.isInfiniteLoop());
		if (this.elementConstraint!=null)
			copy.setElementConstraint(this.elementConstraint.copy());
		
		for (int i=0; i<neighbors.size(); i++)
		{
			Node node = neighbors.get(i);
			//copia neighbors pode gerar um loop infinito quando existem laços
		}
		
		for (int i=0; i<varUpdates.size(); i++){
			AbstractUpdate update = varUpdates.get(i);
			copy.addVarUpdate(update.copy());			
		}
			
		return copy;
	}

	public boolean isRoot() {
		return root;
	}

	public void setRoot(boolean root) {
		this.root = root;
	}

	public boolean isVisited() {
		return visited;
	}

	public void setVisited(boolean visited) {
		this.visited = visited;
	}
	
	
	
}

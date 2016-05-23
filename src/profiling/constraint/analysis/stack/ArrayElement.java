package profiling.constraint.analysis.stack;


public class ArrayElement extends CodeElement {
		
	private CodeElement var;
	private CodeElement index;
	

	public ArrayElement(CodeElement var, CodeElement index){
		this.var = var;	
	    this.type= var.getType();
		this.index = index;		
		this.value = toString();
	}

	public CodeElement getIndex() {
		return index;
	}

	public void setIndex(CodeElement index) {
		this.index = index;
	}
	
	public String toString(){		
		String ret = this.var.getValue()+"["+index.toString()+"]";		
		return ret;
	}
	
	public CodeElement copy(){
		ArrayElement ae = new ArrayElement(var.copy(),index.copy());
		return ae;
	}
	
	public void setVisited(boolean visited){
		this.visited = visited;		
		index.setVisited(visited);		
	}
	
	public String getType(){
		return this.var.getType();
	}
	
	public CodeElement getVar(){
		return this.var;
	}
	
	public void setVar(CodeElement var){
		this.var = var;
	}			
}

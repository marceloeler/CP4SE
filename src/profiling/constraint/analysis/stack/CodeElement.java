package profiling.constraint.analysis.stack;

public abstract class CodeElement {
	
	public boolean visited;
	protected String value;
	protected String type;
	
	public CodeElement(){
	//	type="";
	}
	
	public CodeElement(String value){
		this.value = value;
		//type="";
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public abstract String getType();
	
	public void setType(String type){
		this.type = type;
	}
	
	public abstract String toString();
	
	public abstract CodeElement copy();
	
	public abstract void setVisited(boolean visited);
	
	public boolean isVisited(){
		return visited;
	}

}

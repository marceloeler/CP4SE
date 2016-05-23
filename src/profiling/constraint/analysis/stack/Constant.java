package profiling.constraint.analysis.stack;

public class Constant extends CodeElement {
	
	public Constant(String value){
		super (value);
	}
	
	public CodeElement copy(){
		Constant constant = new Constant(value);
		constant.setType(type);
		return constant;
	}
	
	public void setVisited(boolean visited){
		this.visited = visited;
	}
	
	public String getType(){
		return type;
	}

	public String toString(){
		return value;
	}
}

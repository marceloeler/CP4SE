package profiling.constraint.analysis.stack;

public class StaticClass extends CodeElement {
	
	private String control;
	
	public StaticClass(String value,String control){
		super (value);
		this.type=value;
		this.control = control;
	}
	
	public CodeElement copy(){
		StaticClass stc = new StaticClass(value,this.control);
		stc.setVisited(visited);
		stc.visited = visited;
		stc.setType(type);			
		return stc;
	}
	
	public void setVisited(boolean visited){
		this.visited = visited;
	}
	
	public String getType(){
		return value;
	}
	
	public String getValue(){
		return value + "_"+control;
	}
	
	public String toString(){
		return value+"_"+control;
	}
	
	public void setControl(String control){
		this.control = control;
	}
	
	public String getControl(){
		return control;
	}

}

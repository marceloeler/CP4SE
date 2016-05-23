package profiling.constraint.analysis.stack;

import java.util.Vector;

public class NewObject extends CodeElement {	
	
	public NewObject(String value){
		super(value);
		type = "newObject";
	}
	
	public CodeElement copy(){
		NewObject no = new NewObject(value);
		return no;
	}
	
	public void setVisited(boolean visited){
		this.visited = visited;
	}
	
	public String getType(){
		return "";
	}
	
	public String toString(){
		return value;
	}
}

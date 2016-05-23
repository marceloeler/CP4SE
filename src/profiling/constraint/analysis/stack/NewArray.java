package profiling.constraint.analysis.stack;

import java.util.Vector;

public class NewArray extends CodeElement {
	
	private Vector<CodeElement> size;

	public NewArray(String value, Vector<CodeElement> size, String type){
		this.size = size;			
		this.size = size;
		this.type = type.replace("[", "");
		this.value = toString();
	}

	public Vector<CodeElement> getSize() {
		return size;
	}

	public void setSize(Vector<CodeElement> size) {
		this.size = size;
	}
	
	public CodeElement copy(){
		NewArray na = new NewArray (value, size, type);
		return na;
	}
	
	public void setVisited(boolean visited){
		this.visited = visited;
		for (int i= 0; i<size.size(); i++)
			size.get(i).setVisited(visited);
	}
	
	public String getType(){		
		return type;
	}
	
	public String toString(){
		String ret= "new_"+type+"_array";
		for (int i=size.size()-1; i>=0;i--)
			ret+="("+size.get(i)+")";
		return ret;
	}
	
	public void setType(String type){
		type=type.replace("[", "");
		this.type=type;
	}
	
	public String getValue(){
		this.value = toString();
		return this.value;
	}
}

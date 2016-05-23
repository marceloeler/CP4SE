package profiling.constraint.analysis.stack;

public class SpecialChar extends CodeElement {

	
	public SpecialChar(String value){		
		super(value);
	}
	
	@Override
	public CodeElement copy() {
		// TODO Auto-generated method stub
		return new SpecialChar(value);
	}
	
	public String toString(){
		return this.value;
	}
	
	public void setVisited(boolean visited){
		this.visited = visited;
	}
	
	
	public String getType(){
		return "";
	}

}

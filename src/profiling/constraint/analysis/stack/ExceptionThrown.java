package profiling.constraint.analysis.stack;

public class ExceptionThrown extends CodeElement {

	
	public ExceptionThrown(String value){
		super (value);
		this.type = value;
	}
	
	@Override
	public String getType() {
		// TODO Auto-generated method stub
		return this.type;
	}

	@Override
	public CodeElement copy() {
		// TODO Auto-generated method stub
		CodeElement copy = new ExceptionThrown(this.value);
		copy.setType(this.type);
		return copy;
	}

	@Override
	public void setVisited(boolean visited) {
		// TODO Auto-generated method stub
		this.visited = true;
	}
	
	public String toString(){
		return value;
	}
}

package profiling.constraint.analysis.stack;


public class ArrayAssignment extends Assignment {
	
	private CodeElement index;
	
	public ArrayAssignment(CodeElement variable, CodeElement assignmentValue, CodeElement index){
		super(variable,assignmentValue);
		this.index = index;		
	}

	public CodeElement getIndex() {
		return index;
	}

	public void setIndex(CodeElement index) {
		this.index = index;
	}
	
	public String toString(){		
		return variable.toString()+"["+index.toString()+"] = "+assignmentValue.toString();
	}
	

}

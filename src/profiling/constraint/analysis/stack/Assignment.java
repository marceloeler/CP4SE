package profiling.constraint.analysis.stack;


public class Assignment extends CodeElement {
	
	protected CodeElement variable;
	protected CodeElement assignmentValue;
	
	public Assignment(){		
	}
	
	public Assignment(CodeElement variable, CodeElement assignmentValue){
		this.variable = variable;
		this.assignmentValue = assignmentValue;		
		//this.value = toString();
	}

	public CodeElement getVariable() {
		return variable;
	}

	public void setVariable(CodeElement variable) {
		this.variable = variable;
	}

	public CodeElement getAssignmentValue() {
		return assignmentValue;
	}

	public void setAssignmentValue(CodeElement assignmentValue) {
		this.assignmentValue = assignmentValue;
	}
		
	
	public String toString(){
		return this.variable.toString() + "=" + this.assignmentValue.toString();
	}
	
	public CodeElement copy(){
		Assignment assignment = new Assignment(variable.copy(),assignmentValue.copy());
		return assignment;
	}
	
	public void setVisited(boolean visited){
		this.visited = visited;
		variable.setVisited(visited);
		assignmentValue.setVisited(visited);
	}
	
	public String getType(){
		return "";
	}
}

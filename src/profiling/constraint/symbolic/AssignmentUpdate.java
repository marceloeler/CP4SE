package profiling.constraint.symbolic;

import profiling.constraint.analysis.stack.Assignment;

/*
 * var = obj.method1();
 * var = var2*var3; 
 * var = var2*var3 + obj.calc();
 */
public class AssignmentUpdate extends AbstractUpdate {		

	private Assignment assignment;
	
	public AssignmentUpdate(Assignment assignment){
		this.assignment = assignment;
		this.variableUpdated = assignment.getVariable();
		this.updatedValue = assignment.getAssignmentValue();
	}

	public Assignment getAssignment() {
		return assignment;
	}

	public void setAssignment(Assignment assignment) {
		this.assignment = assignment;
		this.variableUpdated = assignment.getVariable();
		this.updatedValue = assignment.getAssignmentValue();
	}
	
	
	public AbstractUpdate copy(){
		AssignmentUpdate copy = new AssignmentUpdate((Assignment)this.assignment.copy());
		return copy;		
	}
	
	public String toString(){
		return assignment.toString();
	}

}

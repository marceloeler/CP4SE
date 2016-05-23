package profiling.constraint.graph;

import java.util.Vector;

public class Constraint {
	
	private String expression;
	private Vector<String> variables;
	
	public Constraint(){
		variables = new Vector<String>();
	}
	
	public String getExpression() {
		return expression;
	}
	
	public void setExpression(String expression) {
		this.expression = expression;
	}
	
	public Vector<String> getVariables() {
		return variables;
	}
	
	public void setVariables(Vector<String> variables) {
		this.variables = variables;
	}
	
	public void addVariable(String varName){
		variables.add(varName);
	}
	
}

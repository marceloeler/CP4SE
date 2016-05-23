package profiling.constraint.analysis.stack;

import java.util.Vector;

public class RelationalExpression extends CodeElement {

	private CodeElement leftSide;
	private RelationalOperator operator;
	private CodeElement rightSide;

	public RelationalExpression(){

	}
		
	public RelationalExpression(CodeElement leftSide,
			RelationalOperator operator, CodeElement rightSide) {
		super();

		this.leftSide = leftSide;
		this.operator = operator;
		this.rightSide = rightSide;
	}
	
	public CodeElement getLeftSide() {
		return leftSide;
	}
	public void setLeftSide(CodeElement leftSide) {
		this.leftSide = leftSide;
	}
	public RelationalOperator getOperator() {
		return operator;
	}
	public void setOperator(RelationalOperator operator) {
		this.operator = operator;
	}
	public CodeElement getRightSide() {
		return rightSide;
	}
	public void setRightSide(CodeElement rightSide) {
		this.rightSide = rightSide;
	}
	
	public String toString(){
		return leftSide.toString() + operator.toString() + rightSide.toString();
	}
	
	public CodeElement copy(){
		RelationalExpression re = new RelationalExpression(leftSide.copy(), (RelationalOperator) operator.copy(),rightSide.copy());
		return re;
	}
	
	public void setVisited(boolean visited){
		this.visited = visited;
		leftSide.setVisited(visited);
		operator.setVisited(visited);
		rightSide.setVisited(visited);		
	}
	
	public String getType(){
		String tp = "";
		
			Vector<String> types = new Vector<String>();
			types.add(leftSide.getType());
			types.add(rightSide.getType());

			boolean sameType=true;
			for (int i=0; i<types.size()-1; i++)
				if (!types.get(i).equals(types.get(i+1)))
					sameType=false;
			
			if (sameType==true)
				tp = types.get(0);
			else
			{
				if (types.contains("double"))
					tp = "double";
				else
					if (types.contains("float"))
						tp = "float";
					else
						if (types.contains("int"))
							tp = "int";
						else
							if (types.contains("byte"))
								tp = "byte";
							else
								tp = types.get(0);
			}
		
		return tp;
	}
}

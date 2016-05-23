package profiling.constraint.analysis;

import java.util.Vector;

import profiling.constraint.analysis.stack.ArithmeticExpression;
import profiling.constraint.analysis.stack.ArrayElement;
import profiling.constraint.analysis.stack.CodeElement;
import profiling.constraint.analysis.stack.Constant;
import profiling.constraint.analysis.stack.ExceptionThrown;
import profiling.constraint.analysis.stack.MethodCall;
import profiling.constraint.analysis.stack.RelationalOperator;

public class Constraint {
	
	public String originalConstraint;
	private CodeElement leftSide;
	private RelationalOperator operator;
	private CodeElement rightSide;
	private String type;
	
	private boolean exception;
	
	public Constraint(CodeElement leftSide, RelationalOperator operator,
			CodeElement rightSide, boolean exception) {
		super();
		this.leftSide = leftSide;
		this.operator = operator;
		this.rightSide = rightSide;
		this.exception=exception;
		this.type=findType();		
	}
	
	

	public Constraint(CodeElement leftSide) {
		super();
		this.leftSide = leftSide;
		if (leftSide instanceof ExceptionThrown){
			this.operator = null;
			this.rightSide = null;
			this.type = leftSide.getValue();
			this.exception = true;
		}
		else
		{
			this.operator = new RelationalOperator("==");		
			this.rightSide = new Constant("true");
			this.rightSide.setType("boolean");
			this.type="boolean";	
		}
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
		if (this.isException())
			return leftSide.toString();
		else
			return leftSide.toString() + operator.getValue() + rightSide.toString();
	} 
	
	public String toString2(){
		if (this.isException())
			return leftSide.toString();
		else
			return leftSide.toString() + operator.getValue() + "\n"+rightSide.toString()+"\n";
	} 
	
	public Constraint negate(){
		if (this.isException())
			return this;
		else
			return new Constraint(leftSide, operator.negate(),rightSide, false);		
	}
	
	public Constraint copy(){
				
		Constraint cnstr=null;
		if (this.isException())
			cnstr = new Constraint(leftSide.copy(), null, null, true);		
		else
			cnstr =  new Constraint(leftSide.copy(), (RelationalOperator)operator.copy(), rightSide.copy(), false);
		cnstr.originalConstraint=this.originalConstraint;
		return cnstr;
	}
	
	public String getType(){
		return type;
	}

	public boolean isException() {
		return exception;
	}

	public void setException(boolean exception) {
		this.exception = exception;
	}
	
	private String findType(){	
		
		if (this.isException())
			return type;
		
		String tp="";
		Vector<String> types = new Vector<String>();		
		if (leftSide.getType()==null){
			if (leftSide.getType()==null)
				System.out.println("  leftclass: " + leftSide.getClass());
		//System.out.println("Left: " + leftSide.getType()+ "  =  "+ leftSide.getClass());
		//System.out.println("Right: " + rightSide.getType()+ "  =  "+ rightSide.getClass());
		}
		types.add(leftSide.getType());
		types.add(rightSide.getType());

		boolean sameType=true;
		for (int i=0; i<types.size()-1; i++)
		{
		//	System.out.println("type("+i+"): "+types.get(i));
		//	System.out.println("type("+(i+1)+"): "+types.get(i+1));
			if (!types.get(i).equals(types.get(i+1)))
				sameType=false;
		}
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
	
	
	public boolean negate(Constraint constraint){
		

		CodeElement leftX = constraint.getLeftSide();
		CodeElement rightX = constraint.getRightSide();
		CodeElement opX = constraint.getOperator();
		
		CodeElement leftY = this.getLeftSide();
		CodeElement rightY = this.getRightSide();
		CodeElement opY = this.getOperator();
		
		if (leftX==null || rightX==null || opX==null || leftY==null || rightY==null || opY==null)
			return false;
		
		if ((leftX.getValue().equals(leftY.getValue()))	&& (rightX.getValue().equals(rightY.getValue()))){
			if ( opX.getValue().equals("<") && ( opY.getValue().equals(">") || opY.getValue().equals(">=") || opY.getValue().equals("==")))
				return true;		
			
			if ( opX.getValue().equals("<=") && ( opY.getValue().equals(">")))
				return true;
			
			if ( opX.getValue().equals(">") && ( opY.getValue().equals("<") || opY.getValue().equals("<=") || opY.getValue().equals("==")))
				return true;
			
			if ( opX.getValue().equals(">=") && ( opY.getValue().equals("<")))
				return true;
			
			if ( opX.getValue().equals("==") && ( opY.getValue().equals("!=") || opY.getValue().equals("<") || opY.getValue().equals("<=")))
				return true;
			
			if ( opX.getValue().equals("!=") && ( opY.getValue().equals("==")))
				return true;	
		}
		
		
		
		if ((leftX.getValue().equals(rightY.getValue()))	&& (rightX.getValue().equals(leftY.getValue()))){
			if ( opX.getValue().equals("<") && ( opY.getValue().equals("<") || opY.getValue().equals("<=") || opY.getValue().equals("==")))
				return true;		
			
			if ( opX.getValue().equals("<=") && ( opY.getValue().equals("<")))
				return true;
			
			if ( opX.getValue().equals(">") && ( opY.getValue().equals(">") || opY.getValue().equals(">=") || opY.getValue().equals("==")))
				return true;
			
			if ( opX.getValue().equals(">=") && ( opY.getValue().equals(">")))
				return true;
			
			if ( opX.getValue().equals("==") && ( opY.getValue().equals("!=") || opY.getValue().equals(">")|| opY.getValue().equals("<")))
				return true;
			
			if ( opX.getValue().equals("!=") && ( opY.getValue().equals("==")))
				return true;	
		}
		
				
		return false;
	}
	
	public boolean isUnsolvable(){
		
		CodeElement leftX = this.getLeftSide();
		CodeElement rightX = this.getRightSide();
		CodeElement opX = this.getOperator();
		
		if (leftX==null || rightX==null || opX==null)
			return false;
		
		
		if (leftX.getValue().equals(rightX.getValue())){
			if (opX.getValue().equals("!="))
				return true;
		}
		
		if (!leftX.getValue().equals(rightX.getValue())){
			if (leftX instanceof Constant && rightX instanceof Constant){		
				if (opX.getValue().equals("=="))
				    return true;
				
				if (opX.getValue().equals(">")){
					try{
						float lft = Float.valueOf(leftX.getValue());
						float rt = Float.valueOf(rightX.getValue());
						if (lft<rt)
							return true;
					}
					catch(Exception e){
						
					}
				}
				if (opX.getValue().equals("<")){
					try{
						float lft = Float.valueOf(leftX.getValue());
						float rt = Float.valueOf(rightX.getValue());
						if (lft>rt)
							return true;
					}
					catch(Exception e){
						
					}
				}
			}
		}
		
		
		if (leftX instanceof Constant){
			if (!leftX.getValue().equals("null")){
				if (opX.getValue().equals("==")){
					if (rightX!=null){
						if (rightX instanceof Constant){
							if (rightX.getValue().equals("null"))
								return true;
						}
					}
				}
			}
		}
		
		if (rightX instanceof Constant){
			if (!rightX.getValue().equals("null")){
				if (opX.getValue().equals("==")){
					if (leftX!=null){
						if (leftX instanceof Constant){
							if (leftX.getValue().equals("null"))
								return true;
						}
					}
				}
			}
		}
		

		return false;
	}
	
	public boolean isNonLinearExpression(CodeElement expression){
		if (expression!=null){
			
			if (expression instanceof ArithmeticExpression){
				ArithmeticExpression aexpression = (ArithmeticExpression) expression;
				if (aexpression.isNonLinear()){
					//System.out.println("Non-linear expression: " + aexpression);
					return true;
				}
			}
			else
			{
				if (expression instanceof ArrayElement){
					ArrayElement aelement = (ArrayElement) expression;
					if (isNonLinearExpression(aelement.getIndex()))
						return true;
				}
				
				if (expression instanceof MethodCall){
					MethodCall mcall = (MethodCall) expression;
					
					if (mcall.isNonLinear()){
						//System.out.println("Non-linear call: " + mcall);
						return true;
					}
					
					for (CodeElement parameter: mcall.getParameters())
						if (isNonLinearExpression(parameter))
							return true;
				}
				
			}
			
		}
		
		return false;
	}
	
	public boolean hasNonLinearExpression(){
		
		if (isNonLinearExpression(leftSide)||(isNonLinearExpression(rightSide))){
			return true;
		}
		
		return false;
	}

}

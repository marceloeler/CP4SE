package profiling.constraint.analysis;

import java.util.Vector;

import profiling.constraint.analysis.stack.ArithmeticExpression;
import profiling.constraint.analysis.stack.ArrayElement;
import profiling.constraint.analysis.stack.CodeElement;
import profiling.constraint.analysis.stack.MethodCall;
import profiling.constraint.analysis.stack.Variable;
import profiling.constraint.graph.CFG;
import profiling.constraint.graph.Edge;
import profiling.constraint.graph.Node;
import profiling.constraint.symbolic.AbstractUpdate;
import profiling.constraint.symbolic.AssignmentUpdate;
import profiling.constraint.symbolic.ByThirdPartyUpdate;

public class CopyOfConstraintSequenceGenerator {
	
	private String currentConstraint="";
	
	public Vector<Constraint> getConstraintSequenceElements(CFG cfg, Path path){
		Vector<Constraint> constraints = new Vector<Constraint> ();  		
		if (path!=null){
			Node prevNode = null;
			int i=0;
			while (i<path.getNodes().size()){
				Node node = path.getNodes().get(i);
				if (prevNode!=null){
					String stedge = prevNode.getId()+"-"+node.getId();
					Edge edge = cfg.getEdge(stedge);

					if (edge.isConstrained()){
					//	System.out.println();
					//	System.out.println();
					//	System.out.println(" EDGE: " + edge.getStrEdge());
						Constraint cnstr = edge.getConstraint();
					//	System.out.println("###CONSTRAINT: " + cnstr);
						if (edge.isFalseBranch()){

						}						
						constraints.add(replaceConstraint(path,i-1,cnstr));
					}
						
					else
						if (edge.isMultConstrained()){
							for (Constraint constraint: edge.getConstraints())
								constraints.add(replaceConstraint(path,i-1,constraint));
						}
				}												
				prevNode = node;
				i++;
			}
														
		}
				
		return constraints;
	}
	
	public Constraint replaceConstraint(Path path, int currentPosition, Constraint constraint){
		currentConstraint = constraint.toString();
		if (constraint.toString().equals("p.x<left")){			
			System.out.println("Constraint to be solved: " + constraint);
		}
		constraint.originalConstraint = constraint.toString();
		Constraint constraintCopy = constraint.copy();
		if (constraint.isException())
			return constraintCopy;
		
		int i=currentPosition;
		Vector<AbstractUpdate> updates = new Vector<AbstractUpdate>();
		for (i=0; i<=currentPosition; i++)
		{
			Node node = path.getNodes().get(i);
		//	System.out.println("NODE: " + node.getId());
			updates.addAll(node.getVarUpdates());
		}
	
		CodeElement leftSide = constraintCopy.getLeftSide();
		
		CodeElement rightSide = constraintCopy.getRightSide();

		leftSide = this.replaceExpression(leftSide, updates).copy();

		rightSide = this.replaceExpression(rightSide, updates).copy();
		constraintCopy.setLeftSide(leftSide);
		constraintCopy.setRightSide(rightSide);
						
		return constraintCopy;
	}	
	
	public CodeElement replaceExpression(CodeElement expression, Vector<AbstractUpdate> updates){
	//	System.out.println();
		//System.out.println("Expression to be replaced: " + expression);
		if (needsToPrint()){
			System.out.println("  Expression to be replaced: " + expression);
		}
		CodeElement newExpression = expression.copy();
		CodeElement cpExpression = expression.copy();
		for (int i = updates.size()-1; i>=0; i--){
			AbstractUpdate update = updates.get(i);			
			
			if (update instanceof AssignmentUpdate){
		//		System.out.println("  Update "+i+": "+update + "                      class: " + update.getClass());
				newExpression.setVisited(false);
				cpExpression.setVisited(false);
				newExpression = applyUpdate(cpExpression, update);
				cpExpression = newExpression.copy();
			}						
		}
		
		
		return newExpression;
	}
	
	public CodeElement applyUpdateOnVariable(Variable var, AbstractUpdate update){		
		CodeElement object = var.getObject();
		if (object!=null){
			if (object instanceof Variable){																	
				CodeElement newObject = replaceVariable((Variable) object, update).copy();					
				var.setObject(newObject);												
			}
			else
			{				
				//	System.out.println("     Objct not var: " + object + "  class: " + object.getClass());
				if (object instanceof MethodCall){
					CodeElement newObject = this.applyUpdateOnMethodCall((MethodCall)object, update);					
					var.setObject(newObject);
				}																								
			}
		}		
		CodeElement replacement = replaceVariable(var, update);
		return replacement;
	}
	
	public CodeElement applyUpdateOnMethodCall(MethodCall mc, AbstractUpdate update){
		CodeElement object = mc.getObject();	
		
		while (object!=null && object instanceof MethodCall){
			mc = (MethodCall) object;			
			object = mc.getObject();
		}
								
		
		
		if (object instanceof Variable){		
			
			object = applyUpdateOnVariable((Variable) object, update);
		
			mc.setObject(object);
		}
		
		
		for (int x=0; x<mc.getParameters().size(); x++){
			CodeElement el = mc.getParameters().get(x);
			
			if (el instanceof Variable){			
				
				el = applyUpdateOnVariable((Variable) el,update).copy();
				mc.getParameters().setElementAt(el, x);						
			}
			else
			{												
				el = applyUpdate(el,update);		
				mc.getParameters().setElementAt(el, x);	
			}
		}
		
		return mc;
			
	}
			
	
	public CodeElement applyUpdate(CodeElement expression, AbstractUpdate update){	
	//	System.out.println(" >> Apply update: " + expression);
				
		String before = expression.toString();
		if (expression.visited==true){
			
			if (needsToPrint())
			{
				System.out.println("    visited: " + expression);
			}
			
			if (expression instanceof Variable){
				Variable var = (Variable) expression;
				if (var.getObject()!=null && var.getObject().visited==false)
				{
					expression = applyUpdateOnVariable(var, update);					
				}
			}
			
			if (expression instanceof MethodCall){
				
				//System.out.println("CPT: " + expression);
				MethodCall mc = (MethodCall) expression;
				//expression = applyUpdateOnMethodCall(mc, update);
				
				CodeElement object = mc.getObject();
				
				while (object!=null && object instanceof MethodCall){
					mc = (MethodCall) object;
					object = mc.getObject();
				}
				
				if (object instanceof Variable){
					//System.out.println("  apply update on "+object);
					//System.out.println("  Update da vez: " +update);
					object = applyUpdateOnVariable((Variable) object, update);
					//System.out.println("  updated: " + object);
					mc.setObject(object);
					expression = mc;
				}
			}
			
			return expression;
		}
	//	else
		//	expression.visited=true; //só visitar quando houver troca mesmo
				
		if (expression instanceof Variable){						
			
			expression = applyUpdateOnVariable((Variable) expression, update).copy();			
		}
		else
		{
			if (expression instanceof MethodCall){									
			//	System.out.println(" MC: " + expression);
				
				MethodCall mc = (MethodCall) expression;
				//expression = applyUpdateOnMethodCall(mc, update);
				
				CodeElement object = mc.getObject();
				
				while (object instanceof MethodCall){
					mc = (MethodCall) object;
					object = mc.getObject();
				}
								
				
				if (object instanceof Variable){		
					
					object = replaceVariable((Variable)object, update);
				
					mc.setObject(object);
				}
				
				for (int x=0; x<mc.getParameters().size(); x++){
					CodeElement el = mc.getParameters().get(x);
					//if (expression.toString().contains("getOffset"))
				//		System.out.println("   Parameter: " + el + "  type: "+ el.getClass());
					if (el instanceof Variable){			
						
						el = applyUpdateOnVariable((Variable) el,update).copy();
						mc.getParameters().setElementAt(el, x);						
					}
					else
					{												
						el = applyUpdate(el,update);						
					}
				}
			}
			else
			{
				if (expression instanceof ArrayElement){					
					ArrayElement ae = (ArrayElement) expression;
					CodeElement index = ae.getIndex();
					if (index instanceof Variable)
					{						
						index = applyUpdateOnVariable((Variable) index, update).copy();																					
					}
					else
					{						
						index = applyUpdate(index,update);
					}
					
				}
				else
				{
					if (expression instanceof ArithmeticExpression){	
						//System.out.println("  Arithmetic expression");
						ArithmeticExpression ae = (ArithmeticExpression) expression;						
						Vector<CodeElement> elements = ae.getExpressionElements();
						for (int x=0; x<elements.size(); x++){
							CodeElement el = elements.get(x);
							if (el instanceof Variable){								
																	
								el = applyUpdateOnVariable((Variable)el, update).copy();
								
								elements.setElementAt(el, x);
								ae.setParenthesis(true);
							}
							else
							{							
								el = applyUpdate(el,update);
							}
						}						
					}										
				}
			}
		}
		String after = expression.toString();
		if (!before.equals(after))
			expression.visited=true;
		return expression;
	}
	
	public CodeElement replaceVariable(Variable variable, AbstractUpdate update){					
		if (variable.getValue().equals(update.getVariableUpdated().getValue()))
		{
			CodeElement replacement = update.getUpdatedValue();
			
			//System.out.println("     Update var: " +variable + " using ::" + update);					
			variable.visited = true;
			replacement.visited = true;
			//replacement.setVisited(true);
			//variable.setVisited(true);
			//if (variable.toString().contains(("iter")))
				//	System.out.println("   VISITING "+variable);
	
			return replacement;
		}
		else
			return variable;
	}
	
	/*
	public void printExpressionDetails(CodeElement expression){
		if (expression==null)
			return;
		if (expression instanceof Variable){
			Variable var = (Variable) expression;
			CodeElement object = var.getObject();
			if (object!=null)
				printExpressionDetails(object);
			System.out.println(var.getValue()+": visited -"+var.visited);				
		}
		else
		{
			if (expression instanceof MethodCall){
				System.out.println("MC");
				MethodCall mc = (MethodCall) expression;
				CodeElement object = mc.getObject();
				
				while (object instanceof MethodCall){
					mc = (MethodCall) object;
					object = mc.getObject();
				}
												
				if (object instanceof Variable)		
					printExpressionDetails(object);
					
				System.out.println(mc.toString()+": visited - "+mc.visited);
				
				for (int x=0; x<mc.getParameters().size(); x++){
					CodeElement el = mc.getParameters().get(x);																	
					printExpressionDetails(el);																						
				}		
				
			}
			else
			{
				if (expression instanceof ArrayElement){					
					ArrayElement ae = (ArrayElement) expression;
					CodeElement index = ae.getIndex();
					System.out.println(ae.toString()+": visited - "+ae.visited);
					printExpressionDetails(index);					
				}
				else
				{
					if (expression instanceof ArithmeticExpression){							
						ArithmeticExpression ae = (ArithmeticExpression) expression;						
						Vector<CodeElement> elements = ae.getExpressionElements();
						System.out.println(ae.toString()+": visited - "+ae.visited);
						for (int x=0; x<elements.size(); x++){
							CodeElement el = elements.get(x);
							printExpressionDetails(el);
						}						
					}
					else
					{
						System.out.println(expression.toString()+": visited - "+expression.visited);
					}
				}
				
			}
		}
	}*/
	
	public boolean needsToPrint(){
		
		if (currentConstraint.equals("p.x<left"))
			return true;
		return false;
	}
	 
}

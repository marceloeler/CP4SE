package profiling.constraint.analysis;

import java.util.Vector;

import profiling.constraint.analysis.stack.ArithmeticExpression;
import profiling.constraint.analysis.stack.ArrayElement;
import profiling.constraint.analysis.stack.CodeElement;
import profiling.constraint.analysis.stack.MethodCall;
import profiling.constraint.analysis.stack.NewArray;
import profiling.constraint.analysis.stack.StaticClass;
import profiling.constraint.analysis.stack.Variable;
import profiling.constraint.graph.CFG;
import profiling.constraint.graph.Edge;
import profiling.constraint.graph.Node;
import profiling.constraint.project.Method;
import profiling.constraint.symbolic.AbstractUpdate;
import profiling.constraint.symbolic.AssignmentUpdate;

public class ConstraintSequenceGenerator {
				
	public static Path currentPath;
	
	public String currentConstraint = "";
	
	public Vector<Constraint> getConstraintSequenceElements(CFG cfg, Path path){		
		currentPath=path;
		//Method.currentMethod = cfg.getMethodName();
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
						Constraint cnstr = edge.getConstraint();
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
		//System.out.println("\nPath: " + path.toString());
		currentConstraint = constraint.toString();		
		constraint.originalConstraint = constraint.toString();
		Constraint constraintCopy = constraint.copy();
		if (constraint.isException())
			return constraintCopy;
		//System.out.println("\n\nUpdates: ");
		int i=currentPosition;
		Vector<AbstractUpdate> updates = new Vector<AbstractUpdate>();		
		for (i=0; i<=currentPosition; i++)
		{
			Node node = path.getNodes().get(i);			
			updates.addAll(node.getVarUpdates());
			for (int j=0; j<updates.size(); j++){
				AbstractUpdate up = updates.get(j);
				//if (up instanceof AssignmentUpdate)
					//System.out.println(updates.get(j));
			}
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
				
		CodeElement newExpression = expression.copy();
		CodeElement cpExpression = expression.copy();
		for (int i = updates.size()-1; i>=0; i--){		
			AbstractUpdate update = updates.get(i);			
			if (update instanceof AssignmentUpdate){	
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
				CodeElement newObject = replaceVariable((Variable) object, update);					
				var.setObject(newObject);												
			}
			else
			{				
				if (object instanceof MethodCall){
					CodeElement newObject = this.applyUpdateOnMethodCall((MethodCall)object, update);					
					var.setObject(newObject);
				}																								
			}
		}				
		CodeElement replacement = replaceVariable(var, update);		
		return replacement;
	}
	
	public CodeElement applyUpdateOnMethodCall(MethodCall cpmc, AbstractUpdate update){
		
		
		CodeElement object = cpmc.getObject();				
		CodeElement copyObject = object.copy();
		copyObject = applyUpdate(copyObject, update);
				
		cpmc.setObject(copyObject);
	
		for (int x=0; x<cpmc.getParameters().size(); x++){
			CodeElement el = cpmc.getParameters().get(x);				
			if (el instanceof Variable){			
				
				el = applyUpdateOnVariable((Variable) el,update);
				cpmc.getParameters().setElementAt(el, x);						
			}
			else
			{												
				el = applyUpdate(el,update);
				//mc.getParameters().setElementAt(el, x);	
			}
		}
			
		return cpmc;
			
	}
			
	
	public CodeElement applyUpdate(CodeElement cpexpression, AbstractUpdate update){
	
		String before = cpexpression.toString();
		if (cpexpression.visited==true){			
			
			if (cpexpression instanceof Variable){
				Variable var = (Variable) cpexpression;
				if (var.getObject()!=null && var.getObject().visited==false)
				{
					cpexpression = applyUpdateOnVariable(var, update).copy();					
				}
			}
			
			if (cpexpression instanceof MethodCall){				
				MethodCall mc = (MethodCall) cpexpression;
				CodeElement object = mc.getObject();
				
				while (object!=null && object instanceof MethodCall){
					mc = (MethodCall) object;
					object = mc.getObject();
				}
				
				if (object instanceof Variable){
					object = applyUpdateOnVariable((Variable) object, update).copy();
					mc.setObject(object);
					cpexpression = mc;
				}
			}
			
			return cpexpression;
		}
				
		if (cpexpression instanceof Variable){			
			CodeElement copy = cpexpression.copy();
			copy = applyUpdateOnVariable((Variable) copy, update).copy();	
			return copy;
		}
		else
		{		
		
			if (cpexpression instanceof MethodCall){
				CodeElement cpExpression = cpexpression.copy();				
				MethodCall mc = (MethodCall) cpexpression;
				cpexpression = applyUpdateOnMethodCall(mc, update).copy();				
			}
			else
			{
				if (cpexpression instanceof ArrayElement){	
					CodeElement cp = cpexpression.copy();
					
					ArrayElement ae = (ArrayElement) cpexpression;					
					
					boolean changed = false;					
					cpexpression = replaceArrayElement(ae, update);
					if (!cpexpression.toString().equals(cp.toString()))
						changed = true;
					
					if (!changed)
					{					
						CodeElement index = ae.getIndex();
						if (index instanceof Variable)
						{	
							CodeElement cpIndex = index.copy();
							cpIndex = applyUpdateOnVariable((Variable) index, update);
							ae.setIndex(cpIndex);																		
						}
						else
						{						
							index = applyUpdate(index,update);
							ae.setIndex(index);
						}						
												
						CodeElement var = ae.getVar();
						var = applyUpdate(var,update);
						ae.setVar(var);			
					}																	
				}
				else
				{
					if (cpexpression instanceof ArithmeticExpression){	
						ArithmeticExpression ae = (ArithmeticExpression) cpexpression;						
						Vector<CodeElement> elements = ae.getExpressionElements();
						for (int x=0; x<elements.size(); x++){
							CodeElement el = elements.get(x);
							if (el instanceof Variable){								
														
								
								el = applyUpdateOnVariable((Variable)el, update);
								
								elements.setElementAt(el, x);
								ae.setParenthesis(true);
							}
							else
							{							
								
								el = applyUpdate(el,update);
								elements.setElementAt(el, x);
								ae.setParenthesis(true);
							}
						}						
					}
					else
					{
						if (cpexpression instanceof StaticClass){					
							
							CodeElement copy = cpexpression.copy();
							
							copy = replaceStaticClass(copy,update);
							
							return copy;
						}
						else
						{
							if (cpexpression instanceof NewArray){
								NewArray newArray = (NewArray) cpexpression;
								Vector<CodeElement> sizes = newArray.getSize();
															
								for (int i=0; i<sizes.size(); i++){
									CodeElement size = sizes.get(i);
									size = applyUpdate(size,update);
									sizes.set(i,size);
								}
								
								newArray.setSize(sizes);
								cpexpression = (CodeElement) newArray;
							}
						}						
					}
				}
			}
		}
		String after = cpexpression.toString();
		if (!before.equals(after))
			cpexpression.visited=true;								
		return cpexpression;
	}
	
	public CodeElement replaceStaticClass(CodeElement expression, AbstractUpdate update){
		CodeElement replacement = expression;
		
		String op1 = expression.getValue();
		String op2 = update.getVariableUpdated().getValue();
		
		
		if (op1.equals(op2)){		
			replacement = update.getUpdatedValue();
			expression.visited= true;
			replacement.visited=true;
		}
		
		return replacement;		
	}
	
	public CodeElement replaceVariable(Variable cpvariable, AbstractUpdate update){				
				
		Variable variable = (Variable) cpvariable.copy();
		String op1 = variable.getValue();
		String op2 = update.getVariableUpdated().getValue();		

		if (op1.equals(op2))		
		{							
			CodeElement replacement = update.getUpdatedValue();			
			variable.visited = true;
			replacement.visited = true;
			return replacement;						
		}
		else
		{		
			return variable;
		}
	}
	
	public CodeElement replaceArrayElement(ArrayElement arrayElement, AbstractUpdate update){						
		String op1 = arrayElement.getValue();
		String op2 = update.getVariableUpdated().getValue();
			
		if (op1.equals(op2))		
		{								
			CodeElement replacement = update.getUpdatedValue();
			arrayElement.visited = true;
			replacement.visited = true;		
			return replacement;
		}
		else
		{		
			return arrayElement;
		}
	}
	
		
	public boolean needsToPrint(){	
			return false;
	}
	 
}
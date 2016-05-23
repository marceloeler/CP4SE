package profiling.constraint.bytecode.instructionhandler;

import org.apache.bcel.generic.IINC;
import org.apache.bcel.generic.Instruction;

import profiling.constraint.analysis.stack.ArithmeticExpression;
import profiling.constraint.analysis.stack.ArithmeticOperator;
import profiling.constraint.analysis.stack.Assignment;
import profiling.constraint.analysis.stack.CodeElement;
import profiling.constraint.analysis.stack.Constant;
import profiling.constraint.analysis.stack.Variable;
import profiling.constraint.bytecode.CFGBuilder;
import profiling.constraint.graph.CFG;
import profiling.constraint.graph.Node;
import profiling.constraint.symbolic.AssignmentUpdate;

public class IINCHandler  extends AbstractHandler{

	@Override
	public void handle(Instruction currentInstruction, Node currentNode, CFG cfg) {
		
		IINC instruction = (IINC) currentInstruction;
		int varIndex = instruction.getIndex();
		String varName = this.getVariableName(varIndex);			
			
		int increment = instruction.getIncrement();
		String operator="";
		if (increment>0)			
			operator="+";
		else
			operator="-";

		//currentNode.addAssignment(varName, expression);
		
		Variable var = cfg.getVariable(varName);
		if (var==null){
			var = new Variable(varName);
			var.setType("int");
			cfg.addVariable(var);
		}							
		
		//PORÇÃO PARALELA PARA CONTROLAR PILHA POR MEIO DE ELEMENTOS AO INVÉS DE STRING
		
		if (!CFGBuilder.elementStack.isEmpty() &&  CFGBuilder.elementStack.peek()!=null){ //se tem algo na pilha			
			//se a variável carregada na pilha é a mesma do incremento, então é do tipo x++ (carrega antes e depois incrementa)				
			if (CFGBuilder.elementStack.peek().getValue().equals(varName)){				
					CodeElement varEl = CFGBuilder.elementStack.pop();
					ArithmeticExpression varExp = new ArithmeticExpression();
					ArithmeticOperator aritOp = null;
					if (operator.equals("+"))
						aritOp = new ArithmeticOperator("-");						
					else
						aritOp = new ArithmeticOperator("+");
					varExp.addElement(var);
					varExp.addElement(aritOp);
					Constant c1 = new Constant("1");
					c1.setType("int");
					varExp.addElement(c1);
					
					CFGBuilder.elementStack.push(varExp);					
					//System.out.println("CP1: "+var);
				}			
		}
		
		
		ArithmeticExpression aritExpr = new ArithmeticExpression();
		aritExpr.addElement(var);
		aritExpr.addElement(new ArithmeticOperator(operator));
		Constant nc = new Constant(String.valueOf(increment));
		nc.setType("int");
		aritExpr.addElement(nc);
		Assignment assignment = new Assignment(var,aritExpr);
		
		//adiciona update da variável ao nó
		AssignmentUpdate update = new AssignmentUpdate(assignment);				
		currentNode.addVarUpdate(update);				
	}

}

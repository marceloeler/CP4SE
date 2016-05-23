package profiling.constraint.bytecode.instructionhandler;

import org.apache.bcel.generic.ArithmeticInstruction;
import org.apache.bcel.generic.DNEG;
import org.apache.bcel.generic.FNEG;
import org.apache.bcel.generic.INEG;
import org.apache.bcel.generic.Instruction;
import org.apache.bcel.generic.LNEG;

import profiling.constraint.analysis.stack.ArithmeticExpression;
import profiling.constraint.analysis.stack.ArithmeticOperator;
import profiling.constraint.analysis.stack.CodeElement;
import profiling.constraint.analysis.stack.Constant;
import profiling.constraint.bytecode.BytecodeUtils;
import profiling.constraint.bytecode.CFGBuilder;
import profiling.constraint.graph.CFG;
import profiling.constraint.graph.Node;

public class ArithmeticHandler extends AbstractHandler {

	@Override
	public void handle(Instruction currentInstruction, Node currentNode, CFG cfg) {
		ArithmeticInstruction ainstr = (ArithmeticInstruction) currentInstruction;
		
		String opName = ainstr.getName();
		String operator ="";
		
		if (opName.contains("mul"))
			operator="*";
		if (opName.contains("div"))
			operator="/";
		if (opName.contains("add"))
			operator="+";
		if (opName.contains("sub"))
			operator="-";
		if (opName.contains("rem"))
			operator = "%";
		
		if (opName.contains("and")){
			operator = "&";
		}
		
		if (opName.contains("or"))
			operator="|";
		
		if (opName.contains("xor"))
			operator="^";
		
		if (opName.contains("shl"))
			operator="<<";
		
		if (opName.contains("shr"))
			operator=">>";
		
		//if (operator.equals(""))
			//System.out.println("Operator: " + opName);
		
		//PORÇÃO PARALELA PARA CONTROLAR PILHA POR MEIO DE ELEMENTOS AO INVÉS DE STRING
		CodeElement e1 = CFGBuilder.elementStack.pop();
		CodeElement e2 = null;
		
		if (BytecodeUtils.isNEG(ainstr)){
			e2 = new Constant("0"); 		
			e2.setType("int");
			operator = "-";
		} 
		else
		{
			e2=CFGBuilder.elementStack.pop();		
		}
		
		ArithmeticOperator aritOp = new ArithmeticOperator(operator);
		ArithmeticExpression expr = new ArithmeticExpression();
		expr.addElement(e2);
		expr.addElement(aritOp);
		expr.addElement(e1);
		
		CFGBuilder.elementStack.push(expr);
	}
	

}

package profiling.constraint.bytecode.instructionhandler;

import org.apache.bcel.generic.Instruction;
import org.apache.bcel.generic.StoreInstruction;

import profiling.constraint.analysis.stack.Assignment;
import profiling.constraint.analysis.stack.CodeElement;
import profiling.constraint.analysis.stack.Variable;
import profiling.constraint.bytecode.CFGBuilder;
import profiling.constraint.graph.CFG;
import profiling.constraint.graph.Node;
import profiling.constraint.symbolic.AssignmentUpdate;

public class StoreHandler extends AbstractHandler {

	@Override
	public void handle(Instruction currentInstruction, Node currentNode, CFG cfg) {		
		StoreInstruction instruction = (StoreInstruction) currentInstruction;
		int indexVariable = instruction.getIndex();
		String varName = this.getVariableName(indexVariable);
		String varType = this.getVariableType(indexVariable);

		Variable var = cfg.getVariable(varName);			
		if (var==null){
			var = new Variable(varName);
			var.setLocal(true);
			var.setVarType(varType);
			var.setType(varType);
			cfg.addVariable(var);
		}
		
		
		//PORÇÃO PARALELA PARA CONTROLAR PILHA POR MEIO DE ELEMENTOS AO INVÉS DE STRING
		CodeElement expEl = null;
		if (CFGBuilder.elementStack.size()>0)
			expEl = CFGBuilder.elementStack.pop();
			
		if (expEl!=null){
			Assignment assignment = new Assignment(var,expEl);						
			AssignmentUpdate update = new AssignmentUpdate(assignment);		
			currentNode.addVarUpdate(update);
			if (cfg.getMethodName().equals("refresh")&& cfg.getClassName().contains("ColumnSubscriptionNew")){
				//CFGBuilder.showElementStack(); 
				//System.out.println("Instruction "+CFGBuilder.currentIh+": pop "+ expEl);
				//CFGBuilder.showElementStack();
			 }
		}
			

	}

}

package profiling.constraint.bytecode.instructionhandler;

import org.apache.bcel.generic.ALOAD;
import org.apache.bcel.generic.Instruction;
import org.apache.bcel.generic.LoadInstruction;

import profiling.constraint.analysis.stack.Variable;
import profiling.constraint.bytecode.CFGBuilder;
import profiling.constraint.graph.CFG;
import profiling.constraint.graph.Node;

public class LoadHandler extends AbstractHandler {

	@Override
	public void handle(Instruction currentInstruction, Node currentNode, CFG cfg) {
		
		LoadInstruction instruction = (LoadInstruction) currentInstruction;
		int indexVariable = instruction.getIndex();
		String varName = this.getVariableName(indexVariable);			
		String varType = instruction.getType(CFGBuilder.cp).getSignature();
		
		//PORÇÃO PARALELA PARA CONTROLAR PILHA POR MEIO DE ELEMENTOS AO INVÉS DE STRING
		Variable var = cfg.getVariable(varName);
		if (var==null){
			var = new Variable(varName);
			var.setLocal(true);
			var.setVarType(varType);
			var.setType(varType);
			cfg.addVariable(var);
		}
		if (currentInstruction instanceof ALOAD){			
			var.setObject(true);
		}
		CFGBuilder.elementStack.push(var);
		
	}

}

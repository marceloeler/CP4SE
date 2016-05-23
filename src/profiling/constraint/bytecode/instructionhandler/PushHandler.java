package profiling.constraint.bytecode.instructionhandler;

import org.apache.bcel.generic.BIPUSH;
import org.apache.bcel.generic.Instruction;
import org.apache.bcel.generic.SIPUSH;

import profiling.constraint.analysis.stack.Constant;
import profiling.constraint.bytecode.CFGBuilder;
import profiling.constraint.graph.CFG;
import profiling.constraint.graph.Node;

public class PushHandler extends AbstractHandler {

	@Override
	public void handle(Instruction currentInstruction, Node currentNode, CFG cfg) {
		String constant = "";
		String type = "";
		if (currentInstruction instanceof BIPUSH){
			BIPUSH loadInstr = (BIPUSH) currentInstruction;
			constant = String.valueOf(loadInstr.getValue());				
			type = loadInstr.getType(CFGBuilder.cp).getSignature();
		}
		else{
			if (currentInstruction instanceof SIPUSH){
				SIPUSH loadInstr = (SIPUSH) currentInstruction;
				constant = String.valueOf(loadInstr.getValue());				
				type = loadInstr.getType(CFGBuilder.cp).getSignature();
			}
		}
		
		
		//PORÇÃO PARALELA PARA CONTROLAR PILHA POR MEIO DE ELEMENTOS AO INVÉS DE STRING
		Constant cnst = new Constant(constant);
		cnst.setType(type);
		CFGBuilder.elementStack.push(cnst);

	}

}

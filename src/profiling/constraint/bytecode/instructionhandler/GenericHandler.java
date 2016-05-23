package profiling.constraint.bytecode.instructionhandler;

import org.apache.bcel.generic.Instruction;
import org.apache.bcel.generic.SIPUSH;

import profiling.constraint.analysis.stack.Constant;
import profiling.constraint.bytecode.CFGBuilder;
import profiling.constraint.graph.CFG;
import profiling.constraint.graph.Node;

public class GenericHandler extends AbstractHandler{

	@Override
	public void handle(Instruction instruction, Node currentNode, CFG cfg) {
		if (instruction instanceof SIPUSH){
			SIPUSH sipush = (SIPUSH) instruction;
			String value = String.valueOf(sipush.getValue());
			String type = sipush.getType(CFGBuilder.cp).getSignature();			
			
			//PORÇÃO PARALELA PARA CONTROLAR PILHA POR MEIO DE ELEMENTOS AO INVÉS DE STRING
			Constant constant = new Constant(value);
			constant.setType(type);
			CFGBuilder.elementStack.push(constant);
			
		}
		
	}
	
	

}

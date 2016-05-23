package profiling.constraint.bytecode.instructionhandler;

import org.apache.bcel.generic.Instruction;
import org.apache.bcel.generic.LDC;
import org.apache.bcel.generic.LDC2_W;
import org.apache.bcel.generic.LDC_W;

import profiling.constraint.analysis.stack.Constant;
import profiling.constraint.bytecode.CFGBuilder;
import profiling.constraint.graph.CFG;
import profiling.constraint.graph.Node;

public class LDCHandler extends AbstractHandler {
	
	@Override
	public void handle(Instruction currentInstruction, Node currentNode, CFG cfg) {
		String value="";
		String type = "";
		if (currentInstruction instanceof LDC){
			LDC instruction = (LDC) currentInstruction;
			type = instruction.getType(CFGBuilder.cp).getSignature();				
			value = String.valueOf(instruction.getValue(CFGBuilder.cp));	
		}
		else
		{
			if (currentInstruction instanceof LDC2_W){
				LDC2_W instruction = (LDC2_W) currentInstruction;
				type = instruction.getType(CFGBuilder.cp).getSignature();				
				value = String.valueOf(instruction.getValue(CFGBuilder.cp));	
			}
			if (currentInstruction instanceof LDC_W){
				LDC_W instruction = (LDC_W) currentInstruction;
				type = instruction.getType(CFGBuilder.cp).getSignature();				
				value = String.valueOf(instruction.getValue(CFGBuilder.cp));
			}
				
		}
		
		
		//System.out.println("CONSTANT TYPE: " + value+ " = "+type);
		//PORÇÃO PARALELA PARA CONTROLAR PILHA POR MEIO DE ELEMENTOS AO INVÉS DE STRING
		Constant constant = new Constant(value);
		constant.setType(type);
		CFGBuilder.elementStack.push(constant);
	}

}

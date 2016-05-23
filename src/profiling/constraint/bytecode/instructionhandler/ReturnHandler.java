package profiling.constraint.bytecode.instructionhandler;

import java.util.Vector;

import maintenance.Maintenance;

import org.apache.bcel.generic.DRETURN;
import org.apache.bcel.generic.FRETURN;
import org.apache.bcel.generic.IRETURN;
import org.apache.bcel.generic.Instruction;
import org.apache.bcel.generic.InvokeInstruction;
import org.apache.bcel.generic.LRETURN;
import org.apache.bcel.generic.RET;
import org.apache.bcel.generic.ReturnInstruction;

import profiling.constraint.analysis.stack.CodeElement;
import profiling.constraint.analysis.stack.Variable;
import profiling.constraint.bytecode.CFGBuilder;
import profiling.constraint.graph.CFG;
import profiling.constraint.graph.Node;

public class ReturnHandler extends AbstractHandler {

	@Override
	public void handle(Instruction currentInstruction, Node currentNode, CFG cfg) {
		
		if (currentInstruction instanceof RET){
			RET instruction = (RET) currentInstruction;
			currentNode.setHasReturn(true);
			boolean addValueReturn=false;
		}
		else
		{
			ReturnInstruction instruction = (ReturnInstruction) currentInstruction;
			
			currentNode.setHasReturn(true);
			boolean addValueReturn=false;
			
			/*
	    	//il.insert(currentIh,new NOP());
	    	if (!instruction.getType().toString().equals("void")){    	
				String returnArg = CFGBuilder.symbolicStack.pop();
				
				String type="object";
				if (instruction instanceof IRETURN)
					type="int";
				if (instruction instanceof LRETURN)
					type="long";
				if (instruction instanceof DRETURN)
					type="double";
				if (instruction instanceof FRETURN)
					type="float";
				
				addValueReturn=true;
			}    */	    	
					
			    	    
	    	//PORÇÃO PARALELA PARA CONTROLAR PILHA POR MEIO DE ELEMENTOS AO INVÉS DE STRING
	    	if (!instruction.getType().toString().equals("void")){  
	    		String type="object";
				if (instruction instanceof IRETURN)
					type="int";
				if (instruction instanceof LRETURN)
					type="long";
				if (instruction instanceof DRETURN)
					type="double";
				if (instruction instanceof FRETURN)
					type="float";
				
				Maintenance.gambiarra("return");
				//CodeElement returnEl = CFGBuilder.elementStack.pop();
				addValueReturn=true;
			}
		}
		
	}

}

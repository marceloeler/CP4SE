package profiling.constraint.bytecode.instructionhandler;

import org.apache.bcel.generic.Instruction;

import profiling.constraint.bytecode.CFGBuilder;
import profiling.constraint.graph.CFG;
import profiling.constraint.graph.Node;

public abstract class AbstractHandler {
	
	public abstract void handle(Instruction currentInstruction, Node currentNode, CFG cfg);
	
	protected String getVariableName(int index){
		String varName = "unknown_"+String.valueOf(index);
		if (CFGBuilder.localVariables!=null && CFGBuilder.localVariables.getLocalVariable(index)!=null){
			varName = CFGBuilder.localVariables.getLocalVariable(index).getName();			
		}
		return varName;
	}
	
	protected String getVariableType(int index){
		String type = "unknown_type";
		if (CFGBuilder.localVariables!=null && CFGBuilder.localVariables.getLocalVariable(index)!=null){
			type = CFGBuilder.localVariables.getLocalVariable(index).getSignature();			
		}
		return type;
	}
}

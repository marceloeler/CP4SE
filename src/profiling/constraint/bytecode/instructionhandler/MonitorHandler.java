package profiling.constraint.bytecode.instructionhandler;

import org.apache.bcel.generic.Instruction;

import profiling.constraint.analysis.stack.CodeElement;
import profiling.constraint.bytecode.CFGBuilder;
import profiling.constraint.graph.CFG;
import profiling.constraint.graph.Node;

public class MonitorHandler extends AbstractHandler {

	@Override
	public void handle(Instruction currentInstruction, Node currentNode, CFG cfg) {
		// TODO Auto-generated method stub
		CodeElement obj = CFGBuilder.elementStack.pop();
	}

}

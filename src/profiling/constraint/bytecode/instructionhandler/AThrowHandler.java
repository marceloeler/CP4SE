package profiling.constraint.bytecode.instructionhandler;

import org.apache.bcel.generic.ATHROW;
import org.apache.bcel.generic.DRETURN;
import org.apache.bcel.generic.FRETURN;
import org.apache.bcel.generic.IRETURN;
import org.apache.bcel.generic.Instruction;
import org.apache.bcel.generic.LRETURN;
import org.apache.bcel.generic.ReturnInstruction;

import profiling.constraint.bytecode.CFGBuilder;
import profiling.constraint.graph.CFG;
import profiling.constraint.graph.Node;

public class AThrowHandler extends AbstractHandler {

	@Override
	public void handle(Instruction currentInstruction, Node currentNode, CFG cfg) {
       ATHROW instruction = (ATHROW) currentInstruction;	   
      
    	   currentNode.setHasReturn(true);//nó final	
    	   currentNode.setException(true);
      
	}

}

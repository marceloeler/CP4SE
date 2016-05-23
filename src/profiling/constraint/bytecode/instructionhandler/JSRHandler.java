package profiling.constraint.bytecode.instructionhandler;

import org.apache.bcel.generic.Instruction;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.JSR;

import profiling.constraint.graph.CFG;
import profiling.constraint.graph.Edge;
import profiling.constraint.graph.Node;

public class JSRHandler extends AbstractHandler{

	@Override
	public void handle(Instruction currentInstruction, Node currentNode, CFG cfg) {

		JSR instruction = (JSR) currentInstruction;
		
		InstructionHandle tgtInstruction = instruction.getTarget();
		int tgtPosition = tgtInstruction.getPosition();
		
		Node nextNode = cfg.getNode(String.valueOf(tgtPosition));		
		if (nextNode==null){			
			
			nextNode = new Node();		
			nextNode.setFirstInstruction(tgtPosition);
			nextNode.setId(String.valueOf(tgtInstruction.getPosition()));			
			cfg.addNode(nextNode);
		}    		
				    		
		String stedge = String.valueOf(currentNode.getId())+"-"+String.valueOf(nextNode.getId());  		
		currentNode.addNeighbor(nextNode);    			
		cfg.addStedge(stedge);    			
		Edge edge = new Edge(currentNode, nextNode);		
		edge.setFalseBranch(false);
		cfg.addEdge(edge);			
	}
	
}

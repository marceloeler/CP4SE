package profiling.constraint.bytecode.instructionhandler;

import org.apache.bcel.generic.GOTO;
import org.apache.bcel.generic.Instruction;
import org.apache.bcel.generic.InstructionHandle;

import profiling.constraint.bytecode.CFGBuilder;
import profiling.constraint.graph.CFG;
import profiling.constraint.graph.Edge;
import profiling.constraint.graph.Node;

public class GotoHandler extends AbstractHandler {

	@Override
	public void handle(Instruction instruction, Node currentNode, CFG cfg) {
				
		currentNode.setHasGoto(true);    			    			
		GOTO gotoInstruction = (GOTO) instruction;
	    InstructionHandle tgtInstruction = gotoInstruction.getTarget();
	    int tgtPosition = tgtInstruction.getPosition();
	    
	    if (tgtPosition < CFGBuilder.currentIh.getPosition()) //se volta para o início das instruções então é um loop
		{
			//currentNode.setLoop(true);
			//if (cfg.getMethodName().equals("placeOrder")){
				Node nodeLoop = cfg.getNode(String.valueOf(tgtPosition));
				if (nodeLoop!=null){
					nodeLoop.setLoop(true);
					nodeLoop.setInfiniteLoop(true);
				}
					//System.out.println("Is loop: " + nodeLoop.getId());
			//}
		}
	    
	    Node tgtNode = cfg.getNode(String.valueOf(tgtPosition));
	    if (tgtNode == null){
	    	tgtNode = new Node();
	    	tgtNode.setFirstInstruction(tgtPosition);
	    	tgtNode.setId(String.valueOf(tgtInstruction.getPosition()));
	    	cfg.addNode(tgtNode);  	
	    }
	    
	  //tgtNode.setSequentialBranch(true);	 
    	if (tgtPosition < CFGBuilder.currentIh.getPosition()){
    		tgtNode.setLoop(true);
    		tgtNode.setInfiniteLoop(true);
    	}
	    
	    String stedge = String.valueOf(currentNode.getId())+"-"+String.valueOf(tgtNode.getId());
	   // System.out.println(edge);	    
		currentNode.addNeighbor(tgtNode);    			
		cfg.addStedge(stedge); 		
		Edge edge = new Edge(currentNode, tgtNode);
		cfg.addEdge(edge);
		//System.out.println("ADD EDGE: " + edge.getStrEdge());
		currentNode.setHasGoto(true);
		//System.out.println("Target node: " + tgtNode.getId());    

	}

}

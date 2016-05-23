package profiling.constraint.bytecode.instructionhandler;

import java.util.Hashtable;

import org.apache.bcel.generic.Instruction;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.LOOKUPSWITCH;
import org.apache.bcel.generic.TABLESWITCH;

import profiling.constraint.analysis.Constraint;
import profiling.constraint.analysis.stack.CodeElement;
import profiling.constraint.analysis.stack.Constant;
import profiling.constraint.analysis.stack.RelationalOperator;
import profiling.constraint.analysis.stack.Variable;
import profiling.constraint.bytecode.CFGBuilder;
import profiling.constraint.graph.CFG;
import profiling.constraint.graph.Edge;
import profiling.constraint.graph.Node;

public class SwitchHandler extends AbstractHandler {

	@Override
	public void handle(Instruction currentInstruction, Node currentNode, CFG cfg) {		
		Hashtable<String,String> switchConstraints = new Hashtable<String,String>();		
		
		InstructionHandle[] tgts=null;
		int ndTgts[]=null;
		
		
		CodeElement var = CFGBuilder.elementStack.pop();
		//String varName = CFGBuilder.symbolicStack.pop();
		//Variable var = cfg.getVariable(varName);
						
		if (currentInstruction instanceof TABLESWITCH){		
			TABLESWITCH instruction = (TABLESWITCH) currentInstruction;			
			tgts =  instruction.getTargets();			
			ndTgts = new int[tgts.length+1];			
			ndTgts[0] = instruction.getTarget().getPosition(); //default	
											
			int i;
			for (i=0; i<tgts.length; i++)
				ndTgts[i+1]=tgts[i].getPosition(); //tgts		
			
			int defaultIndice = instruction.getTarget().getPosition();		
			int[] matches = instruction.getMatchs();								
			
			for (int d=0; d<ndTgts.length; d++){
				if (ndTgts[d]!=defaultIndice){
					String strIndice = String.valueOf(ndTgts[d]);
					String strMatch = String.valueOf(matches[d-1]);
					if (switchConstraints.get(strIndice)==null){
						switchConstraints.put(strIndice, strMatch);
					}
				}			
			}

		}
		
		if (currentInstruction instanceof LOOKUPSWITCH)
		{
			LOOKUPSWITCH instruction = (LOOKUPSWITCH) currentInstruction;
			tgts =  instruction.getTargets();			
			ndTgts = new int[tgts.length+1];			
			ndTgts[0] = instruction.getTarget().getPosition(); //default	
			
			int i;
			for (i=0; i<tgts.length; i++)
				ndTgts[i+1]=tgts[i].getPosition(); //tgts		
			
			int defaultIndice = instruction.getTarget().getPosition();		
			int[] matches = instruction.getMatchs();								
			
			for (int d=0; d<ndTgts.length; d++){
				if (ndTgts[d]!=defaultIndice){
					String strIndice = String.valueOf(ndTgts[d]);
					String strMatch = String.valueOf(matches[d-1]);
					if (switchConstraints.get(strIndice)==null){
						switchConstraints.put(strIndice, strMatch);
					}
				}			
			}
			
		}
		
		int i;								
		//liga o nó do switch aos nós que são alvos de cada case e o default
		for (i=0; i<ndTgts.length; i++){
			String posStr = String.valueOf(ndTgts[i]);			
			Node nd = cfg.getNode(posStr);
			if (nd==null){ //se o nó não existe
				nd = new Node (posStr);
				nd.setFirstInstruction(ndTgts[i]);
				cfg.addNode(nd);
			}
			
			//cria aresta do nó do switch para o nó alvo									
			String stedge = String.valueOf(currentNode.getId())+"-"+String.valueOf(nd.getId());
			if (!cfg.getStedges().contains(stedge))
			{
				currentNode.addNeighbor(nd);    			
				cfg.addStedge(stedge);    			
				Edge edge = new Edge(currentNode, nd);
				cfg.addEdge(edge);
				
				String value = switchConstraints.get(nd.getId());
				if (value!=null){
					RelationalOperator operator = new RelationalOperator("==");
					Constant constant = new Constant(value);
					constant.setType("int");
					Constraint constraint = new Constraint(var,operator,constant, false);
					edge.setConstraint(constraint);
					edge.setConstrained(true);
				}
				else
				{
					edge.setMultConstrained(true);
					for (String key: switchConstraints.keySet()){
						value = switchConstraints.get(key);
						RelationalOperator operator = new RelationalOperator("!=");
						Constant constant = new Constant(value);
						constant.setType("int");
						Constraint constraint = new Constraint(var,operator,constant, false);
						edge.addMultConstraint(constraint);
					}
				}
			}			
		}						
	}

}

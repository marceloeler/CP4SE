package profiling.constraint.bytecode.instructionhandler;

import org.apache.bcel.generic.IFEQ;
import org.apache.bcel.generic.IFGE;
import org.apache.bcel.generic.IFGT;
import org.apache.bcel.generic.IFLE;
import org.apache.bcel.generic.IFLT;
import org.apache.bcel.generic.IFNE;
import org.apache.bcel.generic.IfInstruction;
import org.apache.bcel.generic.Instruction;
import org.apache.bcel.generic.InstructionHandle;

import profiling.constraint.analysis.Constraint;
import profiling.constraint.analysis.stack.CodeElement;
import profiling.constraint.analysis.stack.Constant;
import profiling.constraint.analysis.stack.MethodCall;
import profiling.constraint.analysis.stack.RelationalExpression;
import profiling.constraint.analysis.stack.RelationalOperator;
import profiling.constraint.bytecode.CFGBuilder;
import profiling.constraint.graph.CFG;
import profiling.constraint.graph.Edge;
import profiling.constraint.graph.Node;

public class IfHandler extends AbstractHandler{

	@Override
	public void handle(Instruction currentInstruction, Node currentNode, CFG cfg) {
		
		IfInstruction instruction = (IfInstruction) currentInstruction;
		
		currentNode.setHasConstraint(true);
		
		//Cria nó para condição falsa
		IfInstruction ifInst = (IfInstruction) instruction;
		InstructionHandle tgtInstruction = ifInst.getTarget();
		int tgtPosition = tgtInstruction.getPosition();
		if (cfg.getMethodName().equals("placeOrder")){
			//System.out.println("Tgt: " + tgtPosition + "   currentPosition: " + CFGBuilder.currentIh.getPosition());
		}
		if (tgtPosition < CFGBuilder.currentIh.getPosition()) //se volta para o início das instruções então é um loop
		{
			//Node nodeLoop = cfg.getNode(String.valueOf(tgtPosition));
			//if (nodeLoop!=null)
				//nodeLoop.setLoop(true);
			currentNode.setLoop(true);//qdo eh if, o noh loop eh o atual. diferente do goto, que eh o noh target
		}
				
		Node falseConditionNode = cfg.getNode(String.valueOf(tgtPosition));		
		if (falseConditionNode==null){			
			
			falseConditionNode = new Node();		
			falseConditionNode.setFirstInstruction(tgtPosition);
			falseConditionNode.setId(String.valueOf(tgtInstruction.getPosition()));			
			cfg.addNode(falseConditionNode);
		}    		
		
		    			
		String stedge = String.valueOf(currentNode.getId())+"-"+String.valueOf(falseConditionNode.getId());  		
		currentNode.addNeighbor(falseConditionNode);    			
		cfg.addStedge(stedge);    			
		Edge falseEdge = new Edge(currentNode, falseConditionNode);
		falseEdge.setFalseBranch(true);
		cfg.addEdge(falseEdge);
					
				
		InstructionHandle ih = CFGBuilder.currentIh;
		//Cria nó para condição verdadeira
		Node trueConditionNode = cfg.getNode(String.valueOf(ih.getNext().getPosition()));
		if (trueConditionNode==null){	
			trueConditionNode = new Node();		
			trueConditionNode.setFirstInstruction(ih.getNext().getPosition());
			trueConditionNode.setId(String.valueOf(ih.getNext().getPosition()));
			cfg.addNode(trueConditionNode);
		}
				
		stedge = String.valueOf(currentNode.getId())+"-"+String.valueOf(trueConditionNode.getId());		
		currentNode.addNeighbor(trueConditionNode);
		cfg.addStedge(stedge);    	
		Edge trueEdge = new Edge(currentNode, trueConditionNode);
		cfg.addEdge(trueEdge);
		
				
		currentNode.setHasConstraint(true);					
		
		
		Constraint constraint = null;	

		if (!CFGBuilder.xcmpInstruction){ //se não for uma comparação do tipo XCMP ... DCMP, FCMP, etc...
			CodeElement e1 = CFGBuilder.elementStack.pop();
			CodeElement e2 = null;
			
			String operator="";
			
			if (ifInst.getName().contains("eq"))//IF_ACMPEQ") ||ifInst.getName().contains("IF_ICMPEQ") || ifInst.getName().contains("IFEQ"))
				//as operações são invertidas. No bytecode a condição é para ir para o else
				operator="!=";
			
			if (ifInst.getName().contains("ne"))// || ifInst.getName().contains("IF_ICMPNE") || ifInst.getName().contains("IFNE"))
				//as operações são invertidas. No bytecode a condição é para ir para o else
				operator="==";
			
			if (ifInst.getName().contains("ge"))// || ifInst.getName().contains("IFGE"))
				//as operações são invertidas. No bytecode a condição é para ir para o else
				operator="<";
			
			if (ifInst.getName().contains("le"))// || ifInst.getName().contains("IFLE"))
				//as operações são invertidas. No bytecode a condição é para ir para o else
				operator=">";
			
			if (ifInst.getName().contains("lt"))// || ifInst.getName().contains("IFLT"))
				//as operações são invertidas. No bytecode a condição é para ir para o else
				operator=">=";
			
			if (ifInst.getName().contains("gt"))// || ifInst.getName().contains("IFGT"))
				//as operações são invertidas. No bytecode a condição é para ir para o else
				operator="<=";
			
			if (ifInst.getName().contains("nonnull"))
			{
				//as operações são invertidas. No bytecode a condição é para ir para o else
				operator="==";
				}
			else
			{
				  if (ifInst.getName().contains("null")){
						//as operações são invertidas. No bytecode a condição é para ir para o else
						  operator="!=";
					  }
			}
					
			
			if (!ifInst.getName().contains("null"))
			{
				if (CFGBuilder.elementStack.isEmpty()||instruction instanceof IFNE || instruction instanceof IFEQ || instruction instanceof IFGE || instruction instanceof IFLE || instruction instanceof IFGT || instruction instanceof IFLT)
				{
					//System.out.println("ZERO");
					e2=new Constant("0");
					e2.setType("int");
				}
				else
					e2=CFGBuilder.elementStack.pop();
			}
			else{
				e2=new Constant("null");
				e2.setType("null");
			}
			
			RelationalOperator relOperator = new RelationalOperator(operator);
						
				
			if (e2.getValue().equals("0"))
			{					
				if (e1 instanceof MethodCall)
				{
					MethodCall mc = (MethodCall) e1;
					if (mc.getType().contains("boolean")){
						//constraint = new Constraint(e1);
						constraint = new Constraint(e1,relOperator,e2, false);
					}
					else
						constraint = new Constraint(e1,relOperator,e2, false);	
						
				}
				else 
					constraint = new Constraint(e1,relOperator,e2, false);	
							
			}
			else
			{			
				constraint = new Constraint(e2,relOperator,e1, false);
			}
		}
		else
		{
			CodeElement element = CFGBuilder.elementStack.pop();
			if (element instanceof RelationalExpression){
				RelationalExpression relExpression = (RelationalExpression) element;
				constraint = new Constraint(relExpression.getLeftSide(), relExpression.getOperator(), relExpression.getRightSide(), false);
			}
		}
		
		
								
		currentNode.setHasConstraint(true);
		currentNode.setElementConstraint(constraint);
		
		trueEdge.setConstraint(constraint);
		trueEdge.setConstrained(true);
		falseEdge.setConstraint(constraint.negate());
		falseEdge.setConstrained(true);
				
		CFGBuilder.xcmpInstruction=false;
	}

	
}

package profiling.constraint.bytecode.instructionhandler;

import org.apache.bcel.generic.DUP;
import org.apache.bcel.generic.DUP2;
import org.apache.bcel.generic.DUP2_X1;
import org.apache.bcel.generic.DUP2_X2;
import org.apache.bcel.generic.DUP_X1;
import org.apache.bcel.generic.DUP_X2;
import org.apache.bcel.generic.Instruction;
import org.apache.bcel.generic.POP;
import org.apache.bcel.generic.POP2;
import org.apache.bcel.generic.SWAP;
import org.apache.bcel.generic.StackInstruction;

import profiling.constraint.analysis.stack.CodeElement;
import profiling.constraint.bytecode.CFGBuilder;
import profiling.constraint.graph.CFG;
import profiling.constraint.graph.Node;

public class StackHandler extends AbstractHandler{

	@Override
	public void handle(Instruction currentInstruction, Node currentNode, CFG cfg) {
		StackInstruction instruction = (StackInstruction) currentInstruction;
				
		//PORÇÃO PARALELA PARA CONTROLAR PILHA POR MEIO DE ELEMENTOS AO INVÉS DE STRING
		CodeElement e1, e2, e3, e4;
		
		//word -> ..., word, word
		if (instruction instanceof DUP){
			e1=CFGBuilder.elementStack.peek();
			//if (e1!=null)//gambiarra - precisa rever
			//System.out.println(e1);
			//if (e1!=null && !e1.getValue().contains("new")) //gambiarra - precisa rever
			CFGBuilder.elementStack.push(e1);			
		}
		
		//word2, word1 -> ..., word2, word1, word2, word1
		if (instruction instanceof DUP2){														
			e1=CFGBuilder.elementStack.pop();
			
			//o double e o long são colocados em 2 elementos na pilha
			//portanto, para duplicar, usa-se o DUP2
			//como a pilhas simbólica usa um único símbolo para representar um double ou long, não é necessário usar o DUP2, só colocar duas vezes o elemento na pilha
			
			if (CFGBuilder.elementStack.isEmpty()){//(e1.getType().equals("D")|| e1.getType().equals("double") || e1.getType().equals("J") || e1.getType().equals("long")){
				CFGBuilder.elementStack.push(e1);
				CFGBuilder.elementStack.push(e1);
			}
			else
			{
				if (CFGBuilder.elementStack.isEmpty())
				{
					//System.out.println("  Method: " + cfg.getMethodName());
					//System.out.println("  InstructioN: " + CFGBuilder.currentIh.getPosition());
				}
				e2=CFGBuilder.elementStack.pop();
				CFGBuilder.elementStack.push(e2);
				CFGBuilder.elementStack.push(e1);
				CFGBuilder.elementStack.push(e2);
				CFGBuilder.elementStack.push(e1);
			}									
		}
		
		//word2, word1 -> ..., word1, word2, word1
		if (instruction instanceof DUP_X1){
			e1=CFGBuilder.elementStack.pop();
			e2=null;
			if ((e1.getType().equals("D")|| e1.getType().equals("[D")||e1.getType().equals("D")||e1.getType().contains("double") || e1.getType().equals("J") || e1.getType().equals("long"))){
				
			}
			else 				
			{	
				if (CFGBuilder.elementStack.isEmpty())
					e2=e1;
				else
					e2=CFGBuilder.elementStack.pop();
			}
			CFGBuilder.elementStack.push(e1);
			if (e2!=null)
				CFGBuilder.elementStack.push(e2);			
			CFGBuilder.elementStack.push(e1);
		}
		
		//word3, word2, word1 -> ..., word1, word3, word2, word1
		if (instruction instanceof DUP_X2){
			e1=CFGBuilder.elementStack.pop();
			e2=null;
			if (e1.getType().equals("D")|| e1.getType().equals("double") ||  e1.getType().equals("J") || e1.getType().equals("long")){
				
			}
			else 				
				e2=CFGBuilder.elementStack.pop();
			e3=CFGBuilder.elementStack.pop();
			CFGBuilder.elementStack.push(e1);
			CFGBuilder.elementStack.push(e3);
			if (e2!=null)
				CFGBuilder.elementStack.push(e2);
			CFGBuilder.elementStack.push(e1);
		}
		
		//word3, word2, word1 -> ..., word2, word1, word3, word2, word1
		if (instruction instanceof DUP2_X1){
			e1=CFGBuilder.elementStack.pop();
			e2=null;
			if (e1.getType().equals("D")|| e1.getType().equals("double")  || e1.getType().equals("J") || e1.getType().equals("long")){
				//System.out.println("DOUBLE ON PEEK");
			}
			else 
				e2=CFGBuilder.elementStack.pop();
				
			if (CFGBuilder.elementStack.isEmpty())
			{
			//	System.out.println("  Method: " + cfg.getMethodName());
				//System.out.println("  InstructioN: " + CFGBuilder.currentIh.getPosition());
			}
		//	e3=CFGBuilder.elementStack.pop();
			
			
			if (e2!=null)
				CFGBuilder.elementStack.push(e2);
			CFGBuilder.elementStack.push(e1);
			//CFGBuilder.elementStack.push(e3);
			if (e2!=null)
				CFGBuilder.elementStack.push(e2);
			CFGBuilder.elementStack.push(e1);
		}
		
		//word4, word3, word2, word1 -> ..., word2, word1, word4, word3, word2, word1
		if (instruction instanceof DUP2_X2){
			e1=CFGBuilder.elementStack.pop();
			e2 = null;
			if (e1.getType().equals("D")|| e1.getType().equals("double") || e1.getType().equals("J") || e1.getType().equals("long")){
				
			}
			else
				e2=CFGBuilder.elementStack.pop();
			e3=CFGBuilder.elementStack.pop();
			e4=CFGBuilder.elementStack.pop();
			if (e2!=null)
				CFGBuilder.elementStack.push(e2);
			CFGBuilder.elementStack.push(e1);
			CFGBuilder.elementStack.push(e4);
			CFGBuilder.elementStack.push(e3);
			if (e2!=null)
				CFGBuilder.elementStack.push(e2);
			CFGBuilder.elementStack.push(e1);
		}
		
		//word2, word1 -> ..., word1, word2
		if (instruction instanceof SWAP){
			e1=CFGBuilder.elementStack.pop();
			e2=CFGBuilder.elementStack.pop();
			CFGBuilder.elementStack.push(e1);
			CFGBuilder.elementStack.push(e2);
		}
		
		//word -> ...
		if (instruction instanceof POP){
			//Quando faz o pop de uma chamada de método é porque o método não é void
			//armazena algo na pilha, mas como não é usado é dispensado - mostrar expressão porque pode alterar o estado de algum objeto
			if (!CFGBuilder.elementStack.isEmpty()){
				e1=CFGBuilder.elementStack.pop();
			}
			
			//System.out.println("Invoke expression: " + e1);
		}

		//word2, word1 -> ...
		if (instruction instanceof POP2){
			if (!CFGBuilder.elementStack.isEmpty()){
				e1=CFGBuilder.elementStack.pop();
			}
			if (!CFGBuilder.elementStack.isEmpty()){
				e1=CFGBuilder.elementStack.pop();
			}			
		}
	}

}

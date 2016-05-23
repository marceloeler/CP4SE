package profiling.constraint.bytecode.instructionhandler;

import java.util.Vector;

import maintenance.Maintenance;

import org.apache.bcel.generic.AASTORE;
import org.apache.bcel.generic.ANEWARRAY;
import org.apache.bcel.generic.ARRAYLENGTH;
import org.apache.bcel.generic.ArrayInstruction;
import org.apache.bcel.generic.BASTORE;
import org.apache.bcel.generic.CASTORE;
import org.apache.bcel.generic.DASTORE;
import org.apache.bcel.generic.FASTORE;
import org.apache.bcel.generic.IASTORE;
import org.apache.bcel.generic.Instruction;
import org.apache.bcel.generic.LASTORE;
import org.apache.bcel.generic.MULTIANEWARRAY;
import org.apache.bcel.generic.NEWARRAY;
import org.apache.bcel.generic.SASTORE;

import profiling.constraint.analysis.stack.ArrayAssignment;
import profiling.constraint.analysis.stack.ArrayElement;
import profiling.constraint.analysis.stack.ArrayVar;
import profiling.constraint.analysis.stack.Assignment;
import profiling.constraint.analysis.stack.CodeElement;
import profiling.constraint.analysis.stack.Constant;
import profiling.constraint.analysis.stack.MethodCall;
import profiling.constraint.analysis.stack.NewArray;
import profiling.constraint.analysis.stack.NewObject;
import profiling.constraint.analysis.stack.Variable;
import profiling.constraint.bytecode.CFGBuilder;
import profiling.constraint.graph.CFG;
import profiling.constraint.graph.Node;
import profiling.constraint.symbolic.AssignmentUpdate;

public class CopyOfArrayHandler extends AbstractHandler {

	@Override
	public void handle(Instruction currentInstruction, Node currentNode, CFG cfg) {
		System.out.println("ARRAY HANDLER");
		if (currentInstruction instanceof NEWARRAY){
			NEWARRAY newArray = (NEWARRAY) currentInstruction;												
			//PORÇÃO PARALELA PARA CONTROLAR PILHA POR MEIO DE ELEMENTOS AO INVÉS DE STRING
			
			String type = ((NEWARRAY) currentInstruction).getType().getSignature();
		//	System.out.println("type: " + type);
			CodeElement elSize = CFGBuilder.elementStack.pop();
			Vector<CodeElement> size = new Vector<CodeElement>();
			size.add(elSize);
			NewArray na = new NewArray(newArray.getType().toString(), size, type);
			na.setType(type);
			
			String arrayName = type+"_array_";
			ArrayVar arrayVar = new ArrayVar(arrayName,na);
			arrayVar.setArray(true);
			arrayVar.setType(type);
			//System.out.println("Array ref: " + na);
			//CFGBuilder.elementStack.push(arrayVar);
			CFGBuilder.elementStack.push(na);
		}
		else
		{
			if (currentInstruction instanceof MULTIANEWARRAY){
				MULTIANEWARRAY mnewArray = (MULTIANEWARRAY) currentInstruction;
				int dimensions = mnewArray.getDimensions();
				String type = mnewArray.getType(CFGBuilder.cp).getSignature();		
				
				Vector<CodeElement> size = new Vector<CodeElement>();
				for (int i=0; i<dimensions; i++){
					CodeElement elSize = CFGBuilder.elementStack.pop();
					size.add(elSize);
				}
				NewArray na = new NewArray(type, size, type);
				na.setType(type);;
				String arrayName = type+"_array_";
				ArrayVar arrayVar = new ArrayVar(arrayName,na);
				arrayVar.setArray(true);
				//System.out.println("Array ref: " + na);
				//CFGBuilder.elementStack.push(arrayVar);
				CFGBuilder.elementStack.push(na);
			}
			else
			{
				if (currentInstruction instanceof ANEWARRAY){
					ANEWARRAY anewArray = (ANEWARRAY) currentInstruction;									
					String type = anewArray.getType(CFGBuilder.cp).getSignature();		
					//PORÇÃO PARALELA PARA CONTROLAR PILHA POR MEIO DE ELEMENTOS AO INVÉS DE STRING
					
					CodeElement elSize = CFGBuilder.elementStack.pop();
					Vector<CodeElement> size = new Vector<CodeElement>();
					size.add(elSize);
					NewArray na = new NewArray(type, size, type);
					na.setType(type);
					String arrayName = type+"_array_";
					ArrayVar arrayVar = new ArrayVar(arrayName,na);
					arrayVar.setArray(true);

					CFGBuilder.elementStack.push(na);
				}
				else 
				{
					if (currentInstruction instanceof ARRAYLENGTH){
						ARRAYLENGTH instruction = (ARRAYLENGTH) currentInstruction;
						CodeElement array = CFGBuilder.elementStack.pop();												
						
						Variable fieldVar = new Variable("length");
						fieldVar.setType("int");
						fieldVar.setObject(false);
						fieldVar.setInstanceAttribute(true);
						fieldVar.setObject(array);
						fieldVar.setVarType("int");

						CFGBuilder.elementStack.push(fieldVar);										
					}
					else
					{
						//STORE
						ArrayInstruction instruction = (ArrayInstruction) currentInstruction;
						
						String type = instruction.getType(CFGBuilder.cp).getSignature();
						//				
						if (isStoreInstruction(currentInstruction)){			   																			
							
							//PORÇÃO PARALELA PARA CONTROLAR PILHA POR MEIO DE ELEMENTOS AO INVÉS DE STRING
							CodeElement evalue = CFGBuilder.elementStack.pop();
							CodeElement eindex = CFGBuilder.elementStack.pop();							
							CodeElement evar = CFGBuilder.elementStack.pop();
							if (evar instanceof Variable){
								Variable var = (Variable) evar;
								var.setArray(true);								
							}
							
							if (!(evar instanceof Variable || evar instanceof ArrayElement || evar instanceof MethodCall)){
								Maintenance.gambiarra("contornando situação para não ter que fazer análise da pilha conforme fluxo");
								evar = CFGBuilder.elementStack.pop();
							}											
							
							ArrayElement ae = new ArrayElement(evar, eindex);
							
							Assignment assignment = new Assignment(ae,evalue);
							AssignmentUpdate update = new AssignmentUpdate(assignment);		
							currentNode.addVarUpdate(update);						
						}
						else
						{
						//	LOAD	
							
							//System.out.println("  Instruction: " + CFGBuilder.currentIh.getPosition());
							CodeElement eindex = CFGBuilder.elementStack.pop();
							CodeElement evar = CFGBuilder.elementStack.pop();								
							ArrayElement arrayElement = new ArrayElement(evar,eindex);
							CFGBuilder.elementStack.push(arrayElement);								
						}
					}								
				}
			}
			
		}					
							
	}
	
	public boolean isStoreInstruction(Instruction instruction){
		if ((instruction instanceof IASTORE) ||
				(instruction instanceof AASTORE) ||
				(instruction instanceof BASTORE) ||
				(instruction instanceof CASTORE) ||
				(instruction instanceof DASTORE) ||
				(instruction instanceof FASTORE) ||
				(instruction instanceof LASTORE) ||
				(instruction instanceof SASTORE) )
			return true;
		return false;
	}

}

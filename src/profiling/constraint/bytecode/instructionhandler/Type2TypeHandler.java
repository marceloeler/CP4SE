package profiling.constraint.bytecode.instructionhandler;

import java.util.Vector;

import org.apache.bcel.generic.D2F;
import org.apache.bcel.generic.D2I;
import org.apache.bcel.generic.D2L;
import org.apache.bcel.generic.F2D;
import org.apache.bcel.generic.F2I;
import org.apache.bcel.generic.I2B;
import org.apache.bcel.generic.I2C;
import org.apache.bcel.generic.I2D;
import org.apache.bcel.generic.I2F;
import org.apache.bcel.generic.I2L;
import org.apache.bcel.generic.I2S;
import org.apache.bcel.generic.Instruction;
import org.apache.bcel.generic.L2D;
import org.apache.bcel.generic.L2F;
import org.apache.bcel.generic.L2I;

import profiling.constraint.analysis.stack.CodeElement;
import profiling.constraint.analysis.stack.MethodCall;
import profiling.constraint.analysis.stack.Variable;
import profiling.constraint.bytecode.CFGBuilder;
import profiling.constraint.graph.CFG;
import profiling.constraint.graph.Node;

public class Type2TypeHandler extends AbstractHandler {

	@Override
	public void handle(Instruction currentInstruction, Node currentNode, CFG cfg) {
		
		if (currentInstruction instanceof I2C){
			/*
			CodeElement element = CFGBuilder.elementStack.pop();
			MethodCall mcall = new MethodCall();
			mcall.setClassName("IntToChar");
			mcall.setReturnType("char");
			mcall.setType("char");
			Variable var = new Variable("Char");
			var.setType("Char");
			mcall.setObject(var);
			mcall.setMethodName("toChar");
			Vector<CodeElement> parameters = new Vector<CodeElement>();
			parameters.add(element);
			mcall.setIntraStaticMethodCall(true);
			mcall.setStaticCall(true);
			mcall.setParameters(parameters);
			CFGBuilder.elementStack.push(mcall);*/
		}
		
		if (currentInstruction instanceof D2F){
			
		}
		
		if (currentInstruction instanceof I2B){
			
		}
				
		if (currentInstruction instanceof D2I){
			
		}
		if (currentInstruction instanceof I2D){
			
		}
				
		if (currentInstruction instanceof L2D){
			
		}

		if (currentInstruction instanceof I2F){
			
		}
		
		if (currentInstruction instanceof D2L){
			
		}
		
		if (currentInstruction instanceof I2L){
													
		}
		
		if (currentInstruction instanceof L2F){
															
		}
		
		if (currentInstruction instanceof L2I){
																	
		}
		
		if (currentInstruction instanceof F2I){
																			
		}
		
		if (currentInstruction instanceof F2D){
																					
		}
		
		if (currentInstruction instanceof I2S){
																							
		}
		
		if (currentInstruction instanceof I2B){
			
		}
		
		
	}
}

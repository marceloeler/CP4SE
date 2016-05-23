package profiling.constraint.bytecode.instructionhandler;

import org.apache.bcel.generic.DCMPG;
import org.apache.bcel.generic.DCMPL;
import org.apache.bcel.generic.FCMPG;
import org.apache.bcel.generic.FCMPL;
import org.apache.bcel.generic.Instruction;
import org.apache.bcel.generic.LCMP;

import profiling.constraint.analysis.stack.CodeElement;
import profiling.constraint.analysis.stack.RelationalExpression;
import profiling.constraint.analysis.stack.RelationalOperator;
import profiling.constraint.bytecode.CFGBuilder;
import profiling.constraint.graph.CFG;
import profiling.constraint.graph.Node;

public class XCMPHandler extends AbstractHandler{

	@Override
	public void handle(Instruction currentInstruction, Node currentNode, CFG cfg) {
		//System.out.println("Stack: ");
		//CFGBuilder.showElementStack();
		
		CodeElement e2 = CFGBuilder.elementStack.pop();
		CodeElement e1 = CFGBuilder.elementStack.pop();		
		RelationalOperator operator = null;
		if (currentInstruction instanceof DCMPL || currentInstruction instanceof DCMPG){
			if (currentInstruction instanceof DCMPL){
				DCMPL instruction = (DCMPL) currentInstruction;
				operator = new RelationalOperator("<");			
			}
			else
			{			
				operator = new RelationalOperator(">");
				DCMPG instruction = (DCMPG) currentInstruction;
			}	
		}
		
		if (currentInstruction instanceof FCMPL || currentInstruction instanceof FCMPG){
			if (currentInstruction instanceof FCMPL){
				FCMPL instruction = (FCMPL) currentInstruction;
				operator = new RelationalOperator("<");			
			}
			else
			{			
				operator = new RelationalOperator(">");
				FCMPG instruction = (FCMPG) currentInstruction;
			}	
		}
		
		if (currentInstruction instanceof LCMP){
			LCMP instruction = (LCMP) currentInstruction;
			operator = new RelationalOperator ("=");
		}
		
		RelationalExpression relExpression = new RelationalExpression(e1,operator,e2);
		
		CFGBuilder.xcmpInstruction=true;
		
		CFGBuilder.elementStack.push(relExpression);
		
	}
	

}

package profiling.constraint.bytecode.instructionhandler;

import java.util.Vector;

import org.apache.bcel.generic.INSTANCEOF;
import org.apache.bcel.generic.Instruction;

import profiling.constraint.analysis.stack.CodeElement;
import profiling.constraint.analysis.stack.Constant;
import profiling.constraint.analysis.stack.MethodCall;
import profiling.constraint.analysis.stack.Variable;
import profiling.constraint.bytecode.CFGBuilder;
import profiling.constraint.graph.CFG;
import profiling.constraint.graph.Node;

public class InstanceofHandler extends AbstractHandler{

	@Override
	public void handle(Instruction currentInstruction, Node currentNode, CFG cfg) {
		// TODO Auto-generated method stub
		INSTANCEOF instruction = (INSTANCEOF) currentInstruction;
		int index  = instruction.getIndex();			
		String classType = instruction.getType(CFGBuilder.cp).getSignature();
		//System.out.println("INSTANCEOF : " + classType);
		
		CodeElement object = CFGBuilder.elementStack.pop();
		//System.out.println(object + " instanceof "+ classType);
		
		MethodCall mcall = new MethodCall();
		mcall.setClassName("String");
		mcall.setObject(object);
		mcall.setMethodName("instanceof");
		mcall.setType("boolean");
		Vector<CodeElement> parameters = new Vector<CodeElement>();
		mcall.setReturnType("boolean");
		mcall.setIntraStaticMethodCall(true);
		mcall.setStaticCall(true);
		Constant constant = new Constant(classType);
		constant.setType("Class");
		parameters.add(constant);
		mcall.setParameters(parameters);
		
		CFGBuilder.elementStack.push(mcall);
	}
	

}

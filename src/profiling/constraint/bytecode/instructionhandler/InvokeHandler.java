package profiling.constraint.bytecode.instructionhandler;

import java.util.Date;
import java.util.Vector;

import maintenance.Maintenance;

import org.apache.bcel.generic.INVOKEINTERFACE;
import org.apache.bcel.generic.INVOKESTATIC;
import org.apache.bcel.generic.Instruction;
import org.apache.bcel.generic.InvokeInstruction;
import org.apache.bcel.generic.Type;

import profiling.constraint.analysis.stack.Assignment;
import profiling.constraint.analysis.stack.CodeElement;
import profiling.constraint.analysis.stack.Constant;
import profiling.constraint.analysis.stack.MethodCall;
import profiling.constraint.analysis.stack.StaticClass;
import profiling.constraint.analysis.stack.Variable;
import profiling.constraint.bytecode.CFGBuilder;
import profiling.constraint.graph.CFG;
import profiling.constraint.graph.Node;
import profiling.constraint.project.Project;
import profiling.constraint.symbolic.AssignmentUpdate;
import profiling.constraint.symbolic.ByThirdPartyUpdate;
import profiling.constraint.symbolic.MethodCallUpdate;


public class InvokeHandler extends AbstractHandler {		

	@Override
	public void handle(Instruction currentInstruction, Node currentNode, CFG cfg) {
		InvokeInstruction instruction = (InvokeInstruction) currentInstruction;
		
		String mn = instruction.getMethodName(CFGBuilder.cp);
		String sign = instruction.getSignature(CFGBuilder.cp);
		String cl = instruction.getClassName(CFGBuilder.cp);
		String methodDeclaration = cl+"#"+mn+"#"+sign;
						
		Type[] argTypes = instruction.getArgumentTypes(CFGBuilder.cp);
		String[] arguments = new String[argTypes.length];
										
		String methodType = instruction.getType(CFGBuilder.cp).toString(); //usar para traçar perfil da constraint		
		String methodName = instruction.getMethodName(CFGBuilder.cp);
		String className = instruction.getClassName(CFGBuilder.cp);
		String forStatic = instruction.getClassName(CFGBuilder.cp);
		
		String callExpression = forStatic+"."+methodName;		
		if (callExpression.contains("org.junit.Assert.fail")){
			currentNode.setHasReturn(true);			
			return;
		}

		Vector<CodeElement> args = new Vector<CodeElement>();					
						
		for (int i=arguments.length-1; i>=0; i--){						
			if (!CFGBuilder.elementStack.isEmpty())	{
				CodeElement elArg = CFGBuilder.elementStack.pop();
				args.add(0, elArg);						
			}		
		}
							
		boolean isStatic  = false;
		CodeElement objEl = null;		
		if (instruction instanceof INVOKESTATIC){
			
			Date nd = new Date();			
			isStatic = true;			
			objEl = new StaticClass(forStatic,String.valueOf(CFGBuilder.controlID++));			
			//if (forStatic.contains("Math"))
			//	System.out.println(forStatic+"."+methodName);
		}
		else{
			if (CFGBuilder.elementStack.isEmpty())
				System.out.println(" METHOD COM EXCEPTION: " + cfg.getClassName() + "::"+ cfg.getMethodName()+" arguments length: " + arguments.length);
			Maintenance.gambiarra("parametros dos métodos");
			
			if (cfg.getClassName().contains("MainMenu$12") && cfg.getMethodName().contains("menuShown")){
			//	System.out.println("Peek: " + CFGBuilder.elementStack.peek().getValue());
				//System.out.println("  method: " + mn);
			}
			
			if (!CFGBuilder.elementStack.isEmpty())
				objEl = CFGBuilder.elementStack.pop();
			else
				objEl = new Constant("unknown");
			
			if (cfg.getClassName().contains("MainMenu$12") && cfg.getMethodName().contains("menuShown")){
				//System.out.println("  obj: " + objEl.getValue());
			}
		}			
				
		
		MethodCall methodCall = new MethodCall();
		//System.out.println("Objel: " + objEl);
		methodCall.setObject(objEl);
				
		methodCall.setStaticCall(isStatic);
		
		if (objEl.toString().equals("this")){				
			methodCall.setIntraClassMethodCall(true);
		}
		else
			if (Project.allMethods!=null){
				if (Project.allMethods.contains(methodDeclaration))
				{					
					methodCall.setInterClassMethodCall(true);
				}
				else
				{
					methodCall.setExternalLibrary(true);					
				}
			}
			else
				methodCall.setInterClassMethodCall(true);
							
		methodCall.setMethodName(methodName);		
		methodCall.setClassName(className);
		methodCall.setParameters(args);
		methodCall.setReturnType(methodType);
				
						
		if (methodType.equals("void") && !methodName.equals("<init>")){
			//System.out.println("dont Push: " + methodCall);
		}
		else
		{		
			CFGBuilder.elementStack.push(methodCall);
			
		}
		
		
		if (CFGBuilder.newObject==true)
		{
			//Assignment assignment = new Assignment(methodCall.getObject(),methodCall);
			//System.out.println(assignment);
			//AssignmentUpdate assignmentUpdate;
			//assignmentUpdate = new AssignmentUpdate(assignment);
			//currentNode.addVarUpdate(assignmentUpdate);
											
			CFGBuilder.newObject=false;
		}
		
		//adiciona update do objeto que invocou o método
		MethodCallUpdate update = new MethodCallUpdate();
		update.setMethodCall(methodCall);		
		currentNode.addVarUpdate(update);
		
		//adiciona o update das variáveis passadas como parâmetros que podem ter sido alteradas pelo método
		for (CodeElement cel: args){
			if (cel instanceof Variable){
				ByThirdPartyUpdate btpUpdate = new ByThirdPartyUpdate();				
				btpUpdate.setVariableUpdated((Variable)cel);
				btpUpdate.setMethodCall(methodCall);				
				currentNode.addVarUpdate(btpUpdate);	
			}			
		}
		
		if (cfg.getClassName().contains("MainMenu$12") && cfg.getMethodName().contains("menuShown")){
			//System.out.println("   "+methodCall);
		}
	}
}

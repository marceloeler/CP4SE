package profiling.constraint.bytecode.instructionhandler;

import org.apache.bcel.generic.FieldInstruction;
import org.apache.bcel.generic.GETFIELD;
import org.apache.bcel.generic.GETSTATIC;
import org.apache.bcel.generic.Instruction;
import org.apache.bcel.generic.PUTFIELD;
import org.apache.bcel.generic.PUTSTATIC;

import profiling.constraint.analysis.stack.Assignment;
import profiling.constraint.analysis.stack.CodeElement;
import profiling.constraint.analysis.stack.Variable;
import profiling.constraint.bytecode.CFGBuilder;
import profiling.constraint.graph.CFG;
import profiling.constraint.graph.Node;
import profiling.constraint.symbolic.AssignmentUpdate;

public class FieldHandler extends AbstractHandler{

	@Override
	public void handle(Instruction currentInstruction, Node currentNode, CFG cfg) {
		
		boolean classAttribute = false;
		boolean instanceAttribute = false;
		boolean put=false;						
		
		FieldInstruction instruction = (FieldInstruction) currentInstruction;
		
		String type = instruction.getType(CFGBuilder.cp).getSignature();
		String className="";
		String fieldName="";				
		String varName = "";
		
		if (instruction instanceof GETFIELD){
			GETFIELD gf = (GETFIELD) instruction;
			fieldName = gf.getFieldName(CFGBuilder.cp);
			varName = fieldName;			
			instanceAttribute = true;			
		}
				
		if (instruction instanceof GETSTATIC){									
			GETSTATIC gs = (GETSTATIC) instruction;
			className = gs.getClassName(CFGBuilder.cp);
			fieldName = gs.getFieldName(CFGBuilder.cp);
			varName = className+"."+fieldName;					
			classAttribute = true;					
		}
				
		if (instruction instanceof PUTFIELD){
			PUTFIELD pf = (PUTFIELD) instruction;					
			fieldName = pf.getFieldName(CFGBuilder.cp);
			varName = fieldName;			
			instanceAttribute = true;
			put = true;
		}
		
		if (instruction instanceof PUTSTATIC){
			PUTSTATIC pf = (PUTSTATIC) instruction;
			className = pf.getClassName(CFGBuilder.cp);
			fieldName = pf.getFieldName(CFGBuilder.cp);
			varName = className+"."+fieldName;			
			classAttribute = true;
			put=true;
		}									
						
		
		CodeElement el=null;
		if (put==true){ //store
			el = CFGBuilder.elementStack.pop();
			varName="";
			if (!className.equals(""))
				varName=className+"."+fieldName;
			else
				varName=fieldName;							
			//currentNode.addAssignment(varName, expression);						
		}
		
		
		CodeElement object = null;
		if (instanceAttribute){			
			if (!CFGBuilder.elementStack.isEmpty())			
				object = CFGBuilder.elementStack.pop();		
		}
		
		String keyName=varName;
		if (object!=null)
			keyName = object.getValue()+"."+varName;
		
		Variable var = cfg.getVariable(keyName);				
		if (var==null){
			var = new Variable(varName);
			var.setType(type);
			var.setInstanceAttribute(instanceAttribute);
			var.setClassAttribute(classAttribute);
			if (object!=null){
				var.setObject(object);		
				//if (cfg.getMethodName().contains("siteForPoint"))
				//	System.out.println(" Criou: " + var);
			}
			cfg.addVariable(var);
		}
								
							
		if (instruction instanceof GETFIELD ||	instruction instanceof GETSTATIC){

				CFGBuilder.elementStack.push(var);							
		}
		
				
		//STORE/PUT - EXPRESSÃO DE ATRIBUIÇÃO													
		if (put==true){ //store
								
			Assignment assignment = new Assignment(var,el);										
			AssignmentUpdate update = new AssignmentUpdate(assignment);			
			currentNode.addVarUpdate(update);
						
			
			if (cfg.getMethodName().contains("siteForPoint")){				
				//System.out.print(CFGBuilder.currentIh.getPosition()+":");				
				//System.out.println("PUT:: " + var.toString() + " popEl: " + object +  " class: " + object.getClass());													
				//System.out.println(" "+update);
			}
		}
					
	}
	

}

package profiling.constraint.bytecode.instructionhandler;

import java.util.Date;

import org.apache.bcel.generic.Instruction;
import org.apache.bcel.generic.NEW;
import org.apache.bcel.generic.Type;

import profiling.constraint.analysis.stack.NewObject;
import profiling.constraint.analysis.stack.StaticClass;
import profiling.constraint.analysis.stack.Variable;
import profiling.constraint.bytecode.CFGBuilder;
import profiling.constraint.graph.CFG;
import profiling.constraint.graph.Node;

public class NewHandler extends AbstractHandler {		

	@Override
	public void handle(Instruction currentInstruction, Node currentNode, CFG cfg) {		
		NEW ni = null;
		
		if (currentInstruction instanceof NEW){
			ni = (NEW) currentInstruction;	
			CFGBuilder.newObject =true;
		}
		
		//PORÇÃO PARALELA PARA CONTROLAR PILHA POR MEIO DE ELEMENTOS AO INVÉS DE STRING
		
		if (ni!=null){
						
			
			//Date nd = new Date();			
			//String control = String.valueOf(nd.getTime());			
			String control = String.valueOf(CFGBuilder.controlID++);
			StaticClass stc = new StaticClass(String.valueOf(String.valueOf(ni.getLoadClassType(CFGBuilder.cp))),String.valueOf(CFGBuilder.controlID++));
			if (cfg.getMethodName().equals("read") && CFGBuilder.className.contains("ObjectTag")){
				//System.out.println("   NEW: " + stc);
			}
			//NewObject newObject = new NewObject(String.valueOf(ni.getLoadClassType(CFGBuilder.cp)));
			
			CFGBuilder.elementStack.push(stc);
		}
		
		
		

	}

}

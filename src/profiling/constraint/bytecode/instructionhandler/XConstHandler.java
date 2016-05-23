package profiling.constraint.bytecode.instructionhandler;

import org.apache.bcel.generic.ACONST_NULL;
import org.apache.bcel.generic.DCONST;
import org.apache.bcel.generic.FCONST;
import org.apache.bcel.generic.ICONST;
import org.apache.bcel.generic.Instruction;
import org.apache.bcel.generic.LCONST;

import profiling.constraint.analysis.stack.Constant;
import profiling.constraint.bytecode.CFGBuilder;
import profiling.constraint.graph.CFG;
import profiling.constraint.graph.Node;

public class XConstHandler extends AbstractHandler {

	@Override
	public void handle(Instruction instruction, Node currentNode, CFG cfg) {
		 String value="";
		 String type="";
		 if (instruction instanceof ACONST_NULL){
			 ACONST_NULL constInstr = (ACONST_NULL) instruction;
			 value = "null";
			 type="null";
		 }
			
		 if (instruction instanceof ICONST){
			 ICONST constInstr = (ICONST) instruction;
			 value = String.valueOf(constInstr.getValue());
			 type="int";
		 }
		 
		 if (instruction instanceof FCONST){
			 FCONST constInstr = (FCONST) instruction;
			 value = String.valueOf(constInstr.getValue());
			 type="float";
		 }
		 
		 if (instruction instanceof DCONST){
			 DCONST constInstr = (DCONST) instruction;
			 value = String.valueOf(constInstr.getValue());
			 type="double";
		 }
		 		 
		 if (instruction instanceof LCONST){
			 LCONST constInstr = (LCONST) instruction;
			 value = String.valueOf(constInstr.getValue());
			 type="long";
		 }
	 
		
		//PORÇÃO PARALELA PARA CONTROLAR PILHA POR MEIO DE ELEMENTOS AO INVÉS DE STRING
		 if (!value.contains("java.")){
			 Constant cnst = new Constant(value);
			 cnst.setType(type);
			 CFGBuilder.elementStack.push(cnst);
			 if (cfg.getMethodName().equals("refresh")&& cfg.getClassName().contains("ColumnSubscriptionNew")){
				/// System.out.println("Instruction "+CFGBuilder.currentIh+": push "+cnst);
				// CFGBuilder.showElementStack();
			 }
			// System.out.println("CONSTANT TYPE: " + value+ " = "+type);
			// System.out.println("CP8: "+value);
		 }

	}

}

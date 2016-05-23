package profiling.constraint.bytecode;

import org.apache.bcel.generic.ACONST_NULL;
import org.apache.bcel.generic.ANEWARRAY;
import org.apache.bcel.generic.ARETURN;
import org.apache.bcel.generic.ARRAYLENGTH;
import org.apache.bcel.generic.ArrayInstruction;
import org.apache.bcel.generic.D2F;
import org.apache.bcel.generic.D2I;
import org.apache.bcel.generic.D2L;
import org.apache.bcel.generic.DCMPG;
import org.apache.bcel.generic.DCMPL;
import org.apache.bcel.generic.DCONST;
import org.apache.bcel.generic.DNEG;
import org.apache.bcel.generic.DRETURN;
import org.apache.bcel.generic.F2D;
import org.apache.bcel.generic.F2I;
import org.apache.bcel.generic.FCMPG;
import org.apache.bcel.generic.FCMPL;
import org.apache.bcel.generic.FCONST;
import org.apache.bcel.generic.FNEG;
import org.apache.bcel.generic.FRETURN;
import org.apache.bcel.generic.I2B;
import org.apache.bcel.generic.I2C;
import org.apache.bcel.generic.I2D;
import org.apache.bcel.generic.I2F;
import org.apache.bcel.generic.I2L;
import org.apache.bcel.generic.I2S;
import org.apache.bcel.generic.ICONST;
import org.apache.bcel.generic.IF_ACMPEQ;
import org.apache.bcel.generic.IF_ACMPNE;
import org.apache.bcel.generic.IF_ICMPEQ;
import org.apache.bcel.generic.IF_ICMPGE;
import org.apache.bcel.generic.IF_ICMPGT;
import org.apache.bcel.generic.IF_ICMPLE;
import org.apache.bcel.generic.IF_ICMPLT;
import org.apache.bcel.generic.IF_ICMPNE;
import org.apache.bcel.generic.INEG;
import org.apache.bcel.generic.IRETURN;
import org.apache.bcel.generic.Instruction;
import org.apache.bcel.generic.L2D;
import org.apache.bcel.generic.L2F;
import org.apache.bcel.generic.L2I;
import org.apache.bcel.generic.LCMP;
import org.apache.bcel.generic.LCONST;
import org.apache.bcel.generic.LNEG;
import org.apache.bcel.generic.LRETURN;
import org.apache.bcel.generic.MULTIANEWARRAY;
import org.apache.bcel.generic.NEWARRAY;
import org.apache.bcel.generic.RET;
import org.apache.bcel.generic.RETURN;

public class BytecodeUtils {
	
     public static boolean isInstanceOfIfInstruction(Instruction instruction){
    	
    	if ((instruction instanceof IF_ICMPEQ) ||
    			(instruction instanceof IF_ICMPNE) ||
    			(instruction instanceof IF_ICMPGE) ||
    			(instruction instanceof IF_ICMPLE) ||
    			(instruction instanceof IF_ICMPGT) ||
    			(instruction instanceof IF_ICMPLT) ||
    			(instruction instanceof IF_ACMPEQ) ||
    			(instruction instanceof IF_ACMPNE) )
    		return true;
    	else
    		return false;    	    	
    }
     
     public static boolean isInstanceOfIfReturn(Instruction instruction){
    	 
    	 if ( (instruction instanceof RETURN) ||
    			 (instruction instanceof ARETURN) ||
    			 (instruction instanceof IRETURN) ||
    			 (instruction instanceof DRETURN) ||
    			 (instruction instanceof FRETURN) ||
    			 (instruction instanceof LRETURN) ||
    			 (instruction instanceof RET))
    		 return true;
    	 else
    		 return false;    			     			    	    
     }
     
     public static boolean isInstanceOfXCONST(Instruction instruction){
    	 if (    (instruction instanceof ACONST_NULL) ||
    			 (instruction instanceof ICONST) ||
    			 (instruction instanceof DCONST) ||
    			 (instruction instanceof FCONST) ||
    			 (instruction instanceof LCONST) )
    		 return true;
    	 else
    		 return false;    
     }
     
     public static boolean isType2TypeInstruction(Instruction instruction){
 		
 		if (instruction instanceof D2F ||
 				instruction instanceof I2C ||
 				instruction instanceof I2B ||
 				instruction instanceof D2I ||
 				instruction instanceof I2D ||
 				instruction instanceof L2D ||
 				instruction instanceof I2F ||
 				instruction instanceof D2L ||
 				instruction instanceof I2L ||
 				instruction instanceof L2F ||
 				instruction instanceof L2I ||
 				instruction instanceof F2I ||
 				instruction instanceof F2D ||
 				instruction instanceof I2S ||
 				instruction instanceof I2B )
 			return true;		
 		return false;
 	}
 	
 	public static boolean isXCMPInstruction(Instruction instruction){
 		if (instruction instanceof DCMPG 
 				|| instruction instanceof DCMPL
 				|| instruction instanceof FCMPL
 				|| instruction instanceof FCMPG
 				|| instruction instanceof LCMP)
 			return true;		
 		return false;
 	}
 	
 	public static boolean isArrayInstruction(Instruction currentInstruction){
 		if (currentInstruction instanceof ArrayInstruction || 
 				currentInstruction instanceof NEWARRAY || 
 				currentInstruction instanceof ANEWARRAY || 
 				currentInstruction instanceof ARRAYLENGTH || 
 				currentInstruction instanceof MULTIANEWARRAY)
 			return true;
 		else
 			return false;
 	}
 	
 	public static boolean isNEG(Instruction instruction){
 		if (instruction instanceof INEG ||
 				instruction instanceof DNEG ||
 				instruction instanceof FNEG ||
 				instruction instanceof LNEG)
 			return true;
 		else
 			return false;
 				
 				
 	}

}

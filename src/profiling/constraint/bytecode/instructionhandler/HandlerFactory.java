package profiling.constraint.bytecode.instructionhandler;

import org.apache.bcel.generic.ANEWARRAY;
import org.apache.bcel.generic.ARRAYLENGTH;
import org.apache.bcel.generic.ATHROW;
import org.apache.bcel.generic.ArithmeticInstruction;
import org.apache.bcel.generic.ArrayInstruction;
import org.apache.bcel.generic.BIPUSH;
import org.apache.bcel.generic.CHECKCAST;
import org.apache.bcel.generic.D2F;
import org.apache.bcel.generic.D2I;
import org.apache.bcel.generic.D2L;
import org.apache.bcel.generic.DCMPG;
import org.apache.bcel.generic.DCMPL;
import org.apache.bcel.generic.F2D;
import org.apache.bcel.generic.F2I;
import org.apache.bcel.generic.FCMPG;
import org.apache.bcel.generic.FCMPL;
import org.apache.bcel.generic.FieldInstruction;
import org.apache.bcel.generic.GOTO;
import org.apache.bcel.generic.I2B;
import org.apache.bcel.generic.I2C;
import org.apache.bcel.generic.I2D;
import org.apache.bcel.generic.I2F;
import org.apache.bcel.generic.I2L;
import org.apache.bcel.generic.I2S;
import org.apache.bcel.generic.IINC;
import org.apache.bcel.generic.INSTANCEOF;
import org.apache.bcel.generic.IfInstruction;
import org.apache.bcel.generic.Instruction;
import org.apache.bcel.generic.InvokeInstruction;
import org.apache.bcel.generic.JSR;
import org.apache.bcel.generic.L2D;
import org.apache.bcel.generic.L2F;
import org.apache.bcel.generic.L2I;
import org.apache.bcel.generic.LDC;
import org.apache.bcel.generic.LDC2_W;
import org.apache.bcel.generic.LOOKUPSWITCH;
import org.apache.bcel.generic.LoadInstruction;
import org.apache.bcel.generic.MONITORENTER;
import org.apache.bcel.generic.MONITOREXIT;
import org.apache.bcel.generic.MULTIANEWARRAY;
import org.apache.bcel.generic.NEW;
import org.apache.bcel.generic.NEWARRAY;
import org.apache.bcel.generic.NOP;
import org.apache.bcel.generic.SIPUSH;
import org.apache.bcel.generic.StackInstruction;
import org.apache.bcel.generic.StoreInstruction;
import org.apache.bcel.generic.TABLESWITCH;

import profiling.constraint.bytecode.BytecodeUtils;
import profiling.constraint.bytecode.CFGBuilder;

public class HandlerFactory {
	
	public static AbstractHandler createInstructionHandler(Instruction currentInstruction){
		
		if (currentInstruction instanceof LoadInstruction)
			return new LoadHandler();
		
		if (BytecodeUtils.isInstanceOfXCONST(currentInstruction))
			return new XConstHandler();
		
		if (currentInstruction instanceof FieldInstruction)
			return new FieldHandler();
		
		if (currentInstruction instanceof BIPUSH || currentInstruction instanceof SIPUSH)
			return new PushHandler();
		
		if (currentInstruction instanceof BIPUSH)
			return new XConstHandler();
		
		if (currentInstruction instanceof LDC || currentInstruction instanceof LDC2_W)
			return new LDCHandler();
		
		if (currentInstruction instanceof StoreInstruction)
			return new StoreHandler();
		
		if (currentInstruction instanceof ArithmeticInstruction)
			return new ArithmeticHandler();
		
		if (currentInstruction instanceof IINC)
			return new IINCHandler();
		
		if (currentInstruction instanceof IfInstruction)
			return new IfHandler();
		
		if (currentInstruction instanceof StackInstruction)
			return new StackHandler();
		
		if (currentInstruction instanceof InvokeInstruction)
			return new InvokeHandler();
		
		if (currentInstruction instanceof NEW)
			return new NewHandler();
		
		if (currentInstruction instanceof GOTO)
			return new GotoHandler();
		
		if (BytecodeUtils.isInstanceOfIfReturn(currentInstruction))
			return new ReturnHandler();
		
		if (currentInstruction instanceof ATHROW)		
			return new AThrowHandler();
		
		if ((currentInstruction instanceof TABLESWITCH) || (currentInstruction instanceof LOOKUPSWITCH)) 
			return new SwitchHandler();
		
		if (BytecodeUtils.isArrayInstruction(currentInstruction))
			return new ArrayHandler();
		
		if (currentInstruction instanceof CHECKCAST)
			return new CheckCastHandler();
		
		if (currentInstruction instanceof INSTANCEOF)
			return new InstanceofHandler();
		
		if (BytecodeUtils.isXCMPInstruction(currentInstruction))
			return new XCMPHandler();
		
		if (BytecodeUtils.isType2TypeInstruction(currentInstruction))
			return new Type2TypeHandler();
		
		if (currentInstruction instanceof JSR)
			return new JSRHandler();
		
		if (currentInstruction instanceof MONITORENTER || currentInstruction instanceof MONITOREXIT)
			return new MonitorHandler();
		
		if (currentInstruction instanceof NOP){
			return new NOPHandler();
		}
		
		//System.out.println("Not handled instruction at position: " + CFGBuilder.currentIh.getPosition()+" = " +currentInstruction.getName());
		CFGBuilder.notHandledInstructions.add(currentInstruction.getName());
		//System.out.println("Not handled: " + currentInstruction.getName());
		return new GenericHandler();
	}
	
	
	

}

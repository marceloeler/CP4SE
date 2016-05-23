package profiling.constraint.symbolic;

import profiling.constraint.analysis.stack.MethodCall;
import profiling.constraint.analysis.stack.Variable;


/*
 * obj.method1();
 */
public class MethodCallUpdate extends AbstractUpdate {
 	
	private MethodCall methodCall;
	
	public MethodCallUpdate(){
		 
	}
	
	public MethodCallUpdate(MethodCall methodCall){		
		this.methodCall = methodCall;
		this.variableUpdated = methodCall.getObject();
		this.updatedValue = methodCall;
	}
	
	public MethodCall getMethodCall() {
		return methodCall;
	}
	
	public void setMethodCall(MethodCall methodCall) {
		this.methodCall = methodCall;
		this.updatedValue = methodCall;
		this.variableUpdated = methodCall.getObject();
	}

	public AbstractUpdate copy(){
		MethodCallUpdate copy = new MethodCallUpdate((MethodCall)this.methodCall.copy());
		return copy;
	}
	
	public String toString(){
		return methodCall.toString();
	}
}

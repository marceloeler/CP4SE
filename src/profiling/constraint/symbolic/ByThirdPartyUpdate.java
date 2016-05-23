package profiling.constraint.symbolic;

import profiling.constraint.analysis.stack.CodeElement;
import profiling.constraint.analysis.stack.MethodCall;
import profiling.constraint.analysis.stack.Variable;

/*
 * obj.method(var); //update de var
 * this.method2(var); //update de var
 * 
 * Pode ser um update tanto do método que chamou quanto do objeto ou variável primitiva que foi passado(a) como parâmetro
 */

public class ByThirdPartyUpdate extends AbstractUpdate {
	
	
	private MethodCall methodCall;
	
	public ByThirdPartyUpdate(){
		
	}
	
	public ByThirdPartyUpdate(CodeElement variableUpdate, MethodCall methodCall){
		this.variableUpdated=variableUpdate;
		this.methodCall = methodCall;
		this.updatedValue = methodCall;
	}
	
	public CodeElement getVariableUpdated() {
		return variableUpdated;
	}
	
	public void setVariableUpdated(Variable variableUpdated) {
		this.variableUpdated = variableUpdated;
	}
	
	public MethodCall getMethodCall() {
		return methodCall;
	}
	
	public void setMethodCall(MethodCall methodCall) {		
		this.methodCall = methodCall;
		this.updatedValue = methodCall;		
	}

	
	public AbstractUpdate copy(){
		ByThirdPartyUpdate copy = new ByThirdPartyUpdate((Variable)this.variableUpdated.copy(),(MethodCall)this.methodCall.copy());
		return copy;
	}
	
	public String toString(){
		return methodCall.toString();
	}

}
 
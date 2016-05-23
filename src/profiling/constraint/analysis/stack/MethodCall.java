package profiling.constraint.analysis.stack;

import java.util.Vector;

import profiling.constraint.analysis.info.Utils;

public class MethodCall extends CodeElement {
	
	private static int ident=0;
	
	private CodeElement object;
	private String className;
	private String methodName;
	private Vector<CodeElement> parameters;
	private String returnType;
	private boolean interClassMethodCall;
	private boolean intraClassMethodCall;
	private boolean intraStaticMethodCall;
	private boolean interStaticMethodCall;
	private boolean staticCall;
	private boolean externalLibrary;

	public MethodCall(){				
		parameters = new Vector<CodeElement>();	
		interClassMethodCall=false;
		intraClassMethodCall=false;
		intraStaticMethodCall=false;
		interStaticMethodCall=false;
		staticCall = false;
		externalLibrary=false;
	}

	public CodeElement getObject() {
		return object;
	}

	public void setObject(CodeElement object) {
		this.object = object;
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
		this.value = methodName; 
	}

	public Vector<CodeElement> getParameters() {
		return parameters;
	}

	public void setParameters(Vector<CodeElement> parameters) {
		this.parameters = parameters;
	}
	
	public String toString(){
		
		//System.out.println("    VALUE: " + this.value);
		//System.out.println("    object: " + this.object.getValue());
		String ret = "";
		if (object!=null){
			ident++;
			String stIdent="";
			for (int i=0; i<ident; i++)
				stIdent+=" ";
			//if (object.getValue().contains("getState"))
			//	System.out.println(stIdent+"Object value: " + object.getValue());
			if (object.getValue().contains("getState") && ident>4)
			{
				//System.out.println(" Type: " + object.getClass());
				//System.out.println("  ret=vazio");
				ret+="";
			}
			else 
				ret+=object.toString()+".";
			ident--;
		}
		ret+=this.methodName+"(";
		for (int i=0; i<this.parameters.size(); i++){
			ret+=this.parameters.get(i).toString();
			if (i<this.parameters.size()-1)
				ret+=", ";				
		}
		ret+=")";
		return ret;
	}

	public String getReturnType() {
		return returnType;
	}

	public void setReturnType(String returnType) {
		this.returnType = returnType;
	}
	
	
	
	public boolean isStaticCall() {
		return staticCall;
	}

	public void setStaticCall(boolean staticCall) {
		this.staticCall = staticCall;
	}

	public boolean isInterClassMethodCall() {
		return interClassMethodCall;
	}

	public void setInterClassMethodCall(boolean interClassMethodCall) {
		this.interClassMethodCall = interClassMethodCall;
	}

	public boolean isIntraClassMethodCall() {
		return intraClassMethodCall;
	}

	public void setIntraClassMethodCall(boolean intraClassMethodCall) {
		this.intraClassMethodCall = intraClassMethodCall;
	}
		

	public boolean isIntraStaticMethodCall() {
		return intraStaticMethodCall;
	}

	public void setIntraStaticMethodCall(boolean intraStaticMethodCall) {
		this.intraStaticMethodCall = intraStaticMethodCall;
	}

	public boolean isInterStaticMethodCall() {
		return interStaticMethodCall;
	}

	public void setInterStaticMethodCall(boolean interStaticMethodCall) {
		this.interStaticMethodCall = interStaticMethodCall;
	}		

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public boolean isExternalLibrary() {
		return externalLibrary;
	}

	public void setExternalLibrary(boolean externalLibrary) {
		this.externalLibrary = externalLibrary;
	}

	public CodeElement copy(){
		MethodCall mc = new MethodCall();
		mc.setMethodName(methodName);
		mc.setClassName(className);
		mc.setValue(value);
		if (object!=null)
			mc.setObject(object.copy());
		mc.setReturnType(returnType);
		mc.setType(type);
		mc.setInterClassMethodCall(interClassMethodCall);
		mc.setInterStaticMethodCall(interStaticMethodCall);
		mc.setIntraClassMethodCall(intraClassMethodCall);
		mc.setIntraStaticMethodCall(intraStaticMethodCall);
		mc.setStaticCall(staticCall);	
		mc.setExternalLibrary(externalLibrary);
		for (CodeElement el: parameters){
			mc.getParameters().add(el.copy());
		}
		
		return mc;
	}
	
	public void setVisited(boolean visited){
		this.visited = visited;
		object.setVisited(visited);
		for (int x=0; x<parameters.size(); x++)
		{
			CodeElement el = parameters.get(x);
			el.setVisited(visited);
			parameters.setElementAt(el, x);
		}
	}
	
	public String getType(){
		return this.returnType;
	}
	
	
	private boolean isVariableElement(CodeElement element){
		
		if (element instanceof ArithmeticExpression){
			ArithmeticExpression ae = (ArithmeticExpression) element;
			if (ae.hasVariableElement())
				return true;
		}
		
		if (element instanceof Variable)
			return true;
		
		if (element instanceof ArrayVar)
			return true;
		
		if (element instanceof ArrayElement)
			return true;
		
		return false;
	}
	
	private boolean hasMethodElement(CodeElement element){
		
		if (element instanceof ArithmeticExpression){
			ArithmeticExpression ae = (ArithmeticExpression) element;
			if (ae.hasMethodElement())
				return true;
		}
				
		return false;
	}
	
	public boolean isNonLinear(){
		/*
		String callx = object.getType()+"."+this.getMethodName();
		//if (call.contains("Math"))
		//	System.out.println(this);
		if (Utils.isNonLinearMethod(callx))
		{
			System.out.println(" NON LINEAR: " + this);
		}*/
		
		CodeElement object = this.getObject();
		if (object instanceof Variable || object instanceof StaticClass){
			
			for (CodeElement parameter: this.getParameters()){
				if (isVariableElement(parameter)|| hasMethodElement(parameter) || (parameter instanceof MethodCall)){
					String call = object.getType()+"."+this.getMethodName();
					//if (call.contains("Math"))
					//	System.out.println(this);
					if (Utils.isNonLinearMethod(call))
					{
						//System.out.println(" NON LINEAR: " + call);
						return true;
					}
				}
			}
		}
		
		if (object instanceof MethodCall){
			MethodCall mcall = (MethodCall) object;
			if (mcall.isNonLinear())
				return true;
		}
		
		return false;
	}
	
}

package profiling.constraint.analysis.stack;

import java.util.Vector;

import maintenance.Maintenance;

import profiling.constraint.bytecode.CFGBuilder;
import profiling.constraint.symbolic.AbstractUpdate;


public class Variable extends CodeElement{	
	
	protected boolean inputParameter;
	protected boolean instanceAttribute;
	protected boolean classAttribute;
	protected boolean array;
	protected boolean typeObject;
	protected boolean local;
	protected String varType;	
	protected CodeElement object;
	
	public Variable(String value){		
		this.value = value;
		inputParameter = false;		
		instanceAttribute = false;
		classAttribute = false;
		array = false;
		typeObject = false;
		object=null;				
	}
	
	public boolean isInputParameter() {
		return inputParameter;
	}
	public void setInputParameter(boolean inputParameter) {
		this.inputParameter = inputParameter;
	}

	public boolean isInstanceAttribute() {
		return instanceAttribute;
	}

	public void setInstanceAttribute(boolean instanceAttribute) {
		this.instanceAttribute = instanceAttribute;
	}

	public boolean isClassAttribute() {
		return classAttribute;
	}

	public void setClassAttribute(boolean classAttribute) {
		this.classAttribute = classAttribute;
	}

	public boolean isArray() {
		return array;
	}

	public void setArray(boolean array) {
		this.array = array;
	}
	
	
	public String getVarType() {
		return varType;
	}

	public void setVarType(String varType) {
		this.varType = varType;
		this.type = varType;
	}
	
	public void setType(String type){
		this.type = type;
		this.varType = type;
	}

	public boolean isLocal() {
		return local;
	}

	public void setLocal(boolean local) {
		this.local = local;
	}

	public boolean isObject() {
		return typeObject	;
	}

	public void setObject(boolean object) {		
		this.typeObject = object;
	}

	public CodeElement copy(){	
		//System.out.println("cp1");
		Variable var = new Variable(this.value);
		var.setInputParameter(inputParameter);
		var.setInstanceAttribute(instanceAttribute);
		var.setClassAttribute(classAttribute);
		var.setArray(array);
		var.setType(type);
		var.setVarType(varType);	
		var.visited = this.visited;
		var.setObject(typeObject);
		var.setLocal(local);
		Maintenance.gambiarra("");
		if (object!=null)
			if (!object.toString().equals(this.toString()))			
				var.setObject(object.copy());
		else
			var.setObject(null);
		return var;
	}
	
	public void setVisited(boolean visited){
		this.visited = visited;
		Maintenance.gambiarra("wheel.asm.MethodWriter.visitMaxs");
		if (object!=null && !object.toString().equals(this.toString()))
			object.setVisited(true);
	}
	
	
	public String getType(){
		return varType;
	}

	public CodeElement getObject() {
		return object;
	}

	public void setObject(CodeElement object) {		
		this.object = object;		
	}
	
	public String toString(){
		String ret = "";
		
		if (this.instanceAttribute && this.object!=null)
			ret+=object.toString()+".";
		
		ret+=value;
		return ret;
	}
	
	public String getValue(){
		return this.toString();
	}
		
		
}

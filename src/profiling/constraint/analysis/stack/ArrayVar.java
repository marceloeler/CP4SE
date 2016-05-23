package profiling.constraint.analysis.stack;

import java.util.Vector;

public class ArrayVar extends Variable {
	
	private NewArray newArray;
	private Vector<CodeElement> size;

	public ArrayVar(String value) {
		super(value);
		this.setArray(true);
		// TODO Auto-generated constructor stub
	}
	
	public ArrayVar(String value, NewArray newArray) {
		super(value);
		this.newArray=newArray;
		this.setArray(true);
		this.size = newArray.getSize();
		// TODO Auto-generated constructor stub
	}
	
	public CodeElement copy(){
		ArrayVar var = new ArrayVar(this.value);
		var.setInputParameter(inputParameter);
		var.setInstanceAttribute(instanceAttribute);
		var.setClassAttribute(classAttribute);
		var.setArray(array);
		var.setType(type);
		var.setVarType(varType);	
		var.visited = this.visited;
		var.setObject(typeObject);
		var.setObject(this.getObject());
		var.setLocal(local);
		var.setNewArray(newArray);
		return var;
	}

	public NewArray getNewArray() {
		return newArray;
	}

	public void setNewArray(NewArray newArray) {
		this.newArray = newArray;
	}

	public Vector<CodeElement> getSize() {
		return size;
	}
	
	public String toString(){
		return value;
	}
	
}

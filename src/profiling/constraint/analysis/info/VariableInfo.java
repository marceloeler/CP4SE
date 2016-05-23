package profiling.constraint.analysis.info;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

import profiling.constraint.analysis.stack.Variable;

public class VariableInfo {
	
	private float totalCount;
	private float inputDataCount;
	private float localVarCount;
	private float classFieldCount;
	private float instanceFieldCount;
	private float arrayTypeCount;
	private HashMap<String, Float> typeCount;				
	
	public VariableInfo() {
		super();
		typeCount = new HashMap<String,Float>();
		// TODO Auto-generated constructor stub
	}

	
	
	public VariableInfo(float totalCount, float inputDataCount,
			float localVarCount, float classFieldCount,
			float instanceFieldCount, float arrayTypeCount,
			HashMap<String, Float> typeCount) {
		super();
		this.totalCount = totalCount;
		this.inputDataCount = inputDataCount;
		this.localVarCount = localVarCount;
		this.classFieldCount = classFieldCount;
		this.instanceFieldCount = instanceFieldCount;
		this.arrayTypeCount = arrayTypeCount;
		this.typeCount = typeCount;
	}



	public float getTotalCount() {
		return totalCount;
	}



	public void setTotalCount(float totalCount) {
		this.totalCount = totalCount;
	}



	public float getInputDataCount() {
		return inputDataCount;
	}



	public void setInputDataCount(float inputDataCount) {
		this.inputDataCount = inputDataCount;
	}



	public float getLocalVarCount() {
		return localVarCount;
	}



	public void setLocalVarCount(float localVarCount) {
		this.localVarCount = localVarCount;
	}



	public float getClassFieldCount() {
		return classFieldCount;
	}



	public void setClassFieldCount(float classFieldCount) {
		this.classFieldCount = classFieldCount;
	}



	public float getInstanceFieldCount() {
		return instanceFieldCount;
	}



	public void setInstanceFieldCount(float instanceFieldCount) {
		this.instanceFieldCount = instanceFieldCount;
	}



	public float getArrayTypeCount() {
		return arrayTypeCount;
	}



	public void setArrayTypeCount(float arrayTypeCount) {
		this.arrayTypeCount = arrayTypeCount;
	}



	public HashMap<String, Float> getTypeCount() {
		return typeCount;
	}



	public void setTypeCount(HashMap<String, Float> typeCount) {
		this.typeCount = typeCount;
	}



	public void incrementTotalCount(float increment){
		totalCount+=increment;
	}
	
	public void incrementInputDataCount(float increment){
		inputDataCount+=increment;
	}
	
	public void incrementLocalVarCount(float increment){
		localVarCount+=increment;
	}
	
	public void incrementInstanceFieldCount(float increment){
		instanceFieldCount+=increment;
	}
	
	public void incrementClassFieldCount(float increment){
		classFieldCount+=increment;
	}
	
	public void incrementArrayTypeCount(float increment){
		arrayTypeCount+=increment;
	}			
	
	public void increment(VariableInfo info){
		totalCount+=info.getTotalCount();
		inputDataCount+=info.getInputDataCount();
		localVarCount+=info.getLocalVarCount();
		instanceFieldCount+=info.getInstanceFieldCount();
		classFieldCount+=info.getClassFieldCount();
		arrayTypeCount+=info.getArrayTypeCount();
		
		for (String type: info.getTypeCount().keySet()){
			if (typeCount.containsKey(type)){
				Float value = typeCount.get(type);
				value+=info.getTypeCount().get(type);
				typeCount.put(type, value);
			}
			else
				typeCount.put(type,info.getTypeCount().get(type));
		}				
	}
	
	public void increment(Variable var){		
		this.incrementTotalCount(1);		
		if (var.isArray())
			this.incrementArrayTypeCount(1);
		if (var.isClassAttribute())
			this.incrementClassFieldCount(1);
		if (var.isInstanceAttribute())
			this.incrementInstanceFieldCount(1);
		if (var.isInputParameter())
			this.incrementInputDataCount(1);
		if (var.isLocal())
			this.incrementLocalVarCount(1);
		
		String varType = var.getVarType();
		varType = Utils.standardType(varType);
		if (this.getTypeCount().containsKey(varType))
		{
			Float value = this.getTypeCount().get(varType);
			value++;
			this.getTypeCount().put(varType, value);
		}
		else
			this.getTypeCount().put(varType, 1f);
	}
	
	public float getObjectTypes(){
		float cont = 0;
		for (String type: typeCount.keySet()){
			if (!Utils.isPrimitiveType(type) && !Utils.isArrayType(type))
				cont+=typeCount.get(type);
		}
		return cont;
	}
	
	public Set<String> getDistinctObjectTypes(){
		HashSet<String> set = new HashSet<String>();
		for (String type: typeCount.keySet()){
			if (!Utils.isPrimitiveType(type))
				set.add(type);
		}
		return set;
	}
	
	public float getPrimitiveTypes(){
		float cont = 0;
		for (String type: typeCount.keySet()){
			if (Utils.isPrimitiveType(type))
				cont+=typeCount.get(type);
		}
		return cont;
	}
	
	public float getArrayTypes(){
		float cont = 0;
		for (String type: typeCount.keySet()){
			if (Utils.isArrayType(type))
				cont+=typeCount.get(type);
		}
		return cont;
	}
	
	public void calculateMean(Vector<VariableInfo> infos){
		float size = infos.size();
		for (VariableInfo info: infos){
			this.totalCount+=info.getTotalCount();
			this.inputDataCount+=info.getInputDataCount();
			this.localVarCount+=info.getLocalVarCount();
			this.classFieldCount+=info.getClassFieldCount();
			this.instanceFieldCount+=info.getInstanceFieldCount();
			this.arrayTypeCount+=info.getArrayTypeCount();	
			
			for (String type: info.getTypeCount().keySet()){
				if (typeCount.containsKey(type)){
					Float value = typeCount.get(type);
					value+=info.getTypeCount().get(type);
					typeCount.put(type, value);
				}
				else
					typeCount.put(type,info.getTypeCount().get(type));
			}			
			
		}
		
		this.totalCount/=size;
		this.inputDataCount/=size;
		this.localVarCount/=size;
		this.classFieldCount/=size;
		this.instanceFieldCount/=size;
		this.arrayTypeCount/=size;
		for (String type: this.typeCount.keySet()){
			Float value = typeCount.get(type);
			value/=size;
			typeCount.put(type, value);					
		}	
	}

}

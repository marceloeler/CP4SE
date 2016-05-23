package profiling.constraint.analysis.info;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

import profiling.constraint.analysis.stack.MethodCall;


public class MethodCallInfo {
	
	private float totalCount;	
	private float interClassCount;
	private float intraClassCount;
	private float externalLibraryCount;
	private float staticCallCount;	
	private float parametersCount;
	
	private HashMap<String, Float> methodTypeCount;

	public MethodCallInfo() {
		methodTypeCount = new HashMap<String,Float>();		
	}

	public MethodCallInfo(float totalCount, float interClassCount,
			float intraClassCount, int externalLibraryCount, float staticCallCount,
			float parametersCount) {
		super();
		this.totalCount = totalCount;
		this.interClassCount = interClassCount;
		this.intraClassCount = intraClassCount;
		this.externalLibraryCount = externalLibraryCount;
		this.staticCallCount = staticCallCount;
		this.parametersCount = parametersCount;
		methodTypeCount = new HashMap<String,Float>();	
	}

	public float getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(float totalCount) {
		this.totalCount = totalCount;
	}

	public float getInterClassCount() {
		return interClassCount;
	}

	public void setInterClassCount(float interClassCount) {
		this.interClassCount = interClassCount;
	}

	public float getIntraClassCount() {
		return intraClassCount;
	}

	public void setIntraClassCount(float intraClassCount) {
		this.intraClassCount = intraClassCount;
	}

	public float getExternalLibraryCount() {
		return externalLibraryCount;
	}

	public void setExternalLibraryCount(float externalLibraryCount) {
		this.externalLibraryCount = externalLibraryCount;
	}

	public float getStaticCallCount() {
		return staticCallCount;
	}

	public void setStaticCallCount(float staticCallCount) {
		this.staticCallCount = staticCallCount;
	}

	public float getParametersCount() {
		return parametersCount;
	}

	public void setParametersCount(float parametersCount) {
		this.parametersCount = parametersCount;
	}
		

	public HashMap<String, Float> getMethodTypeCount() {
		return methodTypeCount;
	}

	public void setMethodTypeCount(HashMap<String, Float> methodTypeCount) {
		this.methodTypeCount = methodTypeCount;
	}

	public void incrementTotalCount(float increment){
		totalCount+=increment;
	}
	
	public void incrementInterClassCount(float increment){
		interClassCount+=increment;
	}
	
	public void incrementIntraClassCount(float increment){
		intraClassCount+=increment;
	}
	
	public void incrementParametersCount(float increment){
		parametersCount+=increment;
	}
	
	public void incrementStaticCallCount(float increment){
		staticCallCount+=increment;
	}
	
	public void incrementExternalLibraryCount(float increment){
		externalLibraryCount+=increment;
	}
	
	
	public void increment(MethodCallInfo info){
		totalCount+=info.getTotalCount();
		interClassCount+=info.getInterClassCount();
		intraClassCount+=info.getIntraClassCount();
		externalLibraryCount+=info.getExternalLibraryCount();
		staticCallCount+=info.getStaticCallCount();
		parametersCount+=info.getParametersCount();				
		
		for (String rtype: info.getMethodTypeCount().keySet()){
			if (methodTypeCount.containsKey(rtype)){
				Float value =methodTypeCount.get(rtype);
				value+=info.getMethodTypeCount().get(rtype);
				methodTypeCount.put(rtype, value);
			}
			else
				methodTypeCount.put(rtype, info.getMethodTypeCount().get(rtype));
		}
			
	}
	
	public void increment(MethodCall mcall){
		this.incrementTotalCount(1);		
		this.parametersCount+=mcall.getParameters().size();
		if (mcall.isInterClassMethodCall())
			this.incrementInterClassCount(1);
		if (mcall.isIntraClassMethodCall())
			this.incrementIntraClassCount(1);
		if (mcall.isStaticCall())
			this.incrementStaticCallCount(1);
		if (mcall.isExternalLibrary())
			this.incrementExternalLibraryCount(1);
		
		String returnType = mcall.getReturnType();
		returnType = Utils.standardType(returnType);
		if (returnType.contains("void"))
			if (mcall.toString().contains("<init>"))
				returnType=mcall.getObject().toString();
		if (this.getMethodTypeCount().containsKey(returnType))
		{
			Float value = this.getMethodTypeCount().get(returnType);
			value++;
			this.getMethodTypeCount().put(returnType, value);
		}
		else
			this.getMethodTypeCount().put(returnType, 1f);
	}
	
	public float getObjectTypes(){
		float cont = 0;
		for (String type: methodTypeCount.keySet()){
			if (!Utils.isPrimitiveType(type))
				cont+=methodTypeCount.get(type);
		}
		return cont;
	}

	public Set<String> getDistinctObjectTypes(){
		HashSet<String> set = new HashSet<String>();
		for (String type: methodTypeCount.keySet()){
			if (!Utils.isPrimitiveType(type))
				set.add(type);
		}
		return set;
	}
	
	public float getPrimitiveTypes(){
		float cont = 0;
		for (String type: methodTypeCount.keySet()){
			if (Utils.isPrimitiveType(type))
				cont+=methodTypeCount.get(type);
		}
		return cont;
	}
	
	public float getArrayTypes(){
		float cont = 0;
		for (String type: methodTypeCount.keySet()){
			if (Utils.isArrayType(type))
				cont+=methodTypeCount.get(type);
		}
		return cont;
	}
	
	public void calculateMean(Vector<MethodCallInfo> infos){
		float size = infos.size();
		for (MethodCallInfo info: infos){
			this.totalCount+=info.getTotalCount();
			this.interClassCount+=info.getInterClassCount();
			this.intraClassCount+=info.getIntraClassCount();
			this.externalLibraryCount+=info.getExternalLibraryCount();
			this.parametersCount+=info.getParametersCount();
			this.staticCallCount+=info.getStaticCallCount();
			for (String rtype: info.getMethodTypeCount().keySet()){
				if (methodTypeCount.containsKey(rtype)){
					Float value =methodTypeCount.get(rtype);
					value+=info.getMethodTypeCount().get(rtype);
					methodTypeCount.put(rtype, value);
				}
				else
					methodTypeCount.put(rtype, info.getMethodTypeCount().get(rtype));
			}
		}
		
		this.totalCount/=size;
		this.interClassCount/=size;
		this.intraClassCount/=size;
		this.externalLibraryCount/=size;
		this.staticCallCount/=size;
		this.parametersCount/=size;
		for (String type: this.methodTypeCount.keySet()){
			Float value = methodTypeCount.get(type);
			value/=size;
			methodTypeCount.put(type, value);					
		}	
		
	}
	/*
	private int totalCount;	
	private int interClassCount;
	private int intraClassCount;
	private int externalLibraryCount;
	private int staticCallCount;	
	private int parametersCount;
	
	private HashMap<String, Integer> methodTypeCount; 
	
	 */
	
	
	
}

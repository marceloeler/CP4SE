package profiling.constraint.analysis.info;

import java.util.Vector;

public class ParametersInfo {
	
	private MethodCallInfo methodCallInfo;
	private VariableInfo varInfo;
	private float constants;		
	
	public ParametersInfo(MethodCallInfo methodCallInfo, VariableInfo varInfo,
			float constants) {
		this.methodCallInfo = methodCallInfo;
		this.varInfo = varInfo;
		this.constants = constants;
	}
	
	public ParametersInfo() {
		super();
		methodCallInfo = new MethodCallInfo();
		varInfo = new VariableInfo();
		// TODO Auto-generated constructor stub
	}
	
	public MethodCallInfo getMethodCallInfo() {
		return methodCallInfo;
	}
	public void setMethodCallInfo(MethodCallInfo methodCallInfo) {
		this.methodCallInfo = methodCallInfo;
	}
	public VariableInfo getVarInfo() {
		return varInfo;
	}
	public void setVarInfo(VariableInfo varInfo) {
		this.varInfo = varInfo;
	}
	public float getConstants() {
		return constants;
	}
	public void setConstants(float constants) {
		this.constants = constants;
	}
	
	public void increment(ParametersInfo info){
		constants+=info.getConstants();
		methodCallInfo.increment(info.getMethodCallInfo());
		varInfo.increment(info.getVarInfo());
	}
	
	public void calculateMean(Vector<ParametersInfo> infos){
		Vector<VariableInfo> vetVarInfo = new Vector<VariableInfo>();
		Vector<MethodCallInfo> vetMcInfo = new Vector<MethodCallInfo>();
		float size = infos.size();
		for (ParametersInfo info: infos){
			this.constants+=info.getConstants();
			vetVarInfo.add(info.getVarInfo());
			vetMcInfo.add(info.getMethodCallInfo());
		}
		
		this.constants/=size;
		this.varInfo = new VariableInfo();
		this.varInfo.calculateMean(vetVarInfo);
		this.methodCallInfo = new MethodCallInfo();
		this.methodCallInfo.calculateMean(vetMcInfo);
	}

}

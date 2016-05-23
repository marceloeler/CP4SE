package profiling.constraint.project;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Vector;

import profiling.constraint.analysis.ConstraintInfoToFile;
import profiling.constraint.analysis.Path;
import profiling.constraint.analysis.PathGenerator;
import profiling.constraint.analysis.info.PathMetric;
import profiling.constraint.bytecode.ByteCodeAnalyser;
import profiling.constraint.graph.CFG;
import profiling.constraint.graph.GraphUtils;

public class Clazz {

	public String name;
	private String path;
	public Hashtable<String,Method> methods;
	public Vector<String> abstractMethods;
	public Vector<String> excludedMethods;
	public static int alternativeMethods2;
	
	public static int sequence;
	
	//to calculate mean
	private Vector<PathMetric> pathMetrics;
	private Vector<Path> paths;
	
	public Clazz(String name){
		abstractMethods = new Vector<String>();
		excludedMethods = new Vector<String>();
		this.name = name;
		methods = new Hashtable<String,Method>();
	}
	
	public Clazz(){
		abstractMethods = new Vector<String>();
		excludedMethods = new Vector<String>();
		methods = new Hashtable<String,Method>();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Hashtable<String, Method> getMethods() {
		return methods;
	}

	public void setMethods(Hashtable<String, Method> methods) {
		this.methods = methods;
	}	
	
	public Vector<String> getExcludedMethods() {
		return excludedMethods;
	}

	public void setExcludedMethods(Vector<String> excludedMethods) {
		this.excludedMethods = excludedMethods;
	}

	public Vector<String> getAbstractMethods() {
		return abstractMethods;
	}

	public void setAbstractMethods(Vector<String> abstractMethods) {
		this.abstractMethods = abstractMethods;
	}

	public Vector<PathMetric> getPathMetrics() {
		return pathMetrics;
	}

	public void setPathMetrics(Vector<PathMetric> pathMetrics) {
		this.pathMetrics = pathMetrics;
	}

	public Vector<Path> getPaths() {
		return paths;
	}

	public void setPaths(Vector<Path> paths) {
		this.paths = paths;
	}
	
	public void setAllToNull(){
		methods = null;
		abstractMethods=null;
		excludedMethods=null;
		pathMetrics=null;
		paths=null;
		
	}

	public void addMethod(String methodName, String signature, CFG cfg){
		String time = String.valueOf(++sequence);
		Method method = new Method();
		method.setSignature(signature);
		method.setName(methodName);
		method.setCfg(cfg);		
		methods.put(methodName+":"+signature+":"+time, method);
	}
	
	public void addExcludedMethod (String excludedMethodName){
		excludedMethods.add(excludedMethodName);
	}
	
	public void addAbstractMethod(String abstractMethodName){
		abstractMethods.add(abstractMethodName);
	}
			
	public void calculateMetrics(){
		for (Method method: methods.values()){				
				method.calculateMetrics();			
		}							
	}
	
	public String meanMetricToCSV(){
		//info+=projectName;
		
		String info="";
		for (Method method: methods.values()){
			info+=method.meanMetricToCSV();
		}
		return info;
	}
	
	
	public void printMetricsToFile_byMethod(FileWriter fw, String projectName){
		try {

			for (Method method: methods.values()){				
				method.printMeanToFile(fw, projectName);
			}					
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

	

}



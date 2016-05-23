package profiling.constraint.project;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

import org.apache.bcel.classfile.ClassParser;
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.generic.ClassGen;

import profiling.constraint.analysis.Info;
import profiling.constraint.analysis.Path;
import profiling.constraint.bytecode.ByteCodeAnalyser;
import profiling.constraint.graph.CFG;


public class Project {

	private String name;
	
	public static String currentName;
	private String path;
	private Hashtable<String, Clazz> classes;
	private ProjectInfo pinfo;
	
	public static int classCount;
	public static int methodsCount;
	public static int excludedMethods;
	public static int abstractMethods;
	public static int analyzedMethods;
	public static int allConstraintSequences;
	public static int allUnsolvableConstraints;
	public static Vector<String> allMethods;
	
	public double LOB = 0;
	
	public static void resetInfo(){
		analyzedMethods=0;
		classCount=0;
		methodsCount=0;
		excludedMethods=0;
		abstractMethods=0;
		allConstraintSequences=0;
		allUnsolvableConstraints=0;	
	}

	public Project(String name, String path){
		LOB=0;
		pinfo = new ProjectInfo();
		currentName = name;
		allMethods=  new Vector<String>();
		classes = new Hashtable<String, Clazz> ();
		this.name = name;
		this.path = path;
		this.loadProject();
	}		
	
	public Project(String name, String path,boolean jar){
		//System.out.println("class name: " + name);
		LOB=0;
		pinfo = new ProjectInfo();
		currentName = name;
		allMethods=  new Vector<String>();
		classes = new Hashtable<String, Clazz> ();
		this.name = name;
		this.path = path;
		this.loadProject();
	}		
	
	public ProjectInfo getPInfo(){
		return pinfo;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public Hashtable<String, Clazz> getClasses() {
		return classes;
	}

	public void setClasses(Hashtable<String, Clazz> classes) {
		this.classes = classes;
	}		

	public void addClass(Clazz clazz){
		classes.put(clazz.getName(), clazz);
	}
	
	public static void resetAllMethods(){
		allMethods = new Vector<String>();
	}
	
	public void addMethod(String className, String methodName, String signature, CFG cfg){
		Clazz clazz = classes.get(className);
		if (clazz!=null){
			clazz.addMethod(methodName, signature, cfg);			
		}
	}
	
	public CFG getCFG(String className, String methodName){		
		Clazz clazz = classes.get(className);		
		if (clazz!=null){
			profiling.constraint.project.Method method= clazz.getMethods().get(methodName);
			if (method!=null)
				return method.getCfg();
		}
		return null;
	}
	
	public void extractAllMethods_jar(JarFile jar){
 		Enumeration<JarEntry> entries = jar.entries();
		while(entries.hasMoreElements())
		{
			JarEntry entry = entries.nextElement();
			if(! entry.isDirectory() && entry.getName().endsWith(".class"))
			{	
				String className = entry.getName().replace("/", ".");
				className = className.replace(".class","");
				
				try{						    
					String className2 = jar.getName();
		            JavaClass javaClass = new ClassParser(jar.getInputStream(entry), className2).parse();
		            ClassGen cgen = new ClassGen(javaClass);  
		            for(org.apache.bcel.classfile.Method method: javaClass.getMethods()){		
		            	String cl = javaClass.getClassName();		            	
		            	String sign = method.getSignature();
		            	String mn = method.getName();
		            	allMethods.add(cl+"#"+mn+"#"+sign);		            			            	
		            }
				}
			    catch(Exception ex){
			      	ex.printStackTrace();
			    }   																
			}
		}	
 	}
	
	public void extractAllMethods(File dir){
		
		for (File f : dir.listFiles()){
			if (f.isDirectory()){
				extractAllMethods(f);
			}
			else
				if (f.getAbsolutePath().endsWith(".class")){
					int lastIndex = f.getAbsolutePath().lastIndexOf("/");
					if (lastIndex==-1)
						lastIndex = f.getAbsolutePath().lastIndexOf("\\");
					//System.out.println("  " + f.getAbsolutePath());
					String className = f.getAbsolutePath().substring(lastIndex, f.getAbsolutePath().length()-6);															
					className = className.replace(".class","");
					
					try{						    
						String className2 = className;
						
						ClassParser parser = new ClassParser(f.getAbsolutePath());
				    	JavaClass javaClass = parser.parse();						
			            ClassGen cgen = new ClassGen(javaClass);  
			            for(org.apache.bcel.classfile.Method method: javaClass.getMethods()){		
			            	String cl = javaClass.getClassName();		            	
			            	String sign = method.getSignature();
			            	String mn = method.getName();
			            	allMethods.add(cl+"#"+mn+"#"+sign);		            			            	
			            }
					}
				    catch(Exception ex){
				      	ex.printStackTrace();
				    }   			
		        	
		        }
        	
		}
 	}
	
	private void loadProject_jar(){
		JarFile zippedProject;
		try {
			zippedProject = new JarFile(path);
			
			extractAllMethods_jar(zippedProject);
			
			Enumeration enEntries = zippedProject.entries();				

	        while (enEntries.hasMoreElements()) {
	        	ZipEntry zipEntry = (ZipEntry) enEntries.nextElement();
	            String className = zipEntry.getName();
	            if (className.endsWith(".class")) {
	            	Clazz clazz = new Clazz();
	            	clazz.setName(className);
	            	//For some reason these classes cannot be processed by the tool - thus I'm skipping them until solve the issue
	            	if (!clazz.getName().equals("org/jboss/util/collection/ConcurrentSkipListMap.class") &&
	    					!clazz.getName().equals("org/neuroph/core/Layer.class") && 
	    					!clazz.getName().equals("org/neuroph/core/NeuralNetwork.class") &&
	    					!clazz.getName().equals("org/codehaus/jackson/impl/WriterBasedGenerator.class"))	
	            	{
	            		ByteCodeAnalyser analyser = new ByteCodeAnalyser(zippedProject, zipEntry);	            		            	
		            	Vector<CFG> cfgs = analyser.getCFGs();
		            	for (CFG cfg: cfgs){
		            		Info.methods++; 
		            		LOB+=(double) cfg.getLOB();
		            		//System.out.println(LOB);
		            		methodsCount++;
		            		if (!cfg.isAbstract){
								
								try {													
									//GraphUtils.checkValidGraph(cfg);
									clazz.addMethod(cfg.getMethodName(), cfg.getMethodSignature(), cfg);		
									analyzedMethods++;
								} 
								catch (Exception e) {	
									excludedMethods++;
									clazz.addExcludedMethod(cfg.getMethodName()+" :: "+ e.getMessage());															
								}	
							}	
							else
							{
								//System.out.println("    ABSTRACT");
								//nao vai aparecer na conta na chamada a method.calcMetrics()
								Info.isabstract++;
								abstractMethods++;
								clazz.addAbstractMethod(cfg.getMethodName());
							}
		            	}
		            	className+="_"+String.valueOf(classCount);
		            	clazz.setName(className);
		            	classCount++;
		            	classes.put(className, clazz);
	            	}
	            	
	            }
	        }
	        
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
				
	}
	
	private void loadFiles(File dir){
		for (File f : dir.listFiles()){
			if (f.isDirectory()){
				loadFiles(f);
			}
			else
				if (f.getAbsolutePath().endsWith(".class")){
					int lastIndex = f.getAbsolutePath().lastIndexOf("/");
					if (lastIndex==-1)
						lastIndex = f.getAbsolutePath().lastIndexOf("\\");
					String className = f.getAbsolutePath().substring(lastIndex+1, f.getAbsolutePath().length()-6);
					Clazz clazz = new Clazz();
					clazz.setName(className);			
					
					//For some reason these classes cannot be processed by the tool - thus I'm skipping them until solve the issue
		        	if (!clazz.getName().equals("org/jboss/util/collection/ConcurrentSkipListMap.class") &&
							!clazz.getName().equals("org/neuroph/core/Layer.class") && 
							!clazz.getName().equals("org/neuroph/core/NeuralNetwork.class") &&
							!clazz.getName().equals("org/codehaus/jackson/impl/WriterBasedGenerator.class") && 
							!clazz.getName().contains("RegressionSplitEvaluator_934") &&
							!clazz.getName().contains("ClassifierSplitEvaluator_908") &&
							!clazz.getName().contains("Entity20") &&
							!clazz.getName().equals("CostSensitiveClassifierSplitEvaluator_910") &&
							
							!f.getAbsolutePath().endsWith("Test.class") && //exclude Test cases
							!f.getAbsolutePath().endsWith("Tests.class") )	
		        	{
		        		//System.out.println(f.getAbsolutePath());
		        		ByteCodeAnalyser analyser = new ByteCodeAnalyser(f.getAbsolutePath());	   		        		
		            	Vector<CFG> cfgs = analyser.getCFGs();
		            	for (CFG cfg: cfgs){
		            		Info.methods++; 
		            		LOB+=(double) cfg.getLOB();
		            		methodsCount++;
		            		
		            		if (!cfg.isAbstract){
								
								try {													
									clazz.addMethod(cfg.getMethodName(), cfg.getMethodSignature(), cfg);		
									analyzedMethods++;
								} 
								catch (Exception e) {	
									excludedMethods++;
									clazz.addExcludedMethod(cfg.getMethodName()+" :: "+ e.getMessage());															
								}	
							}	
							else
							{
								Info.isabstract++;
								abstractMethods++;
								clazz.addAbstractMethod(cfg.getMethodName());
							}
		            	}
		            	className+="_"+String.valueOf(classCount);
		            	clazz.setName(className);
		            	classCount++;
		            	classes.put(className, clazz);
		        	}
		        	
		        }
        	
		}
	}
	
	private void loadProject(){
		File projectFile = new File(path);
		extractAllMethods(projectFile);
		
		projectFile = new File(path);
		loadFiles(projectFile);
	}
		
	public void calculateMetrics(){

		//For some reason these classes cannot be processed by the tool - thus I'm skipping them until solve the issue
		for (Clazz clazz: classes.values()){
			if (!clazz.getName().equals("org/jboss/util/collection/ConcurrentSkipListMap.class")&&
					!clazz.getName().equals("org/neuroph/core/Layer.class") && 
					!clazz.getName().equals("org/neuroph/core/NeuralNetwork.class") &&
					!clazz.getName().equals("org/codehaus/jackson/impl/WriterBasedGenerator"))					
			{
				
				clazz.calculateMetrics();
			}
		}
		
		this.fillProjectInfo();
	}
	
	
	
	public static String headInfo(){
		return "Project name, #Classes, #AnalysedMethods, #AbstractMethods, #ExcludedDueToCFG\n";
	}
	
	public String getInfo(){
		String info = "";
		info+=name+",";
		
		info+=this.classes.size()+",";
				
		int numberOfAnalysedMethods = 0;
		int numberOfAbstractMethods=0;
		int numberOfExcludedMethods=0;
		
		for (Clazz clazz: classes.values()){
			numberOfAnalysedMethods+=clazz.getMethods().size();
			numberOfAbstractMethods+=clazz.getAbstractMethods().size();
			numberOfExcludedMethods+=clazz.getExcludedMethods().size();
		}								
		
		int totalMethods = numberOfAnalysedMethods+numberOfAbstractMethods+numberOfExcludedMethods;
		int numberOfConcreteMethods = numberOfAnalysedMethods + numberOfExcludedMethods;
					
		info+=numberOfAnalysedMethods+",";
		info+=numberOfAbstractMethods+",";
		info+=numberOfExcludedMethods+"\n";
				
		return info;
	}
	
	public String metricsToXML(){
		String metrics="";
		
		
		return metrics;
	}
	
	
	public String infoToXML(){

		String info="";
		info+="<project name=\""+this.name+"\">\n";
		
		info+="   <info>\n";
		info+="      <classes>"+classes.size() + "</classes>\n";
		info+="      <methods total=\""+pinfo.getNumberOfMethods()+"\">\n";
		info+="         <analyzed>"+pinfo.getNumberOfAnalyzedMethods()+"</analyzed>\n";
		info+="         <zero_constraints total=\""+(+pinfo.getNumberOfMethods()-pinfo.getNumberOfAnalyzedMethods())+"\">\n";
		info+="            <abstract>"+pinfo.getNumberOfAbstractMethods()+"</abstract>\n";
		info+="            <getset>"+pinfo.getNumberOfGetSet()+"</getset>\n";
		info+="        	   <inits>"+pinfo.getNumberOfConstructors()+"</inits>\n";
		info+="            <others>"+(pinfo.getNumberOfMethods()-pinfo.getNumberOfAnalyzedMethods()-pinfo.getNumberOfAbstractMethods()-pinfo.getNumberOfGetSet()-pinfo.getNumberOfConstructors())+"</others>\n";
		info+="         </zero_constraints>\n";
		info+="      </methods>\n";
		info+="      <constraint_sequences>\n";
		info+="         <all>"+pinfo.getNumberOfConstraintSequences()+"</all>\n";
		info+="         <unsolvable>"+pinfo.getNumberOfUnsolvableConstraints()+"</unsolvable>\n";
		info+="      </constraint_sequences>\n";
		info+="   </info>\n";
		info+="</project>";		
		return info;
	}
	
	
	public String meanMetricToCSV(){
		String info="";
		for (Clazz clazz: classes.values()){
			info+=clazz.meanMetricToCSV();
		}
		return info;
	}
	
	public void fillProjectInfo(){
		pinfo = new ProjectInfo();
		pinfo.setNumberOfClasses(classes.size());
		pinfo.setNumberOfPaths(Info.paths);
		pinfo.setNumberOfAbstractMethods(Info.isabstract);
		pinfo.setNumberOfAnalyzedMethods(Info.hasconstraint);
		pinfo.setNumberOfArrayMethods((int)Info.arrays);
		pinfo.setNumberOfConstraintSequences(Info.constraintsequences);
		pinfo.setNumberOfConstructors((int)Info.init);
		pinfo.setNumberOfExceptionMethods((int)Info.exceptions);
		pinfo.setNumberOfFloatMethods((int)Info.floats);
		pinfo.setNumberOfGetSet(Info.getset);
		pinfo.setNumberOfIntMethods((int) Info.ints);
		pinfo.setNumberOfMethods(Info.methods);
		pinfo.setNumberOfMethodsWithCalls((int)Info.calls);
		pinfo.setNumberOfMethodsWithExternalCalls((int) Info.external);
		pinfo.setNumberOfMethodsWithInnerCalls((int) Info.inner);
		pinfo.setNumberOfMethodsWithInterCalls((int) Info.inter);
		pinfo.setNumberOfMethodsWithLoop((int)Info.loopDepth);
		pinfo.setNumberOfMethodsWithNestedLoops((int) Info.loopDepth2);
		pinfo.setNumberOfObjectMethods((int) Info.objects);
		pinfo.setNumberOfStringMethods((int) Info.strings);
		pinfo.setNumberOfUnsolvableConstraints(Info.unsolvablecs);
		pinfo.setNumberOfConstraints(Info.constraints);
		pinfo.setNumberOfMethodsWithOnlyArray((int)Info.onlyarray);
		pinfo.setNumberOfMethodsWithOnlyInt((int)Info.onlyint);
		pinfo.setNumberOfMethodsWithOnlyFloat((int)Info.onlyfloat);
		pinfo.setNumberOfMethodsWithOnlyObject((int)Info.onlyobject);
		pinfo.setNumberOfMethodsWithOnlyString((int)Info.onlystring);
		pinfo.setNumberOfMethodsWithOnlyException((int)Info.onlyexception);
		pinfo.setNumberOfMethodsWithNonLinearExpression(Info.method_nonLinear);
		pinfo.setNumberOfPathsWithNonLinearExpression(Info.path_nonLinear);
	}
		
	public int countAllMethods(){
		int cont = 0;
		for (Clazz clazz: classes.values()){
			cont+=(clazz.getMethods().size()+clazz.getAbstractMethods().size()+clazz.getExcludedMethods().size());			
		}
		return cont;
	}
	
	public int countAbstractMethods(){
		int cont = 0;
		for (Clazz clazz: classes.values()){
			cont+=clazz.getAbstractMethods().size();			
		}
		return cont;
	}
	
	public int countGetSetMethods(){
		int cont = 0;
		for (Clazz clazz: classes.values()){
			for (Method method:clazz.getMethods().values()){
				if (method.isGetSet)
					cont++;
			}
		}
		return cont;
	}
	
	public int countInitMethods(){
		int cont = 0;
		for (Clazz clazz: classes.values()){
			for (Method method:clazz.getMethods().values()){
				if (method.isConstructor)
					cont++;
			}
		}
		return cont;
	}
	
	public int countAnalyzedMethods(){
		int cont = 0;
		for (Clazz clazz: classes.values()){
			for (Method method:clazz.getMethods().values()){
				if (method.hasConstraintElements())
					cont++;
			}
		}
		return cont;
	}
	
	public int countConstraintSequences(){
		int cont = 0;
		for (Clazz clazz: classes.values()){
			for (Method method:clazz.getMethods().values()){
				cont+=method.getPaths().size();
			}
		}
		return cont;
	}
	
	public int countUnsolvableConstraintSequences(){
		int cont = 0;
		for (Clazz clazz: classes.values()){
			for (Method method:clazz.getMethods().values()){
				for (Path path: method.getPaths()){
					if (path.isUnsolvable())
						cont++;
				}
			}
		}
		return cont;
	}

}

package profiling.constraint.project;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
import java.util.Vector;

import profiling.constraint.analysis.Constraint;
import profiling.constraint.analysis.ConstraintInfo;
import profiling.constraint.analysis.ConstraintInfoToFile;
import profiling.constraint.analysis.ConstraintSequenceGenerator;
import profiling.constraint.analysis.Info;
import profiling.constraint.analysis.Path;
import profiling.constraint.analysis.PathGenerator;
import profiling.constraint.analysis.info.PathMetric;
import profiling.constraint.graph.CFG;
import profiling.constraint.graph.Edge;
import profiling.constraint.graph.Node;


public class Method {

	public static String currentClassName;
	public static String currentMethodName;
	
	private String name;
	public CFG cfg;
	
	private Vector<Path> paths;
	private String signature;
	public float pathConstraintMeanSize;
	
	public boolean isAbstract;
	public boolean isGetSet;
	public boolean isConstructor;
	private PathMetric methodMeanMetric;
	
	public boolean nonLinear;

	public Method(String name){
		this.name = name;
		nonLinear=false;
		resetType();		
	}
	
	public Method(){
		nonLinear=false;
		resetType();
	}
		
	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public CFG getCfg() {
		return cfg;
	}
	public void setCfg(CFG cfg) {
		this.cfg = cfg;
	}
			
	public Vector<Path> getPaths() {
		return paths;
	}

	public void setPaths(Vector<Path> paths) {
		this.paths = paths;
	}
	
	public void resetType(){
		isAbstract=false;
		isGetSet=false;
		isConstructor=false;
	}

	public String[] sortNodes(){
		
		String[] vet = new String[cfg.getNodes().size()];
		
		int[] vetInt = new int[vet.length];
		int i=0;
		for (Node nd: cfg.getNodes().values()){
			vetInt[i] = Integer.valueOf(nd.getId());
			i++;
		}
		
		for (i=0; i<vetInt.length-1; i++){
			for (int j=0; j<vetInt.length-1; j++){
				if (vetInt[j]>vetInt[j+1]){
					int aux = vetInt[j];
					vetInt[j] = vetInt[j+1];
					vetInt[j+1] = aux;
				}
			}
		}
				
		
		for (i=0; i<vetInt.length; i++)
			vet[i] = String.valueOf(vetInt[i]);
		
		return vet;
	}
	
	public int calcCC(){		
		int pi=0;
		int c = 1;
		int e=cfg.getEdges().size();
		int n=cfg.getNodes().size();
		int r = 0;
		for (Node node: cfg.getNodes().values())
			if (node.hasReturn())
				r++;
		
		for (Node node: cfg.getNodes().values()){
			if (node.hasConstraint())
				pi++;
		}
				
		if (r==1)
			return e-n+2;
		else
			return e - n + c + r;   // pi - r + 2; 			
	}
		
	public static String getPackageName(String fname){
		if (fname!=null && fname.length()>0){
			int index = fname.lastIndexOf(".");
			if (index>0)
				return fname.substring(0,index);
		}
		return fname;
		
	}
	
	public static String getClassName(String fname){
		if (fname!=null && fname.length()>0){
			int index = fname.lastIndexOf(".");	
			if (index>0)
				return fname.substring(index+1, fname.length());
		}
		return fname;
	}
	
	public boolean hasConstraintElements(){
		return (paths.size()>1);
	}
			
	public void printMeanToFile(FileWriter fw, String projectName) throws IOException{
		/*
		
		float CC = this.calcCC();
		
		int excludedPath=0;
		float pathMeanSize = 0;
		currentMethod = this.name;
		for (Path path: paths){						
			PathMetric pathMetric = new PathMetric(cfg,path);
			float tot = pathMetric.getConstraintToPrint().getExceptions() +
					pathMetric.getConstraintToPrint().getConstant() + 
					pathMetric.getConstraintToPrint().getVarInfo().getTotalCount() + 
					pathMetric.getConstraintToPrint().getMethodCallInfo().getTotalCount();
			if (tot>0)
			{				
				pathMetrics.add(pathMetric);			
				pathMeanSize+=path.getNodes().size();
			}
			else
				excludedPath++;
		}	
		
		pathMeanSize /= paths.size();	
		
		PathMetric pathMetric = new PathMetric(pathMetrics); //agrupamento das métricas individuais
		
		float total = pathMetric.getConstraintToPrint().getExceptions() +
				pathMetric.getConstraintToPrint().getConstant() + 
				pathMetric.getConstraintToPrint().getVarInfo().getTotalCount() + 
				pathMetric.getConstraintToPrint().getMethodCallInfo().getTotalCount();		
		if (total>0){	
			analyzedMethods++;
			fw.write(projectName+",");
			fw.write(getPackageName(cfg.getClassName())+",");
			fw.write(getClassName(cfg.getClassName())+",");
			fw.write(name+",");
			fw.write(cfg.getNodes().size()+",");
			fw.write(cfg.getEdges().size()+",");
			
			fw.write(CC+",");		
			fw.write((paths.size()-excludedPath)+","); //qtde de caminhos
			fw.write(cfg.getLoops()+",");
			fw.write(cfg.getLoopDepth()+",");
			
			fw.write(pathMeanSize+",");//média do número de nós dos caminhos	
			
			
			fw.write(pathMetric.toOneLineString());						
			fw.write("\n");
		
			float elTot, intTot;
			elTot =pathMetric.getConstraintToPrint().getElements();
			intTot = pathMetric.getConstraintToPrint().getTypeAmount("int");
			
			
			if (cfg.getLoopDepth()>0)
				ConstraintInfoToFile.sumInfo.loopDepth++;
			if (cfg.getLoopDepth()>1)
				ConstraintInfoToFile.sumInfo.loopDepth2++;
			
		}
		else
			{
								
				if (this.name.length()>=3){
					String initial = this.name.substring(0,3);
					//System.out.println("initial: " + initial);
					if (initial.contains("get") || initial.contains("set")){
						this.isGetSet=true;
						getset++;					
					}
					if (this.name.equals("init")){
						this.isConstructor=true;
						inits++;
					}
				}	
				zeroConstraintsMethod++;
			}
		*/
	}	
	
	public String meanMetricToCSV(){
		//System.out.print("MeanMetricCSV,");
		String info = "";
		
		if (hasConstraintElements()){
			float pathMeanSize=0;
			int cont=0;
			for (Path path: paths){						
				if (path.getPathMetric().hasElements())
				{
					pathMeanSize+=path.getNodes().size();
					cont++;
				}
			}
			
			pathMeanSize/=cont;
			
			float CC = this.calcCC();
			
			if (methodMeanMetric.hasElements()){
				
				info+=Project.currentName+",";
				info+=getPackageName(cfg.getClassName())+",";
				info+=getClassName(cfg.getClassName())+",";
				info+=name+",";
				info+=cfg.getNodes().size()+",";
				info+=cfg.getEdges().size()+",";
				
				info+=CC+",";		
				info+=(paths.size())+","; //qtde de caminhos
				info+=cfg.getLoops()+",";
				info+=cfg.getLoopDepth()+",";
				
				info+=pathMeanSize+",";//média do número de nós dos caminhos	
				
				
				info+=methodMeanMetric.toOneLineString();						
				info+="\n";
				
				
			}
		}
		
		return info;
	}
	
	static int contr = 0;
	static int contprint=0;
	
	static int[] contvet=null;
	
	static void generatecontvet(){
		Random random = new Random();
		contvet = new int[163];
		for (int i=0; i<163; i++){
			contvet[i]=random.nextInt(34500)+1;
			//System.out.print(contvet[i]+ "  ");
		}
		//System.out.println();
		//System.out.println();
	}
	
	static boolean isIn(int cont){
		for (int i=0; i<163; i++)
			if (contvet[i]==cont)
				return true;
		return false;
	}
	
	int calculateCyclomaticComplexity(){
		int nodes = cfg.getNodes().size();
		int edges = cfg.getEdges().size();
		
		int cont=0;
		for (Node nd: cfg.getNodes().values()){
			if (nd.isException()||nd.hasReturn())
				cont++;
		}
		
		int cc;
		if (cont>1)
			cc = (edges-nodes)+2+(cont-1);
		else
			cc = (edges-nodes)+2;
			
		
		
		return cc;
	}
	
	public String printComparisonsToCSV(){
		String info="";
		if (hasConstraintElements()){
			info+=Project.currentName+",";
			info+=getPackageName(cfg.getClassName())+",";
			info+=getClassName(cfg.getClassName())+",";
			info+=name+",";
			float cpdt[] = methodMeanMetric.getCombinedInfo().getComparisonDataTypes();
			for (int idc=0; idc<6; idc++){
				info+=String.valueOf(cpdt[idc]);
				info+=",";
			}
			
			float cpet[] = methodMeanMetric.getCombinedInfo().getComparisonElementTypes();
			for (int idc=0; idc<15; idc++){
				info+=String.valueOf(cpet[idc]);
				if (idc<14)
					info+=",";
			}
			info+="\n";
		}
		return info;
	}
	
	public String meanMetricToCSV_short(){
		//System.out.print("MeanMetricCSV_short,");
		if (contvet==null)
			generatecontvet();
		String info = "";
		
		int cc = calculateCyclomaticComplexity();
		//System.out.println(cfg.getMethodName() + ": nodes - "+ cfg.getNodes().size() + " edges: " + cfg.getEdges().size()+ " cc: "+ cc + " basic paths");
		
		boolean methodUnsolvable = false;
		
		if (hasConstraintElements()){
			float pathMeanSize=0;
			int cont=0;
			for (Path path: paths){						
				if (path.getPathMetric().hasElements())
				{
					if (path.isUnsolvable())
						methodUnsolvable=true;
					pathMeanSize+=path.getNodes().size();
					cont++;
				}
			}
			
			pathMeanSize/=cont;
			
			if (methodMeanMetric.hasElements()){
				
				info+=Project.currentName+",";
				info+=getPackageName(cfg.getClassName())+",";
				info+=getClassName(cfg.getClassName())+",";
				info+=name+",";
				info+=cfg.getLoops()+",";
				info+=cfg.getLoopDepth()+",";
				info+=cfg.getNodes().size()+",";
				info+=cfg.getEdges().size()+",";
				info+=cc+",";
				info+=(paths.size())+","; //qtde de caminhos
				info+=pathMeanSize+",";//média do número de nós dos caminhos	
				
				
				info+=methodMeanMetric.toOneLineString_short();
				contr++;
				if (this.nonLinear)
				{
					info+=",1";
					
				}
				else
				{
					info+=",0";
					//if (this.isIn(contr))
						//System.out.println(this.cfg.getClassName()+"."+this.cfg.getMethodName());
				}
				
				if (methodUnsolvable)
				{
					info+=",1";
					
				}
				else
				{
					info+=",0";
					//if (this.isIn(contr))
						//System.out.println(this.cfg.getClassName()+"."+this.cfg.getMethodName());
				}
				
				info+=","+cfg.getArguments()+",";
				info+=cfg.getLOB();
				
				info+="\n";

			}
		}
		
		return info;
	}
	
	public String pathsMetricToCSV(){
		//System.out.print("PathMetricsCSV,");
		String info = "";
		
		if (hasConstraintElements()){
			int pathCount=0;
			for (Path path: paths){		
				pathCount++;

				PathMetric pathMetric = path.getPathMetric();

				if (methodMeanMetric.hasElements()){
					
					info+=Project.currentName+",";
					info+=getPackageName(cfg.getClassName())+",";
					info+=getClassName(cfg.getClassName())+",";
					info+=name+",";
					info+=pathCount+","; //path id					
					info+=path.getNodes().size()+",";//número de nós dos caminhos											
					info+=pathMetric.toOneLineString();		
					
					
					if (path.isNonLinear())
					{
						info+=",1";
						
					}
					else
					{
						info+=",0";
						//if (this.isIn(contr))
							//System.out.println(this.cfg.getClassName()+"."+this.cfg.getMethodName());
					}
					
					if (path.isUnsolvable())
					{
						info+=",1";
						
					}
					else
					{
						info+=",0";
						//if (this.isIn(contr))
							//System.out.println(this.cfg.getClassName()+"."+this.cfg.getMethodName());
					}
					
					info+="\n";

				}	
				
			}	
		}
		
		return info;
	}
	
	public String pathsMetricToCSV_short(){
		//System.out.print("PathMetricsCSV_short,");
		String info = "";
		
		if (hasConstraintElements()){
			int pathCount=0;
			for (Path path: paths){		
				pathCount++;
				//path.calculateMetrics(cfg);
				PathMetric pathMetric = path.getPathMetric();
				if (methodMeanMetric.hasElements()){
					
					info+=Project.currentName+",";
					info+=getPackageName(cfg.getClassName())+",";
					info+=getClassName(cfg.getClassName())+",";
					info+=name+",";
					info+=pathCount+","; //path id					
					info+=path.getNodes().size()+",";//número de nós dos caminhos											
					info+=pathMetric.toOneLineString_short();
					
					if (path.isNonLinear())
					{
						info+=",1";
						
					}
					else
					{
						info+=",0";
						//if (this.isIn(contr))
							//System.out.println(this.cfg.getClassName()+"."+this.cfg.getMethodName());
					}
					
					if (path.isUnsolvable())
					{
						info+=",1";
						
					}
					else
					{
						info+=",0";
						//if (this.isIn(contr))
							//System.out.println(this.cfg.getClassName()+"."+this.cfg.getMethodName());
					}
					
					info+="\n";
					
					
					info+="\n";
				}

			}	

		}
		
		return info;
	}
	
	public String constraintsToCSV(){
		//System.out.print("ConstraintsToCSV,");
		String info = "";
		
		if (hasConstraintElements()){
			int pathCount=0;
			for (Path path: paths){		
				pathCount++;
				info+=Project.currentName+",";
				info+=getPackageName(cfg.getClassName())+",";
				info+=getClassName(cfg.getClassName())+",";
				info+=name+",";
				info+=pathCount+","; //path id					
				for (Constraint constraint: path.getConstraints()){
					info+="("+constraint.toString()+") &&";
				}
				info+="\n";				
			}	
		}		
		return info;
	}
	
	private static int contm=0;
	
	public void calculateMetrics(){
		
		PathGenerator pathGen = new PathGenerator();		
		//uses span tree to generate all paths (one loop iteration, no integration)
		paths = pathGen.generateAllDistinctPaths(cfg);
		
		//Info is used to store the metrics		
		Info.paths+=paths.size();
		
		
		if (this.hasConstraintElements()==false){ //ther is no constraints
			
			//counting gets and sets (but only those methods whose name start with get or set)
			if (this.name.length()>=3){
				String initial = this.name.substring(0,3);
				if (initial.contains("get") || initial.contains("set")){
					this.isGetSet=true;
					Info.getset++;
				}
				if (this.name.equals("init")){
					this.isConstructor=true;
					Info.init++;
				}
			}	
		}
		else { //method has constraints
			float pSize = 0;
			float cont = 0;
			Vector<PathMetric> vetPathMetrics = new Vector<PathMetric>();
			
			for (Path path: paths){		
				
				//calculate metrics for each path
				path.calculateMetrics(cfg);
				
				//check whether the path has nonlinear expressions
				if (path.isNonLinear()){
					this.nonLinear=true;
				}
				
				Info.constraints+=path.getConstraints().size();
				pSize+=path.getConstraints().size();
				
				//get the metrics of the path
				PathMetric pathMetric = path.getPathMetric();
				if (pathMetric.hasElements()){
					//add the metrics of the path to a collection of the path metrics of the method under analysis
					vetPathMetrics.add(pathMetric);
					cont++;
				}
			}
			
			if (this.nonLinear==true){
				Info.method_nonLinear++;
			}
			
			//mean path size
			pSize = pSize / cont;
			pathConstraintMeanSize = pSize;
			
			Info.hasconstraint++;
						
			currentClassName=cfg.getClassName();
			currentMethodName=cfg.getMethodName();
			
			/*
			 * uses the metrics collected for each path to calculate the mean for the method
			 */
			methodMeanMetric = new PathMetric(vetPathMetrics, 0f);
			
			/*
			 * to calculates the sum of all metrics instead of the mean use
			 *  new PathMetric(Vector<ConstraintInfo> constraintsInfo, int dif)
			 */
			
		}
		
			
		
		if (cfg.getLoops()>0)
			Info.loopDepth++;
		if (cfg.getLoopDepth()>=1)
			Info.loopDepth2++;
	}
	
	public String getPlanEdges(){
		String plan = "";
	
		if (hasConstraintElements()){
		
		plan+=Project.currentName+",";
		plan+=getPackageName(cfg.getClassName())+",";
		plan+=getClassName(cfg.getClassName())+",";
		plan+=name+",";
		plan+=cfg.getPlanEdges()+"\n";
		}
		return plan;
	}

}

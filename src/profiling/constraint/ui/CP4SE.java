package profiling.constraint.ui;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import profiling.constraint.analysis.Info;
import profiling.constraint.project.Benchmark;
import profiling.constraint.project.Clazz;
import profiling.constraint.project.Method;
import profiling.constraint.project.Project;

public class CP4SE {
	
	public static String currentProjectName;

	
	private static boolean metricByBenchmark;
	private static boolean metricByProject;
	
	
	private static String benchmarkName;
	private static String currentBenchmarkName;
	
	private static Project currentProject;
	
	
	//method that analyze each individual project	
	public static void analyzeProject(String projectName, String projectPath, String resultDir){
		
		
		currentProjectName = projectName;
		System.out.print("\n... analyzing "+ projectName + "\n");
		
		//A summary of several pieces of information are written to this file (numer of classes, methods, etc)
		FileWriter fwProjects = null;
		
		/*
		Metrics related to constraints are written to this file, but considering the mean of the method
		For example: if in method M path 1 has 2 integers, and path 2 has 4 integers, and path 3 has no integer
		             than the mean for this method is (2+4+0)/3 ==> 2 integers
		This file is used to write the metrics for each individual project if the variable metricByProject is true
		*/
		FileWriter fwMethodMean = null;
		
		/*
		 * The metrics related to the comparisons within the constrains are written to this file
		 * For example: 3 comparisons INT - INT, 2 comparisions FLOAT - FLOAT, and so on
		 * This file is created if variable metricByBenchmark is true
		 */
		FileWriter fwComparisons = null;
		
		/* The control flow graph (CFG) of each method is written in this file
		 * This file is created if variable metricByBenchmark is true
		 */
		FileWriter fwCFG = null;
		
		try {
						
			//If only one individual project is analyzed, then a new empty file must be created
			//and a header must be written to the file
			if (metricByProject){
				fwMethodMean= new FileWriter(resultDir+projectName+"_meanMethod.csv");
				fwMethodMean.write("Project,Package,Class,Method,NLp,NNLp,NPths,PS,NCtr,NEl,NCtt,NVar,NEx,NMC,NIC,NIntC,NEC,NInt,NFlt,NNull,NStr,NArr,NObj,NLExp,UPC,Args,Lob\n");
				
				fwCFG = new FileWriter(resultDir+projectName+"_cfg.csv");
				fwCFG.write("Project,Package,Class,Method,CFG\n");
				
				fwComparisons = new FileWriter(resultDir+projectName+"_comparisons.csv");
				String comparisonsHeader = "Project,Package,Class,Method,INT-INT,INT-FLOAT,FLOAT-FLOAT,BOOL-BOOL,REF-REF,REF-NULL,";
				comparisonsHeader+="VARVAR,VAREXP,VARMETHOD,VARCONST,VARARRAY,EXPEXP,EXPMETHOD,EXPCONST,EXPARRAY,";
				comparisonsHeader+="METHODMETHOD,METHODCONST,METHODARRAY,CONSTCONST,CONSTARRAY,ARRAYARRAY\n";
				fwComparisons.write(comparisonsHeader);
				
				
			}
			
			//If a set of projects is analyzed, then the file to write the metrics must have been already created
			//Therefore during its instantiation it must be used append=true. The header of the file have already been written at this point.
			if (metricByBenchmark){
				fwMethodMean = new FileWriter(resultDir+benchmarkName+"_meanMethod.csv",true);
				fwComparisons = new FileWriter(resultDir+benchmarkName+"_comparisons.csv",true);
				fwCFG = new FileWriter(resultDir+currentBenchmarkName+"_cfg.csv",true);
			}
				
			
			//reset the general information related to each individual project
			Project.resetInfo();
			Info.resetInfo();
			
			/*
			* Creates and loads a project (method loadProject within the constructor)
			* The bytecode of each class is analyzed and a CFG is produced for each method
			* The CFG contains information that is used to perform symbolic execution
			*  In the next version of this tool the symbolic execution will be performed during CFG analysis
			*  But, for now, it is performed for each individual path before calculating the metrics
			*/
			Project project = new Project(projectName,projectPath,false);
			
			//For some reason these classes cannot be processed by the tool - thus I'm skipping them until solve the issue
			for (Clazz clazz: project.getClasses().values()){
				if (!clazz.getName().equals("org/jboss/util/collection/ConcurrentSkipListMap.class")&&
						!clazz.getName().equals("org/neuroph/core/Layer.class") && 
						!clazz.getName().equals("org/neuroph/core/NeuralNetwork.class") &&
						!clazz.getName().equals("RegressionSplitEvaluator_934") && 
						!clazz.getName().equals("CostSensitiveClassifierSplitEvaluator_910") &&
						!clazz.getName().contains("ClassifierSplitEvaluator_908") &&
						!clazz.getName().contains("Entity20") &&
						!clazz.getName().equals("org/codehaus/jackson/impl/WriterBasedGenerator"))			
				{
					
					for (Method method: clazz.getMethods().values()){
						//Generate paths for each method, perform symbolic execution and calculate metrics
						method.calculateMetrics();
						
						if (metricByProject){ //writes a file for each individual project
							fwMethodMean.write(method.meanMetricToCSV_short());
							fwComparisons.write(method.printComparisonsToCSV());
							fwCFG.write(method.getPlanEdges());
						}
						if (metricByBenchmark){ //writes a single file for all projects of the benchmark
							fwMethodMean.write(method.meanMetricToCSV_short());
							fwComparisons.write(method.printComparisonsToCSV());
							fwCFG.write(method.getPlanEdges());
						}
					}
					clazz.setAllToNull();
				} 
			}
			
			
			
				//get the details stored in Info (a data clump used to register some details of each project)
			project.fillProjectInfo();
			
			if (metricByBenchmark){
				//writes information of each project to a single file
				fwProjects = new FileWriter(resultDir+currentBenchmarkName+"_ProjectInfo.csv",true);
				fwProjects.write(project.getName()+","+project.getPInfo().getNumberOfClasses() + "," + project.getPInfo().getNumberOfMethods()+"\n");
				fwProjects.close();
			}
			System.out.println(": " + project.getPInfo().getNumberOfClasses() + " classes and " + project.getPInfo().getNumberOfMethods()+" methods");
			
			currentProject = project;
			
			
			fwMethodMean.close();
			fwComparisons.close();
			fwCFG.close();
	

		} catch (IOException e) {
			System.out.println("Problems creating, writing or reading files \n\n\n");
			e.printStackTrace();
		}

	} 	
	
	public static void analyzeBenchmark(String benchmarkName, String benchmarkPath, String resultDir){
		//counts the amount of project of the benchmark

		
		currentBenchmarkName = benchmarkName;
		//choose to generate a single file to store all metrics of all projects
		metricByProject = false;
		metricByBenchmark=true;
		
		Benchmark benchmark = new Benchmark(benchmarkName);
		
		
		/*
		 * The same reasoning to calculate the metrics to a method is used to write data to this file
		 * The difference is that it is used to write the metrics for the whole benchmark if the variable metricByBenchmark is true
		 */
		FileWriter fwMethodMeanBenchmark = null;
		
		/*
		 * The metrics related to the comparisons within the constrains are written to this file
		 * For example: 3 comparisons INT - INT, 2 comparisions FLOAT - FLOAT, and so on
		 * This file is created if variable metricByBenchmark is true
		 */
		FileWriter fwComparisonsBenchmark = null;
		
		/* The control flow graph (CFG) of each method is written in this file
		 * This file is created if variable metricByBenchmark is true
		 */
		FileWriter fwCFGBenchmark;
		
		FileWriter fwProjects;
		
		try {
			//creates the header of each CSV file
			
			fwMethodMeanBenchmark = new FileWriter(resultDir+benchmarkName+"_meanMethod.csv");
			fwMethodMeanBenchmark.write("Project,Package,Class,Method,NLp,NNLp,Nodes,Edges,CompCicl,NPths,PS,NCtr,NEl,NCtt,NVar,NEx,NMC,NIC,NIntC,NEC,NInt,NFlt,NNull,NArr,NStr,NObj,NLExp,UPC,Args,Lob\n");
			fwMethodMeanBenchmark.close();

			fwCFGBenchmark = new FileWriter(resultDir+currentBenchmarkName+"_cfg.csv");
			fwCFGBenchmark.write("Project,Package,Class,Method,CFG\n");
			fwCFGBenchmark.close();
			
			fwComparisonsBenchmark = new FileWriter(resultDir+benchmarkName+"_comparisons.csv");
			String comparisonsHeader = "Project,Package,Class,Method,INT-INT,INT-FLOAT,FLOAT-FLOAT,BOOL-BOOL,REF-REF,REF-NULL,";
			comparisonsHeader+="VARVAR,VAREXP,VARMETHOD,VARCONST,VARARRAY,EXPEXP,EXPMETHOD,EXPCONST,EXPARRAY,";
			comparisonsHeader+="METHODMETHOD,METHODCONST,METHODARRAY,CONSTCONST,CONSTARRAY,ARRAYARRAY\n";
			fwComparisonsBenchmark.write(comparisonsHeader);
			fwComparisonsBenchmark.close();
			
			fwProjects = new FileWriter(resultDir+benchmarkName+"_ProjectInfo.csv");
			fwProjects.write("Name,classes,methods\n");
			fwProjects.close();
			
			File benchDir = new File(benchmarkPath);
			System.out.println("Analyse projects at: " + benchmarkPath);
			
			for(File entry : benchDir.listFiles())
			{
				if(entry.isDirectory())
				{
					String absolutePath = entry.getAbsolutePath();
					int lastIndex = absolutePath.lastIndexOf("\\");
					String projectName = absolutePath.substring(lastIndex+1, absolutePath.length());
					String projectPath = entry.getAbsolutePath();	
					
					analyzeProject(projectName, projectPath, resultDir);
					benchmark.addProject(currentProject);
				}
				
			}
			
		} catch (IOException e2) {
			System.out.println("Problems creating result files in " + resultDir + "\n\n\n");
			e2.printStackTrace();
		}
		
		
		
		/*
		 * If there is only a jar file instead of the directories of the project
		 * 
		 * 
		 * for(File entry : benchDir.listFiles())
		   {
			File jarFile=null;
			
			if(entry.isDirectory())
			{
				jarFile = getJar(entry);
				try {
					  JarFile jar = new JarFile(jarFile);
				} catch (IOException e) {
					 //TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else {
				
				jarFile = new File(entry.getAbsolutePath());
				try {
					JarFile jar = new JarFile(jarFile);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			
			String prjName = jarFile.getName();
			prjName = prjName.substring(0,prjName.lastIndexOf("."));
			String projectPath = jarFile.getAbsolutePath();
			analyzeProject(prjName, projectPath, resultDir);
			benchmark.addProject(currentProject);
		}
		 * 
		 */
		
		FileWriter fwProject  = null;
		try{
			fwProject = new FileWriter(resultDir+benchmarkName+"_benchmarkInfo.txt");
			fwProject.write(benchmark.getInfo().toVerboseString());
			fwProject.close();
		}
		catch(IOException e){
			e.printStackTrace();
		}

	} 
	
	
	private static String getAbsolutePath(String path){
		
		  if (path.startsWith("/") || path.contains(":\\")){
			 if (!(path.endsWith("\\") || path.endsWith("/"))){
				 path+="/";
			 }
			 File f = new File(path);
			 String abspth = f.getAbsolutePath();
		  }
		  else {
			  File f = new File("");
			  String abspth = f.getAbsolutePath();
			  path = abspth+"/"+path+"/";
		  }
		  
		  return path;
	}
	

 	
 	public static void main(String[] args){

 		//String benchmarkPath = "SF100\\";
		//String resultDir = "reportDir\\sf100-jss\\";
		
		//benchmarkName="sub";

 		//CP4SE.analyzeBenchmark(benchmarkName, benchmarkPath, resultDir);
 		
 		


 		
 		
 		if (args.length !=4){
 			System.out.println("\nYou are missing any argument:");
 			System.out.println("1 - type (-b for benchmark, -p for isolated project)");
 			System.out.println("2 - name of the project or benchmark");
 			System.out.println("3 - absolute path of the project or benchmark (all projects of the benchmark must be in a folder under the benchmark's path");
 			System.out.println("4 - absolute path to which the results should be written\n\n");
 			System.out.println("Example:\n java -cp .;C:/tools/CP4SE/lib/*  profiling.constraint.ui.CP4SE -b sf100 C:/DevTools/SF100/ C:/DevTools/reports/");
 		}
 		else {
 			
 		  String option = args[0];
 		  String name = args[1];
 		  String path = args[2];
 		  String resultDir = args[3];
 		  
 		  path = getAbsolutePath(path);
 		  resultDir = getAbsolutePath(resultDir);
 		  	
 		  
 		  if (option.equals("-b")){
 			  metricByBenchmark = true;
 			  metricByProject = false;
 			  benchmarkName = name;
 			  analyzeBenchmark(name, path, resultDir);
 		  } 
 		  else 
 			  if (option.equals("-p")){
 				  metricByBenchmark = false;
 				  metricByProject = true;
 				  currentProjectName = name;
 				  analyzeProject(name, path, resultDir);
 			  }
 			  else {
 				  System.out.println("Invalid type. Use -b for benchmark or -p for project  in the first argument");
 			  }

 		  System.out.println("END");
 		}
 		
 	}
	

	
}

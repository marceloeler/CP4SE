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
		FileWriter fwMethodMeanProject = null;
		
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
		FileWriter fwCFGBenchmark = null;
		
		try {
						
			//If only one individual project is analyzed, then a new empty file must be created
			//and a header must be written to the file
			if (metricByProject){
				fwMethodMeanProject= new FileWriter(resultDir+projectName+"_meanMethod.csv");
				fwMethodMeanProject.write("Project,Package,Class,Method,NLp,NNLp,NPths,PS,NCtr,NEl,NCtt,NVar,NEx,NMC,NIC,NIntC,NEC,NInt,NFlt,NNull,NStr,NArr,NObj,NLExp,UPC,Args,Lob\n");	
			}
			
			//If a set of projects is analyzed, then the file to write the metrics must have been already created
			//Therefore during its instantiation it must be used append=true. The header of the file have already been written at this point.
			if (metricByBenchmark){
				fwMethodMeanBenchmark = new FileWriter(resultDir+benchmarkName+"_meanMethod.csv",true);
				fwComparisonsBenchmark = new FileWriter(resultDir+benchmarkName+"_comparisons.csv");
				fwCFGBenchmark = new FileWriter(resultDir+currentBenchmarkName+"_cfg.csv",true);
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
						
						if (metricByProject) //writes a file for each individual project
							fwMethodMeanProject.write(method.meanMetricToCSV_short());
						if (metricByBenchmark){ //writes a single file for all projects of the benchmark
							fwMethodMeanBenchmark.write(method.meanMetricToCSV_short());
							fwComparisonsBenchmark.write(method.printComparisonsToCSV());
							fwCFGBenchmark.write(method.getPlanEdges());
						}
					}
					clazz.setAllToNull();
				} 
			}
			
			//get the details stored in Info (a data clump used to register some details of each project)
			project.fillProjectInfo();
			
			//writes information of each project to a single file
			fwProjects = new FileWriter(resultDir+currentBenchmarkName+"_ProjectInfo.csv",true);
			fwProjects.write(project.getName()+","+project.getPInfo().getNumberOfClasses() + "," + project.getPInfo().getNumberOfMethods()+"\n");
			
			System.out.println(": " + project.getPInfo().getNumberOfClasses() + " classes and " + project.getPInfo().getNumberOfMethods()+" methods");
			
			currentProject = project;
			
			fwProjects.close();
					
			if (metricByProject) 
				fwMethodMeanProject.close();
			if (metricByBenchmark){
				fwMethodMeanBenchmark.close();
				fwComparisonsBenchmark.close();
				fwCFGBenchmark.close();
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
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
			
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		
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
	

 	
 	public static void main(String[] args){

 		String benchmarkPath = "SF100\\";
		String resultDir = "reportDir\\sf100-jss\\";
		
		benchmarkName="sub";

 		CP4SE.analyzeBenchmark(benchmarkName, benchmarkPath, resultDir);
 		
 		System.out.println("END");


 		/*
 		
 		if (args.length !=3){
 			System.out.println("\nARGUMENTOS NECESSÁRIOS:");
 			System.out.println("1 - nome do benchmark (conjunto de projetos)");
 			System.out.println("2 - enderece absoluto da pasta onde estao os projetos");
 			System.out.println("3 - endereço absoluto da pasta onde os resultados serao armazenados\n");
 			System.out.println("java -jar CP4SE.jar sf100 C:\\DevTools\\projects\\ C:\\DevTools\\results\\");
 		}
 		else {
 			System.out.println(args.length);
 			System.out.println (args[0]);
 			System.out.println (args[1]);
 			System.out.println (args[2]);
 			
 		  String bName = args[0];
 		  String bPath = args[1];
 		  String resultDir = args[2];
 		  benchmarkFile=true;
 		  benchmarkName=bName;
 		  analyzeBenchmark(bName, bPath, resultDir);
 		}*/
 		
 	}
	

	
}

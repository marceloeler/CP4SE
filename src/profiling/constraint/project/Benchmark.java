package profiling.constraint.project;

import java.util.Vector;

public class Benchmark {
	
	private String name;
	private Vector<Project> projects;
	private ProjectInfo binfo;
	
	public Benchmark(String name){
		binfo = new ProjectInfo();
		projects = new Vector<Project>();
		this.name = name;
	}
	
	public ProjectInfo getInfo(){
		return binfo;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Vector<Project> getProjects() {
		return projects;
	}

	public void setProjects(Vector<Project> projects) {
		this.projects = projects;
	}
	
	public void addProject(Project project){
		projects.add(project);
		updateBenchmarkInfo(project.getPInfo());
	}
	
	public String infoToXML(){
		int allM=binfo.getNumberOfMethods();
		int absM=binfo.getNumberOfAbstractMethods();
		int getset=binfo.getNumberOfGetSet();
		int inits=binfo.getNumberOfConstructors();
		int analyzedM=binfo.getNumberOfAnalyzedMethods();
		int allCS = binfo.getNumberOfConstraintSequences();
		int allUCS = binfo.getNumberOfUnsolvableConstraints();
		int allClasses = this.countAllClasses();
		String info="";
		info+="<benchmark name=\""+this.name+"\">\n";
		
		info+="   <info>\n";
		info+="      <classes>"+allClasses + "</classes>\n";
		info+="      <methods total=\""+allM+"\">\n";
		info+="         <analyzed>"+analyzedM+"</analyzed>\n";
		info+="         <zero_constraints total=\""+(allM-analyzedM)+"\">\n";
		info+="            <abstract>"+absM+"</abstract>\n";
		info+="            <getset>"+getset+"</getset>\n";
		info+="        	   <inits>"+inits+"</inits>\n";
		info+="            <others>"+(allM-analyzedM-absM-getset-inits)+"</others>\n";
		info+="         </zero_constraints>\n";
		info+="      </methods>\n";
		info+="      <constraint_sequences>\n";
		info+="         <all>"+allCS+"</all>\n";
		info+="         <unsolvable>"+allUCS+"</unsolvable>\n";
		info+="      </constraint_sequences>\n";
		info+="   </info>\n";
		info+="</benchmark>";		
		return info;
	}
	
	public int countAllClasses(){
		int cont = 0;
		for (Project project : projects){
			cont+=project.getClasses().size();
		}
		return cont;
	}
	
	public int countAllMethods(){
		int cont = 0;
		for (Project project:projects){
			cont+=project.countAllMethods();
		}
		return cont;
	}
	
	public int countAbstractMethods(){
		int cont = 0;
		for (Project project:projects){
			cont+=project.countAbstractMethods();
		}
		return cont;
	}
	public int countGetSetMethods(){
		int cont = 0;
		for (Project project:projects){
			cont+=project.countGetSetMethods();
		}
		return cont;
	}
	
	public int countInitMethods(){
		int cont = 0;
		for (Project project:projects){
			cont+=project.countInitMethods();
		}
		return cont;
	}
	
	public int countAnalyzedMethods(){
		int cont = 0;
		for (Project project:projects){
			cont+=project.countAnalyzedMethods();
		}
		return cont;
	}
	
	public int countConstraintSequences(){
		int cont = 0;
		for (Project project:projects){
			cont+=project.countConstraintSequences();
		}
		return cont;
	}
	
	public int countUnsolvableConstraintSequences(){
		int cont = 0;
		for (Project project:projects){
			cont+=project.countUnsolvableConstraintSequences();
		}
		return cont;
	}
	
	public void updateBenchmarkInfo(ProjectInfo pinfo){
		binfo.setNumberOfAbstractMethods(binfo.getNumberOfAbstractMethods()+pinfo.getNumberOfAbstractMethods());
		binfo.setNumberOfAnalyzedMethods(binfo.getNumberOfAnalyzedMethods()+pinfo.getNumberOfAnalyzedMethods());
		binfo.setNumberOfArrayMethods(binfo.getNumberOfArrayMethods()+pinfo.getNumberOfArrayMethods());
		binfo.setNumberOfConstraintSequences(binfo.getNumberOfConstraintSequences()+pinfo.getNumberOfConstraintSequences());
		binfo.setNumberOfConstructors(binfo.getNumberOfConstructors()+pinfo.getNumberOfConstructors());
		binfo.setNumberOfExceptionMethods(binfo.getNumberOfExceptionMethods()+pinfo.getNumberOfExceptionMethods());
		binfo.setNumberOfFloatMethods(binfo.getNumberOfFloatMethods()+pinfo.getNumberOfFloatMethods());
		binfo.setNumberOfGetSet(binfo.getNumberOfGetSet()+pinfo.getNumberOfGetSet());
		binfo.setNumberOfIntMethods(binfo.getNumberOfIntMethods()+pinfo.getNumberOfIntMethods());
		binfo.setNumberOfMethods(binfo.getNumberOfMethods()+pinfo.getNumberOfMethods());
		binfo.setNumberOfMethodsWithCalls(binfo.getNumberOfMethodsWithCalls()+pinfo.getNumberOfMethodsWithCalls());
		binfo.setNumberOfMethodsWithExternalCalls(binfo.getNumberOfMethodsWithExternalCalls()+pinfo.getNumberOfMethodsWithExternalCalls());
		binfo.setNumberOfMethodsWithInnerCalls(binfo.getNumberOfMethodsWithInnerCalls()+pinfo.getNumberOfMethodsWithInnerCalls());
		binfo.setNumberOfMethodsWithInterCalls(binfo.getNumberOfMethodsWithInterCalls()+pinfo.getNumberOfMethodsWithInterCalls());
		binfo.setNumberOfMethodsWithLoop(binfo.getNumberOfMethodsWithLoop()+pinfo.getNumberOfMethodsWithLoop());
		binfo.setNumberOfMethodsWithNestedLoops(binfo.getNumberOfMethodsWithNestedLoops()+pinfo.getNumberOfMethodsWithNestedLoops());
		binfo.setNumberOfObjectMethods(binfo.getNumberOfObjectMethods()+pinfo.getNumberOfObjectMethods());
		binfo.setNumberOfStringMethods(binfo.getNumberOfStringMethods()+pinfo.getNumberOfStringMethods());
		binfo.setNumberOfUnsolvableConstraints(binfo.getNumberOfUnsolvableConstraints()+pinfo.getNumberOfUnsolvableConstraints());
		
		binfo.setNumberOfClasses(binfo.getNumberOfClasses()+pinfo.getNumberOfClasses());
		binfo.setNumberOfPaths(binfo.getNumberOfPaths() + pinfo.getNumberOfPaths());
		binfo.setNumberOfConstraints(binfo.getNumberOfConstraints()+pinfo.getNumberOfConstraints());
		
		binfo.setNumberOfMethodsWithOnlyInt(binfo.getNumberOfMethodsWithOnlyInt()+pinfo.getNumberOfMethodsWithOnlyInt());
		binfo.setNumberOfMethodsWithOnlyFloat(binfo.getNumberOfMethodsWithOnlyFloat()+pinfo.getNumberOfMethodsWithOnlyFloat());
		binfo.setNumberOfMethodsWithOnlyArray(binfo.getNumberOfMethodsWithOnlyArray()+pinfo.getNumberOfMethodsWithOnlyArray());
		binfo.setNumberOfMethodsWithOnlyString(binfo.getNumberOfMethodsWithOnlyString()+pinfo.getNumberOfMethodsWithOnlyString());
		binfo.setNumberOfMethodsWithOnlyObject(binfo.getNumberOfMethodsWithOnlyObject()+pinfo.getNumberOfMethodsWithOnlyObject());
		binfo.setNumberOfMethodsWithOnlyException(binfo.getNumberOfMethodsWithOnlyException()+pinfo.getNumberOfMethodsWithOnlyException());
		
		binfo.setNumberOfPathsWithNonLinearExpression(binfo.getNumberOfPathsWithNonLinearExpression()+pinfo.getNumberOfPathsWithNonLinearExpression());
		binfo.setNumberOfMethodsWithNonLinearExpression(binfo.getNumberOfMethodsWithNonLinearExpression()+pinfo.getNumberOfMethodsWithNonLinearExpression());
	}

}

package profiling.constraint.analysis.info;


import java.util.Vector;
import profiling.constraint.analysis.Constraint;
import profiling.constraint.analysis.ConstraintInfo;
import profiling.constraint.analysis.ConstraintInfoToFile;


public class PathMetric {
	
	private ConstraintInfo combinedInfo;
	private ConstraintInfoToFile constraintToPrint;
	private Vector<ConstraintInfo> constraintsInfo;
	//public static Path currentPath;
	
	
	public PathMetric(Vector<Constraint> constraints){		
		
		constraintsInfo = new Vector<ConstraintInfo>();

		for (Constraint constraint: constraints){		

			ConstraintInfo info = new ConstraintInfo(constraint);
			constraintsInfo.add(info);
			
			ConstraintInfoToFile toFile = new ConstraintInfoToFile(info);					
		}
					
		combinedInfo = new ConstraintInfo(constraintsInfo);		
		
		constraintToPrint = new ConstraintInfoToFile(combinedInfo);				
	}
	
	
	public PathMetric(Vector<PathMetric> pathMetrics, float p){ //arithmetic mean
		constraintsInfo = new Vector<ConstraintInfo>();				
		combinedInfo = new ConstraintInfo();
		for (PathMetric pathMetric: pathMetrics){ 			
			constraintsInfo.add(pathMetric.getCombinedInfo());
		}
		
		combinedInfo.sumDataAndTypeComparison(constraintsInfo);
		combinedInfo.calculateMean(constraintsInfo);
		constraintToPrint = new ConstraintInfoToFile(combinedInfo);
	}
	
	public PathMetric(Vector<ConstraintInfo> constraintsInfo, int dif){ //sum
		combinedInfo = new ConstraintInfo(constraintsInfo);
		constraintToPrint = new ConstraintInfoToFile(combinedInfo);
	}
	
	public String toOneLineString(){
		return constraintToPrint.toOneLineString();
	}
	
	public String toOneLineString_short(){
		return constraintToPrint.toOneLineString_short();
	}
	
	public ConstraintInfo getCombinedInfo() {
		return combinedInfo;
	}

	public void setCombinedInfo(ConstraintInfo combinedInfo) {
		this.combinedInfo = combinedInfo;
	}

	public ConstraintInfoToFile getConstraintToPrint() {
		return constraintToPrint;
	}

	public void setConstraintToPrint(ConstraintInfoToFile constraintToPrint) {
		this.constraintToPrint = constraintToPrint;
	}

	public Vector<ConstraintInfo> getConstraintsInfo() {
		return constraintsInfo;
	}

	public void setConstraintsInfo(Vector<ConstraintInfo> constraintsInfo) {
		this.constraintsInfo = constraintsInfo;
	}
	
	public String infoToString(){
		return constraintToPrint.toOneLineString();
	}
	
	public String toVerboseString(){
		return constraintToPrint.toVerboseString();
	}
	
	public boolean hasElements(){
		float tot = getConstraintToPrint().getExceptions() +
		getConstraintToPrint().getConstant() + 
		getConstraintToPrint().getVarInfo().getTotalCount() + 
		getConstraintToPrint().getMethodCallInfo().getTotalCount();
		if (tot>0)
			return true;
		else
			return false;
	}

}

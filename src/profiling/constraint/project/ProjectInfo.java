package profiling.constraint.project;

public class ProjectInfo {
	
	private int numberOfClasses;
	
	private int numberOfPaths;
	private int numberOfMethods;
	private int numberOfAbstractMethods;
	private int numberOfGetSet;
	private int numberOfConstructors;
	private int numberOfAnalyzedMethods;
	private int numberOfConstraintSequences;
	
	private double numberOfConstraints;
	
	private int numberOfUnsolvableConstraints;
	private int numberOfIntMethods;
	private int numberOfFloatMethods;
	private int numberOfArrayMethods;
	private int numberOfStringMethods;
	private int numberOfObjectMethods;
	private int numberOfExceptionMethods;
	private int numberOfMethodsWithCalls;
	private int numberOfMethodsWithInnerCalls;
	private int numberOfMethodsWithInterCalls;
	private int numberOfMethodsWithExternalCalls;
	private int numberOfMethodsWithLoop;
	private int numberOfMethodsWithNestedLoops;
	
	private int numberOfMethodsWithOnlyInt;
	private int numberOfMethodsWithOnlyFloat;
	private int numberOfMethodsWithOnlyArray;
	private int numberOfMethodsWithOnlyString;
	private int numberOfMethodsWithOnlyObject;
	private int numberOfMethodsWithOnlyException;
	
	private int numberOfMethodsWithNonLinearExpression;
	private int numberOfPathsWithNonLinearExpression;
	
	
	public int getNumberOfMethods() {
		return numberOfMethods;
	}
	public void setNumberOfMethods(int numberOfMethods) {
		this.numberOfMethods = numberOfMethods;
	}
	public int getNumberOfAbstractMethods() {
		return numberOfAbstractMethods;
	}
	public void setNumberOfAbstractMethods(int numberOfAbstractMethods) {
		this.numberOfAbstractMethods = numberOfAbstractMethods;
	}
	public int getNumberOfGetSet() {
		return numberOfGetSet;
	}
	public void setNumberOfGetSet(int numberOfGetSet) {
		this.numberOfGetSet = numberOfGetSet;
	}
	public int getNumberOfConstructors() {
		return numberOfConstructors;
	}
	public void setNumberOfConstructors(int numberOfConstructors) {
		this.numberOfConstructors = numberOfConstructors;
	}
	public int getNumberOfAnalyzedMethods() {
		return numberOfAnalyzedMethods;
	}
	public void setNumberOfAnalyzedMethods(int numberOfAnalyzedMethods) {
		this.numberOfAnalyzedMethods = numberOfAnalyzedMethods;
	}
	public int getNumberOfConstraintSequences() {
		return numberOfConstraintSequences;
	}
	public void setNumberOfConstraintSequences(int numberOfConstraintSequences) {
		this.numberOfConstraintSequences = numberOfConstraintSequences;
	}
	public int getNumberOfUnsolvableConstraints() {
		return numberOfUnsolvableConstraints;
	}
	public void setNumberOfUnsolvableConstraints(int numberOfUnsolvableConstraints) {
		this.numberOfUnsolvableConstraints = numberOfUnsolvableConstraints;
	}
	public int getNumberOfIntMethods() {
		return numberOfIntMethods;
	}
	public void setNumberOfIntMethods(int numberOfIntMethods) {
		this.numberOfIntMethods = numberOfIntMethods;
	}
	public int getNumberOfFloatMethods() {
		return numberOfFloatMethods;
	}
	public void setNumberOfFloatMethods(int numberOfFloatMethods) {
		this.numberOfFloatMethods = numberOfFloatMethods;
	}
	public int getNumberOfArrayMethods() {
		return numberOfArrayMethods;
	}
	public void setNumberOfArrayMethods(int numberOfArrayMethods) {
		this.numberOfArrayMethods = numberOfArrayMethods;
	}
	public int getNumberOfStringMethods() {
		return numberOfStringMethods;
	}
	public void setNumberOfStringMethods(int numberOfStringMethods) {
		this.numberOfStringMethods = numberOfStringMethods;
	}
	public int getNumberOfObjectMethods() {
		return numberOfObjectMethods;
	}
	public void setNumberOfObjectMethods(int numberOfObjectMethods) {
		this.numberOfObjectMethods = numberOfObjectMethods;
	}
	public int getNumberOfExceptionMethods() {
		return numberOfExceptionMethods;
	}
	public void setNumberOfExceptionMethods(int numberOfExceptionMethods) {
		this.numberOfExceptionMethods = numberOfExceptionMethods;
	}
	public int getNumberOfMethodsWithCalls() {
		return numberOfMethodsWithCalls;
	}
	public void setNumberOfMethodsWithCalls(int numberOfMethodsWithCalls) {
		this.numberOfMethodsWithCalls = numberOfMethodsWithCalls;
	}
	public int getNumberOfMethodsWithInnerCalls() {
		return numberOfMethodsWithInnerCalls;
	}
	public void setNumberOfMethodsWithInnerCalls(int numberOfMethodsWithInnerCalls) {
		this.numberOfMethodsWithInnerCalls = numberOfMethodsWithInnerCalls;
	}
	public int getNumberOfMethodsWithInterCalls() {
		return numberOfMethodsWithInterCalls;
	}
	public void setNumberOfMethodsWithInterCalls(int numberOfMethodsWithInterCalls) {
		this.numberOfMethodsWithInterCalls = numberOfMethodsWithInterCalls;
	}
	public int getNumberOfMethodsWithExternalCalls() {
		return numberOfMethodsWithExternalCalls;
	}
	public void setNumberOfMethodsWithExternalCalls(
			int numberOfMethodsWithExternalCalls) {
		this.numberOfMethodsWithExternalCalls = numberOfMethodsWithExternalCalls;
	}
	public int getNumberOfMethodsWithLoop() {
		return numberOfMethodsWithLoop;
	}
	public void setNumberOfMethodsWithLoop(int numberOfMethodsWithLoop) {
		this.numberOfMethodsWithLoop = numberOfMethodsWithLoop;
	}
	public int getNumberOfMethodsWithNestedLoops() {
		return numberOfMethodsWithNestedLoops;
	}
	public void setNumberOfMethodsWithNestedLoops(int numberOfMethodsWithNestedLoops) {
		this.numberOfMethodsWithNestedLoops = numberOfMethodsWithNestedLoops;
	}
		
	public double getNumberOfConstraints() {
		return numberOfConstraints;
	}
	public void setNumberOfConstraints(double numberOfConstraints) {
		this.numberOfConstraints = numberOfConstraints;
	}
	public int getNumberOfMethodsWithOnlyInt() {
		return numberOfMethodsWithOnlyInt;
	}
	public void setNumberOfMethodsWithOnlyInt(int numberOfMethodsWithOnlyInt) {
		this.numberOfMethodsWithOnlyInt = numberOfMethodsWithOnlyInt;
	}
	public int getNumberOfMethodsWithOnlyFloat() {
		return numberOfMethodsWithOnlyFloat;
	}
	public void setNumberOfMethodsWithOnlyFloat(int numberOfMethodsWithOnlyFloat) {
		this.numberOfMethodsWithOnlyFloat = numberOfMethodsWithOnlyFloat;
	}
	public int getNumberOfMethodsWithOnlyArray() {
		return numberOfMethodsWithOnlyArray;
	}
	public void setNumberOfMethodsWithOnlyArray(int numberOfMethodsWithOnlyArray) {
		this.numberOfMethodsWithOnlyArray = numberOfMethodsWithOnlyArray;
	}
	public int getNumberOfMethodsWithOnlyString() {
		return numberOfMethodsWithOnlyString;
	}
	public void setNumberOfMethodsWithOnlyString(int numberOfMethodsWithOnlyString) {
		this.numberOfMethodsWithOnlyString = numberOfMethodsWithOnlyString;
	}
	public int getNumberOfMethodsWithOnlyObject() {
		return numberOfMethodsWithOnlyObject;
	}
	public void setNumberOfMethodsWithOnlyObject(int numberOfMethodsWithOnlyObject) {
		this.numberOfMethodsWithOnlyObject = numberOfMethodsWithOnlyObject;
	}
	public int getNumberOfMethodsWithOnlyException() {
		return numberOfMethodsWithOnlyException;
	}
	public void setNumberOfMethodsWithOnlyException(
			int numberOfMethodsWithOnlyException) {
		this.numberOfMethodsWithOnlyException = numberOfMethodsWithOnlyException;
	}
		
	public int getNumberOfPaths() {
		return numberOfPaths;
	}
	public void setNumberOfPaths(int numberOfPaths) {
		this.numberOfPaths = numberOfPaths;
	}
	public int getNumberOfClasses() {
		return numberOfClasses;
	}
	public void setNumberOfClasses(int numberOfClasses) {
		this.numberOfClasses = numberOfClasses;
	}
	
	
	public int getNumberOfMethodsWithNonLinearExpression() {
		return numberOfMethodsWithNonLinearExpression;
	}
	public void setNumberOfMethodsWithNonLinearExpression(
			int numberOfMethodsWithNonLinearExpression) {
		this.numberOfMethodsWithNonLinearExpression = numberOfMethodsWithNonLinearExpression;
	}
	public int getNumberOfPathsWithNonLinearExpression() {
		return numberOfPathsWithNonLinearExpression;
	}
	public void setNumberOfPathsWithNonLinearExpression(
			int numberOfPathsWithNonLinearExpression) {
		this.numberOfPathsWithNonLinearExpression = numberOfPathsWithNonLinearExpression;
	}
	
	public String toVerboseString(){
		String retInfo = "";
		retInfo+="\n";
		retInfo+="Number of classes: " + numberOfClasses+"\n";
		retInfo+="\n";
		retInfo+="Methods\n";
		retInfo+="   Number of methods: " + numberOfMethods+"\n";
		retInfo+="   Number of analyzed methods: " + numberOfAnalyzedMethods + "\n";
		retInfo+="   Number of not analyzed methods: " + (numberOfMethods-numberOfAnalyzedMethods) + "\n";
		retInfo+="       Number of abstract methods: " + numberOfAbstractMethods + "\n";
		retInfo+="       Number of get e set: " + numberOfGetSet + "\n";
		retInfo+="       Number of constructors: " + numberOfConstructors + "\n";
		retInfo+="       Number of other methods without constraints: " + ( (numberOfMethods-numberOfAnalyzedMethods)-(numberOfAbstractMethods+numberOfGetSet+numberOfConstructors)) + "\n";
		retInfo+="\n";
		retInfo+="Complexity\n";
		retInfo+="   Number of methods with non-linear expressions: " + numberOfMethodsWithNonLinearExpression+"\n";
		retInfo+="\n";
		retInfo+="Types\n";
		retInfo+="   Number of methods with at least one int: " + numberOfIntMethods + "\n";
		retInfo+="   Number of methods with at least one float: " +  numberOfFloatMethods + "\n";
		retInfo+="   Number of methods with at least one array: " +  numberOfArrayMethods + "\n";
		retInfo+="   Number of methods with at least one string: " +  numberOfStringMethods + "\n";
		retInfo+="   Number of methods with at least one object: " + numberOfObjectMethods + "\n";
		retInfo+="   Number of methods with at least one exception: " + numberOfExceptionMethods + "\n";
		retInfo+="\n";
		retInfo+="   Number of methods with only int: " + numberOfMethodsWithOnlyInt + "\n";
		retInfo+="   Number of methods with only float: " +  numberOfMethodsWithOnlyFloat + "\n";
		retInfo+="   Number of methods with only array: " +  numberOfMethodsWithOnlyArray + "\n";
		retInfo+="   Number of methods with only string: " +  numberOfMethodsWithOnlyString + "\n";
		retInfo+="   Number of methods with only object: " + numberOfMethodsWithOnlyObject + "\n";
		retInfo+="   Number of methods with only exception: " + numberOfMethodsWithOnlyException+ "\n";
		retInfo+="\n";
		retInfo+="Dependency\n";
		retInfo+="   Number of method with at least one call: " + numberOfMethodsWithCalls + "\n";
		retInfo+="   Number of method with at least one inner call: " + numberOfMethodsWithInnerCalls + "\n";
		retInfo+="   Number of method with at least one inter call: " + numberOfMethodsWithInterCalls + "\n";
		retInfo+="   Number of method with at least one external call: " + numberOfMethodsWithExternalCalls + "\n";
		retInfo+="\n";
		retInfo+="Paths\n";
		retInfo+="   Number of paths: " + numberOfPaths + "\n";
		retInfo+="   Number of path constraints: " + numberOfConstraintSequences + "\n";
		retInfo+="   Number of definitely unsolvable constraint sequences: " + numberOfUnsolvableConstraints + "\n";
		retInfo+="   Number of paths with non linear expressions: " + numberOfPathsWithNonLinearExpression + "\n";
		retInfo+="\n";
		retInfo+="Loops\n";
		retInfo+="   Number of methods with at least one loop: " + numberOfMethodsWithLoop + "\n";
		retInfo+="   Number of methods with at least one nested loop: " + numberOfMethodsWithNestedLoops + "\n";
		
		return retInfo;
	}
	
}

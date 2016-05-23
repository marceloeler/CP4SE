package profiling.constraint.analysis;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Set;
import java.util.Vector;

import maintenance.Maintenance;
import profiling.constraint.analysis.info.MethodCallInfo;
import profiling.constraint.analysis.info.ParametersInfo;
import profiling.constraint.analysis.info.PathMetric;
import profiling.constraint.analysis.info.Utils;
import profiling.constraint.analysis.info.VariableInfo;
import profiling.constraint.analysis.stack.ArithmeticExpression;
import profiling.constraint.analysis.stack.ArrayElement;
import profiling.constraint.analysis.stack.CodeElement;
import profiling.constraint.analysis.stack.Constant;
import profiling.constraint.analysis.stack.ExceptionThrown;
import profiling.constraint.analysis.stack.MethodCall;
import profiling.constraint.analysis.stack.NewArray;
import profiling.constraint.analysis.stack.Variable;
import profiling.constraint.project.Method;
import profiling.constraint.ui.CP4SE;

public class ConstraintInfo {
	
	protected ParametersInfo parInfo;
	protected VariableInfo varInfo;
	protected Set<String> variables;
		
	protected MethodCallInfo methodCallInfo;
	protected Set<String> methodCalls;
	
	protected float distinctMC;
	protected float distinctVar;
	
	protected Hashtable<String,Float> types;
	
	protected float constant;
	protected float exceptions;
	protected float complexity;
	
	protected float mcDepth;
	protected float maxMcDepth;
	
	protected float constraintCount;
	
	protected float distinctTypes;
	
	
	
	//int-int, float-float, bool-bool, ref-ref, ref-null
	protected static final int INTINT=0;
	protected static final int INTFLOAT=1;
	protected static final int FLOATFLOAT=2;
	protected static final int BOOLBOOL=3;
	protected static final int REFREF=4;
	protected static final int REFNULL=5;
	protected float comparisonDataTypes[];
	
	//var-var, var-exp, var-method, var-const
	//exp-exp, exp-method, exp-const
	//method-method, method-const
	//const-const
	protected static final int VARVAR=0;
	protected static final int VAREXP=1;
	protected static final int VARMETHOD=2;
	protected static final int VARCONST=3;
	protected static final int VARARRAY=4;
	protected static final int EXPEXP=5;
	protected static final int EXPMETHOD=6;
	protected static final int EXPCONST=7;
	protected static final int EXPARRAY=8;
	protected static final int METHODMETHOD=9;
	protected static final int METHODCONST =10;
	protected static final int METHODARRAY=11;
	protected static final int CONSTCONST=12;
	protected static final int CONSTARRAY=13;
	protected static final int ARRAYARRAY=14;
	protected float comparisonElementTypes[];

	public ConstraintInfo(){
		initializeInfo();
	}
	
	public void initializeInfo(){			
		types = new Hashtable<String,Float>();
		variables = new HashSet<String>();
		methodCalls = new HashSet<String>();
		
		varInfo = new VariableInfo();
		methodCallInfo = new MethodCallInfo();
		parInfo = new ParametersInfo();
		constant=0;				
		complexity = 0;
		mcDepth=0;
		maxMcDepth=0;
		constraintCount = 0;
		exceptions = 0;
		
		comparisonDataTypes = new float[6];
		comparisonElementTypes = new float[15];
		//distinctMC=0;
		//distinctVar=0;
	}	
	

	
	public void extractTypesOfComparison(CodeElement leftSide, CodeElement rightSide){
		if (leftSide.getType()==null){
			//System.out.println(leftSide);
		}
		else
			if (rightSide.getType()==null)
			{
				//System.out.println(rightSide);
			}
			else
			{
				//comparison regarding data types
				String leftType = Utils.standardType(leftSide.getType());
				String rightType = Utils.standardType(rightSide.getType());
				
				if (Utils.isInt(leftType) && Utils.isInt(rightType))
					comparisonDataTypes[INTINT]++;
				
				if (Utils.isFloat(leftType) && Utils.isFloat(rightType))
					comparisonDataTypes[FLOATFLOAT]++;
				
				if (Utils.isInt(leftType) && Utils.isFloat(rightType))
					comparisonDataTypes[INTFLOAT]++;
				
				if (Utils.isFloat(leftType) && Utils.isInt(rightType))
					comparisonDataTypes[INTFLOAT]++;
				
				if (Utils.isBool(leftType) && Utils.isInt(rightType))
					comparisonDataTypes[BOOLBOOL]++;
				
				if (Utils.isBool(leftType) && Utils.isBool(rightType))
					comparisonDataTypes[BOOLBOOL]++;
				
				if (Utils.isInt(leftType) && Utils.isBool(rightType))
					comparisonDataTypes[BOOLBOOL]++;
				
				if (Utils.isRef(leftType) && Utils.isRef(rightType))
					comparisonDataTypes[REFREF]++;
				
				if (Utils.isRef(leftType) && Utils.isNull(rightType))
					comparisonDataTypes[REFNULL]++;
					
				if (Utils.isNull(leftType) && Utils.isRef(rightType))
					comparisonDataTypes[REFNULL]++;	
						
				/*
				System.out.println(leftSide + "  " + rightSide + "   types: " + leftSide.getType() + " - " + rightSide.getType());
				 
				for (int i=0; i<6; i++)
					System.out.print(comparisonDataTypes[i]+" ");
				System.out.println();
				*/
				
				//comparison regarding element types
				
				if (Utils.isVar(leftSide) && Utils.isVar(rightSide))
					comparisonElementTypes[VARVAR]++;
				
				if (Utils.isArithmeticExpression(leftSide) && Utils.isVar(rightSide))
					comparisonElementTypes[VAREXP]++;
				
				if (Utils.isVar(leftSide) && Utils.isArithmeticExpression(rightSide))
					comparisonElementTypes[VAREXP]++;
				
				if (Utils.isMethodCall(leftSide) && Utils.isVar(rightSide))
					comparisonElementTypes[VARMETHOD]++;
				
				if (Utils.isVar(leftSide) && Utils.isMethodCall(rightSide))
					comparisonElementTypes[VARMETHOD]++;

				if (Utils.isConstant(leftSide) && Utils.isVar(rightSide))
					comparisonElementTypes[VARCONST]++;
				
				if (Utils.isVar(leftSide) && Utils.isConstant(rightSide))
					comparisonElementTypes[VARCONST]++;
				
				if (Utils.isArray(leftSide) && Utils.isVar(rightSide))
					comparisonElementTypes[VARARRAY]++;
				
				if (Utils.isVar(leftSide) && Utils.isArray(rightSide))
					comparisonElementTypes[VARARRAY]++;
				
				if (Utils.isArithmeticExpression(leftSide) && Utils.isArithmeticExpression(rightSide))
					comparisonElementTypes[EXPEXP]++;
				
				if (Utils.isArithmeticExpression(leftSide) && Utils.isMethodCall(rightSide))
					comparisonElementTypes[EXPMETHOD]++;
				
				if (Utils.isMethodCall(leftSide) && Utils.isArithmeticExpression(rightSide))
					comparisonElementTypes[EXPMETHOD]++;
				
				if (Utils.isArithmeticExpression(leftSide) && Utils.isConstant(rightSide))
					comparisonElementTypes[EXPCONST]++;
				
				if (Utils.isConstant(leftSide) && Utils.isArithmeticExpression(rightSide))
					comparisonElementTypes[EXPCONST]++;
		
				if (Utils.isArithmeticExpression(leftSide) && Utils.isArray(rightSide))
					comparisonElementTypes[EXPARRAY]++;
				
				if (Utils.isArray(leftSide) && Utils.isArithmeticExpression(rightSide))
					comparisonElementTypes[EXPARRAY]++;
				
				if (Utils.isMethodCall(leftSide) && Utils.isMethodCall(rightSide))
					comparisonElementTypes[METHODMETHOD]++;
				
				if (Utils.isMethodCall(leftSide) && Utils.isConstant(rightSide))
					comparisonElementTypes[METHODCONST]++;
				
				if (Utils.isConstant(leftSide) && Utils.isMethodCall(rightSide))
					comparisonElementTypes[METHODCONST]++;
				
				if (Utils.isMethodCall(leftSide) && Utils.isArray(rightSide))
					comparisonElementTypes[METHODARRAY]++;
				
				if (Utils.isArray(leftSide) && Utils.isMethodCall(rightSide))
					comparisonElementTypes[METHODARRAY]++;
				
				if (Utils.isConstant(leftSide) && Utils.isConstant(rightSide))
					comparisonElementTypes[CONSTCONST]++;
				
				if (Utils.isConstant(leftSide) && Utils.isArray(rightSide))
					comparisonElementTypes[CONSTARRAY]++;
				
				if (Utils.isArray(leftSide) && Utils.isConstant(rightSide))
					comparisonElementTypes[CONSTARRAY]++;
				
				if (Utils.isArray(leftSide) && Utils.isArray(rightSide))
					comparisonElementTypes[ARRAYARRAY]++;
			
			}
	}
		
	public ConstraintInfo(Constraint constraint){	
		initializeInfo();		
		constraintCount++;
		CodeElement leftSide = constraint.getLeftSide();
		CodeElement rightSide = constraint.getRightSide();		
		extractInfo(leftSide);
		extractInfo(rightSide);
		if (leftSide!=null && rightSide!=null){
			extractTypesOfComparison(leftSide,rightSide);
			
		}
	}
	
	public ConstraintInfo(Vector<ConstraintInfo> infos){
		initializeInfo();
		
		for (ConstraintInfo info: infos){
			constraintCount++;
			constant+=info.getConstant();			
			varInfo.increment(info.getVarInfo());
			methodCallInfo.increment(info.getMethodCallInfo());
			parInfo.increment(info.getParInfo());
			
			if (complexity<info.getComplexity())
				complexity=info.getComplexity();
			
			variables.addAll(info.getVariables());			
			methodCalls.addAll(info.getMethodCalls());				
			
			exceptions+=info.getExceptions();
			
			distinctMC+=info.getDistinctMC();
			distinctVar+=info.getDistinctVar();
			distinctTypes+=info.getDistinctTypes();
			
			for (String type: info.getTypes().keySet()){				
				if (types.containsKey(type)){					
					Float value = types.get(type);
					value+=info.getTypes().get(type);
					types.put(type, value);
				}
				else
					types.put(type, info.getTypes().get(type));
			}
			
			for (String type: info.getVarInfo().getTypeCount().keySet()){				
				if (types.containsKey(type)){
					Float value = types.get(type);
					value+=info.getVarInfo().getTypeCount().get(type);
					types.put(type, value);
				}
				else
					types.put(type, varInfo.getTypeCount().get(type));
			}
			
			//types - method call
			for (String type: info.getMethodCallInfo().getMethodTypeCount().keySet()){				
				if (types.containsKey(type)){
					Float value = types.get(type);
					value+=info.getMethodCallInfo().getMethodTypeCount().get(type);
					types.put(type, value);
				}
				else
					types.put(type, info.getMethodCallInfo().getMethodTypeCount().get(type));
			}
			
			
			
			for (int idc=0; idc<6; idc++)
			{
				comparisonDataTypes[idc]+=info.comparisonDataTypes[idc];
			}
			
			for (int idc=0; idc<15; idc++){
				comparisonElementTypes[idc]+=info.comparisonElementTypes[idc];
			}
		}
		
		int distinctObjectTypes = getDistinctTypes(methodCallInfo.getDistinctObjectTypes(),varInfo.getDistinctObjectTypes());
		//System.out.println("DOT: " + distinctObjectTypes);
		distinctTypes=types.size() +distinctObjectTypes;
		distinctVar = variables.size();
		distinctMC = methodCalls.size();
		
		
		
		
	}
			
	public VariableInfo getVarInfo() {
		return varInfo;
	}		

	public void setVarInfo(VariableInfo varInfo) {
		this.varInfo = varInfo;
	}
	
	public float getElements(){
		float total = constant+varInfo.getTotalCount()+methodCallInfo.getTotalCount()+exceptions;
		return total;
	}

	public MethodCallInfo getMethodCallInfo() {
		return methodCallInfo;
	}

	public void setMethodCallInfo(MethodCallInfo methodCallInfo) {
		this.methodCallInfo = methodCallInfo;
	}

	public float getConstant() {
		return constant;
	}

	public void setConstant(float constant) {
		this.constant = constant;
	}

	public float getComplexity() {
		return complexity;
	}

	public void setComplexity(float complexity) {
		this.complexity = complexity;
	}		

	public ParametersInfo getParInfo() {
		return parInfo;
	}

	public void setParInfo(ParametersInfo parInfo) {
		this.parInfo = parInfo;
	}

	public float getMaxMcDepth() {
		return maxMcDepth;
	}

	public void setMaxMcDepth(float maxMcDepth) {
		this.maxMcDepth = maxMcDepth;
	}	
			
	public Set<String> getVariables() {
		return variables;
	}

	public void setVariables(Set<String> variables) {
		this.variables = variables;
	}

	public Set<String> getMethodCalls() {
		return methodCalls;
	}

	public void setMethodCalls(Set<String> methodCalls) {
		this.methodCalls = methodCalls;
	}
		
	public float getConstraintCount() {
		return constraintCount;
	}

	public void setConstraintCount(float constraintCount) {
		this.constraintCount = constraintCount;
	}

	public Hashtable<String, Float> getTypes() {
		return types;
	}

	public void setTypes(Hashtable<String, Float> types) {
		this.types = types;
	}
		
	
	public float getDistinctTypes() {
		return distinctTypes;
	}

	public void setDistinctTypes(float distinctTypes) {
		this.distinctTypes = distinctTypes;
	}

	public float getDistinctMC() {
		return distinctMC;
	}

	public void setDistinctMC(float distinctMC) {
		this.distinctMC = distinctMC;
	}

	public float getDistinctVar() {
		return distinctVar;
	}

	public void setDistinctVar(float distinctVar) {
		this.distinctVar = distinctVar;
	}

	public float getExceptions() {
		return exceptions;
	}

	public void setExceptions(float exceptions) {
		this.exceptions = exceptions;
	}

	public void extractInfo(CodeElement element){		
		//if (Method.currentMethod.contains("aMsgTypeNam"))
			//System.out.println("Extract info: " + element);
		if (element == null)
			return;
		if (element instanceof Variable)		
			handleVariable(element);		
		else		
			if (element instanceof ArithmeticExpression)
				handleArithmeticExpression(element);
			else
				if (element instanceof ArrayElement)
					handleArrayElement(element);
				else
					if (element instanceof Constant)
					{												
						handleConstant(element);			
					}
					else						
						if (element instanceof MethodCall)
							handleMethodCall(element);
						else
							if (element instanceof ExceptionThrown)
								handleExceptionThrown(element);
							else
								if (element instanceof NewArray)
									handleNewArray(element);
	}
	
	public void handleNewArray(CodeElement element){
		NewArray na = (NewArray) element;
		Vector<CodeElement> sizes = na.getSize();
		for (int i=0; i<sizes.size(); i++)
			extractInfo(sizes.get(i));
	}
	
	public void handleExceptionThrown(CodeElement element){
	//	System.out.println("exception ++");
		ExceptionThrown et = (ExceptionThrown) element;
		/*
		String cn = Method.currentClassName;
		Set<String> exceptionsL = CP4SEUCP.classExceptions.get(cn);
		if (exceptionsL==null){
			exceptionsL = new HashSet<String>();			
			exceptionsL.add(et.getType());
			CP4SEUCP.classExceptions.put(cn, exceptionsL);
		}
		else
		{
			exceptionsL.add(et.getType());
			CP4SEUCP.classExceptions.replace(cn, exceptionsL);
		}*/
		//System.out.println(Method.currentClassName + " : " +et.getType());
		exceptions++;
	}
	
	public void handleConstant(CodeElement element){
		//if (mcDepth>=1)//se está dentro de uma chamada - são parâmetros
		//	parInfo.setConstants(parInfo.getConstants()+1);
	//	else
	//	{
			//System.out.println("+ constant: " + element);			
			String constantType = element.getType();
			constantType = Utils.standardType(constantType);			
			if (types.containsKey(constantType)){
				Float value = types.get(constantType);
				value+=1;
				types.put(constantType, value);
			}
			else
				types.put(constantType,1f);	
			constant++;
	//	}				
	}
	
	public void handleMethodCall(CodeElement element){
		
		//if (Method.currentMethod.contains("siteForPoint") && PathMetric.currentPath.getNodes().size()==5)
		//	System.out.println(" METHOD COUNT: " + element);
		
		mcDepth++;
		//System.out.println(">>depth: " + mcDepth);
		MethodCall mcall = (MethodCall) element;
		methodCalls.add(mcall.toString());
		distinctMC = methodCalls.size();
		//if (mcDepth<=1){
			//System.out.println("+ mcall: "+ mcall);
			
			CodeElement object = mcall.getObject();
			//System.out.println("   Object: "+ object +  "  class: " + object.getClass());
			if ((object!=null) && !object.toString().equals("this")){
				
				extractInfo(object);				
			}
			
			methodCallInfo.increment(mcall);
			
			for (int i=0; i<mcall.getParameters().size(); i++){
				extractInfo(mcall.getParameters().get(i));
			}
		//}
	//	else //se depth>1, então o methodCall é parâmetro		
			//parInfo.getMethodCallInfo().increment(mcall);		
		
		if (maxMcDepth<mcDepth)
			maxMcDepth=mcDepth;		
		//System.out.println("<<depth: " + mcDepth);
		mcDepth--;
	}
	
	
	public void handleArrayElement(CodeElement element){
		ArrayElement arrayElement = (ArrayElement) element;
		extractInfo(arrayElement.getVar());
		//Maintenance.gambiarra("não tratar index do array");
		extractInfo(arrayElement.getIndex());		
	}
	
	public void handleArithmeticExpression(CodeElement element){
		ArithmeticExpression expression = (ArithmeticExpression) element;
		for (int i=0; i<expression.getExpressionElements().size(); i++)
			extractInfo(expression.getExpressionElements().get(i));
	}
	
	public void handleVariable(CodeElement element){	
		//System.out.println("  Extract info var: " + element);
		Variable var = (Variable) element;
		variables.add(var.toString());
		distinctVar = variables.size();
		//if (mcDepth<1){
			//System.out.println("+ var: "+var);
		if (var.getObject()!=null && !var.getObject().toString().equals("this"))
			extractInfo(var.getObject());
		varInfo.increment(var);			
		//}
	//	else
		//	parInfo.getVarInfo().increment(var);						
	}
	
	public void sumDataAndTypeComparison(Vector<ConstraintInfo> constraintsInfo){
		for (ConstraintInfo info: constraintsInfo){
			for (int idc=0; idc<6; idc++)
			{
				comparisonDataTypes[idc]+=info.comparisonDataTypes[idc];
			}
			
			for (int idc=0; idc<15; idc++){
				comparisonElementTypes[idc]+=info.comparisonElementTypes[idc];
			}
		}
		/*
		for (int i=0; i<6; i++)
			System.out.print(comparisonDataTypes[i]+" ");
		System.out.println();
		*/
	}
	
	public void calculateMean(Vector<ConstraintInfo> constraintsInfo){
		Vector<ParametersInfo> vetParInfo = new Vector<ParametersInfo>();
		Vector<VariableInfo> vetVarInfo = new Vector<VariableInfo>();
		Vector<MethodCallInfo> vetMcInfo = new Vector<MethodCallInfo>();
		float size = constraintsInfo.size();
		for (ConstraintInfo info: constraintsInfo){
			this.constraintCount+=info.getConstraintCount();
			this.exceptions+=info.getExceptions();
			this.constant+=info.getConstant();
			this.complexity+=info.getComplexity();	
			this.distinctMC+=info.getDistinctMC();
			this.distinctVar+=info.getDistinctVar();
			this.distinctTypes+=info.getDistinctTypes();
			vetParInfo.add(info.getParInfo());
			vetVarInfo.add(info.getVarInfo());
			vetMcInfo.add(info.getMethodCallInfo());
			
			
			for (String type: info.getTypes().keySet()){
				if (types.containsKey(type)){
					Float value = types.get(type);
					value+=info.getTypes().get(type);
					types.put(type, value);
				}
				else
					types.put(type, info.getTypes().get(type));
			}
			
		}
		
		this.constraintCount/=size;
		this.exceptions/=size;
		this.constant/=size;
		this.complexity/=size;
		this.distinctMC/=size;
		this.distinctVar/=size;
		this.distinctTypes/=size;
		

		for (int idc=0; idc<6; idc++)
			comparisonDataTypes[idc]/=size;
		for (int idc=0; idc<15; idc++)
			comparisonElementTypes[idc]/=size;
	
		this.varInfo = new VariableInfo();
		this.varInfo.calculateMean(vetVarInfo);
		this.methodCallInfo = new MethodCallInfo();
		methodCallInfo.calculateMean(vetMcInfo);
		this.parInfo = new ParametersInfo();
		this.parInfo.calculateMean(vetParInfo);
		
		for (String type: this.types.keySet()){
			Float value = types.get(type);
			value/=size;
			types.put(type, value);					
		}	
		
		
		float bt = 0;
		if (types.get("byte")!=null)
			bt=types.get("byte");
		
		float ch = 0;
		if (types.get("char")!=null)
			ch=types.get("char");

		float sh = 0;
		if (types.get("short")!=null)
			sh=types.get("short");
		
		float it = 0;
		if (types.get("int")!=null)
			it=types.get("int");
		
		float lg = 0;
		if (types.get("long")!=null)
			lg=types.get("long");
		
		float ft=0;		
		if (types.get("float")!=null)
			ft=types.get("float");
		
		float db=0;
		if (types.get("double")!=null)
			db=types.get("double");
		
		float bool=0;
		if (types.get("boolean")!=null)
			bool=types.get("boolean");
		
		float nl=0;
		if (types.get("null")!=null)
			nl=types.get("null");
		
		float st=0;
		if (types.get("string")!=null)
			st=types.get("string");
		
		//methodCallInfo.getArrayTypes()
		float ot=methodCallInfo.getObjectTypes()+varInfo.getObjectTypes();
		
		float at=methodCallInfo.getArrayTypes() + varInfo.getArrayTypeCount();
		
		if (bt+ch+sh+it+lg+bool > 0)
			Info.ints++;
		if (ft+db > 0)
			Info.floats++;
		if (ot>0)
			Info.objects++;
		if (at>0)
			Info.arrays++;
		if (st>0)
			Info.strings++;
		if (exceptions>0)
			Info.exceptions++;		
		
		float total = constant+varInfo.getTotalCount()+methodCallInfo.getTotalCount()+exceptions;
		if ((bt+ch+sh+it+lg+bool)/total>=0.999)
			Info.onlyint++;
		if ((ft+db)/total>=0.999)
			Info.onlyfloat++;
		if ((st/total)>=0.999) {
			Info.onlystring++;
			//System.out.println("ONLY STRING: " + CP4SE.currentProjectName+"::"+Method.currentClassName+"::"+Method.currentMethodName);
		}
		if ((at/total)>=0.999)
			Info.onlyarray++;
		if ((ot/total)>=0.999)
			Info.onlyobject++;
		if ((exceptions/total)>=0.999)
			Info.onlyexception++;
		
		if (methodCallInfo.getIntraClassCount()>0)
			Info.inner++;
		if (methodCallInfo.getInterClassCount()>0)
			Info.inter++;
		if (methodCallInfo.getExternalLibraryCount()>0)
			Info.external++;
		
		Info.tot++;
		if (methodCallInfo.getTotalCount()>0)
			Info.calls++;
		
	}
	
	/*
	 *
	protected ParametersInfo parInfo;
	protected VariableInfo varInfo;			
	protected MethodCallInfo methodCallInfo;	
		
	 */
	
	public float getTypeAmount(String type){
		float t=0;		
		if (types.get(type)!=null)
			t=types.get(type);
		
		return t;
	}
	
	protected int getDistinctTypes(Set<String> set1, Set<String> set2){
		set1.addAll(set2);
		return set1.size();
	}
	
	public float[] getComparisonDataTypes(){
		return comparisonDataTypes;
	}
	
	public float[] getComparisonElementTypes(){
		return comparisonElementTypes;
	}
}



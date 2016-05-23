package profiling.constraint.analysis;

public class ConstraintInfoToFile extends ConstraintInfo{
	
	//public static Info sumInfo = new Info();
	
	public ConstraintInfoToFile(ConstraintInfo info){
		super();
		this.exceptions = info.getExceptions();
		this.complexity = info.getComplexity();
		this.constant = info.getConstant();
		this.maxMcDepth = info.getMaxMcDepth();
		this.methodCallInfo = info.getMethodCallInfo();
		this.methodCalls = info.getMethodCalls();
		this.parInfo = info.getParInfo();
		this.types = info.getTypes();
		this.variables = info.getVariables();
		this.varInfo = info.getVarInfo();		
		this.constraintCount=info.getConstraintCount();
		this.distinctMC = info.getDistinctMC();
		this.distinctVar = info.getDistinctVar();
		this.distinctTypes = info.getDistinctTypes();
	}
	
	public static String head(){
		String ret[] = 
				new String[]
					{
				"Project",
				"Package",
				"Class",
				"Method",
				"Nodes",
				"Edges",
				"CC", //complexidade ciclomática
				"Paths",
				"Loops",//número de laços
				"Loop depth",
				"Path ID",
				"Path size",
				"Constraint size", //qtas constraints tem o caminho
				
				"Elements", //total de elementos de todas as constraints juntas
								
				"Constants",
				
				"Exceptions",
				"bin-Exceptions",
				
				"Variables",//número total de variáveis
				"Fields", //qtas variáveis são atributos
				"Locals",//qtas variáveis são locais
				"Statics",//qtas variáveis são estáticas - atributos de classe
				"Inputs", //qtas variáveis são parâmetros de entrada
				"Objects_Var", //qtas variáveis são objetos
				"Arrays_Var", //qtas variáveis são arrays
				"Primitive_Var", //qtas variáveis são de tipos primitivos	
				"String_Var",
				"Distinct Var", //qtas variáveis distintas são utilizadas								
				
				"Calls", //número total de chamada a métodos				
				"Intras", //qtas chamadas são intraclasse
				"Inters", //qtas chamadas são interclasse
				"External",//qtas chamadas são para bibliotecas externas,
				"Statics", //qtas chamadas são para métodos estáticos
				"Parameters", //qtos parâmetros são utilizados em todas as chamadas
				"Object_RET_CALL", //qtas chamadas retornam um objeto
				"Primitive_RET_CALL", //qtas chamadas retornam um tipo primitivo				
				"Array_RET_CALL",//qtas chamadas retornam um array				
				"String_RET_CALL",
				"Distinct calls", //qtas chamadas distintas são feitas
				
				"Distinct types", //qtos tipos diferentes estão envolvidos
				"byte",
				"char",
				"short",
				"int",
				"long",
				"float",
				"double",
				"boolean",
				"null",
				"String",
				"Object",
				"Array"
				};
		
		String items="";
		for (int i=0; i<ret.length-1; i++)			
			items+=ret[i]+",";
		items+=ret[ret.length-1];		
		return items;
	}
	
	public static String headMethod(){
		String ret[] = 
				new String[]
					{
				"Project",
				"Package",
				"Class",
				"Method",
				"Nodes",
				"Edges",
				"CC", //complexidade ciclomática
				"Paths",
				"Loops",//número de laços
				"Loop depth",
				"Path size",
				"Constraint size", //qtas constraints tem o caminho
				
				"Elements", //total de elementos de todas as constraints juntas
								
				"Constants",
				
				"Exceptions",
				"bin-Exceptions",
				
				"Variables",//número total de variáveis
				"Fields", //qtas variáveis são atributos
				"Locals",//qtas variáveis são locais
				"Statics",//qtas variáveis são estáticas - atributos de classe
				"Inputs", //qtas variáveis são parâmetros de entrada
				"Objects_Var", //qtas variáveis são objetos
				"Arrays_Var", //qtas variáveis são arrays
				"Primitive_Var", //qtas variáveis são de tipos primitivos	
				"String_Var",
				"Distinct Var", //qtas variáveis distintas são utilizadas								
				
				"Calls", //número total de chamada a métodos				
				"Intras", //qtas chamadas são intraclasse
				"Inters", //qtas chamadas são interclasse
				"External",//qtas chamadas são para bibliotecas externas,
				"Statics", //qtas chamadas são para métodos estáticos
				"Parameters", //qtos parâmetros são utilizados em todas as chamadas
				"Object_RET_CALL", //qtas chamadas retornam um objeto
				"Primitive_RET_CALL", //qtas chamadas retornam um tipo primitivo				
				"Array_RET_CALL",//qtas chamadas retornam um array				
				"String_RET_CALL",
				"Distinct calls", //qtas chamadas distintas são feitas
				
				"Distinct types", //qtos tipos diferentes estão envolvidos
				"byte",
				"char",
				"short",
				"int",
				"long",
				"float",
				"double",
				"boolean",
				"null",
				"String",
				"Object",
				"Array"
				};
		
		String items="";
		for (int i=0; i<ret.length-1; i++)			
			items+=ret[i]+",";
		items+=ret[ret.length-1];		
		return items;
	}
	
	public static String headClass(){
		String ret[] = 
				new String[]
					{
				"Project",
				"Package",
				"Class",
				"Nodes",
				"Edges",
				"CC", //complexidade ciclomática
				"Paths",
				"Loops",//número de laços	
				"Loop depth",
				"Path size",
				"Constraint size", //qtas constraints tem o caminho
				
				"Elements", //total de elementos de todas as constraints juntas
								
				"Constants",
				
				"Exceptions",
				"bin-Exceptions",
				
				"Variables",//número total de variáveis
				"Fields", //qtas variáveis são atributos
				"Locals",//qtas variáveis são locais
				"Statics",//qtas variáveis são estáticas - atributos de classe
				"Inputs", //qtas variáveis são parâmetros de entrada
				"Objects_Var", //qtas variáveis são objetos
				"Arrays_Var", //qtas variáveis são arrays
				"Primitive_Var", //qtas variáveis são de tipos primitivos	
				"String_Var",
				"Distinct Var", //qtas variáveis distintas são utilizadas								
				
				"Calls", //número total de chamada a métodos				
				"Intras", //qtas chamadas são intraclasse
				"Inters", //qtas chamadas são interclasse
				"External",//qtas chamadas são para bibliotecas externas,
				"Statics", //qtas chamadas são para métodos estáticos
				"Parameters", //qtos parâmetros são utilizados em todas as chamadas
				"Object_RET_CALL", //qtas chamadas retornam um objeto
				"Primitive_RET_CALL", //qtas chamadas retornam um tipo primitivo				
				"Array_RET_CALL",//qtas chamadas retornam um array				
				"String_RET_CALL",
				"Distinct calls", //qtas chamadas distintas são feitas
				
				"Distinct types", //qtos tipos diferentes estão envolvidos
				"byte",
				"char",
				"short",
				"int",
				"long",
				"float",
				"double",
				"boolean",
				"null",
				"String",
				"Object",
				"Array"
				};
		
		String items="";
		for (int i=0; i<ret.length-1; i++)			
			items+=ret[i]+",";
		items+=ret[ret.length-1];		
		return items;
	}
	
	public static String headProject(){
		String ret[] = 
				new String[]
					{
				"Project",										
				"Paths",//qtde de caminhos
				//"Loops",//número de laços				
				"Path size",//tamanho dos caminhos (média)
				"Constraint size", //qtas constraints tem o caminho
				
				"Elements", //total de elementos de todas as constraints juntas
								
				"Constants",
				
				"Exceptions",
				"bin-Exceptions",
				
				"Variables",//número total de variáveis
				"Fields", //qtas variáveis são atributos
				"Locals",//qtas variáveis são locais
				"Statics",//qtas variáveis são estáticas - atributos de classe
				"Inputs", //qtas variáveis são parâmetros de entrada
				"Objects_Var", //qtas variáveis são objetos
				"Arrays_Var", //qtas variáveis são arrays
				"Primitive_Var", //qtas variáveis são de tipos primitivos	
				"String_Var",
				"Distinct Var", //qtas variáveis distintas são utilizadas								
				
				"Calls", //número total de chamada a métodos				
				"Intras", //qtas chamadas são intraclasse
				"Inters", //qtas chamadas são interclasse
				"External",//qtas chamadas são para bibliotecas externas,
				"Statics", //qtas chamadas são para métodos estáticos
				"Parameters", //qtos parâmetros são utilizados em todas as chamadas
				"Object_RET_CALL", //qtas chamadas retornam um objeto
				"Primitive_RET_CALL", //qtas chamadas retornam um tipo primitivo				
				"Array_RET_CALL",//qtas chamadas retornam um array				
				"String_RET_CALL",
				"Distinct calls", //qtas chamadas distintas são feitas
				
				"Distinct types", //qtos tipos diferentes estão envolvidos
				"byte",
				"char",
				"short",
				"int",
				"long",
				"float",
				"double",
				"boolean",
				"null",
				"String",
				"Object",
				"Array"
				};
		
		String items="";
		for (int i=0; i<ret.length-1; i++)			
			items+=ret[i]+",";
		items+=ret[ret.length-1];		
		return items;
	}
	

	public String toOneLineString(){
		String ret = "";
		ret+=this.constraintCount+",";
		float total = constant+varInfo.getTotalCount()+methodCallInfo.getTotalCount()+exceptions;
		ret+=total+",";
				
		ret+=constant+",";
		ret+=exceptions+",";
		if (exceptions>0)
			ret+="1,";
		else
			ret+="0,";
		
		ret+=varInfo.getTotalCount()+",";
		ret+=varInfo.getInstanceFieldCount() +",";
		ret+=varInfo.getLocalVarCount() +",";
		ret+=varInfo.getClassFieldCount() +",";
		ret+=varInfo.getInputDataCount() +",";
		ret+=varInfo.getObjectTypes() +",";
		ret+=varInfo.getArrayTypeCount() +",";		
		ret+=varInfo.getPrimitiveTypes() + ",";
		if (varInfo.getTypeCount().get("string")==null)
			ret+="0,";
		else
			ret+=varInfo.getTypeCount().get("string")+",";
			
		ret+=distinctVar+","; //número de varíaveis distintas
		
		ret+=methodCallInfo.getTotalCount()+",";
		ret+=methodCallInfo.getIntraClassCount() + ",";
		ret+=methodCallInfo.getInterClassCount() + ",";
		ret+=methodCallInfo.getExternalLibraryCount() + ",";
		ret+=methodCallInfo.getStaticCallCount() + ",";
		ret+=methodCallInfo.getParametersCount() + ",";
		ret+=methodCallInfo.getObjectTypes()+",";
		ret+=methodCallInfo.getPrimitiveTypes()+",";
		ret+=methodCallInfo.getArrayTypes()+",";		

		if (methodCallInfo.getMethodTypeCount().get("string")==null)
			ret+="0,";
		else
			ret+=methodCallInfo.getMethodTypeCount().get("string")+",";
		
		ret+=distinctMC + ",";
		
		float bt = 0;
		if (types.get("byte")!=null)
			bt=types.get("byte");
		ret+=distinctTypes+ ",";
		if (types.get("byte")!=null)
			ret+=types.get("byte") + ",";
		else ret+="0,";
		
		
		float ch = 0;
		if (types.get("char")!=null)
			ch=types.get("char");
		if (types.get("char")!=null)
			ret+=types.get("char") + ",";
		else ret+="0,";
		
		float sh = 0;
		if (types.get("short")!=null)
			sh=types.get("short");
		if (types.get("short")!=null)
			ret+=types.get("short") + ",";
		else ret+="0,";
		
		float it = 0;
		if (types.get("int")!=null)
			it=types.get("int");
		if (types.get("int")!=null)
			ret+=types.get("int") + ",";
		else ret+="0,";
		
		float lg = 0;
		if (types.get("long")!=null)
			lg=types.get("long");
		if (types.get("long")!=null)
			ret+=types.get("long") + ",";
		else ret+="0,";
		
		float ft=0;		
		if (types.get("float")!=null)
			ft=types.get("float");
		if (types.get("float")!=null)
			ret+=types.get("float") + ",";
		else ret+="0,";
		
		float db=0;
		if (types.get("double")!=null)
			db=types.get("double");
		if (types.get("double")!=null)
			ret+=types.get("double") + ",";
		else ret+="0,";
		
		float bool=0;
		if (types.get("boolean")!=null)
			bool=types.get("boolean");
		if (types.get("boolean")!=null)
			ret+=types.get("boolean") + ",";
		else ret+="0,";
		
		float nl=0;
		if (types.get("null")!=null)
			nl=types.get("null");
		if (types.get("null")!=null)
			ret+=types.get("null") + ",";
		else ret+="0,";
		
		float st=0;
		if (types.get("string")!=null)
			st=types.get("string");
		if (types.get("string")!=null)
			ret+=types.get("string") + ",";
		else ret+="0,";
		
		//methodCallInfo.getArrayTypes()
		float ot=methodCallInfo.getObjectTypes()+varInfo.getObjectTypes();
		ret+=String.valueOf(methodCallInfo.getObjectTypes()+varInfo.getObjectTypes())+",";
		
		float at=methodCallInfo.getArrayTypes() + varInfo.getArrayTypeCount();
		ret+=String.valueOf(methodCallInfo.getArrayTypes() + varInfo.getArrayTypeCount());
		
		/*
		if (bt+ch+sh+it+lg+bool > 0)
			sumInfo.ints++;
		if (ft+db > 0)
			sumInfo.floats++;
		if (ot>0)
			sumInfo.objects++;
		if (at>0)
			sumInfo.arrays++;
		if (st>0)
			sumInfo.strings++;
		if (exceptions>0)
			sumInfo.exceptions++;		
		
		if ((bt+ch+sh+it+lg+bool)/total==1)
			sumInfo.onlyint++;
		if ((ft+db)/total>=0.99)
			sumInfo.onlyfloat++;
		if ((st/total)>=0.99)
			sumInfo.onlystring++;
		if ((at/total)>=0.99)
			sumInfo.onlyarray++;
		if ((ot/total)>=0.99)
			sumInfo.onlyobject++;
		if ((exceptions/total)>=0.99)
			sumInfo.onlyexception++;
		
		if (methodCallInfo.getIntraClassCount()>0)
			sumInfo.inner++;
		if (methodCallInfo.getInterClassCount()>0)
			sumInfo.inter++;
		if (methodCallInfo.getExternalLibraryCount()>0)
			sumInfo.external++;
		
		sumInfo.tot++;
		if (methodCallInfo.getTotalCount()>0)
			sumInfo.hasMethodCall++;*/
		return ret;
	}
	
	
	public String toOneLineString_short(){
		String ret = "";
		ret+=this.constraintCount+","; //Number of constraints
		float total = constant+varInfo.getTotalCount()+methodCallInfo.getTotalCount()+exceptions;
		ret+=total+",";//Number of elements
				
		ret+=constant+","; //Number of constants
		ret+=varInfo.getTotalCount()+","; //Number of variables
		ret+=exceptions+","; //Number of exceptions
				
		ret+=methodCallInfo.getTotalCount()+","; //Number of method calls
		ret+=methodCallInfo.getIntraClassCount() + ","; //Number of inner method calls
		ret+=methodCallInfo.getInterClassCount() + ","; //Number of inter method calls
		ret+=methodCallInfo.getExternalLibraryCount() + ","; //Number of external calls
		
		
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
							
		float integers = bt+ch+sh+lg+it+bool;
		
		float floats = ft+db;
		
		float ot=methodCallInfo.getObjectTypes()+varInfo.getObjectTypes();
				
		float at=methodCallInfo.getArrayTypes() + varInfo.getArrayTypeCount();
		
		
		ret+=integers+",";
		
		ret+=floats+",";
		
		ret+=nl+",";
		
		ret+=st+",";
		
		ret+=at+",";
		
		ret+=ot;
		
		
		/*
		if (bt+ch+sh+it+lg+bool > 0)
			sumInfo.ints++;
		if (ft+db > 0)
			sumInfo.floats++;
		if (ot>0)
			sumInfo.objects++;
		if (at>0)
			sumInfo.arrays++;
		if (st>0)
			sumInfo.strings++;
		if (exceptions>0)
			sumInfo.exceptions++;		
		
		if ((bt+ch+sh+it+lg+bool)/total==1)
			sumInfo.onlyint++;
		if ((ft+db)/total>=0.99)
			sumInfo.onlyfloat++;
		if ((st/total)>=0.99)
			sumInfo.onlystring++;
		if ((at/total)>=0.99)
			sumInfo.onlyarray++;
		if ((ot/total)>=0.99)
			sumInfo.onlyobject++;
		if ((exceptions/total)>=0.99)
			sumInfo.onlyexception++;
		
		if (methodCallInfo.getIntraClassCount()>0)
			sumInfo.inner++;
		if (methodCallInfo.getInterClassCount()>0)
			sumInfo.inter++;
		if (methodCallInfo.getExternalLibraryCount()>0)
			sumInfo.external++;
		
		sumInfo.tot++;
		if (methodCallInfo.getTotalCount()>0)
			sumInfo.hasMethodCall++;*/
		return ret;
	}
	
	public String toVerboseString(){
		String ret = "";
		ret+="Sequence size: " + this.constraintCount+"\n";
		float total = constant+varInfo.getTotalCount()+methodCallInfo.getTotalCount()+exceptions;
		ret+="Elements: " + total+"\n";
				
		ret+="Constantes: "+ constant+"\n";
		ret+="Exceptions: " + exceptions+"\n";
		if (exceptions>0)
			ret+="Bin exceptions: 1\n";
		else
			ret+="Bin exceptions: 0\n";
		
		ret+="\n";
		ret+="Variables: " + varInfo.getTotalCount()+"\n";
		ret+="  Fields: " + varInfo.getInstanceFieldCount() +"\n";
		ret+="  Locals: " + varInfo.getLocalVarCount() +"\n";
		ret+="  Statics: " + varInfo.getClassFieldCount() +"\n";
		ret+="  Input: " + varInfo.getInputDataCount() +"\n";
		ret+="  Objects: " + varInfo.getObjectTypes() +"\n";
		ret+="  Arrays: " + varInfo.getArrayTypeCount() +"\n";
		ret+="  Primitive: " + varInfo.getPrimitiveTypes() + "\n";
		
		if (varInfo.getTypeCount().get("string")==null)
			ret+="String type: 0\n";
		else
			ret+="String type: " + varInfo.getTypeCount().get("string")+"\n";
		
		ret+="Distinct variables: " + distinctVar+"\n"; //número de varíaveis distintas
		ret+="\n";
		ret+="Methods: " + methodCallInfo.getTotalCount()+"\n";
		ret+="  Intra: " + methodCallInfo.getIntraClassCount() + "\n";
		ret+="  Inter: " + methodCallInfo.getInterClassCount() + "\n";
		ret+="  External: " + methodCallInfo.getExternalLibraryCount() + "\n";
		ret+="  Static: " + methodCallInfo.getStaticCallCount() + "\n";
		ret+="  Parameters (total): " + methodCallInfo.getParametersCount() + "\n";
		ret+="  Object type return: " + methodCallInfo.getObjectTypes()+"\n";
		ret+="  Primitive type return: " + methodCallInfo.getPrimitiveTypes()+"\n";
		ret+="  Array type return: " + methodCallInfo.getArrayTypes()+"\n";		
		
		if (methodCallInfo.getMethodTypeCount().get("string")==null)
			ret+="String type: 0\n";
		else
			ret+="String type: " + methodCallInfo.getMethodTypeCount().get("string")+"\n";
		
		ret+="Distinct method calls: " + distinctMC + "\n"; //make sense?
		ret+="\n";		
		
		
		ret+="Distinct types: "+distinctTypes+ "\n";
		if (types.get("byte")!=null)
			ret+="  Byte: " + types.get("byte") + "\n";
		else ret+="  0\n";
		
		if (types.get("char")!=null)
			ret+="  Char: " + types.get("char") + "\n";
		else ret+="  0\n";
		
		if (types.get("short")!=null)
			ret+="  Short" + types.get("short") + "\n";
		else ret+="  0\n";
		
		if (types.get("int")!=null)
			ret+="  Int: " + types.get("int") + "\n";
		else ret+="  0\n";
		
		if (types.get("long")!=null)
			ret+="  Long: " + types.get("long") + "\n";
		else ret+="  0\n";
		
		if (types.get("float")!=null)
			ret+="  Float: " + types.get("float") + "\n";
		else ret+="  0\n";
		
		if (types.get("double")!=null)
			ret+="  Double: " + types.get("double") + "\n";
		else ret+="  0\n";
		
		if (types.get("boolean")!=null)
			ret+="  Boolean: " + types.get("boolean") + "\n";
		else ret+="  0\n";
		
		if (types.get("null")!=null)
			ret+="  Null: " + types.get("null") + "\n";
		else ret+="  0\n";
		
		if (types.get("string")!=null)
			ret+="  String: " + types.get("string") + "\n";
		else ret+="  0\n";
		
		ret+="  Object return type: " + String.valueOf(methodCallInfo.getObjectTypes()+varInfo.getObjectTypes())+"\n";
		
		ret+="  Array types: " + String.valueOf(methodCallInfo.getArrayTypes() + varInfo.getArrayTypeCount());
		return ret;
	}		

	
	public String toPaperString_metrics(String pathId){
		String ret = pathId+" & ";
		ret+=this.constraintCount+" & ";
		float total = constant+varInfo.getTotalCount()+methodCallInfo.getTotalCount();
		ret+=total+" & ";
				
		ret+=constant+" & ";
		ret+=exceptions+" &      ";
		
		ret+=varInfo.getTotalCount()+" & ";
		ret+=distinctVar+" & "; //número de varíaveis distintas
		ret+=varInfo.getInstanceFieldCount() +" & ";
		ret+=varInfo.getLocalVarCount() +" & ";
		ret+=varInfo.getClassFieldCount() +" & ";
		ret+=varInfo.getInputDataCount() +" & ";
		ret+=varInfo.getObjectTypes() +" & ";
		ret+=varInfo.getArrayTypeCount() +" & ";
		ret+=varInfo.getPrimitiveTypes() + " &";
		if (varInfo.getTypeCount().get("string")==null)
			ret+="0           &";
		else
			ret+=varInfo.getTypeCount().get("string")+" &      ";
		
		ret+=methodCallInfo.getTotalCount()+" & ";
		ret+=distinctMC + " & ";
		ret+=methodCallInfo.getIntraClassCount() + " & ";
		ret+=methodCallInfo.getInterClassCount() + " & ";
		ret+=methodCallInfo.getExternalLibraryCount() + " & ";
		ret+=methodCallInfo.getStaticCallCount() + " & ";		
		ret+=methodCallInfo.getObjectTypes()+" & ";		
		ret+=methodCallInfo.getArrayTypes()+" & ";
		ret+=methodCallInfo.getPrimitiveTypes()+" & ";		
		if (methodCallInfo.getMethodTypeCount().get("string")==null)
			ret+="0";
		else
			ret+=methodCallInfo.getMethodTypeCount().get("string");
		
		return ret;
	}
	
	public String toPaperString_types(String pathId){

		String ret=pathId+" & ";		
		ret+=distinctTypes+ " & ";
		if (types.get("byte")!=null)
			ret+=types.get("byte") + " & ";
		else ret+="0 & ";
		
		if (types.get("char")!=null)
			ret+=types.get("char") + " & ";
		else ret+="0 & ";
		
		if (types.get("short")!=null)
			ret+=types.get("short") + " & ";
		else ret+="0 & ";
		
		if (types.get("int")!=null)
			ret+=types.get("int") + " & ";
		else ret+="0 & ";
		
		if (types.get("float")!=null)
			ret+=types.get("float") + " & ";
		else ret+="0 & ";
		
		if (types.get("double")!=null)
			ret+=types.get("double") + " & ";
		else ret+="0 & ";
		
		if (types.get("boolean")!=null)
			ret+=types.get("boolean") + " & ";
		else ret+="0 & ";
		
		if (types.get("null")!=null)
			ret+=types.get("null") + " & ";
		else ret+="0 & ";
		
		if (types.get("string")!=null)
			ret+=types.get("string") + " & ";
		else ret+="0 & ";
		
		ret+=String.valueOf(methodCallInfo.getObjectTypes()+varInfo.getObjectTypes())+ " & ";
		ret+=String.valueOf(methodCallInfo.getArrayTypes() + varInfo.getArrayTypeCount());
		return ret;
	}

}

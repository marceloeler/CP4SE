package profiling.constraint.analysis.info;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Vector;

import profiling.constraint.analysis.stack.ArithmeticExpression;
import profiling.constraint.analysis.stack.ArrayElement;
import profiling.constraint.analysis.stack.ArrayVar;
import profiling.constraint.analysis.stack.CodeElement;
import profiling.constraint.analysis.stack.Constant;
import profiling.constraint.analysis.stack.MethodCall;
import profiling.constraint.analysis.stack.Variable;

public class Utils {
	
	/*
	 * typedef union jvalue { 
    jboolean z; 
    jbyte    b; 
    jchar    c; 
    jshort   s; 
    jint     i; 
    jlong    j; 
    jfloat   f; 
    jdouble  d; 
    jobject  l; 
} jvalue;
	 */
	
	private static BufferedReader nonLinearMethodsFile = null;
	private static Vector<String> nonLinearMethods = null;
	private static String[] nonLinearMethodsVet = null;
		
	public static boolean isInt(String type){
		if (type.equals("int") ||
				type.equals("long") ||
				type.equals("short") ||
				type.equals("byte"))
			return true;
		return false;
	}
	
	public static boolean isFloat(String type){
		if (type.equals("float") ||
				type.equals("double"))
			return true;
		return false;
	}
	
	public static boolean isRef(String type){
		if (type.charAt(0)=='L')
			return true;
		return false;
	}
	
	public static boolean isBool(String type){
		if (type.equals("boolean"))
			return true;
		return false;
	}
	
	public static boolean isNull(String type){
		if (type.equals("null"))
			return true;
		return false;
	}
	
	public static String standardType(String type){	
		if (type==null)
			return "null";
		if (type.equals("D"))
			return "double";
		if (type.equals("F"))
			return "float";
		if (type.equals("J"))
			return "long";
		if (type.equals("Z"))
			return "boolean";
		if (type.equals("B"))
			return "byte";
		if (type.equals("S"))
			return "short";
		if (type.equals("I"))
			return "int";	
		if (type.contains("Ljava/lang/String"))
			return "string";		
		return type;
	}
	
	public static boolean isPrimitiveType(String type){
		Vector<String> primitiveTypes = new Vector<String>();
		primitiveTypes.add("double");
		primitiveTypes.add("float");
		primitiveTypes.add("boolean");
		primitiveTypes.add("long");
		primitiveTypes.add("short");
		primitiveTypes.add("byte");
		primitiveTypes.add("int");
		
		return primitiveTypes.contains(type);
	}
	
	public static boolean isArrayType(String type){
		return type.contains("[");
	}
	
	public static boolean isNonLinearMethod(String methodCall){
		
		if (nonLinearMethodsVet==null){
		nonLinearMethodsVet = new String[]{
			"Math.cbrt",
			"Math.exp",
			"Math.expm1",
			"Math.hypot",
			"Math.log",
			"Math.log10",
			"Math.log1p",
			"Math.pow",
			"Math.scalb",
			"Math.sqrt",
			"Math.acos",
			"Math.asin",
			"Math.atan",
			"Math.atan2",
			"Math.cos",
			"Math.cosh",
			"Math.sin",
			"Math.sinh",
			"Math.tan",
			"Math.tanh",
			"StrictMath.cbrt",
			"StrictMath.exp",
			"StrictMath.expm1",
			"StrictMath.hypot",
			"StrictMath.log",
			"StrictMath.log10",
			"StrictMath.log1p",
			"StrictMath.pow",
			"StrictMath.scalb",
			"StrictMath.sqrt",
			"StricMath.acos",
			"StricMath.asin",
			"StricMath.atan",
			"StricMath.atan2",
			"StricMath.cos",
			"StricMath.cosh",
			"StricMath.sin",
			"StricMath.sinh",
			"StricMath.tan",
			"StricMath.tanh",};
		}
		/*
		if (nonLinearMethods==null){
			try {
				//System.out.println("carregando metodos");
				BufferedReader nonLinearMethodsFile =  new BufferedReader(new FileReader("C:\\Users\\Marcelo\\Dropbox\\Projects\\Eclipse\\ConstraintProfiling\\properties\\nonLinearMethods.txt"));
				nonLinearMethods = new Vector<String>();
				String line="";
				while (line!=null){
					line = nonLinearMethodsFile.readLine();
					//System.out.println(line);
					if (line!=null)
						nonLinearMethods.add(line);
				}
				
				nonLinearMethodsFile.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		*/
		
		//if (nonLinearMethods!=null){
			
		if (nonLinearMethodsVet!=null){
			String[] call = methodCall.split(".");
			
			for (String mc: nonLinearMethodsVet){
				
				if (mc.contains(".")){
					if (methodCall.contains(mc))
						return true;
				}
				else
				{
					if (mc.contains(call[1])) //only the method name - ignore the name of the object or of the class
						return true;
				}
			}
		}
		
		
		return false;
	}
	
	public static boolean isVar(CodeElement element){
		return element instanceof Variable;
	}
	
	public static boolean isArray(CodeElement element){
		return (element instanceof ArrayElement || element instanceof ArrayVar);
	}
	
	public static boolean isArrayVar(CodeElement element){
		return element instanceof ArrayVar;
	}
	
	public static boolean isArrayElement(CodeElement element){
		return element instanceof ArrayElement;
	}
	
	public static boolean isConstant(CodeElement element){
		return element instanceof Constant;
	}
	
	public static boolean isArithmeticExpression(CodeElement element){
		return element instanceof ArithmeticExpression;
	}
	
	public static boolean isMethodCall(CodeElement element){
		return element instanceof MethodCall;
	}
	

}

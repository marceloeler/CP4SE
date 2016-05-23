package profiling.constraint.analysis.stack;

import java.util.EmptyStackException;
import java.util.Stack;
import java.util.Vector;

public class ArithmeticExpression extends CodeElement {
	
	private boolean parenthesis;
	
	private Vector<CodeElement> expressionElements;
	
	public ArithmeticExpression(){
		expressionElements = new Vector<CodeElement>();		
		parenthesis = false;		
		value = "";
	}
	
	public ArithmeticExpression(String value) {		
		super(value);
		expressionElements = new Vector<CodeElement>();
		type = "expression";
		parenthesis = false;
	}
	
	public void addElement(CodeElement element){
		expressionElements.add(element);		
	}
	
	public Vector<CodeElement> getExpressionElements(){
		return expressionElements;
	}
	
	public void setParenthesis(boolean parenthesis){
		this.parenthesis = parenthesis;
		handleParenthesis();		
	}
	
	public boolean getParenthesis(){
		return this.parenthesis;
	}
	
	public String toString(){
		String ret = "";
		for (CodeElement el: this.expressionElements){
			ret+=el.toString();
		}
		return ret;
	}
	
	public CodeElement copy(){
		ArithmeticExpression ae = new ArithmeticExpression();		
		ae.setType(type);
		ae.setValue(value);
		for (CodeElement el: expressionElements)
			ae.addElement(el.copy());
		ae.setParenthesis(parenthesis);
		ae.visited = this.visited;
		return ae;
	}
	
	private void handleParenthesis(){
		
		if (expressionElements.size()>0){
			if (this.parenthesis==false){
				if (expressionElements.get(0).toString().equals("(")){
					expressionElements.remove(0);
					expressionElements.remove(expressionElements.size()-1);
				}								
			}
			else
			{
				if(!expressionElements.get(0).toString().equals("(")){
				expressionElements.add(0, new SpecialChar("("));
				expressionElements.add(expressionElements.size(),new SpecialChar(")"));
				}							
			}							
		}
	}
	
	public void setVisited(boolean visited){
		this.visited = visited;
		for (int x =0; x< expressionElements.size(); x++){
			CodeElement el = expressionElements.get(x);
			el.setVisited(visited);
			expressionElements.setElementAt(el, x);
		}						
	}
	
	public String getType(){
		String tp = "";
		if (expressionElements.size()>0)
		{
			Vector<String> types = new Vector<String>();
			for (CodeElement ce: expressionElements){	
				if (ce.getType()==null){
					System.out.println(toString());
					System.out.println("ce.getType() == " + ce.getClass());
				}
				if (!ce.getType().equals(""))
					types.add(ce.getType());
			}
			
			boolean sameType=true;
			for (int i=0; i<types.size()-1; i++)
				if (!types.get(i).equals(types.get(i+1)))
					sameType=false;
			
			if (sameType==true)
				tp = types.get(0);
			else
			{
				if (types.contains("double") || types.equals("D"))
					tp = "double";
				else
					if (types.contains("float") || types.equals("F"))
						tp = "float";				
					else
						if (types.contains("long") || types.equals("J"))
							tp="long";
						else							
							if (types.contains("int") || types.equals("I"))
								tp = "int";
							else
								if (types.contains("byte") || types.equals("B"))
									tp = "byte";
								else
									tp = types.get(0);
			}
		}
		return tp;
	}
	
	public String getValue(){
		return toString();
	}
	
	public Vector<CodeElement> getPostfixExpression(){
		Stack<CodeElement> operadores = new Stack<CodeElement>();
		Vector<CodeElement> postfixExpression = new Vector<CodeElement>();
		for (CodeElement element: this.expressionElements){
			CodeElement chOp = new SpecialChar("");
			if (element instanceof ArithmeticExpression){
				ArithmeticExpression aexpression = (ArithmeticExpression) element;
				postfixExpression.addAll (aexpression.getPostfixExpression());
			} 
			
			else
			if (element.getValue().equals("("))
				operadores.push(element);
			
			else
			if (element.getValue().equals(")"))
			{
				
				while (!operadores.isEmpty() && !chOp.getValue().equals("(")){
					chOp = operadores.pop();
					if (!chOp.getValue().equals("("))
						postfixExpression.add(chOp);
				}
			}
			
			else
			if (element.getValue().equals("+") || element.getValue().equals("-") || element.getValue().equals("&") || 
					element.getValue().equals("|") ||element.getValue().equals("<<") ||element.getValue().equals(">>") || 
					element.getValue().equals("^") || element.getValue().equals("~")){
				if (!operadores.isEmpty()){
					chOp = operadores.peek();
					if (!chOp.getValue().equals("(")){
						chOp = operadores.pop();
						postfixExpression.add(chOp);
					}
				}
				operadores.push(element);
			}
			
			else
			if (element.getValue().equals("*") || element.getValue().equals("/") || element.getValue().equals("%")){
				if (!operadores.isEmpty()){
					chOp = operadores.peek();
					if (chOp.getValue().equals("*") || chOp.getValue().equals("/") || chOp.getValue().equals("%")){
						chOp = operadores.pop();
						postfixExpression.add(chOp);
					}
				}
				operadores.push(element);
			}
			
			else{
				postfixExpression.add(element);
			}
			
			
			
		}
		
		while (!operadores.isEmpty())
		   {
		     CodeElement chOp=operadores.pop();     
		     postfixExpression.add(chOp);
		   }  
		
		return postfixExpression;
	}
	
	
	public boolean hasVariableElement(){
		
		for (CodeElement element: this.expressionElements){
			if (element instanceof ArithmeticExpression){
				ArithmeticExpression ae = (ArithmeticExpression) element;
				if (ae.hasVariableElement())
					return true;
			}
			
			if (element instanceof Variable)
				return true;
			
			if (element instanceof ArrayVar)
				return true;
			
			if (element instanceof ArrayElement)
				return true;
			
		}
		
		return false;
	}
	
	public boolean hasMethodElement(){
		
		for (CodeElement element: this.expressionElements){
			if (element instanceof ArithmeticExpression){
				ArithmeticExpression ae = (ArithmeticExpression) element;
				if (ae.hasMethodElement())
					return true;
			}
			
			if (element instanceof MethodCall)
				return true;
		
		}
		
		return false;
	}
	
	private boolean isVariableElement(CodeElement element){
		
		if (element instanceof ArithmeticExpression){
			ArithmeticExpression ae = (ArithmeticExpression) element;
			if (ae.hasVariableElement())
				return true;
		}
		
		if (element instanceof Variable)
			return true;
		
		if (element instanceof ArrayVar)
			return true;
		
		if (element instanceof ArrayElement)
			return true;
		
		return false;
	}
	
	
	public boolean isNonLinear(){
		
		try{
		Stack<CodeElement> expression = new Stack<CodeElement>();
		Vector<CodeElement> postfixExpression = this.getPostfixExpression();
		
		if (this.getValue().contains("asdfasdfasdf)")){
			System.out.println(this);
			for (CodeElement el: postfixExpression)
				System.out.print(el+"("+el.getClass() +")  ");
			System.out.println();
			System.out.println();
		}
		for (CodeElement element: postfixExpression){
			if (this.getValue().equals("((0-1)+1)")){
				//System.out.println(" element: " + element);
			}
			if (element instanceof ArithmeticOperator){
				if (this.getValue().equals("((0-1)+1)")){
					//System.out.println("    is arithmetic operator");
				}
				CodeElement x = expression.pop();
				CodeElement y = expression.pop();
				
				if (element.getValue().equals("*") || element.getValue().equals("/") || element.getValue().equals("%")|| 
						element.getValue().equals("&") || element.getValue().equals("|") || element.getValue().equals(">>") || 
						element.getValue().equals("<<")){
					//System.out.println("ANALISE: " + x + " e " + y);
					if ( (isVariableElement(x) || (x instanceof MethodCall)) && 
							(isVariableElement(y) || (y instanceof MethodCall)) ){
						return true;
					}
				}
				
				ArithmeticExpression newExpression = new ArithmeticExpression();
				newExpression.addElement(x);
				newExpression.addElement(element);
				newExpression.addElement(y);
				expression.push(newExpression);
			}
			else
			{
				expression.push(element);
			}
			if (this.getValue().equals("((0-1)+1)")){
				
			}
		}
		}
		catch(EmptyStackException ese){
			System.out.println("ERRO: Pilha vazia: ");
			System.out.println("   Original expression: " + this);
			System.out.print("   Postfix expression: ");
			for (CodeElement el: this.getPostfixExpression())
				System.out.print(el+" ");
			System.out.println();
		}

		
		return false;
	}

	
	public static void main(String[] args){
		ArithmeticExpression ae = new ArithmeticExpression();
		Variable x = new Variable("x");
		Variable y = new Variable("y");
		Variable z = new Variable("z");
		Variable p = new Variable("p");
		SpecialChar abreP = new SpecialChar("(");
		SpecialChar fechaP = new SpecialChar(")");
		ArithmeticOperator sum = new ArithmeticOperator("+");
		ArithmeticOperator sub = new ArithmeticOperator("-");
		ArithmeticOperator mul = new ArithmeticOperator("*");
		ArithmeticOperator div = new ArithmeticOperator("/");
		ArithmeticOperator resto = new ArithmeticOperator("%");
		
		ae.addElement(abreP);
		ae.addElement(x);
		ae.addElement(sum);
		ae.addElement(y);
		ae.addElement(fechaP);
		ae.addElement(mul);
		
		
		ArithmeticExpression ae2 = new ArithmeticExpression();
		ae2.addElement(abreP);
		ae2.addElement(x);
		ae2.addElement(sum);
		ae2.addElement(z);
		ae2.addElement(fechaP);
		
		ae.addElement(ae2);
		
		ae.addElement(div);
		ae.addElement(abreP);
		ae.addElement(x);
		ae.addElement(mul);
		ae.addElement(z);
		ae.addElement(fechaP);
		
		System.out.println(ae);
		System.out.println();
		System.out.println();
		for (CodeElement element: ae.getPostfixExpression()){
			System.out.print(element+ " ");
		}
		
		
		ArithmeticExpression ae3 = new ArithmeticExpression();
		ae3.addElement(x);
		ae3.addElement(sum);
		ae3.addElement(y);
		
		ArithmeticExpression ae4 = new ArithmeticExpression();
		ae4.addElement(z);
		ae4.addElement(mul);
		ae4.addElement(p);
		
		ArithmeticExpression ae5 = new ArithmeticExpression();
		ae5.addElement(ae3);
		ae5.addElement(div);
		ae5.addElement(ae4);
		
		System.out.println();
		System.out.println();
		System.out.println(ae5);
		System.out.println();
		System.out.println();
		for (CodeElement element: ae5.getPostfixExpression()){
			System.out.print(element+ " ");
		}
		
		
		
		System.out.println();
		System.out.println(ae3.isNonLinear());
		System.out.println(ae5.isNonLinear());
	}
}


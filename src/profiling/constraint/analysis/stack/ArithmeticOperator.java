package profiling.constraint.analysis.stack;

public class ArithmeticOperator extends CodeElement {
		
	public ArithmeticOperator(String value){
		super (value);		
		if (value.equals(""))
			System.out.println("operador vazio");
	}
	
	public CodeElement copy(){
		ArithmeticOperator ao = new ArithmeticOperator(value);
		return ao;
	}
	
	public void setVisited(boolean visited){
		this.visited = visited;
	}
	
	public String getType(){
		return "";
	}
	
	public String toString(){
		return value;
	}

}

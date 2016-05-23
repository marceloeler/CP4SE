package profiling.constraint.analysis.stack;

public class RelationalOperator extends CodeElement {
	
	public RelationalOperator(String value){
		super (value);		
	}
	
	public RelationalOperator negate(){		
		String newName=this.value;
		if (newName.contains("<="))
			newName = newName.replace("<=",">");
		else
		{
			if (newName.contains(">="))
				newName = newName.replace(">=","<");
			else
			{
				if (newName.contains(">"))
					newName = newName.replace(">", "<=");
				else
				{
					if (newName.contains("<"))
						newName = newName.replace("<", ">=");
					else
					{
						if (newName.contains("=="))
							newName = newName.replace("==", "!=");
						else						
							newName = newName.replace("!=","==");																				
					}
				}
			}
		}
		return new RelationalOperator(newName);
	}
	
	public CodeElement copy(){
		RelationalOperator relOp = new RelationalOperator(value);
		return relOp;
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

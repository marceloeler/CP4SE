package profiling.constraint.symbolic;

import profiling.constraint.analysis.stack.CodeElement;
import profiling.constraint.analysis.stack.Variable;
import profiling.constraint.graph.Node;

/*
 * Esta classe representa as atualizações que as variáveis e objetos podem sofrer durante
 * a execução do programa. Em particular, duas opções concretas são previstas:
 *  -> atualização por meio de atribuição de uma expressão ou chamada de método
 *  -> atualização do estado de um objeto por meio da invocação de métodos
 */
public abstract class AbstractUpdate { 	
	
	protected CodeElement variableUpdated;
	protected int instructionPosition;
	protected Node node;
	protected CodeElement updatedValue;
	
	public int getInstructionPosition() {
		return instructionPosition;
	}
	
	public void setInstructionPosition(int instructionPosition) {
		this.instructionPosition = instructionPosition;
	}
	
	public Node getNode() {
		return node;
	}
	
	public void setNode(Node node) {
		this.node = node;
	}

	public CodeElement getVariableUpdated() {
		return variableUpdated;
	}

	public void setVariableUpdated(Variable variableUpdated) {
		this.variableUpdated = variableUpdated;
	}

	public CodeElement getUpdatedValue() {
		return updatedValue;
	}

	public void setUpdatedValue(CodeElement updatedValue) {
		this.updatedValue = updatedValue;
	}
	
	public abstract String toString();
	
	public abstract AbstractUpdate copy();

}

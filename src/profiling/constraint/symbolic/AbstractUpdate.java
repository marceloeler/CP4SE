package profiling.constraint.symbolic;

import profiling.constraint.analysis.stack.CodeElement;
import profiling.constraint.analysis.stack.Variable;
import profiling.constraint.graph.Node;

/*
 * Esta classe representa as atualiza��es que as vari�veis e objetos podem sofrer durante
 * a execu��o do programa. Em particular, duas op��es concretas s�o previstas:
 *  -> atualiza��o por meio de atribui��o de uma express�o ou chamada de m�todo
 *  -> atualiza��o do estado de um objeto por meio da invoca��o de m�todos
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

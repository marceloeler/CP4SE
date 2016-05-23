package profiling.constraint.bytecode;

import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;
import java.util.Stack;
import java.util.Vector;

import org.apache.bcel.classfile.LocalVariableTable;
import org.apache.bcel.classfile.Method;
import org.apache.bcel.generic.ClassGen;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.GOTO;
import org.apache.bcel.generic.ALOAD;
import org.apache.bcel.generic.CodeExceptionGen;
import org.apache.bcel.generic.IfInstruction;
import org.apache.bcel.generic.Instruction;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.generic.LOOKUPSWITCH;
import org.apache.bcel.generic.TABLESWITCH;
import org.apache.bcel.generic.LocalVariableGen;
import org.apache.bcel.generic.MethodGen;

import profiling.constraint.analysis.Constraint;
import profiling.constraint.analysis.stack.CodeElement;
import profiling.constraint.analysis.stack.ExceptionThrown;
import profiling.constraint.analysis.stack.Variable;
import profiling.constraint.bytecode.instructionhandler.AbstractHandler;
import profiling.constraint.bytecode.instructionhandler.HandlerFactory;
import profiling.constraint.graph.CFG;
import profiling.constraint.graph.Edge;
import profiling.constraint.graph.Node;


public class CFGBuilder {	
	
	public static int controlID;
	
	public static boolean aload;
	
	public static boolean newObject;
	
	public static boolean xcmpInstruction;	
	public static CodeExceptionGen[] exceptionGens;
	
	private Method method;
	MethodGen mgen;	
	
	private Node root;
	private CFG cfg;		
	
	public static String className;
	public static String methodName;
			
	private InstructionList il;
	private InstructionHandle[] ihs;
	
	public static InstructionHandle currentIh;	
	//public static Stack<String> symbolicStack;
	public static LocalVariableTable localVariables;
	//public static LocalVariableGen[] localVariables;	
	public static ConstantPoolGen cp;
	
	public static Stack<CodeElement> elementStack;
	
	public static Set<String> notHandledInstructions = new HashSet<String>();
	
	
	private boolean showDetails = false;
		
	public static void showElementStack(){
		System.out.print("Element Stack: ");
		for (CodeElement el: elementStack){
			System.out.print(el.toString()+ " ");
		}
		System.out.println();
	}
			
	public CFGBuilder(ClassGen cgen, ConstantPoolGen cp, Method method){
		//System.out.println(cgen.getClassName()+":"+method.getName());
		controlID=0;
		//System.out.println("Method: " + method.getName());
		//System.out.println("Static: "+method.isStatic());
		//System.out.println("Abstract: "+method.isAbstract());
		
		
		//mgen.getModifiers();
		
		xcmpInstruction=false;
		this.cp = cp;
		this.method = method;		
		this.mgen = new MethodGen(method,cgen.getClassName(),cp);	
		this.className = cgen.getClassName();
		this.methodName = method.getName();		
		this.methodName=this.methodName.replace("<", "");
		this.methodName=this.methodName.replace(">", "");
		
		elementStack = new Stack<CodeElement>();
		//symbolicStack = new Stack<String>();
		cfg = new CFG();
		cfg.setMethodName(this.methodName);
		cfg.setClassName(this.className);
		cfg.setMethodSignature(method.getSignature());
		il = mgen.getInstructionList();  
		if (il!=null)
			cfg.setLOB(il.getInstructions().length);
		else
			cfg.setLOB(0);

		//System.out.println("Number of instructions: " + il.getInstructions().length);
		if (il==null){

			ihs=null;
			return;
			}
		
		ihs = il.getInstructionHandles();

		
		localVariables = method.getLocalVariableTable();		
		
		//System.out.println("LOCAL VARIABLE TABLE");
		//for (int lv=0; lv<localVariables.getLength(); lv++){
		//	if (localVariables.getLocalVariable(lv)!=null){				
			//	//System.out.println(lv+": "+localVariables.getLocalVariable(lv).getName());
			//}
		//}
		//System.out.println();
		
				
		//identificar vari�veis que s�o par�metros de entrada
		//localVariables = mgen.getLocalVariables();    		
		//System.out.println("Parameters: ");		
		
		String[] parameters = mgen.getArgumentNames();
		//System.out.println("#Parameters: " + parameters.length);
		//System.out.println();
		int begin=1;
		int parametersSize = parameters.length+1;
		if (method.isStatic()){
			begin = 0; //n�o tem o this na posi��o 0
			parametersSize--;
		}
		for (int l=0; l<parametersSize; l++){
			String inputParameterName = "unknownParameter_"+String.valueOf(l);
			String inputParameterType = "unknownType_"+String.valueOf(l);
			if (localVariables!=null && localVariables.getLocalVariable(l)!=null){
				inputParameterName = localVariables.getLocalVariable(l).getName();	
				inputParameterType = localVariables.getLocalVariable(l).getSignature();
			}
						
			//System.out.println(" Par�metro "+l+": "+inputParameterName);
			//System.out.println("par: "+inputParameterName);
			Variable var = cfg.getVariable(inputParameterName);    				
			if (var==null){
				//System.out.print("cp2 ");
				var = new Variable(inputParameterName);				
				var.setInputParameter(true);
				var.setVarType(inputParameterType);
				cfg.addVariable(var);
			}    				    				    				  				    				    			
		}    
	//	System.out.println();
		//System.out.println();

	}	
	
	int getNextTargetNode(int currentPosition, Vector<String> targets){
		int last=0;
		if (targets.size()>0)
			last = Integer.valueOf(targets.get(targets.size()-1));
		
		
		for (int i=0; i<targets.size(); i++)
		{
			int tgt = Integer.valueOf(targets.get(i));
			if (tgt>currentPosition){
				if (tgt<last)
					last = tgt;
			}
		}
		
		return last;
	}
	
	
	int getIhIndex(int instructionPosition){
		for (int i=0; i<ihs.length; i++)
			if (instructionPosition == ihs[i].getPosition())
				return i;
		return -1;
	}
	
	/*	 
	  * Detalhes:
	  * 1 - O ID do n� � definido pelo n�mero da instru��o inicial do bloco de instru��es
	  * 2 -  
	  */
	public CFG generateCFG(){
				
		
		if (ihs==null){
			Node node = new Node("0");
			node.setFirstInstruction(0);
			cfg.addNode(node);
			cfg.setRoot(node);
			
			return cfg;
		}
		
		root = null;																	 
    	Node currentNode= root;
							    
    	//Descobre quais s�o os n�meros das instru��es que s�o alvos dos desvios de fluxo de if e goto
    	//Cada instru��o alvo dar� origem a um n�. Se fosse usado o padr�o visit para gerar o CFG n�o seria necess�rio este passo
    	//No futuro pretendo refazer este processo de an�lise usando o padr�o visit com recursividade
    	
    	Vector<String> targets = new Vector<String>();
    	for (int ii=0; ii<ihs.length; ii++){
    		InstructionHandle inst = ihs[ii];
    		Instruction instruction = inst.getInstruction();
    		
    		if (instruction instanceof IfInstruction){
    			IfInstruction ifi = (IfInstruction) instruction;
    			targets.add(String.valueOf( ifi.getTarget().getPosition()));    			
    		}
    		
    		if (instruction instanceof GOTO){
    			GOTO gt = (GOTO) instruction;
    			targets.add(String.valueOf( gt.getTarget().getPosition()));
    		}
    		
    		if (instruction instanceof TABLESWITCH){
    			TABLESWITCH sw = (TABLESWITCH) instruction;
    			InstructionHandle[] tgts =  sw.getTargets();
    			for (int i=0; i<tgts.length; i++)
    				targets.add(String.valueOf(tgts[i].getPosition()));
    			targets.add(String.valueOf(sw.getTarget().getPosition()));
    		}
    		
    		if (instruction instanceof LOOKUPSWITCH){
    			LOOKUPSWITCH lw = (LOOKUPSWITCH) instruction;
    			InstructionHandle[] tgts =  lw.getTargets();
    			for (int i=0; i<tgts.length; i++)
    				targets.add(String.valueOf(tgts[i].getPosition()));
    			targets.add(String.valueOf(lw.getTarget().getPosition()));
    		}
    	}
    	
    	Vector<String> exceptionNodes = new Vector<String>();    	    	    	
    	//TRATAR O GRAFO QUANDO TEM EXCE��ES
    	exceptionGens = mgen.getExceptionHandlers();
    	int cont = 0;;
    	for (CodeExceptionGen exceptionGen: exceptionGens){ 
    		
    		String idStr = String.valueOf(exceptionGen.getHandlerPC().getPosition());
    		
    		exceptionNodes.add(idStr);
    		Node exceptionNode = new Node(idStr);
    		exceptionNode.setFirstInstruction(exceptionGen.getHandlerPC().getPosition());
    		exceptionNode.setExceptionHandle(true);
    		cfg.addNode(exceptionNode);
    		
    		//in�cio do n� tratado pelo catch
    		targets.add(String.valueOf(exceptionGen.getStartPC().getPosition()));
    		
    		int endPosition = exceptionGen.getEndPC().getPosition();
    		
    		//get index
    		int index = this.getIhIndex(endPosition);
    		
    		if (ihs[index+1].getInstruction() instanceof GOTO){
    			//se o �ltimo for goto, n�o inclui

    			targets.add(String.valueOf(ihs[index+2].getPosition()));
    		}
    		else{
    				targets.add(String.valueOf(ihs[index+1].getPosition()));

    		}
    		//fim do n� tratado pelo catch - deve iniciar outro n�
    		
    		
    		//System.out.println("HANDLER PC: " + exceptionGen.getHandlerPC().getPosition());
    		//System.out.println(" FROM "+exceptionGen.getStartPC().getPosition() +" to " + exceptionGen.getEndPC().getPosition());
    		//System.out.println("Handler type: " + exceptionGen.getCatchType().getSignature());
    		cont++;
    	}
    	
    	int InstrPosition = 0;
    	int lastInstructionPosition=0;
    	int lastLastInstructionPosition=0;
    	showDetails = showDetails || ( methodName.contains("asdfasdf") && className.contains("asdf"));
    	for (int z=0; z<ihs.length; z++){    	
    		    	     		    	
            currentIh = ihs[z];
            Instruction currentInstruction = currentIh.getInstruction();
                            		
            lastLastInstructionPosition = lastInstructionPosition;
    		lastInstructionPosition = InstrPosition;
    		InstrPosition = currentIh.getPosition();
    		
    		
    		String instrPositionStr = String.valueOf(InstrPosition); 
    		
    		if (showDetails){
    			System.out.println("Instruction "+InstrPosition);
    			System.out.println(currentInstruction);
    			System.out.print("BEFORE: ");
    			showElementStack();    			
    		}
    		/*
    		 * Verificar se a posi��o da instru��o a ser analisada � o ID de um n� que j� foi criado anteriormente
    		 * Se sim, ent�o ligar o n� atual ao n� que est� para come�ar, a n�o ser que:
    		 *   - o n� atual tenha um GOTO para outra instru��o, e n�o a que est� para ser analisada
    		 *   - o n� atual tenha um return 
    		 *   - a aresta entre o n� atual e o pr�ximo n� (instru��o atual) j� exista
    		 * Se n�o, esta instru��o pode pertencer tamb�m ao n� atual (bloco de instru��e sem desvio de fluxo) 
    		 */
    		
    		Node nextCurrentNode = cfg.getNode(instrPositionStr);
    		if (nextCurrentNode!=null){    			    		
    			    			    			
    			//verifica se o n� atual teve um goto e a instru��o j� criada � para tratar exce��es
    			if (nextCurrentNode.isExceptionHandle()){    				    				
    				
    				//n�o incluir o goto na lista de �ndice de instru��es
        			//usar o lastlastinstructionposition
        			//isso para efeitos de indicar os tratamentos de exce��o no grafo
    				if (currentNode.hasGoto())
    					currentNode.setLastInstruction(lastLastInstructionPosition);
    				else
    					currentNode.setLastInstruction(lastInstructionPosition);
    				
    				//o n� de tratamento de exce��o � o novo n� atual
    				currentNode = nextCurrentNode;
    				
    				//ligar este n� aos n�s para os quais ele faz o tratamento de exce��o
    				//precisa descobrir quais s�o esses n�s pela tabela
    				//System.out.println("Instruction: " + InstrPosition);    				
    				for (int hPos=0; hPos<exceptionGens.length;hPos++){    					
    					if (exceptionGens[hPos].getHandlerPC().getPosition()==InstrPosition){
    						int firstPosition = exceptionGens[hPos].getStartPC().getPosition();
    						int lastPosition = exceptionGens[hPos].getEndPC().getPosition();
    						
    						for (Node handledNode: cfg.getNodes().values()){
    							
    							//System.out.print("Exception handler range: " + firstPosition +" - " + lastPosition + "  node rande: " + handledNode.getFirstInstruction()+" - " + handledNode.getLastInstruction());
    	    					//a primeira posi��o tem que ser >=  e a �ltima posi��o tem que ser <=
    	    					if (handledNode.getFirstInstruction()>=firstPosition && 
    	    							handledNode.getLastInstruction()<=lastPosition && handledNode.getLastInstruction()!=0){ //�ltima posi��o tem que ser diferente de 0 para n�o incluir n�s que n�o tem aresta e n�o terminaram ainda
    	    						//System.out.println(" CAPTURE");
    	    						
    	    						handledNode.addNeighbor(currentNode);
    	    						
    	    						String stedge = String.valueOf(handledNode.getId())+"-"+String.valueOf(currentNode.getId());
    	    						if (!cfg.getStedges().contains(stedge)){ //se n�o existe a aresta

    	    							cfg.addStedge(stedge);	
    	    	    					Edge edge = new Edge(handledNode, currentNode);
    	    	    					edge.setConstrained(true);
    	    	    					
    	    	    					String exceptionName = "AnyException";
    	    	    					if (exceptionGens[hPos].getCatchType()!=null)
    	    	    						exceptionName = exceptionGens[hPos].getCatchType().getClassName();
    	    	    					//System.out.println("Exception name: " + exceptionName);
    	    	    					//BenchmarkTest_GetExceptionsType.addExceptionName(exceptionName);
    	    	    					
    	    	    					ExceptionThrown exceptionThrown = new ExceptionThrown(exceptionName);
    	    	    					
    	    	    					Constraint exceptionConstraint = new Constraint(exceptionThrown);
    	    	    					edge.setConstraint(exceptionConstraint); 

    	    	    					cfg.addEdge(edge);
    	    						}
    	    					}
    	    					//else
    	    						//System.out.println();
    	    					
    	    				}
    						
    					}
    				}
    				    				    				    			
    				/*
					String stedge = String.valueOf(currentNode.getId())+"-"+String.valueOf(nextCurrentNode.getId());      				
					currentNode.addNeighbor(nextCurrentNode);    			
					cfg.addStedge(stedge);    			
					Edge edge = new Edge(currentNode, nextCurrentNode);
					cfg.addEdge(edge);*/
				}
    			else   			
    			{
    				String stedge = String.valueOf(currentNode.getId())+"-"+String.valueOf(nextCurrentNode.getId());    			
    				if ((!cfg.getStedges().contains(stedge)) && (currentNode.hasGoto()==false) && (currentNode.hasReturn()==false)){    				
    					currentNode.addNeighbor(nextCurrentNode);       			 
    					cfg.addStedge(stedge);	
    					Edge edge = new Edge(currentNode, nextCurrentNode);
    					cfg.addEdge(edge);
    				}    			
    				currentNode.setLastInstruction(lastInstructionPosition);
    				currentNode = nextCurrentNode;
    			}       			    			     			
    		}  
    		
    		/*
    		 * Verificar se a posi��o da instru��o a ser analisada corresponde a um alvo de if ou goto
    		 * Se sim, verificar se o n� j� foi criado anteriormente
    		 * 	  Se o n� n�o foi criado ainda, ent�o criar e ligar o n� atual ao n� que est� para come�ar, a n�o ser que:
    		 *   - o n� atual tenha um GOTO para outra instru��o, e n�o a que est� para ser analisada
    		 *   - o n� atual tenha um return 
    		 *   - a aresta entre o n� atual e o pr�ximo n� (instru��o atual) j� exista
    		 * Se n�o, esta instru��o pertence tamb�m ao n� atual (bloco de instru��e sem desvio de fluxo) 
    		 */
    		       		
    		if (targets.contains(instrPositionStr) && cfg.getNode(instrPositionStr)==null && currentIh.getPosition()!=0){     			
    			nextCurrentNode = new Node();
    			nextCurrentNode.setId(instrPositionStr);
    			nextCurrentNode.setFirstInstruction(currentIh.getPosition());
    			//nextCurrentNode.setSequentialBranch(true);
    			//nextCurrentNode.setHasGoto(false);    			
    			cfg.addNode(nextCurrentNode);    			
    			String stedge = String.valueOf(currentNode.getId())+"-"+String.valueOf(nextCurrentNode.getId());
    			//System.out.println(edge);
    			if ((!cfg.getStedges().contains(stedge)) && (currentNode.hasGoto()==false) && (currentNode.hasReturn()==false)){
    				currentNode.addNeighbor(nextCurrentNode);       			 
    				cfg.addStedge(stedge);	
        			Edge edge = new Edge(currentNode, nextCurrentNode);
        			cfg.addEdge(edge);
    			}    
    			currentNode.setLastInstruction(lastInstructionPosition);
    			currentNode = nextCurrentNode;       			
    		}
    		
    		    		    		
    		//cria um n� para a primeira instru��o
    		if (z==0){
    			currentNode = new Node();    			
    			currentNode.setId(instrPositionStr);
    			cfg.addNode(currentNode);
    			root = currentNode;    			    
    			cfg.setRoot(root);
    		}
    		
    		/*
    		 * se o n� atual (currentNode) teve um goto, criar um novo n� para a instru��o atual 
    		 */
    		    		
    		if (currentNode.hasGoto()==true){ 
    			
    			//n�o incluir o goto na lista de �ndice de instru��es
    			//usar o lastlastinstructionposition
    			//isso para efeitos de indicar os tratamentos de exce��o no grafo
    			
    			//se j� existe um n� criado para esta posi��o
    			if (cfg.getNode(instrPositionStr)!=null){
    				Node nextNode = cfg.getNode(instrPositionStr); 
    				currentNode.setLastInstruction(lastLastInstructionPosition);
    				currentNode = nextNode;
    			}
    			else
    			{ //sen�o
    				currentNode.setLastInstruction(lastLastInstructionPosition);
    				
    				currentNode = new Node();
    				currentNode.setFirstInstruction(InstrPosition);
        			currentNode.setId(instrPositionStr);	
        			cfg.addNode(currentNode);
    			}
    			    			
    			//currentNode.setSequentialBranch(true);    			    			
    		}   
    		

    		
    		
    		//A partir de agora come�a a tratar cada instru��o separadamente
    		
    		AbstractHandler instructionHandler = HandlerFactory.createInstructionHandler(currentInstruction);    		
    		
    		if (instructionHandler!=null){    			
    			instructionHandler.handle(currentInstruction, currentNode, cfg);
    		}
    		    		
    	} 
    	
    	
    	
    	    	    	
		return cfg;		

    }

}

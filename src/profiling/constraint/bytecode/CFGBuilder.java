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
		
				
		//identificar variáveis que são parâmetros de entrada
		//localVariables = mgen.getLocalVariables();    		
		//System.out.println("Parameters: ");		
		
		String[] parameters = mgen.getArgumentNames();
		//System.out.println("#Parameters: " + parameters.length);
		//System.out.println();
		int begin=1;
		int parametersSize = parameters.length+1;
		if (method.isStatic()){
			begin = 0; //não tem o this na posição 0
			parametersSize--;
		}
		for (int l=0; l<parametersSize; l++){
			String inputParameterName = "unknownParameter_"+String.valueOf(l);
			String inputParameterType = "unknownType_"+String.valueOf(l);
			if (localVariables!=null && localVariables.getLocalVariable(l)!=null){
				inputParameterName = localVariables.getLocalVariable(l).getName();	
				inputParameterType = localVariables.getLocalVariable(l).getSignature();
			}
						
			//System.out.println(" Parâmetro "+l+": "+inputParameterName);
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
	  * 1 - O ID do nó é definido pelo número da instrução inicial do bloco de instruções
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
							    
    	//Descobre quais são os números das instruções que são alvos dos desvios de fluxo de if e goto
    	//Cada instrução alvo dará origem a um nó. Se fosse usado o padrão visit para gerar o CFG não seria necessário este passo
    	//No futuro pretendo refazer este processo de análise usando o padrão visit com recursividade
    	
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
    	//TRATAR O GRAFO QUANDO TEM EXCEÇÕES
    	exceptionGens = mgen.getExceptionHandlers();
    	int cont = 0;;
    	for (CodeExceptionGen exceptionGen: exceptionGens){ 
    		
    		String idStr = String.valueOf(exceptionGen.getHandlerPC().getPosition());
    		
    		exceptionNodes.add(idStr);
    		Node exceptionNode = new Node(idStr);
    		exceptionNode.setFirstInstruction(exceptionGen.getHandlerPC().getPosition());
    		exceptionNode.setExceptionHandle(true);
    		cfg.addNode(exceptionNode);
    		
    		//início do nó tratado pelo catch
    		targets.add(String.valueOf(exceptionGen.getStartPC().getPosition()));
    		
    		int endPosition = exceptionGen.getEndPC().getPosition();
    		
    		//get index
    		int index = this.getIhIndex(endPosition);
    		
    		if (ihs[index+1].getInstruction() instanceof GOTO){
    			//se o último for goto, não inclui

    			targets.add(String.valueOf(ihs[index+2].getPosition()));
    		}
    		else{
    				targets.add(String.valueOf(ihs[index+1].getPosition()));

    		}
    		//fim do nó tratado pelo catch - deve iniciar outro nó
    		
    		
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
    		 * Verificar se a posição da instrução a ser analisada é o ID de um nó que já foi criado anteriormente
    		 * Se sim, então ligar o nó atual ao nó que está para começar, a não ser que:
    		 *   - o nó atual tenha um GOTO para outra instrução, e não a que está para ser analisada
    		 *   - o nó atual tenha um return 
    		 *   - a aresta entre o nó atual e o próximo nó (instrução atual) já exista
    		 * Se não, esta instrução pode pertencer também ao nó atual (bloco de instruçõe sem desvio de fluxo) 
    		 */
    		
    		Node nextCurrentNode = cfg.getNode(instrPositionStr);
    		if (nextCurrentNode!=null){    			    		
    			    			    			
    			//verifica se o nó atual teve um goto e a instrução já criada é para tratar exceções
    			if (nextCurrentNode.isExceptionHandle()){    				    				
    				
    				//não incluir o goto na lista de índice de instruções
        			//usar o lastlastinstructionposition
        			//isso para efeitos de indicar os tratamentos de exceção no grafo
    				if (currentNode.hasGoto())
    					currentNode.setLastInstruction(lastLastInstructionPosition);
    				else
    					currentNode.setLastInstruction(lastInstructionPosition);
    				
    				//o nó de tratamento de exceção é o novo nó atual
    				currentNode = nextCurrentNode;
    				
    				//ligar este nó aos nós para os quais ele faz o tratamento de exceção
    				//precisa descobrir quais são esses nós pela tabela
    				//System.out.println("Instruction: " + InstrPosition);    				
    				for (int hPos=0; hPos<exceptionGens.length;hPos++){    					
    					if (exceptionGens[hPos].getHandlerPC().getPosition()==InstrPosition){
    						int firstPosition = exceptionGens[hPos].getStartPC().getPosition();
    						int lastPosition = exceptionGens[hPos].getEndPC().getPosition();
    						
    						for (Node handledNode: cfg.getNodes().values()){
    							
    							//System.out.print("Exception handler range: " + firstPosition +" - " + lastPosition + "  node rande: " + handledNode.getFirstInstruction()+" - " + handledNode.getLastInstruction());
    	    					//a primeira posição tem que ser >=  e a última posição tem que ser <=
    	    					if (handledNode.getFirstInstruction()>=firstPosition && 
    	    							handledNode.getLastInstruction()<=lastPosition && handledNode.getLastInstruction()!=0){ //última posição tem que ser diferente de 0 para não incluir nós que não tem aresta e não terminaram ainda
    	    						//System.out.println(" CAPTURE");
    	    						
    	    						handledNode.addNeighbor(currentNode);
    	    						
    	    						String stedge = String.valueOf(handledNode.getId())+"-"+String.valueOf(currentNode.getId());
    	    						if (!cfg.getStedges().contains(stedge)){ //se não existe a aresta

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
    		 * Verificar se a posição da instrução a ser analisada corresponde a um alvo de if ou goto
    		 * Se sim, verificar se o nó já foi criado anteriormente
    		 * 	  Se o nó não foi criado ainda, então criar e ligar o nó atual ao nó que está para começar, a não ser que:
    		 *   - o nó atual tenha um GOTO para outra instrução, e não a que está para ser analisada
    		 *   - o nó atual tenha um return 
    		 *   - a aresta entre o nó atual e o próximo nó (instrução atual) já exista
    		 * Se não, esta instrução pertence também ao nó atual (bloco de instruçõe sem desvio de fluxo) 
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
    		
    		    		    		
    		//cria um nó para a primeira instrução
    		if (z==0){
    			currentNode = new Node();    			
    			currentNode.setId(instrPositionStr);
    			cfg.addNode(currentNode);
    			root = currentNode;    			    
    			cfg.setRoot(root);
    		}
    		
    		/*
    		 * se o nó atual (currentNode) teve um goto, criar um novo nó para a instrução atual 
    		 */
    		    		
    		if (currentNode.hasGoto()==true){ 
    			
    			//não incluir o goto na lista de índice de instruções
    			//usar o lastlastinstructionposition
    			//isso para efeitos de indicar os tratamentos de exceção no grafo
    			
    			//se já existe um nó criado para esta posição
    			if (cfg.getNode(instrPositionStr)!=null){
    				Node nextNode = cfg.getNode(instrPositionStr); 
    				currentNode.setLastInstruction(lastLastInstructionPosition);
    				currentNode = nextNode;
    			}
    			else
    			{ //senão
    				currentNode.setLastInstruction(lastLastInstructionPosition);
    				
    				currentNode = new Node();
    				currentNode.setFirstInstruction(InstrPosition);
        			currentNode.setId(instrPositionStr);	
        			cfg.addNode(currentNode);
    			}
    			    			
    			//currentNode.setSequentialBranch(true);    			    			
    		}   
    		

    		
    		
    		//A partir de agora começa a tratar cada instrução separadamente
    		
    		AbstractHandler instructionHandler = HandlerFactory.createInstructionHandler(currentInstruction);    		
    		
    		if (instructionHandler!=null){    			
    			instructionHandler.handle(currentInstruction, currentNode, cfg);
    		}
    		    		
    	} 
    	
    	
    	
    	    	    	
		return cfg;		

    }

}

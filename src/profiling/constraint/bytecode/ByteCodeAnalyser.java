package profiling.constraint.bytecode;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.apache.bcel.classfile.ClassParser;
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.classfile.Method;
import org.apache.bcel.generic.ClassGen;
import org.apache.bcel.generic.ConstantPoolGen;

import profiling.constraint.graph.CFG;
import profiling.constraint.graph.Node;


public class ByteCodeAnalyser {
	
	private Hashtable<String,CFG> cfgs;
	public boolean showCode = false;
	
	public static int methodsCountAlternative;
			
	public ByteCodeAnalyser(String classPath){		
		try{						               
	    	ClassParser parser = new ClassParser(classPath);
	    	JavaClass javaClass = parser.parse();	    	
	    	processClass(javaClass);	    	
		}
	    catch(Exception ex){
	      	ex.printStackTrace();
	    }       
	}
	
	public ByteCodeAnalyser(ZipFile zippedProject, ZipEntry zipEntry){				
		try{						    
			String className = zippedProject.getName();
            JavaClass javaClass = new ClassParser(zippedProject.getInputStream(zipEntry), className).parse();
            processClass(javaClass);	    	
		}
	    catch(Exception ex){
	      	ex.printStackTrace();
	    }       
	}	
	
	public void processClass(JavaClass javaClass){
		cfgs = new Hashtable<String, CFG>();
		CFG cfg = new CFG();
		
		ConstantPoolGen cp = new ConstantPoolGen(javaClass.getConstantPool());		 	        
    	ClassGen cgen = new ClassGen(javaClass);    
    	
    	int sequence=0;
    	for(Method method: javaClass.getMethods()){
    		  
    		int qtdeParametros = method.getArgumentTypes().length;
    		cfg.setArguments(qtdeParametros);
    		
    		
    		String time = String.valueOf(++sequence);
        	if (!method.isAbstract()) //exclui métodos abstratos
        	{
        		if (showCode && (method.getName().equals("setRefid")&& cfg.getClassName().contains("StringResource")))
          		    System.out.println(method.getCode());
        		
        			
        			CFGBuilder cfgBuilder = new CFGBuilder(cgen, cp, method);            	
        			cfg = cfgBuilder.generateCFG();	
        			
        			cfg.setMethodSignature(method.getSignature());
        			cfg.isAbstract=false;
    			
        			String key = cfg.getMethodName()+":"+cfg.getMethodSignature()+":"+time;    			
        			cfgs.put(key, cfg);
        		
        	}      
        	else
        	{            		
        		cfg.setMethodName(method.getName());
        		cfg.setClassName(cgen.getClassName());
        		cfg.setRoot(new Node("0"));
        		cfg.isAbstract=true;
        		cfg.setMethodSignature(method.getSignature());
        		String key = cfg.getMethodName()+":"+cfg.getMethodSignature()+":"+time;        		
        		cfgs.put(key, cfg);
        	}
        }    	    		    	 

	}
	
	
	public CFG getCFG(String methodName){				
		for (CFG cfg: cfgs.values()){
			if (cfg.getMethodName().equals(methodName))
				return cfg;
		}		
		return null;
	}
	
	public Vector<CFG> getCFGs(){
		Vector<CFG> vetCFG = new Vector<CFG>();
		Iterator<CFG> itCFG = cfgs.values().iterator();
		while (itCFG.hasNext())
			vetCFG.add(itCFG.next());
		return vetCFG;
	}	

}

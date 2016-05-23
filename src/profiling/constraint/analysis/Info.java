package profiling.constraint.analysis;

public class Info {
	
	public static float strings;
	public static float objects;
	public static float arrays;
	public static float ints;
	public static float floats;
	public static float exceptions;
	
	public static float loopDepth;
	public static float loopDepth2;
	
	public static float onlyint;
	public static float onlyfloat;
	public static float onlyarray;
	public static float onlyobject;
	public static float onlystring;
	public static float onlyexception;
	
	public static double constraints;
	
	public static float calls;
	public static float inner;
	public static float inter;
	public static float external;
	
	
	public static float tot;
	
	
	public static int paths;
	
	public static int getset;
	public static int init;
	public static int hasconstraint;
	public static int isabstract;
	public static int methods;
	
	public static int unsolvablecs;
	public static int constraintsequences;
	
	public static int path_nonLinear;
	public static int method_nonLinear;
	
	public static void resetInfo(){
		strings=objects=floats=arrays=exceptions=ints=0;
		loopDepth=loopDepth2=0;
		onlyint=onlyfloat=onlyarray=onlyobject=onlystring=onlyexception=0;
		calls=inner=inter=external=0;
		tot=0;
		getset=init=isabstract=hasconstraint=methods=0;
		constraintsequences=unsolvablecs=0;
		paths=0;
		constraints=0;
		path_nonLinear=0;
		method_nonLinear=0;
	}

}

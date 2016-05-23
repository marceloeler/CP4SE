package profiling.constraint.analysis;

import java.util.Vector;

public class PathUtil {
	
	//ordena paths do maior para o menor
	public static void sortPaths(Vector<Path> paths){		
		
		for (int i=0; i<paths.size(); i++){
			int maxPath = paths.get(i).getNodes().size();
			int posMaxPath = i;
			
			for (int j=i+1; j<paths.size(); j++){
				if (paths.get(j).getNodes().size()>maxPath){
					maxPath = paths.get(j).getNodes().size();
					posMaxPath = j;
				}
			}
			
			Path path = paths.remove(posMaxPath);
			paths.add(i, path);
		}				
	}

}

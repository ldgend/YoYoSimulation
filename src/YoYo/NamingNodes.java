package YoYo;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class NamingNodes {
	private static List<Integer> list = new ArrayList<Integer>(); 
	
    public List<Integer> Naming(int networksize){
    	
    	for(int i=0;i<1000;i++){
    		list.add(new Integer(i));
    	}
    	int temp = 1000-networksize;
    	for(int i=0;i<temp;i++){
    		int random = new Random().nextInt(list.size());
    		list.remove(random);
    	}
    	return list;
    }
}

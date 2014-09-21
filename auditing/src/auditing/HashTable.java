package auditing;

import java.util.HashMap;

/**
* @author Anjo Iype Joseph (anjoiype@gmail.com)
*/

public class HashTable {
	
	public HashMap<Integer, Values> hashValue;
	private int n;
	private Values value;
	
	
	//constructor
	public HashTable(int n){
		
		this.n = n;
		
		hashValue = new HashMap<Integer, Values>();
	}
	
	public void putValue(){
		
		for(int i = 0; i<n; i++){
			value = new Values();
			hashValue.put(i, value);
		}
		
	}
	
	public HashMap<Integer, Values> getHashTable(){
		return hashValue;
	}
 	
//	public static void main(String[] args) {
//		HashTable h = new HashTable(5);
//		Values v;
//		h.putValue();
//		for(int i = 0; i<5; i++){
//			v = h.hashValue.get(i);
//			System.out.print(v.random);
//			System.out.println("\t"+v.version);
//			
//		}
//		
//	}

}

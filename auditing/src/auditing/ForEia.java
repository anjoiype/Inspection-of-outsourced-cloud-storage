package auditing;

import java.io.Serializable;
import java.util.HashMap;

/**
 * @author Anjo Iype Joseph (anjoiype@gmail.com)
 */

public class ForEia implements Serializable {
	private final HashMap<Integer, Values> hashTable;
	private final byte[][] u;
	private final byte[][] publicKey;
	final byte[][] etaTwoArray;
	
	public ForEia(HashMap<Integer, Values> hashTable, byte[][] u, byte[][] publicKey, byte[][] etaTwoArray) {
		super();
		this.hashTable = hashTable;
		this.u = u;
		this.publicKey = publicKey;
		this.etaTwoArray = etaTwoArray;
	}

	public byte[][] getEtaTwoArray() {
		return etaTwoArray;
	}

	public byte[][] getPublicKey() {
		return publicKey;
	}

	public HashMap<Integer, Values> getHashTable() {
		return hashTable;
	}

	public byte[][] getU() {
		return u;
	}
	
	
	 
	 
	 

}

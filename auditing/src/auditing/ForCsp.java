package auditing;

import java.io.Serializable;

/**
 * @author Anjo Iype Joseph (anjoiype@gmail.com)
 */

public class ForCsp implements Serializable {
	private final byte[][] tags;
	private final byte[][] publicKey;
	private final byte[][] u;
	private final byte[][] splitArray;

	public ForCsp(byte[][] tags , byte[][] publicKey,byte[][] u, byte[][] splitArray) {
		super();
		this.tags = tags;
		this.publicKey = publicKey;
		this.u = u;
		this.splitArray = splitArray;
	}

	public byte[][] getTags() {
		return tags;
	}

	public byte[][] getPublicKey() {
		return publicKey;
	}
	
	public byte[][] getU() {
		return u;
	}

	public byte[][] getSplitArray() {
		return splitArray;
	}
	
	
	

}

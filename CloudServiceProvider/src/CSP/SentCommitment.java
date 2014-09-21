package CSP;

import java.io.Serializable;

/**
 * @author Anjo Iype Joseph (anjoiype@gmail.com)
 */

public class SentCommitment implements Serializable{
	
	
	private byte[] h1dashBytes;
	private byte[] piBytes;
	
	public SentCommitment(byte[] h1dashBytes, byte[] piBytes) {
		
		this.h1dashBytes = h1dashBytes;
		this.piBytes = piBytes;
	}

	public byte[] getH1dashBytes() {
		return h1dashBytes;
	}

	public byte[] getPiBytes() {
		return piBytes;
	}

	

}

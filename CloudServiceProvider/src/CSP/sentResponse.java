package CSP;

import java.io.Serializable;

public class sentResponse implements Serializable {
	
	private byte[] sigmaDash;
	private byte[][] mue;
	private byte[][] splitdArray;
	private byte[][] splitArray;
	

	public sentResponse(byte[] sigmaDash, byte[][] mue, byte[][]splitdArray, byte[][] splitArray ) {
		
		this.sigmaDash = sigmaDash;
		this.mue = mue;
		this.splitdArray = splitdArray;
		this.splitArray = splitArray;
	}
	
	public byte[] getSigmaDash() {
		return sigmaDash;
	}
	
	public byte[][] getMue() {
		return mue;
	}

	public byte[][] getSplitdArray() {
		return splitdArray;
	}
	
	public byte[][] getSplitArray() {
		return splitArray;
	}
	
	
	

}

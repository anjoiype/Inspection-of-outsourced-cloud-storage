package CSP;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.net.Socket;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;

public class Response {
	byte[][] qv;
	
	byte[][] tags;
	Element gama;
	byte[][] splitArray;
	Pairing pairing;
	int n;
	int s;
	Element[] lambda;
	public Element mueJ;
	public Element[] mue;
	byte[] cldArray;
	byte[][] splitdArray;
	

	public Response(byte[][] qv,int  n, int s) {
		
		this.qv = qv;
		pairing  = PairingFactory.getPairing("d159.properties");
		this.n = n;
		this.s = s;
		lambda = new Element[s];
		mueJ = pairing.getZr().newElement();
		mueJ.setToZero();
		mue = new Element[s];
		
		
	}
	
	public void setTagsFromServer(byte[][] tags){
		this.tags = tags;
	}
	
	public void setGamaFromServer(Element gama){
		this.gama = gama;
	}
	
	public void setSplitArrayFromServer(byte[][] splitArray) {
		this.splitArray = splitArray;
		//this.splitdArray = splitArray;
	}
	
	public void setLambda(Element[] lambda) {
		this.lambda = lambda;
	}
	
	public void setCldArray() throws IOException{
		GetFile f = new GetFile("test_bckt707", "testing1.txt");
		cldArray = f.getFileFrmServr();
		int blockSize = (int)Math.ceil((float)cldArray.length/(n*s));
		int sectors = blockSize*s;
		
		splitdArray = new byte[n][sectors];		  
		int element = -1;
		
		for(int i = 0; i<n; i++){
			
			for(int j = 0; j<(sectors); j++){
				element++;
				if(element == cldArray.length)
		    		break;
		    	splitdArray[i][j] = cldArray[element];
		 
		    	
		    	}
			
		    if(element == cldArray.length)
	    		break;			    	
		    }
	}
	

	public Element getSigmaDash(){
		Element prod = pairing.getG1().newRandomElement();
		Element gamaElem = pairing.getZr().newElement();
		Element sigmaDash = pairing.getG1().newRandomElement();
		gamaElem.setFromBytes(qv[0]);
		prod.setFromBytes(tags[0]);
		prod.powZn(gama.mul(gamaElem));
		sigmaDash.set(prod);
		for(int i = 1; i < qv.length; i++){
			gamaElem.setFromBytes(qv[i]);
			prod.setFromBytes(tags[i]);
			prod.powZn(gama.mul(gamaElem));
			sigmaDash.mul(prod);
			
		}
		return sigmaDash;			
	}
	
	public void getMue(){		
		Element sigma = pairing.getZr().newRandomElement();
		//sigma.setToZero();
		
		
		Element elemQv = pairing.getZr().newRandomElement();
		elemQv.setFromBytes(qv[0]);
		sigma.set((elemQv.mul(splitArray[0][0])));
		mueJ.set(lambda[0].add(gama.mul(sigma)));
		Element random = pairing.getZr().newRandomElement();
		mue[0] = random;
		mue[0].set(mueJ);
		for(int j = 1; j < s; j++){	
			random = pairing.getZr().newRandomElement();
			for(int i = 1; i < qv.length; i++){
				elemQv.setFromBytes(qv[i]);
				sigma.add((elemQv.mul(splitArray[i][j])));
			}
			mueJ.set(lambda[j].add(gama.mul(sigma)));
			mue[j] = random;
			mue[j].set(mueJ);			
		}
	}
	
	public void sentToEia() throws IOException{
		byte[] sigmaDashBytes = getSigmaDash().toBytes();
		byte[][] mueBytes = new byte[s][];
		
		getMue();
		
		
		for(int j = 0; j < s; j++){
			
			byte[] tempMue = mue[j].toBytes();
			mueBytes[j] = tempMue;
//			
			
//			for(int i = 0; i < tempMue.length; i++){
//				mueBytes[j][i] = tempMue[i];
//			}
		}
		setCldArray();
		sentResponse  response = new sentResponse(sigmaDashBytes, mueBytes, splitdArray, splitArray);
		try{
			Socket commitSock = new Socket("localhost",8889);
			ObjectOutputStream oosCommitEia =  new ObjectOutputStream(commitSock.getOutputStream());
			oosCommitEia.writeObject(response);
			//*************************************************
			File file3 = new File("Response_to_EIA.txt");
			if(!file3.exists())
				file3.createNewFile();
			FileWriter fstream43  = new FileWriter(file3.getAbsoluteFile());
			BufferedWriter out43 = new BufferedWriter(fstream43);
			for(int i = 0 ; i < mueBytes.length; i++){
				byte[][] u1 = mueBytes;
				BigInteger b = new BigInteger(u1[i]);
				out43.write("mue"+i+"----"+b);
				out43.write("\n");
			}
			out43.write("\n");
			byte[] u1 = sigmaDashBytes;
			BigInteger b = new BigInteger(u1);
			out43.write("sidmadash----"+b);
			out43.write("\n");
			
			out43.close();
			//***************************************************
			
		}
		catch(Exception e){
			System.out.println(e);
		}
	}

	
	

}

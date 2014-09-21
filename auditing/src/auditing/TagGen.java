package auditing;

import it.unisa.dia.gas.jpbc.Element;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.net.Socket;
import java.util.HashMap;

/**
* @author Anjo Iype Joseph (anjoiype@gmail.com)
*/

public class TagGen {
	
	private final static String FILE_NAME = "testing1.txt";
	private Element publicKey;
	private Element privateKey;
	private Element[] sPrivateKey;
	private Element[] sPublicKey;
	private Bls sigElem;
	private SegmentFile file;
	private HashTable hashTable;
	//private Hmac hmacAlgo;
	private Element sumPrivateKey;
	int s;
	int n;
	private byte[][] u ;
	Element G1;
	byte[][] etaTwoArray;
	
	public TagGen(int n,int s, String fileName) {
		publicKey = null;
		privateKey = null;
		sigElem = new Bls();
		sPrivateKey = new Element[s];
		sPublicKey = new Element[s];
		sumPrivateKey =  null;
		this.s = s;
		this.n = n;	
		file = new SegmentFile(n,s);
		hashTable = new HashTable(n);
		u = new byte[s+1][];
		sigElem.generateSystemParams();
		etaTwoArray = new byte[n][];
		//
		//G1 = sigElem.pairing.getG1().newRandomElement();
		
	}
	
	//call SegmentFile class to preprocess the file
	public void preprocessFile(){
		file.readFile(FILE_NAME);
		file.convertFile();
		
	}
	
	public void getBlsKey(){
		
		//sigElem.initPairing();
		
		privateKey = sigElem.getSecretKey();
		publicKey = sigElem.getPublicKey();
		
	}
	
	//read the array of  pk as said in the paper
	public byte[][] getPk(){
		Element tmp[] = new Element[4];
		byte[][] publicky = new byte[4][]; 
		tmp = sigElem.getPk();
		for(int i = 0; i < 4 ; i++){
			 publicky[i] = tmp[i].toBytes();
		}
		return publicky;
		
	}
	
	//add up s keys
	public void generateU(){
		
		
		for(int i = 0; i< s; i++){
			getBlsKey();
			Element e = sigElem.pairing.getG1().newRandomElement();
			sPublicKey[i] = e;
			sPrivateKey[i] = privateKey;
			sPublicKey[i].set(publicKey);
		}
		
	}
	
	public void sumPrivateKey(){
		sumPrivateKey = sPrivateKey[0];
		for(int i =1; i< s; i++)
		{
			sumPrivateKey = sumPrivateKey.add(sPrivateKey[i]);
			
		}
	}
	
	public byte[] etaOne(String fileName){
		
		sumPrivateKey();
		BigInteger bi = sumPrivateKey.toBigInteger();
		byte[] etaOneHashValue = null;
		
		String privateKeySumString = ""+bi;
		try{
		etaOneHashValue = Hmac.calculateHMAC(fileName, privateKeySumString);
		}
		catch(Exception e){
			System.out.println("hmac exception error"+e);
		}
		
		//*****Sets the first field of U to send to EIA*****
		BigInteger biHash = new BigInteger(etaOneHashValue);
		Element temp = sigElem.pairing.getZr().newElement();
		temp.set(biHash);//Randomly allocating some values to avoid null pointer exception
		u[0] = temp.toBytes();	
		//***************************************************************
		
		return etaOneHashValue;
	}
	
	//fill hashtable with values
	public void fillHashTable(){
		hashTable.putValue();
	}
	
		
	public byte[] etaTwo(Values value,int i){
		byte[] etaTwoHashValue = null;
		byte[] etaOneHashValue;
		etaOneHashValue = etaOne(FILE_NAME);		
		BigInteger biHash = new BigInteger(etaOneHashValue);
		String key = ""+biHash;
		String random = Integer.toString(value.random);
		String version = Integer.toString(value.version);
		String block = Integer.toString(i);
		String message = random+version+block;
		try{
			etaTwoHashValue = Hmac.calculateHMAC(message, key);
		}
		catch(Exception e){
			System.out.println("hmac exception error"+e);
		}
		return etaTwoHashValue;		
		
	}
	
	public  Element tagRow(Values value, int i){
		byte[] varEtaTwo = etaTwo(value,i);
		Element etaTwoelem = sigElem.pairing.getG1().newRandomElement();
		etaTwoelem.setFromHash(varEtaTwo,0, varEtaTwo.length);
		etaTwoArray[i] = etaTwoelem.toBytes();

		Element elemAlpha =  sigElem.genarateAlpha();
		Element elemBeta = sigElem.generateBeta();
		Element g = sigElem.generateG();
		BigInteger alpha = elemAlpha.toBigInteger();
		//BigInteger beta = elemBeta.toBigInteger();
		BigInteger etaTwoBI = new BigInteger(varEtaTwo);
		Element etaTwo = sigElem.pairing.getZr().newElement();		//randomly initializing to some variable to avoid null pointer excepption  					
		etaTwo.set(etaTwoBI);
		//SegmentFile file = new SegmentFile(n,s);
		Element tag = null;
		Element elemSigma = sigElem.pairing.getZr().newElement();	//randomly initializing to some variable to avoid null pointer excepption  					
		byte marr[][] = file.getSplitArray();
		Element m = sigElem.pairing.getZr().newElement();			//randomly initializing to some variable to avoid null pointer excepption  					
						
		for(int j=0; j<s; j++){
			m.set(marr[i][j]);
			elemSigma = elemSigma.add(sPrivateKey[j].mul(m.mul(elemBeta)));
			
		}
		BigInteger sigma = elemSigma.toBigInteger();
		Element e = etaTwo.pow(alpha);
		
		Element h =  g.pow(sigma);
		h.set(g.pow(sigma));
		//BigInteger b = h.toBigInteger();
		tag = h.mulZn(e);
		//tag = (etaTwo.pow(alpha)).mul((g.pow(sigma)));
		return tag;
				
	}
	
	public Element[] getPublicKeyU(){
		return sPublicKey;
	}
	
	public byte[][] getU(){
		for(int j = 0; j < s; j++){
			u[j+1] = sPublicKey[j].toBytes();
		}
		
		return u;
	}
	
	
	
	public static void main(String[] args) {
		final int n = 5;
		final int s = 5;
		
		TagGen t = new TagGen(n,s,FILE_NAME);
		Values value;
		
		t.preprocessFile();
		t.generateU();
		//t.sumPrivateKey();
		//byte[] b = t.etaOne(FILE_NAME);
		t.fillHashTable();
		Element[] tagArray = new Element[n];
		byte tags[][] = new byte[n][];
		HashMap<Integer, Values> hashMap = t.hashTable.getHashTable();
		for(int i = 0; i < n; i++){
			value = hashMap.get(i);
			tagArray[i] = t.tagRow(value, i);
			tags[i] = tagArray[i].toBytes();
			System.out.println(tagArray[i]);
		}
		byte[][] pkArr = t.getPk();
		byte[][] tempU = t.getU();
		ForEia eia = new ForEia(hashMap, tempU,pkArr, t.etaTwoArray);
		
		ForCsp csp = new ForCsp(tags, pkArr,tempU,t.file.getSplitArray());
		
		try{
			//********************************************
			File file1 = new File("output_for_eia.txt");
			if(!file1.exists())
				file1.createNewFile();
			FileWriter fstream  = new FileWriter(file1.getAbsoluteFile());
			BufferedWriter out = new BufferedWriter(fstream);
			for(int i = 0 ; i < t.getU().length; i++){
				byte[][] u = t.getU();
				BigInteger b = new BigInteger(u[i]);
				out.write("U"+i+"----"+b);
				out.write("\n");
			}
			out.write("\n");
			for(int i = 0 ; i < t.etaTwoArray.length; i++){
				byte[][] u = t.etaTwoArray;
				BigInteger b = new BigInteger(u[i]);
				out.write("etatwo"+i+"----"+b);
				out.write("\n");
			}
			out.write("\n");
			for(int i = 0 ; i < pkArr.length; i++){
				byte[][] u = pkArr;
				BigInteger b = new BigInteger(u[i]);
				out.write("PK"+i+"----"+b);
				out.write("\n");
			}
			out.close();
			//********************************************
			File file2 = new File("output_for_csp.txt");
			if(!file2.exists());
				file2.createNewFile();
			FileWriter fstream42  = new FileWriter(file2.getAbsoluteFile());
			BufferedWriter out42 = new BufferedWriter(fstream42);
			for(int i = 0 ; i < t.getU().length; i++){
				byte[][] u = t.getU();
				BigInteger b = new BigInteger(u[i]);
				out42.write("U"+i+"----"+b);
				out42.write("\n");
			}
			out42.write("\n");
			for(int i = 0 ; i < tags.length; i++){
				byte[][] u = tags;
				BigInteger b = new BigInteger(u[i]);
				out42.write("tag"+i+"----"+b);
				out42.write("\n");
			}
			out42.write("\n");
			for(int i = 0 ; i < pkArr.length; i++){
				byte[][] u = pkArr;
				BigInteger b = new BigInteger(u[i]);
				out42.write("PK"+i+"----"+b);
				out42.write("\n");
			}
			
			for(int i = 0 ; i < t.file.getSplitArray().length; i++){
				byte[][] u = t.file.getSplitArray();
				BigInteger b = new BigInteger(u[i]);
				out42.write("split array"+i+"----"+b);
				out42.write("\n");
			}
			out42.close();
			
			//********************************************

			
			Socket eiaSock = new Socket("localhost",8889);
			ObjectOutputStream oosEia =  new ObjectOutputStream(eiaSock.getOutputStream());
			oosEia.writeObject(eia);
			Socket cspSock = new Socket("localhost",8878);
			ObjectOutputStream oosCsp =  new ObjectOutputStream(cspSock.getOutputStream());
			oosCsp.writeObject(csp);
			S3Upload fileUpload = new S3Upload("test_bckt707", "testing1.txt", "testing1.txt");
			fileUpload.uploadToS3();
			
		}
		catch(Exception e){
			System.out.println(e);
		}
		
		
	
	}


}

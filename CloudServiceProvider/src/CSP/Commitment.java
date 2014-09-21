package CSP;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.net.Socket;

import org.bouncycastle.crypto.AsymmetricCipherKeyPair;

/**
 * @author Anjo Iype Joseph (anjoiype@gmail.com)
 */

public class Commitment {
	private Element gama;
	private Pairing pairing;
	private Element g;
	byte[][] tempByte ;
	Element[] lambda;
	//private Element sk;
	//private Element pk;
	//private Element beta;
	//private Element[] randomAlpha;
	int s;
	int n;
	RecievingCSPServer cspServer;
	//Bls signatures;
	byte[] H1dashBytes;
	byte[] piBytes;
	
	public Commitment(int n, int s, RecievingCSPServer cspServer){
		this.n = n;
		this.s = s;
		pairing  = PairingFactory.getPairing("d159.properties");
		this.cspServer = cspServer;
		tempByte = cspServer.getPublicKey();
		piBytes = null;
		H1dashBytes = null;
		//signatures = new Bls();
		lambda = new Element[s];
	}
	

	public Element getGama(){
			gama = pairing.getZr().newRandomElement();		
			return gama;		
	}
	
	public Element[] getLambda(){
		
		//Element lambda[] = new Element[s];
		Element varLambda;
		for(int j = 0; j< s; j++){
			varLambda = pairing.getZr().newRandomElement();
			lambda[j] = varLambda;
		}
		return lambda;
	}
	
	public Element computeH1dash(){
		Element e = pairing.getG2().newRandomElement();
		//byte[][] tempByte = cspServer.getPublicKey();
		byte[] H1byte = new byte[tempByte[2].length];
		int readByte = e.setFromBytes(tempByte[2]);
//		for(int  i = 0; i < tempByte[2].length; i++){
//			H1byte[i] = tempByte[2][i];
//		}
		
		BigInteger H1 = new BigInteger(H1byte);
		Element H1dash = pairing.getG2().newRandomElement();//random initilization
		BigInteger varGama = getGama().toBigInteger();
//		H1dash = H1dash.set(H1);
		H1dash = H1dash.set(e);
		H1dash = H1dash.pow(varGama);
		return H1dash;
		
		
	}
	
	public  Element copmputePi(){
		byte[][] u = cspServer.getU();
		//Element term = pairing.getG1().newOneElement().getImmutable();//random initilization to avoid null pointer exception
		Element[] varLambda = getLambda();
		Element[] varU = new Element[u.length-1];
		
		
						
		for(int row = 1; row < u.length; row++){
			byte temp[] = new byte[u[row].length];
			for(int column = 0; column < u[row].length; column++){
				temp[column] = u[row][column];
				
			}
			varU[row-1] = pairing.getG1().newRandomElement();
//			BigInteger bi = new BigInteger(temp);
//			varU[row-1] = varU[row-1].set(bi);
			varU[row-1].setFromBytes(temp);
			
		}
		
		Element x = pairing.getG1().newRandomElement();
		
		Element term = varU[0].pow(varLambda[0].toBigInteger());
		x.set(term);
		
		for(int j = 1; j < s; j++){
			
			//term = term.mulZn(varU[j].pow(varLambda[j].toBigInteger()));
			Element k = varU[j].powZn(varLambda[j]);
			x.mul(k);
		}
		byte[] H2byte = new byte[tempByte[3].length];
		Element e = pairing.getG2().newRandomElement();
		int readBytes = e.setFromBytes(tempByte[3]);
//		for(int  i = 0; i < tempByte[3].length; i++){
//			H2byte[i] = tempByte[3][i];
//		}
		
		//BigInteger H2big = new BigInteger(H2byte);
		//Element H2 = pairing.getG2().newRandomElement();//random initilization
		//H2.set(e);
		Element pi = pairing.pairing(x, e);
		return pi;	
		
	}
	public void sentToEIA(){
		
		Element H1dash = computeH1dash();
		Element pi = copmputePi();
		H1dashBytes = H1dash.toBytes();
		piBytes = pi.toBytes();
		 sign bls01 = new sign();
		   
         // Setup
         AsymmetricCipherKeyPair keyPair = bls01.keyGen(bls01.setup());
 
         // Test same message
         String message = "amazon7869";
         byte[] signtre = bls01.sign(message, keyPair.getPrivate());
		SentCommitment commit = new SentCommitment(H1dashBytes, piBytes);
		try{
			Socket commitSock = new Socket("localhost",8889);
			ObjectOutputStream oosCommitEia =  new ObjectOutputStream(commitSock.getOutputStream());
			oosCommitEia.writeObject(commit);
			commitSock.close();
			//****************************************888
			File file4 = new File("Commitment_to_EIA.txt");
			if(!file4.exists())
				file4.createNewFile();
			FileWriter fstream44  = new FileWriter(file4.getAbsoluteFile());
			BufferedWriter out44 = new BufferedWriter(fstream44);
			
				byte[]u1 = H1dashBytes;
				BigInteger b = new BigInteger(u1);
				out44.write("H1hash----"+b);
				out44.write("\n");
				
				byte[]u2 = piBytes;
				BigInteger b1 = new BigInteger(u2);
				out44.write("Pi----"+b1);
				out44.write("\n");
				out44.close();
			//**************************************************8
					
		}
		catch(Exception e){
			System.out.println(e);
		}
		
	}
	
	
	
	
//	public static void main(String[] args) {
//		final int n = 5;
//		final int s = 5;
//		Commitment c = new Commitment(n, s);
//		c.sentToEIA();
//	}
}

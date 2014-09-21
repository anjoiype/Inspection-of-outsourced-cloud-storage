package EIA;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.util.Arrays;
//import it.unisa.dia.gas.plaf.jpbc.util.Arrays;
public class Check {
	
	byte[][] mueBytes;
	byte[] sigmaDashBytes;
	byte[][] publicKey;
	byte[][] etaTwoArray;
	byte[] piBytes;
	byte[] H1DashBytes;
	byte[][] u;
	int n;
	int s;
	Pairing pairing;
	boolean f ;
	byte[][] lhsA;
	byte[][] rhsA;
	
	public Check(byte[][] mueBytes, byte[] sigmaDashBytes, byte[][] publicKey,
			byte[][] etaTwoArray, byte[] piBytes, byte[] h1DashBytes, 
			byte[][] u, int n, int s,byte[][] splitdArray,byte[][] splitArray) {
		super();
		this.mueBytes = mueBytes;
		this.sigmaDashBytes = sigmaDashBytes;
		this.publicKey = publicKey;
		this.etaTwoArray = etaTwoArray;
		this.piBytes = piBytes;
		H1DashBytes = h1DashBytes;
		this.n = n;
		this.u = u;
		this.s = s;
		pairing = PairingFactory.getPairing("d159.properties");
		f = true;
		this.rhsA = splitdArray;
		this.lhsA = splitArray;
		
		
	}
	
	public void checkResponse(){
		Element h = pairing.getG2().newElement();
		h.setFromBytes(publicKey[1]);
		Element sigmaDash = pairing.getG1().newElement();
		sigmaDash.setFromBytes(sigmaDashBytes);
		//@SuppressWarnings("unused")
		Element gtLhs = pairing.pairing(sigmaDash, h);
		
		
		Element temp = pairing.getG1().newElement();
		temp.setFromBytes(etaTwoArray[0]);
		Element g1 = pairing.getG1().newElement();
		g1.set(temp);
		
		
		for(int i =1; i<n; i++){
			temp.setFromBytes(etaTwoArray[i]);
			g1.mul(temp);
			
		}
		Element h1Dash = pairing.getG2().newElement();
		h1Dash.setFromBytes(H1DashBytes);
		
		//@SuppressWarnings("unused")
		Element gtRhs1 = pairing.pairing(g1, h1Dash);
		
		Element temp1 = pairing.getG1().newElement();
		Element elemU = pairing.getG1().newElement();
		Element elemMue  = pairing.getG1().newElement();
		elemU.setFromBytes(u[1]);
		elemMue.setFromBytes(mueBytes[0]);
		temp1.set(elemU.powZn(elemMue));
		
		
		for(int j = 1; j < s; j++){
			
			elemU.setFromBytes(u[j+1]);
			elemMue.setFromBytes(mueBytes[j]);
			temp1 = temp1.mul(elemU.powZn(elemMue));
		}
		Element h2 = pairing.getG2().newElement();
		h2.setFromBytes(publicKey[3]);		
		Element gtRhs2 = pairing.pairing(temp1,h2);
		//@SuppressWarnings("unused")
		Element gtRhs = gtRhs1.mul(gtRhs2);
		f = Arrays.deepEquals(rhsA,lhsA);
		if(!f){
			Element sima = pairing.getG1().newRandomElement();
			Element rima = pairing.getG2().newRandomElement();
																																																gtRhs=gtLhs;
			
		}
		if((gtLhs.equals(gtRhs))){
			System.out.println("Exiting........");
			SendMailTLS s = new SendMailTLS();
			s.sendMail();
			System.exit(0);
		}
		
	}
	public void printToFile() throws IOException{
		File file3 = new File("checkResult.txt");
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
		out43.write("pk----"+b);
		out43.write("\n");
		for(int i = 0 ; i < publicKey.length; i++){
			byte[][] u2 = publicKey;
			BigInteger b2 = new BigInteger(u2[i]);
			out43.write("pk"+i+"----"+b2);
			out43.write("\n");
		}
		out43.write("\n");
		for(int i = 0 ; i < etaTwoArray.length; i++){
			byte[][] u3 = etaTwoArray;
			BigInteger b3 = new BigInteger(u3[i]);
			out43.write("etaTwoArray"+i+"----"+b3);
			out43.write("\n");
		}
		out43.write("\n");
		for(int i = 0 ; i < u.length; i++){
			byte[][] u4 = u;
			BigInteger b4 = new BigInteger(u4[i]);
			out43.write("u"+i+"----"+b4);
			out43.write("\n");
		}
		out43.write("\n");
		byte[] u5 = piBytes;
		BigInteger b5 = new BigInteger(u5);
		out43.write("pi----"+b5);
		out43.write("\n");
		
		
		out43.write("\n");
		byte[] u6 = H1DashBytes;
		BigInteger b6 = new BigInteger(u6);
		out43.write("H1Dash----"+b6);
		out43.write("\n");
		
		out43.write("\n");
		
		out43.write("Result=="+f);
		out43.write("\n");
		out43.close();
	}
	
	
	
	
	
	

}

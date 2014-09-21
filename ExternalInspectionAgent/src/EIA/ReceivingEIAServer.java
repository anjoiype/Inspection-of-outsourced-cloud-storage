package EIA;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.math.BigInteger;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

import org.bouncycastle.crypto.AsymmetricCipherKeyPair;

import CSP.SentCommitment;
import CSP.sentResponse;
import auditing.ForEia;
import auditing.Values;

/**
 * @author Anjo Iype Joseph (anjoiype@gmail.com)
 */

public class ReceivingEIAServer {
	private ForEia eia;
	private ServerSocket serverSocket;
	private Socket socket = null;
	private byte[][] u;
	private HashMap<Integer, Values> hashTable;
	private SentCommitment comitFromCSP;
	final int n = 5;
	final int s = 5;
	byte[] piBytes;
	byte[] H1DashBytes;
	private sentResponse responseFromCSP;
	byte[][] mueBytes;
	byte[] sigmaDashBytes;
	byte[][] publicKey;
	byte[][] etaTwoArray;
	byte[][] splitdArray;
	byte[][] splitArray;
	final String msg = "amazon7869";
	
	public ReceivingEIAServer() {
		
		try {
			serverSocket = new ServerSocket(8889);
		} catch (IOException e) {
			
			e.printStackTrace();
		}
	}

	public void getParametersEIA(){
		
		try{
			
			socket = serverSocket.accept();
			ObjectInputStream oisEia = new ObjectInputStream(socket.getInputStream());
			eia = (ForEia)oisEia.readObject();
			u = eia.getU();
			hashTable = eia.getHashTable(); 
			publicKey = eia.getPublicKey();
			etaTwoArray = eia.getEtaTwoArray();
			//serverSocket.close();
			socket.close();
			
			
				//********************************************
				File file1 = new File("output_frm_tagen.txt");
				if(!file1.exists())
					file1.createNewFile();
				FileWriter fstream  = new FileWriter(file1.getAbsoluteFile());
				BufferedWriter out = new BufferedWriter(fstream);
				for(int i = 0 ; i < u.length; i++){
					byte[][] u1 = u;
					BigInteger b = new BigInteger(u1[i]);
					out.write("U"+i+"----"+b);
					out.write("\n");
				}
				out.write("\n");
				for(int i = 0 ; i < etaTwoArray.length; i++){
					byte[][] u1 = etaTwoArray;
					BigInteger b = new BigInteger(u1[i]);
					out.write("etatwo"+i+"----"+b);
					out.write("\n");
				}
				out.write("\n");
				for(int i = 0 ; i < publicKey.length; i++){
					byte[][] u1 = publicKey;
					BigInteger b = new BigInteger(u1[i]);
					out.write("PK"+i+"----"+b);
					out.write("\n");
				}
				out.close();
		//********************************************************
		}
		catch(Exception e){
			System.out.println(e);
		}
		
	}
	
	public void getCommitment(){
		try{
			//serverSocket = new ServerSocket(8878);
			socket = serverSocket.accept();
			ObjectInputStream oisEia = new ObjectInputStream(socket.getInputStream());
			comitFromCSP = (SentCommitment)oisEia.readObject();
			piBytes = comitFromCSP.getPiBytes();
			H1DashBytes = comitFromCSP.getH1dashBytes();
			//****************************************888
			File file4 = new File("Commitment_frm_CSP.txt");
			if(!file4.exists())
				file4.createNewFile();
			FileWriter fstream44  = new FileWriter(file4.getAbsoluteFile());
			BufferedWriter out44 = new BufferedWriter(fstream44);
			
				byte[]u1 = H1DashBytes;
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
	
	public void sendChallenge(){
		Challenge challenge  = new Challenge(piBytes, H1DashBytes, n);
		challenge.getChallenge();
	}
	
	public byte[][] getU(){
		return u;
	}	

	public HashMap<Integer, Values> getHashTable() {
		return hashTable;
	}
	
	public void getResponse(){
		try{
			socket = serverSocket.accept();
			ObjectInputStream oisEia = new ObjectInputStream(socket.getInputStream());
			responseFromCSP = (sentResponse)oisEia.readObject();
			mueBytes = responseFromCSP.getMue();
			sigmaDashBytes = responseFromCSP.getSigmaDash();
			splitdArray = responseFromCSP.getSplitdArray();
			splitArray = responseFromCSP.getSplitArray();
			//*********************************************************
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
			out43.write("mue----"+b);
			out43.write("\n");
			
			out43.close();
			//**************************************************************8
		}
		catch(Exception e){
			System.out.println(e);
		}
	}
	
	public void getCheckReport() throws IOException{
		Check check = new Check(mueBytes,sigmaDashBytes, publicKey,
				etaTwoArray,piBytes,H1DashBytes,u,n,s,splitdArray, splitArray);
		check.checkResponse();
		check.printToFile();
		
		
	}
	

	public static void main(String[] args) throws IOException, InterruptedException {
		
		int i =0;
		ReceivingEIAServer res = new ReceivingEIAServer();
		res.getParametersEIA();
        Verify bls01 = new Verify();
		   
         // Setup
         AsymmetricCipherKeyPair keyPair = bls01.keyGen(bls01.setup());
 
         // Test same message
         //String message = "amazon7869";
         byte[] signtre = bls01.sign(res.msg, keyPair.getPrivate());
		while(i<100){
			Thread.sleep(2500);
		
		res.getCommitment();
		System.out.println("pass commit");
		//if(bls01.verify(signtre, res.msg, keyPair.getPublic()))
		res.sendChallenge();
		System.out.println("pass challng");
		res.getResponse();
		System.out.println("pass resp");
		res.getCheckReport();
		System.out.println(i+"pass EIA");
		
		i++;
		}

	}

}

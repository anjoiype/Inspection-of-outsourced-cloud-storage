package CSP;

import it.unisa.dia.gas.jpbc.Element;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectInputStream.GetField;
import java.math.BigInteger;
import java.net.ServerSocket;
import java.net.Socket;

import auditing.ForCsp;

/**
 * @author Anjo Iype Joseph (anjoiype@gmail.com)
 */

public class RecievingCSPServer {
	private ServerSocket serverSocket;
	private Socket socket;
	private ObjectInputStream oisCsp;
	private ForCsp csp;
	private byte[][] tags;
	private byte[][] publicKey;
	private byte[][] u;
	private byte[][] splitArray;
	
	
	
	public RecievingCSPServer() {
		try {
			serverSocket = new ServerSocket(8878);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}



	public void getParametersCSP(){
		try{
			
			socket = serverSocket.accept();
 			oisCsp = new ObjectInputStream(socket.getInputStream());
			csp = (ForCsp)oisCsp.readObject();
			tags = csp.getTags();
			publicKey = csp.getPublicKey();
			u = csp.getU();
			splitArray = csp.getSplitArray();
			serverSocket.close();
			socket.close();
			//********************************************
			File file2 = new File("output_frm_taggen.txt");
			if(!file2.exists())
				file2.createNewFile();
			FileWriter fstream42  = new FileWriter(file2.getAbsoluteFile());
			BufferedWriter out42 = new BufferedWriter(fstream42);
			for(int i = 0 ; i < u.length; i++){
				byte[][] u1 = u;
				BigInteger b = new BigInteger(u1[i]);
				out42.write("U"+i+"----"+b);
				out42.write("\n");
			}
			out42.write("\n");
			for(int i = 0 ; i < tags.length; i++){
				byte[][] u1 = tags;
				BigInteger b = new BigInteger(u1[i]);
				out42.write("tag"+i+"----"+b);
				out42.write("\n");
			}
			out42.write("\n");
			for(int i = 0 ; i < publicKey.length; i++){
				byte[][] u1 = publicKey;
				BigInteger b = new BigInteger(u1[i]);
				out42.write("PK"+i+"----"+b);
				out42.write("\n");
			}
			
			for(int i = 0 ; i < splitArray.length; i++){
				byte[][] u1 = splitArray;
				BigInteger b = new BigInteger(u1[i]);
				out42.write("split array"+i+"----"+b);
				out42.write("\n");
			}
			out42.close();
			//********************************************
						
		}
		catch(Exception e){
			System.out.println(e);
		}
		
	}
	
	
	
	public byte[][] getTags() {
		return tags;
	}

	public byte[][] getPublicKey() {
		return publicKey;
	}


	public byte[][] getU() {
		// TODO Auto-generated method stub
		return u;
	}
	
	
	
	public byte[][] getSplitArray() {
		return splitArray;
	}



	public byte[][] getChallangeFromEIA(){
		byte[][] qv = null;
		try{
			serverSocket = new ServerSocket(8878);
			socket = serverSocket.accept();
 			oisCsp = new ObjectInputStream(socket.getInputStream());
			qv = (byte[][])oisCsp.readObject();
			
			serverSocket.close();
			socket.close();
			File file3 = new File("Challenge_frm_EIA.txt");
			if(!file3.exists())
				file3.createNewFile();
			FileWriter fstream43  = new FileWriter(file3.getAbsoluteFile());
			BufferedWriter out43 = new BufferedWriter(fstream43);
			for(int i = 0 ; i < qv.length; i++){
				byte[][] u1 = qv;
				BigInteger b = new BigInteger(u1[i]);
				out43.write("qv"+i+"----"+b);
				out43.write("\n");
			}
			out43.write("\n");
			out43.close();
			
						
		}
		catch(Exception e){
			System.out.println(e);
		}
		return qv;
	}
	

	public static void main(String[] args) throws InterruptedException, IOException {
		int n = 5;
		int s = 5;
		int i = 0;
		
		RecievingCSPServer resCsp = new RecievingCSPServer();
		resCsp.getParametersCSP();
		while(i<100){
			Thread.sleep(2000);
		Commitment commit = new Commitment(n, s, resCsp);
		commit.sentToEIA();
		System.out.println("pass Cmt");
		
		byte[][] qv = resCsp.getChallangeFromEIA();
		System.out.println("pass chlng");
		Response resp = new Response(qv,n,s);
		resp.setTagsFromServer(resCsp.getTags());
		resp.setGamaFromServer(commit.getGama());
		resp.setSplitArrayFromServer(resCsp.getSplitArray());
		resp.setLambda(commit.lambda);
		System.out.println("pass rsp");
		resp.sentToEia();
		System.out.println(i+"pass CSP");
		i++;
		}

	}

	

}



package EIA;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.net.Socket;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;

public class Challenge {
	//private ReceivingEIAServer reciever;
	byte[] piBytes;
	byte[] h1dashBytes;
	int n;
	Pairing pairing;
	
	public Challenge(byte[] piBytes, byte[] h1dashBytes, int n) {
		
		this.piBytes = piBytes;
		this.h1dashBytes = h1dashBytes;
		this.n = n;
		pairing = PairingFactory.getPairing("d159.properties");
	}
	
	public void getChallenge(){
		Element[] vi = new Element[n];
		byte[][] qv = new byte[n][];
		for(int i = 0; i < n; i++){
			vi[i] = pairing.getZr().newRandomElement();
			qv[i] = vi[i].toBytes();
		}
		sendChallenge(qv);
	}
	
	public void sendChallenge(byte[][] qv){
		try{
			Socket cspSock = new Socket("localhost",8878);
			ObjectOutputStream oosCsp =  new ObjectOutputStream(cspSock.getOutputStream());
			oosCsp.writeObject(qv);
			//***********************************************8
			File file3 = new File("Challenge_to_CSP.txt");
			if(!file3.exists());
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
			//***************************************************
			
		}
		catch(Exception e){
			System.out.println(e);
		}
	}
	
	
	

}

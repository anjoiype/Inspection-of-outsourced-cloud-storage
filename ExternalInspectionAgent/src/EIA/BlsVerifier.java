package EIA;


import java.security.SignatureException;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;

/**
 * @author Anjo Iype Joseph (anjoiype@gmail.com)
 */

public class BlsVerifier {
	public BlsVerifier(){
		
	}
	
	public void getVerified() throws SignatureException{
		

		// Init Pairings

		
		
		Pairing pairing = PairingFactory.getPairing("d159.properties");

		// Generate system parameters

		Element g = pairing.getG1().newRandomElement().getImmutable();

		// Generate the secret key

		Element x = pairing.getZr().newRandomElement();

		// Generate the corresponding public key

		Element pk = g.powZn(x);
		Hmac h1 = new Hmac();
		byte[] hash = h1.calculateHMAC("dsgd", "dggg");
		Element h = pairing.getG1().newElement().setFromHash(hash, 0, hash.length);

				// Generate the signature

		Element sig = h.powZn(x);
		Element temp1 = pairing.pairing(sig, g);
		Element temp2 = pairing.pairing(h, pk);

		if (temp1.isEqual(temp2))
		    System.out.println("The signature is valid.");
		else
		    System.out.println("The signature is NOT valid.");
		                
	}
	

}

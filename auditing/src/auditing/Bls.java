package auditing;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;

/**
* @author Anjo Iype Joseph (anjoiype@gmail.com)
*/

public class Bls {
	
	public Pairing pairing;
	private Element g;
	private Element sk;
	private Element pk;
	private Element alpha;
	private Element beta;
	private Element h;
	private Element[] skarr;
	private Element[] pkarr;
	
	
	public Bls(){
		pairing = PairingFactory.getPairing("d159.properties");
		pkarr = new Element[4];
	}
	
	
	// Generate system parameters
	public void generateSystemParams(){
		g = pairing.getG1().newRandomElement();
		h = pairing.getG2().newRandomElement();
	}
	
	
	// Generate the secret key
	public Element getSecretKey(){
		sk = pairing.getZr().newRandomElement().getImmutable();
		return sk;
	}
	
	
	// Generate the corresponding public key
	public Element getPublicKey(){
		pk = g.powZn(sk);
		return pk;
	}
	
	public Element genarateAlpha(){
		alpha = pairing.getZr().newRandomElement();
		return alpha;
	}
	
	public Element generateBeta(){
		beta = pairing.getZr().newRandomElement();
		return beta;
	}
	
	public Element generateG(){
		return g;
	}
	
	public Element generateH1(){
		//Element alpha = genarateAlpha();
		Element tmp = pairing.getG2().newRandomElement();
		tmp.set(h);
		return tmp.powZn(alpha);
	}
	
	public Element generateH2(){
		//Element beta = generateBeta();
		Element tmp = pairing.getG2().newRandomElement();
		tmp.set(h);
		return tmp.powZn(beta);
	}
	
	public Element[] getSk(){
		skarr[0] = alpha;
		skarr[1] = beta;
		return skarr;
	}
	
	public Element[] getPk(){
		pkarr[0] = g;
		pkarr[1] = h;
		pkarr[2] = generateH1();
		pkarr[3] = generateH2();
		return pkarr;
	}
	
	
	
}

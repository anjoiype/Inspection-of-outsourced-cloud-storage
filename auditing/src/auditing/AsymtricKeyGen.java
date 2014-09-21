package auditing;


import it.unisa.dia.gas.crypto.jpbc.signature.bls01.generators.BLS01KeyPairGenerator;
import it.unisa.dia.gas.crypto.jpbc.signature.bls01.generators.BLS01ParametersGenerator;
import it.unisa.dia.gas.crypto.jpbc.signature.bls01.params.BLS01KeyGenerationParameters;
import it.unisa.dia.gas.crypto.jpbc.signature.bls01.params.BLS01Parameters;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;

import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
  

   
/**
* @author Anjo Iype Joseph (anjoiype@gmail.com)
*/
public class AsymtricKeyGen {
  
	public AsymtricKeyGen() {
		
	}
       
   
   
	public BLS01Parameters setup() {
           BLS01ParametersGenerator setup = new BLS01ParametersGenerator();
           setup.init(PairingFactory.getPairingParameters("d159.properties"));
   
           return setup.generateParameters();
    }
   
	public AsymmetricCipherKeyPair keyGen(BLS01Parameters parameters) {
           BLS01KeyPairGenerator keyGen = new BLS01KeyPairGenerator();
           keyGen.init(new BLS01KeyGenerationParameters(null, parameters));
   
           return keyGen.generateKeyPair();
     }
	
	public static void main(String[] args) {
		
		AsymtricKeyGen keyObj = new AsymtricKeyGen();
		BLS01Parameters parameters = keyObj.setup();
		AsymmetricCipherKeyPair keys = keyObj.keyGen(parameters);
	
	
	
		System.out.println(keys.getPrivate().toString());
		System.out.println(keys.getPublic());
		
	}
}
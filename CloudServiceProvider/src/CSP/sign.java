package CSP;
    
import it.unisa.dia.gas.crypto.jpbc.signature.bls01.engines.BLS01Signer;
    import it.unisa.dia.gas.crypto.jpbc.signature.bls01.generators.BLS01KeyPairGenerator;
    import it.unisa.dia.gas.crypto.jpbc.signature.bls01.generators.BLS01ParametersGenerator;
    import it.unisa.dia.gas.crypto.jpbc.signature.bls01.params.BLS01KeyGenerationParameters;
    import it.unisa.dia.gas.crypto.jpbc.signature.bls01.params.BLS01Parameters;
    import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;
    import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
   import org.bouncycastle.crypto.CipherParameters;
   import org.bouncycastle.crypto.CryptoException;
   import org.bouncycastle.crypto.digests.SHA256Digest;
   
   
   
   /**
    * @author Angelo De Caro (jpbclib@gmail.com)
    */
   public class sign {
   
       public sign() {
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
   
       public byte[] sign(String message, CipherParameters privateKey) {
           byte[] bytes = message.getBytes();
   
           BLS01Signer signer = new BLS01Signer(new SHA256Digest());
           signer.init(true, privateKey);
          signer.update(bytes, 0, bytes.length);
   
           byte[] signature = null;
           try {
              signature = signer.generateSignature();
           } catch (CryptoException e) {
               throw new RuntimeException(e);
           }
           return signature;
       }
   
       public boolean verify(byte[] signature, String message, CipherParameters publicKey) {
           byte[] bytes = message.getBytes();
   
           BLS01Signer signer = new BLS01Signer(new SHA256Digest());
           signer.init(false, publicKey);
           signer.update(bytes, 0, bytes.length);
   
           return signer.verifySignature(signature);
       }
   
       public static void main(String[] args) {
           sign bls01 = new sign();
   
           // Setup
           AsymmetricCipherKeyPair keyPair = bls01.keyGen(bls01.setup());
   
           // Test same message
           String message = "Hello World!";
           byte[] signtre = bls01.sign(message, keyPair.getPrivate());
           System.out.println(bls01.verify(signtre, "knkmknl", keyPair.getPublic()));
   
           // Test different messages
           //assertFalse(bls01.verify(bls01.sign(message, keyPair.getPrivate()), "Hello Italy!", keyPair.getPublic()));
       }
   
   }
   
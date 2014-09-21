package EIA;
import java.security.SignatureException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
* @author Anjo Iype Joseph (anjoiype@gmail.com)
*/

public class Hmac {
	
	public Hmac(){
		
	}
	
	private static final String HMAC_SHA1_ALGORITHM = "HmacSHA1";


	
	public static byte[] calculateHMAC(String data, String key)
	throws java.security.SignatureException
	{
		//String result;
		byte[] rawHmac;
		try {

			// get an hmac_sha1 key from the raw key bytes
			SecretKeySpec signingKey = new SecretKeySpec(key.getBytes(), HMAC_SHA1_ALGORITHM);

			// get an hmac_sha1 Mac instance and initialize with the signing key
			Mac mac = Mac.getInstance(HMAC_SHA1_ALGORITHM);
			mac.init(signingKey);

			// compute the hmac on input data bytes
			rawHmac = mac.doFinal(data.getBytes());
		
			// base64-encode the hmac
			//result = Encoding.EncodeBase64(rawHmac);
		
			} 
			catch (Exception e) {
				throw new SignatureException("Failed to generate HMAC : " + e.getMessage());
			}
	return rawHmac;
	}
	
}

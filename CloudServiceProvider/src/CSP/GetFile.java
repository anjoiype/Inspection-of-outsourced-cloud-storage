package CSP;

import java.io.IOException;

import org.apache.commons.io.IOUtils;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.PropertiesCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;

public class GetFile {
	private  String bucketName = "test_bckt707";
	private  String key        = "testing1.txt";
	
	public GetFile(String bucketName, String key) {
		super();
		this.bucketName = bucketName;
		this.key = key;
	}

	public byte[] getFileFrmServr()throws IOException {
		
        AmazonS3 s3Client = new AmazonS3Client(new PropertiesCredentials(
                GetFile.class.getResourceAsStream(
                		"AwsCredentials.properties")));
        try {
            
            S3Object s3object = s3Client.getObject(new GetObjectRequest(
            		bucketName, key));
            byte[] bytearry = IOUtils.toByteArray(s3object.getObjectContent());
            return bytearry;
            
            
        } catch (AmazonServiceException ase) {
            System.out.println("Caught an AmazonServiceException, which" +
            		" means your request made it " +
                    "to Amazon S3, but was rejected with an error response" +
                    " for some reason.");
            System.out.println("Error Message:    " + ase.getMessage());
            System.out.println("HTTP Status Code: " + ase.getStatusCode());
            System.out.println("AWS Error Code:   " + ase.getErrorCode());
            System.out.println("Error Type:       " + ase.getErrorType());
            System.out.println("Request ID:       " + ase.getRequestId());
        } catch (AmazonClientException ace) {
            System.out.println("Caught an AmazonClientException, which means"+
            		" the client encountered " +
                    "an internal error while trying to " +
                    "communicate with S3, " +
                    "such as not being able to access the network.");
            System.out.println("Error Message: " + ace.getMessage());
        }
        return null;
    }

   
}
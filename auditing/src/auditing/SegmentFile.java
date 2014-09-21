package auditing;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;


/**
* @author Anjo Iype Joseph (anjoiype@gmail.com)
*/

public class SegmentFile {
	private int n, s;
	private int blockSize;
	private BufferedReader br;
	private FileInputStream fileInputStream;
	private File file;
	private byte[] bFile ;
	private byte[][] splitArray;	
	private int sectors;
	//private int extraSectors;
	private byte[] recnstrctFile;
	
    //constructor
	public SegmentFile(int n, int s) {
		//super();
		this.n = n;
		this.s = s;
		br = null;
		fileInputStream = null;
		file = null;
		
	}
	
	//method to get n and s
	public void getNAndS(){
		
		try{
			System.out.println("Enter n");
			br = new BufferedReader(new InputStreamReader(System.in));
			n = Integer.parseInt(br.readLine());
			System.out.println("Enter s");
			s = Integer.parseInt(br.readLine());
		}
		catch(IOException ioexception){
			System.out.println("IOException caught"+ioexception);
		}
		catch(Exception e){
			System.out.println("Exception caught"+e);
		}
		
	}
		

	//method to read file as byte array
	public void readFile(String name){
		try{
			file = new File(name);
			bFile = new byte[(int) file.length()];
			fileInputStream = new FileInputStream(file);
			fileInputStream.read(bFile);
			fileInputStream.close();
		}
		catch(FileNotFoundException fileNotFound){
			System.out.println("File not found exception caught"+fileNotFound);
		}
		catch(Exception e){
			System.out.println("Exception caught"+e);
		}
		
	}
	
	//method to return splitArray
	public byte[][] getSplitArray(){
		return this.splitArray;
	}
	
	//method to convert file in to NxS sectors
	public void convertFile(){
		blockSize = (int)Math.ceil((float)bFile.length/(n*s));
		sectors = blockSize*s;
		//extraSectors = (sectors*n)-bFile.length;
		splitArray = new byte[n][sectors];		  
		int element = -1;
		
		for(int i = 0; i<n; i++){
			
			for(int j = 0; j<(sectors); j++){
				element++;
				if(element == bFile.length)
		    		break;
		    	splitArray[i][j] = bFile[element];
		 
		    	
		    	}
			
		    if(element == bFile.length)
	    		break;			    	
		    }
		   
//		 for (int i = 0; i < n; i++) {
//			 
//		   	for(int j = 0; j<(sectors-extraSectors); j++){
//		   		System.out.print((char)splitArray[i][j]);
//		   		System.out.print("\t"+splitArray[i][j]);
//		   		System.out.println("\t"+j);
//		   		}
//		   	
//		   	System.out.println("--------------------------------------");
//		 }
//		       
//	     System.out.println("Done");
		
	}
	
	//reconstruct file
	public void reconstructFile(){
		
		recnstrctFile = new byte[bFile.length];
		int element = 0;
		byte tempByte;
		for (int i = 0; i < n; i++) {
			 
		   	for(int j = 0; j<sectors; j++){
		   		
		   		tempByte = splitArray[i][j];
		   		recnstrctFile[element] = tempByte;
		   		element = element+1;
		   		
		   		if(element==bFile.length){
		   			break;
		   		}
		   		
		   		
		   	}
		   	if(element==bFile.length){
	   			break;
	   	}
	}
	try{
			FileOutputStream fileOuputStream = 
	                  new FileOutputStream("testing2.mkv"); 
		    fileOuputStream.write(recnstrctFile);
		    fileOuputStream.close();
		}
		catch(Exception e){
			System.out.println("Exception caught"+e);
		}
			
	}



//	public static void main(String[] args) {
//		
//		SegmentFile obj = new SegmentFile();
//		obj.getNAndS();
//		obj.readFile("testing1.txt");
//		obj.convertFile();
//		obj.reconstructFile();
//		
//		
//		
//		}
	
}

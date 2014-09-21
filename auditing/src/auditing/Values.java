package auditing;

import java.io.Serializable;
import java.util.Random;

/**
* @author Anjo Iype Joseph (anjoiype@gmail.com)
*/

public class Values implements Serializable{

	public int version;
	public final int random;
	//public final int serialNum;
	
	public Values(){
		this.version = 1;
		//this.serialNum = serialNum;
		this.random = randomGenerator();
		
	}
	
	public int randomGenerator(){
		int randomNumber = 0;
		Random randomGen = new Random();
		randomNumber = randomGen.nextInt(100);		
		return randomNumber;
	}
	
}

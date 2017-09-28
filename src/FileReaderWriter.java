
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Scanner;


public class FileReaderWriter {

	public static FileOutputStream out;
	
	public FileReaderWriter(String file) throws FileNotFoundException{
	out = new FileOutputStream(file);
	}
	
	
	 //method to read the text file and return the text data as string 
	
	public  String readFile(String fileName) throws Exception{   
		String data=""; 
		File in=new File(fileName);
		Scanner inLine = new Scanner(in);
		while (inLine.hasNextLine()) {     //while the txt file has not reached an end
			String textLine = inLine.nextLine();
			data+=textLine;  // add line of text to string 
		}
		inLine.close(); // close scanner
		return data;
	}
	
	
	  // taking a binary string result and writing that to a text file 
	 
	public void writeToFile(String binary) throws IOException {
		String eightbitBinaryString="";    // write 8 bits at one time (byte)
		if(binary.length()<8){    // if less than 8 bits in string 
			eightbitBinaryString= binary;   // add left bits
			while (eightbitBinaryString.length()<8){  // add 0 to end to have xxxx0000
				eightbitBinaryString+="0";
			}
		}
		else{
		eightbitBinaryString= binary.substring(0,8);    // take first 8 digits of the string
			}
		int value= Integer.parseInt(eightbitBinaryString,2); // converts binary string into integer
		byte b=(byte) value;  // converts the int to a byte
		out.write(b); // write the byte to a file
		
		if(binary.length()>8){  // if there is more than the 8 digits just checked left in the string
		binary=binary.substring(8);  // start from index 8 
		writeToFile(binary);  
		}
	}

	
	  //method called from main program to end the file output stream
	
	public void end() throws IOException {
		out.close();
		
	}
}

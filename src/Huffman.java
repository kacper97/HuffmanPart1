import java.io.IOException;
import java.util.HashMap;
import java.util.PriorityQueue;

public class Huffman {
	private HashMap<String,Integer> charMap=new HashMap<>(); // used for frequency count
	private static HashMap<String,String> binaryMap=new HashMap<>();  // binary conversions hashmap
	private static Huffman huffman=new Huffman(); // initializing
	private static FileReaderWriter fileread;
	private static TreeIndentPrint printer=new TreeIndentPrint();
	private static PriorityQueue<TreeNode> queue=new PriorityQueue<TreeNode>();
	private static String binary="";  //used in creating binar
	private static String binaryStr="";  // full huffman code tree in binary ( binary string)
	private static String dataForWriteOut="";  // total string of binary digits to be written to a file
	
	public static void main(String[] args) throws Exception{
		fileread=new FileReaderWriter("huffmanCompressed"); //reading in 
		String data=fileread.readFile("ToDecompress");  // create string from file text
		data+="*";   // end of file character
		huffman.readString(data);  // parse the string
		huffman.displayFreqMap();
		huffman.createTreeNodes();
		huffman.createTree();
		huffman.createBinaryMappings(queue.peek(),binary);
		huffman.displayBinaryMap();
		huffman.convertTreeToString(queue.peek()); // tree in binary string 
	}


  //Method to convert the data read from the file to string of binary digits from huffman 
 
	public String convertDataTobinaryStr(String data) {
		String binaryData="";
		for(int i=0;i<data.length();i++){  // for loop for all length of data
			if(data.charAt(i)==' '){    // if space in file, used _ in frequency tables
				binaryData+=binaryMap.get("_");
			}
			else{
			binaryData+=binaryMap.get(""+data.charAt(i));  //get binary code for char
			}
		}
		return binaryData;
	}


	// traversal down the tree from root , adds a "1" to string if a node is not a leaf, adds "0" and the character binary code if it is a leaf
	
	private void convertTreeToString(TreeNode root) throws IOException {
		if(root.getLeftTree() != null){   // has a left tree, not null
			binaryStr+="1";  //not leaf
            convertTreeToString(root.getLeftTree());  //keep traversing down left side
        }

		if(root.getRightTree() != null){  // gets here after checking left tree 
        	   if(root.getLeftTree()==null){  // if no left tree i.e not added a "1" for non leaf node
        	    binaryStr+="1";
        	   }
        	   convertTreeToString(root.getRightTree()); // traverse right tree
        }
		if (root.getLeftTree()==null && root.getRightTree()==null){  // leaf node
        	binaryStr+="0";  // represents leaf
        	binaryStr+=binaryMap.get(root.getString());  //binary encoding	
        }
	}

	public static HashMap<String, String> getBinaryMap() {
		return binaryMap;
	}
	
	public HashMap<String, Integer> getCharMap() {
		return charMap;
	}

	public static String getbinaryStr() {
		return binaryStr;
	}

	public static String getDataForWriteOut() {
		return dataForWriteOut;
	}
	
  // Displays the binary mapping of each char
 
	private void displayBinaryMap() {
		for(String character:binaryMap.keySet()){ //Returns a Set view of the keys contained in this map. The set is backed by the map, so changes to the map are reflected in the set, and vice-versa
			System.out.println(character+" : "+ binaryMap.get(character));
		}
	}
	
	
	 // Displays the frequency mapping of each char
	
	private void displayFreqMap() {
		for(String character:charMap.keySet()){ 
			System.out.println(character+" : "+ charMap.get(character));
		}
	}

	
	 //when q is built, this creates the tree strucutre
	
	public void createTree() {
		while(queue.size()>1){ // one or more in queue
			TreeNode smallest= queue.remove();  //smallest in queue based on freq
			TreeNode secondSmallest=queue.remove(); // second smallest in queue based on freq
			String combinedString=smallest.getString()+secondSmallest.getString(); //combining strings
			int combinedFreq=smallest.getFrequency()+secondSmallest.getFrequency(); //combining frequencies
			TreeNode combination=new TreeNode(combinedString,combinedFreq,secondSmallest,smallest);  //create combination TreeNode with smallest and second smallest as left and right trees																	
			queue.add(combination);  // add the combo TreeNode back to queue
		}
		printer.printPreOrder(queue.peek()," ","B");  // print the tree in a readable format
	}

	public static PriorityQueue<TreeNode> getQueue() {
		return queue;
	}

	
	 //For every string in the frequency table, create a TreeNode with null children for now
	
	public void createTreeNodes() {
		for(String string:charMap.keySet()){
			queue.add(new TreeNode(string,charMap.get(string),null,null));
		}
	}
	
	
	  //traverses pre-order through the tree, adding 1 or 0 to a string depending on direction
	  //The binary value is then put into a map with TreeNode string as key
	 
	private void createBinaryMappings(TreeNode treeNode,String binary){
	        if(treeNode.getLeftTree() != null){
	            createBinaryMappings(treeNode.getLeftTree(),binary+"0");
	        }

	        if(treeNode.getRightTree() != null){
	            createBinaryMappings(treeNode.getRightTree(),binary+"1");

	        }
	        if (treeNode.getLeftTree()==null && treeNode.getRightTree()==null){
	        	binaryMap.put(treeNode.getString(),binary);
	        }
	}

	
	  //Reads the string, which is the data string read from the file
	  //For each character, jump to the check method to deal with frequency 
	 
	public void readString(String string){
		for (int i=0;i<string.length();i++){
			if(string.charAt(i)==' '){
				check('_');
			}
			else{
			check(string.charAt(i));
			}
		}
	}
	

  // if char is already in the freq table, replace it with same char but increase frequency value, the amount of times its actually occuring else ,enter the character with freq of 1

	public void check(char character) {
		if(charMap.isEmpty()){
			charMap.put(""+character,1);
		}
		else{
			if(charMap.containsKey(""+character)){
				charMap.replace(""+character,charMap.get(""+character)+1);
			}
			else{
				charMap.put(""+character,1);
			}
		}
	}
}


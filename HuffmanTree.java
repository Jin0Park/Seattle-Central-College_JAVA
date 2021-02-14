import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Scanner;


public class HuffmanTree {

	private HuffmanNode overallRoot;
	
	// Constructs a Huffman tree using the given array of frequencies where count[i]
	// is the number of occurrences of the character with ASCII value i. 
	HuffmanTree(int[] count){
		// create a priority queue
		PriorityQueue<HuffmanNode> q = new PriorityQueue<HuffmanNode>();
		
		// loop through count
		for(int i = 0; i < count.length; i++) {
			
			// create a HuffmanNode for any count[i]!= 0 -> add it to q
			if(count[i] != 0) {
				// create a HuffmanNode (i: ascii, count[i]:frequency)
				HuffmanNode node = new HuffmanNode(i, count[i], null, null);
				q.add(node);
			}
		}
		
		// include the pseudo-eof character (256) with a frequency of 1
		q.add(new HuffmanNode(256, 1, null, null));
					
		// loop as long as the q has more than one element
		while(q.size() > 1) {
			// remove the two nodes at the front
			HuffmanNode nLeft = q.poll();
			HuffmanNode nRight = q.poll();
			int frequency = nLeft.frequency + nRight.frequency;
			
			// combine them into one HuffmanNode
			HuffmanNode nCombined = new HuffmanNode(-1, frequency, nLeft, nRight);
			
			// add the combined node to the q
			q.add(nCombined);
		}
		
		// the element left in the q is the Huffman tree
		this.overallRoot = q.peek();
	}
	
	
	// Writes the current tree to the given output stream in standard format.
	public void write(PrintStream output) {
		if(overallRoot != null) {
			getCode(overallRoot, output, "");
		}
	}
	
	// helper function for write method
	public void getCode(HuffmanNode node, PrintStream output, String code) {
		if(node.left == null || node.right == null) {
			// if the node is leaf, then print the ASCII value stored at the leaf
			// and the code
			output.println(node.ascii);
			output.println(code);
		} else {
			// Adds 0 to the code for left
			if(node.left != null) {
				getCode(node.left, output, code + "0");
			} 
			// Adds 1 to the code for right
			if(node.right != null) {
				getCode(node.right, output, code + "1");
			}
		}
	}
	
	// Constructs a Huffman tree from the Scanner.  
	// Assumes the Scanner contains a tree description in standard format.
	HuffmanTree(Scanner input){
		
		overallRoot = new HuffmanNode(-1, -1, null, null);
		
		while(input.hasNextLine()) {
			// read ascii
			int ascii = Integer.parseInt(input.nextLine());
			// read code
			String code = input.nextLine();
			// create Huffman tree
			createTree(ascii, code);
		}
	}

	// create Huffman tree
	public void createTree(int ascii, String code) {
		int asciiNum = -1, frequency = -1;

		// decompose the code to char array
		char[] cArray = code.toCharArray();
		
		//initially set the currentNode to overallRoot
		HuffmanNode currentNode = overallRoot;

		// loop each char in the array
		for(int i = 0; i < cArray.length; i++) {
			
			// set the ascii number for the leaf node
			if(i == cArray.length - 1) {
				asciiNum = ascii;
			}
			
			if(cArray[i] == '0') {
				// if null, create left node
				if(currentNode.left == null) {
					currentNode.left = new HuffmanNode(asciiNum, frequency, null, null);
				}
				// update current node
				currentNode = currentNode.left;
				
			} else {
				// if null, create right node
				if(currentNode.right == null) {
					currentNode.right = new HuffmanNode(asciiNum, frequency, null, null);
				}
				// update current node
				currentNode = currentNode.right;
			}
		}
	}
	
	
	// Reads bits from the given input stream and writes the corresponding characters to the output.
	// Stops reading when it encounters a character with value equal to eof.  
	// This is a pseudo-eof character, so it should not be written to the output file. 
	// Assumes the input stream contains a legal encoding of characters for this tree’s Huffman code.
	public void decode(BitInputStream input, PrintStream output, int eof) {
		
		HuffmanNode currentNode = overallRoot;

		// loop until hit the pseudo-eof character 
		while(true) {
			// if it hit a leaf node (end of an encoded sequence)
			if(currentNode.left == null && currentNode.right == null) {
				
				// if its ascii number is pseudo-eof
				if(currentNode.ascii == eof) {
					// break the loop
					break;
				}else {
					//
					output.write(currentNode.ascii);
					// start from the root node
					currentNode = overallRoot; 
				}
			}else {
				// read bit
				int n = input.readBit();
				if(n==0) {
					// move to left node
					currentNode = currentNode.left;
				}else {
					// move to right node
					currentNode = currentNode.right;
				}
			}
		}
	}


	// inner class to describe a HuffmanNode
	private class HuffmanNode implements Comparable<HuffmanNode>{
		public int frequency;
		public int ascii;
		public HuffmanNode left, right;
		
		public HuffmanNode(int ascii, int frequency, HuffmanNode left, HuffmanNode right) {
			this.ascii = ascii;
			this.frequency = frequency;
			this.left = left;
			this.right = right;
		}
		
		public int compareTo(HuffmanNode n) {
			// return < 0 if the frequency of this node < frequency of n
			if(this.frequency < n.frequency) {
				return -1;
			}
			// return > 0 if the frequency of this node > frequency of n
			else if(this.frequency > n.frequency) {
				return 1;
			}
			// return 0 if the frequency of this node == frequency of n
			else {
				return 0;
			}
		}

	} 
}

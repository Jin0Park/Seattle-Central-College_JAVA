import java.util.PriorityQueue;

import java.io.PrintStream;

public class HuffmanTree2 {
	private HuffmanNode overallRoot;
	
	
	HuffmanTree2(int[] count){
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
	
	// Constructs a Huffman tree from the given input stream.  
	// Assumes that the standard bit representation has been used for the tree.
	HuffmanTree2(BitInputStream input) {
		overallRoot = new HuffmanNode(-1, -1, null, null);
		createTree(overallRoot, input);
	}
	
	// create Huffman tree
	public void createTree(HuffmanNode node, BitInputStream input) {

		// read bit
		int n = input.readBit();

		// base case
		if(n == 1) {
			// if the bit is 1, the node is leaf
			// use the read9 method to create leaf with the character
			int ascii = read9(input);
			node.ascii = ascii; 

		} else if(n == 0) {
			// set as a branch node 
			node.left = new HuffmanNode(-1, -1, null, null);
			node.right = new HuffmanNode(-1, -1, null, null);
			
			// move to children nodes
			createTree(node.left, input);
			createTree(node.right, input);
			
		}
	}
	
	// Assigns codes for each character of the tree.  
	// Assumes the array has null values before the method is called.  
	// Fills in a String for each character in the tree indicating its code.
	protected void assign(String[] code) {
		if(overallRoot != null) {
			getCode(overallRoot, "", code);
		}
	}
	
	
	// helper function for assign method
	public void getCode(HuffmanNode node, String code, String[] codes) {
		if(node.left == null && node.right == null) {
			// if leaf node, use ASCII value as an index for the code
			codes[node.ascii] = code;
		} else {
			// if branch node, add 0 to the code for left 
			// and 1 to the code for right
			getCode(node.left, code + "0", codes);
			getCode(node.right, code + "1", codes);
		}
	}
	
	// Writes the current tree to the output stream using the standard bit representation.
	protected void writeHeader(BitOutputStream output) {
		if(overallRoot != null) {
			preorderTraversal(overallRoot, output);
		}
	}
	
	// helper function for writeHeader method
	public void preorderTraversal(HuffmanNode node, BitOutputStream output) {
		if(node.left == null && node.right == null) {
			// if leaf node, print 1
			output.writeBit(1);
			// then write the ASCII value of the character stored at this leaf
			write9(output, node.ascii);
		} else {
			// if branch node, print 0
			output.writeBit(0);
			preorderTraversal(node.left, output);
			preorderTraversal(node.right, output);
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

	
	// pre : 0 <= n < 512
	// post: writes a 9-bit representation of n to the given output stream
	private void write9(BitOutputStream output, int n) {
	    for (int i = 0; i < 9; i++) {
	        output.writeBit(n % 2);
	        n /= 2;
	    }
	}


	// pre : an integer n has been encoded using write9 or its equivalent
	// post: reads 9 bits to reconstruct the original integer
	private int read9(BitInputStream input) {
	    int multiplier = 1;
	    int sum = 0;
	    for (int i = 0; i < 9; i++) {
	        sum += multiplier * input.readBit();
	        multiplier *= 2;
	    }
	    return sum;
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

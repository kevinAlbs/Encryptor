package encryption;
import java.util.LinkedList;
import java.util.Arrays;

public class HuffmanEncoder{
	HuffmanTree tree = null;

	/* Static instatiation */
	private HuffmanEncoder(){}

	/* Generates new Huffman tree from input */
	public static HuffmanEncoder fromInput(String input){
		HuffmanEncoder h = new HuffmanEncoder();
		h.buildTree(input);
		return h;
	}
	/* 
		Builds a new huffman encoder from tree signature.
		Tree signature is of format:
		<pre-order><in-order>END
		Where pre and in order traversals use SEPARATOR
	*/
	public static HuffmanEncoder fromSignature(String tree_signature){
		HuffmanEncoder h = new HuffmanEncoder();
		h.loadTree(tree_signature);
		return h;
	}

	private HuffmanTree getMin(LinkedList<HuffmanTree> q1, LinkedList<HuffmanTree> q2){
		if(q1.isEmpty()){
			return q2.pollFirst();
		}
		else if(q2.isEmpty()){
			return q1.pollFirst();
		}
		else{
			//get least two
			int f1 = q1.peekFirst().getRootData().n;
			int f2 = q2.peekFirst().getRootData().n;
			if(f1 < f2){
				return q1.pollFirst();
			}
			else{
				return q2.pollFirst();
			}
		}
	}

	/* Builds a new huffman encoder from input */
	public void buildTree(String input){
		int[] freqs = new int[256];//hash map for frequencies
		int totalMyLetters = 0;//counts how many distinct MyLetters

		for(int i = 0; i < input.length(); i++){
			int char_num = (int)input.charAt(i);
			if(freqs[char_num] == 0){
				//new Letter
				totalMyLetters++;
			}
			freqs[char_num]++;
		}

		//build an array of the used MyLetters and sort it ascending
		Letter[] list = new Letter[totalMyLetters];
		int listIndex = 0;
		for(int i = 0; i < freqs.length; i++){
			if(freqs[i] != 0){
				list[listIndex] = new Letter(i, freqs[i]);
				listIndex++;
			}
		}
		Arrays.sort(list, new LetterComparator());
		LinkedList<HuffmanTree> q1 = new LinkedList<HuffmanTree>();
		LinkedList<HuffmanTree> q2 = new LinkedList<HuffmanTree>();
		HuffmanTree output;
		for(int i = 0; i < list.length; i++){
			//System.out.println(list[i].c + "," + list[i].n);
			HuffmanTree newTree = new HuffmanTree();
			newTree.insert(list[i]);
			q1.addLast(newTree);
		}

		while(true){
			//get first item
			HuffmanTree h1,h2;
			h1 = this.getMin(q1,q2);
			h2 = this.getMin(q1,q2);
			//h1 should never be null since at the beginning of the loop, there should at least be one item in one queue
			if(h2 == null){
				output = h1;
				break;
			}
			//make the new tree
			Letter newLetter = new Letter();//interior node
			newLetter.n = h1.getRootData().n + h2.getRootData().n;
			h1.addTree(h2, newLetter);
			if(q1.isEmpty() && q2.isEmpty()){
				output = h1;
				break;
			}
			else{
				q2.addLast(h1);
			}
		}
		tree = output;
	}

	public void loadTree(String tree_signature){
		tree = HuffmanTree.buildTree(tree_signature);
	}

	/*
	 * @param String s - a string of 0's and 1's
	 * @return String a string of characters formed by treating each 8 characters as bytes
	 */
	public static String bitStringToCharacters(String s){
		String output = "";
		int numBytes = (int)Math.ceil(s.length()/8.0);
		//how do I deal with the last bits?
		int byteIndex = s.length();
		while(s.length() > 0){
			String bit_string = s.substring(0, Math.min(s.length(), 8));
			if(s.length() < 8){
				s = "";
				//pad the end with zeros
				String zeros = "00000000";
				bit_string += zeros.substring(8 - bit_string.length());
			} else {
				s = s.substring(8); //chop off first 8
			}
			char c = (char)Integer.parseInt(bit_string, 2); //convert string to byte, then covert to char
			output += c;
		}
		return output;
	}

	public static String charactersToBitString(String s){
		String bit_string = "";
		for(int i = 0; i < s.length(); i++){
			String byte_string = "";
			char c = s.charAt(i);
			int b = (int)c;
			for(int j = 0; j < 8; j++){
				int result = b & 1;
				if(result == 1){
					byte_string = "1" + byte_string;
				} else {
					byte_string = "0" + byte_string;
				}
				b = b >> 1;
			}
			bit_string += byte_string;
		}
		return bit_string;
	}

	/* Decodes a string.
	 * @param s String to decode. Should be a string of 1 and 0 characters. "100100101110" etc.
	 * @param num_bits Number of bit characters to decode
	 */
	public String decode(String s, int num_bits){
		if(tree == null){
			throw new IllegalStateException("Tree has not been built");
		}
		return tree.decode(s.substring(0, num_bits));
	}

	/* Takes any string, returns bit string compressed to characters */
	public String encode(String s){
		if(tree == null){
			throw new IllegalStateException("Tree has not been built");
		}
		return tree.encode(s);
	}
	/*
	Returns preorder+inorder tree signature
	*/
	public String export(){
		if(tree == null){
			throw new IllegalStateException("Tree has not been built");
		}
		String treeString = tree.toString();
		return treeString;
	}
}
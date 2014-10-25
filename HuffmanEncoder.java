
import java.util.Scanner;
import java.io.*;

public class HuffmanEncoder{
	MyHuffTree tree = null;

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

	private void  quicksort(MyLetter[] list){
		quicksort(list, 0, list.length - 1);
	}
	/* n * log(n) expected */
	private void quicksort(MyLetter[] list, int bot, int top){
		if(bot >= top){
			//base case
			return;
		}

		//choose first as pivot
		int lo = bot + 1;
		int hi = top;
		int pivot = list[bot].n;

		while(lo <= hi){
			while(lo <= hi && list[lo].n < pivot){
				lo++;
			}
			while(hi >= lo && list[hi].n >= pivot){
				hi--;
			}
			//at this point they either crossed or require a swap
			if(lo < hi){
				//swap
				MyLetter tmp = list[hi];
				list[hi] = list[lo];
				list[lo] = tmp;
				lo++;
				hi--;
			}
		}
		//swap pivot with hi
		MyLetter tmp = list[hi];
		list[hi] = list[bot];
		list[bot] = tmp;

		quicksort(list, bot, hi-1);
		quicksort(list, hi+1, top);

	}
	private MyHuffTree getMin(Queue<MyHuffTree> q1, Queue<MyHuffTree> q2){
		if(q1.isEmpty()){
			return q2.dequeue();
		}
		else if(q2.isEmpty()){
			return q1.dequeue();
		}
		else{
			//get least two
			int f1 = q1.peek().getRootData().n;
			int f2 = q2.peek().getRootData().n;
			if(f1 < f2){
				return q1.dequeue();
			}
			else{
				return q2.dequeue();
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
				//new MyLetter
				totalMyLetters++;
			}
			freqs[char_num]++;
		}
		
		String toEncode = input;
		//build an array of the used MyLetters and sort it ascending
		MyLetter[] list = new MyLetter[totalMyLetters];
		int listIndex = 0;
		for(int i = 0; i < freqs.length; i++){
			if(freqs[i] != 0){
				list[listIndex] = new MyLetter(i, freqs[i]);
				listIndex++;
			}
		}
		quicksort(list);
		Queue<MyHuffTree> q1 = new Queue<MyHuffTree>();
		Queue<MyHuffTree> q2 = new Queue<MyHuffTree>();
		MyHuffTree output;
		for(int i = 0; i < list.length; i++){
			//System.out.println(list[i].c + "," + list[i].n);
			MyHuffTree newTree = new MyHuffTree();
			newTree.insert(list[i]);
			q1.enqueue(newTree);
		}

		while(true){
			//get first item
			MyHuffTree h1,h2;
			h1 = this.getMin(q1,q2);
			h2 = this.getMin(q1,q2);
			//h1 should never be null since at the beginning of the loop, there should at least be one item in one queue
			if(h2 == null){
				output = h1;
				break;
			}
			//make the new tree
			MyLetter newLetter = new MyLetter();//interior node
			newLetter.n = h1.getRootData().n + h2.getRootData().n;
			h1.addTree(h2, newLetter);
			if(q1.isEmpty() && q2.isEmpty()){
				output = h1;
				break;
			}
			else{
				q2.enqueue(h1);
			}
		}
		tree = output;
	}

	public void loadTree(String tree_signature){
		tree = MyHuffTree.buildTree(tree_signature);
	}

	/*
	 * @param String s - a string of 0's and 1's
	 */
	private String bitStringToCharacters(String s){
		/* TODO finish implementing
		 * The extra padding on the end can be troublesome
		 * since reading the extra characters is ambiguous.
		 * Best option might be to encode the string length
		 */
		int numBytes = (int)Math.ceil(s.length()/8);
		Byte[] data = new Byte[numBytes];
		//how do I deal with the last bits?
		int byteIndex = s.length();
		for(int i = 0; i < numBytes; i++){
			String bit_string = "";
			if(s.length() < 8){
				//pad the beginning with zeros
			}
			//Byte.parseByte(byteStr, 2); - use to convert string to byte, then covert to char
		}
		return "";
	}

	public String decode(String s){
		if(tree == null){
			throw new IllegalStateException("Tree has not been built");
		}
		return tree.decode(s);
	}

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
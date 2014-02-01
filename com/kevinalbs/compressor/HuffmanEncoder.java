package com.kevinalbs.compressor;
import java.util.Scanner;
import java.io.*;

public class HuffmanEncoder{
	String toEncode = "";
	MyHuffTree tree = null;
	//private MyHuffTree<MyLetter> output;
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

	public HuffmanEncoder(File f){
		int[] freqs = new int[256];//hash map for frequencies
		int totalMyLetters = 0;//counts how many distinct MyLetters
		String input = "";
		try{
			FileInputStream in = new FileInputStream(f);
			int d = in.read();
			while(d != -1){
				if(freqs[d] == 0){
					//new MyLetter
					totalMyLetters++;
				}
				freqs[d]++;
				input += (char)d;
				d = in.read();
			}
			in.close();
		}
		catch(IOException ioe){}
		toEncode = input;
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
		//output.encode(input);
		System.out.println(output);
		System.out.println("================================================");
		String str = output.toString();
		int p1 = str.length()/2;
		String preorder = str.substring(0,p1);
		String inorder = str.substring(p1);
		String[] pre = preorder.split("\\|"); //it's a regex... derp
		String[] in = inorder.split("\\|");
		MyHuffTree newTree = MyHuffTree.buildTree(pre,in);
		System.out.println(newTree);
		//put each element of the array into a tree and add to the first queue

		//use two queues to build the final huffman encoded tree
	}

	//returns the flattened tree preceded by the number of characters as an integer (so we know how much to read)
	public Byte[] export(){
		if(tree == null){
			System.err.println("The tree has not been built");
			return null;	
		}
		else{
			//get encoding for each character in input and (each character in the tree which will not be compressed)
			String bitString = tree.encode(toEncode);
			String treeString = tree.toString();
			int numBytes = (int)Math.ceil(bitString.length()/8) + treeString.length() + 1;//TODO: why is +1 necessary?
			System.out.println("NumBytes " + numBytes);
			Byte[] data = new Byte[numBytes];
			//first put in the tree
			for(int i = 0; i < treeString.length(); i++){
				data[i] = (byte)treeString.charAt(i);
			}
			//how do I deal with the last bits?
			int byteIndex = treeString.length();
			for(int i = 0; i < bitString.length(); i+=8){
				String byteStr = "";
				if(i + 8 > bitString.length()){
					byteStr = bitString.substring(i);//get last bits (not 8 though)
					//append zeros
					while(byteStr.length() < 8){
						byteStr = byteStr + "0";
					}
				}
				else{
					byteStr = bitString.substring(i, i+8);
				}
				//parse byte does not work with 2's compliment, it expects the leading digit to be a - if negative
				if(byteStr.charAt(0) == '1'){
					byteStr = "-" + byteStr.substring(1);
				}
				data[byteIndex] = Byte.parseByte(byteStr, 2);
				byteIndex++;
			}
			return data;
		}
		
	}
}
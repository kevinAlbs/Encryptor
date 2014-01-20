package com.kevinalbs.compressor;
public class MyHuffTree{
	private class Node{
		public MyLetter data;
		public Node left, right;
		public Node(MyLetter data){
			this.data = data;
		}
	}
	private boolean mapSet = false;
	private float avgLength = -1;
	private String[] map = new String[256]; //maps char to bit string
	private Node root; 
	private String flattened;
	/*
	builds a tree from a flattened string
	since static, use a different generic variable
	*/
	public static MyHuffTree buildTree(String s){
		return null;
	}

	public void addTree(MyHuffTree otherTree, MyLetter topNodeData){
		//make new node with added frequencies
		Node topNode = new Node (topNodeData);
		topNode.left = this.root;
		topNode.right = otherTree.root;
		this.root = topNode;
	}

	public void insert(MyLetter data){
		Node newNode = new Node(data);
		if(root == null){
			root = newNode;
			return;
		}
		Node prev = null, ptr = root;
		int c = 0;
		while(ptr != null){
			prev = ptr;
			c = data.compareTo(ptr.data);
			if(c < 0){
				ptr = ptr.left;
			}
			else{
				ptr = ptr.right;
			}
		}
		if(c < 0){
			prev.left = newNode;
		}
		else{
			prev.right = newNode;
		}
	}

	public void inOrder(){
		flattened = "";
		pInOrder(root);
	}
	private void pInOrder(Node n){
		if(n == null){
			return;
		}
		else{
			pInOrder(n.left);
			flattened += n.data.toString() + " ";
			pInOrder(n.right);
		}
	}
	public MyLetter getRootData(){
		return root.data;
	}

	//returns the average length
	public void getCodes(int totalLength){
		if(mapSet){
			return;
		}
		mapSet = true;
		getCodes(root, "", totalLength);
	}
	private void getCodes(Node n, String str, int totalLength){
		if(n == null){
			return;
		}
		getCodes(n.left, str + "0", totalLength);
		if(n.left == null && n.right == null){
			//leaf node
			map[(int)(n.data.c)] = str;
			avgLength += str.length() * ((float)n.data.n) / totalLength;
		}
		getCodes(n.right, str + "1", totalLength);
	}

	public String encode(String input){
		//first get the map of the letters
		getCodes(input.length());
		System.out.println("Average length is : " + avgLength);
		for(int i = 0; i < input.length(); i++){
			System.out.println(input.charAt(i) + " = " + map[(int)input.charAt(i)]);
		}
		return "";
	}
	//returns flattened tree
	public String toString(){
		inOrder();
		return flattened;
	}
}
package com.kevinalbs.compressor;
public class MyHuffTree <T extends Comparable<T>>{
	private class Node<T>{
		public T data;
		public Node<T> left, right;
		public Node(T data){
			this.data = data;
		}
	}
	private Node<T> root; 
	private String flattened;
	/*
	builds a tree from a flattened string
	since static, use a different generic variable
	*/
	public static <S extends Comparable<S>> MyHuffTree<S> buildTree(String s){
		return null;
	}

	public void addTree(MyHuffTree<T> otherTree, T topNodeData){
		//make new node with added frequencies
		Node<T> topNode = new Node<T>(topNodeData);
		topNode.left = this.root;
		topNode.right = otherTree.root;
		this.root = topNode;
	}

	public void insert(T data){
		Node<T> newNode = new Node<T>(data);
		if(root == null){
			root = newNode;
			return;
		}
		Node<T> prev = null, ptr = root;
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
			flattened += n.data.toString();
			pInOrder(n.right);
		}
	}
	public T getRootData(){
		return root.data;
	}
	//returns flattened tree
	public String toString(){
		inOrder();
		return flattened;
	}
}
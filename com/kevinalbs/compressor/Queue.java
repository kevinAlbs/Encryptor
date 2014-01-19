package com.kevinalbs.compressor;
public class Queue<T>{
	private class Node<T>{
		public T data;
		public Node<T> next;
		public Node(T data, Node<T> next){
			this.data = data;
			this.next = next;
		}
	}
	private Node<T> rear;
	private int numItems = 0;
	public Queue(){

	}
	public void enqueue(T data){
		Node<T> newNode = new Node<T>(data, null);
		if(rear == null){
			newNode.next = newNode;
		}
		else{
			newNode.next = rear.next;
			rear.next = newNode;
		}
		rear = newNode;
		numItems++;
	}
	public T dequeue(){
		T toReturn = null;
		if(rear == null){
			return null;
		}
		if(rear.next == rear){
			//single element
			toReturn = rear.data;
			rear = null;
		}
		else{
			toReturn = rear.next.data;
			rear.next = rear.next.next;
		}
		numItems--;
		return toReturn;
	}
	public T peek(){
		if(rear == null){
			return null;
		}
		else{
			return rear.next.data;
		}
	}
	public boolean isEmpty(){
		return numItems == 0;
	}
	public String toString(){
		if(rear == null){
			return "";
		}
		Node<T> ptr = rear.next;
		String toReturn = "";
		do{
			toReturn += ptr.data.toString() + " ";
			ptr = ptr.next;
		}while(ptr != rear.next);
		return toReturn;
	}
}
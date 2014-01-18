public class Queue<T extends Comparable<T>>{
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
}
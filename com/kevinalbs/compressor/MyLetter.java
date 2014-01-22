package com.kevinalbs.compressor;
public class MyLetter implements Comparable<MyLetter>{
	public int n; //num of occurrences
	public int id = -1;
	private static int UNIQUE_ID = -1;
	public boolean isMyLetter = true; //false if this is a non-leaf node in the tree
	public MyLetter(int id, int n){
		this.id = id;
		this.n = n;
	}
	public MyLetter(){
		//for inner uninitialized nodes needing a unique id
		this.id = UNIQUE_ID;
		UNIQUE_ID--;
		this.n = 0;
	}
	public int compareTo(MyLetter o){
		//MyLetter l = (MyLetter)o;
		return this.n - o.n;
	}
	public String toString(){
		if(!isMyLetter){
			//doesn't really matter what character I use I guess, just has to be unique
			return this.id + "|";
		}
		else{
			return this.id + "|";
		}
	}
}
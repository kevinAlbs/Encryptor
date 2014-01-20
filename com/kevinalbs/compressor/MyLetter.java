package com.kevinalbs.compressor;
public class MyLetter implements Comparable<MyLetter>{
	public int n; //num of occurrences
	public char c;
	public boolean isMyLetter = true; //false if this is a non-leaf node in the tree
	public MyLetter(char c, int n){
		this.c = c;
		this.n = n;
	}
	public MyLetter(boolean isMyLetter){
		this.isMyLetter = isMyLetter;
	}
	public int compareTo(MyLetter o){
		//MyLetter l = (MyLetter)o;
		return this.n - o.n;
	}
	public String toString(){
		if(!isMyLetter){
			//doesn't really matter what character I use I guess
			return "0";
		}
		else{
			return this.c + "";
		}
	}
}
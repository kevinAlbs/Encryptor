package com.kevinalbs.compressor;
public class MyLetter implements Comparable{
	public int n; //num of occurrences
	public char c;
	public boolean isMyLetter = true; //false if this is a non-leaf node in the tree
	public MyLetter(char c, int n){
		this.c = c;
		this.n = n;
	}
	public int compareTo(Object o){
		MyLetter l = (MyLetter)o;
		return this.n - l.n;
	}
}
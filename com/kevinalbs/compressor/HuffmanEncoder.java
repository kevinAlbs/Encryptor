package com.kevinalbs.compressor;
import java.util.Scanner;
import java.io.*;

public class HuffmanEncoder{
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

	public HuffmanEncoder(File f){
		int[] freqs = new int[256];//hash map for frequencies
		int totalMyLetters = 0;//counts how many distinct MyLetters
		try{
			FileInputStream in = new FileInputStream(f);
			int d = in.read();
			while(d != -1){
				if(freqs[d] == 0){
					//new MyLetter
					totalMyLetters++;
				}
				freqs[d]++;
				d = in.read();
			}
		}
		catch(IOException ioe){}
		//build an array of the used MyLetters and sort it ascending
		MyLetter[] list = new MyLetter[totalMyLetters];
		int listIndex = 0;
		for(int i = 0; i < freqs.length; i++){
			if(freqs[i] != 0){
				list[listIndex] = new MyLetter((char)i, freqs[i]);
				listIndex++;
			}
		}
		quicksort(list);
		for(int i = 0; i < list.length; i++){
			System.out.println(list[i].c + "," + list[i].n);
		}
		//put this array into a queue

		//use two queues to build the final huffman encoded tree
	}

	//returns the flattened tree preceded by the number of characters as an integer (so we know how much to read)
	public String export(){
		return "";
	}
}
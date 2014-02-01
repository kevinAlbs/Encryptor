package com.kevinalbs.compressor;
import java.util.Scanner;
import java.io.File;
/* this is the runner, it manages file input/output, and passes off data to huffman encoder and encryptor */
public class Compressor{

	public static void err(String msg){
		System.err.println(msg);
		System.out.println("Usage: java com.kevinalbs.Compressor <input file>");
	}	
	/*
	first arg will be file to compress
	*/
	public static void main(String[] args){
		if(args.length != 1){
			Compressor.err("Incorrect number of arguments");
			return;
		}
		File f = new File(args[0]);
		if(!f.exists() || !f.canRead()){
			Compressor.err("File " + args[0] + " does not exist or is not readable");
			return;
		}
		HuffmanEncoder h = new HuffmanEncoder(f);
		Byte[] data = h.export();
		for(int i = 0; i < data.length; i++){
			System.out.print((char)data[i].intValue());
		}
		
	}
}
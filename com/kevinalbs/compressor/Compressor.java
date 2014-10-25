package com.kevinalbs.compressor;
import java.util.Scanner;
import java.io.*;
/* CLI runner
 * manages file input/output, and passes off data to huffman encoder and encryptor 
 */
public class Compressor{

	public static void err(String msg){
		System.err.println(msg);
		System.out.println("Usage: java com.kevinalbs.Compressor <input file>");
	}

	private static String readFile(String filename){
		return readFile(new File(filename));
	}
	public static String readFile(File f){
		String file_contents = "";
		try{
			FileInputStream in = new FileInputStream(f);
			int d = in.read();
			while(d != -1){
				file_contents += (char)d;
				d = in.read();
			}
			in.close();
		}
		catch(IOException ioe){
			System.out.println("Cannot read file" + ioe.getMessage());
		}
		return file_contents;
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
		String file_contents = readFile(f);

		HuffmanEncoder h = HuffmanEncoder.fromInput(file_contents);
	}
}
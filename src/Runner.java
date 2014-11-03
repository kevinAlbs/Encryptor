import java.util.Scanner;
import java.io.*;

/* CLI runner
 * manages file input/output, and passes off data to huffman encoder and encryptor 
 */
public class Runner{
	private static class ArgParser{
		public static String from_file="",
							to_file="",
							key="default",
							action="encrypt";

		public static boolean parse(String[] args){
			try {
				for(int i = 0; i < args.length; i++){
					if(args[i].equals("--from-file")){
						from_file = args[i+1];
						i++;
					} else if(args[i].equals("--to-file")){
						to_file = args[i+1];
						i++;
					} else if(args[i].equals("--action")){
						action = args[i+1];
						i++;
					} else if(args[i].equals("--key")){
						key = args[i+1];
						i++;
					}
				}
			} catch(IndexOutOfBoundsException ae){
				return false;
			}

			if(from_file.equals("")){
				return false;
			}
			return true;
		}
	}

	public static void err(String msg){
		System.err.println(msg);
		System.out.println("Usage: java Runner --from-file <input file> --to-file <output file> --action <encrypt|decrypt> --key <password>");
	}

	/*
	first arg will be file to compress
	*/
	public static void main(String[] args){
		if(!ArgParser.parse(args)){
			Runner.err("Bad arguments");
			return;
		}

		Encryptor e = new NaiveEncryptor(); //change this to use a custom encryptor


		if(ArgParser.action.equals("encrypt")){
			// read, compress, and encrypt
			String file_contents = Utilities.readFile(ArgParser.from_file);
			HuffmanEncoder h = HuffmanEncoder.fromInput(file_contents);
			String encoded = h.encode(file_contents);
			String compressed = HuffmanEncoder.bitStringToCharacters(encoded);
			String encrypted = e.encrypt(ArgParser.key, compressed);
			
			if(ArgParser.to_file.equals("")){
				ArgParser.to_file = "encrypted.dat";
			}
			
			//write number of bits, tree signature, and encrypted data
			String file_output = encoded.length() + "\n" + h.export() + "\n" + encrypted;
			Utilities.writeFile(file_output, ArgParser.to_file);

			System.out.println("New file size is " + file_output.length() + " characters");
			if (file_output.length() < file_contents.length()){
				System.out.format("File size reduced by %4.2f%%\n", (file_output.length() * 1.0 / file_contents.length()) * 100);
			}
			System.out.println("Written to " + ArgParser.to_file);
			
		} else if(ArgParser.action.equals("decrypt")){
			String file_contents = Utilities.readFile(ArgParser.from_file);
			//first line is number of bits
			int nl_index = file_contents.indexOf("\n");
			if(nl_index == -1){
				throw new IllegalArgumentException("Bad input file format");
			}
			int num_bits = Integer.parseInt(file_contents.substring(0, nl_index));
			file_contents = file_contents.substring(nl_index+1);

			//second line is tree signature
			nl_index = file_contents.indexOf("\n");
			if(nl_index == -1){
				throw new IllegalArgumentException("Bad input file format");
			}
			String tree_signature = file_contents.substring(0, nl_index);
			HuffmanEncoder h = HuffmanEncoder.fromSignature(tree_signature);

			String encrypted = file_contents.substring(nl_index+1);
			String compressed = e.decrypt(ArgParser.key, encrypted);
			String encoded = HuffmanEncoder.charactersToBitString(compressed);
			String decoded = h.decode(encoded, num_bits);

			if(ArgParser.to_file.equals("")){
				//print out
				System.out.println(decoded);
			} else {
				Utilities.writeFile(decoded, ArgParser.to_file);
				System.out.println("Written to " + ArgParser.to_file);
			}
		}
	}
}
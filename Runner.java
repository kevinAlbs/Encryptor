
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
				System.out.println("Missing");
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
		System.out.println("Usage: Runner --from-file <input file> --to-file <output file>");
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

	public static void writeFile(String contents, File f){
		try{
			FileWriter out = new FileWriter(f);
			out.write(contents);
			out.flush();
			out.close();
		} catch(IOException ioe){
			System.out.println("Cannot write file " + ioe.getMessage());
		}
	}

	/*
	first arg will be file to compress
	*/
	public static void main(String[] args){
		if(!ArgParser.parse(args)){
			Runner.err("Bad arguments");
			return;
		}

		File f = new File(ArgParser.from_file);
		if(!f.exists() || !f.canRead()){
			Runner.err("File " + ArgParser.from_file + " does not exist or is not readable");
			return;
		}

		if(ArgParser.action.equals("encrypt")){
			String file_contents = readFile(f);
			HuffmanEncoder h = HuffmanEncoder.fromInput(file_contents);
			String encoded = h.encode(file_contents);
			String encrypted = Encryptor.encrypt(ArgParser.key, encoded);
			if(ArgParser.to_file.equals("")){
				ArgParser.to_file = "encrypted.dat";
			}
			File out = new File(ArgParser.to_file);
			writeFile(h.export() + "\n" + encrypted, out);
			System.out.println("Written to " + ArgParser.to_file);
		} else if(ArgParser.action.equals("decrypt")){
			String file_contents = readFile(f);
			//first line is tree
			int nl_index = file_contents.indexOf("\n");
			if(nl_index == -1){
				Runner.err("Bad file format");
				return;
			}
			String tree_signature = file_contents.substring(0, nl_index);
			String encrypted = file_contents.substring(nl_index+1);

			String encoded = Encryptor.decrypt(ArgParser.key, encrypted);

			HuffmanEncoder h = HuffmanEncoder.fromSignature(tree_signature);
			String decoded = h.decode(encoded);

			if(ArgParser.to_file.equals("")){
				//print out
				System.out.println(decoded);
			} else {
				File out = new File(ArgParser.to_file);
				writeFile(decoded, out);
				System.out.println("Written to " + ArgParser.to_file);
			}
		}
		
	}
}
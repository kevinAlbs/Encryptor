import encryption.*;

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
			
			if(ArgParser.to_file.equals("")){
				ArgParser.to_file = "encrypted.dat";
			}
			
			//write number of bits, tree signature, and encrypted data
			String file_output = FileEncryptor.write(file_contents, ArgParser.to_file, e, ArgParser.key);

			System.out.println("New file size is " + file_output.length() + " characters");
			if (file_output.length() < file_contents.length()){
				System.out.format("File size reduced by %4.2f%%\n", (file_output.length() * 1.0 / file_contents.length()) * 100);
			}
			System.out.println("Written to " + ArgParser.to_file);
			
		} else if(ArgParser.action.equals("decrypt")){
			String decoded = FileEncryptor.read(ArgParser.from_file, e, ArgParser.key);

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
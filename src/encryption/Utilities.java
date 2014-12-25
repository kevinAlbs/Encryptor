package encryption;

import java.io.*;

public class Utilities{
    public static String readFile(String filename){
        File f = new File(filename);
        if(!f.exists() || !f.canRead()){
            throw new IllegalArgumentException("File " + filename + " does not exist or is not readable");
        }
        return readFile(f);
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

    public static void writeFile(String contents, String filename){
        File f = new File(filename);
        writeFile(contents, f);
    }

    public static void writeFile(String contents, File f){
        try{
            FileOutputStream out = new FileOutputStream(f);
            for(int i = 0; i < contents.length(); i++){
                char c = contents.charAt(i);
                out.write((int)c);
            }
            out.close();
        } catch(IOException ioe){
            System.out.println("Cannot write file " + ioe.getMessage());
        }
    }
}
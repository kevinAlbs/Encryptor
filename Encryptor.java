
/*
At the moment, this is a toy encryptor, not secure in the least.
TODO: generalize this to have an ecryptor interface to make it easy
to swap in a different encryptor function
*/
public class Encryptor{
    private static String lengthen(String from, int to){
        String output = "";
        while(output.length() < to){
            output += from;
        }
        return output.substring(0, to);
    }
    public static String encrypt(String key, String s){
        if(key.length() == 0){
            throw new IllegalArgumentException("Key must not be empty");
        }
        //lengthen key to be same size as s
        String fullkey = lengthen(key, s.length());
        String output = "";
        //perform xor
        for(int i = 0; i < s.length(); i++){
            output += (char)((byte)fullkey.charAt(i) ^ (byte)s.charAt(i));
        }
        return output;
    }
    public static String decrypt(String key, String s){
        return encrypt(key, s);
    }
}
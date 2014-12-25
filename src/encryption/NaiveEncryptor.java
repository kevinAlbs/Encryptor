package encryption;

/*
At the moment, this is a toy encryptor, not secure in the least.
to swap in a different encryptor function
*/
public class NaiveEncryptor implements Encryptor{

    private static String lengthen(String from, int to){
        String output = "";
        while(output.length() < to){
            output += from;
        }
        return output.substring(0, to);
    }

    public String encrypt(String key, String input){
        if(key.length() == 0){
            throw new IllegalArgumentException("Key must not be empty");
        }
        //lengthen key to be same size as s
        String fullkey = lengthen(key, input.length());
        String output = "";
        //perform xor
        for(int i = 0; i < input.length(); i++){
            output += (char)((byte)fullkey.charAt(i) ^ (byte)input.charAt(i));
        }
        return output;
    }

    public String decrypt(String key, String s){
        return encrypt(key, s);
    }
}
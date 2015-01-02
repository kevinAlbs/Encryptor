package encryption;

import java.io.File;

public class FileEncryptor {
    /* reads file and decrypts with key
     * if key is incorrect, you will get garbage back
     */
    public static String read(String filename, Encryptor e, String key) {
        String file_contents = Utilities.readFile(filename);
        return decrypt(file_contents, e, key);

    }
    public static String read(File f, Encryptor e, String key) {
        String file_contents = Utilities.readFile(f);
        return decrypt(file_contents, e, key);
    }

    private static String decrypt(String file_contents, Encryptor e, String key){
        int nl_index = file_contents.indexOf("\n");
        if (nl_index == -1) {
            throw new IllegalArgumentException("Bad input file format");
        }
        int num_bits = Integer.parseInt(file_contents.substring(0, nl_index));
        file_contents = file_contents.substring(nl_index + 1);

        //second line is tree signature
        nl_index = file_contents.indexOf("\n");
        if (nl_index == -1) {
            throw new IllegalArgumentException("Bad input file format");
        }

        String tree_signature = file_contents.substring(0, nl_index);
        HuffmanEncoder h = HuffmanEncoder.fromSignature(tree_signature);

        String encrypted = file_contents.substring(nl_index + 1);
        String compressed = e.decrypt(key, encrypted);
        String encoded = HuffmanEncoder.charactersToBitString(compressed);
        return h.decode(encoded, num_bits);
    }

    /* returns compressed+encrypted contents */
    public static String write(String contents, File file, Encryptor e, String key){
        HuffmanEncoder h = HuffmanEncoder.fromInput(contents);
        String encoded = h.encode(contents);
        String compressed = HuffmanEncoder.bitStringToCharacters(encoded);
        String encrypted = e.encrypt(key, compressed);
        Utilities.writeFile(encoded.length() + "\n" + h.export() + "\n" + encrypted, file);
        return encrypted;
    }

    public static String write(String contents, String filename, Encryptor e, String key){
        HuffmanEncoder h = HuffmanEncoder.fromInput(contents);
        String encoded = h.encode(contents);
        String compressed = HuffmanEncoder.bitStringToCharacters(encoded);
        String encrypted = e.encrypt(key, compressed);
        Utilities.writeFile(encoded.length() + "\n" + h.export() + "\n" + encrypted, filename);
        return encrypted;
    }

}

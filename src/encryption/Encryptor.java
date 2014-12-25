package encryption;

public interface Encryptor {
    public String encrypt(String key, String input);
    public String decrypt(String key, String input);
}
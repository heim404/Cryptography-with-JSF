import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.Cipher;

public class SymCrypt {
    //private static byte[] iv = { 0x0a, 0x01, 0x02, 0x03, 0x04, 0x0b, 0x0c, 0x0d };

    private static byte[] encrypt(byte[] inpBytes, SecretKey key, String algorithm) throws Exception {
        Cipher cipher = Cipher.getInstance(algorithm);
        cipher.init(Cipher.ENCRYPT_MODE, key);
        return cipher.doFinal(inpBytes);
    }

    private static byte[] decrypt(byte[] inpBytes, SecretKey key, String algorithm) throws Exception {
        Cipher cipher = Cipher.getInstance(algorithm);
        cipher.init(Cipher.DECRYPT_MODE, key);
        return cipher.doFinal(inpBytes);
    }

    public static void main(String[] args) throws Exception {
        // Definition of Algorithm, Mode, and Padding
        String algorithm = "DES/ECB/PKCS5Padding";

        // Generate Key
        KeyGenerator kg = KeyGenerator.getInstance("DES");
        kg.init(56); // 56 is the keysize. Fixed for DES
        SecretKey key = kg.generateKey();

        // Data to Encrypt
        String dataString = "Example of Symmetric Encryption in Java - Applied Cryptography April 2024";
        byte[] dataBytes = dataString.getBytes();
        System.out.println("Original Data:\n" + dataString);

        // Encrypt
        byte[] encBytes = encrypt(dataBytes, key, algorithm);
        System.out.println("\nEncrypted Data:\n" + new String(encBytes));
        System.out.println("Key:\n" + key.toString());

        // Save Encrypted Data to File
        saveToFile("encrypted_data.txt", encBytes);

        // Read Encrypted Data from File
        byte[] encBytesFromFile = readFromFile("encrypted_data.txt");

        // Decrypt and Display Result
        byte[] decBytes = decrypt(encBytesFromFile, key, algorithm);
        System.out.println("\nDecrypted Data:\n" + new String(decBytes));

        // Test Encryption/Decryption
        boolean expected = java.util.Arrays.equals(dataBytes, decBytes);
        System.out.println("Test " + (expected ? "Passed!" : "Failed!"));
    }

    private static void saveToFile(String path, byte[] data) throws IOException {
        FileOutputStream fop = new FileOutputStream(new File(path));
        fop.write(data);
        fop.close();
    }

    private static byte[] readFromFile(String path) throws IOException {
        File file = new File(path);
        byte[] retBytes = new byte[(int) file.length()];
        FileInputStream fis = new FileInputStream(file);
        fis.read(retBytes);
        fis.close();
        return retBytes;
    }
}

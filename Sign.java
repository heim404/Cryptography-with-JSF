import java.io.*;
import java.security.*;

/**
 * This class is used to generate a digital signature for a file.
 */
class Sign{

    /**
     * The main method for the Sign class
     *
     * @param args Takes one argument: the file to be signed.
     */
    public static void main(String[] args) {

        /* Generate a RSA signature */

        // Check if the correct number of arguments are provided
        if (args.length != 1) {
            System.out.println("Usage: Sign filename");
        }
        else try{

            /* Generate key pair */

            // Create a KeyPairGenerator object for RSA
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
            // Create a SecureRandom object for SHA1PRNG
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG");

            // Initialize the KeyPairGenerator object
            keyGen.initialize(1024, random);

            // Generate the key pair
            KeyPair pair = keyGen.generateKeyPair();
            // Get the private key from the key pair
            PrivateKey priv = pair.getPrivate();
            // Get the public key from the key pair
            PublicKey pub = pair.getPublic();


            // Create a Signature object and initialize it with the private key

            // Create a Signature object for SHA1 with RSA
            Signature rsa = Signature.getInstance("SHA1withRSA");
            // Initialize the Signature object for signing
            rsa.initSign(priv);

            // Read the original data and load it into the Signature object

            // Open the original data file
            FileInputStream fis = new FileInputStream(args[0]);
            BufferedInputStream bufin = new BufferedInputStream(fis);

            // Buffer for reading the data
            byte[] buffer = new byte[1024];
            int len;
            // Read the data and update the Signature object
            while (bufin.available() != 0) {
                len = bufin.read(buffer);
                rsa.sign(buffer, 0, len);
            };
            // Close the original data file
            bufin.close();

            // After reading all the data, generate the signature
            byte[] realSig = rsa.sign();


            // Save the signature to a file

            // Open the signature file
            FileOutputStream sigfos = new FileOutputStream("sig");
            // Write the signature to the file
            sigfos.write(realSig);

            // Close the signature file
            sigfos.close();


            // Save the public key to a file for later validation

            // Get the encoded public key
            byte[] key = pub.getEncoded();
            // Open the public key file
            FileOutputStream keyfos = new FileOutputStream("suepk");
            // Write the public key to the file
            keyfos.write(key);

            // Close the public key file
            keyfos.close();

            // Print the exported public key and the created signature
            System.out.println("Exported public key (suepk):\n" + new String(key) );
            System.out.println("\n\nCreated signature (sig):\n" + new String(realSig) );
            System.out.println("\n\nLocation of public key: " + new String(keyfos.toString()));


        } catch (Exception e) {
            // Print any exceptions that occur
            System.err.println("Exception: " + e.toString());
        }

    };

}
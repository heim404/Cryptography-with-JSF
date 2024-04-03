import java.io.*;
import java.security.*;
import java.security.spec.*;


class ValidateSign{

    public static void main(String[] args) {

        // Check if the correct number of arguments are provided
        if (args.length != 3) {
            System.out.println("Usage: Validate public_key signature original_data");
        }
        else try{

            // Import the public key for validation

            // Open the public key file
            FileInputStream keyfis = new FileInputStream(args[0]);
            // Read the public key bytes
            byte[] encKey = new byte[keyfis.available()];
            keyfis.read(encKey);

            // Close the public key file
            keyfis.close();

            // Create a public key specification from the encoded key
            X509EncodedKeySpec pubKeySpec = new X509EncodedKeySpec(encKey);

            // Generate the public key from the specification
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PublicKey pubKey = keyFactory.generatePublic(pubKeySpec);

            // Read the signature

            // Open the signature file
            FileInputStream sigfis = new FileInputStream(args[1]);
            // Read the signature bytes
            byte[] sigToVerify = new byte[sigfis.available()];
            sigfis.read(sigToVerify );

            // Close the signature file
            sigfis.close();

            // Create a Signature object and initialize it with the public key

            // Create a Signature object for SHA1 with RSA
            Signature sig = Signature.getInstance("SHA1withRSA");
            // Initialize the Signature object for verification
            sig.initVerify(pubKey);

            // Read the original data and load it into the Signature object

            // Open the original data file
            FileInputStream datafis = new FileInputStream(args[2]);
            BufferedInputStream bufin = new BufferedInputStream(datafis);

            // Buffer for reading the data
            byte[] buffer = new byte[1024];
            int len;
            // Read the data and update the Signature object
            while (bufin.available() != 0) {
                len = bufin.read(buffer);
                sig.update(buffer, 0, len);
            };

            // Close the original data file
            bufin.close();

            // Validate the signature

            // Verify the signature
            boolean verifies = sig.verify(sigToVerify);

            // Print the result of the verification
            System.out.println("Signature verified?: " + verifies);


        } catch (Exception e) {
            // Print any exceptions that occur
            System.err.println("Exception: " + e.toString());
        };

    }

}
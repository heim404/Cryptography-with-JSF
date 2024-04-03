import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.io.*;
import java.nio.file.*;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PublicKey;
import java.security.SecureRandom;

/**
 * This class contains unit tests for the Sign and ValidateSign classes.
 * It tests the generation of signature and public key files when a file exists,
 * and the validation of the signature with a wrong key.
 */
public class SignAndValidateTest {

    /**
     * This method is executed before each test.
     * It deletes any existing signature or public key files.
     */
    @BeforeEach
    void setup() {
        // Delete any existing signature or public key files
        try {
            Files.deleteIfExists(Paths.get("sig"));
            Files.deleteIfExists(Paths.get("suepk"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * This test checks if the Sign class generates signature and public key files when a file exists.
     * It also checks if the ValidateSign class can validate the generated signature with the correct public key.
     */
    @Test
    void shouldGenerateSignatureAndPublicKeyFilesWhenFileExists() {
        String[] args = {"src/Document.pdf"};
        Sign.main(args);
        assertTrue(Files.exists(Paths.get("sig")));
        assertTrue(Files.exists(Paths.get("suepk")));
        ValidateSign.main(new String[] {"suepk", "sig", "src/Document.pdf"});
    }

    /**
     * This test checks if the ValidateSign class fails to validate the signature when a wrong public key is used.
     * It generates a new key pair, saves the wrong public key to a file, and tries to validate the signature with the wrong public key.
     */
    @Test
    void testWithSignDocumentButValidatedWithWrongKey() {
        String[] signArgs = {"src/Document.pdf"};
        Sign.main(signArgs);
        assertTrue(Files.exists(Paths.get("sig")));
        assertTrue(Files.exists(Paths.get("suepk")));

        // Generate a new key pair
        try {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
            keyGen.initialize(1024, random);
            KeyPair pair = keyGen.generateKeyPair();
            PublicKey wrongPub = pair.getPublic();

            // Save the wrong public key to a file
            byte[] wrongKey = wrongPub.getEncoded();
            FileOutputStream wrongKeyFos = new FileOutputStream("wrong_suepk");
            wrongKeyFos.write(wrongKey);
            wrongKeyFos.close();

            // Try to validate the signature with the wrong public key
            ValidateSign.main(new String[] {"wrong_suepk", "sig", "src/Document.pdf"});
        } catch (Exception e) {
            fail("Exception thrown during test: " + e.toString());
        }
    }

}
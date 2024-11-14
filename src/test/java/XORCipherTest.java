package test.java;

import main.java.XORCipher;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class XORCipherTest {

    private static XORCipher xorCipher;

    @BeforeAll
    static void setUp() {
        xorCipher = XORCipher.getInstance();
    }

    @Test
    void testEncryptAndDecryptWithScoring() {
        String plaintext = "This is a wonder world please make it safe";
        String key = "abc";

        // Encrypt the plaintext
        byte[] encrypted = xorCipher.encrypt(plaintext, key);
        assertNotNull(encrypted, "Encrypted result should not be null");

        // Decrypt the encrypted text
        String decryptedText = xorCipher.decryptWithScoring(encrypted, key);
        assertEquals(plaintext, decryptedText, "Decrypted text should match the original plaintext");

        assertEquals(decryptedText, plaintext);
    }

    @Test
    void testDecryptWithScoringWithInvalidKey() {
        String plaintext = "This is a wonder world please make it safe";
        String correctKey = "abc";
        String incorrectKey = "wrong-key";

        // Encrypt the text
        byte[] encrypted = xorCipher.encrypt(plaintext, correctKey);

        // Try decrypting with the wrong key
        String decryptedText = xorCipher.decryptWithScoring(encrypted, incorrectKey);

        assertNotEquals(plaintext, decryptedText, "Decrypted text should not match the plaintext with an incorrect key");
        assertNotEquals(plaintext, decryptedText);
    }

    @Test
    void testCrackXORCipher() {
        int[] encryptedMessage = { 46,14,14,1,79,15,8,79,43,30,7,15,12,10,14,67,79,49,2,2,7,77,22,7,12,29,17,77,14,5,2,66,79,3,10,20,8,29,66,0,6,12,9,79,10,2,24,66,1,0,12,10,79,18,31,10,1,4,28,7,1,22,79,64,7,3,27,6,12,10,79,14,4,27,22,1,10,66,2,29,66,3,0,66,0,0,12,8,22,66,4,1,66,0,22,66,29,26,16,30,10,78,77,14,12,9,79,12,2,27,10,4,1,5,77,31,3,31,27,11,14,26,14,12,29,66,25,0,66,4,1,22,8,29,7,30,27,66,0,10,66,2,1,66,30,7,13,31,10,78 };  // Partial for brevity
        String readableMessage = "Call me Ishmael. Some years ago--never mind how long precisely--having little or no money in my purse, and nothing particular to interest me on shore,";
        // Crack the XOR Cipher using frequency analysis
        String crackedMessage = xorCipher.crackXORCipher(encryptedMessage);

        // Check that the decrypted message contains readable text
        assertNotNull(crackedMessage, "Cracked message should not be null");
        assertFalse(crackedMessage.isEmpty(), "Cracked message should not be empty");
        assertEquals(crackedMessage, readableMessage);
    }
}
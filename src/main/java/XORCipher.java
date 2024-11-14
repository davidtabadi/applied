package main.java;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

public class XORCipher {

    private static final String NOT_PRINTABLE_REGEX = "[^\\x20-\\x7E]";
    private static final String COMMON_CHARACTERS = " etaoinshrdlcumwfgypbvkjxqz";

    //    Singleton class - we need only 1 instance of XORCipher
    private XORCipher() {
    }

    private static final XORCipher INSTANCE = new XORCipher();

    public static XORCipher getInstance() {
        return INSTANCE;
    }

    // Encrypt the text using XOR and a key
    public byte[] encrypt(String plaintext, String key) {
        byte[] keyBytes = key.getBytes();
        byte[] plaintextBytes = plaintext.getBytes();
        byte[] encrypted = new byte[plaintextBytes.length];

        for (int i = 0; i < plaintextBytes.length; i++) {
            encrypted[i] = (byte) (plaintextBytes[i] ^ keyBytes[i % keyBytes.length]);
        }

        return encrypted;
    }

    // Decrypt the encrypted text using XOR
    public String decryptWithScoring(byte[] encryptedText, String key) {
        // XOR with the same key for decryption (XOR is symmetric)
        byte[] decryptedBytes = xorWithKey(encryptedText, key);
        String decryptedText = new String(decryptedBytes);

        // Score the decrypted text based on letter frequencies
        double score = calculateFrequencyScore(decryptedText);

        return decryptedText;
    }

    // XOR the encrypted text with a given key (for decryption)
    private byte[] xorWithKey(byte[] encryptedText, String key) {
        byte[] keyBytes = key.getBytes();
        byte[] decrypted = new byte[encryptedText.length];

        for (int i = 0; i < encryptedText.length; i++) {
            decrypted[i] = (byte) (encryptedText[i] ^ keyBytes[i % keyBytes.length]);
        }
        return decrypted;
    }

    // Calculate the frequency score of the decrypted text
    private double calculateFrequencyScore(String text) {
        // Clean the text by removing non-printable ASCII characters
        text = text.replaceAll(NOT_PRINTABLE_REGEX, "").toLowerCase();

        // Count the frequency of each character in the text
        Map<Character, Integer> frequencyCount = new HashMap<>();
        for (char c : text.toCharArray()) {
            frequencyCount.put(c, frequencyCount.getOrDefault(c, 0) + 1);
        }

        // Calculate the score based on English letter frequencies
        double score = 0.0;
        for (Map.Entry<Character, Integer> entry : frequencyCount.entrySet()) {
            char c = entry.getKey();
            int count = entry.getValue();
            if (FrequencyHelper.ENGLISH_FREQUENCY.containsKey(c)) {
                // Calculate how well the frequency matches the expected frequency
                double expectedFreq = FrequencyHelper.ENGLISH_FREQUENCY.get(c);
                score += Math.abs(expectedFreq - (count / (double) text.length()) * 100);
            }
        }

        return score;
    }

    public String decrypt(int[] encrypted, String key) {
        StringBuilder decrypted = new StringBuilder();
        for (int i = 0; i < encrypted.length; i++) {
            decrypted.append((char) (encrypted[i] ^ key.charAt(i % key.length())));
        }
        return decrypted.toString();
    }

    private int scoreText(String text) {
        String commonCharacters = COMMON_CHARACTERS;
        int score = 0;
        for (char c : text.toLowerCase().toCharArray()) {
            if (commonCharacters.indexOf(c) != -1) {
                score++;
            }
        }
        return score;
    }

    public String crackXORCipher(int[] encrypted) {
        final int[] bestScore = {0};
        final String[] bestDecryption = {""};

        // Parallel stream over ASCII printable range only (32–126)
        IntStream.range(32, 127).parallel().forEach(k1 -> {
            for (int k2 = 32; k2 < 127; k2++) {
                for (int k3 = 32; k3 < 127; k3++) {
                    String key = "" + (char) k1 + (char) k2 + (char) k3;
                    String decryptedText = decrypt(encrypted, key);
                    int score = scoreText(decryptedText);

                    synchronized (bestScore) { // synchronize for thread-safe update
                        if (score > bestScore[0]) {
                            bestScore[0] = score;
                            bestDecryption[0] = decryptedText;
                        }
                    }
                }
            }
        });

        return bestDecryption[0];
    }


    public static void main(String[] args) {

        XORCipher xorCipher = getInstance();

        // Question A
        String textToEncryptA = "This is a wonder world please make it safe";
        String key = "abc";

        // Encrypt the text with XOR encryption
        byte[] encryptedText = xorCipher.encrypt(textToEncryptA, key);

        // Decrypt the text using XOR and scoring stats (frequency analysis)
        String decryptedText = xorCipher.decryptWithScoring(encryptedText, key);

        // Question B
        int[] encryptedTextB = {
                46,14,14,1,79,15,8,79,43,30,7,15,12,10,14,67,79,49,2,2,7,77,22,7,12,29,17,77,14,5,2,66,79,3,10,20,8,29,66,0,6,12,9,79,10,2,24,66,1,0,12,10,79,18,31,10,1,4,28,7,1,22,79,64,7,3,27,6,12,10,79,14,4,27,22,1,10,66,2,29,66,3,0,66,0,0,12,8,22,66,4,1,66,0,22,66,29,26,16,30,10,78,77,14,12,9,79,12,2,27,10,4,1,5,77,31,3,31,27,11,14,26,14,12,29,66,25,0,66,4,1,22,8,29,7,30,27,66,0,10,66,2,1,66,30,7,13,31,10,78,77,38,66,25,7,13,24,8,10,25,79,43,77,24,13,24,3,6,77,28,3,4,3,66,12,13,13,24,27,66,12,79,14,4,27,22,1,10,66,12,1,6,77,28,7,8,79,22,5,10,66,26,14,22,8,29,27,77,31,3,31,27,66,2,9,66,25,7,7,77,24,13,31,3,6,67,79,43,25,79,11,30,79,3,77,24,3,20,79,43,77,7,3,27,10,66,2,9,66,9,29,11,27,6,12,10,79,13,11,9,66,25,7,7,77,28,18,1,10,7,3,79,3,3,11,66,31,10,5,24,3,3,25,6,12,10,79,22,5,10,66,14,6,16,14,26,14,12,27,11,2,1,76,77,56,10,8,1,7,27,10,16,77,38,66,11,6,12,9,79,15,20,28,7,1,9,66,10,29,13,26,6,12,10,79,5,31,6,15,77,14,0,2,26,22,77,27,10,8,79,15,2,26,22,5,84,66,26,7,7,3,10,20,8,29,66,4,27,66,4,28,66,12,79,6,12,2,18,65,79,6,31,6,24,23,3,27,77,33,13,27,10,15,15,10,16,77,6,12,77,2,27,77,28,13,24,3,89,77,24,10,8,1,7,27,10,16,77,38,66,11,6,12,9,79,15,20,28,7,1,9,66,4,1,20,2,3,23,3,27,3,31,6,14,20,79,18,12,26,17,4,1,5,77,13,7,11,0,16,8,79,1,2,9,4,4,1,66,26,14,16,8,7,13,24,28,7,30,67,66,12,1,6,77,13,16,4,1,5,4,1,5,77,26,18,77,27,10,8,79,16,8,14,16,77,0,4,77,10,20,8,29,27,77,9,23,3,10,16,12,3,66,36,79,15,8,10,22,86,79,3,3,11,66,8,28,18,8,12,11,12,3,14,20,79,21,5,10,12,8,25,7,31,79,15,20,79,10,20,31,13,30,79,5,8,27,66,30,26,1,5,79,3,3,79,23,29,31,7,31,79,10,12,1,6,77,0,4,77,2,7,65,79,22,5,14,22,77,6,22,77,29,7,28,26,11,31,10,17,77,14,66,30,27,16,2,1,5,77,2,13,31,14,14,77,31,16,4,1,1,4,31,14,8,79,22,2,79,18,31,10,20,8,1,22,77,2,7,77,9,16,2,2,66,9,10,14,4,13,7,31,14,22,8,3,27,77,28,22,8,31,18,4,1,5,77,6,12,25,0,66,25,7,7,77,28,22,31,10,7,25,67,66,12,1,6,77,2,7,25,7,13,9,6,1,12,3,14,20,79,9,3,0,1,6,6,12,10,79,18,8,0,18,1,10,69,30,79,10,12,27,17,77,0,4,11,66,79,25,7,7,3,67,66,36,79,3,14,12,13,24,1,22,77,6,22,77,7,11,10,7,66,25,6,15,8,79,22,2,79,5,8,27,66,25,0,66,30,10,3,77,14,17,77,28,13,2,1,66,12,28,66,36,79,1,12,1,76,77,59,10,4,28,66,4,28,66,0,22,66,30,26,0,30,27,11,25,26,22,8,79,4,2,29,66,29,6,17,25,0,14,77,14,12,9,79,0,12,3,14,67,79,53,4,27,10,77,14,66,29,7,11,1,0,17,2,31,10,4,12,3,1,79,4,1,0,23,31,6,17,5,79,33,12,27,13,77,27,10,31,0,21,30,79,10,4,2,17,8,3,4,77,26,18,2,1,66,5,6,17,77,28,21,2,29,6,86,79,43,77,30,23,4,10,22,1,22,66,25,14,9,8,79,22,2,79,22,5,10,66,30,7,11,29,65,66,57,7,7,31,10,66,4,28,66,3,0,22,5,6,12,10,79,17,24,29,18,31,6,17,4,1,5,77,6,12,77,27,10,4,28,76,77,38,4,77,27,10,8,22,66,15,26,22,77,4,12,8,24,66,4,27,78,77,14,14,0,0,17,25,79,3,1,3,66,0,10,12,77,6,12,77,27,10,8,6,16,77,11,7,10,29,7,8,67,66,30,0,15,8,79,22,4,2,7,77,0,16,77,0,22,5,10,16,65,79,1,5,10,16,4,28,10,77,25,7,31,22,66,3,10,3,31,3,27,77,27,10,8,79,17,12,2,7,77,9,7,8,3,11,3,8,17,77,27,13,26,14,16,9,28,66,25,7,7,77,0,1,8,14,12,77,24,11,25,7,66,0,10,76};
        String decryptedMessage = xorCipher.crackXORCipher(encryptedTextB);

    }


}



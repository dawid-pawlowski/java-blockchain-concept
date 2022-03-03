package blockchain;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class Block {
    private String hash;
    private final String previousHash;
    private final String data;
    private final long timestamp;
    private int nonce;

    public Block(String data, String previousHash) throws NoSuchAlgorithmException {
        this.data = data;
        this.previousHash = previousHash;
        this.timestamp = System.currentTimeMillis();
        this.hash = computeHash();
    }

    public String computeHash() throws NoSuchAlgorithmException {
        String input = previousHash + timestamp + data;
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
        byte[] bytes = messageDigest.digest(input.getBytes(StandardCharsets.UTF_8));

        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < bytes.length; i++) {
            String hex = byteToHex(bytes[i]);
            stringBuilder.append(hex);
        }

        return stringBuilder.toString();
    }

    private String byteToHex(byte value) {
        char[] digits = new char[2];
        digits[0] = Character.forDigit((value >> 4) & 0xF, 16);
        digits[1] = Character.forDigit(value & 0xF, 16);
        return new String(digits);
    }

    public String getHash() {
        return hash;
    }

    public String getPreviousHash() {
        return previousHash;
    }

    public void mine(int difficulty) throws NoSuchAlgorithmException {
        String target = new String(new char[difficulty]).replace("\0", "0");

        while (!hash.substring(0, difficulty).equals(target)) {
            nonce++;
            hash = computeHash();
        }
    }
}

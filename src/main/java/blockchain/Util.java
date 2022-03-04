package blockchain;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class Util {
    public static String byteToHex(byte value) {
        char[] digits = new char[2];
        digits[0] = Character.forDigit((value >> 4) & 0xF, 16);
        digits[1] = Character.forDigit(value & 0xF, 16);
        return new String(digits);
    }

    public static String getSha256(String input) throws NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
        byte[] bytes = messageDigest.digest(input.getBytes(StandardCharsets.UTF_8));

        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < bytes.length; i++) {
            String hex = byteToHex(bytes[i]);
            stringBuilder.append(hex);
        }

        return stringBuilder.toString();
    }

    public static String getStringFromKey(Key key) {
        return Base64.getEncoder().encodeToString(key.getEncoded());
    }
}

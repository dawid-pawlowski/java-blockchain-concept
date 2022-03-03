package blockchain;

import java.security.*;
import java.security.spec.ECGenParameterSpec;
import java.util.Base64;

public class Wallet {
    private PublicKey publicKey;
    private PrivateKey privateKey;

    public Wallet() throws NoSuchAlgorithmException, NoSuchProviderException, InvalidAlgorithmParameterException {
        generateKeys();
    }

    private void generateKeys() throws NoSuchAlgorithmException, NoSuchProviderException, InvalidAlgorithmParameterException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("ESDSA", "SUN");
        SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG", "SUN");
        ECGenParameterSpec ecGenParameterSpec = new ECGenParameterSpec("prime192v1");

        keyPairGenerator.initialize(ecGenParameterSpec, secureRandom);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();

        publicKey = keyPair.getPublic();
        privateKey = keyPair.getPrivate();
    }

    public String getAddress() {
        return Base64.getEncoder().encodeToString(publicKey.getEncoded());
    }
}

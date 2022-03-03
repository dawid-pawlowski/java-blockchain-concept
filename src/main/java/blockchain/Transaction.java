package blockchain;

import java.nio.charset.StandardCharsets;
import java.security.*;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class Transaction {
    private String id;
    private PublicKey sender;
    private PublicKey recipient;
    private float amount;
    private byte[] signature;

    private List<TransactionOutput> inputs = new ArrayList<>();
    private List<TransactionOutput> output = new ArrayList<>();

    public Transaction(PublicKey sender, PublicKey recipient, float amount, List<TransactionOutput> inputs) {
        this.sender = sender;
        this.recipient = recipient;
        this.amount = amount;
        this.inputs = inputs;
    }

    public void sign(PrivateKey privateKey) throws NoSuchAlgorithmException, SignatureException, NoSuchProviderException, InvalidKeyException {
        String data = getStringFromKey(sender) + getStringFromKey(recipient) + Float.toString(amount);
        signature = generateSignature(privateKey, data);
    }

    public boolean verify() throws SignatureException, NoSuchAlgorithmException, NoSuchProviderException, InvalidKeyException {
        String data = getStringFromKey(sender) + getStringFromKey(recipient) + Float.toString(amount);
        return verifySignature(sender, data, signature);
    }

    private boolean verifySignature(PublicKey publicKey, String data, byte[] signatureData) throws SignatureException, NoSuchAlgorithmException, NoSuchProviderException, InvalidKeyException {
        Signature signature = Signature.getInstance("ECDSA", "SUN");
        signature.initVerify(publicKey);
        signature.update(data.getBytes(StandardCharsets.UTF_8));

        return signature.verify(signatureData);
    }

    private byte[] generateSignature(PrivateKey privateKey, String data) throws NoSuchAlgorithmException, NoSuchProviderException, InvalidKeyException, SignatureException {
        Signature signature = Signature.getInstance("ECDSA", "SUN");
        signature.initSign(privateKey);
        signature.update(data.getBytes(StandardCharsets.UTF_8));

        return signature.sign();
    }

    private String getStringFromKey(Key key) {
        return Base64.getEncoder().encodeToString(key.getEncoded());
    }
}

package blockchain;

import java.nio.charset.StandardCharsets;
import java.security.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Transaction {
    private static int counter = 0;
    private final PublicKey sender;
    private final PublicKey recipient;
    private final double amount;
    private final long timestamp;
    private final List<TransactionOutput> outputs = new ArrayList<>();
    private final List<TransactionOutput> inputs = new ArrayList<>();
    private final String hash;
    private byte[] signature;

    public Transaction(PublicKey sender, PublicKey recipient, double amount) throws NoSuchAlgorithmException {
        this.sender = sender;
        this.recipient = recipient;
        this.amount = amount;
        this.timestamp = System.currentTimeMillis();
        this.hash = computeHash();
    }

    public List<TransactionOutput> getInputs() {
        return inputs;
    }

    public List<TransactionOutput> getOutputs() {
        return outputs;
    }

    public boolean process() throws SignatureException, NoSuchAlgorithmException, NoSuchProviderException, InvalidKeyException {
        if (!verify()) {
            System.out.println("Transaction signature verification failed");
            return false;
        }

        inputs.addAll(Blockchain.getUTXO().stream().filter(transactionOutput -> transactionOutput.isMine(sender)).collect(Collectors.toList()));

        double totalInputsValue = inputs.stream().mapToDouble(TransactionOutput::getAmount).sum();
        // TODO: check minimal transaction amount
        double change = totalInputsValue - amount;

        outputs.add(new TransactionOutput(recipient, amount, hash));
        if (change > 0) outputs.add(new TransactionOutput(sender, change, hash));

        Blockchain.getUTXO().removeAll(inputs);
        Blockchain.getUTXO().addAll(outputs);

        return true;
    }

    public String getHash() {
        return hash;
    }

    private String computeHash() throws NoSuchAlgorithmException {
        return Util.getSha256(Util.getStringFromKey(sender) + Util.getStringFromKey(recipient) + amount + timestamp + (++counter));
    }

    public void sign(PrivateKey privateKey) throws NoSuchAlgorithmException, SignatureException, NoSuchProviderException, InvalidKeyException {
        String data = Util.getStringFromKey(sender) + Util.getStringFromKey(recipient) + amount;
        signature = generateSignature(privateKey, data);
    }

    public boolean verify() throws SignatureException, NoSuchAlgorithmException, NoSuchProviderException, InvalidKeyException {
        String data = Util.getStringFromKey(sender) + Util.getStringFromKey(recipient) + amount;
        return verifySignature(sender, data, signature);
    }

    private boolean verifySignature(PublicKey publicKey, String data, byte[] signatureData) throws SignatureException, NoSuchAlgorithmException, NoSuchProviderException, InvalidKeyException {
        Signature signature = Signature.getInstance("SHA1withDSA", "SUN");
        signature.initVerify(publicKey);
        signature.update(data.getBytes(StandardCharsets.UTF_8));

        return signature.verify(signatureData);
    }

    private byte[] generateSignature(PrivateKey privateKey, String data) throws NoSuchAlgorithmException, NoSuchProviderException, InvalidKeyException, SignatureException {
        Signature signature = Signature.getInstance("SHA1withDSA", "SUN");
        signature.initSign(privateKey);
        signature.update(data.getBytes(StandardCharsets.UTF_8));

        return signature.sign();
    }
}

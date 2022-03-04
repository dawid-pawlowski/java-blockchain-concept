package blockchain;

import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;

public class TransactionOutput {
    private final String hash;
    private final PublicKey recipient;
    private final double amount;
    private final String transactionHash;

    public TransactionOutput(PublicKey recipient, double amount, String transactionHash) throws NoSuchAlgorithmException {
        this.recipient = recipient;
        this.amount = amount;
        this.transactionHash = transactionHash;
        this.hash = computeHash();
    }

    private String computeHash() throws NoSuchAlgorithmException {
        return Util.getSha256(Util.getStringFromKey(recipient) + amount + transactionHash);
    }

    public boolean isMine(PublicKey publicKey) {
        return recipient == publicKey;
    }

    public double getAmount() {
        return amount;
    }
}

package blockchain;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SignatureException;
import java.util.ArrayList;
import java.util.List;

public class Block {
    private final String previousHash;
    private final long timestamp;
    private final List<Transaction> transactions = new ArrayList<>();
    private String hash;
    private int nonce;

    public Block(String previousHash) throws NoSuchAlgorithmException {
        this.previousHash = previousHash;
        this.timestamp = System.currentTimeMillis();
        this.hash = computeHash();
    }

    public String computeHash() throws NoSuchAlgorithmException {
        return Util.getSha256(previousHash + timestamp + nonce + transactions);
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

        System.out.println("Block mined");
    }

    public void addTransaction(Transaction transaction) throws SignatureException, NoSuchAlgorithmException, NoSuchProviderException, InvalidKeyException {
        if (transaction == null) return;

        if (!transaction.process()) {
            System.out.println("Transaction rejected");
            return;
        }

        transactions.add(transaction);
        System.out.println("Transaction accepted");
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }
}

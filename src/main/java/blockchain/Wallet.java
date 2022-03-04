package blockchain;

import java.security.*;
import java.util.List;
import java.util.stream.Collectors;

public class Wallet {
    private PublicKey publicKey;
    private PrivateKey privateKey;

    public Wallet() throws NoSuchAlgorithmException, NoSuchProviderException, InvalidAlgorithmParameterException {
        generateKeys();
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }

    public PrivateKey getPrivateKey() {
        return privateKey;
    }

    private void generateKeys() throws NoSuchAlgorithmException, NoSuchProviderException, InvalidAlgorithmParameterException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("DSA", "SUN");
        SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG", "SUN");
        keyPairGenerator.initialize(1024, secureRandom);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();

        publicKey = keyPair.getPublic();
        privateKey = keyPair.getPrivate();
    }

    public double getBalance() {
        List<TransactionOutput> transactions = Blockchain.getUTXO().stream().filter(transactionOutput -> transactionOutput.isMine(publicKey)).collect(Collectors.toList());
        return transactions.stream().mapToDouble(TransactionOutput::getAmount).sum();
    }

    public Transaction send(PublicKey recipient, double amount) throws NoSuchAlgorithmException, SignatureException, NoSuchProviderException, InvalidKeyException {
        if (getBalance() < amount) {
            System.out.println("Not enough funds");
            return null;
        }

        Transaction transaction = new Transaction(publicKey, recipient, amount);
        transaction.sign(privateKey);

        return transaction;
    }
}

package blockchain;

import java.security.*;

public class App {

    public static void main(String[] args) throws NoSuchAlgorithmException, SignatureException, NoSuchProviderException, InvalidKeyException, InvalidAlgorithmParameterException {

        Wallet coinbase = new Wallet();
        Wallet walletA = new Wallet();
        Wallet walletB = new Wallet();

        Transaction coinbaseTransaction = new Transaction(coinbase.getPublicKey(), coinbase.getPublicKey(), 10000000);
        coinbaseTransaction.sign(coinbase.getPrivateKey());

        Block genesis = new Block("0");

        genesis.addTransaction(coinbaseTransaction);

        System.out.println(coinbase.getBalance());
        System.out.println(walletA.getBalance());
        System.out.println(walletB.getBalance());

        Blockchain.add(genesis);

        Block secondBlock = new Block(genesis.getHash());
        secondBlock.addTransaction(coinbase.send(walletA.getPublicKey(), 100));
        secondBlock.addTransaction(walletA.send(walletB.getPublicKey(), 50));
        Blockchain.add(secondBlock);

        System.out.println(coinbase.getBalance());
        System.out.println(walletA.getBalance());
        System.out.println(walletB.getBalance());

        System.out.println(Blockchain.validate());
    }
}

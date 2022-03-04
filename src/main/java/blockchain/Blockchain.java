package blockchain;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SignatureException;
import java.util.ArrayList;
import java.util.List;

public class Blockchain {

    private final static List<Block> blocks = new ArrayList<>();
    private final static List<TransactionOutput> UTXO = new ArrayList<>();
    private static final int difficulty = 5;

    public static boolean validate() throws NoSuchAlgorithmException, SignatureException, NoSuchProviderException, InvalidKeyException {
        Block currentBlock;
        Block previousBlock;
        String target = new String(new char[difficulty]).replace("\0", "0");

        if (blocks.isEmpty()) return true;

        if (blocks.size() == 1) {
            currentBlock = blocks.get(0);
            if (!currentBlock.getHash().equals(currentBlock.computeHash())) {
                System.out.println("Current hash error");
                return false;
            }

            return true;
        }

        for (int i = 1; i < blocks.size(); i++) {
            currentBlock = blocks.get(i);
            previousBlock = blocks.get(i - 1);

            if (!currentBlock.getHash().equals(currentBlock.computeHash())) {
                System.out.println("Current hash error");
                return false;
            }

            if (!previousBlock.getHash().equals(currentBlock.getPreviousHash())) {
                System.out.println("Previous hash error");
                return false;
            }

            if (!currentBlock.getHash().substring(0, difficulty).equals(target)) {
                System.out.println("Block not mined");
                return false;
            }

            for (int j = 0; j < currentBlock.getTransactions().size(); j++) {
                Transaction currentTransaction = currentBlock.getTransactions().get(j);

                if (!currentTransaction.verify()) {
                    System.out.println("Transaction " + j + " signature verification failed");
                    return false;
                }

                if (currentTransaction.getInputs().stream().mapToDouble(TransactionOutput::getAmount).sum()
                        != currentTransaction.getOutputs().stream().mapToDouble(TransactionOutput::getAmount).sum()) {
                    System.out.println("Transaction " + j + " inputs not equal to outputs");
                    return false;
                }
            }
        }

        return true;
    }

    public static List<TransactionOutput> getUTXO() {
        return UTXO;
    }

    public static Block add(Block block) throws NoSuchAlgorithmException {
        block.mine(difficulty);
        blocks.add(block);
        return block;
    }

}

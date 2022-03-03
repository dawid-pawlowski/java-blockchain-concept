package blockchain;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

public class Chain {

    private final List<Block> list = new ArrayList<>();

    public boolean validateChain() throws NoSuchAlgorithmException {
        Block current;
        Block previous;

        if (list.isEmpty()) return true;

        for (int i = 1; i < list.size(); i++) {
            current = list.get(i);
            previous = list.get(i - 1);

            if (!current.getHash().equals(current.computeHash())) {
                System.out.println("Current hash error");
                return false;
            }

            if (!previous.getHash().equals(current.getPreviousHash())) {
                System.out.println("Previous hash error");
                return false;
            }
        }
        
        return true;
    }
    
    public Block add(Block block) {
        list.add(block);
        return block;
    }

}

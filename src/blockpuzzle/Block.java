package blockpuzzle;

import java.util.ArrayList;
import java.util.List;

public class Block {
    
    private final List<boolean[][]> block = new ArrayList<>(); //the block itself
    private final int[] rotationPossibilities; //all rotation possibilities
    
    //constructor for the block
    public Block(int[] rotationPossibilities, boolean[][] block) {
        this.rotationPossibilities = rotationPossibilities;
        this.block.add(block);
    }
    //getter for the possible rotations
    public int[] getRotations() {
        return rotationPossibilities;
    }
    //getter for the block
    public boolean[][] getBlock(int rotation) {
        boolean tmpBlock[][];
        switch (rotation){
            case 1: //default rotation (the way it is saved in "block")
                return block.get(0);
            case 2:
                if (block.size() > 1) return block.get(1); //check if the block has already been calculated, and if so get it and return it
                
                //calculate, add and return block
                tmpBlock = new boolean[block.get(0)[0].length][block.get(0).length];
                for (int d1 = 0; d1 < block.get(0)[0].length; d1++)
                {
                    for (int d2 = 0; d2 < block.get(0).length; d2++)
                    {
                        tmpBlock[d1][block.get(0).length - 1 - d2] = block.get(0)[d2][d1];
                    }
                }
                block.add(tmpBlock);
                return tmpBlock;
            case 3:
                if (block.size() > 2) return block.get(2); //check if the block has already been calculated, and if so get it and return it
                
                //calculate, add and return block
                tmpBlock = new boolean[block.get(0).length][block.get(0)[0].length];
                for (int d1 = 0; d1 < block.get(0)[0].length; d1++)
                {
                    for (int d2 = 0; d2 < block.get(0).length; d2++)
                    {
                        tmpBlock[block.get(0).length - 1 - d2][block.get(0)[0].length - 1 - d1] = block.get(0)[d2][d1];
                    }
                }
                block.add(tmpBlock);
                return tmpBlock;
            case 4:
                if (block.size() > 3) return block.get(3); //check if the block has already been calculated, and if so get it and return it
                
                //calculate, add and return block
                tmpBlock = new boolean[block.get(0)[0].length][block.get(0).length];
                for (int d1 = 0; d1 < block.get(0)[0].length; d1++)
                {
                    for (int d2 = 0; d2 < block.get(0).length; d2++)
                    {
                        tmpBlock[block.get(0)[0].length - 1- d1][d2] = block.get(0)[d2][d1];
                    }
                }
                block.add(tmpBlock);
                return tmpBlock;
        }
        return block.get(0); //default return (should never reach this point, but if it does, it returns the default unrotated block)
    }
    
}

package blockpuzzle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;
import javax.swing.SwingWorker;

public class BlockpuzzleWorker extends SwingWorker<int[][], int[][]> {
    private final int[][] grid;
    private static final int SIZE = 7;
    private static final List<Block> blocks = fillBlokken(); //list of blocks
    private int[][] newGrid; //grid which gets filled until a solution is found and than will be add to the solutions in the controller
    private final Runnable callbackFoundAll; //callback to run when the recursive function is done
    private final Runnable callbackFoundOne; //callback to run when a solution is found
    private final Runnable callbackPlacedBlock; //callback to run when a block is placed (when the user has checked "show block placing")
    private int[][] putGrid; //grid in which blocks will be put and, when the users want to see block placing, is send to the frame
    private boolean showPuts = true; //variable which is equal to the state of the "show block placing" checkbox

    //constructor for the worker
    public BlockpuzzleWorker(int[][] grid, Runnable callbackFoundAll, Runnable callbackFoundOne, Runnable callbackPlacedBlock, boolean showPuts) {
        this.grid = grid;
        this.callbackFoundAll = callbackFoundAll;
        this.callbackFoundOne = callbackFoundOne;
        this.callbackPlacedBlock = callbackPlacedBlock;
        this.showPuts = showPuts;
    }
    
    //returns a list of all blocks
    private static List<Block> fillBlokken() {
        List<Block> blocks = new ArrayList<>();
        blocks.add(new Block(new int[]{1, 2, 3, 4},
                                new boolean[][]{{false, true, true, false},
                                                {false, true, true, true},
                                                {true, true, true, false}}));
        
        blocks.add(new Block(new int[]{1, 2, 3, 4},
                                new boolean[][]{{false, true, false},
                                                {true, true, true},
                                                {true, true, true}}));
        
        blocks.add(new Block(new int[]{1, 2, 3, 4},
                                new boolean[][]{{true, false, false},
                                                {true, true, false},
                                                {true, true, true}}));
        
        blocks.add(new Block(new int[]{1, 2, 3, 4},
                                new boolean[][]{{false, true, true, false},
                                                {true, true, true, true}}));
        
        blocks.add(new Block(new int[]{1, 2, 3, 4},
                                new boolean[][]{{false, false, true},
                                                {false, true, true},
                                                {true, true, false}}));
        
        blocks.add(new Block(new int[]{1, 2, 3, 4},
                                new boolean[][]{{false, false, true, false},
                                                {true, true, true, true}}));
        
        blocks.add(new Block(new int[]{1, 2},
                                new boolean[][]{{true, true, true, true, true}}));
        
        blocks.add(new Block(new int[]{1, 2, 3, 4},
                                new boolean[][]{{false, true, false},
                                                {true, true, true}}));
        
        blocks.add(new Block(new int[]{1, 2},
                                new boolean[][]{{true, true}}));
        return blocks;
    }
    
    @Override protected int[][] doInBackground() throws Exception {
        solution(grid, 0); //find all solutions
        callbackFoundAll.run(); //call the method that finishes the worker
        return grid; //"doInBackground" has to return an int[][], but it is not used
    }

    //recursive method which finds all the solutions
    private int[][] solution(int[][] matrix, int blockNr) {
        if (!hasOnlyGoodHoles(matrix)){ //Check if the grid has any closed in holes
            return null; //return null so the recursive method goes a step back
        }
        if (!checkForNr(matrix, 0)) { //check if the matrix has empty cells left
            return matrix; //return matrix, because a solution is found
        }
        for (int y = 0; y < SIZE; y++) { //y-axis on the grid
            for (int x = 0; x < SIZE; x++) { //x-axis on the grid
                int[] rotationPossibilities = blocks.get(blockNr).getRotations(); //all rotation possibilities for the current block
                for (int rotation = 0; rotation < rotationPossibilities.length; rotation++) { //loop through all rotations
                    if (tryGrid(matrix, x, y, blocks.get(blockNr), rotationPossibilities[rotation])) { //check if the block (with the current rotation) can be placed at the current location on the grid
                        newGrid = solution(putGrid(copyGrid(matrix), x, y, blockNr, rotationPossibilities[rotation]), blockNr + 1); //copy grid, put the block in the copied grid and call solution with it and increment the blockNr by 1
                        if (newGrid != null) { //if a solution is found
                            callbackFoundOne.run(); //run the callback for a found solution
                            if (showPuts){ //if the user has checked the "Show block placing" checkbox
                                try{
                                    Thread.sleep(500); //sleep for half a second
                                }
                                catch(Exception ex){

                                }
                            }
                        }
                    }
                    if (isCancelled()){ //if the user wants to cancel the worker
                        return null; //return null so the worker steps out of the recursion asap
                    }
                }
            }
        }
        return null; //nothing more to try, all possibilies have been found
    }

    //method which checks if a given block can be placed on a given location on a given grid
    private boolean tryGrid(int[][] grid, int x, int y, Block block, int rotation) {
        if (block.getBlock(rotation)[0].length + x > 7 || block.getBlock(rotation).length + y > 7) //check if the block wont go out of range of the grid
            return false;
        for (int d1 = block.getBlock(rotation)[0].length - 1; d1 >= 0; d1--) {
            for (int d2 = block.getBlock(rotation).length - 1; d2 >= 0 ; d2--) {
                if (block.getBlock(rotation)[d2][d1] && grid[d2 + y][d1 + x] != 0) { //check if the blocks places itself over another block
                    return false;
                }
            }
        }
        return true; //block fits, return true!
    }

    //check if the grid has empty cells without atleast 1 other empty cell next to it
    private boolean hasOnlyGoodHoles(int[][] grid){
        for (int y = 0; y < SIZE; y++)
            for (int x = 0; x < SIZE; x++)
                if (grid[x][y] == 0){
                    if (!isGoodHole(grid, x, y)) return false;
                }
        return true;
    }
    //check if a cell has atleast 1 empty cell next to it
    private boolean isGoodHole(int[][] grid, int x, int y){
        if (x > 0){ //if the cell is not on the left edge
            if (grid[x - 1][y] == 0) return true;
        }
        if (y > 0){ //if the cell is not on the top edge
            if (grid[x][y - 1] == 0) return true;
        }
        if (x < SIZE - 1){ //if the cell is not on the right edge
            if (grid[x + 1][y] == 0) return true;
        }
        if (y < SIZE - 1){ //if the cell is not on the bottom edge
            if (grid[x][y + 1] == 0) return true;
        }
        return false;
    }
    
    //copy a grid and return the copy
    private int[][] copyGrid(int[][] oldGrid) {
        int newGrid[][] = new int[SIZE][];
        IntStream.range(0, SIZE).forEach(i -> newGrid[i] = Arrays.copyOf(oldGrid[i], SIZE));
        return newGrid;
    }

    //put a block in the grid
    private int[][] putGrid(int[][] grid, int x, int y, int blockNr, int rotation) {
        boolean[][] tmpBlock = blocks.get(blockNr).getBlock(rotation); //put the block in tmpBlock
        for (int d1 = 0; d1 < tmpBlock[0].length; d1++) {
            for (int d2 = 0; d2 < tmpBlock.length; d2++) {
                if (tmpBlock[d2][d1]) {
                    grid[d2 + y][d1 + x] = blockNr + 1; //insert block in the grid cell by cell
                }
            }
        }
        if (showPuts){ //if the user has checked the "show block placement" checkbox
            putGrid = grid; //set the putgrid variable
            callbackPlacedBlock.run(); //run the callback for the placement of a block
            try{
                Thread.sleep(1); //sleep for 1ms to the user can see whats happening
            }
            catch(Exception e){
                
            }
        }
        return grid; //return the grid with the block in it
    }

    //check for a number in the grid
    private boolean checkForNr(int[][] grid, int n) {
        for (int y = 0; y < grid.length; y++) {
            for (int x = 0; x < grid[y].length; x++) {
                if (grid[x][y] == n) {
                    return true;
                }
            }
        }
        return false;
    }
    
    //getter for the newGrid (grid with a solution)
    public int[][] getNewGrid(){
        return newGrid;
    }
    //getter for the putGrid (grid with a new block in it)
    public int[][] getPutGrid(){
        return putGrid;
    }
}

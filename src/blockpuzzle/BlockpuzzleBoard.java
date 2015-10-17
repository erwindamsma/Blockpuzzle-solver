package blockpuzzle;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JPanel;

class BlockpuzzleBoard extends JPanel implements MouseListener
{
    private static final int SIZE = 7;
    private final BlockpuzzleFrame frame; //declare the frame
    private final List<int[][]> solutions; //declare the solutionslist
    private int[][] grid; //declare the (visible) grid
    private static final int[][] holes = { //all holes on the board in which the user can place the pin
        {1, 0}, {2, 0}, {6, 0}, {3, 1}, {0, 2}, {2, 2}, {5, 2},
        {3, 3}, {4, 3}, {1, 5}, {2, 5}, {6, 5}, {3, 6}
    }; 
    private boolean readyForPinPlacement = true; //boolean for whether the app is ready for the user to place the pin
    private boolean isDisplayingBlocks = false; //boolean whether the app is currently showing blocks on the board
    
    //constructor for the blockpuzzleboard
    public BlockpuzzleBoard(BlockpuzzleFrame frame){
        this.setPreferredSize(new Dimension(490,490)); //set the preferred size of the panel
        this.frame = frame; //set the global frame variable
        solutions = new ArrayList<>(); //initialize the solutionslist
        grid = new int[7][7]; //initialize the grid
        this.addMouseListener(this); //add mouselistener to itself so the board can detect the users desired pinplacement location
    }
    
    //Clear and reset everything to its settings when the app was started
    public void clear(){
        solutions.clear(); //remove all solutions
        frame.clearSolutionList(); //clear all solutions in the solutionslist on the frame
        setGrid(new int[SIZE][SIZE]); //clear the grid on the frame
        readyForPinPlacement = true;
        isDisplayingBlocks = false;
        frame.setSolveBtn(false); //disable the solvebutton (the pin is not placed yet)
        frame.setClearBtn(false); //disable the clearbutton (there is nothing to be cleared)
    }
    //add a solution to the solutions and to the (visible)solutionlist
    public void addToSolutions(int[][] solution){
        solutions.add(solution); //add solution to solutions
        setGrid(solution); //set the grid to newly added solution
        frame.addToSolutionList(solution); //add the solution to the visible solutionslist
    }
    //show a grid on the frame
    public void setGrid(int[][] grid){
        this.grid = grid;
        this.repaint();
    }
    //method to use after the worker has been cancelled. this makes it possible to user solve and clear buttons and place the pin, without first having to click "clear"
    public void cancel(){
        if (!isDisplayingBlocks){
            readyForPinPlacement = true;
            frame.setSolveBtn(true);
            frame.setClearBtn(true);
            frame.setCancelBtn(false);
        }
    }
    //Check if there is a hole at the given xy coordinates
    private boolean isHole(int x, int y) {
        for (int hole = 0; hole < holes.length; hole++)
            if (holes[hole][0] == x && holes[hole][1] == y)
                return true;
        return false;
    }
    //gets the grid that is shown on the frame
    public int[][] getgrid() {
        return grid;
    }
    //sets the frames subelements to "solving" mode
    public void setSolveMode() {
        readyForPinPlacement = false;
        frame.setSolveBtn(false);
        frame.setClearBtn(false);
        frame.setCancelBtn(true);
    }
    //sets the frames subelements to "finished solving" mode
    public void setFinishedSolveMode() {
        frame.setClearBtn(true);
        frame.setCancelBtn(false);
        setGrid(solutions.get(solutions.size() - 1)); //show the last solution on the frame
    }
    //get a specific solution
    public int[][] getSolution(int index){
        return solutions.get(index);
    }
    //set the "isDisplayingBlocks" boolean. The variable should be set to true when a block is placed
    public void setIsDisplayingBlocks(boolean b){
        if (isDisplayingBlocks != b)
            isDisplayingBlocks = true;
    }
    
    @Override protected void paintComponent(Graphics g){
        super.paintComponent(g); //call paintComponent on the superclass
        
        int cellSizeX = this.getWidth() / grid.length; //figure out what the cellsize should be
        int cellSizeY = this.getHeight() / grid.length; //figure out what the cellsize should be
        
        for(int i = 0; i < grid.length; i++){
            int y = cellSizeY * i; //figure out the upper position of a cell
            for(int j = 0; j < grid[0].length; j++){
                int x = cellSizeX * j; //figure out the left position of a cell
                if (grid[i][j] == 0 || grid[i][j] == 10){ //if the cell is empty or its the cell where the pin is placed (pin == 10)
                    g.setColor(Color.getHSBColor(0.105f, 0.545f, 0.8275f)); //set the color to lightbrown
                } else { //if a block is on the current position
                    g.setColor(Color.getHSBColor(grid[i][j] / 10f, 1, 0.9f)); //set the color to a color that is dependent on the number of the block that is on its position
                }
                g.fillRect(x, y, cellSizeX, cellSizeY); //fill the current cell
                
                if (isHole(j, i)){ //if there is a hole in the current cell
                    if (grid[i][j] == 10) //if there is a pin in the current hole
                        g.setColor(Color.LIGHT_GRAY); //set the color to lightgray
                    else if (grid[i][j] == 0) //if there is NO pin in the current hole
                        g.setColor(Color.getHSBColor(0.07f, 0.68f, 0.66f)); //set the color to brown
                    
                    int min = (int)(Math.min(cellSizeX, cellSizeY) / 1.2f); //calculate the hole/pin size
                    g.fillOval(x + (cellSizeX/2) - (min/2), y + (cellSizeY/2) - (min/2), min, min); //paint the hole/pin
                }
            }
        }
        drawBorders(g);
    }
    
    //draw borders around the board and
    public void drawBorders(Graphics g)
    {
        int cellSizeX = this.getWidth() / grid.length;
        int cellSizeY = this.getHeight() / grid.length;
        
        g.setColor(Color.BLACK);
        
        //loop through the grid
        for(int i = 0; i < grid.length; i++){
            int y = cellSizeY * i;
            for(int j = 0; j < grid[0].length; j++){
                int x = cellSizeX * j;
                
                //Draw borders around filled cells
                if (grid[i][j] > 0 && grid[i][j] < 10){ //don't draw border around empty cells and pin
                    if(i<SIZE-1)
                        if (grid[i][j] != grid[i+1][j])
                            g.fillRect(x, y + cellSizeY - 2, cellSizeX, 4); //bottom border
                    if(j<SIZE-1)
                        if (grid[i][j] != grid[i][j+1])
                            g.fillRect(x + cellSizeX - 2, y, 4, cellSizeY); //right border
                    if(i>0)
                        if(grid[i][j] != grid[i-1][j])
                            g.fillRect(x, y - 2, cellSizeX, 4); //top border
                    if(j>0)
                        if(grid[i][j] != grid[i][j-1])
                            g.fillRect(x - 2, y, 4, cellSizeY); //left border
                }
                

            }
        }
        
        //Draw borders around board
        g.fillRect(0, 0, 4, this.getHeight());//left border
        g.fillRect(this.getWidth() - 4, 0, 4, this.getHeight());//right border
        g.fillRect(0, 0, this.getWidth(), 4);//Top border
        g.fillRect(0, this.getHeight() - 4, this.getWidth(), 4);//Bottom border
    }
    
    @Override public void mouseClicked(MouseEvent e){} //not used
    @Override public void mousePressed(MouseEvent e){} //not used
    @Override public void mouseReleased(MouseEvent e){
        int cellSizeX = this.getWidth() / grid.length; //calculate the cellsize
        int cellSizeY = this.getHeight() / grid.length; //calculate the cellsize
        
        int x = e.getX() / cellSizeX; //calculate which cell is clicked
        int y = e.getY() / cellSizeY; //calculate which cell is clicked
        
        //1. check if the board is ready for the placement of a pin 
        //2. check if the cell isn't out of range. out of range is the result of the user clicking in the board and dragging out the mouse before releasing the mousebutton
        //3. check if there is a hole in the current cell
        if (readyForPinPlacement && x < SIZE && y < SIZE && isHole(x, y)){
            setGrid(new int[SIZE][SIZE]); //empty the grid
            grid[y][x] = 10; //place the pin
            repaint(); //repaint the board
            frame.setSolveBtn(true); //enable the solve button
            frame.setClearBtn(true); //enable the clear button
        }
    }
    @Override public void mouseEntered(MouseEvent e){} //not used
    @Override public void mouseExited(MouseEvent e){} //not used
    
}
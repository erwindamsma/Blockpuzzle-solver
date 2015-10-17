package blockpuzzle;

import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JList;
import javax.swing.event.ListSelectionEvent;

public class BlockpuzzleController implements MouseListener {
    private final BlockpuzzleBoard board;
    private BlockpuzzleWorker worker;
    private boolean showPuts;
    
    //constructor for the blockpuzzlecontroller
    public BlockpuzzleController(BlockpuzzleBoard board){
        this.board = board; //set the board
    }
    //method which is called when the worker has completed solving
    private void onSolveComplete(){
        board.setFinishedSolveMode(); //set the board to "finished solving" mode
    }
    //method which is called when the worker has found a solution
    private void onFoundSolution(int[][] solution){
        board.addToSolutions(solution); //add a solution to the list
        board.setIsDisplayingBlocks(true); //set the "isDisplayingBlocks" variable to true
    }
    //method which is called when the worker has placed a block while the "show block placing" checkbox is selected
    private void onBlockPlaced(int[][] putGrid){
        board.setGrid(putGrid); //set the grid on the board
        board.setIsDisplayingBlocks(true); //set the "isDisplayingBlocks" variable to true
    }
    //ready the board for solving and initialize and execute the worker
    public void actionPerformedSolve (ActionEvent ae){
        board.setSolveMode();
        //initialize worker
        worker = new BlockpuzzleWorker(board.getgrid(), () -> {onSolveComplete();}, () -> {onFoundSolution(worker.getNewGrid());}, () -> {onBlockPlaced(worker.getPutGrid());}, showPuts);
        worker.execute();
    }
    //clear the board
    public void actionPerformedClear (ActionEvent ae){
        board.clear();
    }
    //cancel solving
    public void actionPerformedCancel (ActionEvent ae){
        worker.cancel(true); //stop the worker
        board.cancel();
    }
    //show a solution the user has chosen from the list
    public void actionPerformedChooseSolution (ListSelectionEvent e, JList list){
        if(e.getValueIsAdjusting()) //check if the user adjusted the selection on the list
            board.setGrid(board.getSolution(list.getSelectedIndex())); //set the grid to the selected solution
    }
    //show the the placing of blocks
    public void actionToggledShowBlockPlacing(ItemEvent ae){
        switch(ae.getStateChange()){ //state of the checkbox
            case 1: //if the checkbox is checked
                showPuts = true;
                break;
            case 2: //if the checkbox is unchecked
                showPuts = false;
                break;
        }
    }
    
    @Override public void mouseClicked(MouseEvent e){} //not used
    @Override public void mousePressed(MouseEvent e){} //not used
    @Override public void mouseReleased(MouseEvent e){} //not used
    @Override public void mouseEntered(MouseEvent e){} //not used
    @Override public void mouseExited(MouseEvent e){} //not used
}

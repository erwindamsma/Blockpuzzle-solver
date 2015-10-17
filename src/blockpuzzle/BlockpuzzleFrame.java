package blockpuzzle;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.JCheckBox;

public class BlockpuzzleFrame {
    private final JFrame frame = new JFrame(); //declare and initialize the main frame
    private final JPanel framePanel = new JPanel (); //declare and initialize the framePanel on which the board and controlPanel will be drawn
    private final JPanel controlPanel = new JPanel(); //declare and initialize the framePanel on which the controls will be drawn
    
    private final BlockpuzzleBoard board = new BlockpuzzleBoard(this); //declare and initialize the board which is to be placed on the framePanel
    private final BlockpuzzleController controller = new BlockpuzzleController(board); //declare and initialize the controller
    
    private final DefaultListModel dfList = new DefaultListModel(); //declare and initialize the content for the list of solutions
    private final JList list = new JList(dfList); //declare and initialize the list for the solutions
    private final JScrollPane listScroller = new JScrollPane(list); //declare and initialize the listscroller in which the solution-list will be placed
    
    private final JButton solveBtn = new JButton("Solve");
    private final JButton cancelBtn = new JButton("Cancel");
    private final JButton clearBtn = new JButton("Clear");
    private final JCheckBox showPutsCheckBox = new JCheckBox("Show block placing");
    
    //Constructor for the BlockpuzzleFrame
    public BlockpuzzleFrame()
    {   
        framePanel.setBorder (BorderFactory.createEmptyBorder(5, 5, 5, 5)); //Give the framePanel an invisible border of 5 pixels
        framePanel.setLayout (new BorderLayout (5, 5)); //Set the layout to borderLayout with 5px gaps
        framePanel.add(board, BorderLayout.CENTER); //add the board to the framePanel
        
        controlPanel.setPreferredSize(new Dimension(150, 300));
        controlPanel.setLayout(new FlowLayout()); //set the layout to flowlayout
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.setLayoutOrientation(JList.VERTICAL); //set the orientation to vertical
        listScroller.setPreferredSize(new Dimension(145, 200));
        list.addListSelectionListener(e -> controller.actionPerformedChooseSolution(e, list)); //add listener to the list
        controlPanel.add(listScroller); //add the solutionslist to the controlpanel
        solveBtn.addActionListener(e -> controller.actionPerformedSolve(e)); //add listener to the solve button
        solveBtn.setPreferredSize(new Dimension(145, 30));
        solveBtn.setEnabled(false); //disable the button (greyed out)
        controlPanel.add(solveBtn); //add the solvebutton to the controlpanel
        cancelBtn.addActionListener(e -> controller.actionPerformedCancel(e)); //add listener to the cancel button
        cancelBtn.setPreferredSize(new Dimension(145, 30));
        cancelBtn.setEnabled(false); //disable the button (greyed out)
        controlPanel.add(cancelBtn); //add the cancelbutton to the controlpanel
        clearBtn.addActionListener(e -> controller.actionPerformedClear(e)); //add listener to the clear button
        clearBtn.setPreferredSize(new Dimension(145, 30));
        clearBtn.setEnabled(false); //disable the button (greyed out)
        controlPanel.add(clearBtn); //add the clearbutton to the controlpanel
        showPutsCheckBox.addItemListener(e -> controller.actionToggledShowBlockPlacing(e)); //add listener to the checkbox
        controlPanel.add(showPutsCheckBox); //add the checkbox to the controlpanel
        
        framePanel.add(controlPanel, BorderLayout.LINE_END); //add the controlpanel to right part of the framepanel
        frame.add(framePanel); //add the framepanel to the "main" frame
        frame.setTitle ("Blockpuzzle Solver");
        frame.setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE); //Exit the app when the frame user closes the screen
        frame.pack(); //Causes this Window to be sized to fit the preferred size and layouts of its subcomponents
        frame.setResizable(true);
        frame.setLocationRelativeTo(null); //centers the window on starting
        frame.setVisible(true); //shows the frame
    }
    
    //clear the visible solutionlist on the frame of all items
    public void clearSolutionList(){
        dfList.clear();
    }
    //add a solution to the list of solutions on the frame
    public void addToSolutionList(int[][] solutions){
        dfList.addElement("Solution: " + (dfList.getSize() + 1));
    }
    //enable or disable the solvebutton
    public void setSolveBtn(boolean enabled){
        solveBtn.setEnabled(enabled);
    }
    //enable or disable the clearbutton
    public void setClearBtn(boolean enabled){
        clearBtn.setEnabled(enabled);
    }
    //enable or disable the cancelbutton
    public void setCancelBtn(boolean enabled){
        cancelBtn.setEnabled(enabled);
    }
}
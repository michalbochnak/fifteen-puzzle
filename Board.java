// Michal Bochnak, Netid: mbochn2
// CS 342 Project #2 - Fifteen Puzzle
// 10/4/2017
// UIC, Pat Troy
//
// Board.java
//

//
// import libraries
//
import javax.swing.*;
import javax.swing.Timer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.List;


//
// Most of the program is in that class, class keeps all the information
// about the game such as board representation, GUI to display,
// move numbers and so forth.
// This class handles the moves related operations such as,
// updating the GUI when it gets changed,
// contains the Listeners for all the menu options, displays the info dialogs
//
public class Board{

    // private members
    private List<BoardPiece> board;
    private BufferedImage bg;               // image
    private GUI boardGUI;
    private int numOfButtons;
    private int numOfMoves;
    private int freeSpot;
    private Stack<List<BoardPiece>> movesStack;
    private Timer timer;
    private int delay;                                  // delay for animation
    // keeps track if user had chosen his image
    private boolean userImageChoosen = false;
    private int puzzleComplexity;

    // contructor, build the board based on instance of BackgroundImage class
    Board(BackgroundImage img) {
        puzzleComplexity = 0;
        bg = img.getWholeImg();
        numOfButtons = 16;
        freeSpot = 15;
        boardGUI = new GUI();
        board = new ArrayList<BoardPiece>();
        movesStack = new Stack<List<BoardPiece>>();
        numOfMoves = 0;
        delay = 250;            // milliseconds
        timer = new Timer(delay, new TimerHandler());
        boardGUI.setBgImg(img.getWholeImg());

        // build the board and GUI from the images
        // add action listeners to the buttons
        for ( int i = 0; i < numOfButtons; ++i) {
            board.add(new BoardPiece(img.getImageAtIndex(i), i));
            boardGUI.updateButtonImage(img.getImageAtIndex(i), i);
            // set property to be able to identify the button during event
            boardGUI.getButtonAtIndex(i).putClientProperty("id", i);
            boardGUI.getButtonAtIndex(i).addActionListener(new ImageListener());
        }

        boardGUI.updateButtonImage(img.getImageAtIndex(1), 1);
        boardGUI.setButtonsImages(board);
        // add action listeners for the menu items
        addListenersToMenuItems();
        // generate shuffled, solvable board
        generatePuzzles();
    }

    // ----------------------------------------------------------------------------------
    // getters
    // ----------------------------------------------------------------------------------
    public GUI getBoardGUI() {
        return boardGUI;
    }

    // ----------------------------------------------------------------------------------
    // setters
    // ----------------------------------------------------------------------------------
    public void setUserImageChoosen(boolean val) {
        // trigger image choosen
        userImageChoosen = val;
    }

    // ----------------------------------------------------------------------------------
    // methods
    // ----------------------------------------------------------------------------------

    // perform move
    public void move(int clickedSpot) {
        movesStack.push(new ArrayList<BoardPiece>(board));
        numOfMoves++;

        // update the free spot index
        // swap elements in the array
        BoardPiece temp = board.get(clickedSpot);
        board.set(clickedSpot, board.get(freeSpot));
        board.set(freeSpot, temp);

        // switch labels on buttons
        boardGUI.switchImages(board.get(freeSpot).getImage(),
                                board.get(clickedSpot).getImage(), freeSpot, clickedSpot);

        // update free spot index
        freeSpot = clickedSpot;

        // update the red border on new free spot
        boardGUI.updateFreeSpot(board);
        boardGUI.updateNumberOfMoves(numOfMoves);

        // update images
        boardGUI.setButtonsImages(board);

        // game completed
        if(gameOver()) {
                showSolvedDialog();
        }
    }

    // display dialog if puzzles solved
    private void showSolvedDialog() {
        // solved, but started form reseted board (win arrangement)
        String info = "Puzzles are in solved position.\n" +
                                        "Press 'OK' to shuffle, exit to close the dialog.\n";
        // random arrangement
        if (puzzleComplexity != 0) {
            info = "Puzzles solved! Congratulations.\n" +
                    "Puzzles were solved with " + numOfMoves + " moves\n" +
                    "Complexity of the original puzzle was " + puzzleComplexity + ".\n" +
                    "Click ok to start new game.";
        }

        int action = JOptionPane.showOptionDialog(boardGUI.getFrame(),
                info, "Solved",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null, null, null);

        if (action == 0)
            generatePuzzles();
    }

    // returns true if move was valid, false otherwise
    public boolean moveValid(int clickedSpot) {
        // look for free spot around the clicked button
        if (freeSpot == clickedSpot - 1 || freeSpot == clickedSpot + 1 ||
                freeSpot == clickedSpot - 4 || freeSpot == clickedSpot + 4)
            return true;        // free spot found around
        else
            return false;       // free spot not found around
    }

    // returns true if puzzle was completed, false otherwise
    // compares the puzzle position id with index with array
    // when puzzles are solved puzzle id matches index in the array
    public boolean gameOver() {
        for (int i = 0; i < board.size(); ++i) {
            if (board.get(i).getPosition() != i)
                return false;       // condition broken, not solved
        }
        return true;                // solved
    }

    // generates solvable puzzles
    public void generatePuzzles() {
        shufflePuzzles();
        // shuffle till solvable board is generated
        while (!solvable()) {
            shufflePuzzles();
        }
    }

    // randomize the puzzles
    public void shufflePuzzles() {

        // shuffle the board array
        Collections.shuffle(board);

        // find the puzzle number 16 ( index 15 ) and make sure
        // it is on the last spot
        int foundAt = -1;
        for (int i = 0; i < board.size(); ++i) {
            if (board.get(i).getPosition() == 15) {
                foundAt = i;
                break;
            }
        }
        // not found, error
        if (foundAt == -1)
            System.out.println("Error, piece with index 15 not found ( 16th piece on board )");
        // piece in correct, last spot
        else if (foundAt == 15) {
            return;
        }
        // need to swap to put it at the end
        else {
            BoardPiece tempPiece =  board.get(board.size()-1);
            board.set(board.size()-1, board.get(foundAt));
            board.set(foundAt, tempPiece);
        }

        // set up the images on buttons
        boardGUI.setButtonsImages(board);
        freeSpot = 15;
    }

    // check if board is solvable, set the value of the complexity button in GUI
    public boolean solvable(){
        int totalnversionCount = 0;

        // calculate inversion
        for (int i = 0; i < board.size(); ++i) {
            totalnversionCount += inversionCount(i);
        }

        // assign to puzzleComplexity
        puzzleComplexity = totalnversionCount;
        boardGUI.setComplexityText(puzzleComplexity);
        // true if even, false if odd
        return (totalnversionCount % 2) == 0;
    }

    // calculate the inversion count for given index
    public int inversionCount(int index) {
        int invCount = 0;
        int pieceID = board.get(index).getPosition();
        for (int i = index + 1; i < board.size(); ++i)  {
            if (board.get(i).getPosition() < pieceID) {
                invCount++;
            }
        }
        return invCount;
    }

    private void addListenersToMenuItems() {
        boardGUI.getUndoMenuItem().addActionListener(new UndoMoveListener());
        boardGUI.getUndoAllMenuItem().addActionListener(new UndoAllMovesListener());
        boardGUI.getAutoSolveMenuItem().addActionListener(new AutoSolveListener());
        boardGUI.getShuffleMenuItem().addActionListener(new ShuffleListener());
        boardGUI.getHelpMenuItem().addActionListener(new HelpListener());
        boardGUI.getQuitMenuItem().addActionListener(new QuitListener());
        boardGUI.getAboutMenuItem().addActionListener(new AboutListener());
        boardGUI.getResetMenuItem().addActionListener(new ResetListener());
        boardGUI.getShowImageMenuItem().addActionListener(new ShowImageListener());
        boardGUI.getChooseImageMenuItem().addActionListener(new ImageChooserListener());
    }

    // solve the puzzle using Breath-First-Search
    public void solve() {

        // no need to solve
        if (gameOver()) {
            int action = JOptionPane.showOptionDialog(boardGUI.getFrame(),
                    "Puzzles already solved.\n", "Solved",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.INFORMATION_MESSAGE,
                    null, null, null);

            if (action == 0)
                generatePuzzles();

            return;
        }

        // BFS algorithm
        Queue<QueuePair> q = new LinkedList<QueuePair>();
        Set<SetPair> s = new HashSet<SetPair>();
        List<BoardPiece> P = board;
        List<List<BoardPiece>> arrangements;

        QueuePair tempPair = new QueuePair();
        tempPair.UpdateBoardAddMovement(P, -1);

        // add pairs to queue and set
        q.add(tempPair);
        s.add(new SetPair(P, true));

        QueuePair current = new QueuePair();
        while (!q.isEmpty()) {
            // dequeue
            current =  q.remove();
            if (current.solved())
                break;          // solution found

            // get arrangement List
            arrangements = current.possibleArrangements();
            for (List<BoardPiece> r: arrangements) {
                if(!s.contains(r)) {
                    List<Integer> tempList = new  ArrayList(current.getMovements());
                    tempList.add(findFreeSpot(r));
                    q.add(new QueuePair(r, tempList));
                    s.add(new SetPair(r, false));
                }
            }
        }

        // no solution
        if (q.isEmpty()) {
            System.out.println("No solution found, unsolvable puzzles created.");
        }
        // solution found
        showAnimation(current.getMovements());
    }

    // show solution
    private void showAnimation(final List<Integer> path) {

        // timer to wait after each move
        timer = new Timer(delay, new TimerHandler() {

            public void actionPerformed(ActionEvent event) {
                if (!path.isEmpty()) {
                    move(path.get(0));
                    path.remove(0);
                }
                // done, stop the timer
                else {
                    timer.stop();
                }
            }
        });

        timer.start();
    }

    // find and return the index of free spot
    private int findFreeSpot(List<BoardPiece> arrangement) {

        for (int i = 0; i < arrangement.size(); ++i) {
            if (arrangement.get(i).getPosition() == 15)
                return i;
        }

        return -1;
    }

    // reset the board to the solved combination
    public void reset() {
        freeSpot = 15;
        numOfMoves = 0;

        // sort list by BoardPos
        Collections.sort(board, new Comparator<BoardPiece>() {
            public int compare(BoardPiece p1, BoardPiece p2) {
                if (p1.getPosition() > p2.getPosition())
                    return 1;
                else if (p1.getPosition() < p2.getPosition())
                    return -1;
                else
                    return 0;
            }
        });

        // update the board GUI
        boardGUI.setButtonsImages(board);
        movesStack.clear();
        puzzleComplexity = 0;
        boardGUI.setComplexityText(puzzleComplexity);
    }

    // undo one move
    public void undoOne() {
        board = movesStack.pop();
        boardGUI.setButtonsImages(board);
        freeSpot = boardGUI.updateFreeSpot(board);
        numOfMoves--;
        boardGUI.updateNumberOfMoves(numOfMoves);
    }

    // undo all moves
    public void undoAll() {
        timer = new Timer(delay, new TimerHandler());

        timer.start();
    }

    // --------------------------------------------------------------------------------------------
    // Listeners classes
    // --------------------------------------------------------------------------------------------

    // image buttons listener
    private class ImageListener implements ActionListener{
        public void actionPerformed(ActionEvent event) {
            JButton button = (JButton) event.getSource();
            int clickedPos = (Integer)button.getClientProperty("id");

            // verify if move correct, move if so
            if (moveValid(clickedPos)) {
                move(clickedPos);
            }
        }
    }
    // menu items listeners
    private class UndoMoveListener implements ActionListener{
        public void actionPerformed(ActionEvent event) {
            if (movesStack.empty())
                System.out.println("Stack empty");
            else {
                undoOne();
            }
        }
    }
    private class UndoAllMovesListener implements ActionListener{
        public void actionPerformed (ActionEvent event){
            if (movesStack.empty())
                System.out.println("Stack empty");
            else {
                undoAll();
            }
        }
    }
    private class AutoSolveListener implements ActionListener{
        public void actionPerformed(ActionEvent event) {
            solve();
        }
    }
    private class ShuffleListener implements ActionListener{
        public void actionPerformed(ActionEvent event) {
            movesStack.clear();
            generatePuzzles();
        }
    }
    private class HelpListener implements ActionListener{
        public void actionPerformed(ActionEvent event) {
            String info = "How to play:\n" +
                    "Rearrange the pieces to recreate the original Image\n" +
                    "by clicking on pieces next to the piece in blue box.\n" +
                    "Blue box indicates the piece that should be at right bottom\n" +
                    "corner when the board is solved\n\n" +
                    "Menu options:\n" +
                    "   Game: \n" +
                    "       Undo move - undo one move.\n" +
                    "       Undo all moves - back trace all the moves.\n" +
                    "       Auto solve - run to see the solution path.\n" +
                    "       Shuffle - start new game, generate new board.\n" +
                    "       Reset - show original arrangement for image.\n" +
                    "       Show image - see the image for reference.\n" +
                    "       Choose your image - choose your image to create the puzzles.\n" +
                    "                 Recommended Image size is square of size 750 x 750.\n" +
                    "                 Please note, that image that is bigger than 750 x 750\n" +
                    "                 will be resized to 750 x 750. and might be deformed,\n" +
                    "                 if it is not square.\n" +
                    "   Other:\n" +
                    "       Help - instructions about game and menu.\n" +
                    "       Quit - end the program.\n" +
                    "       About - info about the application.";

            JOptionPane.showMessageDialog(boardGUI.getFrame(), info);
        }
    }
    private class QuitListener implements ActionListener{
        public void actionPerformed(ActionEvent event) {
            System.exit(0);
        }
    }
    private class AboutListener implements ActionListener{
        public void actionPerformed(ActionEvent event) {
            String info = "Author: Michal Bochnak\n" +
                    "netid: mbochn2" +
                    "Date: Oct 4, 2017\n CS 342 Assignment #2 - Fifteen Puzzle\n\n" +
                    "Both Extra credits attempted successfully.";

            JOptionPane.showMessageDialog(boardGUI.getFrame(), info);
        }
    }
    private class ResetListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            reset();
        }
    }
    private class ShowImageListener implements  ActionListener{
        public void actionPerformed(ActionEvent event) {
            boardGUI.setBgImg(bg);
            boardGUI.showWholeImage();
        }
    }
    private class ImageChooserListener implements  ActionListener{
        public void actionPerformed(ActionEvent event) {
            if(userImageChoosen == true) {
                String info = "Image was already loaded";
                JOptionPane.showMessageDialog(boardGUI.getFrame(), info);
            }
            else {
                // display image chooser
                boardGUI.showImageChooser();
            }
        }
    }
    // Timer
    private class TimerHandler implements ActionListener {
        public void actionPerformed( ActionEvent event ) {
            undoOne();

            if (movesStack.empty())
                timer.stop();
        }
    }

}   //  end of Board class

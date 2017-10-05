// Michal Bochnak, Netid: mbochn2
// CS 342 Project #2 - Fifteen Puzzle
// 10/4/2017
// UIC, Pat Troy
//
// GUI.java
//

//
// import libraries
//
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;

//
// keeps info about Graphical User Interface
// and interacts with the user
//
public class GUI {

    // private members
    private int numOfButtons;
    private JFrame frame;
    private JFrame bgImgFrame;
    private JLabel wholeImage;
    private JPanel mainPanel;
    private JPanel infoPanel;
    private JPanel squares;
    private JButton buttons[];
    private JLabel complexityText;
    private JLabel numOfMovesText;
    private JFileChooser imageChooser;
    private int selectedOption;
    private JMenuBar gameMenuBar;
    private JMenuItem undoMenuItem;
    private JMenuItem undoAllMenuItem;
    private JMenu gameMenu;
    private JMenu otherMenu;
    private JMenuItem helpMenuItem;
    private JMenuItem autoSolveMenuItem;
    private JMenuItem shuffleMenuItem;
    private JMenuItem quitMenuItem;
    private JMenuItem aboutMenuItem;
    private JMenuItem resetMenuItem;
    private JMenuItem showImageMenuItem;
    private JMenuItem chooseImageMenuItem;


    // constructor
    GUI() {
        frame = new JFrame();
        mainPanel = new JPanel();
        squares = new JPanel();
        numOfButtons = 16;
        gameMenuBar = new JMenuBar();
        gameMenu = new JMenu("Game");
        undoMenuItem = new JMenuItem("Undo move");
        undoAllMenuItem  = new JMenuItem("Undo All moves");
        otherMenu = new JMenu("Other");
        helpMenuItem = new JMenuItem("Help");
        autoSolveMenuItem = new JMenuItem("Auto solve");
        shuffleMenuItem = new JMenuItem("Shuffle");
        quitMenuItem = new JMenuItem("Quit");
        aboutMenuItem = new JMenuItem("About");
        resetMenuItem = new JMenuItem("Reset");
        showImageMenuItem = new JMenuItem("Show Image");
        chooseImageMenuItem = new JMenuItem("Choose Your Image");
        buttons = new JButton[numOfButtons];
        complexityText = new JLabel("Complexity: ");
        numOfMovesText = new JLabel("   |    Moves so far: 0");
        infoPanel = new JPanel();
        bgImgFrame = new JFrame();
        wholeImage = new JLabel();
        imageChooser = new JFileChooser();
        imageChooser.setDialogTitle("Choose your image");
        selectedOption = 0;

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // set up box layout along x-axis
        // also add 16 buttons to panels in total, 4 to each
        // add 16 buttons
        for (int i = 0; i < numOfButtons; ++i) {
            buttons[i] = new JButton();
            buttons[i].setBorder(BorderFactory.createEmptyBorder());
            squares.add(buttons[i]);
        }

        // built the GUI layout
        squares.setLayout(new GridLayout(4, 4));
        gameMenuBar.add(gameMenu);
        gameMenuBar.add(otherMenu);
        gameMenu.add(undoMenuItem);
        gameMenu.add(undoAllMenuItem);
        gameMenu.add(autoSolveMenuItem);
        gameMenu.add(shuffleMenuItem);
        gameMenu.add(resetMenuItem);
        gameMenu.add(showImageMenuItem);
        gameMenu.add(chooseImageMenuItem);
        otherMenu.add(helpMenuItem);
        otherMenu.add(quitMenuItem);
        otherMenu.add(aboutMenuItem);
        mainPanel.add(squares);
        infoPanel.add(BorderLayout.NORTH, complexityText);
        infoPanel.add(BorderLayout.NORTH, numOfMovesText);
        frame.getContentPane().add(BorderLayout.NORTH, gameMenuBar);
        frame.getContentPane().add(BorderLayout.SOUTH, infoPanel);
        frame.getContentPane().add(mainPanel);
        frame.setSize(950, 950);
        frame.setResizable(false);

        // add frame to distinquish the free pie ce
        buttons[buttons.length - 1].setBorder(BorderFactory.createLineBorder(Color.BLUE, 8));
    }


    // ----------------------------------------------------------------------------------
    // getters
    // ----------------------------------------------------------------------------------
    public JFrame getFrame() {
        return frame;
    }
    public JMenuItem getUndoMenuItem() {
        return undoMenuItem;
    }
    public JMenuItem getUndoAllMenuItem() {
        return undoAllMenuItem;
    }
    public JMenuItem getHelpMenuItem() {
        return helpMenuItem;
    }
    public JMenuItem getAutoSolveMenuItem() {
        return autoSolveMenuItem;
    }
    public JMenuItem getShuffleMenuItem() {
        return shuffleMenuItem;
    }
    public JMenuItem getQuitMenuItem() {
        return quitMenuItem;
    }
    public JMenuItem getAboutMenuItem() {
        return aboutMenuItem;
    }
    public JMenuItem getResetMenuItem() { return resetMenuItem;}
    public JMenuItem getShowImageMenuItem() {
        return showImageMenuItem;
    }
    public JFileChooser getImageChooser() {
        return imageChooser;
    }
    public JMenuItem getChooseImageMenuItem() {
        return  chooseImageMenuItem;
    }
    public int getSelectedOption() {
        return selectedOption;
    }

    // ----------------------------------------------------------------------------------
    // setters
    // ----------------------------------------------------------------------------------
    public void setComplexityText(int val) {

        complexityText.setText("Complexity: " + val);
    }
    public void setBgImg(BufferedImage img) {
        wholeImage.setIcon(new ImageIcon(img));
        bgImgFrame.setSize(img.getWidth(), img.getHeight());
        bgImgFrame.add(wholeImage);
    }

    // ----------------------------------------------------------------------------------
    // methods
    // ----------------------------------------------------------------------------------
    public void showWholeImage() {
        bgImgFrame.setVisible(true);
    }
    public void hideWholeImage() {
        bgImgFrame.setVisible(false);
    }
    public void updateNumberOfMoves(int val) {

        // freezes on this line
        numOfMovesText.setText("   |    Moves so far: " + val);
    }
    public void displayFrame() {
        frame.setVisible(true);
    }
    public void hideFrame() {
        frame.setVisible(false);
    }
    public void switchImages(Image img1, Image img2,
                                                            int from, int to) {

        Image temp = img1;
        buttons[to].setIcon(new ImageIcon(img2));
        buttons[from].setIcon(new ImageIcon(temp));
    }

    // update the image on the button
    public void updateButtonImage(BufferedImage img, int index) {
        buttons[index].setIcon(new ImageIcon(img));
    }

    // returns the button at given index
    public JButton getButtonAtIndex(int index) {
        return buttons[index];
    }

    // sets the images on the buttons according to board array
    public void setButtonsImages(List <BoardPiece> board) {
            int freeSpot = -1;

        for (int i = 0; i < board.size(); ++i) {
            buttons[i].setIcon(new ImageIcon(board.get(i).getImage()));
            buttons[i].setBorder(BorderFactory.createEmptyBorder());
            if (board.get(i).getPosition() == 15)
                freeSpot = i;
        }

        buttons[freeSpot].setBorder(BorderFactory.createLineBorder(Color.BLUE, 8));
    }

    // updates the gui free spot and returns the index of updated piece on the board
    public int updateFreeSpot(List <BoardPiece> board) {

        int freeSpot = 0;
        for (int i = 0; i < board.size(); ++i) {
            buttons[i].setBorder(BorderFactory.createEmptyBorder());
            if (board.get(i).getPosition() == 15) {
                freeSpot = i;
            }
        }

        // update free spot
        buttons[freeSpot].setBorder(BorderFactory.createLineBorder(Color.BLUE, 8));
        return freeSpot;
    }

    public void showImageChooser() {
        selectedOption = imageChooser.showOpenDialog(frame);
    }

}   //  end of GUI class

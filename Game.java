// Michal Bochnak, Netid: mbochn2
// CS 342 Project #2 - Fifteen Puzzle
// 10/4/2017
// UIC, Pat Troy
//
// Game.java
//

//
// import libraries
//
import javax.swing.*;
import java.awt.event.*;
import javax.imageio.ImageIO;
import java.io.*;


//
// Contains main function, Instantiates the board and all related game
// elements such as GUI etc. Contains ActionListener to handle
// the change of the board image read in by user
//
public class Game {

    // grab and cut the image
    static BackgroundImage bgImg = new BackgroundImage();
    // build game board based on the above image
    static Board board = new Board(bgImg);

    public static void main(String[] args) throws IOException {

        // dd listener to image chooser to allow changing the image
        board.getBoardGUI().getImageChooser().
                            addActionListener(new ImageChooserListener());

        // show the GUI
        board.getBoardGUI().displayFrame();
    }

    //
    // Listener for MenuOption which allows to change the image
    // build in new board based on image that was read in
    //
    private static class ImageChooserListener implements ActionListener {

        public void actionPerformed (ActionEvent event) {

            // ok selected, read new image
            if (board.getBoardGUI().getSelectedOption() == JFileChooser.APPROVE_OPTION) {
                File f = board.getBoardGUI().getImageChooser().getSelectedFile();
                try {
                    FileInputStream fileStream = new FileInputStream(f);
                    bgImg = new BackgroundImage(ImageIO.read(fileStream));
                    // cleat out old board
                    board.getBoardGUI().hideFrame();
                    board.getBoardGUI().hideWholeImage();
                    // instantiate new board
                    board = new Board(bgImg);
                    board.getBoardGUI().displayFrame();
                    board.setUserImageChoosen(true);
                } catch (Exception ex) {
                    System.out.println(ex);
                }
            }
            else {
                System.out.println("Cancel selected");
            }
        }
    }

}   //  end of Game class
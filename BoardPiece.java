// Michal Bochnak, Netid: mbochn2
// CS 342 Project #2 - Fifteen Puzzle
// 10/4/2017
// UIC, Pat Troy
//
// BoardPiece.java
//

//
// import libraries
//
import java.awt.*;
import java.awt.image.BufferedImage;


public class BoardPiece {

    private BufferedImage img;
    // position in solved puzzles
    private int boardPos;


    // contructor
    BoardPiece (BufferedImage image, int position) {
        img = image;
        boardPos = position;
    }


    // ----------------------------------------------------------------------------------
    // getters
    // ----------------------------------------------------------------------------------

    // returns the position of the piece in the solved puzzles
    public int getPosition() {
        return boardPos;
    }

    // returns the image
    public Image getImage() {
        return img;
    }


}   //  end of BoardPiece class
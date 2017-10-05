// Michal Bochnak, Netid: mbochn2
// CS 342 Project #2 - Fifteen Puzzle
// 10/4/2017
// UIC, Pat Troy
//
// BackgroundImage.java
//

//
// import libraries
//
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;

import static java.lang.System.exit;


//
// instance of the image used to create the board
// keeps the whole image and pieces
//
public class BackgroundImage {

    // provate members
    private BufferedImage wholeImg;
    private BufferedImage piecesImgs[];
    private int cols ;
    private int rows ;
    private int pieces;
    private int pieceW ;
    private int pieceH;

    // constructor
    BackgroundImage() {

        // get the image
        try {
            File f = new File("puzzle.jpg");
            FileInputStream fileStream = new FileInputStream(f);
            wholeImg = ImageIO.read(fileStream);
        } catch (Exception ex) {
            System.out.println(ex);
        }

        if (wholeImg == null) {
            System.out.println("error, 'puzzle.jpg' image not found");
            exit(-1);
        }

        // resize if too big
        if(wholeImg.getHeight() > 750 || wholeImg.getWidth() > 750)
            resize();

        // columns and rows for cutting, determine the size of pieces
        cols = 4;
        rows = 4;
        pieces = cols * rows;
        pieceW = wholeImg.getWidth() / cols;
        pieceH = wholeImg.getHeight() / rows;

        // instantiate the array and slice the image
        piecesImgs = new BufferedImage[pieces];
        sliceImage();
    }

    BackgroundImage(BufferedImage image) {

      wholeImg = image;

      // resize if too big
        if(wholeImg.getHeight() > 750 || wholeImg.getWidth() > 750)
            resize();

        // columns and rows for cutting, determine the size of pieces
        cols = 4;
        rows = 4;
        pieces = cols * rows;
        pieceW = wholeImg.getWidth() / cols;
        pieceH = wholeImg.getHeight() / rows;

        // initialize the array and slice the image
        piecesImgs = new BufferedImage[pieces];
        sliceImage();
    }


    // ----------------------------------------------------------------------------------
    // getters
    // ----------------------------------------------------------------------------------
    public BufferedImage getWholeImg() {
        return wholeImg;
    }
    // returns the image at requested index
    public BufferedImage getImageAtIndex(int index) {

        return piecesImgs[index];
    }


    // ----------------------------------------------------------------------------------
    // methods
    // ----------------------------------------------------------------------------------

    // slices the image and save them
    private void sliceImage() {

        int count = 0;  // spot in array

        for (int i = 0; i < rows; ++i) {
            for (int j = 0; j < cols; ++j) {
                //Initialize the image array with image chunks
                piecesImgs[count] = new BufferedImage(pieceW, pieceH, wholeImg.getType());

                // draws the image chunk
                Graphics2D gr = piecesImgs[count++].createGraphics();
                gr.drawImage(wholeImg, 0, 0, pieceW, pieceH, pieceW* j,
                                            pieceH* i, pieceW * j + pieceW, pieceH* i +
                                            pieceH, null);
                gr.dispose();
            }
        }

        // resize and write the small images to the files
        for (int i = 0; i < piecesImgs.length; ++i) {

            try {
                ImageIO.write(piecesImgs[i], "jpg",
                                                new File("image" + i + ".jpg"));
            } catch (Exception ex) {
                System.out.println(ex);
            }
        }
    }

    // resize the image to 750 x 750
    public void resize() {
        Image img = wholeImg.getScaledInstance
                (750, 750, Image.SCALE_DEFAULT);

        BufferedImage scaledImg = new BufferedImage(img.getWidth(null),
                img.getHeight(null), BufferedImage.TYPE_INT_ARGB );

        // draw the image
        Graphics2D temp = scaledImg.createGraphics();
        temp.drawImage(img, 0, 0, null);
        temp.dispose();

        wholeImg = scaledImg;
    }
    
}   //  end of BackgroundImage class

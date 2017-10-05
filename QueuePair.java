// Michal Bochnak, Netid: mbochn2
// CS 342 Project #2 - Fifteen Puzzle
// 10/4/2017
// UIC, Pat Troy
//
// QueuePair.java
//

//
// import libraries
//
import java.util.ArrayList;
import java.util.List;


//
// Pair that is used for Queue when doing Breath first search
public class QueuePair {

    // private members
    private List<BoardPiece> board;
    private List<Integer> movements;

    private String dupa[];

    // constructors
    QueuePair() {
        board = new ArrayList<BoardPiece>();
        movements = new ArrayList<Integer>();
    }

    QueuePair(List<BoardPiece> arrangement, List<Integer> moves) {
        board = arrangement;
        movements = moves;
    }


    // ----------------------------------------------------------------------------------
    // getters
    // ----------------------------------------------------------------------------------
    public List<Integer> getMovements() {
        return movements;
    }

    // ----------------------------------------------------------------------------------
    // methods
    // ----------------------------------------------------------------------------------
    public boolean solved() {

        for (int i = 0; i < board.size(); ++i)
            if (board.get(i).getPosition() != i)
                return false;

        return true;
    }

    // Update board and adds the movement
    public void UpdateBoardAddMovement(List<BoardPiece> b, int m) {
        board = b;
        if (m != -1) {
            movements.add(m);
        }
    }

    // returns possible board arrangement that could be created
    // by one move from the current board
    public List<List<BoardPiece>> possibleArrangements() {

        List<List<BoardPiece>> possibleArrangements = new ArrayList<List<BoardPiece>>();
        int freeSpot = findFreeSpot();

        // check for the left move, add arrangement if moveValid
        if (freeSpot - 1 >= 0 ) {
            possibleArrangements.add(generateArrangement(freeSpot, freeSpot-1));
        }
        // check for the right move, add arrangement if moveValid
        if (freeSpot + 1 <= 15 ) {
            possibleArrangements.add(generateArrangement(freeSpot, freeSpot+1));
        }
        // check for the top move, add arrangement if moveValid
        if (freeSpot -4 >= 0 ) {
            possibleArrangements.add(generateArrangement(freeSpot, freeSpot-4));
        }
        // check for the down move, add arrangement if moveValid
        if (freeSpot + 4 <= 15 ) {
            possibleArrangements.add(generateArrangement(freeSpot, freeSpot+4));
        }

        return possibleArrangements;
    }

    // generate possible board arrangement
    public List<BoardPiece> generateArrangement(int freeSpot, int newSpot) {
        List<BoardPiece> tempArrangement = new ArrayList<BoardPiece>(board);

        // switch the pieces in the tempArrangement
        BoardPiece tempPiece = tempArrangement.get(freeSpot);
        tempArrangement.set(freeSpot, tempArrangement.get(newSpot));
        tempArrangement.set(newSpot, tempPiece);

        return tempArrangement;
    }

    public int findFreeSpot() {

        for (int i = 0; i < board.size(); ++i) {
            if (board.get(i).getPosition() == 15)
                return i;
        }

        return -1;
    }

}   // end of QueuePair class

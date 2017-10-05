// Michal Bochnak, Netid: mbochn2
// CS 342 Project #2 - Fifteen Puzzle
// 10/4/2017
// UIC, Pat Troy
//
// SetPair.java
//

//
// import libraries
//
import java.util.List;


//
// Pair that is inserted into Set while doing Breath first search
//
public class SetPair {

    // private members
    private List<BoardPiece> board;
    private boolean visited;

    // constructor
    SetPair(List<BoardPiece> b, boolean v) {
        board = b;
        visited = v;
    }

}
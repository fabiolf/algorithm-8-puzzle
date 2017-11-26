import java.util.Arrays;

import edu.princeton.cs.algs4.Queue;

/*
 * Class Board
 * Represents a board for the 8-puzzle game in a specific arrangement of tiles
 * Author: Fabio Fonseca (fabio.l.fonseca@gmail.com)
 *         (using skeleton from Coursera Algorithms part I week 4 programming assignment)
 */

public final class Board {
    private final int[][] mBlocks;

    public Board(int[][] blocks) {
        // construct a board from an n-by-n array of blocks
        // (where blocks[i][j] = block in row i, column j)
        mBlocks = new int[blocks.length][blocks[0].length];
        for (int i = 0; i < blocks.length; i++) {
            for (int j = 0; j < blocks[0].length; j++) {
                mBlocks[i][j] = blocks[i][j];
            }
        }
    }

    public int dimension() {
        // board dimension n
        return mBlocks.length;
    }

    public int hamming() {
        // number of blocks out of place
        int boardDimension = mBlocks.length;
        int count = 0;
        for (int i = 0; i < boardDimension; i++) {
            for (int j = 0; j < boardDimension; j++) {
                if (mBlocks[i][j] != 0 && mBlocks[i][j] != i*boardDimension + j + 1)
                    count++;
            }
        }
        return count;
    }

    public int manhattan() {
        // sum of Manhattan distances between blocks and goal
        // calculate position each tile should be
        // manhattan equals to should-be-position less current position
        int boardDimension = mBlocks.length;
        int sum = 0;
        for (int i = 0; i < boardDimension; i++) {
            for (int j = 0; j < boardDimension; j++) {
                if (mBlocks[i][j] != 0) {
                    int iCorrect = (mBlocks[i][j]-1) / boardDimension;
                    int jCorrect = (mBlocks[i][j]-1) % boardDimension;
                    sum += Math.abs(iCorrect - i) + Math.abs(jCorrect - j);
                }
            }
        }
        return sum;
    }

    public boolean isGoal() {
        // is this board the goal board?
        return (hamming() == 0);
    }

    private int[][] copy(int[][] a) {
        int[][] copy = new int[a.length][a.length];
        for (int i = 0; i < a.length; i++)
            for (int j = 0; j < a.length; j++)
                copy[i][j] = a[i][j];
        return copy;
    }
    
    public Board twin() {
        // a board that is obtained by exchanging any pair of blocks
        // a criteria would be to iterate horizontally and then vertically over the
        // board to find the first two tiles non blank and swap them
        
        // create a copy of the board
        int[][] auxBlocks = copy(mBlocks);
        
        for (int i = 0; i < auxBlocks.length; i++) {
            for (int j = 0; j < auxBlocks.length; j++) {
                if (isValid(auxBlocks, i, j) && isValid(auxBlocks, i, j+1)) {
                    int aux = auxBlocks[i][j];
                    auxBlocks[i][j] = auxBlocks[i][j+1];
                    auxBlocks[i][j+1] = aux;
                    return (new Board(auxBlocks));
                }
            }
        }
        return null;
    }

    private boolean isValid(int[][] auxBlocks, int i, int j) {
        if (i >= 0 && i <= auxBlocks.length-1 && j >= 0 && j <= auxBlocks.length-1)
            if (auxBlocks[i][j] != 0)
                return true;
        return false;
    }

    public boolean equals(Object y) {
        // does this board equal y?
        if (y == null)
            return false;
        if (this == y)
            return true;
        if (this.getClass() != y.getClass())
            return false;
        if (mBlocks == null)
            if (((Board) y).mBlocks == null)
                return true;
//            else
//                return false;
        return Arrays.deepEquals(this.mBlocks, ((Board) y).mBlocks);
    }

    public Iterable<Board> neighbors() {
        // all neighboring boards
        // neighbors are all board that an be obtained if we make 1 movement
        // basically swapping the blank tile with all its four neighbors if the neighbor is in the board (negative and out of bounds indexes are not allowed)

        int boardDimension = mBlocks.length;
        // first find the blank tile
        int iBlank = -1, jBlank = -1;
        for (int i = 0; i < boardDimension; i++) {
            for (int j = 0; j < boardDimension; j++) {
                if (mBlocks[i][j] == 0) {
                    iBlank = i;
                    jBlank = j;
                    break;
                }
            }
        }

        // now create a stack to keep all neighbors
        Queue<Board> neighbors = new Queue<Board>();

        // compute all neighbors (4 maximum and at least 2)
        if (iBlank > 0) { // then we can swap it to its upper neighbor (and then return the tile back to its original place)
            int[][] auxBlocks = copy(mBlocks);
            auxBlocks[iBlank][jBlank] = auxBlocks[iBlank-1][jBlank];
            auxBlocks[iBlank-1][jBlank] = 0;
            neighbors.enqueue(new Board(auxBlocks));
        }
        if (iBlank < mBlocks.length-1) { // then we can swap it to its lower neighbor (and then return the tile back to its original place)
            int[][] auxBlocks = copy(mBlocks);
            auxBlocks[iBlank][jBlank] = auxBlocks[iBlank+1][jBlank];
            auxBlocks[iBlank+1][jBlank] = 0;
            neighbors.enqueue(new Board(auxBlocks));
        }
        if (jBlank > 0) { // then we can swap it to its left neighbor (and then return the tile back to its original place)
            int[][] auxBlocks = copy(mBlocks);
            auxBlocks[iBlank][jBlank] = auxBlocks[iBlank][jBlank-1];
            auxBlocks[iBlank][jBlank-1] = 0;
            neighbors.enqueue(new Board(auxBlocks));
        }
        if (jBlank < mBlocks.length-1) { // then we can swap it to its right neighbor (and then return the tile back to its original place)
            int[][] auxBlocks = copy(mBlocks);
            auxBlocks[iBlank][jBlank] = auxBlocks[iBlank][jBlank+1];
            auxBlocks[iBlank][jBlank+1] = 0;
            neighbors.enqueue(new Board(auxBlocks));
        }
        return neighbors;
    }

    public String toString() {
        // string representation of this board (in the output format specified below)
        int boardDimension = dimension();

        StringBuilder output = new StringBuilder();
        output.append(Integer.toString(boardDimension) + "\n");
        for (int i = 0; i < boardDimension; i++) {
            for (int j = 0; j < boardDimension; j++)
                output.append(String.format("%2d ", mBlocks[i][j]));
            output.append("\n");
        }
        return output.toString();
    }
    
    public static void main(String[] args) {
        // unit tests
/*
        Board board = new Board(new int[][]{{1, 2, 3}, {4, 5, 6}, {7, 8, 0}});
        System.out.println("====================\nBoard1\n".concat(board.toString()));
        System.out.println("dimension should be 3: [" + (board.dimension() == 3 ? "OK" : "FAIL - " + board.dimension()) + "]");
        System.out.println("hamming should be 0: [" + (board.hamming() == 0 ? "OK" : "FAIL - " + board.hamming()) + "]");
        System.out.println("manhattan should be 0: [" + (board.manhattan() == 0 ? "OK" : "FAIL - " + board.manhattan()) + "]");
        System.out.println("isGoal should be true: [" + (board.isGoal() ? "OK" : "FAIL - " + board.isGoal()) + "]");
        System.out.println("Printing neighbors:");
        int nCount = 0;
        for (Board b : board.neighbors()) {
            System.out.println(b.toString());
            nCount++;
        }
        System.out.println("Total neighbors should be 2: [" + (nCount == 2 ? "OK" : "FAIL - " + nCount) + "]");
        System.out.println("Printing one twin:");
        System.out.println(board.twin().toString());

        Board board2 = new Board(new int[][]{{1, 3, 2}, {6, 5, 4}, {0, 8, 7}});
        System.out.println("====================\nBoard2\n".concat(board2.toString()));
        System.out.println("dimension should be 3: [" + (board2.dimension() == 3 ? "OK" : "FAIL - " + board2.dimension()) + "]");
        System.out.println("hamming should be 5: [" + (board2.hamming() == 5 ? "OK" : "FAIL - " + board2.hamming()) + "]");
        System.out.println("manhattan should be 8: [" + (board2.manhattan() == 8 ? "OK" : "FAIL - " + board2.manhattan()) + "]");
        System.out.println("isGoal should be false: [" + (!board2.isGoal() ? "OK" : "FAIL - " + board2.isGoal()) + "]");
        System.out.println("Printing neighbors:");
        nCount = 0;
        for (Board b : board2.neighbors()) {
            System.out.println(b.toString());
            nCount++;
        }
        System.out.println("Total neighbors should be 2: [" + (nCount == 2 ? "OK" : "FAIL - " + nCount) + "]");
        System.out.println("Printing one twin:");
        System.out.println(board2.twin().toString());

        Board board3 = new Board(new int[][]{{1, 2, 3, 4}, {5, 6, 7, 8}, {9, 10, 11, 12}, {13, 14, 15, 0}});
        System.out.println("====================\nBoard3\n".concat(board3.toString()));
        System.out.println("dimension should be 4: [" + (board3.dimension() == 4 ? "OK" : "FAIL - " + board3.dimension()) + "]");
        System.out.println("hamming should be 0: [" + (board3.hamming() == 0 ? "OK" : "FAIL - " + board3.hamming()) + "]");
        System.out.println("manhattan should be 0: [" + (board3.manhattan() == 0 ? "OK" : "FAIL - " + board3.manhattan()) + "]");
        System.out.println("isGoal should be true: [" + (board3.isGoal() ? "OK" : "FAIL - " + board3.isGoal()) + "]");
        System.out.println("Printing neighbors:");
        nCount = 0;
        for (Board b : board3.neighbors()) {
            System.out.println(b.toString());
            nCount++;
        }
        System.out.println("Total neighbors should be 2: [" + (nCount == 2 ? "OK" : "FAIL - " + nCount) + "]");
        System.out.println("Printing one twin:");
        System.out.println(board3.twin().toString());

        Board board4 = new Board(new int[][]{{1, 2, 3}, {4, 0, 5}, {6, 7, 8}});
        System.out.println("====================\nBoard4\n".concat(board4.toString()));
        System.out.println("dimension should be 3: [" + (board4.dimension() == 3 ? "OK" : "FAIL - " + board4.dimension()) + "]");
        System.out.println("hamming should be 4: [" + (board4.hamming() == 4 ? "OK" : "FAIL - " + board4.hamming()) + "]");
        System.out.println("manhattan should be 6: [" + (board4.manhattan() == 6 ? "OK" : "FAIL - " + board4.manhattan()) + "]");
        System.out.println("isGoal should be false: [" + (!board4.isGoal() ? "OK" : "FAIL - " + board4.isGoal()) + "]");
        System.out.println("Printing neighbors:");
        nCount = 0;
        for (Board b : board4.neighbors()) {
            System.out.println(b.toString());
            nCount++;
        }
        System.out.println("Total neighbors should be 4: [" + (nCount == 4 ? "OK" : "FAIL - " + nCount) + "]");
        System.out.println("Printing one twin:");
        System.out.println(board4.twin().toString());

        Board board5 = new Board(new int[][]{{0, 2, 3}, {4, 5, 1}, {6, 7, 8}});
        System.out.println("====================\nBoard5\n".concat(board5.toString()));
        System.out.println("dimension should be 3: [" + (board5.dimension() == 3 ? "OK" : "FAIL - " + board5.dimension()) + "]");
        System.out.println("hamming should be 3: [" + (board5.hamming() == 3 ? "OK" : "FAIL - " + board5.hamming()) + "]");
        System.out.println("manhattan should be 5: [" + (board5.manhattan() == 5 ? "OK" : "FAIL - " + board5.manhattan()) + "]");
        System.out.println("isGoal should be false: [" + (!board5.isGoal() ? "OK" : "FAIL - " + board5.isGoal()) + "]");
        System.out.println("Printing neighbors:");
        nCount = 0;
        for (Board b : board5.neighbors()) {
            System.out.println(b.toString());
            nCount++;
        }
        System.out.println("Total neighbors should be 3: [" + (nCount == 3 ? "OK" : "FAIL - " + nCount) + "]");
        System.out.println("Printing one twin:");
        System.out.println(board5.twin().toString());

        Board board6 = new Board(new int[][]{{1, 0}, {2, 3}});
        System.out.println("====================\nBoard6\n".concat(board6.toString()));
        System.out.println("dimension should be 2: [" + (board6.dimension() == 2 ? "OK" : "FAIL - " + board6.dimension()) + "]");
        System.out.println("hamming should be 2: [" + (board6.hamming() == 2 ? "OK" : "FAIL - " + board6.hamming()) + "]");
        System.out.println("manhattan should be 3: [" + (board6.manhattan() == 3 ? "OK" : "FAIL - " + board6.manhattan()) + "]");
        System.out.println("isGoal should be false: [" + (!board6.isGoal() ? "OK" : "FAIL - " + board6.isGoal()) + "]");
        System.out.println("Printing neighbors:");
        nCount = 0;
        for (Board b : board6.neighbors()) {
            System.out.println(b.toString());
            nCount++;
        }
        System.out.println("Total neighbors should be 2: [" + (nCount == 2 ? "OK" : "FAIL - " + nCount) + "]");
        System.out.println("Printing one twin:");
        System.out.println(board6.twin().toString());

        System.out.println("====================\nTesting equals():");
        System.out.println("Board 1 equals to Board 1? Should be true: [" + (board.equals(board) ? "OK" : "FAIL - " + board.equals(board)) + "]");
        System.out.println("Board 1 equals to Board 2? Should be false: [" + (!board.equals(board2) ? "OK" : "FAIL - " + board.equals(board2)) + "]");
        System.out.println("Board 1 equals to Board 3? Should be false: [" + (!board.equals(board3) ? "OK" : "FAIL - " + board.equals(board3)) + "]");
        System.out.println("Board 3 equals to Board 3? Should be true: [" + (board3.equals(board3) ? "OK" : "FAIL - " + board3.equals(board3)) + "]");
        System.out.println("Finished!");
*/
    }
}
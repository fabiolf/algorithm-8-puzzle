import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

public class Solver {
    private final boolean mSolvable;
//    private final int mMoves;
    private Stack<Board> mBoards = null;
    
    private class Node implements Comparable<Node> {

        private final Board mBoard;
        private final int mPriority;
        private final int mMoves;
        private final int mManhattan;
        private final Node mPredecessor;
        
//        public Node(Board board, int moves, Node predecessor) {
        public Node(Board board, Node predecessor) {
            mBoard = board;
            if (predecessor == null)
                mMoves = 0;
            else
                mMoves = predecessor.getMoves() + 1;
//            mMoves = moves;
            mManhattan = board.manhattan();
            mPredecessor = predecessor;
            mPriority = mManhattan + mMoves;
        }
        
        public int getPriority() {
            return mPriority;
        }
        
//        public int getManhattan() {
//            return mManhattan;
//        }
//        
        public int getMoves() {
            return mMoves;
        }
        
        public Board getBoard() {
            return mBoard;
        }
        
        public Node getPredecessor() {
            return mPredecessor;
        }
        
        // returns -1 if it has lower priority than that, 1 if it has higher priority than that and 0 if priorities are the same
        public int compareTo(Node that) {
            if (mPriority > that.getPriority())
                return 1;
            if (mPriority < that.getPriority())
                return -1;
            return 0;
        }
        
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("priority  = ").append(mPriority).append("\n");
            sb.append("moves     = ").append(mMoves).append("\n");
            sb.append("manhattan = ").append(mManhattan).append("\n");
            sb.append(mBoard.toString());
            return sb.toString();
        }
        
    }
    
    public Solver(Board initial) {
        // find a solution to the initial board (using the A* algorithm)
        if (initial == null)
            throw new java.lang.IllegalArgumentException("null argument received");
        
        boolean solvable = false;
        boolean solvableTwin = false;
        
//        int moves = 0;
//        int movesTwin = 0;
        // create required infrastructure
        MinPQ<Node> pq = new MinPQ<Node>();
        MinPQ<Node> pqTwin = new MinPQ<Node>();
        
        // first step - insert the initial board with 0 moves and null predecessor
//        pq.insert(new Node(initial, moves, null));
//        pqTwin.insert(new Node(initial.twin(), movesTwin, null));
        pq.insert(new Node(initial, null));
        pqTwin.insert(new Node(initial.twin(), null));
        
        // second step - loop through the following steps:
        Node node;
        Node nodeTwin;
//        int step = 0;
        do {
            // DEBUG
//            System.out.println("STEP " + step);
//            for (Node n : pq)
//                System.out.println(n.toString());
//            step++;
            // - remove the node with minimum priority
            node = pq.delMin();
//            System.out.println("Priority = ".concat(Integer.toString(node.getPriority())));
//            System.out.println("Moves = ".concat(Integer.toString(node.getMoves())));
//            System.out.println(node.getBoard().toString());
//            visitedNodes.push(node);
            if (node.getBoard().isGoal()) {
                // - if it is the goal board (use isGoal(), populate the Solution queue and do nothing more as the exit condition is now true
                solvable = true;
                // initialize the mBoards member
                mBoards = new Stack<Board>();
                mBoards.push(node.getBoard());
                while (node.getPredecessor() != null) {
                    node = node.getPredecessor();
                    mBoards.push(node.getBoard());
                }
            } else {
                // - increment the number of moves
//                moves++;
                // - calculate the neighbors
                for (Board board : node.getBoard().neighbors()) {
                    // - insert the neighbors (as nodes) in the PQ
                    if (node.getPredecessor() != null)
                        if (board.equals(node.getPredecessor().getBoard()))
                            continue;
                    // if the neighbor is equal to any other board already in the Queue, part of the solution, skip it to avoid solution loops
//                    boolean found = false;
//                    for (Node visitedNode : visitedNodes)
//                        if (board.equals(visitedNode.getBoard())) {
//                            found = true;
//                            break;
//                        }
//                    if (!found) 
                    pq.insert(new Node(board, node));
                }
            }
            // - repeat until the dequeued move represents the goal board
            // do the same for the Twin board
            nodeTwin = pqTwin.delMin();
            if (nodeTwin.getBoard().isGoal())
                // - if it is the goal board (use isGoal(), populate the Solution queue and do nothing more as the exit condition is now true
                solvableTwin = true;
            else {
                // - increment the number of moves
//                movesTwin++;
                // - calculate the neighbors
                for (Board boardTwin : nodeTwin.getBoard().neighbors()) {
                    // - insert the neighbors (as nodes) in the PQ
                    if (nodeTwin.getPredecessor() != null)
                        if (boardTwin.equals(nodeTwin.getPredecessor().getBoard()))
                            continue;
                    pqTwin.insert(new Node(boardTwin, nodeTwin));
                }
            }
            // - repeat until the dequeued move represents the goal board
        } while (!solvable && !solvableTwin);
        mSolvable = solvable;
    }

    public boolean isSolvable() {
        // is the initial board solvable?
        return mSolvable;
    }

    public int moves() {
        // min number of moves to solve initial board; -1 if unsolvable
    	if (!mSolvable)
    		return -1;
    	if (mBoards != null)
    		return mBoards.size() == 0 ? 0 : mBoards.size()-1;
    	return 0;
    }

    public Iterable<Board> solution() {
        // sequence of boards in a shortest solution; null if unsolvable
        return mBoards;
    }

    public static void main(String[] args) {
        // solve a slider puzzle (given below)
        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] blocks = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }
}
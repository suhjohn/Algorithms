import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

public class Solver {
    // find a solution to the initial board (using the A* algorithm)
    private int totalMove = 0;
    private SearchNode LastNode = null;
    private boolean solvable = false;

    private class SearchNode implements Comparable<SearchNode> {
        private final Board board;
        private final SearchNode prevNode;
        private final int move, manhattan, priority;

        private SearchNode(Board board, SearchNode prevNode) {
            this.board = board;
            this.prevNode = prevNode;
            this.move = prevNode != null ? prevNode.move + 1 : 0;
            // Second opotimization
            this.manhattan = board.manhattan();
            this.priority = this.manhattan + this.move;
        }

        @Override
        public int compareTo(SearchNode that) {
            int comp = Integer.compare(this.priority, that.priority);
            if (comp == 0) comp = Integer.compare(this.manhattan, that.manhattan);
//            if (comp == 0) comp = Integer.compare(this.board.hamming(), that.board.hamming());
            return comp;
        }
    }

    public Solver(Board initial) {
        // Define a Search node and twin Search node.
        SearchNode initialNode = new SearchNode(initial, null);
        SearchNode initialNodeTwin = new SearchNode(initial.twin(), null);
        // Insert the initial search node (the initial board, 0 moves, and a null predecessor search node) into Heap.
        MinPQ<SearchNode> queue = new MinPQ<>();
        MinPQ<SearchNode> twinQueue = new MinPQ<>();
        queue.insert(initialNode);
        twinQueue.insert(initialNodeTwin);

        // Repeat the following procedure until the search node dequeued corresponds to a goal board:
        //      Delete from the priority queue the search node with the minimum priority,
        //      and insert onto the priority queue all neighboring search nodes
        //      (those that can be reached in one move from the dequeued search node).
        SearchNode curr = queue.delMin();
        SearchNode twinCurr = twinQueue.delMin();
        while (!twinCurr.board.isGoal() && !curr.board.isGoal()) {
            for (Board neighbor : curr.board.neighbors()) {
                // Critical optimization
                if (curr.prevNode != null && neighbor.equals(curr.prevNode.board)) {
                    continue;
                }
                SearchNode neighborNode = new SearchNode(neighbor, curr);
                queue.insert(neighborNode);
            }
            for (Board neighbor : twinCurr.board.neighbors()) {
                SearchNode neighborNode = new SearchNode(neighbor, twinCurr);
                twinQueue.insert(neighborNode);
            }

            curr = queue.delMin();
            twinCurr = twinQueue.delMin();
        }
        if (curr.board.isGoal()) {
            this.solvable = true;
            this.LastNode = curr;
        }
    }


    // is the initial board solvable?
    public boolean isSolvable() {
        return this.solvable;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        if (!this.solvable) return -1;
        return this.LastNode.move;
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        if (!this.solvable) return null;

        Stack<Board> solutionBoards = new Stack<>();
        SearchNode curr = this.LastNode;
        while (curr != null) {
            solutionBoards.push(curr.board);
            curr = curr.prevNode;
        }
        return solutionBoards;
    }

    // solve a slider puzzle (given below)
    public static void main(String[] args) {

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

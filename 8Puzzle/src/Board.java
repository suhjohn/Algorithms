import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.StdRandom;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;

/****
 *
 * Corner cases.
 * You may assume that the constructor receives an n-by-n array
 *      containing the n2 integers between 0 and n2 − 1, where 0 represents the blank square.
 *
 * Performance requirements.
 * Your implementation should support all Board methods in time proportional to n2 (or better) in the worst case.
 *
 * To reduce unnecessary exploration of useless search nodes,
 *       don't enqueue a neighbor if its board is the same as the board of the predecessor search node.
 *
 * To avoid recomputing the Manhattan priority of a search node from scratch each time during various priority queue operations,
 * pre-compute its value when you construct the search node; save it in an instance variable;
 *      and return the saved value as needed.
 *
 * Your program should work correctly for arbitrary n-by-n boards (for any 2 ≤ n < 128),
 *      even if it is too slow to solve some of them in a reasonable amount of time.
 */

public class Board {
    private int[][] board;
    private int[][] goalBoard;
    private int blankI;
    private int blankJ;
    private int n;

    // construct a board from an n-by-n array of blocks
    // (where blocks[i][j] = block in row i, column j)
    public Board(int[][] blocks) {
        this.board = blocks;
        this.n = blocks[0].length;

        this.goalBoard = new int[this.n][this.n];
        for (int i = 0; i < this.n; i++) {
            for (int j = 0; j < this.n; j++) {
                if (this.board[i][j] == 0) {
                    this.blankI = i;
                    this.blankJ = j;
                }
                this.goalBoard[i][j] = (i * this.n) + j + 1;
            }
        }
        this.goalBoard[this.n - 1][this.n - 1] = 0;
    }


    // board dimension n
    public int dimension() {
        return this.n;
    }

    /**
     * The number of blocks in the wrong position
     *
     * @return hamming value
     */
    public int hamming() {
        int count = 0;
        for (int i = 0; i < this.n; i++) {
            for (int j = 0; j < this.n; j++) {
                if (this.goalBoard[i][j] != this.board[i][j]
                        && this.board[i][j] != 0) count++;
            }
        }
        return count;
    }

    /**
     * The sum of the Manhattan distances (sum of the vertical and horizontal distance)
     * from the blocks to their goal positions, plus the number of moves made so far to get to the search node.
     *
     * @return manhattan value
     */
    public int manhattan() {
        int total = 0;
        for (int i = 0; i < this.n; i++) {
            for (int j = 0; j < this.n; j++) {
                int curr = this.board[i][j];
                int goalI = (curr - 1) / this.n;
                int goalJ = (curr - 1) % this.n;
                if (this.board[i][j] != 0) {
                    total += Math.abs(i - goalI) + Math.abs(j - goalJ);
                }
            }
        }
        return total;
    }

    // is this board the goal board?
    public boolean isGoal() {
        return Arrays.deepEquals(this.board, this.goalBoard);
    }

    // a board that is obtained by exchanging any pair of blocks
    public Board twin() {
        int[][] twinBoardBlocks = new int[n][n];
        for (int i = 0; i < this.n; i++) {
            System.arraycopy(this.board[i], 0, twinBoardBlocks[i], 0, this.n);
        }

        int i = 0, j = 0, exchI = 0, exchJ = 0;
        if (twinBoardBlocks[0][0] != 0) {
            if (twinBoardBlocks[0][1] != 0) {
                exchJ = 1;
            } else {
                exchI = 1;
            }
        } else {
            j = 1;
            exchI = 1;
        }
        int temp = twinBoardBlocks[i][j];
        twinBoardBlocks[i][j] = twinBoardBlocks[exchI][exchJ];
        twinBoardBlocks[exchI][exchJ] = temp;
        return new Board(twinBoardBlocks);
    }

    // does this board equal y?
    public boolean equals(Object y) {
        Board compareBoard = (Board) y;
        boolean eq = true;
        for (int i = 0; i < this.n; i++) {
            for (int j = 0; j < this.n; j++) {
                if (compareBoard.board[i][j] != this.board[i][j]) {
                    eq = false;
                    break;
                }
            }
            if (!eq) break;
        }
        return eq;
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        return new Neighbors(this);
    }

    private class Neighbors implements Iterable<Board> {
        private Board currBoard;

        private Neighbors(Board currBoard) {
            this.currBoard = currBoard;
        }

        public Iterator<Board> iterator() {
            return new NeighborsIterator(this.currBoard);
        }

        private class NeighborsIterator implements Iterator<Board> {
            private ArrayList<Board> boardList = new ArrayList<>();
            private int i = 0;

            private NeighborsIterator(Board board) {
                if (isValidSquare("up", board.blankI, board.n)) {
                    this.addNeighborBoard(board, "up");
                }
                if (isValidSquare("down", board.blankI, board.n)) {
                    this.addNeighborBoard(board, "down");
                }
                if (isValidSquare("left", board.blankJ, board.n)) {
                    this.addNeighborBoard(board, "left");
                }
                if (isValidSquare("right", board.blankJ, board.n)) {
                    this.addNeighborBoard(board, "right");
                }
            }

            private boolean isValidSquare(String direction, int val, int n) {
                boolean validSquare = false;
                switch (direction) {
                    case "up":
                        validSquare = val - 1 >= 0;
                        break;
                    case "down":
                        validSquare = val + 1 < n;
                        break;
                    case "left":
                        validSquare = val - 1 >= 0;
                        break;
                    case "right":
                        validSquare = val + 1 < n;
                        break;
                }
                return validSquare;
            }

            private void addNeighborBoard(Board board, String direction) {
                int[][] neighborBoard = new int[board.n][board.n];
                for (int i = 0; i < board.n; i++) {
                    System.arraycopy(board.board[i], 0, neighborBoard[i], 0, board.n);
                }
                switch (direction) {
                    case "up": {
                        int toSwitch = board.board[board.blankI - 1][board.blankJ];
                        neighborBoard[board.blankI - 1][board.blankJ] = 0;
                        neighborBoard[board.blankI][board.blankJ] = toSwitch;
                        break;
                    }
                    case "down": {
                        int toSwitch = board.board[board.blankI + 1][board.blankJ];
                        neighborBoard[board.blankI + 1][board.blankJ] = 0;
                        neighborBoard[board.blankI][board.blankJ] = toSwitch;
                        break;
                    }
                    case "left": {
                        int toSwitch = board.board[board.blankI][board.blankJ - 1];
                        neighborBoard[board.blankI][board.blankJ - 1] = 0;
                        neighborBoard[board.blankI][board.blankJ] = toSwitch;
                        break;
                    }
                    case "right": {
                        int toSwitch = board.board[board.blankI][board.blankJ + 1];
                        neighborBoard[board.blankI][board.blankJ + 1] = 0;
                        neighborBoard[board.blankI][board.blankJ] = toSwitch;
                        break;
                    }
                }
                this.boardList.add(new Board(neighborBoard));
            }

            @Override
            public boolean hasNext() {
                return this.boardList.size() > this.i;
            }

            @Override
            public void remove() {
                this.boardList.remove(this.i);
                this.i--;
            }

            @Override
            public Board next() {
                Board curr = this.boardList.get(this.i);
                this.i++;
                return curr;
            }
        }
    }


    // string representation of this board (in the output format specified below)
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(this.n);
        stringBuilder.append("\n");
        for (int i = 0; i < this.n; i++) {
            stringBuilder.append(" ");
            for (int j = 0; j < this.n; j++) {
                stringBuilder.append(this.board[i][j]);
                stringBuilder.append("  ");
            }
            stringBuilder.append("\n");
        }
        return stringBuilder.toString();
    }

    // unit tests (not graded)
    public static void main(String[] args) {
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] blocks = new int[n][n];
        int curr = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                curr = in.readInt();
                blocks[i][j] = curr;
            }
        }
        Board initial = new Board(blocks);
        System.out.println("Initial Board");
        System.out.println(initial.toString());

        System.out.println("Initial twin board");
        System.out.println(initial.twin().toString());

        System.out.println("Initial GoalBoard");
        for (int i = 0; i < n; i++) {
            System.out.println(Arrays.toString(initial.goalBoard[i]));
        }

        for (Board neighbor:initial.neighbors()) {
            System.out.println("Neighbor");
            System.out.println(neighbor.toString());
        }

        boolean initialIsGoal = initial.isGoal();
        System.out.println("Initial is goal board? " + initialIsGoal);

        int hamming = initial.hamming();
        System.out.println("Initial Board hamming: " + hamming);

        int manhattan = initial.manhattan();
        System.out.println("Initial Board manhattan: " + manhattan);
    }
}

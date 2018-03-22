import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Queue;
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
    private final int[][] board;
    private int blankI;
    private int blankJ;
    private final int n;

    // construct a board from an n-by-n array of blocks
    // (where blocks[i][j] = block in row i, column j)
    public Board(int[][] blocks) {
        this.board = blocks;
        this.n = blocks[0].length;

        for (int i = 0; i < this.n; i++) {
            for (int j = 0; j < this.n; j++) {
                if (this.board[i][j] == 0) {
                    this.blankI = i;
                    this.blankJ = j;
                }
            }
        }
    }

    private int convert(int i, int j) {
        if (i == this.n - 1 && j == this.n - 1) return 0;
        return (i * this.n) + j + 1;
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
                if (this.board[i][j] != 0 && this.convert(i, j) != this.board[i][j]) count++;
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
        boolean isGoal = true;

        for (int i = 0; i < this.n; i++) {
            for (int j = 0; j < this.n; j++) {
                if (this.convert(i, j) != this.board[i][j]) {
                    isGoal = false;
                    break;
                }
            }
            if (!isGoal) break;
        }

        return isGoal;
    }

    // a board that is obtained by exchanging any pair of blocks
    public Board twin() {
        int[][] twinBoardBlocks = this.copyBoard();

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
        if (y == this) return true;
        if (y == null) return false;
        if (y.getClass() != this.getClass()) return false;
        Board compareBoard = (Board) y;
        if (this.n != compareBoard.n) return false;
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
        Queue<Board> neighbors = new Queue<>();
        // Up
        if (this.blankJ - 1 >= 0) this.enqueueNeighbor(neighbors, "up");
        // Right
        if (this.blankI + 1 < this.n) this.enqueueNeighbor(neighbors, "right");
        // Down
        if (this.blankJ + 1 < this.n) this.enqueueNeighbor(neighbors, "down");
        // Left
        if (this.blankI - 1 >= 0) this.enqueueNeighbor(neighbors, "left");
        return neighbors;
    }

    private void enqueueNeighbor(Queue<Board> neighbors, String direction) {
        int[][] copy = this.copyBoard();
        switch (direction) {
            case "up":
                this.exch(copy, this.blankI, this.blankJ, this.blankI, this.blankJ - 1);
                break;
            case "right":
                this.exch(copy, this.blankI, this.blankJ, this.blankI + 1, this.blankJ);
                break;
            case "down":
                this.exch(copy, this.blankI, this.blankJ, this.blankI, this.blankJ + 1);
                break;
            case "left":
                this.exch(copy, this.blankI, this.blankJ, this.blankI - 1, this.blankJ);
                break;
        }
        neighbors.enqueue(new Board(copy));
    }

    private int[][] copyBoard() {
        int[][] copy = new int[n][n];
        for (int i = 0; i < this.n; i++) {
            System.arraycopy(this.board[i], 0, copy[i], 0, this.n);
        }
        return copy;
    }

    private void exch(int[][] board, int i1, int j1, int i2, int j2) {
        int temp = board[i1][j1];
        board[i1][j1] = board[i2][j2];
        board[i2][j2] = temp;
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

        for (Board neighbor : initial.neighbors()) {
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

/*---------------------------------------------------------
 *  Author:        John Suh

 *---------------------------------------------------------*/


import edu.princeton.cs.algs4.WeightedQuickUnionUF;


public class Percolation {
    private final int n; // n by n grid n
    private int countOpen = 0; // number of open squares
    private WeightedQuickUnionUF grid; // main grid for percolation representation
    private final WeightedQuickUnionUF bottomSiteGrid; // backwash check grid
    private boolean[] boolGrid; // open true / false check grid
    private final int virtualTopSite; // virtual topsite that connects all the sites on the top row
    private final int virtualBottomSite; // virtual bottom site that connects all the sites on the bottom row

    public Percolation(int n) {
        if (n <= 0) throw new IllegalArgumentException();
        this.n = n;
        int gridSize = (int) (Math.pow(n, 2)) + 2;
        this.grid = new WeightedQuickUnionUF(gridSize);
        this.bottomSiteGrid = new WeightedQuickUnionUF(gridSize);
        this.boolGrid = new boolean[gridSize];
        this.virtualTopSite = gridSize - 2;
        this.virtualBottomSite = gridSize - 1;
        this.initializeSites();
    }

    private void initializeSites() {
        // form union between the top sites and keep track of bottom sites
        for (int i = 1; i <= this.n; i++) {
            this.grid.union(this.getIndex(1, i), this.virtualTopSite);
            this.bottomSiteGrid.union(this.getIndex(this.n, i), this.virtualBottomSite);
        }
    }

    private boolean isValidSite(int row, int col) {
        // Return True for row and col that are between indexes 0 ~ n, n exclusive
        return 0 < row && 0 < col && row <= this.n && col <= this.n;
    }

    private int getIndex(int row, int col) {
        // Returns the index that corresponds to the (row, col)
        return ((row - 1) * this.n) + col - 1;
    }

    /**
     * Establishes the connection between the current index and the adjacent index
     * Connects the index to the virtual bottom site if it is connected from the top and bottom
     */
    private void connect(int currIndex, int adjIndex, int adjRow, int adjCol) {
        if (this.isValidSite(adjRow, adjCol) && this.isOpen(adjRow, adjCol)
                && !this.grid.connected(currIndex, adjIndex)) {
            this.grid.union(currIndex, adjIndex);
            this.bottomSiteGrid.union(currIndex, adjIndex);
            // Backwash solution
            if (this.bottomSiteGrid.connected(adjIndex, this.virtualBottomSite)
                    && this.grid.connected(adjIndex, this.virtualTopSite))
                this.grid.union(adjIndex, this.virtualBottomSite);
        }
    }

    /**
     * Opens the site for the given row and col.
     *
     * Throws IllegalArgumentException if it is not a valid site.
     */
    public void open(int row, int col) {
        if (!this.isValidSite(row, col)) throw new IllegalArgumentException(row + " " + col);
        if (!this.isOpen(row, col)) {
            int index = this.getIndex(row, col);
            this.boolGrid[index] = true;
            this.countOpen++;
            if (this.n == 1) this.grid.union(index, this.virtualBottomSite); // Edge case

            int[][] adjacentRowCol = {{row - 1, col}, {row, col + 1}, {row + 1, col}, {row, col - 1}};
            for (int[] rowCol : adjacentRowCol) {
                int adjRow = rowCol[0];
                int adjCol = rowCol[1];
                int adjIndex = this.getIndex(adjRow, adjCol);
                this.connect(index, adjIndex, adjRow, adjCol);
            }
        }
    }

    public boolean isOpen(int row, int col) {
        if (!this.isValidSite(row, col)) throw new IllegalArgumentException();
        int index = this.getIndex(row, col);
        return this.boolGrid[index];
    }

    public boolean isFull(int row, int col) {
        if (!this.isValidSite(row, col)) throw new IllegalArgumentException(row + " " + col);
        int index = this.getIndex(row, col);
        return this.isOpen(row, col) && this.grid.connected(index, this.virtualTopSite);
    }

    public int numberOfOpenSites() {
        return this.countOpen;
    }

    public boolean percolates() {
        return this.grid.connected(this.virtualTopSite, this.virtualBottomSite);
    }

    public static void main(String[] args) {
        int n = 2;
        Percolation percolation = new Percolation(n);
        assert percolation.numberOfOpenSites() == 0;
        assert !percolation.percolates();

        // Test open and is open
        percolation.open(0, 0);
        assert percolation.numberOfOpenSites() == 1;
        assert percolation.isOpen(0, 0);
        assert percolation.isFull(0, 0);
        assert !percolation.isOpen(0, 1);
        assert !percolation.isOpen(1, 0);
        assert !percolation.isOpen(1, 1);
        assert !percolation.isFull(1, 1);

        percolation.open(1, 1);
        assert percolation.isOpen(1, 1);
        assert !percolation.isFull(1, 1);
    }
}

import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private final double[] percThresholds;
    private final int T;
    private Double mean;
    private Double stddev;

    // perform trials independent experiments on an n-by-n grid
    // throw a java.lang.IllegalArgumentException if either n ≤ 0 or trials ≤ 0.
    public PercolationStats(int n, int trials) {
        if (n < 0 || trials < 0) throw new IllegalArgumentException();

        this.T = trials;
        this.percThresholds = new double[trials];
        if (n <= 0 || trials <= 0) throw new IllegalArgumentException();
        for (int trial = 0; trial < trials; trial++) {
            Percolation perc = new Percolation(n);
            while (!perc.percolates()) {
                int row = StdRandom.uniform(n) + 1;
                int col = StdRandom.uniform(n) + 1;
                perc.open(row, col);
            }
            this.percThresholds[trial] = perc.numberOfOpenSites() / Math.pow(n, 2);
        }
    }

    // sample mean of percolation threshold
    public double mean() {
        if (this.mean == null){
            this.mean = StdStats.mean(this.percThresholds);
        }
        return this.mean;
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        if (this.stddev == null){
            this.stddev = StdStats.stddev(this.percThresholds);
        }
        return this.stddev;
    }

    // low  endpoint of 95% confidence interval
    public double confidenceLo() {
        return (this.mean() - (1.96 * this.stddev()) / Math.sqrt(this.T));
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return (this.mean() + (1.96 * this.stddev()) / Math.sqrt(this.T));

    }

    // test client (described below)
    // takes two command-line arguments n and T,
    // performs T independent computational experiments (discussed above) on an n-by-n grid
    // prints the sample mean, sample standard deviation, and the 95% confidence interval for the percolation threshold.
    // Use StdRandom to generate random numbers;
    // use StdStats to compute the sample mean and sample standard deviation.
    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        int T = Integer.parseInt(args[1]);
        PercolationStats percolationStats = new PercolationStats(n, T);
        System.out.println("mean                    = " + percolationStats.mean());
        System.out.println("stddev                  = " + percolationStats.stddev());
        System.out.println("95% confidence interval = ["
                + percolationStats.confidenceLo() + ", " + percolationStats.confidenceHi() + "]");
    }
}

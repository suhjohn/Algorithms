import edu.princeton.cs.algs4.StdRandom;

public class PercolationStats {
    private double[] percThresholds;
    private int T;
    private double mean;
    private double stddev;
    // perform trials independent experiments on an n-by-n grid
    // throw a java.lang.IllegalArgumentException if either n ≤ 0 or trials ≤ 0.
    public PercolationStats(int n, int trials) {
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
        this.mean = this.mean();
        this.stddev = this.stddev();
    }

    // sample mean of percolation threshold
    public double mean() {
        double sum = 0.0;
        for (double threshold : this.percThresholds) {
            sum += threshold;
        }
        return sum / this.T;
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        double sum = 0.0;
        for (double threshold : this.percThresholds) {
            sum += Math.pow(threshold - this.mean, 2);
        }
        return Math.sqrt(sum / (this.T - 1));
    }

    // low  endpoint of 95% confidence interval
    public double confidenceLo() {
        return (this.mean - (1.96*this.stddev) / Math.sqrt(this.T));
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return (this.mean + (1.96*this.stddev) / Math.sqrt(this.T));

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

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private int m; //number of independent experiments
    private double[] x; //percolation threshold for m experiments

    // Performs m independent experiments on an n x n percolation system.
    public PercolationStats(int n, int m) {
        this.m = m;
        this.x = new double[this.m];


        for(int i = 0; i < this.m; i++){
            UFPercolation ufPercolation = new UFPercolation(n);
            while(!ufPercolation.percolates()){
                int row = StdRandom.uniform(0, n); // get a random int for row 0
                int col = StdRandom.uniform(0, n); // get a random int for col
                if(!ufPercolation.isOpen(row, col)){
                    ufPercolation.open(row, col);
                }
            }
            double threshold = (double) ufPercolation.numberOfOpenSites() / (double) (n * n);
            x[i] = threshold;

        }
    }

    // Returns sample mean of percolation threshold.
    public double mean() {
        return StdStats.mean(x) ;
    }

    // Returns sample standard deviation of percolation threshold.
    public double stddev() {
        return StdStats.stddev(x);
    }

    // Returns low endpoint of the 95% confidence interval.
    public double confidenceHigh() {
        return mean() - ((1.96 * stddev()) / Math.sqrt(this.m));
    }

    // Returns high endpoint of the 95% confidence interval.
    public double confidenceLow() {
        return mean() + ((1.96 * stddev()) / Math.sqrt(this.m));
    }

    // Unit tests the data type. [DO NOT EDIT]
    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        int m = Integer.parseInt(args[1]);
        PercolationStats stats = new PercolationStats(n, m);
        StdOut.printf("Percolation threshold for a %d x %d system:\n", n, n);
        StdOut.printf("  Mean                = %.3f\n", stats.mean());
        StdOut.printf("  Standard deviation  = %.3f\n", stats.stddev());
        StdOut.printf("  Confidence interval = [%.3f, %.3f]\n", stats.confidenceLow(),
                stats.confidenceHigh());
    }
}
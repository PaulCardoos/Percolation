import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
// An implementation of the Percolation API using the UF data structure.
public class UFPercolation implements Percolation {
    private int N; //size of the system
    private boolean[][] open; //grid to represent system
    private int openSites; //number of openSites
    private WeightedQuickUnionUF uf;
    private WeightedQuickUnionUF back; //another weighted object to handle backwash problem
    // Constructs an n x n percolation system, with all sites blocked.
    public UFPercolation(int n) {
        this.N = n;
        this.open = new boolean[this.N][this.N];
        uf = new WeightedQuickUnionUF(this.N * this.N + 2);
        back = new WeightedQuickUnionUF(this.N * this.N + 2); //to resolve backwash

        for(int col = 0; col < this.N; col++){
            //connect top row to sink
            uf.union(0, encode(0, col));
            back.union(0, encode(0, col));
            //connect bottom row to faucet
            uf.union(N * N + 1, encode(N - 1, col));
        }
    }

    // Opens site (i, j) if it is not already open.
    public void open(int i, int j) {
        if(i < 0 || i > this.N - 1 || j < 0 || j > this.N - 1)
            throw new IndexOutOfBoundsException("i or j is not valid");
        //if site not open
        if(!isOpen(i, j)){
            open[i][j] = true;
            openSites++;
        }
        //right check
        if(j + 1 < this.N && isOpen(i, j + 1)){
            uf.union(encode(i, j), encode(i, j + 1));
            back.union(encode(i, j), encode(i, j + 1));
        }
        //down check
        if(i + 1 < this.N && isOpen(i + 1, j)){
            uf.union(encode(i, j), encode(i + 1, j));
            back.union(encode(i, j), encode(i + 1, j));
        }
        //left check
        if(j - 1 >= 0 && isOpen(i, j - 1)){
            uf.union(encode(i, j), encode(i, j - 1));
            back.union(encode(i, j), encode(i, j - 1));
        }
        //up check
        if(i - 1 >= 0 &&isOpen(i - 1, j)){
            uf.union(encode(i, j), encode(i - 1, j));
            back.union(encode(i, j), encode(i - 1, j));
        }
    }

    // Returns true if site (i, j) is open, and false otherwise.
    public boolean isOpen(int i, int j) {
        if(i < 0 || i > this.N - 1 || j < 0 || j > this.N - 1)
            throw new IndexOutOfBoundsException("i or j is not valid");
        //return the position of the boolean open array
        return open[i][j];
    }

    // Returns true if site (i, j) is full, and false otherwise.
    public boolean isFull(int i, int j) {
        if(i < 0 || i > this.N - 1 || j < 0 || j > this.N - 1)
            throw new IndexOutOfBoundsException("i or j is not valid");
        return (isOpen(i, j) && uf.connected(0, encode(i, j)));
    }

    // Returns the number of open sites.
    public int numberOfOpenSites() {
        return openSites;
    }

    // Returns true if this system percolates, and false otherwise.
    public boolean percolates() {
        return uf.connected(0, N * N + 1);
    }

    // Returns an integer ID (1...n) for site (i, j).
    private int encode(int i, int j) {
        return i * this.N + j + 1;
    }

    // Unit tests the data type. [DO NOT EDIT]
    public static void main(String[] args) {
        String filename = args[0];
        In in = new In(filename);
        int n = in.readInt();
        UFPercolation perc = new UFPercolation(n);
        while (!in.isEmpty()) {
            int i = in.readInt();
            int j = in.readInt();
            perc.open(i, j);
        }
        StdOut.printf("%d x %d system:\n", n, n);
        StdOut.printf("  Open sites = %d\n", perc.numberOfOpenSites());
        StdOut.printf("  Percolates = %b\n", perc.percolates());
        if (args.length == 3) {
            int i = Integer.parseInt(args[1]);
            int j = Integer.parseInt(args[2]);
            StdOut.printf("  isFull(%d, %d) = %b\n", i, j, perc.isFull(i, j));
        }
    }
}
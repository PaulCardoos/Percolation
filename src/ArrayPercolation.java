import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

// An implementation of the Percolation API using a 2D array.
public class ArrayPercolation implements Percolation {
    private boolean[][] open; //2d array
    private int N; //size of the grid
    private int openSites; //keeps track of the number of open sites

    // Constructs an n x n percolation system, with all sites blocked.
    public ArrayPercolation(int n) {
        this.N = n;
        this.openSites = 0;
        this.open = new boolean[this.N][this.N];
    }

    // Opens site (i, j) if it is not already open.
    public void open(int i, int j) {
        //check for valid index
        if(i < 0 || i > this.N - 1 || j < 0 || j > this.N - 1)
            throw new IndexOutOfBoundsException("i or j is not valid");

        //if site(i, j) is not open open site else nothing
        if(this.open[i][j] == false){
            this.open[i][j] = true;
            openSites++;
        }

        return;
    }

    // Returns true if site (i, j) is open, and false otherwise.
    public boolean isOpen(int i, int j) {
        //check for valid index
        if(i < 0 || i > this.N - 1 || j < 0 || j > this.N - 1)
            throw new IndexOutOfBoundsException("i or j is not valid");

        //check if site is open, if so return true
        if(this.open[i][j] == true)
            return true;

        //else return false
        return false;
    }

    // Returns true if site (i, j) is full, and false otherwise.
    public boolean isFull(int i, int j) {
        //check for valid index
        if(i < 0 || i > this.N - 1 || j < 0 || j > this.N - 1)
            throw new IndexOutOfBoundsException("i or j is not valid");

        //create a new n x n array
        boolean[][] full = new boolean[this.N][this.N];

        //call floodfill on every site in the first row of the percolation system
        for(int col = 0; col < this.N; col ++){
            floodFill(full, 0, col);
        }
        return full[i][j];
    }

    // Returns the number of open sites.
    public int numberOfOpenSites() {
        return openSites;
    }

    // Returns true if this system percolates, and false otherwise.
    public boolean percolates() {
        for(int col = 0; col < this.N; col++){
            if(isFull(this.N - 1, col)){
                return true;
            }
        }
        return false;
    }

    // Recursively flood fills full[][] using depth-first exploration, starting at (i, j).
    private void floodFill(boolean[][] full, int i, int j) {
        //return if i or j is out of bounds
        if(i < 0 || i > this.N - 1 || j < 0 || j > this.N - 1)
            return;
        //return if site is full or site is not open
        if(full[i][j] || !isOpen(i, j))
            return;

        //depth-first search on N, E, W, and S of site i, j
        full[i][j] = true;
        floodFill(full, i + 1, j);
        floodFill(full, i, j - 1);
        floodFill(full, i, j + 1);
        floodFill(full, i - 1, j);
    }

    // Unit tests the data type. [DO NOT EDIT]
    public static void main(String[] args) {
        String filename = args[0];
        In in = new In(filename);
        int n = in.readInt();
        ArrayPercolation perc = new ArrayPercolation(n);
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
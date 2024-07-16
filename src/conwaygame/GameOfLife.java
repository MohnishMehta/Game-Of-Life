package conwaygame;
import java.util.ArrayList;
/**
 * Conway's Game of Life Class holds various methods that will
 * progress the state of the game's board through it's many iterations/generations.
 *
 * Rules 
 * Alive cells with 0-1 neighbors die of loneliness.
 * Alive cells with >=4 neighbors die of overpopulation.
 * Alive cells with 2-3 neighbors survive.
 * Dead cells with exactly 3 neighbors become alive by reproduction.

 * @author Seth Kelley 
 * @author Maxwell Goldberg
 */
public class GameOfLife {

    // Instance variables
    private static final boolean ALIVE = true;
    private static final boolean  DEAD = false;

    private boolean[][] grid;    // The board has the current generation of cells
    private int totalAliveCells; // Total number of alive cells in the grid (board)

    /**
    * Default Constructor which creates a small 5x5 grid with five alive cells.
    * This variation does not exceed bounds and dies off after four iterations.
    */
    public GameOfLife() {
        grid = new boolean[5][5];
        totalAliveCells = 5;
        grid[1][1] = ALIVE;
        grid[1][3] = ALIVE;
        grid[2][2] = ALIVE;
        grid[3][2] = ALIVE;
        grid[3][3] = ALIVE;
    }

    /**
    * Constructor used that will take in values to create a grid with a given number
    * of alive cells
    * @param file is the input file with the initial game pattern formatted as follows:
    * An integer representing the number of grid rows, say r
    * An integer representing the number of grid columns, say c
    * Number of r lines, each containing c true or false values (true denotes an ALIVE cell)
    */
    public GameOfLife (String file) {

        // WRITE YOUR CODE HERE
        StdIn.setFile(file);
        int r = StdIn.readInt();
        int c = StdIn.readInt();
        grid = new boolean[r][c];
        for(int x  = 0; x < r; x++) {
            for(int y = 0;y < c;y++) {
                grid[x][y] = StdIn.readBoolean();
            }
        }
    }

    /**
     * Returns grid
     * @return boolean[][] for current grid
     */
    public boolean[][] getGrid () {
        return grid;
    }
    
    /**
     * Returns totalAliveCells
     * @return int for total number of alive cells in grid
     */
    public int getTotalAliveCells () {
        return totalAliveCells;
    }

    /**
     * Returns the status of the cell at (row,col): ALIVE or DEAD
     * @param row row position of the cell
     * @param col column position of the cell
     * @return true or false value "ALIVE" or "DEAD" (state of the cell)
     */
    public boolean getCellState (int row, int col) {

       if(grid[row][col])
        return true;
    return false; // update this line, provided so that code compiles
    }

    /**
     * Returns true if there are any alive cells in the grid
     * @return true if there is at least one cell alive, otherwise returns false
     */
    public boolean isAlive () {
        for(int x = 0;x < grid.length;x++) {
            for(int y =0; y < grid[0].length;y++) {
                if(grid[x][y])
                return true;
            }
        }
        
        return false; // update this line, provided so that code compiles
    }

    /**
     * Determines the number of alive cells around a given cell.
     * Each cell has 8 neighbor cells which are the cells that are 
     * horizontally, vertically, or diagonally adjacent.
     * 
     * @param col column position of the cell
     * @param row row position of the cell
     * @return neighboringCells, the number of alive cells (at most 8).
     */
    public int numOfAliveNeighbors (int row, int col) {

        int numA = 0;
        int rowS= row - 1;
        int rowE= row + 1;
        int colS= col - 1;
        int colE= col + 1;
        int temp1;
        int temp2;
        for (int x = rowS; x < rowE + 1; x++) {
            for (int y = colS; y < colE + 1; y++) {
                temp1 = x;
                temp2 = y;
                if (temp1 == -1) {
                    temp1 = grid.length - 1;
                }
                if (temp2 == -1) {
                    temp2 = grid[0].length - 1;
                }
                if (temp1 == grid.length) {
                    temp1 = 0;
                }
                if (temp2 == grid[0].length) {
                    temp2 = 0;
                }
                if (grid[temp1][temp2])
                    numA++;
            }
        }
        if (grid[row][col])
            numA--;
        return numA; 
    }

    /**
     * Creates a new grid with the next generation of the current grid using 
     * the rules for Conway's Game of Life.
     * 
     * @return boolean[][] of new grid (this is a new 2D array)
     */
    public boolean[][] computeNewGrid () {

        boolean[][] newgrid = new boolean[grid.length][grid[0].length];
        for(int x = 0;x < grid.length;x++) {
            for(int y =0; y < grid[0].length;y++) {
                int count = numOfAliveNeighbors(x, y);
                if(grid[x][y]) {
                    if(count <= 1)
                        newgrid[x][y] = false;
                    else if(count == 2 || count == 3)
                        newgrid[x][y] = true;
                    else
                        newgrid[x][y] = false;
                }
                else if(count == 3) {
                    newgrid[x][y] = true;
                }
                else
                    newgrid[x][y] = false;
            }
        }
        return newgrid;// update this line, provided so that code compiles
    }

    /**
     * Updates the current grid (the grid instance variable) with the grid denoting
     * the next generation of cells computed by computeNewGrid().
     * 
     * Updates totalAliveCells instance variable
     */
    public void nextGeneration () {
        grid = computeNewGrid();
        for(int x = 0;x < grid.length;x++) {
            for(int y =0; y < grid[0].length;y++) {
                if(grid[x][y])
                    totalAliveCells++;
            }
        }

        // WRITE YOUR CODE HERE
    }

    /**
     * Updates the current grid with the grid computed after multiple (n) generations. 
     * @param n number of iterations that the grid will go through to compute a new grid
     */
    public void nextGeneration (int n) {

        for(int x = 0;x < n;x++) {
            nextGeneration();
        }
    }

    /**
     * Determines the number of separate cell communities in the grid
     * @return the number of communities in the grid, communities can be formed from edges
     */
    public int numOfCommunities() {
        WeightedQuickUnionUF total =  new WeightedQuickUnionUF(grid.length, grid[0].length);
        
        for(int i = 0; i < grid.length;i++) {
            for(int j = 0; j < grid[0].length;j++) {
                if(grid[i][j]) {
                int rowS= i - 1;
                int rowE= i + 1;
                int colS= j - 1;
                int colE= j + 1;
                int temp1 = i;
                int temp2 = j;
                for (int x = rowS; x < rowE + 1; x++) {
                    for (int y = colS; y < colE + 1; y++) {
                        temp1 = x;
                        temp2 = y;
                        if (temp1 == -1) {
                            temp1 = grid.length - 1;
                        }
                        if (temp2 == -1) {
                            temp2 = grid[0].length - 1;
                        }
                        if (temp1 == grid.length) {
                            temp1 = 0;
                        }
                        if (temp2 == grid[0].length) {
                            temp2 = 0;
                        }
                        if (grid[temp1][temp2]) {
                            total.union(i, j, temp1, temp2);
                        }
                            
                    }
                }
            }
        }
        }
            //Stage 2
            ArrayList<Integer> unique = new ArrayList<Integer>();
            for (int x= 0; x < grid.length; x++) {
                for (int y = 0; y < grid[0].length; y++) {
                    if (getCellState(x, y) && !unique.contains(total.find(x, y))) {
                        unique.add(total.find(x, y));
                    }
                }
            }
            return unique.size();
        // WRITE YOUR CODE HERE
     // update this line, provided so that code compiles
    }
}

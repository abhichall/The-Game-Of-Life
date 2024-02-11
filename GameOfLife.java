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

        //starts to read the file introduced in method
        StdIn.setFile(file);

        //goes into the file mentioned above
        //reads first line, finds the number, but it is a string, so Integer.parseInt will cast it into an int so it can be used later as an int
        int r = Integer.parseInt(StdIn.readLine());
        int c = Integer.parseInt(StdIn.readLine());

        //initials 2d array with given specs for rows and columns
        grid = new boolean [r][c];

        //goes through each index in the array and assigns it with the value from the file
        for (int i = 0; i<r; i++){
            for (int j = 0; j<c; j++) {
                    grid [i][j] = StdIn.readBoolean();

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

        return grid[row][col]; // update this line, provided so that code compiles
    }

    /**
     * Returns true if there are any alive cells in the grid
     * @return true if there is at least one cell alive, otherwise returns false
     */
    public boolean isAlive () {

//pretty much, this piece of code will run through each index of the array. if any one part of the 2D array is true,
//then the return will be true since that means there are still living cells
        
        for (int i = 0; i<grid.length; i++){                              
            for (int j = 0; j<grid[i].length; j++) {
                    if (grid [i][j] == true) {
                        return true;
                    }

            }
        }

        return false; 
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

        int counter = 0;

        for(int i = row-1;i<=row+1;i++){
            for(int j=col-1;j<=col+1;j++){
                int rowTemp = i;
                int colTemp = j;
                if(i<0){
                    rowTemp=grid.length-1;
                }
                if(i==grid.length){
                    rowTemp=0;
                }
                if(j<0){
                    colTemp=grid[0].length-1;
                }
                if(j==grid[0].length){
                    colTemp=0;
                }
                if(getCellState(rowTemp, colTemp)){
                    counter++;
                }
            }
        }
        if(getCellState(row,col)){counter--;}
        return counter;  // update this line, provided so that code compiles
    }
    /**
     * Creates a new grid with the next generation of the current grid using 
     * the rules for Conway's Game of Life.
     * 
     * @return boolean[][] of new grid (this is a new 2D array)
     */
    public boolean[][] computeNewGrid () {
        boolean[][] newOne = new boolean[grid.length][grid[0].length];
        for(int row = 0; row<grid.length;row++){
            for(int col = 0; col<grid[0].length;col++){
                if(getCellState(row,col) && numOfAliveNeighbors(row,col)<=1){
                    newOne[row][col]=false;
                }
                else if(getCellState(row,col) && numOfAliveNeighbors(row,col)<=3){
                    newOne[row][col]=true;
                }
                else if(numOfAliveNeighbors(row,col)==3){
                    newOne[row][col]=true;
                }
                else{newOne[row][col]=false;}
            }
        }
        // WRITE YOUR CODE HERE
        return newOne;// update this line, provided so that code compiles
    }

    /**
     * Updates the current grid (the grid instance variable) with the grid denoting
     * the next generation of cells computed by computeNewGrid().
     * 
     * Updates totalAliveCells instance variable
     */
    public void nextGeneration () {
        grid = computeNewGrid();
    }

    /**
     * Updates the current grid with the grid computed after multiple (n) generations. 
     * @param n number of iterations that the grid will go through to compute a new grid
     */
    public void nextGeneration (int n) {
        for(int i = 1; i<=n;i++){
            grid = computeNewGrid();
        }
    }

    /**
     * Determines the number of separate cell communities in the grid
     * @return the number of communities in the grid, communities can be formed from edges
     */
    public int numOfCommunities() {

        int counter = 0;

        WeightedQuickUnionUF communities = new WeightedQuickUnionUF(grid.length, grid[0].length);

        for(int i = 0; i<grid.length; i++){
            for(int j = 0; j<grid[0].length;j++){
                
                if(getCellState(i,j)&& numOfAliveNeighbors(i,j)>0){
                    for(int row = i-1; row<=i+1; row++){
                        for(int col = j-1; col<= j+1;col++){
                            int rowTemp = row;
                            int colTemp = col;

                            if(row<0) {
                                rowTemp=grid.length-1;

                            }

                            if(row==grid.length)  {
                                rowTemp=0;
                            }

                            if(col<0) {
                                colTemp=grid[0].length-1;
                            }

                            if(col==grid[0].length) { 
                                colTemp=0;
                            }

                            if(getCellState(rowTemp, colTemp)) {
                            communities.union(i,j,rowTemp,colTemp);
                }
                }
                
                
            }
        }
        
    }
}


ArrayList<Integer> root = new ArrayList<Integer>();

    for(int i = 0; i<grid.length; i++){
        for(int j = 0; j<grid[0].length;j++){
            if(getCellState(i, j)){
                if(root.size()==0){
                    root.add(communities.find(i,j));
                }
            int location = communities.find(i,j);
            boolean found = false;
            for(Integer list: root){
                if(list==location){
                    found = true;
                }
            }
            if(!found){
                counter++;
                root.add(location);
            }
        }
        }
    }
            if(isAlive()) {
                return counter + 1;
            }
            else {
                return 0;}
}
}
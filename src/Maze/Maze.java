//This class sets all the information for the maze

package Maze;

import MazeGenerators.*;
import MazeSolvers.MazeSolver;
import javafx.application.Platform;

import java.util.HashMap;
import java.util.Random;
import java.util.function.Consumer;
import java.util.function.Function;

public class Maze {
    private boolean[][] visited;
    private Cell[][] maze;
    private int size;
    private Cell topOpening;
    private boolean topSeen;
    private Cell bottomOpening;
    private boolean bottomSeen;
    private MazeGenerator mazeGenerator;
    private BaseSolver solver;

    public Maze(int size, String generatorString, String solverString, Runnable reRender) {
        this.maze = new Cell[size][size];
        this.visited = new boolean[size][size];
        this.size = size;
        this.mazeGenerator = GeneratorFactory.generate(generatorString, maze, size);
        for (int i = 0 ; i < size; i++){
            for(int j = 0; j < size; j++){
                maze[i][j] = new Cell(i, j);
                visited[i][j] = false;
            }
        }
        this.generate();
        this.solver = SolverFactory.generate(solverString, bottomOpening, topOpening,
                reRender, () -> clearAllButSolved());
    }

    public Cell[][] generate(Consumer<Cell> addToBoard, Runnable reRender, Runnable x){
        Thread t = new Thread(() -> {
            mazeGenerator.initGenerator(maze, size);
            this.mazeGenerator.generate(addToBoard);
            setRandomOpenings();
            clearVisited();
            for (int i = 0 ; i < size; i++){
                for(int j = 0; j < size; j++){
                    Cell c = maze[i][j];
                    c.Direct_Call = new HashMap<>();
                    c.Direct_Call.put("NORTH", c.getTop());
                    c.Direct_Call.put("WEST", c.getLeft());
                    c.Direct_Call.put("SOUTH", c.getBottom());
                    c.Direct_Call.put("EAST", c.getRight());
                }
            }
            Platform.runLater(x);
            solve(reRender, x);
        });
        t.setDaemon(true);
        t.start();
        return maze;
    }

    public void solve(Runnable reRender, Runnable lastRender){
        System.out.println("Solving......");
        solver.solve(bottomOpening, topOpening, reRender, () -> {
            clearAllButSolved();
            lastRender.run();
        });
    }

    /**
     * Prints the maze
     */
    public void printMaze(){
        for (Cell[] row : this.maze){
            for(Cell c : row) {
                System.out.print(c.getChar());
            }
            System.out.println();
        }

    }

    /**
     * Gets the maze
     * @return type Cell[][]
     */
    public Cell[][] getMaze() {
        return maze;
    }

    /**
     * Clears all the tried paths except the solving path
     */
    private void clearAllButSolved(){
        System.out.println("Got called");
        for (Cell[] row : this.maze){
            for(Cell c : row) {
                if(!c.isSolutionPath()){
                    c.setVisited(false);
                }
            }
        }
    }

    /**
     * Sets the random openings in the maze
     */
    private void setRandomOpenings(){
        Random r = new Random();

        int column = r.nextInt(size);
        this.topOpening = new OpeningCell(-1,column);
        maze[0][column].setTopNeighbor(topOpening);
        topOpening.setBottomNeighbor(maze[0][column]);

        column = r.nextInt(size);
        this.bottomOpening = new OpeningCell(size, column);
        maze[size-1][column].setBottomNeighbor(bottomOpening);
        bottomOpening.setTopNeighbor(maze[size-1][column]);
    }

    /**
     * Clear the visited cells
     */
    private void clearVisited(){
        for (Cell[] row : this.maze) {
            for (Cell c : row) {
                c.setVisited(false);
            }
        }
    }

    /**
     * Get the top opening of the maze
     * @return
     */
    public Cell getTopOpening() {
        return topOpening;
    }

    /**
     * Get the bottom opening of the maze
     * @return
     */
    public Cell getBottomOpening() {
        return bottomOpening;
    }

    public boolean isVisited(int row, int col){
        return this.visited[row][col];
    }

    public void setVisited(int row, int col){
        this.visited[row][col] = true;
    }

    public boolean isBottomSeen() {
        return bottomSeen;
    }

    public boolean isTopSeen() {
        return topSeen;
    }

    public void setBottomSeen(boolean bottomSeen) {
        this.bottomSeen = bottomSeen;
    }

    public void setTopSeen(boolean topSeen) {
        this.topSeen = topSeen;
    }
}

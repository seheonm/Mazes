package Maze;

import MazeGenerators.*;
import MazeSolvers.BaseSolver;
import MazeSolvers.SolverFactory;

import java.util.HashMap;
import java.util.Random;

public class Maze {
    private Cell[][] maze;
    private int size;
    private Cell topOpening;
    private Cell bottomOpening;
    private MazeGenerator mazeGenerator;
    private MazeSolver solver;

    public Maze(int size, MazeGenerator mazeGenerator, MazeSolver solver) {
        this.maze = new Cell[size][size];
        this.size = size;
        this.mazeGenerator = mazeGenerator;
        this.solver = solver;

        for (int i = 0 ; i < size; i++){
            for(int j = 0; j < size; j++){
                maze[i][j] = new Cell(i, j);
            }
        }
    }

    /**
     * Generates the maze
     * @return Cell[][]
     */
    private Cell[][] generate() {
        //mazeGenerator.initGenerator(maze, size);
        this.mazeGenerator.generate();
        setRandomOpenings();
        clearVisited();
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                Cell c = maze[i][j];
                c.Direct_Call = new HashMap<>();
                c.Direct_Call.put("NORTH", c.getTop());
                c.Direct_Call.put("WEST", c.getLeft());
                c.Direct_Call.put("SOUTH", c.getBottom());
                c.Direct_Call.put("EAST", c.getRight());
            }
        }
        return maze;
    }

    /**
     * Solves the maze
     */
    public void solve(){
        System.out.println("Solving......");
        solver.start();
    }

    public void printMaze(){
        for (Cell[] row : this.maze){
            for(Cell c : row) {
                System.out.print(c.getChar());
            }
            System.out.println();
        }

    }

    public Cell[][] getMaze() {
        return maze;
    }

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

    private void clearVisited(){
        for (Cell[] row : this.maze) {
            for (Cell c : row) {
                c.setVisited(false);
            }
        }
    }

    public Cell getTopOpening() {
        return topOpening;
    }

    public Cell getBottomOpening() {
        return bottomOpening;
    }

}

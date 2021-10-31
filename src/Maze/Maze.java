package Maze;

import MazeGenerators.*;

import java.util.Random;

public class Maze {
    private Cell[][] maze;
    private int size;
    private Cell topOpening;
    private Cell bottomOpening;
    private MazeGenerator mazeGenerator;

    public Maze(int size, MazeGenerator mazeGenerator) {
        this.maze = new Cell[size][size];
        this.size = size;
        this.mazeGenerator = mazeGenerator;

        for (int i = 0 ; i < size; i++){
            for(int j = 0; j < size; j++){
                maze[i][j] = new Cell(i, j);
            }
        }
    }

    public Cell[][] generate(){
        mazeGenerator.initGenerator(maze, size);
        this.mazeGenerator.generate();
        setRandomOpenings();
        clearVisited();
        return maze;
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

    public static void main(String[] args){
        Maze m = new Maze(10, new PrimsGenerator());
        m.generate();
        m.printMaze();
        System.out.println();
    }
}

package Maze;

import MazeGenerators.DepthFirstGenerator;
import MazeGenerators.KruskalGenerator;
import MazeGenerators.PrimsGenerator;

import java.util.Random;

public class Maze {
    private Cell[][] maze;
    private int size;

    public Maze(int size) {
        this.maze = new Cell[size][size];
        this.size = size;
        for (int i = 0 ; i < size; i++){
            for(int j = 0; j < size; j++){
                maze[i][j] = new Cell(i, j);
            }
        }
    }

    public Cell[][] generateWithDepthFirst(){
        new DepthFirstGenerator(maze, size).generate();
        setRandomOpenings();
        return maze;
    }

    public Cell[][] generateWithKruskal(){
        new KruskalGenerator(maze, size).generate();
        setRandomOpenings();
        return maze;
    }

    public Cell[][] generateWithPrims(){
        new PrimsGenerator(maze, size).generate();
        setRandomOpenings();
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

    private void setRandomOpenings(){
        Random r = new Random();
        maze[0][r.nextInt(size)].setTopNeighbor(new OpeningCell());
        maze[size-1][r.nextInt(size)].setBottomNeighbor(new OpeningCell());
    }

    public static void main(String[] args){
        Maze m = new Maze(10);
        m.generateWithPrims();
        m.printMaze();
        System.out.println();
    }
}

package MazeGenerators;

import Maze.Cell;

public abstract class MazeGenerator {
    protected int boardSize;
    protected Cell[][] board;

    public MazeGenerator(){};

    public void initGenerator(Cell[][] board, int size){
        boardSize = size;
        this.board = board;
        init();
    }

    /**
     * Initializes the maze
     */
    protected abstract void init();

    /**
     * Generates the maze
     */
    public abstract void generate();
}

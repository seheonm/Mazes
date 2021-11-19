//This abstract class generates and initialize the maze
package MazeGenerators;

import Maze.Cell;

import java.util.function.Consumer;

public abstract class MazeGenerator {
    protected int boardSize;
    protected Cell[][] board;

    protected final int waitTime = 10;

    public MazeGenerator(){}

    /**
     * Initialize generator
     * @param board board representation of maze
     * @param size board size
     */
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
    public abstract void generate(Consumer<Cell> addToBoard);
}

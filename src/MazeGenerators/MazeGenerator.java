//This class generates and initialize the maze

package MazeGenerators;

import Maze.Cell;

public abstract class MazeGenerator {
    protected int boardSize;
    protected Cell[][] board;

    public MazeGenerator(Cell[][] board, int size){
        this.boardSize = size;
        this.board = board;
    };

    /**
     * Generates the maze
     */
    public abstract void generate();
}

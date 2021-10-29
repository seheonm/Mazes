package MazeGenerators;

import Maze.Cell;

public abstract class MazeGenerator {
    protected int boardSize;
    protected Cell[][] board;

    public MazeGenerator(Cell[][] board, int size){
        boardSize = size;
        this.board = board;
    }

    public abstract void generate();
}

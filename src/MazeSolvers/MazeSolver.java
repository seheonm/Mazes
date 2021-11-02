package MazeSolvers;

import Maze.Cell;

public interface MazeSolver {
    void solve(Cell start, Cell end, Runnable reRender, Runnable clearAllButSolved);
}

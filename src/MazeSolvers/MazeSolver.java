//Maze solver interface
package MazeSolvers;

import Maze.Cell;

public interface MazeSolver {

    /**
     * Solve the maze
     * @param start staring cell
     * @param end ending cell
     * @param reRender re-render
     * @param clearAllButSolved clear all cells but the solved path
     */
    void solve(Cell start, Cell end, Runnable reRender, Runnable clearAllButSolved);
}

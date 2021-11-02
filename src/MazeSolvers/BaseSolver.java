package MazeSolvers;

import Maze.Cell;

public abstract class BaseSolver extends Thread implements MazeSolver{
    protected Cell start;
    protected Cell end;
    protected Runnable reRender;
    protected Runnable clearAllButSolved;

    @Override
    public abstract void run();

    @Override
    public void solve(Cell start, Cell end, Runnable reRender, Runnable clearAllButSolved){
        this.start = start;
        this.end = end;
        this.reRender = reRender;
        this.clearAllButSolved = clearAllButSolved;

        this.start();
    };
}

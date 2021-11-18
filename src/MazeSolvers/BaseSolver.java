package MazeSolvers;

import Maze.Cell;

import java.util.LinkedList;
import java.util.List;

public abstract class BaseSolver extends Thread implements MazeSolver{
    protected Cell start;
    protected Cell end;
    protected Runnable reRender;
    protected Runnable clearAllButSolved;
    protected List<Cell> path;
    protected boolean running;

    @Override
    public abstract void run();

    @Override
    public void solve(Cell start, Cell end, Runnable reRender, Runnable clearAllButSolved){
        this.start = start;
        this.end = end;
        this.reRender = reRender;
        this.clearAllButSolved = clearAllButSolved;
        this.path = new LinkedList<>();
        running = true;
        setDaemon(true);
        this.start();
    };
    public void solve(Cell start, Cell end, List<Cell> path, Runnable reRender, Runnable clearAllButSolved){
        this.start = start;
        this.end = end;
        this.path = path;
        this.reRender = reRender;
        this.clearAllButSolved = clearAllButSolved;
        running = true;
        setDaemon(true);
        this.start();
    };

    public void stopSolver(){
        running = false;
    }
}

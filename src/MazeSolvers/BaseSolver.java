//This class is the base class for the solver algorithms

package MazeSolvers;

import Maze.Cell;
import javafx.application.Platform;

import java.util.LinkedList;
import java.util.List;

public abstract class BaseSolver extends Thread {
    protected Cell start;
    protected Cell end;
    protected Runnable reRender;
    protected Runnable clearAllButSolved;
    protected List<Cell> path;
    protected boolean running;

    protected final int waitTime = 30;

    @Override
    public abstract void run();

    public BaseSolver(Cell start, Cell end, Runnable reRender, Runnable clearAllButSolved) {
        this.start = start;
        this.end = end;
        this.path = new LinkedList<>();
        this.reRender = reRender;
        this.clearAllButSolved = clearAllButSolved;
        running = true;
        setDaemon(true);
    }

    public BaseSolver(Cell start, Cell end, List<Cell> path, Runnable reRender, Runnable clearAllButSolved){
        this.start = start;
        this.end = end;
        this.path = path;
        this.reRender = reRender;
        this.clearAllButSolved = clearAllButSolved;
        running = true;
        setDaemon(true);
    }

    /**
     * Animates the path going through the maze.
     * @param animationDelay of type int for speed of the path in milliseconds
     */
    protected void animate(int animationDelay) {
        try {
            Thread.sleep(animationDelay);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Platform.runLater(() -> {
            reRender.run();
        });
    }

    public void stopSolver(){
        running = false;
    }
}

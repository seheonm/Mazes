//This class is the algorithm for the Random Mouse Solver

package MazeSolvers;

import Maze.Cell;
import Maze.OpeningCell;
import javafx.application.Platform;

import java.util.ArrayList;
import java.util.List;

public class RandomMouseSolver extends BaseSolver{
    private static boolean solved = false;

    public RandomMouseSolver(Cell start, Cell end, Runnable reRender, Runnable clearAllButSolved){
        super(start,end,reRender,clearAllButSolved);
    }

    public RandomMouseSolver(Cell start, Cell end, List<Cell> path, Runnable reRender, Runnable clearAllButSolved) {
        super(start,end,path,reRender,clearAllButSolved);
    }

    @Override
    public void run(){
        // New Mouse
        if (!solved && mouseSolve(start)) {

            // Solved
            for(Cell c : path){
                c.setSolutionPath(true);
            }
            solved = true;
            Platform.runLater(() -> {
                clearAllButSolved.run();
                reRender.run();
            });

        }
        // Mouse Died
    }


    private boolean mouseSolve(Cell c){
        if(solved || c == null || c.isVisited()) return false;

        try {
            Thread.sleep(waitTime);
            Platform.runLater(() -> {
                if(!solved) reRender.run();
            });
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        c.setVisited(true);
        path.add(c);
        if(c instanceof OpeningCell && c.row == end.row){
            return true;
        }

        if(getNumNeighbors(c) == 1){
            if(mouseSolve(c.getTop())) return true;
            if(mouseSolve(c.getBottom())) return true;
            if(mouseSolve(c.getLeft())) return true;
            if(mouseSolve(c.getRight())) return true;
        } else {
            new BasicSolver(c.getTop(), end, new ArrayList<>(path), reRender, clearAllButSolved).start();
            new BasicSolver(c.getBottom(), end, new ArrayList<>(path), reRender, clearAllButSolved).start();
            new BasicSolver(c.getLeft(), end, new ArrayList<>(path), reRender, clearAllButSolved).start();
            new BasicSolver(c.getRight(), end, new ArrayList<>(path), reRender, clearAllButSolved).start();
        }
        return false;
    }

    public int getNumNeighbors(Cell c){
        int count = 0;
        if(c.getRight() != null && !c.getRight().isVisited()) count++;
        if(c.getLeft() != null && !c.getLeft().isVisited()) count++;
        if(c.getTop() != null && !c.getTop().isVisited()) count++;
        if(c.getBottom() != null && !c.getBottom().isVisited()) count++;
        return count;
    }
}


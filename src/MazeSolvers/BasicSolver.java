package MazeSolvers;

import Maze.Cell;
import Maze.OpeningCell;
import javafx.application.Platform;

public class BasicSolver extends BaseSolver{
    @Override
    public void run(){
        start.setVisited(true);
        solveRecursive(start.getTop());

        Platform.runLater(() -> {
            clearAllButSolved.run();
            reRender.run();
        });
    }

    /**
     * This function solves the maze using recursion
     * @param c of type Cell for which cell
     * @return boolean if there is a good path
     */
    private boolean solveRecursive(Cell c){
        if(c.isVisited()) return false;

        try {
            Thread.sleep(500);
            Platform.runLater(() -> {
                reRender.run();
            });
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        c.setVisited(true);
        if(c instanceof OpeningCell){
            return true;
        }

        if(c.getTop() != null){
            if(solveRecursive(c.getTop())){
                c.setSolutionPath(true);
                return true;
            }
        }
        if(c.getBottom() != null){
            if(solveRecursive(c.getBottom())){
                c.setSolutionPath(true);
                return true;
            }

        }
        if(c.getLeft() != null){
            if(solveRecursive(c.getLeft())){
                c.setSolutionPath(true);
                return true;
            }
        }
        if(c.getRight() != null){
            if(solveRecursive(c.getRight())){
                c.setSolutionPath(true);
                return true;
            }
        }
        return false;
    }
}

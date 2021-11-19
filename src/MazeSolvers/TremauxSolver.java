//This class is the Tremaux Solving algorithm

package MazeSolvers;


import Maze.Cell;
import Maze.OpeningCell;
import javafx.application.Platform;
import java.util.ArrayList;

public class TremauxSolver extends BaseSolver {


    /**
     * This function collects the unvisited neighboring cells around c
     * @param c of type Cell
     * @return ArrayList consisting of Cells that have no neighbors
     */
    private ArrayList<Cell> unvisitedNeighbors(Cell c) {
        ArrayList<Cell> lonelyNeighbors = new ArrayList<Cell>();
        if (c.getTop() != null && !c.getTop().isVisited()) {
            lonelyNeighbors.add(c.getTop());
        }
        if (c.getBottom() != null && !c.getBottom().isVisited()) {
            lonelyNeighbors.add(c.getBottom());
        }
        if (c.getLeft() != null && !c.getLeft().isVisited()) {
            lonelyNeighbors.add(c.getLeft());
        }
        if (c.getRight() != null && !c.getRight().isVisited()) {
            lonelyNeighbors.add(c.getRight());
        }
        return lonelyNeighbors;
    }

    /**
     * Tremaux solving algorithm using backtracking
     * @param c of type Cell
     * @return boolean for if there is a solution or not
     */
    public boolean solveRecursively(Cell c) {
        c.setVisited(true);

        try {
            Thread.sleep(waitTime);
            Platform.runLater(() -> reRender.run());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (!(c instanceof OpeningCell)) {
            ArrayList<Cell> lonelyNeighbors = unvisitedNeighbors(c);
            //More neighbors to visit
            for (Cell lonelyNeighbor : lonelyNeighbors) {
                if (solveRecursively(lonelyNeighbor)) {
                    return true;
                }
            }
            //Dead end
            c.setVisited(false);
            try {
                Thread.sleep(waitTime);
                Platform.runLater(() -> reRender.run());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return false;
        }
        //Found opening cell solution
        return true;
    }

    @Override
    public void run() {
        this.start.setVisited(true);
        ArrayList<Cell> lonelyNeighbors = unvisitedNeighbors(this.start);
        for (Cell lonelyNeighbor : lonelyNeighbors) {
            if (solveRecursively(lonelyNeighbor)) {
                break;
            }
        }
    } 
}

package MazeSolvers;

import Maze.Cell;
import Maze.OpeningCell;
import javafx.application.Platform;

import java.util.*;

public class LightningSolver extends BaseSolver{

    @Override
    public void run() {
        System.out.println("Run Lightning Follower");


        end.setVisited(true);
        solveBFS(end.getBottom());

        Platform.runLater(() -> {
            clearAllButSolved.run();
            reRender.run();
        });
    }


    private boolean solveBFS(Cell begin) {
        if (begin.isVisited()){
            return false;
        }

        ArrayDeque<Cell> queue = new ArrayDeque<>();
        queue.add(begin);
        Cell end = null;
        while (queue.size() != 0)
        {
            try {
                Thread.sleep(100);
                Platform.runLater(() -> {
                    reRender.run();
                });
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // Dequeue a vertex from queue
            Cell curr = queue.poll();

            // already here, go to next node in queue
            if (curr.isVisited()) {
                continue;
            }
            curr.setVisited(true);
            // enqueue c's four friends
            Cell neighbor = curr.getLeft();
            if (neighbor != null && !neighbor.isVisited()) {
                queue.add(neighbor);
                neighbor.setPrevious(curr);
            }
            neighbor = curr.getTop();
            if (neighbor != null && !neighbor.isVisited()) {
                queue.add(neighbor);
                neighbor.setPrevious(curr);
            }
            neighbor = curr.getRight();
            if (neighbor != null && !neighbor.isVisited()) {
                queue.add(neighbor);
                neighbor.setPrevious(curr);
            }
            neighbor = curr.getBottom();
            if (neighbor != null && !neighbor.isVisited()) {
                queue.add(neighbor);
                neighbor.setPrevious(curr);
            }
            // Done
            if (curr instanceof OpeningCell){
                end = curr;
                break;
            }
        }
        // loop from end to begin
        Cell curr = end;
        while (true) {
            curr.setSolutionPath(true);
            // draw water here
            if (curr == begin) break;
            curr = curr.getPrevious();
        }

        System.out.println("Hooray!");
        return true;
    }
}

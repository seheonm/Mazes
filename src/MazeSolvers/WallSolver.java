package MazeSolvers;

import Maze.Cell;
import Maze.Maze;
import Maze.OpeningCell;
import javafx.application.Platform;
import javafx.scene.control.skin.TextInputControlSkin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class WallSolver extends BaseSolver implements Runnable{
    String current_dir;
    HashMap<String, List<String>> Directions;


    @Override
    public void run() {
        System.out.println("Run Wall Follower");

        Directions = new HashMap<>();
        List<String> list1 = Arrays.asList("EAST", "NORTH", "WEST", "SOUTH");
        List<String> list2 = Arrays.asList("NORTH", "WEST", "SOUTH", "EAST");
        List<String> list3 = Arrays.asList("WEST", "SOUTH", "EAST", "NORTH");
        List<String> list4 = Arrays.asList("SOUTH", "EAST", "NORTH", "WEST");
        Directions.put("NORTH", list1);
        Directions.put("WEST", list2);
        Directions.put("SOUTH", list3);
        Directions.put("EAST", list4);

        current_dir = "NORTH";


        start.setVisited(true);
        solveRecursive(start.getTop(), current_dir);

        Platform.runLater(() -> {
            clearAllButSolved.run();
            reRender.run();
        });
    }

    private boolean solveRecursive(Cell c, String current_dir){
        if (c.isVisited()){
            return false;
        }
        try {
            Thread.sleep(500);
            Platform.runLater(() -> {
                reRender.run();
            });
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        c.setVisited(true);
        if (c instanceof OpeningCell){
            return true;
        }


        for (String d : Directions.get(current_dir)) {
            if (c.Direct_Call.get((d)) == null) { // if there's a wall then go to the next direction
                continue;
            }
            if (solveRecursive(c.Direct_Call.get(d), d)) {
                c.setSolutionPath(true);
                return true;
            }
        }
        return false;
    }
}

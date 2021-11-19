//Wall solver class
package MazeSolvers;

import Maze.Cell;
import Maze.OpeningCell;
import MazeGenerators.Wall;
import javafx.application.Platform;

import java.util.*;

public class WallSolver extends BaseSolver implements Runnable{
    String current_dir;
    HashMap<String, List<String>> Directions;
    Cell cell;
    long threadNorthID, threadSouthID;
    private int numThreads = 1;


    /**
     * @param numThreads number of threads will be run
     */
    public WallSolver(int numThreads){
        this.numThreads = numThreads;
    }


    @Override
    public void run() {
        System.out.println("Run Wall Follower");

        //For Right-Hand Rule
        Directions = new HashMap<>();
        List<String> list1 = Arrays.asList("EAST", "NORTH", "WEST", "SOUTH");
        List<String> list2 = Arrays.asList("NORTH", "WEST", "SOUTH", "EAST");
        List<String> list3 = Arrays.asList("WEST", "SOUTH", "EAST", "NORTH");
        List<String> list4 = Arrays.asList("SOUTH", "EAST", "NORTH", "WEST");
        Directions.put("NORTH", list1);
        Directions.put("WEST", list2);
        Directions.put("SOUTH", list3);
        Directions.put("EAST", list4);


        new Thread(() -> { // Going down
            threadSouthID = Thread.currentThread().getId()%2;
            current_dir = "SOUTH";
            end.setVisited(true);
            solveRecursive(end.getBottom(), current_dir);

            Platform.runLater(() -> {
                clearAllButSolved.run();
                reRender.run();
            });
        }).start();


        if(numThreads == 2) {
            new Thread(() -> { // Going up
                threadNorthID = Thread.currentThread().getId() % 2;
                current_dir = "NORTH";
                start.setVisited(true);
                solveRecursive(start.getTop(), current_dir);
                Platform.runLater(() -> {
                    clearAllButSolved.run();
                    reRender.run();
                });
            }).start();
        }
    }

    HashSet<Cell> list1= new HashSet<>();
    HashSet<Cell> list2= new HashSet<>();

    /**
     * Solve using DFS-based algorithm
     * @param c current cell
     * @param current_dir current direction
     * @return
     */
    private boolean solveRecursive(Cell c, String current_dir){

        if(numThreads == 2) {
            if (!Collections.disjoint(list1, list2)) {
                cell = c;
                if (Thread.currentThread().getId() % 2 == threadNorthID) {
                    int x = 0;
                    while (x < 5) {
                        cell = cell.getPrevious();
                        cell.setSolutionPath(true);
                        if (cell == end.getBottom()) {
                            break;
                        }
                        x++;
                    }
                } else {
                    int y = 0;
                    while (y < 5) {
                        cell = cell.getPrevious();
                        cell.setSolutionPath(true);
                        if (cell == start.getTop()) {
                            break;
                        }
                        y++;
                    }
                }
                return true;
            }


            if (Thread.currentThread().getId() % 2 == threadNorthID) {
                list2.add(c); // if north =>list2
            } else {
                list1.add(c);
            }
        }

        if (c.isVisited()){
            return false;
        }
        try {
            Thread.sleep(waitTime);
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
            Cell cell = c.Direct_Call.get((d));
            // if there's a wall then go to the next direction
            if (cell == null) {
                continue;
            }
            cell.setPrevious(c);
            if (solveRecursive(cell, d)) {
                c.setSolutionPath(true);
                return true;
            }
        }
        return false;
    }
}

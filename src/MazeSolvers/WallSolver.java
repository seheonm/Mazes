//This is the Wall Solver algorithm

package MazeSolvers;

import Maze.Cell;
import Maze.OpeningCell;
import javafx.application.Platform;

import java.util.*;

public class WallSolver extends BaseSolver implements Runnable{
    String current_dir;
    HashMap<String, List<String>> Directions;
    Cell cell;
    Cell cell2;
    long threadNorthID, threadSouthID;
    HashSet<Cell> list1= new HashSet<>();
    HashSet<Cell> list2= new HashSet<>();

    public WallSolver(Cell start, Cell end, Runnable reRender, Runnable clearAllButSolved){
        super(start,end,reRender,clearAllButSolved);
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

        ArrayList<WallSolver> solvers = new ArrayList<>(); // index 0 is up thread, index 1 is down thread
        // array of wall solvers
        // whichever finds water first kills the other thread
        // follow water to opening

        /*
        scenario.elves = new ArrayList<>();
		for(int i = 0; i != 10; i++) {
			Elf elf = new Elf(i+1, scenario);
			scenario.elves.add(elf);
			th = new Thread(elf);
			th.start();
		}

		for (Elf elf : scenario.elves) {
					elf.kill();
		}
		// in Elf class
	    public void kill() {
		    this.alive = false;
	    }
         */

        System.out.println("cell = " + end.getBottom().row + "," + end.getBottom().col);
        new Thread(() -> { // Going down
            System.out.println("thread up ID = " + Thread.currentThread().getId());
            threadSouthID = Thread.currentThread().getId()%2;
            current_dir = "SOUTH";
            end.setVisited(true);
            solveRecursive(end.getBottom(), current_dir);

            Platform.runLater(() -> {
                clearAllButSolved.run();
                reRender.run();
            });
        }).start();


        new Thread(() -> { // Going up
            System.out.println("thread down ID = " + Thread.currentThread().getId());
            threadNorthID = Thread.currentThread().getId()%2;
            current_dir = "NORTH";
            start.setVisited(true);
            solveRecursive(start.getTop(), current_dir);
            Platform.runLater(() -> {
                clearAllButSolved.run();
                reRender.run();
            });
        }).start();
    }

    /**
     * Solves the Wall Solver algorithm recursively
     * @param c of type Cell
     * @param current_dir of type String
     * @return boolean to check if there is a solution
     */
    private boolean solveRecursive(Cell c, String current_dir){

        if(!Collections.disjoint(list1, list2)) {
            cell = c;
            if(Thread.currentThread().getId() %2 == threadNorthID) {
                int x = 0;
                while (x < 5) {
                    cell = cell.getPrevious();
                    System.out.println("CELL = " + cell.row + "," + cell.col);
                    cell.setSolutionPath(true);
                    if (cell == end.getBottom()) {
                        System.out.println("got to the end");
                        break;
                    }
                    x++;
                }
            }else{
                int y = 0;
                while (y < 5) {
                    cell = cell.getPrevious();
                    System.out.println("CELL = " + cell.row + "," + cell.col);
                    cell.setSolutionPath(true);
                    if (cell == start.getTop()) {
                        System.out.println("got to the end");
                        break;
                    }
                    y++;
                }
            }
            return true;
        }


        if(Thread.currentThread().getId() %2 == threadNorthID) {
            list2.add(c); // if north =>list2
        }
        else{
            list1.add(c);
        }
//        if(Thread.currentThread().getId() %2 == 0){
//            list1.add(c);
//        }else{
//            list2.add(c);
//        }





        if (c.isVisited()){
            return false;
        }
        try {
            Thread.sleep(100);
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
            if (cell == null) { // if there's a wall then go to the next direction
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

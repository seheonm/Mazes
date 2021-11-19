package MazeGenerators;

import Maze.Cell;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.function.Consumer;

public class PrimsGenerator extends MazeGenerator{
    private List<Wall> walls;

    /**
     * Initialize walls
     */
    @Override
    protected void init(){
        walls = new LinkedList<>();
    }

    /**
     * Generate maze using Prim's algorithm
     * @param addToBoard add to GUI board
     */
    @Override
    public void generate(Consumer<Cell> addToBoard) {
        Random r = new Random();
        // pick random cell
        Cell starting = board[r.nextInt(boardSize)][r.nextInt(boardSize)];
        addWallsToList(starting);

        starting.setVisited(true);

        while (!walls.isEmpty()){
            Collections.shuffle(walls);
            Wall w = walls.remove(0);

            Cell c1 = board[w.row][w.col];
            int rowOffset = w.row + (w.right ? 0 : -1);
            int colOffset = w.col + (w.right ? 1 : 0);
            Cell c2 = board[rowOffset][colOffset];

            if ((c1.isVisited() && !c2.isVisited()) ||
                    (!c1.isVisited() && c2.isVisited())){
                if(w.right){
                    c1.setRightNeighbor(c2);
                    c2.setLeftNeighbor(c1);
                }
                else{
                    c1.setTopNeighbor(c2);
                    c2.setBottomNeighbor(c1);
                }
                try {
                    addToBoard.accept(c1);
                    addToBoard.accept(c2);
                    Thread.sleep(waitTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                addWallsToList(c1.isVisited() ? c2 : c1);
                c1.setVisited(true);
                c2.setVisited(true);
            }
        }
    }

    /**
     * Adds walls to the cells for the rows and columns
     * @param c of type Cell for placement
     */
    private void addWallsToList(Cell c){
        if(c.row > 0){
            walls.add(new Wall(c.row, c.col, false));
        }
        if (c.col < boardSize - 1){
            walls.add(new Wall(c.row, c.col, true));
        }
        if (c.row + 1 < boardSize){
            walls.add(new Wall(c.row + 1, c.col, false));
        }
        if (c.col - 1 >= 0){
            walls.add((new Wall(c.row, c.col - 1, true)));
        }
    }
}

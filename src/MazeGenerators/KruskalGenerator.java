//Generator using Kruskal Generator algorithm
package MazeGenerators;

import Maze.Cell;

import java.util.*;
import java.util.function.Consumer;

public class KruskalGenerator extends MazeGenerator {
    private List<Set<Cell>> cellSets;
    private List<Wall> walls;

    @Override
    protected void init(){
        cellSets = new LinkedList<>();
        walls = new LinkedList<>();


        // list of all wall indexes
        for(int i = 0; i < boardSize; i++){
            for(int j = 0; j < boardSize ; j ++){
                if(i != 0){
                    walls.add(new Wall(i, j, false));
                }
                if (j != boardSize - 1) {
                    walls.add(new Wall(i, j, true));
                }
            }
        }

        // create set for each cell
        for(Cell[] row : board){
            for(Cell c : row){
                cellSets.add(new HashSet<>(){{
                    add(c);
                }});
            }
        }
    }

    /**
     * Generate maze using Kruskal's algorithm
     * @param addToBoard add to GUI board
     */
    @Override
    public void generate(Consumer<Cell> addToBoard) {
        Collections.shuffle(walls);
        for (Wall w : walls){
            Cell c1 = board[w.row][w.col];
            int rowOffset = w.row + (w.right ? 0 : -1);
            int colOffset = w.col + (w.right ? 1 : 0);
            Cell c2 = board[rowOffset][colOffset];

            Set<Cell> c1Set = null;
            Set<Cell> c2Set = null;
            for(Set<Cell> set : cellSets){
                if (set.contains(c1)){
                    c1Set = set;
                }
                if (set.contains(c2)){
                    c2Set = set;
                }
            }

            if(c1Set == null){
                System.out.println();
            }
            if(!c1Set.contains(c2)){
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
                cellSets.remove(c2Set);
                c1Set.addAll(c2Set);
            }
        }
    }
}

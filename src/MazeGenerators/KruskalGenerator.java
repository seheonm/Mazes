package MazeGenerators;

import Maze.Cell;

import java.util.*;

public class KruskalGenerator extends MazeGenerator {
    private List<Set<Cell>> cellSets;
    private List<Wall> walls;

    public KruskalGenerator(Cell[][] board, int size) {
        super(board, size);

        cellSets = new LinkedList<>();
        walls = new LinkedList<>();

        // list of all wall indexes
        for(int i = 0; i < size; i++){
            for(int j = 0; j < size ; j ++){
                if(i != 0){
                    walls.add(new Wall(i, j, false));
                }
                if (j != size - 1) {
                    walls.add(new Wall(i, j, true));
                }
            }
        }
//        x x x
//        x x x
//        x x x

        // create set for each cell
        for(Cell[] row : board){
            for(Cell c : row){
                cellSets.add(new HashSet<>(){{
                    add(c);
                }});
            }
        }
    }

    @Override
    public void generate() {
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

            if(!c1Set.contains(c2)){
                if(w.right){
                    c1.setRightNeighbor(c2);
                    c2.setLeftNeighbor(c1);
                }
                else{
                    c1.setTopNeighbor(c2);
                    c2.setBottomNeighbor(c1);
                }
                cellSets.remove(c2Set);
                c1Set.addAll(c2Set);
            }
        }
    }
}

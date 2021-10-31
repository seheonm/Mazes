package MazeGenerators;

import Maze.Cell;

import java.util.*;

public class DepthFirstGenerator extends MazeGenerator{
    private Stack<Cell> cellStack;

    @Override
    protected void init(){
        cellStack = new Stack<>();
    }

    @Override
    public void generate(){
        Random r = new Random();
        int row = r.nextInt(boardSize);
        int col = r.nextInt(boardSize);

        cellStack.push(board[row][col]);

        while(!cellStack.isEmpty()){
            Cell current = cellStack.pop();

            current.setVisited(true);


            for(Offset offset : randomNeighbors()){
                int newRow = current.row + offset.upDown;
                int newCol = current.col + offset.leftRight;

                if(checkIsValid(newRow, newCol)){
                    Cell newCell = board[newRow][newCol];
                    cellStack.push(current);

                    if(offset.upDown == 1 && offset.leftRight == 0){
                        current.setBottomNeighbor(newCell);
                        newCell.setTopNeighbor(current);
                    }
                    else if(offset.upDown == -1 && offset.leftRight == 0){
                        current.setTopNeighbor(newCell);
                        newCell.setBottomNeighbor(current);
                    }
                    else if(offset.upDown == 0 && offset.leftRight == 1){
                        current.setRightNeighbor(newCell);
                        newCell.setLeftNeighbor(current);
                    }
                    else if(offset.upDown == 0 && offset.leftRight == -1){
                        current.setLeftNeighbor(newCell);
                        newCell.setRightNeighbor(current);
                    }
                    cellStack.push(newCell);
                    break;
                }
            }
        }
    }

    private boolean checkIsValid(int row, int col){
        if(row >= 0 && row < boardSize && col >= 0 && col < boardSize){
            return !board[row][col].isVisited();
        }
        return false;
    }

    private List<Offset> randomNeighbors()
    {
        List<Offset> neighbors = new LinkedList<>(){{
            add(new Offset(1,0));
            add(new Offset(-1,0));
            add(new Offset(0,1));
            add(new Offset(0,-1));
        }};
        Collections.shuffle(neighbors);
        return neighbors;
    }

    private class Offset{
        public int upDown;
        public int leftRight;

        public Offset(int first, int second){
            this.upDown = first;
            this.leftRight = second;
        }
    }
}

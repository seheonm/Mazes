//This class is all the information for the cells of the maze

package Maze;

import java.util.HashMap;

public class Cell {
    public int row;
    public int col;
    private Cell top;
    private Cell bottom;
    private Cell left;
    private Cell right;
    private Cell previous;
    private boolean visited;
    private boolean solutionPath;
    public HashMap<String, Cell> Direct_Call;

    public Cell(int row, int col){
        this.row = row;
        this.col = col;
        visited = false;
        top = null;
        bottom = null;
        left = null;
        right = null;
    }

    public boolean isVisited() {
        return visited;
    }

    public void setVisited(boolean visited) {
        this.visited = visited;
    }

    public void setTopNeighbor(Cell c){
        this.top = c;
    }
    public void setBottomNeighbor(Cell c){
        this.bottom = c;
    }
    public void setLeftNeighbor(Cell c){
        this.left = c;
    }
    public void setRightNeighbor(Cell c){
        this.right = c;
    }
    public void setPrevious(Cell c) { this.previous = c; }

    public Cell getBottom() {
        return bottom;
    }

    public Cell getTop() {
        return top;
    }

    public Cell getLeft() {
        return left;
    }

    public Cell getRight() {
        return right;
    }

    public Cell getPrevious() { return previous; }

    public void setSolutionPath(boolean solutionPath) {
        this.solutionPath = solutionPath;
    }

    public boolean isSolutionPath() {
        return solutionPath;
    }

    /**
     * Get picture number
     * @return picture number
     */
    public int getPicNumber(){
        if ((top != null || bottom != null) && left == null && right == null){
            return 5;
        }
        else if (top == null && bottom == null && (left != null || right != null)){
            return 6;
        }
        else if (top == null && bottom != null && left != null && right == null){
            return 2;
        }
        else if (top != null && bottom == null && left != null && right == null){
            return 1;
        }
        else if (top == null && bottom != null && left == null && right != null){
            return 3;
        }
        else if (top != null && bottom == null && left == null && right != null){
            return 4;
        }
        else if (top != null && bottom != null && left != null && right != null){
            return 7;
        }
        else if (top != null && bottom != null){
            return (char)(left != null ? 11 : 9);
        }
        else if (left != null && right != null){
            return (char)(top != null ? 8 : 10);
        }
        return 'x';
    }


    /**
     * Get the symbols/chars to display the maze
     * @return symbol/char
     */
    public char getChar(){
        if ((top != null || bottom != null) &&
                left == null && right == null){
            if(top == null) return '╥';
            if (bottom == null) return '╨';
            return '║';
        }
        else if (top == null && bottom == null &&
                (left != null || right != null)){
            if(left == null) return '╞';
            if(right == null) return '╡';
            return '═';
        }
        else if (top == null && bottom != null &&
                left != null && right == null){
            return '╗';
        }
        else if (top != null && bottom == null &&
                left != null && right == null){
            return '╝';
        }
        else if (top == null && bottom != null &&
                left == null && right != null){
            return '╔';
        }
        else if (top != null && bottom == null &&
                left == null && right != null){
            return '╚';
        }
        else if (top != null && bottom != null &&
                left != null && right != null){
            return '╬';
        }
        else if (top != null && bottom != null){
            return (char)(left != null ? '╣' : '╠');
        }
        else if (left != null && right != null){
            return (char)(top != null ? '╩' : '╦');
        }
        // all null
        return 'x';
    }
}

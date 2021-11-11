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

    /**
     * Checks to see if cell is visited
     * @return boolean
     */
    public boolean isVisited() {
        return visited;
    }

    /**
     * Sets the visited cells
     * @param visited of type boolean
     */
    public void setVisited(boolean visited) {
        this.visited = visited;
    }

    /**
     * Sets the top neighbor for the cell
     * @param c of type Cell
     */
    public void setTopNeighbor(Cell c){
        this.top = c;
    }

    /**
     * Sets the bottom neighbor for the cell
     * @param c of type Cell
     */
    public void setBottomNeighbor(Cell c){
        this.bottom = c;
    }

    /**
     * Sets the left neighbor for the cell
     * @param c of type Cell
     */
    public void setLeftNeighbor(Cell c){
        this.left = c;
    }

    /**
     * Sets the right neighbor for the cell
     * @param c of type Cell
     */
    public void setRightNeighbor(Cell c){
        this.right = c;
    }

    /**
     * Sets the previous neighbor for the cell
     * @param c of type Cell
     */
    public void setPrevious(Cell c) { this.previous = c; }

    /**
     * Gets the bottom neighbor of the cell
     * @return of type Cell
     */
    public Cell getBottom() {
        return bottom;
    }

    /**
     * Gets the top neighbor of the cell
     * @return of type Cell
     */
    public Cell getTop() {
        return top;
    }

    /**
     * Gets the left neighbor of the cell
     * @return of type Cell
     */
    public Cell getLeft() {
        return left;
    }

    /**
     * Gets the right neighbor of the cell
     * @return of type Cell
     */
    public Cell getRight() {
        return right;
    }

    /**
     * Gets the previous neighbor of the cell
     * @return of type Cell
     */
    public Cell getPrevious() { return previous; }

    /**
     * Sets the solution path for the maze
     * @param solutionPath of type boolean
     */
    public void setSolutionPath(boolean solutionPath) {
        this.solutionPath = solutionPath;
    }

    /**
     * Checks if there is a solution path
     * @return boolean
     */
    public boolean isSolutionPath() {
        return solutionPath;
    }

    /**
     * Gets the picture number for the maze
     * @return int
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
     * Gets the char for the maze
     * @return char
     */
    public char getChar(){
        if ((top != null || bottom != null) && left == null && right == null){
            if(top == null) return '╥';
            if (bottom == null) return '╨';
            return '║';
        }
        else if (top == null && bottom == null && (left != null || right != null)){
            if(left == null) return '╞';
            if(right == null) return '╡';
            return '═';
        }
        else if (top == null && bottom != null && left != null && right == null){
            return '╗';
        }
        else if (top != null && bottom == null && left != null && right == null){
            return '╝';
        }
        else if (top == null && bottom != null && left == null && right != null){
            return '╔';
        }
        else if (top != null && bottom == null && left == null && right != null){
            return '╚';
        }
        else if (top != null && bottom != null && left != null && right != null){
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

//This class makes the Wall for the maze

package Maze;

public class Wall {
    public int row;
    public int col;
    public boolean right; // 0 up, 1 right

    public Wall(int row, int col, boolean right){
        this.row = row;
        this.col = col;
        this.right = right;
    }
}

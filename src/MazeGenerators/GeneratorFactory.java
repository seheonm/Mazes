//This class determines how to generate the maze

package MazeGenerators;

import Maze.Cell;

public class GeneratorFactory {

    /**
     * Determines which type of maze to generate
     * @param generatorString of type String
     * @param board of type Cell[][]
     * @param size of type int
     * @return MazeGenerator
     */
    public static MazeGenerator generate(String generatorString, Cell[][] board, int size){
        switch (generatorString) {
            case "dfs":
                return new DepthFirstGenerator(board, size);
            case "kruskal":
                return new KruskalGenerator(board, size);
            case "prim":
                return new PrimsGenerator(board, size);
//            case "aldous":
//                return new LightningSolver(start, end, reRender, clearAllButSolved);
            default:
                return null;
        }
    }
}

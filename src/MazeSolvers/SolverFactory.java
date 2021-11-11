//This class determines which soling algorithm to use

package MazeSolvers;

import Maze.Cell;

public class SolverFactory {

    /**
     * Determines which solver to use on the maze
     * @param solverString of type String
     * @param start of type Cell
     * @param end of type Cell
     * @param reRender of type Runnable
     * @param clearAllButSolved of type Runnable
     * @return BaseSolver
     */
    public static BaseSolver generate(String solverString, Cell start,
                                      Cell end, Runnable reRender, Runnable clearAllButSolved){
        switch (solverString) {
            case "tremaux":
                return new TremauxSolver(start, end, reRender, clearAllButSolved);
            case "mouse":
                return new RandomMouseSolver(start, end, reRender, clearAllButSolved);
            case "wall":
                return new WallSolver(start, end, reRender, clearAllButSolved);
//            case "mouse_thread":
//                return new RandomMouseSolver(start, end, reRender, clearAllButSolved);
//            case "wall_thread":
//                return new WallSolver(start, end, reRender, clearAllButSolved);
            case "lightning":
                return new LightningSolver(start, end, reRender, clearAllButSolved);
            default:
                return null;
        }
    }
}

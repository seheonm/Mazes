package Gui;

import Maze.*;
import MazeGenerators.*;

import MazeSolvers.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.awt.image.BufferedImage;
import java.io.*;

public class Main extends Application {
    private MazeGenerator generator = new DepthFirstGenerator();
    private MazeSolver solver = new BasicSolver();
    private Maze m;
    private int[][] paths;

    private static int tileSize;
    private static int boardSize;

    public static void main(String[] args){
        if(args.length != 2){
            System.out.println("Wrong args.");
            System.out.println("first arg: tileSize");
            System.out.println(("second arg: boardSize"));
            Application.launch(args);
            return;
        }
        int ts = Integer.parseInt(args[0]);
        int bs = Integer.parseInt(args[1]);
        tileSize = ts;
        boardSize = bs;
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        if(tileSize == 0 || boardSize == 0){
            Platform.exit();
            System.exit(0);

        }
        paths = new int[boardSize / tileSize][boardSize / tileSize];
        ComboBox comboBox = new ComboBox();
        comboBox.setPromptText("Maze Generators");
        comboBox.getItems().add("Depth First Generator");
        comboBox.getItems().add("Kruskal First Generator");
        comboBox.getItems().add("Prims Generator");
        comboBox.setOnAction(e -> {
            int selectedIndex = comboBox.getSelectionModel().getSelectedIndex();
            if (selectedIndex == 0) {
                generator = new DepthFirstGenerator();
            } else if (selectedIndex == 1) {
                generator = new KruskalGenerator();
            } else if (selectedIndex == 2) {
                generator = new PrimsGenerator();
            }
        });
        comboBox.getSelectionModel().selectFirst();

        ComboBox cb = new ComboBox();
        cb.setPromptText("Solvers");
        cb.getItems().add("Basic Solver");
        cb.getItems().add("Random Mouse Solver");
        cb.getItems().add("Wall Solver");
        cb.getItems().add("Lightning Solver");
        cb.getItems().add("Additional Solver");
        cb.setOnAction(e -> {
            int selectedIndex = cb.getSelectionModel().getSelectedIndex();
            if (selectedIndex == 0) {
                solver = new BasicSolver();
            } else if (selectedIndex == 1) {
                solver = new RandomMouseSolver();
            } else if (selectedIndex == 2) {
                solver = new WallSolver();
            } else if (selectedIndex == 3) {
                solver = new LightningSolver();
            }
        });
        cb.getSelectionModel().selectFirst();

        GridPane pane = new GridPane();
        stage.setTitle("Maze");

        HBox hBox = new HBox();
        BorderPane borderPane = new BorderPane();
        //Pipes need to be transparent before changing the background
        borderPane.setTop(hBox);
        borderPane.setCenter(pane);

        Button button = new Button("Play");
        hBox.getChildren().addAll(button, comboBox, cb);
        button.setOnMouseClicked(event -> {
            Maze m = new Maze(boardSize / tileSize, generator, solver);

            System.out.println("Button Pressed");
            Runnable reRender = () -> {
                reRenderMaze(m, pane, boardSize / tileSize, tileSize);
            };

            m.generate((Cell c) -> {
                Platform.runLater(() -> {
                    ImageView img = getCellImage(tileSize, c.getPicNumber(), false, c.row, c.col, "");
                    pane.getChildren().add(img);
                });
            }, reRender, () -> displayMaze(pane, m, boardSize / tileSize, tileSize));
            button.setVisible(false);
            comboBox.setVisible(false);
            cb.setVisible(false);
        });
        stage.setScene(new Scene(borderPane, boardSize, boardSize + tileSize * 2 + 50));

        stage.show();
    }

    private void reRenderMaze(Maze maze, GridPane pane, int size, int imageSize){
        Cell[][] m = maze.getMaze();
        if(maze.getTopOpening().isVisited() && !maze.isTopSeen()){
            maze.setTopSeen(true);
            Cell top = maze.getTopOpening();
            ImageView topImg = getCellImage(imageSize, top.getPicNumber(), top.isVisited(), 0, top.col, "");
            pane.getChildren().add(topImg);
        }
        if(maze.getBottomOpening().isVisited() && !maze.isBottomSeen()){
            maze.setBottomSeen(true);
            Cell bot = maze.getBottomOpening();
            ImageView botImg = getCellImage(imageSize, bot.getPicNumber(), bot.isVisited(), size+1, bot.col, "");
            pane.getChildren().add(botImg);
        }

        for(int i = 0; i < size; i++){
            for(int j = 0; j < size; j++){
                boolean isLightning = solver instanceof LightningSolver;
                if(m[i][j].isVisited() && !maze.isVisited(i, j)){
                    maze.setVisited(i, j);
                    ImageView img = getCellImage(imageSize, m[i][j].getPicNumber(), true, i+1, j, isLightning ? "_3" : "");
                    pane.getChildren().add(img);
                    paths[i][j] = 15;
                }else if(m[i][j].isVisited() && maze.isVisited(i, j)){
                    if(paths[i][j] < 0){}
                    else if(paths[i][j]/3 == 10){
                        ImageView img = getCellImage(imageSize, m[i][j].getPicNumber(), true, i+1, j, "_2");
                        pane.getChildren().add(img);
                    }else if(paths[i][j]/3 == 5){
                        ImageView img = getCellImage(imageSize, m[i][j].getPicNumber(), true, i+1, j, "_1");
                        pane.getChildren().add(img);
                    }else if(paths[i][j]/3 == 0){
                        ImageView img = getCellImage(imageSize, m[i][j].getPicNumber(), true, i+1, j, "");
                        pane.getChildren().add(img);
                    }
                    if(paths[i][j] > 0) paths[i][j]--;
                }
            }
        }
    }

    private Node getNode(GridPane pane, int row, int col){
        for (Node node : pane.getChildren()) {
            if(GridPane.getRowIndex(node) == row && GridPane.getColumnIndex(node) == col) {
                return node;
            }
        }
        return null;
    }

    /**
     * This method displays the Maze
     * @param pane of type GridPane lays out the children with a flexible grid
     * @param maze of type Maze to be displayed
     * @param size of type int for the size of the maze
     * @param imageSize of type int to add in the image
     */
    private void displayMaze(GridPane pane, Maze maze, int size, int imageSize){
        pane.getChildren().clear();

        Cell top = maze.getTopOpening();
        ImageView topImg = getCellImage(imageSize, top.getPicNumber(), top.isVisited(), 0, top.col, "");

        Cell bot = maze.getBottomOpening();
        ImageView botImg = getCellImage(imageSize, bot.getPicNumber(), bot.isVisited(), size+1, bot.col, "");

        pane.getChildren().addAll(topImg, botImg);

        Cell[][] m = maze.getMaze();
        for(int i = 0; i < size; i++){
            for(int j = 0; j < size; j++){
                Cell c = m[i][j];
                ImageView img = getCellImage(imageSize, c.getPicNumber(), c.isVisited(), i+1, j, "");
                pane.getChildren().add(img);
            }
        }
    }

    /**
     * This method gets the Cell's Image
     * @param imageSize of type int is the size of the image chosen
     * @param imageNum of type int is which image is being used
     * @param visible of type boolean is to check if the image is shown
     * @param row of type int is the row place of the image
     * @param col of type int is the column place of the image
     * @return imageView to see the image
     */
    private ImageView getCellImage(int imageSize, int imageNum, boolean visible, int row, int col, String extra){

        InputStream stream = null;
        String path = "resources/pipes/pipeGreen_" + imageNum + (visible ? "f" + extra : "") +  ".png";
        try {
            stream = new FileInputStream(
                    path);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Image image = new Image(stream, imageSize, imageSize, false, false);
        //Creating the image view
        ImageView imageView = new ImageView();
        imageView.setId(path);
        imageView.setFitHeight(imageSize);
        imageView.setFitWidth(imageSize);
        imageView.setImage(image);
        GridPane.setRowIndex(imageView, row+1);
        GridPane.setColumnIndex(imageView, col);
        try {
            stream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return imageView;
    }
}

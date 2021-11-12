package Gui;

import Maze.*;
import MazeGenerators.*;

import MazeSolvers.BasicSolver;
import MazeSolvers.LightningSolver;
import MazeSolvers.RandomMouseSolver;
import MazeSolvers.WallSolver;
import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
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

    int tileSize = 70; //100
    int boardSize = 500; //600
    Maze m = new Maze(boardSize/tileSize, new DepthFirstGenerator(), new WallSolver());
    public static void main(String[] args){
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {

        ComboBox comboBox = new ComboBox();
        comboBox.setPromptText("Maze Generators");
        comboBox.getItems().add("Depth First Generator");
        comboBox.getItems().add("Kruskal First Generator");
        comboBox.getItems().add("Prims Generator");

        ComboBox cb = new ComboBox();
        cb.setPromptText("Solvers");
        cb.getItems().add("Basic Solver");
        cb.getItems().add("Random Mouse Solver");
        cb.getItems().add("Wall Solver");
        cb.getItems().add("Lightning Solver");
        cb.getItems().add("Additional Solver");


        if(comboBox.getValue() == "Depth First Generator"){
            m = new Maze(boardSize/tileSize, new DepthFirstGenerator(), new BasicSolver());
        }else if(comboBox.getValue() == "Kruskal First Generator"){
            m = new Maze(boardSize/tileSize, new KruskalGenerator(), new BasicSolver());
        }else if(comboBox.getValue() == "Prims Generator"){
            m = new Maze(boardSize/tileSize, new PrimsGenerator(), new BasicSolver());
        }
        if(cb.getValue() == "Basic Solver"){
            m = new Maze(boardSize/tileSize, new DepthFirstGenerator(), new BasicSolver());
        }else if(cb.getValue() == "Random Mouse Solver"){
            m = new Maze(boardSize/tileSize, new DepthFirstGenerator(), new RandomMouseSolver());
        }else if(cb.getValue() == "Wall Solver"){
            m = new Maze(boardSize/tileSize, new DepthFirstGenerator(), new WallSolver());
        }else if(cb.getValue() == "Lightning Solver"){
            m = new Maze(boardSize/tileSize, new DepthFirstGenerator(), new LightningSolver());
        }else if(cb.getValue() == "Additional Solver"){
            m = new Maze(boardSize/tileSize, new DepthFirstGenerator(), new RandomMouseSolver());

        }
        m.generate();

        stage.setTitle("Gui");
        GridPane pane = new GridPane();
        HBox hBox = new HBox();
        BorderPane borderPane = new BorderPane();
        //Pipes need to be transparent before changing the background
//        Image image = new Image(new FileInputStream("resources/Background/bricks.jpeg"));
//        ImageView imageView = new ImageView(image);
//        borderPane.getChildren().add(imageView);
        borderPane.setTop(hBox);
        borderPane.setCenter(pane);

        Button button = new Button("Play");
        hBox.getChildren().addAll(button, comboBox, cb);
        button.setOnMouseClicked(event -> {
            System.out.println("Button Pressed");
            m.printMaze();
            displayMaze(pane, m, boardSize/tileSize, tileSize);
            Runnable reRender = () -> {
                displayMaze(pane, m, boardSize/tileSize, tileSize);
            };

            m.solve(reRender);
            button.setVisible(false);
            comboBox.setVisible(false);
            cb.setVisible(false);
        });

        stage.setScene(new Scene(borderPane, boardSize,boardSize + tileSize * 2));
        stage.show();


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
        ImageView topImg = getCellImage(imageSize, top.getPicNumber(), top.isVisited(), 0, top.col);

        Cell bot = maze.getBottomOpening();
        ImageView botImg = getCellImage(imageSize, bot.getPicNumber(), bot.isVisited(), size+1, bot.col);

        pane.getChildren().addAll(topImg, botImg);

        Cell[][] m = maze.getMaze();
        for(int i = 0; i < size; i++){
            for(int j = 0; j < size; j++){
                Cell c = m[i][j];
                ImageView img = getCellImage(imageSize, c.getPicNumber(), c.isVisited(), i+1, j);
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
    private ImageView getCellImage(int imageSize, int imageNum, boolean visible, int row, int col){
        InputStream stream = null;
        try {
            stream = new FileInputStream(
                    "resources/pipes/pipeGreen_" + imageNum + (visible ? "f" : "") + ".png");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Image image = new Image(stream, imageSize, imageSize, false, false);
        //Creating the image view
        ImageView imageView = new ImageView();
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

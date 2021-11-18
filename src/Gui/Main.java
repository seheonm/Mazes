package Gui;

import Maze.*;
import MazeGenerators.*;

import javafx.application.Application;
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
    Maze maze;
    public static void main(String[] args){
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {

        stage.setTitle("Maze");
        HBox hBox = new HBox();
        BorderPane borderPane = new BorderPane();
        borderPane.setTop(hBox);
        borderPane.setCenter(pane);
        stage.setScene(new Scene(borderPane, boardSize,boardSize + tileSize * 2));
        stage.show();

        //TODO take user input to generate the correct outputs
        //for testing
        String solverString = "lightning";
        String generatorString = "dfs";
        Runnable reRender = () -> {
            displayMaze(pane, maze, boardSize/tileSize, tileSize);
        };
        maze = new Maze(boardSize/tileSize, generatorString, solverString, reRender);
        displayMaze(pane, maze, boardSize/tileSize, tileSize);
        maze.printMaze();
        maze.solve();
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

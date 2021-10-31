package Gui;

import Maze.*;
import MazeGenerators.*;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class Main extends Application {
    public static void main(String[] args){
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        int tileSize = 100;
        int boardSize = 600;

        Maze m = new Maze(boardSize/tileSize, new KruskalGenerator());
        m.generate();

        stage.setTitle("Gui");
        GridPane pane = new GridPane();
        stage.setScene(new Scene(pane, boardSize,boardSize + tileSize * 2));

        m.printMaze();
        displayMaze(pane, m, boardSize/tileSize, tileSize);

        stage.show();
    }

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

    private ImageView getCellImage(int imageSize, int imageNum, boolean visible, int row, int col){
        InputStream stream = null;
        try {
            stream = new FileInputStream(
                    "resources/pipes/pipeGreen_" + imageNum + (visible ? "f" : "") + ".png");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Image image = new Image(stream);
        //Creating the image view
        ImageView imageView = new ImageView();
        imageView.setFitHeight(imageSize);
        imageView.setFitWidth(imageSize);
        imageView.setImage(image);
        GridPane.setRowIndex(imageView, row+1);
        GridPane.setColumnIndex(imageView, col);

        return imageView;
    }
}

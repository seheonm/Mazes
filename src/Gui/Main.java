package Gui;

import Maze.*;
import MazeGenerators.*;

import MazeSolvers.BasicSolver;
import MazeSolvers.LightningSolver;
import MazeSolvers.RandomMouseSolver;
import MazeSolvers.WallSolver;
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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class Main extends Application {

    int tileSize = 100;
    int boardSize = 600;
    Maze m = new Maze(boardSize/tileSize, new DepthFirstGenerator(), new BasicSolver());
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

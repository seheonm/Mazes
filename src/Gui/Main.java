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
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

//import java.awt.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;

public class Main extends Application {
    private MazeGenerator generator;// = new DepthFirstGenerator();
    private MazeSolver solver;// = new BasicSolver();
    private Maze m;
    private int[][] paths;

    private static int tileSize;
    private static int boardSize;

    public static void main(String[] args){
//        if(args.length != 2){
//            System.out.println("Wrong args.");
//            System.out.println("first arg: tileSize");
//            System.out.println(("second arg: boardSize"));
//            Application.launch(args);
//            return;
//        }
//        int ts = Integer.parseInt(args[0]);
//        int bs = Integer.parseInt(args[1]);
//        tileSize = ts;
//        boardSize = bs;
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
//        if(tileSize == 0 || boardSize == 0){
//            Platform.exit();
//            System.exit(0);
//
//        }
//        VBox startPane = new VBox();
        BorderPane borderPane = new BorderPane();
//        Scene scene = new Scene(borderPane);
        Label windowSizeLabel = new Label();
        Label cellSizeLabel = new Label();
        Label generatorLabel = new Label();
        Label solverLabel = new Label();

        Button button = new Button("Play");
        button.setVisible(false);

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose config file");
        Button fileBtn = new Button("Choose file");
//        fileBtn.setPrefWidth(150);
        fileBtn.setOnAction(e -> {
            File selectedFile = fileChooser.showOpenDialog(stage);
            if(selectedFile != null){
                button.setVisible(true);
                fileBtn.setVisible(false);
            }
            System.out.println(selectedFile.getPath());
            try {
                BufferedReader reader = new BufferedReader(new FileReader(selectedFile));

                String windowSize = reader.readLine();
                String cellSize = reader.readLine();
                String gen = reader.readLine();
                String s = reader.readLine();

                windowSizeLabel.setText("WindowSize:" + windowSize);
                cellSizeLabel.setText("CellSize: " + cellSize);
                generatorLabel.setText("Generator: " + gen);
                solverLabel.setText("Solver: " + s);

                boardSize = Integer.parseInt(windowSize);
                tileSize = Integer.parseInt(cellSize);

                if (gen.equals("dfs")) {
                    generator = new DepthFirstGenerator();
                } else if (gen.equals("kruskal")) {
                    generator = new KruskalGenerator();
                } else if (gen.equals("prim")) {
                    generator = new PrimsGenerator();
                }
                if (s.equals("mouse thread")) {
                    solver = new RandomMouseSolver();
                } else if (s.equals("wall")) {
                    solver = new WallSolver();
                } else if (s.equals("lightning")) {
                    solver = new LightningSolver();
                } else  if (s.equals("tremaux")) {
                    solver = new TremauxSolver();
                }

                if(generator == null || solver == null){
                    throw new Exception("Could not parse solver or generator from '" + selectedFile.getPath() + "'.");
                }
//                stage.setScene();
                stage.setWidth(boardSize);
                stage.setHeight(boardSize + (tileSize * 2) + 50);
                paths = new int[boardSize / tileSize][boardSize / tileSize];


            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
//        startPane.getChildren().add(fileBtn);
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
        cb.getItems().add("Tremaux Solver");
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
            } else  if (selectedIndex == 4) {
                solver = new TremauxSolver();
            }
        });
        cb.getSelectionModel().selectFirst();

        GridPane pane = new GridPane();
        stage.setTitle("Maze");

        HBox hBox = new HBox();
        hBox.setSpacing(20);

        //Pipes need to be transparent before changing the background
        borderPane.setTop(hBox);
        borderPane.setCenter(pane);

//        hBox.getChildren().addAll(button, comboBox, cb, fileBtn);
        hBox.getChildren().addAll(fileBtn, windowSizeLabel, cellSizeLabel, generatorLabel, solverLabel, button);

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
        stage.setScene(new Scene(borderPane, 300, 100));//, boardSize, boardSize + tileSize * 2 + 50));

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

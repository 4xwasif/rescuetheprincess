package com.example.demo.gameboard;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Cell;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import java.util.Random;

import java.net.URL;

public class Gameboard extends Application {
    // 🔹 Grid constants
    private static final int ROWS = 10;
    private static final int COLS = 10;
    private static final int SCENE_WIDTH = 820;
    private static final int SCENE_HEIGHT = 800;

    enum CellType {
        GRASS, PLAYER, PRINCESS, BOMB, WALL
    }

    // 🔹 Use "matrix" instead of "map"
    private CellType[][] matrix = new CellType[ROWS][COLS];

    @Override
    public void start(Stage stage) {

        initMatrix();

        GridPane grid = new GridPane();
        drawBoard(grid);

        BorderPane root = new BorderPane();
        root.setCenter(grid);

        Scene scene = new Scene(root, SCENE_WIDTH,SCENE_HEIGHT);

        stage.setTitle("Rescue the Princess");
        stage.setScene(scene);
        stage.show();
    }

    private void initMatrix() {
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                matrix[r][c] = CellType.GRASS;
                matrix[0][c] = CellType.WALL;
                matrix[r][0] = CellType.WALL;
                matrix[9][c] = CellType.WALL;
                matrix[r][9] = CellType.WALL;
            }
        }
        // bomb placement
        Random rng = new Random();
        int[] bomb1 = {0, 0};
        int[] bomb2 = {1, 0};
        int[] bomb3 = {0, 1};
        int[] bomb4 = {2, 0};
        int[] bomb5 = {0, 2};

        do{
            bomb1[0] = rng.nextInt(1, 8);
            bomb1[1] = rng.nextInt(1, 8);
        }while(bomb1[0] <= 1 && bomb1[0] >= 8);

        do{
            bomb2[0] = rng.nextInt(1, 8);
            bomb2[1] = rng.nextInt(1, 8);
        }while(bomb2[0] <= 1 && bomb2[0] >= 8);

        do{
            bomb3[0] = rng.nextInt(1, 8);
            bomb3[1] = rng.nextInt(1, 8);
        }while(bomb3[0] <= 1 && bomb3[0] >= 8);

        do{
            bomb4[0] = rng.nextInt(1, 8);
            bomb4[1] = rng.nextInt(1, 8);
        }while(bomb4[0] <= 1 && bomb4[0] >= 8);

        do{
            bomb5[0] = rng.nextInt(1, 8);
            bomb5[1] = rng.nextInt(1, 8);
        }while(bomb5[0] <= 1 && bomb5[0] >= 8);

        // Sample objects
        matrix[1][1] = CellType.PLAYER;
        matrix[8][8] = CellType.PRINCESS;
        matrix[bomb1[0]][bomb1[1]] = CellType.BOMB;
        matrix[bomb2[0]][bomb2[1]] = CellType.BOMB;
        matrix[bomb3[0]][bomb3[1]] = CellType.BOMB;
        matrix[bomb4[0]][bomb4[1]] = CellType.BOMB;
        matrix[bomb5[0]][bomb5[1]] = CellType.BOMB;
    }

    private void drawBoard(GridPane grid) {
        grid.getChildren().clear();

        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {

                StackPane cell = new StackPane();
                cell.setPrefSize(80, 80);
                cell.setStyle("-fx-border-color: black; -fx-background-color: beige;");



                URL pictureUrl = this.getClass().getResource("grass.png");





                Label label = new Label();

                if(matrix[row][col] == CellType.PLAYER ) {
                    pictureUrl = this.getClass().getResource("player.png");
                }else if(matrix[row][col] == CellType.PRINCESS ) {
                    pictureUrl = this.getClass().getResource("princess.png");
                }else if(matrix[row][col] == CellType.BOMB){
                    pictureUrl = this.getClass().getResource("bomb.png");
                }else if(matrix[row][col] == CellType.WALL){
                    pictureUrl = this.getClass().getResource("wall.png");
                }else{
                    label.setText("");
                }

                Image image = new Image(String.valueOf(pictureUrl));
                ImageView imageView = new ImageView(image);
                imageView.setFitHeight(80);
                imageView.setFitWidth(80);

                cell.getChildren().add(imageView);
                grid.add(cell, col, row);
            }
        }
    }


}
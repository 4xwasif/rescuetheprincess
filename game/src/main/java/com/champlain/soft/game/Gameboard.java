package com.champlain.soft.game;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import java.util.Random;

public class Gameboard extends Application {

    private static final int SCENE_WIDTH = 800;
    private static final int SCENE_HEIGHT = 800;
    private static final int CELL_SIZE = 80;
    private static final int ROWS = 10;
    private static final int COLS = 10;

    private CellType[][] matrix = new CellType[ROWS][COLS];
    private int playerRow = 1, playerCol = 1;
    private int princessRow, princessCol;
    private boolean gameActive = true;  // CHECKPOINT 6: Game state
    private GridPane gridPane;

    private Image grassImage;
    private Image playerImage;
    private Image princessImage;
    private Image wallImage;
    private Image bombImage;

    enum CellType {
        GRASS, PLAYER, PRINCESS, BOMB, WALL
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        loadImages();
        initMatrix();

        gridPane = new GridPane();
        drawBoard(gridPane);

        BorderPane root = new BorderPane();
        root.setCenter(gridPane);

        Scene scene = new Scene(root, SCENE_WIDTH, SCENE_HEIGHT);

        scene.setOnKeyPressed(event -> {
            if (gameActive) {  // Only move if game is active
                switch (event.getCode()) {
                    case UP: movePlayer(-1, 0); break;
                    case DOWN: movePlayer(1, 0); break;
                    case LEFT: movePlayer(0, -1); break;
                    case RIGHT: movePlayer(0, 1); break;
                    default: break;
                }
            }
        });

        stage.setTitle("Rescue the Princess");
        stage.setScene(scene);
        stage.show();
        gridPane.requestFocus();
    }

    private void loadImages() {
        String imagePath = "file:C:/Users/somap/rescuetheprincess/game/src/images/";

        grassImage = new Image(imagePath + "grass.png");
        playerImage = new Image(imagePath + "player.png");
        princessImage = new Image(imagePath + "princess.png");
        wallImage = new Image(imagePath + "wall.png");
        bombImage = new Image(imagePath + "bomb.png");
    }

    private void initMatrix() {
        Random rand = new Random();

        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                matrix[r][c] = CellType.GRASS;
            }
        }

        // Walls on perimeter
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                if (r == 0 || r == ROWS - 1 || c == 0 || c == COLS - 1) {
                    matrix[r][c] = CellType.WALL;
                }
            }
        }

        // Player at [1,1]
        playerRow = 1;
        playerCol = 1;
        matrix[playerRow][playerCol] = CellType.PLAYER;

        // Princess randomly placed
        do {
            princessRow = rand.nextInt(ROWS);
            princessCol = rand.nextInt(COLS);
        } while (matrix[princessRow][princessCol] != CellType.GRASS);
        matrix[princessRow][princessCol] = CellType.PRINCESS;

        // CHECKPOINT 6: Bombs visible for testing
        int bombCount = 5;
        int placedBombs = 0;
        while (placedBombs < bombCount) {
            int bombRow = rand.nextInt(ROWS);
            int bombCol = rand.nextInt(COLS);
            if (matrix[bombRow][bombCol] == CellType.GRASS) {
                matrix[bombRow][bombCol] = CellType.BOMB;
                placedBombs++;
            }
        }
    }

    private void drawBoard(GridPane grid) {
        grid.getChildren().clear();

        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                ImageView imageView = new ImageView();
                imageView.setFitWidth(CELL_SIZE);
                imageView.setFitHeight(CELL_SIZE);
                imageView.setPreserveRatio(false);

                switch (matrix[row][col]) {
                    case WALL:
                        imageView.setImage(wallImage);
                        break;
                    case PLAYER:
                        imageView.setImage(playerImage);
                        break;
                    case PRINCESS:
                        imageView.setImage(princessImage);
                        break;
                    case BOMB:
                        imageView.setImage(bombImage);  // CHECKPOINT 6: Visible
                        break;
                    default:
                        imageView.setImage(grassImage);
                        break;
                }

                grid.add(imageView, col, row);
            }
        }
    }

    private void movePlayer(int deltaRow, int deltaCol) {
        if (!gameActive) {
            return;
        }

        int newRow = playerRow + deltaRow;
        int newCol = playerCol + deltaCol;

        if (newRow < 0 || newRow >= ROWS || newCol < 0 || newCol >= COLS) {
            return;
        }

        if (matrix[newRow][newCol] == CellType.WALL) {
            return;
        }

        // CHECKPOINT 6: Bomb collision
        if (matrix[newRow][newCol] == CellType.BOMB) {
            gameActive = false;
            showGameOver("GAME OVER! You stepped on a bomb!");
            return;
        }

        // Move player
        matrix[playerRow][playerCol] = CellType.GRASS;
        playerRow = newRow;
        playerCol = newCol;
        matrix[playerRow][playerCol] = CellType.PLAYER;

        drawBoard(gridPane);

        // Win condition
        if (playerRow == princessRow && playerCol == princessCol) {
            gameActive = false;
            showWinMessage();
        }
    }

    private void showWinMessage() {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Victory!");
        alert.setHeaderText(null);
        alert.setContentText("YOU WIN! You rescued the princess!");
        alert.showAndWait();
    }

    // CHECKPOINT 6: Game over method
    private void showGameOver(String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Game Over");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
package com.champlain.soft.game;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
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
    private boolean gameActive = true;
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

        // MOVEMENT CONTROLS
        scene.setOnKeyPressed(event -> {
            if (gameActive) {
                handleMovement(event.getCode());
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

        // Initialize all as GRASS
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

        // Random princess
        do {
            princessRow = rand.nextInt(ROWS);
            princessCol = rand.nextInt(COLS);
        } while (matrix[princessRow][princessCol] != CellType.GRASS);
        matrix[princessRow][princessCol] = CellType.PRINCESS;

        // 5 Bombs randomly placed
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
                        imageView.setImage(bombImage);
                        break;
                    default:
                        imageView.setImage(grassImage);
                        break;
                }

                grid.add(imageView, col, row);
            }
        }
    }

    private void handleMovement(KeyCode key) {
        int newRow = playerRow;
        int newCol = playerCol;

        switch (key) {
            case UP: newRow--; break;
            case DOWN: newRow++; break;
            case LEFT: newCol--; break;
            case RIGHT: newCol++; break;
            default: return;
        }

        // Check bounds
        if (newRow < 0 || newRow >= ROWS || newCol < 0 || newCol >= COLS) {
            return;
        }

        // Check wall collision
        if (matrix[newRow][newCol] == CellType.WALL) {
            return;
        }

        // Check bomb collision - GAME OVER
        if (matrix[newRow][newCol] == CellType.BOMB) {
            gameActive = false;
            showGameOver("💥 GAME OVER! You stepped on a bomb!", false);
            return;
        }

        // Move player
        matrix[playerRow][playerCol] = CellType.GRASS;
        playerRow = newRow;
        playerCol = newCol;
        matrix[playerRow][playerCol] = CellType.PLAYER;

        drawBoard(gridPane);

        // Check win condition - RESCUED PRINCESS
        if (playerRow == princessRow && playerCol == princessCol) {
            gameActive = false;
            showGameOver("🎉 YOU WIN! You rescued the princess! 🎉", true);
        }
    }

    private void showGameOver(String message, boolean isWin) {
        Alert alert = new Alert(isWin ? Alert.AlertType.INFORMATION : Alert.AlertType.ERROR);
        alert.setTitle(isWin ? "Victory!" : "Game Over");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
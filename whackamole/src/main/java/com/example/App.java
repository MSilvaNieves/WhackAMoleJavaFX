package com.example;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;

public class App extends Application {
    private Game game;
    private final List<StackPane> cells = new ArrayList<>();
    private Label scoreLabel;
    private Label timerLabel;
    private Label missLabel;
    private Timeline timeline;

    @Override
    public void start(Stage stage) {
        Player player = new Player("Player");
        Board board = new Board(3);
        game = new Game(board, player);

        scoreLabel = new Label("Score: 0");
        timerLabel = new Label("Time: 60");
        missLabel = new Label("Misses: 0");

        VBox root = new VBox(10);
        root.setPadding(new Insets(15));
        root.setAlignment(Pos.CENTER);
        root.getChildren().addAll(buildInfoPanel(), buildBoardGrid(), buildControls());

        Scene scene = new Scene(root, 420, 520);
        stage.setTitle("Whack-a-Mole");
        stage.setScene(scene);
        stage.show();
    }

    private GridPane buildBoardGrid() {
        GridPane grid = new GridPane();
        grid.setHgap(8);
        grid.setVgap(8);
        grid.setAlignment(Pos.CENTER);

        Board board = game.getBoard();
        for (int index = 0; index < board.getSize() * board.getSize(); index++) {
            StackPane cell = new StackPane();
            cell.setPrefSize(105, 105);
            cell.setStyle("-fx-background-color: #a4c3a2; -fx-border-color: #4b6b47;");
            int spotIndex = index;
            cell.setOnMouseClicked(event -> onCellClicked(spotIndex));
            cells.add(cell);
            grid.add(cell, index % board.getSize(), index / board.getSize());
        }
        return grid;
    }

    private VBox buildInfoPanel() {
        VBox panel = new VBox(5, scoreLabel, timerLabel, missLabel);
        panel.setAlignment(Pos.CENTER);
        return panel;
    }

    private HBox buildControls() {
        Button startButton = new Button("Start");
        startButton.setOnAction(event -> startGameTimeline());
        HBox controls = new HBox(10, startButton);
        controls.setAlignment(Pos.CENTER);
        return controls;
    }

    private void startGameTimeline() {
        if (timeline != null) {
            timeline.stop();
        }
        game.startGame();
        updateDisplay();

        timeline = new Timeline(
                new KeyFrame(Duration.seconds(1), event -> {
                    game.countDownTimer();
                    showNextMole();
                    updateDisplay();
                }),
                new KeyFrame(Duration.millis(500), event -> showNextMole())
        );
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    private void showNextMole() {
        if (!game.isRunning()) {
            if (timeline != null) {
                timeline.stop();
            }
            return;
        }
        for (int index = 0; index < game.getBoard().getSize() * game.getBoard().getSize(); index++) {
            Mole mole = game.getBoard().getMole(index);
            if (!mole.isVisible()) {
                cells.get(index).setStyle("-fx-background-color: #a4c3a2; -fx-border-color: #4b6b47;");
            }
        }

        int spot = game.getBoard().chooseNextSpot();
        if (spot >= 0) {
            Mole mole = game.getBoard().getMole(spot);
            mole.appear(1500);
            cells.get(spot).setStyle("-fx-background-color: #7a4b32; -fx-border-color: #4b2d1f;");
        }
    }

    private void updateDisplay() {
        scoreLabel.setText("Score: " + game.getScore());
        timerLabel.setText("Time: " + game.getTimeRemaining());
        missLabel.setText("Misses: " + game.getMissCount());
    }

    private void onCellClicked(int index) {
        if (!game.isRunning()) {
            return;
        }
        Mole mole = game.getBoard().getMole(index);
        if (mole.isVisible()) {
            game.checkHit(index);
            cells.get(index).setStyle("-fx-background-color: #a4c3a2; -fx-border-color: #4b6b47;");
        } else {
            game.recordMiss();
        }
        updateDisplay();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

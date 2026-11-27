package com.example;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AppTest {
    @BeforeAll
    static void startJavaFx() throws Exception {
        CountDownLatch started = new CountDownLatch(1);
        Platform.startup(started::countDown);
        assertTrue(started.await(10, TimeUnit.SECONDS), "JavaFX toolkit did not start");
    }

    @AfterAll
    static void stopJavaFx() {
        Platform.exit();
    }

    @Test
    @Order(1)
    void buildsAndOperatesTheGameInterface() throws Exception {
        runOnFxThread(() -> {
            App app = new App();
            Stage stage = new Stage();
            app.start(stage);

            Scene scene = stage.getScene();
            VBox root = (VBox) scene.getRoot();
            VBox info = (VBox) root.getChildren().get(0);
            GridPane grid = (GridPane) root.getChildren().get(1);
            HBox controls = (HBox) root.getChildren().get(2);
            Label score = (Label) info.getChildren().get(0);
            Label timer = (Label) info.getChildren().get(1);
            Label misses = (Label) info.getChildren().get(2);
            Button start = (Button) controls.getChildren().get(0);

            assertEquals("Whack-a-Mole", stage.getTitle());
            assertEquals(9, grid.getChildren().size());
            assertEquals("Score: 0", score.getText());
            assertEquals("Time: 60", timer.getText());
            assertEquals("Misses: 0", misses.getText());

            @SuppressWarnings("unchecked")
            List<Node> cells = (List<Node>) (List<?>) grid.getChildren();
            start.fire();
            Game game = (Game) readField(app, "game");
            ((javafx.animation.Timeline) readField(app, "timeline")).stop();
            assertTrue(game.isRunning());
            assertEquals("Time: 60", timer.getText());

            click(cells.get(0));
            assertEquals("Misses: 1", misses.getText());

            Mole mole = game.getBoard().getMole(1);
            mole.appear(10_000);
            click(cells.get(1));
            assertEquals("Score: 1", score.getText());
            assertEquals(1, game.getPlayer().getHighScore());

            start.fire();
            ((javafx.animation.Timeline) readField(app, "timeline")).stop();
            assertEquals("Score: 0", score.getText());
            assertEquals("Misses: 0", misses.getText());

            invoke(app, "showNextMole");
            assertEquals(1, game.getBoard().getActiveMoleCount());
            game.endGame();
            invoke(app, "showNextMole");
            click(cells.get(0));
            assertEquals("Score: 0", score.getText());
            stage.close();
        });
    }

    @Test
    @Order(2)
    void mainDelegatesToJavaFxApplicationLauncher() {
        String[] args = {"test"};
        try (MockedStatic<javafx.application.Application> launcher =
                     Mockito.mockStatic(javafx.application.Application.class)) {
            App.main(args);
            launcher.verify(() -> javafx.application.Application.launch(args));
        }
    }

    private static void click(Node cell) {
        @SuppressWarnings("unchecked")
        EventHandler<MouseEvent> handler = (EventHandler<MouseEvent>) cell.getOnMouseClicked();
        handler.handle(null);
    }

    private static Object readField(Object instance, String name) throws Exception {
        Field field = App.class.getDeclaredField(name);
        field.setAccessible(true);
        return field.get(instance);
    }

    private static void invoke(Object instance, String name) throws Exception {
        Method method = App.class.getDeclaredMethod(name);
        method.setAccessible(true);
        method.invoke(instance);
    }

    private static void runOnFxThread(ThrowingRunnable action) throws Exception {
        CountDownLatch completed = new CountDownLatch(1);
        AtomicReference<Throwable> failure = new AtomicReference<>();
        Platform.runLater(() -> {
            try {
                action.run();
            } catch (Throwable ex) {
                failure.set(ex);
            } finally {
                completed.countDown();
            }
        });
        assertTrue(completed.await(10, TimeUnit.SECONDS), "JavaFX action did not complete");
        if (failure.get() != null) {
            throw new AssertionError("JavaFX action failed", failure.get());
        }
    }

    @FunctionalInterface
    private interface ThrowingRunnable {
        void run() throws Exception;
    }
}
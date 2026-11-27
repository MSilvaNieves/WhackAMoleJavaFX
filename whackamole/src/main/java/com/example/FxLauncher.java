package com.example;

import javafx.application.Application;

/**
 * Provides a non-{@link Application} entry point for launching the JavaFX UI.
 */
public class FxLauncher {
    /**
     * Launches the Whack-a-Mole JavaFX application.
     *
     * @param args command-line arguments passed to JavaFX
     */
    public static void main(String[] args) {
        Application.launch(App.class, args);
    }
}

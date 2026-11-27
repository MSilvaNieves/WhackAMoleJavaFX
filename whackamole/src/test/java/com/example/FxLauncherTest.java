package com.example;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class FxLauncherTest {
    @Test
    void launchesTheJavaFxApplicationWithProvidedArguments() {
        String[] args = {"test"};
        try (MockedStatic<javafx.application.Application> launcher =
                     Mockito.mockStatic(javafx.application.Application.class)) {
            assertDoesNotThrow(() -> FxLauncher.main(args));
            launcher.verify(() -> javafx.application.Application.launch(App.class, args));
        }
    }
}
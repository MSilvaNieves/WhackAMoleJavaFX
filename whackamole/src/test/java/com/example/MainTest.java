package com.example;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertTrue;

class MainTest {
    @Test
    void consoleDemoPrintsGameResults() {
        PrintStream originalOut = System.out;
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        try {
            System.setOut(new PrintStream(output, true, StandardCharsets.UTF_8));
            Main.main(new String[0]);
        } finally {
            System.setOut(originalOut);
        }

        String text = output.toString(StandardCharsets.UTF_8);
        assertTrue(text.contains("Player: Java Player"));
        assertTrue(text.contains("Hit successful: true"));
        assertTrue(text.contains("Score: 1"));
        assertTrue(text.contains("High score: 1"));
        assertTrue(text.contains("Miss count: 1"));
        assertTrue(text.contains("Game running: false"));
    }
}
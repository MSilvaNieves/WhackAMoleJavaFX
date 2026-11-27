package com.example;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {
    @Test
    void trimsAndUpdatesPlayerName() {
        Player player = new Player("  Sam  ");
        assertEquals("Sam", player.getName());
        player.setName("  Alex ");
        assertEquals("Alex", player.getName());
    }

    @Test
    void rejectsBlankNames() {
        assertThrows(IllegalArgumentException.class, () -> new Player(null));
        assertThrows(IllegalArgumentException.class, () -> new Player("  "));
        Player player = new Player("Valid");
        assertThrows(IllegalArgumentException.class, () -> player.setName(""));
    }

    @Test
    void validatesAndStoresHighScore() {
        Player player = new Player("Valid");
        assertEquals(0, player.getHighScore());
        player.setHighScore(12);
        assertEquals(12, player.getHighScore());
        assertThrows(IllegalArgumentException.class, () -> player.setHighScore(-1));
    }
}
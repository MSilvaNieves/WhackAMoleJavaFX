package com.example;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {
    @Test
    void validatesDependenciesAndStartsWithExpectedState() {
        Player player = new Player("Test");
        Board board = new Board(2);
        assertThrows(IllegalArgumentException.class, () -> new Game(null, player));
        assertThrows(IllegalArgumentException.class, () -> new Game(board, null));

        Game game = new Game(board, player);
        assertEquals(board, game.getBoard());
        assertEquals(player, game.getPlayer());
        assertEquals(0, game.getScore());
        assertEquals(0, game.getMissCount());
        assertEquals(60, game.getTimeRemaining());
        assertEquals(3, game.getMaxMisses());
        assertFalse(game.isRunning());

        board.getMole(0).appear(10_000);
        game.startGame();
        assertTrue(game.isRunning());
        assertFalse(board.getMole(0).isVisible());
    }

    @Test
    void recordsHitsMissesAndUpdatesHighScore() {
        Player player = new Player("Test");
        Game game = new Game(new Board(2), player);
        assertFalse(game.checkHit(0));

        game.startGame();
        assertFalse(game.checkHit(-1));
        assertEquals(0, game.getMissCount());

        game.getBoard().getMole(0).appear(10_000);
        assertTrue(game.checkHit(0));
        assertEquals(1, game.getScore());
        assertEquals(1, player.getHighScore());
        assertFalse(game.checkHit(0));
        assertEquals(1, game.getMissCount());
    }

    @Test
    void endsAfterMaximumMissesAndCanBeRestarted() {
        Player player = new Player("Test");
        Game game = new Game(new Board(2), player);
        game.recordMiss();
        assertEquals(0, game.getMissCount());

        game.startGame();
        game.recordMiss();
        game.recordMiss();
        assertTrue(game.isRunning());
        game.recordMiss();
        assertFalse(game.isRunning());
        assertEquals(3, game.getMissCount());

        game.startGame();
        assertTrue(game.isRunning());
        assertEquals(0, game.getScore());
        assertEquals(0, game.getMissCount());
        assertEquals(60, game.getTimeRemaining());
    }

    @Test
    void countsDownOnlyWhileRunningAndEndsAtZero() {
        Game game = new Game(new Board(1), new Player("Test"));
        game.countDownTimer();
        assertEquals(60, game.getTimeRemaining());

        game.startGame();
        for (int i = 0; i < 59; i++) {
            game.countDownTimer();
        }
        assertTrue(game.isRunning());
        assertEquals(1, game.getTimeRemaining());
        game.countDownTimer();
        assertFalse(game.isRunning());
        assertEquals(0, game.getTimeRemaining());
        game.countDownTimer();
        assertEquals(0, game.getTimeRemaining());
    }

    @Test
    void endGameKeepsHighScoreAndHidesMoles() {
        Player player = new Player("Test");
        Game game = new Game(new Board(1), player);
        game.startGame();
        game.getBoard().getMole(0).appear(10_000);
        assertTrue(game.checkHit(0));
        game.getBoard().getMole(0).appear(10_000);
        game.endGame();
        assertFalse(game.isRunning());
        assertEquals(1, player.getHighScore());
        assertFalse(game.getBoard().getMole(0).isVisible());
    }
}
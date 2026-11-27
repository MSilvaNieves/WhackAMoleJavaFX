package com.example;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BoardTest {
    @Test
    void initializesAndRecreatesRowMajorMoles() {
        Board board = new Board(2);
        assertEquals(2, board.getSize());
        assertEquals(3, board.getMaxActiveMoles());
        assertEquals(0, board.getActiveMoleCount());
        assertEquals(0, board.getMole(0).getRow());
        assertEquals(0, board.getMole(0).getCol());
        assertEquals(0, board.getMole(1).getRow());
        assertEquals(1, board.getMole(1).getCol());
        assertEquals(1, board.getMole(2).getRow());
        assertEquals(0, board.getMole(2).getCol());

        Mole previous = board.getMole(0);
        board.setupBoard();
        assertNotSame(previous, board.getMole(0));
        assertEquals(4, board.getSize() * board.getSize());
    }

    @Test
    void validatesSizeAndMoleIndexes() {
        assertThrows(IllegalArgumentException.class, () -> new Board(0));
        assertThrows(IllegalArgumentException.class, () -> new Board(-1));
        Board board = new Board(1);
        assertThrows(IndexOutOfBoundsException.class, () -> board.getMole(-1));
        assertThrows(IndexOutOfBoundsException.class, () -> board.getMole(1));
    }

    @Test
    void selectsValidSpotsAndAvoidsRepeatingWhenPossible() {
        Board board = new Board(3);
        int first = board.chooseNextSpot();
        int second = board.chooseNextSpot();
        assertTrue(first >= 0 && first < 9);
        assertTrue(second >= 0 && second < 9);
        assertNotEquals(first, second);

        Board singleSpot = new Board(1);
        assertEquals(0, singleSpot.chooseNextSpot());
        assertEquals(0, singleSpot.chooseNextSpot());
    }

    @Test
    void overcrowdingKeepsThePreviousSpotAndResetHidesAllMoles() {
        Board board = new Board(3);
        int previousSpot = board.chooseNextSpot();
        board.getMole(0).appear(10_000);
        board.getMole(1).appear(10_000);
        board.getMole(2).appear(10_000);
        assertEquals(3, board.getActiveMoleCount());
        assertTrue(board.isOvercrowded());
        assertEquals(previousSpot, board.chooseNextSpot());

        board.resetBoard();
        assertEquals(0, board.getActiveMoleCount());
        assertFalse(board.isOvercrowded());
        assertTrue(board.getMole(0).isExpired());
        int afterReset = board.chooseNextSpot();
        assertTrue(afterReset >= 0 && afterReset < 9);
    }
}
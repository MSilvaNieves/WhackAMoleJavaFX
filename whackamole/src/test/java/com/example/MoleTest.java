package com.example;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MoleTest {
    @Test
    void storesCoordinatesAndStartsHidden() {
        Mole mole = new Mole(2, 4);
        assertEquals(2, mole.getRow());
        assertEquals(4, mole.getCol());
        assertFalse(mole.isVisible());
        assertFalse(mole.isExpired());
    }

    @Test
    void appearsCanBeHitAndCanBeHidden() {
        Mole mole = new Mole(0, 0);
        assertThrows(IllegalArgumentException.class, () -> mole.appear(-1));

        mole.appear(10_000);
        assertTrue(mole.isVisible());
        assertFalse(mole.isExpired());
        assertTrue(mole.hit());
        assertFalse(mole.isVisible());
        assertFalse(mole.hit());

        mole.appear(10_000);
        mole.hide();
        assertFalse(mole.isVisible());
    }

    @Test
    void expiresImmediatelyForZeroDuration() {
        Mole mole = new Mole(1, 1);
        mole.appear(0);
        assertTrue(mole.isExpired());
        assertFalse(mole.isVisible());
        assertFalse(mole.hit());
    }
}
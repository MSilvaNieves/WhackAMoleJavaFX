package com.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Represents the square play area and the mole at each board position.
 */
public class Board {
    private final int size;
    private final List<Mole> moles;
    private int lastSpotIndex;
    private int maxActiveMoles;
    private final Random random;

    /**
     * Creates a {@code size} by {@code size} board and initializes its moles.
     *
     * @param size number of rows and columns; must be positive
     * @throws IllegalArgumentException if {@code size} is not positive
     */
    public Board(int size) {
        if (size <= 0) {
            throw new IllegalArgumentException("size must be positive");
        }
        this.size = size;
        this.moles = new ArrayList<>();
        this.lastSpotIndex = -1;
        this.maxActiveMoles = 3;
        this.random = new Random();
        setupBoard();
    }

    /**
     * Recreates the board's moles in row-major order.
     */
    public void setupBoard() {
        moles.clear();
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                moles.add(new Mole(row, col));
            }
        }
    }

    /**
     * Hides every mole and clears the previously selected spot.
     */
    public void resetBoard() {
        for (Mole mole : moles) {
            mole.hide();
        }
        lastSpotIndex = -1;
    }

    /**
     * Selects a random spot, avoiding the previously selected spot when possible.
     *
     * @return the selected spot index, or {@code -1} if the board has no moles
     */
    public int chooseNextSpot() {
        if (moles.isEmpty()) {
            return -1;
        }
        if (isOvercrowded()) {
            return lastSpotIndex;
        }

        int next;
        do {
            next = random.nextInt(moles.size());
        } while (moles.size() > 1 && next == lastSpotIndex);

        lastSpotIndex = next;
        return next;
    }

    /**
     * Checks whether the number of visible moles has reached the active-mole limit.
     *
     * @return {@code true} if the board has reached its active-mole limit
     */
    public boolean isOvercrowded() {
        return getActiveMoleCount() >= maxActiveMoles;
    }

    /**
     * Counts the moles that are currently visible and have not expired.
     *
     * @return the active mole count
     */
    public int getActiveMoleCount() {
        int count = 0;
        for (Mole mole : moles) {
            if (mole.isVisible()) {
                count++;
            }
        }
        return count;
    }

    /**
     * Returns the mole at a row-major board index.
     *
     * @param index zero-based mole index
     * @return the mole at {@code index}
     * @throws IndexOutOfBoundsException if {@code index} is outside the board
     */
    public Mole getMole(int index) {
        if (index < 0 || index >= moles.size()) {
            throw new IndexOutOfBoundsException("Mole index out of bounds: " + index);
        }
        return moles.get(index);
    }

    /**
     * Returns the number of rows and columns on this square board.
     *
     * @return the board dimension
     */
    public int getSize() {
        return size;
    }

    /**
     * Returns the maximum number of moles allowed to be active at once.
     *
     * @return the active-mole limit
     */
    public int getMaxActiveMoles() {
        return maxActiveMoles;
    }
}

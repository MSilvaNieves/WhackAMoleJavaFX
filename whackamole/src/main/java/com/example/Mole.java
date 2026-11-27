package com.example;

/**
 * Represents one mole located at a row and column on a game board.
 */
public class Mole {
    private final int row;
    private final int col;
    private boolean visible;
    private long visibleDurationMs;
    private boolean hit;
    private long appearedAtMs;

    /**
     * Creates a mole at the specified board coordinates.
     *
     * @param row zero-based row
     * @param col zero-based column
     */
    public Mole(int row, int col) {
        this.row = row;
        this.col = col;
        this.visible = false;
        this.visibleDurationMs = 0;
        this.hit = false;
        this.appearedAtMs = 0;
    }

    /**
     * Makes the mole visible for the specified duration.
     *
     * @param durationMs time in milliseconds before the mole expires
     * @throws IllegalArgumentException if {@code durationMs} is negative
     */
    public void appear(long durationMs) {
        if (durationMs < 0) {
            throw new IllegalArgumentException("durationMs cannot be negative");
        }
        visibleDurationMs = durationMs;
        appearedAtMs = System.currentTimeMillis();
        visible = true;
        hit = false;
    }

    /**
     * Hides the mole and clears its visible duration.
     */
    public void hide() {
        visible = false;
        visibleDurationMs = 0;
    }

    /**
     * Attempts to hit this mole.
     *
     * @return {@code true} if the mole was visible and unexpired
     */
    public boolean hit() {
        if (!visible || isExpired()) {
            hide();
            return false;
        }
        hit = true;
        visible = false;
        return true;
    }

    /**
     * Checks whether this mole is visible and has not expired.
     *
     * @return {@code true} if the mole is currently visible
     */
    public boolean isVisible() {
        return visible && !isExpired();
    }

    /**
     * Checks whether this mole's visible duration has elapsed.
     *
     * @return {@code true} if the mole has expired
     */
    public boolean isExpired() {
        return appearedAtMs > 0
                && System.currentTimeMillis() - appearedAtMs >= visibleDurationMs;
    }

    /**
     * Returns this mole's row on the board.
     *
     * @return zero-based row
     */
    public int getRow() {
        return row;
    }

    /**
     * Returns this mole's column on the board.
     *
     * @return zero-based column
     */
    public int getCol() {
        return col;
    }
}

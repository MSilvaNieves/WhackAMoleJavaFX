package com.example;

public class Mole {
    private final int row;
    private final int col;
    private boolean visible;
    private long visibleDurationMs;
    private boolean hit;
    private long appearedAtMs;

    public Mole(int row, int col) {
        this.row = row;
        this.col = col;
        this.visible = false;
        this.visibleDurationMs = 0;
        this.hit = false;
        this.appearedAtMs = 0;
    }

    public void appear(long durationMs) {
        if (durationMs < 0) {
            throw new IllegalArgumentException("durationMs cannot be negative");
        }
        visibleDurationMs = durationMs;
        appearedAtMs = System.currentTimeMillis();
        visible = true;
        hit = false;
    }

    public void hide() {
        visible = false;
        visibleDurationMs = 0;
    }

    public boolean hit() {
        if (!visible || isExpired()) {
            hide();
            return false;
        }
        hit = true;
        visible = false;
        return true;
    }

    public boolean isVisible() {
        return visible && !isExpired();
    }

    public boolean isExpired() {
        return appearedAtMs > 0
                && System.currentTimeMillis() - appearedAtMs >= visibleDurationMs;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }
}

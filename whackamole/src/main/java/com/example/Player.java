package com.example;

/**
 * Stores a player's name and personal high score.
 */
public class Player {
    private String name;
    private int highScore;

    /**
     * Creates a player with the given name and an initial high score of zero.
     *
     * @param name player's display name
     * @throws IllegalArgumentException if {@code name} is null or blank
     */
    public Player(String name) {
        setName(name);
        this.highScore = 0;
    }

    /**
     * Returns this player's name.
     *
     * @return player's name
     */
    public String getName() {
        return name;
    }

    /**
     * Updates this player's name.
     *
     * @param name new non-blank name
     * @throws IllegalArgumentException if {@code name} is null or blank
     */
    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("name cannot be empty");
        }
        this.name = name.trim();
    }

    /**
     * Returns this player's high score.
     *
     * @return high score
     */
    public int getHighScore() {
        return highScore;
    }

    /**
     * Sets this player's high score.
     *
     * @param highScore new non-negative high score
     * @throws IllegalArgumentException if {@code highScore} is negative
     */
    public void setHighScore(int highScore) {
        if (highScore < 0) {
            throw new IllegalArgumentException("highScore cannot be negative");
        }
        this.highScore = highScore;
    }
}

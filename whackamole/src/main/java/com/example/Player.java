package com.example;

public class Player {
    private String name;
    private int highScore;

    public Player(String name) {
        setName(name);
        this.highScore = 0;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("name cannot be empty");
        }
        this.name = name.trim();
    }

    public int getHighScore() {
        return highScore;
    }

    public void setHighScore(int highScore) {
        if (highScore < 0) {
            throw new IllegalArgumentException("highScore cannot be negative");
        }
        this.highScore = highScore;
    }
}

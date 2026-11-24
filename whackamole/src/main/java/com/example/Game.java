package com.example;

public class Game {
    private int score;
    private int missCount;
    private final int maxMisses = 3;
    private int timeRemaining;
    private boolean running;
    private final Board board;
    private final Player player;

    public Game(Board board, Player player) {
        if (board == null || player == null) {
            throw new IllegalArgumentException("board and player are required");
        }
        this.board = board;
        this.player = player;
        this.score = 0;
        this.missCount = 0;
        this.timeRemaining = 60;
        this.running = false;
    }

    public void startGame() {
        score = 0;
        missCount = 0;
        timeRemaining = 60;
        running = true;
        board.resetBoard();
    }

    public void countDownTimer() {
        if (!running) {
            return;
        }
        timeRemaining--;
        if (timeRemaining <= 0) {
            timeRemaining = 0;
            endGame();
        }
    }

    public void recordMiss() {
        if (!running) {
            return;
        }
        missCount++;
        if (missCount >= maxMisses) {
            endGame();
        }
    }

    public boolean checkHit(int spotIndex) {
        if (!running) {
            return false;
        }
        try {
            Mole mole = board.getMole(spotIndex);
            if (mole.hit()) {
                score++;
                if (score > player.getHighScore()) {
                    player.setHighScore(score);
                }
                return true;
            }
        } catch (IndexOutOfBoundsException ignored) {
            return false;
        }
        recordMiss();
        return false;
    }

    public void endGame() {
        if (score > player.getHighScore()) {
            player.setHighScore(score);
        }
        running = false;
        board.resetBoard();
    }

    public int getScore() {
        return score;
    }

    public int getMissCount() {
        return missCount;
    }

    public int getTimeRemaining() {
        return timeRemaining;
    }

    public boolean isRunning() {
        return running;
    }

    public Board getBoard() {
        return board;
    }

    public Player getPlayer() {
        return player;
    }

    public int getMaxMisses() {
        return maxMisses;
    }
}

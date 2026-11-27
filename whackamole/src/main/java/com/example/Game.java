package com.example;

/**
 * Holds the state and rules for a Whack-a-Mole game session.
 */
public class Game {
    private int score;
    private int missCount;
    private final int maxMisses = 3;
    private int timeRemaining;
    private boolean running;
    private final Board board;
    private final Player player;

    /**
     * Creates a game using the supplied board and player.
     *
     * @param board board used for the game
     * @param player player whose score is tracked
     * @throws IllegalArgumentException if either argument is {@code null}
     */
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

    /**
     * Resets the score, misses, timer, and board, then starts the game.
     */
    public void startGame() {
        score = 0;
        missCount = 0;
        timeRemaining = 60;
        running = true;
        board.resetBoard();
    }

    /**
     * Decreases the remaining time by one second while the game is running.
     * Ends the game when the timer reaches zero.
     */
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

    /**
     * Records a miss while the game is running.
     * Ends the game after the maximum number of misses is reached.
     */
    public void recordMiss() {
        if (!running) {
            return;
        }
        missCount++;
        if (missCount >= maxMisses) {
            endGame();
        }
    }

    /**
     * Attempts to hit the mole at a board spot.
     *
     * @param spotIndex zero-based board spot index
     * @return {@code true} if a visible, unexpired mole was hit
     */
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

    /**
     * Stops the game and hides all moles on its board.
     */
    public void endGame() {
        if (score > player.getHighScore()) {
            player.setHighScore(score);
        }
        running = false;
        board.resetBoard();
    }

    /**
     * Returns the current score.
     *
     * @return current score
     */
    public int getScore() {
        return score;
    }

    /**
     * Returns the number of misses recorded in this game.
     *
     * @return miss count
     */
    public int getMissCount() {
        return missCount;
    }

    /**
     * Returns the remaining game time in seconds.
     *
     * @return seconds remaining
     */
    public int getTimeRemaining() {
        return timeRemaining;
    }

    /**
     * Indicates whether the game is currently running.
     *
     * @return {@code true} if the game is running
     */
    public boolean isRunning() {
        return running;
    }

    /**
     * Returns the board owned by this game.
     *
     * @return game board
     */
    public Board getBoard() {
        return board;
    }

    /**
     * Returns the player associated with this game.
     *
     * @return game player
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Returns the number of misses that ends the game.
     *
     * @return maximum allowed misses
     */
    public int getMaxMisses() {
        return maxMisses;
    }
}

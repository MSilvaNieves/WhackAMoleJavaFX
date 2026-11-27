package com.example;

/**
 * Runs a console demonstration of the Whack-a-Mole game model.
 */
public class Main {
    /**
     * Creates a game, demonstrates a hit and miss, and prints the resulting state.
     *
     * @param args command-line arguments (unused)
     */
    public static void main(String[] args) {
        Player player = new Player("Java Player");
        Board board = new Board(3);
        Game game = new Game(board, player);

        game.startGame();
        int spot = board.chooseNextSpot();
        board.getMole(spot).appear(1000);

        boolean hit = game.checkHit(spot);
        System.out.println("Player: " + player.getName());
        System.out.println("Hit successful: " + hit);
        System.out.println("Score: " + game.getScore());
        System.out.println("High score: " + player.getHighScore());

        game.recordMiss();
        System.out.println("Miss count: " + game.getMissCount());
        game.endGame();
        System.out.println("Game running: " + game.isRunning());
    }
}

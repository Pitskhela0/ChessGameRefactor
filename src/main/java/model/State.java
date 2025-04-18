package model;

import model.pieces.*;
import java.util.LinkedList;

/**
 * Central model class that holds the overall state of the chess game.
 */
public class State {
    private Board board;
    private model.Player whitePlayer;
    private model.Player blackPlayer;
    private boolean isWhiteTurn;
    private boolean gameOver;
    private String gameResult;
    private King whiteKing;
    private King blackKing;
    private boolean whiteInCheck = false;
    private boolean blackInCheck = false;

    public void setWhiteInCheck(boolean inCheck) {
        this.whiteInCheck = inCheck;
        if (inCheck) {
            System.out.println("White king is in check!");
        }
    }

    public void setBlackInCheck(boolean inCheck) {
        this.blackInCheck = inCheck;
        if (inCheck) {
            System.out.println("Black king is in check!");
        }
    }

    public boolean isWhiteInCheck() {
        return whiteInCheck;
    }

    public boolean isBlackInCheck() {
        return blackInCheck;
    }

    /**
     * Constructs a new GameState with the given player names and time settings.
     *
     * @param whiteName White player's name
     * @param blackName Black player's name
     * @param timeHours Hours component of the time control
     * @param timeMinutes Minutes component of the time control
     * @param timeSeconds Seconds component of the time control
     */
    public State(String whiteName, String blackName, int timeHours, int timeMinutes, int timeSeconds) {
        // Initialize players
        this.whitePlayer = new model.Player(whiteName, 1, new LinkedList<>(),
                convertToMillis(timeHours, timeMinutes, timeSeconds));
        this.blackPlayer = new model.Player(blackName, 0, new LinkedList<>(),
                convertToMillis(timeHours, timeMinutes, timeSeconds));

        this.isWhiteTurn = true;
        this.gameOver = false;
        this.gameResult = "";
    }

    /**
     * Initializes the game state with a board.
     *
     * @param board The chess board
     */
    public void initialize(Board board) {
        this.board = board;
    }


    /**
     * Sets references to the king pieces.
     *
     * @param whiteKing The white king
     * @param blackKing The black king
     */
    public void setKings(King whiteKing, King blackKing) {
        this.whiteKing = whiteKing;
        this.blackKing = blackKing;
    }

    /**
     * Converts time components to milliseconds.
     *
     * @param hours Hours
     * @param minutes Minutes
     * @param seconds Seconds
     * @return Total time in milliseconds
     */
    private long convertToMillis(int hours, int minutes, int seconds) {
        return ((hours * 60L + minutes) * 60L + seconds) * 1000L;
    }

    /**
     * Toggles the turn between white and black.
     */
    public void toggleTurn() {
        isWhiteTurn = !isWhiteTurn;
    }

    /**
     * Sets the game as over with a specified result.
     *
     * @param result String describing the game result
     */
    public void endGame(String result) {
        this.gameOver = true;
        this.gameResult = result;
    }

    // Getters and setters

    public Board getBoard() {
        return board;
    }

    public model.Player getWhitePlayer() {
        return whitePlayer;
    }

    public model.Player getBlackPlayer() {
        return blackPlayer;
    }

    public boolean isWhiteTurn() {
        return isWhiteTurn;
    }

    public void setWhiteTurn(boolean whiteTurn) {
        isWhiteTurn = whiteTurn;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }

    public String getGameResult() {
        return gameResult;
    }

    public King getWhiteKing() {
        return whiteKing;
    }

    public void setWhiteKing(King whiteKing) {
        this.whiteKing = whiteKing;
    }

    public King getBlackKing() {
        return blackKing;
    }

    public void setBlackKing(King blackKing) {
        this.blackKing = blackKing;
    }
}
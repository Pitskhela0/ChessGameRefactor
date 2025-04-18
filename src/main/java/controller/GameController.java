package controller;

import view.GameWindow;
import model.*;
import model.pieces.*;

import javax.swing.*;
import java.util.List;

/**
 * Main controller for the chess game, coordinating model and view.
 */
public class GameController {
    private State state;
    private GameWindow view;
    private CheckmateDetector checkmateDetector;

    /**
     * Constructs a new GameController with the specified settings.
     *
     * @param whiteName White player's name
     * @param blackName Black player's name
     * @param hours Time control hours
     * @param minutes Time control minutes
     * @param seconds Time control seconds
     */
    // In GameController constructor
    public GameController(String whiteName, String blackName, int hours, int minutes, int seconds) {
        // Initialize game state
        this.state = new State(whiteName, blackName, hours, minutes, seconds);

        // Create the board with a reference to this controller
        Board board = new Board(this);
        state.initialize(board);

        // Initialize the view
        this.view = new GameWindow(this, whiteName, blackName, hours, minutes, seconds);

        // Set up checkmate detector
        setupCheckmateDetector();

        // Show the view
        view.display();
    }
    private void setupCheckmateDetector() {
        try {
            Board board = state.getBoard();
            King whiteKing = state.getWhiteKing();
            King blackKing = state.getBlackKing();

            if (board != null && whiteKing != null && blackKing != null) {
                System.out.println("Initializing CheckmateDetector");
                this.checkmateDetector = new CheckmateDetector(
                        board,
                        board.Wpieces,
                        board.Bpieces,
                        whiteKing,
                        blackKing
                );
                System.out.println("CheckmateDetector initialized successfully");
            } else {
                System.err.println("Cannot initialize CheckmateDetector:");
                System.err.println("Board: " + (board != null ? "OK" : "null"));
                System.err.println("White King: " + (whiteKing != null ? "OK" : "null"));
                System.err.println("Black King: " + (blackKing != null ? "OK" : "null"));
            }
        } catch (Exception e) {
            System.err.println("Error initializing CheckmateDetector: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Initialize the checkmate detector once the kings are set up.
     */
    public void initializeCheckmateDetector() {
        Board board = state.getBoard();
        if (board != null && state.getWhiteKing() != null && state.getBlackKing() != null) {
            this.checkmateDetector = new CheckmateDetector(
                    board,
                    state.getWhitePlayer().getPieces(),
                    state.getBlackPlayer().getPieces(),
                    state.getWhiteKing(),
                    state.getBlackKing()
            );
        }
    }

    /**
     * Handles the start of a piece movement (when a player selects a piece).
     *
     * @param piece The selected piece
     * @return true if the piece can be moved, false otherwise
     */
    public boolean startPieceMove(Piece piece) {
        // Check if it's the correct player's turn
        if ((piece.getColor() == 1 && !state.isWhiteTurn()) ||
                (piece.getColor() == 0 && state.isWhiteTurn())) {
            return false;
        }

        // Check if the game is already over
        if (state.isGameOver()) {
            return false;
        }

        return true;
    }

    /**
     * Handles a piece movement.
     *
     * @param piece The piece to move
     * @param destination The destination square
     * @return true if the move was successful, false otherwise
     */
    public boolean movePiece(Piece piece, Square destination) {
        // First check if move is valid using checkmate detector
        if (checkmateDetector != null && !checkmateDetector.testMove(piece, destination)) {
            System.out.println("Move would leave king in check");
            return false;
        }

        // Execute move
        boolean moveSuccessful = piece.move(destination);

        if (moveSuccessful) {
            // Update game state
            state.toggleTurn();

            if (checkmateDetector != null) {
                checkmateDetector.update();

                // Update check status
                state.setWhiteInCheck(checkmateDetector.whiteInCheck());
                state.setBlackInCheck(checkmateDetector.blackInCheck());

                // Check for checkmate
                if (checkmateDetector.blackCheckMated()) {
                    state.endGame("White wins by checkmate");
                    view.notifyCheckmate(1); // White wins
                    return true;
                } else if (checkmateDetector.whiteCheckMated()) {
                    state.endGame("Black wins by checkmate");
                    view.notifyCheckmate(0); // Black wins
                    return true;
                }

                // Announce check if applicable
                if (state.isWhiteInCheck() || state.isBlackInCheck()) {
                    SwingUtilities.invokeLater(() -> {
                        JOptionPane.showMessageDialog(null,
                                (state.isWhiteInCheck() ? "White" : "Black") + " king is in check!",
                                "Check",
                                JOptionPane.WARNING_MESSAGE);
                    });
                }
            }
        }

        return moveSuccessful;
    }

    /**
     * Gets the name of the piece for logging.
     *
     * @param piece The chess piece
     * @return String representation of the piece
     */
    private String getPieceName(Piece piece) {
        String className = piece.getClass().getSimpleName();
        return className;
    }

    /**
     * Converts a square to algebraic chess notation (e.g., "e4").
     *
     * @param square The square to convert
     * @return Algebraic notation of the square
     */
    private String getSquareNotation(Square square) {
        if (square == null) return "??";

        // Convert the x coordinate to a letter (a-h)
        char file = (char) ('a' + square.getXNum());

        // Convert the y coordinate to a number (1-8)
        // Chess board is typically numbered from bottom to top,
        // so we need to invert the y coordinate
        int rank = 8 - square.getYNum();

        return file + "" + rank;
    }

    /**
     * Gets the legal moves for a piece.
     *
     * @param piece The piece to get moves for
     * @return List of legal move squares
     */
    public List<Square> getLegalMoves(Piece piece) {
        if (state.getBoard() == null) {
            return java.util.Collections.emptyList();
        }
        return piece.getLegalMoves(state.getBoard());
    }

    /**
     * Gets the current list of allowable squares based on check state.
     *
     * @return List of allowable squares
     */
    public List<Square> getAllowableSquares() {
        if (checkmateDetector == null) {
            return java.util.Collections.emptyList();
        }
        return checkmateDetector.getAllowableSquares(state.isWhiteTurn());
    }

    /**
     * Notifies the controller that a player has run out of time.
     *
     * @param isWhite true if white ran out of time, false if black
     */
    public void timeOut(boolean isWhite) {
        String result = isWhite ? "Black wins on time" : "White wins on time";
        state.endGame(result);
        view.notifyTimeout(!isWhite); // Pass the winner
    }

    // Getters
    public State getGameState() {
        return state;
    }

    public CheckmateDetector getCheckmateDetector() {
        return checkmateDetector;
    }
}
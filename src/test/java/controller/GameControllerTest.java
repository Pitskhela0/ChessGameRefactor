package controller;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import model.Board;
import model.Piece;
import model.Player;
import model.Square;
import model.State;
import model.pieces.*;
import model.Move;

import java.util.List;

public class GameControllerTest {
    private GameController controller;
    private Board board;
    private Square[][] squares;

    @Before
    public void setUp() {
        controller = new GameController("White Player", "Black Player", 0, 10, 0);

        State gameState = controller.getGameState();
        board = gameState.getBoard();
        squares = board.getSquareArray();
    }

    @Test
    public void testInitialGameState() {
        State gameState = controller.getGameState();

        // Test initial turn
        assertTrue("Game should start with white's turn", gameState.isWhiteTurn());

        // Test game is not over initially
        assertFalse("Game should not be over at start", gameState.isGameOver());

        // Test player names set correctly
        assertEquals("White player name should be set", "White Player", gameState.getWhitePlayer().getName());
        assertEquals("Black player name should be set", "Black Player", gameState.getBlackPlayer().getName());
    }

    @Test
    public void testTurnToggle() {
        State gameState = controller.getGameState();

        // Initial state is white's turn
        assertTrue("Game should start with white's turn", gameState.isWhiteTurn());

        // Toggle turn
        gameState.toggleTurn();

        // Should now be black's turn
        assertFalse("Should be black's turn after toggle", gameState.isWhiteTurn());

        // Toggle again
        gameState.toggleTurn();

        // Should be white's turn again
        assertTrue("Should be white's turn after second toggle", gameState.isWhiteTurn());
    }

    @Test
    public void testValidMove() {
        // Get a white pawn from starting position
        Pawn whitePawn = (Pawn) squares[6][0].getOccupyingPiece();

        // Test if it's a valid move
        boolean moveValid = controller.startPieceMove(whitePawn);
        assertTrue("White pawn should be able to move on white's turn", moveValid);

        // Test if moving the pawn forward is successful
        boolean moveSuccess = controller.movePiece(whitePawn, squares[5][0]);
        assertTrue("Pawn should be able to move forward one square", moveSuccess);

        // Verify the pawn has moved
        assertEquals("Pawn should be at the new position", squares[5][0], whitePawn.getPosition());
        assertNull("Old position should be empty", squares[6][0].getOccupyingPiece());
    }

    @Test
    public void testWrongTurnMove() {
        // Get a black pawn from starting position
        Pawn blackPawn = (Pawn) squares[1][0].getOccupyingPiece();

        // It's white's turn initially, so black shouldn't be able to move
        boolean moveValid = controller.startPieceMove(blackPawn);
        assertFalse("Black pawn should not be able to move on white's turn", moveValid);
    }

    @Test
    public void testMoveAfterGameOver() {
        // Set game as over
        State gameState = controller.getGameState();
        gameState.endGame("Test game over");

        // Get a white pawn
        Pawn whitePawn = (Pawn) squares[6][0].getOccupyingPiece();

        // Try to move after game is over
        boolean moveValid = controller.startPieceMove(whitePawn);
        assertFalse("Pieces should not be movable after game is over", moveValid);
    }

    @Test
    public void testGetLegalMoves() {
        // Get legal moves for a white pawn at starting position
        Pawn whitePawn = (Pawn) squares[6][0].getOccupyingPiece();
        List<Square> legalMoves = controller.getLegalMoves(whitePawn);

        // Should have 2 legal moves (one and two squares forward)
        assertEquals("Pawn should have 2 legal moves initially", 2, legalMoves.size());
        assertTrue("Pawn can move one square forward", legalMoves.contains(squares[5][0]));
        assertTrue("Pawn can move two squares forward", legalMoves.contains(squares[4][0]));

        // Move the pawn one square
        controller.movePiece(whitePawn, squares[5][0]);

        // Check legal moves again
        legalMoves = controller.getLegalMoves(whitePawn);

        // Should only have 1 legal move now (one square forward)
        assertEquals("Pawn should have 1 legal move after first move", 1, legalMoves.size());
        assertTrue("Pawn can move one square forward", legalMoves.contains(squares[4][0]));
    }

    @Test
    public void testTimeOut() {
        // Test that controller properly handles timeOut signal
        controller.timeOut(true); // White runs out of time

        State gameState = controller.getGameState();

        // Game should be over
        assertTrue("Game should be over after time out", gameState.isGameOver());
        assertEquals("Game result should reflect timeout", "Black wins on time", gameState.getGameResult());
    }

    @Test
    public void testMoveTurnsAlternate() {
        // Make a move with white
        Pawn whitePawn = (Pawn) squares[6][0].getOccupyingPiece();
        controller.movePiece(whitePawn, squares[4][0]);

        // Should now be black's turn
        assertFalse("Should be black's turn after white moves", controller.getGameState().isWhiteTurn());

        // Make a move with black
        Pawn blackPawn = (Pawn) squares[1][1].getOccupyingPiece();
        controller.movePiece(blackPawn, squares[3][1]);

        // Should now be white's turn again
        assertTrue("Should be white's turn after black moves", controller.getGameState().isWhiteTurn());
    }
}
package controller;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import model.Board;
import model.Square;
import model.State;
import model.pieces.*;

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

        assertTrue("Game should start with white's turn", gameState.isWhiteTurn());

        assertFalse("Game should not be over at start", gameState.isGameOver());

        assertEquals("White player name should be set", "White Player", gameState.getWhitePlayer().getName());
        assertEquals("Black player name should be set", "Black Player", gameState.getBlackPlayer().getName());
    }

    @Test
    public void testTurnToggle() {
        State gameState = controller.getGameState();

        assertTrue("Game should start with white's turn", gameState.isWhiteTurn());

        // Toggle turn
        gameState.toggleTurn();

        assertFalse("Should be black's turn after toggle", gameState.isWhiteTurn());

        gameState.toggleTurn();

        assertTrue("Should be white's turn after second toggle", gameState.isWhiteTurn());
    }

    @Test
    public void testValidMove() {
        Pawn whitePawn = (Pawn) squares[6][0].getOccupyingPiece();

        boolean moveValid = controller.startPieceMove(whitePawn);
        assertTrue("White pawn should be able to move on white's turn", moveValid);

        boolean moveSuccess = controller.movePiece(whitePawn, squares[5][0]);
        assertTrue("Pawn should be able to move forward one square", moveSuccess);

        assertEquals("Pawn should be at the new position", squares[5][0], whitePawn.getPosition());
        assertNull("Old position should be empty", squares[6][0].getOccupyingPiece());
    }

    @Test
    public void testWrongTurnMove() {
        Pawn blackPawn = (Pawn) squares[1][0].getOccupyingPiece();

        boolean moveValid = controller.startPieceMove(blackPawn);
        assertFalse("Black pawn should not be able to move on white's turn", moveValid);
    }

    @Test
    public void testMoveAfterGameOver() {
        State gameState = controller.getGameState();
        gameState.endGame("Test game over");

        Pawn whitePawn = (Pawn) squares[6][0].getOccupyingPiece();

        boolean moveValid = controller.startPieceMove(whitePawn);
        assertFalse("Pieces should not be movable after game is over", moveValid);
    }

    @Test
    public void testGetLegalMoves() {
        Pawn whitePawn = (Pawn) squares[6][0].getOccupyingPiece();
        List<Square> legalMoves = controller.getLegalMoves(whitePawn);

        assertEquals("Pawn should have 2 legal moves initially", 2, legalMoves.size());
        assertTrue("Pawn can move one square forward", legalMoves.contains(squares[5][0]));
        assertTrue("Pawn can move two squares forward", legalMoves.contains(squares[4][0]));

        controller.movePiece(whitePawn, squares[5][0]);

        legalMoves = controller.getLegalMoves(whitePawn);

        assertEquals("Pawn should have 1 legal move after first move", 1, legalMoves.size());
        assertTrue("Pawn can move one square forward", legalMoves.contains(squares[4][0]));
    }

    @Test
    public void testTimeOut() {
        controller.timeOut(true); // White runs out of time

        State gameState = controller.getGameState();

        assertTrue("Game should be over after time out", gameState.isGameOver());
        assertEquals("Game result should reflect timeout", "Black wins on time", gameState.getGameResult());
    }

    @Test
    public void testMoveTurnsAlternate() {
        Pawn whitePawn = (Pawn) squares[6][0].getOccupyingPiece();
        controller.movePiece(whitePawn, squares[4][0]);

        assertFalse("Should be black's turn after white moves", controller.getGameState().isWhiteTurn());

        Pawn blackPawn = (Pawn) squares[1][1].getOccupyingPiece();
        controller.movePiece(blackPawn, squares[3][1]);

        assertTrue("Should be white's turn after black moves", controller.getGameState().isWhiteTurn());
    }
}
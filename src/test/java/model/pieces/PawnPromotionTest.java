package model.pieces;

import static org.junit.Assert.*;

import model.Board;
import model.Piece;
import model.Square;
import org.junit.Before;
import org.junit.Test;

import controller.GameController;
import model.pieces.*;

public class PawnPromotionTest {
    private Board board;
    private Square[][] squares;
    private Pawn whitePawn;

    @Before
    public void setUp() {
        // Create test board
        GameController controller = new GameController("White", "Black", 0, 1, 0);
        board = controller.getGameState().getBoard();
        squares = board.getSquareArray();

        // Create a pawn close to promotion
        whitePawn = (Pawn) squares[6][0].getOccupyingPiece();

        // Clear the way to the promotion square
        for (int i = 1; i < 6; i++) {
            squares[i][0].removePiece();
        }

        // Move pawn to the 2nd rank (one move from promotion)
        whitePawn.move(squares[1][0]);
    }

    @Test
    public void testPawnPromotion() {
        // Clear the board for easy testing
        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                if (squares[y][x].isOccupied()) {
                    squares[y][x].removePiece();
                }
            }
        }

        // Place a white pawn one move away from promotion
        Pawn whitePawn = new Pawn(1, squares[1][0], "wpawn.png");
        squares[1][0].put(whitePawn);

        // Move to promotion rank
        whitePawn.move(squares[0][0]);

        // Check if the piece was promoted to a Queen (default)
        Piece promotedPiece = squares[0][0].getOccupyingPiece();
        assertNotNull("Promoted piece should exist", promotedPiece);
        assertTrue("Pawn should be promoted to Queen", promotedPiece instanceof Queen);
        assertEquals("Promoted piece should have the same color", 1, promotedPiece.getColor());

        // Test black pawn promotion
        Pawn blackPawn = new Pawn(0, squares[6][0], "bpawn.png");
        squares[6][0].put(blackPawn);

        // Move to promotion rank
        blackPawn.move(squares[7][0]);

        // Check if the piece was promoted
        promotedPiece = squares[7][0].getOccupyingPiece();
        assertNotNull("Promoted piece should exist", promotedPiece);
        assertTrue("Pawn should be promoted to Queen", promotedPiece instanceof Queen);
        assertEquals("Promoted piece should have the same color", 0, promotedPiece.getColor());
    }

    @Test
    public void testPawnPromotionOptions() {
        // Clear a portion of the board
        squares[1][1].removePiece();

        // Place a white pawn one move away from promotion
        Pawn whitePawn = new Pawn(1, squares[1][1], "wpawn.png");
        squares[1][1].put(whitePawn);

        // Manually test different promotion options
        whitePawn.move(squares[0][1]);
        Piece queen = whitePawn.promote(squares[0][1], "Queen");
        assertTrue("Should promote to Queen", queen instanceof Queen);

        whitePawn = new Pawn(1, squares[1][2], "wpawn.png");
        squares[1][2].put(whitePawn);
        whitePawn.move(squares[0][2]);
        Piece rook = whitePawn.promote(squares[0][2], "Rook");
        assertTrue("Should promote to Rook", rook instanceof Rook);

        whitePawn = new Pawn(1, squares[1][3], "wpawn.png");
        squares[1][3].put(whitePawn);
        whitePawn.move(squares[0][3]);
        Piece bishop = whitePawn.promote(squares[0][3], "Bishop");
        assertTrue("Should promote to Bishop", bishop instanceof Bishop);

        whitePawn = new Pawn(1, squares[1][4], "wpawn.png");
        squares[1][4].put(whitePawn);
        whitePawn.move(squares[0][4]);
        Piece knight = whitePawn.promote(squares[0][4], "Knight");
        assertTrue("Should promote to Knight", knight instanceof Knight);
    }


    @Test
    public void testPromotionOptions() {
        // Move pawn to the promotion rank
        whitePawn.move(squares[0][0]);

        // Test promoting to different pieces
        Piece queen = whitePawn.promote(squares[0][0],"Queen");
        assertTrue("Should be able to promote to Queen", queen instanceof Queen);

        Piece rook = whitePawn.promote(squares[0][0],"Rook");
        assertTrue("Should be able to promote to Rook", rook instanceof Rook);

        Piece bishop = whitePawn.promote(squares[0][0],"Bishop");
        assertTrue("Should be able to promote to Bishop", bishop instanceof Bishop);

        Piece knight = whitePawn.promote(squares[0][0],"Knight");
        assertTrue("Should be able to promote to Knight", knight instanceof Knight);
    }
}
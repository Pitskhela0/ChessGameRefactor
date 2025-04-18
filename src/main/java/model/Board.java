package model;

import controller.GameController;
import model.pieces.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.LinkedList;
import java.util.List;

/**
 * Represents the chess board and manages piece placement and movement.
 */
@SuppressWarnings("serial")
public class Board extends JPanel implements MouseListener, MouseMotionListener {
    // Resource constants for piece images
    private static final String RESOURCES_WBISHOP_PNG = "wbishop.png";
    private static final String RESOURCES_BBISHOP_PNG = "bbishop.png";
    private static final String RESOURCES_WKNIGHT_PNG = "wknight.png";
    private static final String RESOURCES_BKNIGHT_PNG = "bknight.png";
    private static final String RESOURCES_WROOK_PNG = "wrook.png";
    private static final String RESOURCES_BROOK_PNG = "brook.png";
    private static final String RESOURCES_WKING_PNG = "wking.png";
    private static final String RESOURCES_BKING_PNG = "bking.png";
    private static final String RESOURCES_BQUEEN_PNG = "bqueen.png";
    private static final String RESOURCES_WQUEEN_PNG = "wqueen.png";
    private static final String RESOURCES_WPAWN_PNG = "wpawn.png";
    private static final String RESOURCES_BPAWN_PNG = "bpawn.png";

    // Board state
    private final Square[][] board;
    private final GameController controller;

    // Piece lists
    public final LinkedList<Piece> Bpieces;
    public final LinkedList<Piece> Wpieces;
    public List<Square> movable;

    // Current state
    private Piece currPiece;
    private int currX;
    private int currY;

    /**
     * Constructs a new chess board managed by the given controller.
     *
     * @param controller The game controller
     */
    public Board(GameController controller) {
        this.controller = controller;
        board = new Square[8][8];
        Bpieces = new LinkedList<>();
        Wpieces = new LinkedList<>();
        setLayout(new GridLayout(8, 8, 0, 0));

        this.addMouseListener(this);
        this.addMouseMotionListener(this);

        // Initialize the board squares
        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                int color = ((x + y) % 2 == 0) ? 1 : 0;
                board[y][x] = new Square(this, color, x, y);
                super.add(board[y][x]);
            }
        }

        // Initialize the chess pieces
        initializePieces();

        // Set board size
        this.setPreferredSize(new Dimension(400, 400));
        this.setMaximumSize(new Dimension(400, 400));
        this.setMinimumSize(this.getPreferredSize());
        this.setSize(new Dimension(400, 400));
    }

    /**
     * Initialize pieces on the board in their starting positions.
     */
    private void initializePieces() {
        // Set up pawns
        for (int x = 0; x < 8; x++) {
            board[1][x].put(new Pawn(0, board[1][x], RESOURCES_BPAWN_PNG));
            board[6][x].put(new Pawn(1, board[6][x], RESOURCES_WPAWN_PNG));
        }

        // Set up queens
        board[7][3].put(new Queen(1, board[7][3], RESOURCES_WQUEEN_PNG));
        board[0][3].put(new Queen(0, board[0][3], RESOURCES_BQUEEN_PNG));

        // Set up kings
        King bk = new King(0, board[0][4], RESOURCES_BKING_PNG);
        King wk = new King(1, board[7][4], RESOURCES_WKING_PNG);
        board[0][4].put(bk);
        board[7][4].put(wk);

        // Update game state with kings
        if (controller.getGameState() != null) {
            controller.getGameState().setKings(wk, bk);
        }

        // Set up rooks
        board[0][0].put(new Rook(0, board[0][0], RESOURCES_BROOK_PNG));
        board[0][7].put(new Rook(0, board[0][7], RESOURCES_BROOK_PNG));
        board[7][0].put(new Rook(1, board[7][0], RESOURCES_WROOK_PNG));
        board[7][7].put(new Rook(1, board[7][7], RESOURCES_WROOK_PNG));

        // Set up knights
        board[0][1].put(new Knight(0, board[0][1], RESOURCES_BKNIGHT_PNG));
        board[0][6].put(new Knight(0, board[0][6], RESOURCES_BKNIGHT_PNG));
        board[7][1].put(new Knight(1, board[7][1], RESOURCES_WKNIGHT_PNG));
        board[7][6].put(new Knight(1, board[7][6], RESOURCES_WKNIGHT_PNG));

        // Set up bishops
        board[0][2].put(new Bishop(0, board[0][2], RESOURCES_BBISHOP_PNG));
        board[0][5].put(new Bishop(0, board[0][5], RESOURCES_BBISHOP_PNG));
        board[7][2].put(new Bishop(1, board[7][2], RESOURCES_WBISHOP_PNG));
        board[7][5].put(new Bishop(1, board[7][5], RESOURCES_WBISHOP_PNG));

        // Populate piece lists
        for(int y = 0; y < 2; y++) {
            for (int x = 0; x < 8; x++) {
                Bpieces.add(board[y][x].getOccupyingPiece());
                Wpieces.add(board[7-y][x].getOccupyingPiece());
            }
        }

        // Initialize the checkmate detector
        controller.initializeCheckmateDetector();
    }

    /**
     * Gets the 2D array of squares on the board.
     *
     * @return The board squares array
     */
    public Square[][] getSquareArray() {
        return this.board;
    }

    /**
     * Gets the current turn (true for white, false for black).
     *
     * @return The current turn
     */
    public boolean getTurn() {
        return controller.getGameState().isWhiteTurn();
    }

    /**
     * Sets the current piece being moved.
     *
     * @param p The piece being moved
     */
    public void setCurrPiece(Piece p) {
        this.currPiece = p;
    }

    /**
     * Gets the current piece being moved.
     *
     * @return The piece being moved
     */
    public Piece getCurrPiece() {
        return this.currPiece;
    }

    @Override
    public void paintComponent(Graphics g) {
        // Paint all squares
        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                Square sq = board[y][x];
                sq.paintComponent(g);
            }
        }

        // Paint the piece being dragged
        if (currPiece != null) {
            boolean isValidTurn = (currPiece.getColor() == 1 && getTurn()) ||
                    (currPiece.getColor() == 0 && !getTurn());
            if (isValidTurn) {
                final Image i = currPiece.getImage();
                g.drawImage(i, currX, currY, null);
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        currX = e.getX();
        currY = e.getY();

        // Find the square that was clicked
        Square sq = (Square) this.getComponentAt(new Point(e.getX(), e.getY()));

        if (sq.isOccupied()) {
            currPiece = sq.getOccupyingPiece();

            // Check if it's a valid turn through controller
            if (!controller.startPieceMove(currPiece)) {
                currPiece = null;
                return;
            }

            sq.setDisplay(false);
        }
        repaint();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (currPiece == null) return;

        // Find the destination square
        Square sq = (Square) this.getComponentAt(new Point(e.getX(), e.getY()));
        if (sq == null) {
            if (currPiece.getPosition() != null) {
                currPiece.getPosition().setDisplay(true);
            }
            currPiece = null;
            repaint();
            return;
        }

        // Use controller to check if this piece can be moved
        if (!controller.startPieceMove(currPiece)) {
            currPiece.getPosition().setDisplay(true);
            currPiece = null;
            repaint();
            return;
        }






        // Use controller to get legal moves
        List<Square> legalMoves = controller.getLegalMoves(currPiece);
        List<Square> allowableMoves = controller.getAllowableSquares();

        // Check if the target square is a valid destination
        if (legalMoves.contains(sq) && allowableMoves.contains(sq)) {
            // Use controller to move the piece
            boolean moveSuccessful = controller.movePiece(currPiece, sq);

            if (moveSuccessful) {
                sq.setDisplay(true);
                // Movement was successful - controller has already handled turn change
            } else {
                // Movement failed
                currPiece.getPosition().setDisplay(true);
            }
        } else {
            // Not a valid move
            currPiece.getPosition().setDisplay(true);
        }

        if (currPiece != null && currPiece instanceof Pawn) {
            Pawn pawn = (Pawn) currPiece;
            Square square = pawn.getPosition();

            // Check if pawn is on promotion rank
            boolean isPromotionRank = (pawn.getColor() == 1 && square.getYNum() == 0) ||
                    (pawn.getColor() == 0 && square.getYNum() == 7);

            if (isPromotionRank) {
                // Show promotion dialog
                String[] options = {"Queen", "Rook", "Bishop", "Knight"};
                int choice = JOptionPane.showOptionDialog(
                        this,
                        "Choose promotion piece:",
                        "Pawn Promotion",
                        JOptionPane.DEFAULT_OPTION,
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        options,
                        options[0]
                );

                // Promote pawn
                String promotionChoice = (choice >= 0) ? options[choice] : "Queen";
                pawn.promote(square, promotionChoice);
            }
        }






        currPiece = null;
        repaint();
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        currX = e.getX() - 24;
        currY = e.getY() - 24;
        repaint();
    }

    // Unused mouse methods
    @Override
    public void mouseMoved(MouseEvent e) {}

    @Override
    public void mouseClicked(MouseEvent e) {}

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}
}
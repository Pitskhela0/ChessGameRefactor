package Model;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.LinkedList;
import java.util.List;

import util.ResourceManager;
import View.PieceView;

/**
 * Abstract base class for all chess pieces.
 */
public abstract class Piece {
    private final int color;
    private Square currentSquare;
    private BufferedImage img;
    private PieceView view;

    /**
     * Constructs a new chess piece.
     *
     * @param color The piece color (0 for black, 1 for white)
     * @param initSq The initial square position
     * @param imgFile The image file name for this piece
     */
    // In Piece constructor
    public Piece(int color, Square initSq, String imgFile) {
        this.color = color;
        this.currentSquare = initSq;

        // Use ResourceManager to load the image
        this.img = ResourceManager.loadImage(imgFile);

        if (this.img == null) {
            System.err.println("Failed to load piece image: " + imgFile);
        }
    }

    /**
     * Sets the view component for this piece.
     *
     * @param view The PieceView for rendering this piece
     */
    public void setView(PieceView view) {
        this.view = view;
    }

    /**
     * Gets the view component for this piece.
     *
     * @return The PieceView
     */
    public PieceView getView() {
        return this.view;
    }

    /**
     * Moves the piece to a destination square.
     *
     * @param destination The square to move to
     * @return true if the move was successful, false otherwise
     */
    public boolean move(Square destination) {
        Piece occupant = destination.getOccupyingPiece();

        if (occupant != null) {
            if (occupant.getColor() == this.color) return false;
            else destination.capture(this);
        }

        currentSquare.removePiece();
        this.currentSquare = destination;
        currentSquare.put(this);
        return true;
    }

    // Rest of the original piece methods remain the same...

    /**
     * Gets the current position of the piece.
     *
     * @return The square the piece is on
     */
    public Square getPosition() {
        return currentSquare;
    }

    /**
     * Sets the position of the piece.
     *
     * @param sq The new position square
     */
    public void setPosition(Square sq) {
        this.currentSquare = sq;
    }

    /**
     * Gets the color of the piece.
     *
     * @return The piece color (0 for black, 1 for white)
     */
    public int getColor() {
        return color;
    }

    /**
     * Gets the image representing this piece.
     *
     * @return The piece image
     */
    public Image getImage() {
        return img;
    }

    /**
     * Draws the piece at its current position.
     *
     * @param g The graphics context to draw on
     */
    public void draw(Graphics g) {
        if (currentSquare != null && img != null) {
            int x = currentSquare.getX();
            int y = currentSquare.getY();
            g.drawImage(this.img, x, y, null);
        }
    }

    public int[] getLinearOccupations(Square[][] board, int x, int y) {
        int lastYabove = 0;
        int lastXright = 7;
        int lastYbelow = 7;
        int lastXleft = 0;

        for (int i = 0; i < y; i++) {
            if (board[i][x].isOccupied()) {
                if (board[i][x].getOccupyingPiece().getColor() != this.color) {
                    lastYabove = i;
                } else lastYabove = i + 1;
            }
        }

        for (int i = 7; i > y; i--) {
            if (board[i][x].isOccupied()) {
                if (board[i][x].getOccupyingPiece().getColor() != this.color) {
                    lastYbelow = i;
                } else lastYbelow = i - 1;
            }
        }

        for (int i = 0; i < x; i++) {
            if (board[y][i].isOccupied()) {
                if (board[y][i].getOccupyingPiece().getColor() != this.color) {
                    lastXleft = i;
                } else lastXleft = i + 1;
            }
        }

        for (int i = 7; i > x; i--) {
            if (board[y][i].isOccupied()) {
                if (board[y][i].getOccupyingPiece().getColor() != this.color) {
                    lastXright = i;
                } else lastXright = i - 1;
            }
        }

        int[] occups = {lastYabove, lastYbelow, lastXleft, lastXright};

        return occups;
    }

    // The rest of the methods remain unchanged
    public List<Square> getDiagonalOccupations(Square[][] board, int x, int y) {
        LinkedList<Square> diagOccup = new LinkedList<Square>();

        int xNW = x - 1;
        int xSW = x - 1;
        int xNE = x + 1;
        int xSE = x + 1;
        int yNW = y - 1;
        int ySW = y + 1;
        int yNE = y - 1;
        int ySE = y + 1;

        while (xNW >= 0 && yNW >= 0) {
            if (board[yNW][xNW].isOccupied()) {
                if (board[yNW][xNW].getOccupyingPiece().getColor() == this.color) {
                    break;
                } else {
                    diagOccup.add(board[yNW][xNW]);
                    break;
                }
            } else {
                diagOccup.add(board[yNW][xNW]);
                yNW--;
                xNW--;
            }
        }

        while (xSW >= 0 && ySW < 8) {
            if (board[ySW][xSW].isOccupied()) {
                if (board[ySW][xSW].getOccupyingPiece().getColor() == this.color) {
                    break;
                } else {
                    diagOccup.add(board[ySW][xSW]);
                    break;
                }
            } else {
                diagOccup.add(board[ySW][xSW]);
                ySW++;
                xSW--;
            }
        }

        while (xSE < 8 && ySE < 8) {
            if (board[ySE][xSE].isOccupied()) {
                if (board[ySE][xSE].getOccupyingPiece().getColor() == this.color) {
                    break;
                } else {
                    diagOccup.add(board[ySE][xSE]);
                    break;
                }
            } else {
                diagOccup.add(board[ySE][xSE]);
                ySE++;
                xSE++;
            }
        }

        while (xNE < 8 && yNE >= 0) {
            if (board[yNE][xNE].isOccupied()) {
                if (board[yNE][xNE].getOccupyingPiece().getColor() == this.color) {
                    break;
                } else {
                    diagOccup.add(board[yNE][xNE]);
                    break;
                }
            } else {
                diagOccup.add(board[yNE][xNE]);
                yNE--;
                xNE++;
            }
        }

        return diagOccup;
    }


    /**
     * Gets the legal moves for this piece on the current board.
     *
     * @param b The current game board
     * @return List of squares this piece can legally move to
     */
    public abstract List<Square> getLegalMoves(Board b);
}
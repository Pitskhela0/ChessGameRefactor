package view;

import controller.GameController;
import model.Board;
import model.Clock;
import util.ResourceManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Main window for the chess game, displaying the board and game information.
 */
public class GameWindow {
    // Constants
    private static final String WINDOW_TITLE = "Chess";
    private static final String ICON_PATH = "wp.png";
    private static final int BORDER_GAP = 20;
    private static final int CLOCK_UPDATE_INTERVAL = 1000; // milliseconds
    private static final String UNTIMED_TEXT = "Untimed game";

    // UI Components
    private JFrame gameWindow;
    private JLabel blackTimeLabel;
    private JLabel whiteTimeLabel;

    // Game State
    private Clock blackClock;
    private Clock whiteClock;
    private Timer timer;
    private Board board;
    private GameController controller;

    // Game settings
    private String blackPlayerName;
    private String whitePlayerName;
    private int hours;
    private int minutes;
    private int seconds;

    /**
     * Constructs a new game window.
     *
     * @param controller The game controller
     * @param blackName Black player's name
     * @param whiteName White player's name
     * @param hours Time control hours
     * @param minutes Time control minutes
     * @param seconds Time control seconds
     */
    public GameWindow(GameController controller, String blackName, String whiteName,
                      int hours, int minutes, int seconds) {
        this.controller = controller;
        this.blackPlayerName = blackName;
        this.whitePlayerName = whiteName;
        this.hours = hours;
        this.minutes = minutes;
        this.seconds = seconds;

        initializeWindow();
        initializeClocks(hours, minutes, seconds);
    }

    /**
     * Shows the game window.
     */
    public void display() {
        setupUI();
        gameWindow.setVisible(true);
    }

    /**
     * Initializes the game clocks.
     *
     * @param hours Time control hours
     * @param minutes Time control minutes
     * @param seconds Time control seconds
     */
    private void initializeClocks(int hours, int minutes, int seconds) {
        blackClock = new Clock(hours, seconds, minutes);
        whiteClock = new Clock(hours, seconds, minutes);
    }

    /**
     * Initializes the main window.
     */
    private void initializeWindow() {
        gameWindow = new JFrame(WINDOW_TITLE);
        loadWindowIcon();
        gameWindow.setLocation(100, 100);
        gameWindow.setLayout(new BorderLayout(BORDER_GAP, BORDER_GAP));
        gameWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    /**
     * Loads the window icon.
     */
    private void loadWindowIcon() {
        try {
            Image icon = ResourceManager.loadImage(ICON_PATH);
            if (icon != null) {
                gameWindow.setIconImage(icon);
            } else {
                System.err.println("Could not load window icon");
            }
        } catch (Exception e) {
            System.err.println("Error loading window icon: " + e.getMessage());
        }
    }

    /**
     * Sets up the user interface components.
     */
    private void setupUI() {
        // Add game data panel (player names and clocks)
        JPanel gameDataPanel = createGameDataPanel();
        gameWindow.add(gameDataPanel, BorderLayout.NORTH);

        // Add chess board
        board = controller.getGameState().getBoard();
        gameWindow.add(board, BorderLayout.CENTER);

        if (board != null) {
            gameWindow.add(board, BorderLayout.CENTER);
        } else {
            System.err.println("Board not initialized");
        }

        // Add control buttons
        JPanel buttonsPanel = createButtonsPanel();
        gameWindow.add(buttonsPanel, BorderLayout.SOUTH);

        // Finalize window setup
        gameWindow.setMinimumSize(gameWindow.getPreferredSize());
        gameWindow.setSize(gameWindow.getPreferredSize());
        gameWindow.setResizable(false);
        gameWindow.pack();
    }

    /**
     * Creates the panel displaying player names and clocks.
     *
     * @return The game data panel
     */
    private JPanel createGameDataPanel() {
        JPanel gameData = new JPanel();
        gameData.setLayout(new GridLayout(3, 2, 0, 0));

        // Player names
        addPlayerLabels(gameData);

        // Clock displays
        addClockLabels(gameData);

        // Start timer if game is timed
        startTimerIfNeeded();

        gameData.setPreferredSize(gameData.getMinimumSize());
        return gameData;
    }

    /**
     * Adds player name labels to the panel.
     *
     * @param gameData The panel to add labels to
     */
    private void addPlayerLabels(JPanel gameData) {
        JLabel whiteLabel = createCenteredLabel(whitePlayerName);
        JLabel blackLabel = createCenteredLabel(blackPlayerName);

        gameData.add(whiteLabel);
        gameData.add(blackLabel);
    }

    /**
     * Creates a centered label with the given text.
     *
     * @param text Label text
     * @return New JLabel instance
     */
    private JLabel createCenteredLabel(String text) {
        JLabel label = new JLabel(text);
        label.setHorizontalAlignment(JLabel.CENTER);
        label.setVerticalAlignment(JLabel.CENTER);
        label.setSize(label.getMinimumSize());
        return label;
    }

    /**
     * Adds clock labels to the panel.
     *
     * @param gameData The panel to add labels to
     */
    private void addClockLabels(JPanel gameData) {
        whiteTimeLabel = createCenteredLabel(whiteClock.getTime());
        blackTimeLabel = createCenteredLabel(blackClock.getTime());

        boolean isUntimedGame = (hours == 0 && minutes == 0 && seconds == 0);
        if (isUntimedGame) {
            whiteTimeLabel.setText(UNTIMED_TEXT);
            blackTimeLabel.setText(UNTIMED_TEXT);
        }

        gameData.add(whiteTimeLabel);
        gameData.add(blackTimeLabel);
    }

    /**
     * Starts the clock timer if needed.
     */
    private void startTimerIfNeeded() {
        if (hours == 0 && minutes == 0 && seconds == 0) {
            return; // Untimed game
        }

        timer = new Timer(CLOCK_UPDATE_INTERVAL, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateClocks();
            }
        });
        timer.start();
    }

    /**
     * Updates the clocks based on whose turn it is.
     */
    private void updateClocks() {
        if (board == null) return;

        boolean isWhiteTurn = board.getTurn();

        if (isWhiteTurn) {
            updateWhiteClock();
        } else {
            updateBlackClock();
        }
    }

    /**
     * Updates the white clock and checks for timeout.
     */
    private void updateWhiteClock() {
        whiteClock.decr();
        whiteTimeLabel.setText(whiteClock.getTime());

        if (whiteClock.outOfTime()) {
            controller.timeOut(true); // White out of time
        }
    }

    /**
     * Updates the black clock and checks for timeout.
     */
    private void updateBlackClock() {
        blackClock.decr();
        blackTimeLabel.setText(blackClock.getTime());

        if (blackClock.outOfTime()) {
            controller.timeOut(false); // Black out of time
        }
    }

    /**
     * Creates the control buttons panel.
     *
     * @return Panel containing control buttons
     */
    private JPanel createButtonsPanel() {
        JPanel buttons = new JPanel();
        buttons.setLayout(new GridLayout(1, 3, 10, 0));

        JButton instructionsButton = createInstructionsButton();
        JButton newGameButton = createNewGameButton();
        JButton quitButton = createQuitButton();

        buttons.add(instructionsButton);
        buttons.add(newGameButton);
        buttons.add(quitButton);

        buttons.setPreferredSize(buttons.getMinimumSize());
        return buttons;
    }

    /**
     * Creates the instructions button.
     *
     * @return The instructions button
     */
    private JButton createInstructionsButton() {
        JButton button = new JButton("How to play");
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showInstructions();
            }
        });
        return button;
    }

    /**
     * Shows the game instructions.
     */
    private void showInstructions() {
        JOptionPane.showMessageDialog(gameWindow,
                "Move the chess pieces on the board by clicking\n"
                        + "and dragging. The game will watch out for illegal\n"
                        + "moves. You can win either by your opponent running\n"
                        + "out of time or by checkmating your opponent.\n"
                        + "\nGood luck, hope you enjoy the game!",
                "How to play",
                JOptionPane.PLAIN_MESSAGE);
    }

    /**
     * Creates the new game button.
     *
     * @return The new game button
     */
    private JButton createNewGameButton() {
        JButton button = new JButton("New game");
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                confirmNewGame();
            }
        });
        return button;
    }

    /**
     * Confirms starting a new game.
     */
    private void confirmNewGame() {
        int response = JOptionPane.showConfirmDialog(
                gameWindow,
                "Are you sure you want to begin a new game?",
                "Confirm new game",
                JOptionPane.YES_NO_OPTION);

        if (response == JOptionPane.YES_OPTION) {
            SwingUtilities.invokeLater(new StartMenu());
            closeGame();
        }
    }

    /**
     * Creates the quit button.
     *
     * @return The quit button
     */
    private JButton createQuitButton() {
        JButton button = new JButton("Quit");
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                confirmQuit();
            }
        });
        return button;
    }

    /**
     * Confirms quitting the game.
     */
    private void confirmQuit() {
        int response = JOptionPane.showConfirmDialog(
                gameWindow,
                "Are you sure you want to quit?",
                "Confirm quit",
                JOptionPane.YES_NO_OPTION);

        if (response == JOptionPane.YES_OPTION) {
            closeGame();
        }
    }

    /**
     * Closes the game window and stops the timer.
     */
    private void closeGame() {
        if (timer != null) {
            timer.stop();
        }
        gameWindow.dispose();
    }

    /**
     * Notifies the player that checkmate has occurred.
     *
     * @param winnerColor The color of the winner (0 for black, 1 for white)
     */
    public void notifyCheckmate(int winnerColor) {
        if (timer != null) {
            timer.stop();
        }

        String winner = (winnerColor == 1) ? "White" : "Black";
        int response = JOptionPane.showConfirmDialog(
                gameWindow,
                winner + " wins by checkmate! Set up a new game? \n" +
                        "Choosing \"No\" lets you look at the final situation.",
                winner + " wins!",
                JOptionPane.YES_NO_OPTION);

        if (response == JOptionPane.YES_OPTION) {
            SwingUtilities.invokeLater(new StartMenu());
            gameWindow.dispose();
        }
    }

    /**
     * Notifies the player that a timeout has occurred.
     *
     * @param isWhiteWinner The color of the winner (0 for black, 1 for white)
     */
    public void notifyTimeout(boolean isWhiteWinner) {
        if (timer != null) {
            timer.stop();
        }

        String winner = isWhiteWinner ? whitePlayerName : blackPlayerName;
        int response = JOptionPane.showConfirmDialog(
                gameWindow,
                winner + " wins by time! Play a new game? \n" +
                        "Choosing \"No\" quits the game.",
                winner + " wins!",
                JOptionPane.YES_NO_OPTION);

        if (response == JOptionPane.YES_OPTION) {
            SwingUtilities.invokeLater(new StartMenu());
            gameWindow.dispose();
        } else {
            gameWindow.dispose();
        }
    }
}
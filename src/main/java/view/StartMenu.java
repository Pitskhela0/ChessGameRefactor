package view;

import controller.GameController;
import util.ResourceManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;

/**
 * The start menu for the chess game, allowing players to configure a new game.
 */
public class StartMenu implements Runnable {

    @Override
    public void run() {
        final JFrame startWindow = new JFrame("Chess");
        // Set window properties
        startWindow.setLocation(300, 100);
        startWindow.setResizable(false);
        startWindow.setSize(260, 240);

        Box components = Box.createVerticalBox();
        startWindow.add(components);

        // Game title
        final JPanel titlePanel = new JPanel();
        components.add(titlePanel);
        final JLabel titleLabel = new JLabel("Chess");
        titlePanel.add(titleLabel);

        // Black player selections
        final JPanel blackPanel = new JPanel();
        components.add(blackPanel, BorderLayout.EAST);
        final JLabel blackPiece = new JLabel();

        // Load black pawn icon
        // In StartMenu.java
        try {
            Image blackImg = ResourceManager.loadImage("bp.png");
            if (blackImg != null) {
                blackPiece.setIcon(new ImageIcon(blackImg));
                blackPanel.add(blackPiece);
            } else {
                System.err.println("Could not load black pawn icon");
            }
        } catch (Exception e) {
            System.err.println("Error loading black pawn: " + e.getMessage());
        }

        final JTextField blackInput = new JTextField("Black", 10);
        blackPanel.add(blackInput);

        // White player selections
        final JPanel whitePanel = new JPanel();
        components.add(whitePanel);
        final JLabel whitePiece = new JLabel();

        // Load white pawn icon
        try {
            Image whiteImg = ResourceManager.loadImage("wp.png");
            if (whiteImg != null) {
                whitePiece.setIcon(new ImageIcon(whiteImg));
                whitePanel.add(whitePiece);
                startWindow.setIconImage(whiteImg);
            } else {
                System.err.println("Could not load white pawn icon");
            }
        } catch (Exception e) {
            System.err.println("Error loading white pawn: " + e.getMessage());
        }

        final JTextField whiteInput = new JTextField("White", 10);
        whitePanel.add(whiteInput);

        // Timer settings
        final String[] minSecInts = new String[60];
        for (int i = 0; i < 60; i++) {
            if (i < 10) {
                minSecInts[i] = "0" + i;
            } else {
                minSecInts[i] = Integer.toString(i);
            }
        }

        final JComboBox<String> seconds = new JComboBox<>(minSecInts);
        final JComboBox<String> minutes = new JComboBox<>(minSecInts);
        final JComboBox<String> hours = new JComboBox<>(new String[]{"0", "1", "2", "3"});

        Box timerSettings = Box.createHorizontalBox();

        hours.setMaximumSize(hours.getPreferredSize());
        minutes.setMaximumSize(minutes.getPreferredSize());
        seconds.setMaximumSize(minutes.getPreferredSize());

        timerSettings.add(hours);
        timerSettings.add(Box.createHorizontalStrut(10));
        timerSettings.add(minutes);
        timerSettings.add(Box.createHorizontalStrut(10));
        timerSettings.add(seconds);

        timerSettings.add(Box.createVerticalGlue());

        components.add(timerSettings);

        // Buttons
        Box buttons = Box.createHorizontalBox();

        // Quit button
        final JButton quit = new JButton("Quit");
        quit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                startWindow.dispose();
            }
        });

        // Instructions button
        final JButton instr = new JButton("Instructions");
        instr.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(startWindow,
                        "To begin a new game, input player names\n" +
                                "next to the pieces. Set the clocks and\n" +
                                "click \"Start\". Setting the timer to all\n" +
                                "zeroes begins a new untimed game.",
                        "How to play",
                        JOptionPane.PLAIN_MESSAGE);
            }
        });

        // Start button
        final JButton start = new JButton("Start");
        start.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String blackName = blackInput.getText();
                String whiteName = whiteInput.getText();

                try {
                    int hh = Integer.parseInt(Objects.requireNonNull(hours.getSelectedItem()).toString());
                    int mm = Integer.parseInt(Objects.requireNonNull(minutes.getSelectedItem()).toString());
                    int ss = Integer.parseInt(Objects.requireNonNull(seconds.getSelectedItem()).toString());

                    // Create the game controller which will initialize the model and view
                    new GameController(whiteName, blackName, hh, mm, ss);
                    startWindow.dispose();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(startWindow,
                            "Error starting game: " + ex.getMessage(),
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        buttons.add(start);
        buttons.add(Box.createHorizontalStrut(10));
        buttons.add(instr);
        buttons.add(Box.createHorizontalStrut(10));
        buttons.add(quit);
        components.add(buttons);

        Component space = Box.createGlue();
        components.add(space);

        startWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        startWindow.setVisible(true);
    }
}
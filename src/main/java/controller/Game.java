package controller;

import view.StartMenu;

import javax.swing.*;

/**
 * Main entry point for the chess application.
 */
public class Game implements Runnable {

    @Override
    public void run() {
        SwingUtilities.invokeLater(new StartMenu());
    }

    /**
     * Entry point for the application.
     *
     * @param args Command line arguments (not used)
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Game());
    }
}
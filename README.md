# Chess Game MVC Refactoring

A Java application that refactors an existing chess game using the Model-View-Controller (MVC) architectural pattern to improve code organization, maintainability, and testability.

## Project Overview

This project takes an existing chess game implementation and restructures it using the MVC pattern to:
1. Separate game data (Model) from user interface (View) and game logic (Controller)
2. Improve code encapsulation and modularity
3. Add comprehensive unit testing
4. Enhance code documentation and readability
5. Implement proper chess rules including check and checkmate detection

The result is a fully playable two-player chess game with graphical interface, chess clock functionality, and proper enforcement of chess rules.

## Features

- **MVC Architecture**: Clear separation of concerns between:
    - Model (board, pieces, game state)
    - View (UI components)
    - Controller (game logic and user input handling)
- **Complete Chess Rules**: Support for standard chess features:
    - Movement patterns for all pieces
    - Check and checkmate detection
    - Pawn promotion
    - Chess clock functionality
- **Comprehensive Testing**: Unit tests for:
    - Piece movement validation
    - Game state management
    - Board setup and operations
    - Capture mechanics

## Getting Started

### Prerequisites

- Java 11 or higher
- Maven for dependency management and building

### Installation

1. Clone the repository:
   ```
   git clone https://github.com/Pitskhela0/ChessGameRefactor.git
   cd ChessGameRefactor
   ```

2. Build the project:
   ```
   mvn clean package
   ```

### Usage

Run the application:

```
java -jar out/artifacts/ChessGameRefactor_jar/ChessGameRefactor.jar
```


## Project Structure

The project follows a standard MVC structure:

- `controller`: Classes that handle game logic and user input
    - `CheckmateDetector`: Detects check and checkmate conditions
    - `Game`: Application entry point
    - `GameController`: Manages game flow and rules

- `model`: Classes that represent the game state and data
    - `pieces`: Chess piece implementations
        - `Bishop`, `King`, `Knight`, `Pawn`, `Queen`, `Rook`
    - `Board`: Manages the chess board and piece positions
    - `Clock`: Handles chess clock functionality
    - `GameRulesEngine`: Enforces game rules
    - `GameState`: Tracks the overall game state
    - `Piece`: Base class for all chess pieces
    - `Player`: Represents a player in the game
    - `Square`: Represents a square on the chess board

- `view`: Classes that handle the user interface
    - `GameWindow`: Main game window
    - `PieceView`: Visual representation of pieces
    - `SquareView`: Visual representation of board squares
    - `StartMenu`: Game setup screen

- `util`: Utility classes
    - `ResourceManager`: Handles resource loading

## Key Improvements

- **Enhanced Maintainability**: Clean separation of concerns makes code easier to understand and modify
- **Improved Testability**: Modular design allows for comprehensive unit testing
- **Better Encapsulation**: Game components communicate through well-defined interfaces
- **Extended Functionality**: Added support for pawn promotion
- **Centralized Resource Management**: Proper handling of game assets

## Future Enhancements

Potential areas for future improvement include:
- Implementing castling mechanics
- Adding en passant captures
- Supporting game save/load functionality
- Developing an AI opponent
- Adding network play capabilities

## How to Play

1. Launch the game and enter player names
2. Set the clock time (or leave at 0:00:00 for untimed play)
3. Move pieces by clicking and dragging them
4. The game enforces legal moves and detects check/checkmate
5. Use on-screen buttons to view instructions, start a new game, or quit
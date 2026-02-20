package Controller;

import Model.*;
import View.Screen;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

/**
 * base controller class that manages the overall flow of the game


 * holds a reference to the active GameEngine instance
 * responsible for ensuring that all game rules are checked before modifying the Model
 */
public class GameController {
    protected GameEngine gameEngine;
    protected Screen screen;

    public GameController() {
        this.gameEngine = null;
        this.screen = null;
    }

    public GameController(Screen screen) {
        this.screen = screen;
        this.gameEngine = null;
    }

//    /**
//     * initializes the entire game system
//
//     * {@code @pre} the necessary resources and components are ready for instantiation
//
//     * {@code @post} the game starts
//     */
//    public void initializeSystem() {
//        if (gameEngine != null) {
//            gameEngine.initializeGame();
//
//            if (screen != null) {
//                screen.initializeDisplay();
//                screen.updateGameState(getCurrentGameState());
//            }
//        }
//    }

    /**
     * checks if move is legal
     *
     * @param moveDetails a move of an object
     * @return boolean true if the move is valid according to the current game state, false otherwise

     * {@code @pre} moveDetails is not null

     * {@code @post} returns the validation result
     */
    public boolean checkMoveValidity(Object moveDetails) {
        if (moveDetails == null || gameEngine == null) {
            return false;
        }

        if (moveDetails instanceof String move) {
            return checkStringMoveValidity(move);
        }

        return false;
    }

    private boolean checkStringMoveValidity(String move) {
        if (move.startsWith("draw_tiles")) {
            return gameEngine.getCurrentPlayer() != null;
        }
        else if (move.startsWith("select_area:")) {
            String[] parts = move.split(":");
            if (parts.length == 3) {
                String area = parts[1];
                int count = Integer.parseInt(parts[2]);
                return isValidAreaSelection(area, count);
            }
        }
        else if (move.startsWith("use_card:")) {
            String cardName = move.split(":")[1];
            return isValidCardUse(cardName);
        }

        return false;
    }

    private boolean isValidAreaSelection(String area, int count) {
        if (count < 1 || count > 2) return false;

        String[] validAreas = {"mosaic", "statue", "skeleton", "amphora"};
        for (String validArea : validAreas) {
            if (validArea.equals(area)) {
                return true;
            }
        }
        return false;
    }

    private boolean isValidCardUse(String cardName) {
        Player currentPlayer = gameEngine.getCurrentPlayer();
        if (currentPlayer == null) return false;

        String[] validCards = {"Assistant", "Archaeologist", "Excavator", "Professor", "Programmer"};
        for (String validCard : validCards) {
            if (validCard.equals(cardName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * processes a validated move, updates the model, and then updates the view
     *
     * @param moveDetails an object or data structure describing the validated move

     * {@code pre} moveDetails represents a valid move for the current game state

     * {@code @post} the GameEngine is updated
     * {@code @post} the Screen is notified to redraw the new game state
     */
    public void executeMove(Object moveDetails) {
        if (!checkMoveValidity(moveDetails)) {
            return;
        }

        if (moveDetails instanceof String move) {
            executeStringMove(move);
        }

        updateView();
    }

    private void executeStringMove(String move) {
        if (move.equals("draw_tiles")) {
            executeDrawTiles();
        }
        else if (move.startsWith("select_area:")) {
            String[] parts = move.split(":");
            String area = parts[1];
            int count = Integer.parseInt(parts[2]);
            executeSelectArea(area, count);
        }
        else if (move.startsWith("use_card:")) {
            String cardName = move.split(":")[1];
            executeUseCard(cardName);
        }
    }

    private void executeDrawTiles() {
        if (gameEngine != null) {
            Player currentPlayer = gameEngine.getCurrentPlayer();
            if (currentPlayer != null) {
                List<Tile> drawnTiles = GameEngine.drawT(4);
                for (Tile tile : drawnTiles) {
                    gameEngine.getBoard().placeTileInArea(tile);
                }
            }
            checkForGameEnd();
        }
    }

    private void executeSelectArea(String area, int count) {
        if (gameEngine != null && gameEngine.getBoard() != null) {
            Player currentPlayer = gameEngine.getCurrentPlayer();
            if (currentPlayer != null) {
                List<Tile> selectedTiles = gameEngine.getBoard().takeFromArea(area, count);
                if (selectedTiles != null && !selectedTiles.isEmpty()) {
                    currentPlayer.addTiles(selectedTiles);
                } else {
                    if (screen != null) {
                        screen.showMessage("No tiles available in " + area + " area");
                    }
                }
            }
        }
    }

    private void executeUseCard(String cardName) {
        Player currentPlayer = gameEngine.getCurrentPlayer();
        if (currentPlayer != null) {
            currentPlayer.useCharacterPower(findCardByName(cardName));

            if (screen != null) {
                screen.showMessage("player used " + cardName);
            }

            endCurrentTurn();
        }
    }

    private Charactercard findCardByName(String cardName) {
        return null;
    }

    private void endCurrentTurn() {
        if (gameEngine != null) {
            gameEngine.nextTurn();

            Player nextPlayer = gameEngine.getCurrentPlayer();
            if (screen != null && nextPlayer != null) {
                screen.updatePlayerTurn(nextPlayer.getColor());
            }
        }
    }

    private void checkForGameEnd() {
        if (gameEngine != null && gameEngine.getBoard() != null) {
            if (gameEngine.getBoard().isGameOver()) {
                Player winner = gameEngine.checkWinner();
                if (screen != null && winner != null) {
                    screen.showWinner(winner.getColor(), winner.calculateScore());
                }
            }
        }
    }

    private void updateView() {
        if (screen != null) {
            screen.updateGameState(getCurrentGameState());
        }
    }

    private Object getCurrentGameState() {
        if (gameEngine != null && gameEngine.getBoard() != null) {
            return new Controller.GameStateDTO(
                    gameEngine.getCurrentPlayer(),
                    gameEngine.getBoard(),
                    gameEngine.getPlayers()
            );
        }
        return null;
    }


    public void handleDrawTiles() {
        if (gameEngine == null) return;

        Player currentPlayer = gameEngine.getCurrentPlayer();

        if (currentPlayer.hasDrawnThisTurn()) {
            if (screen != null) {
                screen.showMessage("Already drew this turn! End turn to continue.");
            }
            return;
        }

        currentPlayer.setDrawnThisTurn(true);

        List<Tile> drawnTiles = gameEngine.executePlayerTurn();

        if (screen != null) {
            int playerNum = Integer.parseInt(currentPlayer.getColor());
            screen.showMessage("player " + playerNum + " drew 4");
            updateVisualAreas();
            screen.displayGameState(gameEngine);

            promptTileSelectionTwice();
        }

        checkForGameEnd();
    }

    private void promptTileSelectionTwice() {
        if (screen == null) return;

        String[] areas = {"mosaic", "statue", "skeleton", "amphora"};
        String selectedArea = (String) JOptionPane.showInputDialog(
                screen,
                "select area",
                "",
                JOptionPane.QUESTION_MESSAGE,
                null,
                areas,
                areas[0]
        );

        if (selectedArea == null) return;

        // First tile
        String tile1Str = JOptionPane.showInputDialog(
                screen,
                "select tile number from the area",
                "",
                JOptionPane.QUESTION_MESSAGE
        );

        if (tile1Str != null) {
            try {
                int tile1 = Integer.parseInt(tile1Str.trim());
                handleTileSelection(selectedArea, tile1);
            } catch (NumberFormatException e) {
                screen.showMessage("Invalid number!");
            }
        }

        // Second tile (optional)
        String tile2Str = JOptionPane.showInputDialog(
                screen,
                "select second tile",
                "",
                JOptionPane.QUESTION_MESSAGE
        );

        if (tile2Str != null) {
            try {
                int tile2 = Integer.parseInt(tile2Str.trim());
                handleTileSelection(selectedArea, tile2);
            } catch (NumberFormatException e) {
                screen.showMessage("Invalid number!");
            }
        }


    }

    public void handleEndTurn() {
        if (gameEngine == null) return;

        Player currentPlayer = gameEngine.getCurrentPlayer();
        if (currentPlayer == null) return;

        currentPlayer.setDrawnThisTurn(false);

        int playerNum = Integer.parseInt(currentPlayer.getColor());
        if (screen != null) {
            screen.showMessage("turn ended from player" + playerNum);
        }

        gameEngine.nextTurn();

        gameEngine.calculateStatueScores();
        Player nextPlayer = gameEngine.getCurrentPlayer();


        if (screen != null && nextPlayer != null) {
            nextPlayer.calculateScore();
            screen.updatePlayerTurn(nextPlayer.getColor());
            screen.displayGameState(gameEngine);
        }

        checkForGameEnd();
    }


    public void handleTileSelection(String areaType, int tileNumber) {
        if (gameEngine == null || gameEngine.getBoard() == null) return;

        Player currentPlayer = gameEngine.getCurrentPlayer();
        if (currentPlayer == null) return;

        List<Tile> areaTiles = gameEngine.getBoard().getAreaTiles(areaType);

        if (tileNumber < 1 || tileNumber > areaTiles.size()) {
            if (screen != null) {
                screen.showMessage("Invalid tile number! Choose between 1 and " + areaTiles.size());
            }
            return;
        }

        int index = tileNumber - 1;
        Tile selectedTile = gameEngine.getBoard().takeTileByIndex(areaType, index);

        if (selectedTile != null) {
            List<Tile> tiles = new ArrayList<>();
            tiles.add(selectedTile);
            currentPlayer.addTiles(tiles);

            if (screen != null) {
                int playerNum = Integer.parseInt(currentPlayer.getColor());
                screen.showMessage("player " + playerNum + " took tile " + tileNumber + " from " + areaType + " area");
                updateVisualAreas();
                screen.displayGameState(gameEngine);
            }
        } else {
            if (screen != null) {
                screen.showMessage("error");
            }
        }
    }

    public void handleCharacterPower(String cardName) {
        if (gameEngine == null) return;

        Player currentPlayer = gameEngine.getCurrentPlayer();
        if (currentPlayer == null) return;

        if (!canUseCharacterCard(currentPlayer, cardName)) {
            if (screen != null) {
                screen.showMessage("player " + currentPlayer.getColor() + "has used " + cardName);
            }
            return;
        }

        currentPlayer.markCardAsUsed(cardName);

        if (screen != null) {
            screen.showMessage("player " + currentPlayer.getColor() + " used " + cardName);
        }

        executeCharacterCardAbility(cardName, currentPlayer);
    }

    private boolean canUseCharacterCard(Player player, String cardName) {
        return !player.isCardUsed(cardName);
    }

    private void executeCharacterCardAbility(String cardName, Player player) {
        switch (cardName) {
            case "Assistant":
                handleAssistantCard(player);
                break;
            case "Archaeologist":
                handleArchaeologistCard(player);
                break;
            case "Excavator":
                handleExcavatorCard(player);
                break;
            case "Professor":
                handleProfessorCard(player);
                break;
            case "Programmer":
                handleProgrammerCard(player);
                break;
        }
    }

    private void handleAssistantCard(Player player) {
        if (screen != null) {
            screen.showMessage("Assistant: Take 1 tile from any area. Select area and tile.");
        }
    }

    private void handleArchaeologistCard(Player player) {
        if (screen != null) {
            screen.showMessage("Archaeologist: Take up to 2 tiles from any area except the one chosen earlier.");
        }
    }

    private void handleExcavatorCard(Player player) {
        if (screen != null) {
            screen.showMessage("Excavator: Take up to 2 tiles from the area chosen earlier.");
        }
    }

    private void handleProfessorCard(Player player) {
        if (screen != null) {
            screen.showMessage("Professor: Take 1 tile from each area except the one chosen earlier.");
        }
    }

    private void handleProgrammerCard(Player player) {
        if (screen != null) {
            screen.showMessage("Programmer: Select an area. Next turn, draw 2 tiles from it.");
        }
    }

    private void updateVisualAreas() {
        if (screen == null || gameEngine == null || gameEngine.getBoard() == null) return;

        screen.updateAreaTiles("mosaic", gameEngine.getBoard().getAreaTiles("mosaic"));
        screen.updateAreaTiles("statue", gameEngine.getBoard().getAreaTiles("statue"));
        screen.updateAreaTiles("skeleton", gameEngine.getBoard().getAreaTiles("skeleton"));
        screen.updateAreaTiles("amphora", gameEngine.getBoard().getAreaTiles("amphora"));
        screen.updateAreaTiles("landslide", gameEngine.getBoard().getLandslideTiles());
    }




    public void setGameEngine(GameEngine gameEngine) {
        this.gameEngine = gameEngine;
    }

    public void setScreen(Screen screen) {
        this.screen = screen;
    }

    public GameEngine getGameEngine() {
        return gameEngine;
    }


}


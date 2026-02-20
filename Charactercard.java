package Controller;

import Model.AreaType;
import Model.Player;
import Model.Tile;
import View.Screen;

/**
 * will be used to handle user input

 *
 * {@code @invariant} all user inputs are validated before passing them to the Model
 * {@code @invariant} IOhandler maintains a reference to the active GameEngine instance
 */
public class IOhandler extends GameController {

    public IOhandler(Screen screen) {
        super(screen);
    }

    /**
     * processes the user's action of selecting tiles from a designated area
     *
     * @param area the identifier of the area
     * @param tile a list of indices indicating which tiles were selected

     * {@code @pre} the areaId is valid and the indices correspond to available tiles
     * {@code @pre} it is the current player's turn to pick tiles

     * {@code @post} the selected tiles are moved from the area to the current player's inventory
     * {@code @post} the game state in GameEngine is updated to reflect the move
     */
    public void handleTileSelection(AreaType area , Tile tile) {
        if (gameEngine == null || gameEngine.getCurrentPlayer() == null) {
            return;
        }

        Player currentPlayer = gameEngine.getCurrentPlayer();
        String areaName = getAreaName(area);

        if (areaName != null) {
            String move = "select_area:" + areaName + ":1";
            if (checkMoveValidity(move)) {
                executeMove(move);
            }
        }
    }

    /**
     * processes the user's decision to use their character card power

     * {@code @pre} it is the current player's turn
     * {@code @pre} the player's character card has not been used yet (!player.isUsed())

     * {@code @post} the character power is applied
     * {@code @post} the player's character card is marked as used.
     */
    public void handleCharacterPowerUse() {

    }

    /**
     * processes the end of the current player's turn

     * {@code @pre} the player has completed all necessary actions for the turn

     * {@code @post} GameEngine.nextTurn() is called
     * {@code @post} the game state transitions to the next player
     */
    public void handleEndTurn() {
        if (gameEngine != null) {
            String move = "end_turn";
            executeMove(move);
        }
    }

    private String getAreaName(AreaType area) {
        if (area.getX() == 100 && area.getY() == 100) return "mosaic";
        if (area.getX() == 200 && area.getY() == 100) return "statue";
        if (area.getX() == 300 && area.getY() == 100) return "skeleton";
        if (area.getX() == 400 && area.getY() == 100) return "amphora";
        return null;
    }
}
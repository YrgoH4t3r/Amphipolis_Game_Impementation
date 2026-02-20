package Main;

import Controller.GameController;
import Model.GameEngine;
import Model.Player;
import View.Screen;
import Controller.IOhandler;
import javax.swing.SwingUtilities;
import java.io.File;

public class Main {
    public static void main(String[] args) {

                System.out.println("=== Starting Amphipolis Game ===");

                Screen screen = new Screen();
                GameEngine gameEngine = new GameEngine();
                GameController controller = new GameController(screen);

                screen.setGameController(controller);
                controller.setGameEngine(gameEngine);

                IOhandler ioHandler = new IOhandler(screen);
                ioHandler.setGameEngine(gameEngine);


                gameEngine.initializeGame();
                screen.initializeDisplay();
                Player firstPlayer = gameEngine.getCurrentPlayer();
                if (firstPlayer != null) {
                    screen.updatePlayerTurn(firstPlayer.getColor());
                }


                screen.displayGameState(gameEngine);

                System.out.println("=== Game Started ===");
                System.out.println("Players: " + gameEngine.getPlayers().size());
                if (firstPlayer != null) {
                    System.out.println("Current player: " + firstPlayer.getColor());
                }

    }
}
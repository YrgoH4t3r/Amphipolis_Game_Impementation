package Controller;

import Model.Player;
import Model.Board;

public class GameStateDTO {
    public Player currentPlayer;
    public Board board;
    public java.util.List<Player> players;

    public GameStateDTO(Player currentPlayer, Board board, java.util.List<Player> players) {
        this.currentPlayer = currentPlayer;
        this.board = board;
        this.players = players;
    }
}
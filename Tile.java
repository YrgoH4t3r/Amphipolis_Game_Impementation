package Model;

import java.util.*;

public class GameEngine {
    private List<Player> players = new ArrayList<>();
    private static List<Tile> AllTiles = new ArrayList<>();
    private Board board;
    private int currentTurnIndex;
    private Random random;

    public GameEngine() {
        this.board = new Board();
        this.random = new Random();
    }

    /**
     * initializes the game with all components and players
     *
     * {@code @pre} the game state is uninitialized
     *
     * {@code @post} players list is populated
     * {@code @post} game components are initialized
     * {@code @post} currentturnindex is set to 0
     */
    public void initializeGame() {
        initializeAllTiles();
        board.initializeTiles();

        String[] colors = {"1", "2", "3", "4"};
        for (String color : colors) {
            Player player = new Player(color);
            players.add(player);
            board.addPlayer(player);
        }

        currentTurnIndex = 0;
    }

    /**
     * initializes and shuffles all tiles used in the game
     *
     * {@code @pre} not initialized
     *
     * {@code @post} alltiles contains all tiles shuffled
     */
    private void initializeAllTiles() {
        AllTiles.clear();

        for (int i = 0; i < 9; i++) {
            SimpleTile genericTile = new SimpleTile("green");
            MosaicT mosaic = new MosaicT(genericTile);
            AllTiles.add(mosaic);
        }
        for (int i = 0; i < 9; i++) {
            SimpleTile genericTile = new SimpleTile("red");
            MosaicT mosaic = new MosaicT(genericTile);
            AllTiles.add(mosaic);
        }
        for (int i = 0; i < 9; i++) {
            SimpleTile genericTile = new SimpleTile("yellow");
            MosaicT mosaic = new MosaicT(genericTile);
            AllTiles.add(mosaic);
        }

        for (int i = 0; i < 12; i++) {
            SimpleTile genericTile = new SimpleTile("caryatid");
            StatueT statue = new StatueT(genericTile);
            AllTiles.add(statue);
        }
        for (int i = 0; i < 12; i++) {
            SimpleTile genericTile = new SimpleTile("sphinx");
            StatueT statue = new StatueT(genericTile);
            AllTiles.add(statue);
        }

        for (int i = 0; i < 10; i++) {
            SimpleTile genericTile = new SimpleTile("adult_top");
            SkeletonT skeleton = new SkeletonT(genericTile);
            AllTiles.add(skeleton);
        }
        for (int i = 0; i < 10; i++) {
            SimpleTile genericTile = new SimpleTile("adult_bottom");
            SkeletonT skeleton = new SkeletonT(genericTile);
            AllTiles.add(skeleton);
        }
        for (int i = 0; i < 5; i++) {
            SimpleTile genericTile = new SimpleTile("child_top");
            SkeletonT skeleton = new SkeletonT(genericTile);
            AllTiles.add(skeleton);
        }
        for (int i = 0; i < 5; i++) {
            SimpleTile genericTile = new SimpleTile("child_bottom");
            SkeletonT skeleton = new SkeletonT(genericTile);
            AllTiles.add(skeleton);
        }

        String[] colors = {"blue", "brown", "red", "green", "yellow", "purple"};
        for (String color : colors) {
            for (int i = 0; i < 5; i++) {
                SimpleTile genericTile = new SimpleTile(color);
                AmphoraT amphora = new AmphoraT(genericTile);
                AllTiles.add(amphora);
            }
        }

        for (int i = 0; i < 24; i++) {
            SimpleTile genericTile = new SimpleTile("landslide");
            LandslideT landslide = new LandslideT(genericTile);
            AllTiles.add(landslide);
        }

        shuffleAllTiles();
    }

    /**
     * shuffles all tiles randomly
     *
     * {@code @pre} alltiles is initialized
     *
     * {@code @post} tiles are randomly reordered
     */
    private void shuffleAllTiles() {
        for (int i = 0; i < AllTiles.size(); i++) {
            int j = random.nextInt(AllTiles.size());
            Tile temp = AllTiles.get(i);
            AllTiles.set(i, AllTiles.get(j));
            AllTiles.set(j, temp);
        }
    }

    /**
     * draws tiles from the available tiles
     *
     * @param count number of tiles to draw
     * @return list of drawn tiles
     *
     * {@code @pre} count is positive and enough tiles exist
     *
     * {@code @post} returned list contains drawn tiles
     * {@code @post} drawn tiles are removed from alltiles
     */
    public static List<Tile> drawT(int count) {
        List<Tile> drawnTiles = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            if (!AllTiles.isEmpty()) {
                Tile tile = AllTiles.removeFirst();
                drawnTiles.add(tile);
            }
        }
        return drawnTiles;
    }

    /**
     * calculates total score of all players
     *
     * {@code @pre} game end condition is reached
     *
     * {@code @post} player scores are updated
     */
    public int calculateAllScores() {
        int sum = 0;
        for (Player player : players) {
            sum += player.calculateScore();
        }
        return sum;
    }

    /**
     * returns the current player
     *
     * @return current player or null
     *
     * {@code @pre} game is initialized
     *
     * {@code @post} returned player matches currentturnindex
     */
    public Player getCurrentPlayer() {
        if (players.isEmpty() || currentTurnIndex < 0 || currentTurnIndex >= players.size()) {
            return null;
        }
        return players.get(currentTurnIndex);
    }

    /**
     * advances to the next players turn
     *
     * {@code @pre} current player finished turn
     *
     * {@code @post} currentturnindex is updated
     */
    public void nextTurn() {
        currentTurnIndex = (currentTurnIndex + 1) % players.size();
        board.nextPlayer();
    }

    /**
     * checks if the game ended and determines the winner
     *
     * @return winning player or null
     *
     * {@code @pre} true
     *
     * {@code @post} returns winner if game is over
     */
    public Player checkWinner() {
        if (!board.isGameOver()) {
            return null;
        }

        calculateAllScores();

        Player winner = null;
        int highestScore = -1;

        for (Player player : players) {
            int score = player.calculateScore();
            if (score > highestScore) {
                highestScore = score;
                winner = player;
            }
        }

        return winner;
    }

    /**
     * returns the game board
     *
     * @return board
     */
    public Board getBoard() {
        return board;
    }

    /**
     * returns all players
     *
     * @return list of players
     */
    public List<Player> getPlayers() {
        return players;
    }

    /**
     * executes a full player turn
     *
     * {@code @pre} game is initialized
     *
     * {@code @post} tiles are drawn and placed
     */
    public List<Tile> executePlayerTurn() {
        Player current = getCurrentPlayer();
        if (current == null) return null;

        List<Tile> drawn = drawT(4);
        for (Tile tile : drawn) {
            board.placeTileInArea(tile);
        }

        return drawn;
    }

    /**
     * calculates statue scores for all players
     *
     * {@code @pre} statue tiles exist
     *
     * {@code @post} statue points are assigned
     */
    public void calculateStatueScores() {
        Map<Player, Integer> caryatidCounts = new HashMap<>();
        Map<Player, Integer> sphinxCounts = new HashMap<>();

        for (Player player : players) {
            int caryatids = 0;
            int sphinxes = 0;

            for (StatueT statue : player.getStatueTiles()) {
                if (statue.getColour().equals("caryatid")) {
                    caryatids++;
                } else if (statue.getColour().equals("sphinx")) {
                    sphinxes++;
                }
            }

            caryatidCounts.put(player, caryatids);
            sphinxCounts.put(player, sphinxes);
        }

        assignStatuePoints(caryatidCounts);
        assignStatuePoints(sphinxCounts);
    }

    /**
     * assigns statue points based on counts
     *
     * {@code @pre} statue counts are provided
     *
     * {@code @post} players receive statue points
     */
    private void assignStatuePoints(Map<Player, Integer> statueCounts) {
        int maxCount = statueCounts.values().stream().max(Integer::compareTo).orElse(0);
        int minCount = statueCounts.values().stream().min(Integer::compareTo).orElse(0);

        if (maxCount == minCount) {
            return;
        }

        for (Map.Entry<Player, Integer> entry : statueCounts.entrySet()) {
            Player player = entry.getKey();
            int count = entry.getValue();

            if (count == maxCount && count > 0) {
                player.addStatuePoints(6);
            } else if (count > minCount && count > 0) {
                player.addStatuePoints(3);
            }
        }
    }
}

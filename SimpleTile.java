package Model;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Board {
    private List<Tile> bag;
    private List<Tile> landslideTiles;
    private AreaType mosaicArea;
    private AreaType statueArea;
    private AreaType skeletonArea;
    private AreaType amphoraArea;
    private List<Player> players;
    private int currentPlayer;
    private Random random;

    public Board() {
        this.bag = new ArrayList<>();
        this.landslideTiles = new ArrayList<>();
        this.players = new ArrayList<>();
        this.random = new Random();
        this.currentPlayer = 0;

        this.mosaicArea = new AreaType(100, 100);
        this.statueArea = new AreaType(200, 100);
        this.skeletonArea = new AreaType(300, 100);
        this.amphoraArea = new AreaType(400, 100);
    }

    public void initializeTiles() {
        addMosaicTiles();
        addStatueTiles();
        addSkeletonTiles();
        addAmphoraTiles();
        addLandslideTiles();
        shuffleBag();
    }

    private void addMosaicTiles() {
        for (int i = 0; i < 9; i++) {
            SimpleTile genericTile = new SimpleTile("green");
            MosaicT mosaic = new MosaicT(genericTile);
            bag.add(mosaic);
        }
        for (int i = 0; i < 9; i++) {
            SimpleTile genericTile = new SimpleTile("red");
            MosaicT mosaic = new MosaicT(genericTile);
            bag.add(mosaic);
        }
        for (int i = 0; i < 9; i++) {
            SimpleTile genericTile = new SimpleTile("yellow");
            MosaicT mosaic = new MosaicT(genericTile);
            bag.add(mosaic);
        }
    }

    private void addStatueTiles() {
        for (int i = 0; i < 12; i++) {
            SimpleTile genericTile = new SimpleTile("caryatid");
            StatueT statue = new StatueT(genericTile);
            bag.add(statue);
        }
        for (int i = 0; i < 12; i++) {
            SimpleTile genericTile = new SimpleTile("sphinx");
            StatueT statue = new StatueT(genericTile);
            bag.add(statue);
        }
    }

    private void addSkeletonTiles() {
        for (int i = 0; i < 10; i++) {
            SimpleTile genericTile = new SimpleTile("adult_top");
            SkeletonT skeleton = new SkeletonT(genericTile);
            bag.add(skeleton);
        }
        for (int i = 0; i < 10; i++) {
            SimpleTile genericTile = new SimpleTile("adult_bottom");
            SkeletonT skeleton = new SkeletonT(genericTile);
            bag.add(skeleton);
        }
        for (int i = 0; i < 5; i++) {
            SimpleTile genericTile = new SimpleTile("child_top");
            SkeletonT skeleton = new SkeletonT(genericTile);
            bag.add(skeleton);
        }
        for (int i = 0; i < 5; i++) {
            SimpleTile genericTile = new SimpleTile("child_bottom");
            SkeletonT skeleton = new SkeletonT(genericTile);
            bag.add(skeleton);
        }
    }

    private void addAmphoraTiles() {
        String[] colors = {"blue", "brown", "red", "green", "yellow", "purple"};
        for (String color : colors) {
            for (int i = 0; i < 5; i++) {
                SimpleTile genericTile = new SimpleTile(color);
                AmphoraT amphora = new AmphoraT(genericTile);
                bag.add(amphora);
            }
        }
    }

    private void addLandslideTiles() {
        for (int i = 0; i < 24; i++) {
            SimpleTile genericTile = new SimpleTile("landslide");
            LandslideT landslide = new LandslideT(genericTile);
            bag.add(landslide);
        }
    }

    private void shuffleBag() {
        for (int i = 0; i < bag.size(); i++) {
            int j = random.nextInt(bag.size());
            Tile temp = bag.get(i);
            bag.set(i, bag.get(j));
            bag.set(j, temp);
        }
    }

    public List<Tile> drawFromBag(int count) {
        List<Tile> drawn = new ArrayList<>();
        for (int i = 0; i < count && !bag.isEmpty(); i++) {
            drawn.add(bag.removeFirst());
        }
        return drawn;
    }

    public void placeTileInArea(Tile tile) {
        if (tile instanceof MosaicT) {
            mosaicArea.addTile(tile);
        } else if (tile instanceof StatueT) {
            statueArea.addTile(tile);
        } else if (tile instanceof SkeletonT) {
            skeletonArea.addTile(tile);
        } else if (tile instanceof AmphoraT) {
            amphoraArea.addTile(tile);
        } else if (tile instanceof LandslideT) {
            if (landslideTiles.size() < 16) {
                landslideTiles.add(tile);
            }
        }
    }

    public List<Tile> getAreaTiles(String areaType) {
        return switch (areaType.toLowerCase()) {
            case "mosaic" -> mosaicArea.getTiles();
            case "statue" -> statueArea.getTiles();
            case "skeleton" -> skeletonArea.getTiles();
            case "amphora" -> amphoraArea.getTiles();
            default -> new ArrayList<>();
        };
    }

    public List<Tile> getLandslideTiles() {
        return new ArrayList<>(landslideTiles);
    }

    public Tile takeTileByIndex(String areaType, int index) {
        AreaType targetArea = switch (areaType.toLowerCase()) {
            case "mosaic" -> mosaicArea;
            case "statue" -> statueArea;
            case "skeleton" -> skeletonArea;
            case "amphora" -> amphoraArea;
            default -> null;
        };

        if (targetArea != null) {
            return targetArea.removeTileByIndex(index);
        }

        return null;
    }

    public List<Tile> takeFromArea(String areaType, int count) {
        return switch (areaType) {
            case "mosaic" -> mosaicArea.removeTiles(count);
            case "statue" -> statueArea.removeTiles(count);
            case "skeleton" -> skeletonArea.removeTiles(count);
            case "amphora" -> amphoraArea.removeTiles(count);
            default -> new ArrayList<>();
        };
    }

    public void addPlayer(Player player) {
        players.add(player);
    }
//
//    public Player getCurrentPlayer() {
//        if (players.isEmpty()) return null;
//        return players.get(currentPlayer);
//    }

    public void nextPlayer() {
        currentPlayer = (currentPlayer + 1) % players.size();
    }

    public boolean isGameOver() {
        return landslideTiles.size() >= 16;
    }
}
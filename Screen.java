package Model;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Player {
    private final String color; //made by accident into string and then i could not make again the ui part to work
    // due to time restrictions so i implemented a enum type instead of making it into integer value
    private final List<Charactercard> characterCards;
    private final List<MosaicT> mosaicTiles;
    private final List<StatueT> statueTiles;
    private final List<SkeletonT> skeletonTiles;
    private final List<AmphoraT> amphoraTiles;
    private String card;
    private int score;
    private final List<String> usedCards = new ArrayList<>();



    public Player(String color) {
        this.color = color;
        this.score = 0;

        this.characterCards = new ArrayList<>();
        this.mosaicTiles = new ArrayList<>();
        this.statueTiles = new ArrayList<>();
        this.skeletonTiles = new ArrayList<>();
        this.amphoraTiles = new ArrayList<>();

        initializeCharacterCards();
    }

    private void initializeCharacterCards() {
        characterCards.add(new AssistantCard());
        characterCards.add(new Archaelogist());
        characterCards.add(new ExcavatorCard());
        characterCards.add(new ProfessorCard());
        characterCards.add(new ProgrammerCard());
    }



    public String getColor() {
        return color;
    }
    /**0
     * uses game egnine to draw tiles for the player then add it into his List

     * */
    public void drawTiles(int count) {
        List<Tile> drawnTiles = GameEngine.drawT(count);
        addTiles(drawnTiles);
    }

    /**
     * adds tiles to the player's collection based on their type

     * */
    public void addTiles(List<Tile> tiles) {
        for (Tile tile : tiles) {
            if (tile instanceof MosaicT) {
                mosaicTiles.add((MosaicT) tile);
            } else if (tile instanceof StatueT) {
                statueTiles.add((StatueT) tile);
            } else if (tile instanceof SkeletonT) {
                skeletonTiles.add((SkeletonT) tile);
            } else if (tile instanceof AmphoraT) {
                amphoraTiles.add((AmphoraT) tile);
            }
        }
    }

    private boolean drawnThisTurn = false;

    public boolean hasDrawnThisTurn() {
        return drawnThisTurn;
    }

    public void setDrawnThisTurn(boolean drawn) {
        this.drawnThisTurn = drawn;
    }

    /**
     * calculates and returns the player's current score
     *
     */
    public int calculateScore() {
        int totalScore = 0;
        statuePoints = 0;

        totalScore += calculateMosaicScore();
        totalScore += calculateSkeletonScore();
        totalScore += calculateAmphoraScore();
        totalScore += statuePoints;


        this.score = totalScore;
        return totalScore;
    }

    private int calculateMosaicScore() {
        int score = 0;
        int tilesCount = mosaicTiles.size();
        int completeMosaics = tilesCount / 4;

        for (int i = 0; i < completeMosaics; i++) {
            int startIdx = i * 4;
            String firstColor = mosaicTiles.get(startIdx).getColour();
            boolean sameColor = true;

            for (int j = 1; j < 4; j++) {
                if (!mosaicTiles.get(startIdx + j).getColour().equals(firstColor)) {
                    sameColor = false;
                    break;
                }
            }

            score += sameColor ? 4 : 2;
        }

        return score;
    }

    private int calculateSkeletonScore() {
        int score = 0;
        Map<String, Integer> topParts = new HashMap<>();
        Map<String, Integer> bottomParts = new HashMap<>();

        for (SkeletonT skeleton : skeletonTiles) {
            String color = skeleton.getColour();

            if (color.contains("top")) {
                String type = color.replace("_top", "");
                topParts.put(type, topParts.getOrDefault(type, 0) + 1);
            } else if (color.contains("bottom")) {
                String type = color.replace("_bottom", "");
                bottomParts.put(type, bottomParts.getOrDefault(type, 0) + 1);
            }
        }

        int completeAdults = Math.min(
                topParts.getOrDefault("adult", 0),
                bottomParts.getOrDefault("adult", 0)
        );

        int completeChildren = Math.min(
                topParts.getOrDefault("child", 0),
                bottomParts.getOrDefault("child", 0)
        );

        int families = Math.min(completeAdults / 2, completeChildren);
        score += families * 6;

        int remainingAdults = completeAdults - (families * 2);
        int remainingChildren = completeChildren - families;

        score += remainingAdults + remainingChildren;

        return score;
    }

    private int calculateAmphoraScore() {
        Map<String, Integer> colorCounts = new HashMap<>();

        for (AmphoraT amphora : amphoraTiles) {
            String color = amphora.getColour();
            colorCounts.put(color, colorCounts.getOrDefault(color, 0) + 1);
        }

        int uniqueColors;
        uniqueColors = colorCounts.size();

        return switch (uniqueColors) {
            case 6 -> 6;
            case 5 -> 4;
            case 4 -> 2;
            case 3 -> 1;
            default -> 0;
        };
    }

    private int statuePoints = 0;

    public void addStatuePoints(int points) {
        this.statuePoints += points;
    }

    public int getStatuePoints() {
        return statuePoints;
    }


    /**
     * activates a character card's special ability
     */
    public void useCharacterPower(Charactercard card) {
        // Implement each character's special power
    }

    public void markCardAsUsed(String cardName) {
        if (!usedCards.contains(cardName)) {
            usedCards.add(cardName);
        }
    }

    public List<StatueT> getStatueTiles() {
        return statueTiles;
    }

    public boolean isCardUsed(String cardName) {
        return usedCards.contains(cardName);
    }
}
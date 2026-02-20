package Model;

public class AssistantCard implements Charactercard{
    private String cardName="Assistant";
    private boolean used = false;

    @Override
    /**
     * uses the assistant's ability to select a tile from an area
     * the area has no limitations

     *
     * @pre player is not null and it is the current player's turn
     * @pre board is not null
     * @pre !isUsed() (the ability has not been used this game)
     *
     * @post isUsed is true
     */
    public void useAbility(Player player, Board board) {
        if (used) {
            return;
        }
        used = true;
    }

    @Override
    public boolean isUsed() {
        return used;
    }

    @Override
    public String getCardName() {
        return cardName;
    }
}

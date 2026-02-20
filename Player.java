package Model;

public class Archaelogist implements Charactercard{
    private boolean used = false;
    private String cardName="Archaelogist";

    @Override
    /*
      uses the archaologist's ability to select up to 2 tiles from an area
      except the area that he chose the previous turn


      @pre player is not null and it is the current player's turn
     * @pre board is not null.
     * @pre !isUsed() (the ability has not been used this game)
     *
     * @post isUsed is true
     * @post the player is marked with a delayed action, targeting a selected area on the board
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

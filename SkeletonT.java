package Model;

/**
 * interface that represents a character card
 * character cards give the player a unique, single-use ability
 *
 * @invariant isUsed() is true if the card has been used exactly once.
 */
public interface Charactercard {

    /**
     * activates the unique ability of the character card
     *
     * @param player the player who uses the ability
     * @param board the game board that may be affected
     *

     * @pre !isUsed() (the card must be unused).
     *
     * @post isUsed() is true (the card is now marked as used).
     * @post the game state (player or board) is updated according to the ability rules
     */
    void useAbility(Player player, Board board);

    /**
     * checks if the ability of this card has already been used in the game
     *
     * @return boolean true if used, false otherwise
     *
     * @pre true (no special pre-conditions)
     *
     * @post returns the internal usage status of the card
     */
    boolean isUsed();

    /**
     * returns the name of the character card
     *
     * @return String the name of the card
     *
     * @pre true
     *
     * @post returns a non-null string identifying the card
     */
    String getCardName();
}
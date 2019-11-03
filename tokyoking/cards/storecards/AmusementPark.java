package tokyoking.cards.storecards;
import tokyoking.cards.Card;
import tokyoking.effects.Effect;

/**
 * @author Tom Hammarkvist
 */
public class AmusementPark extends Card{
    /**
     * Creates the Amusement park store card
     */
    Effect fourStars = new Effect();
    public AmusementPark(){
        super("Amusement park", 6, true, null, "+4 stars");
        fourStars.setStars(4);
        super.setEffect(fourStars);
    }
}
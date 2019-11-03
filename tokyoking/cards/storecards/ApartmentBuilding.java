package tokyoking.cards.storecards;

import tokyoking.cards.Card;
import tokyoking.effects.Effect;
/**
 * @author Tom Hammarkvist
 */
public class ApartmentBuilding extends Card{
    /**
     * Creates the Apartment building store card
     */
    protected Effect threeStars = new Effect();
    public ApartmentBuilding(){
        super("Apartment Building", 5, true, null, "+3 stars");
        threeStars.setStars(3);
        super.setEffect(threeStars);
    }
}
package tokyoking.cards.storecards;

import tokyoking.cards.Card;
import tokyoking.effects.Effect;
/**
 * @author Tom Hammarkvist
 */
public class AlphaMonster extends Card{
    /**
     * Creates the Alpha monster store card
     */
    protected Effect starsWhenAttacking = new Effect();
    public AlphaMonster(){
        super("Alpha Monster", 5, false, null, "Gain 1 star when you attack");
        starsWhenAttacking.setStarsWhenAttacking(1);
        super.setEffect(starsWhenAttacking);
    }
}
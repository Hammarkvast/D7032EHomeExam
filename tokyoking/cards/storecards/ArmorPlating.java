package tokyoking.cards.storecards;

import tokyoking.cards.Card;
import tokyoking.effects.Effect;
/**
 * @author Tom Hammarkvist
 */
public class ArmorPlating extends Card{
    /**
     * Creates the Armor plating store card
     */
    protected Effect armor = new Effect();
    public ArmorPlating(){
        super("Armor Plating", 4, false, null, "Ignore damage of 1");
        armor.setArmor(1);
        super.setEffect(armor);
    }
}
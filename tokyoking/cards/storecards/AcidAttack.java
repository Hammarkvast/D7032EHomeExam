package tokyoking.cards.storecards;

import tokyoking.cards.Card;
import tokyoking.effects.Effect;
/**
 * @author Tom Hammarkvist
 */
public class AcidAttack extends Card{
    /**
     * Creates the Acid attack store card
     */
    private Effect moreDamage = new Effect();

    public AcidAttack() {
        super("Acid Attack", 6, false, null, "Deal 1 extra damage each turn");
        moreDamage.setMoreDamage(1);
        super.setEffect(moreDamage);
    }

}
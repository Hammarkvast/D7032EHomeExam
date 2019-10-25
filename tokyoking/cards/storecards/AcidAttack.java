package tokyoking.cards.storecards;

import tokyoking.cards.Card;
import tokyoking.effects.Effect;

public class AcidAttack extends Card{
    private Effect moreDamage = new Effect();

    public AcidAttack() {
        super("Acid Attack", 6, false, null, "Deal 1 extra damage each turn");
        moreDamage.setMoreDamage(1);
        super.setEffect(moreDamage);
    }

}
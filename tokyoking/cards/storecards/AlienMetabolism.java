package tokyoking.cards.storecards;

import tokyoking.cards.Card;
import tokyoking.effects.Effect;

public class AlienMetabolism extends Card{
    protected static Effect cardsCostLess = new Effect();
    public AlienMetabolism(){
        super("Alien Metabolism", 3, false, null, "Buying cards costs you 1 less");
        cardsCostLess.setCardsCostLess(1);
        super.setEffect(cardsCostLess);
    }
}
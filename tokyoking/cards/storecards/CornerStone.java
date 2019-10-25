package tokyoking.cards.storecards;

import tokyoking.cards.Card;
import tokyoking.effects.Effect;

public class CornerStone extends Card{
    Effect oneStar = new Effect();
    public CornerStone(){
        super("Corner Stone", 3, true, null, "+1 star");
        oneStar.setStars(1);
        super.setEffect(oneStar);
    }
}
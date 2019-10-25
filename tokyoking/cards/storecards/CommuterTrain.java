package tokyoking.cards.storecards;

import tokyoking.cards.Card;
import tokyoking.effects.Effect;

public class CommuterTrain extends Card{
    protected Effect twoStars = new Effect();
    public CommuterTrain(){
        super("Commuter Train", 4, true, null, "+2 stars");
        twoStars.setStars(2);
        super.setEffect(twoStars);
    }
}
package tokyoking.cards.storecards;
import tokyoking.cards.Card;
import tokyoking.effects.Effect;
/**
 * @author Tom Hammarkvist
 */
public class Energize extends Card{
    /**
     * Creates the Energize store card
     */
    private Effect nineEnergy = new Effect();
    public Energize(){
        super("Energize", 8, true, null, "+9 energy");
        nineEnergy.setEnergy(9);
        super.setEffect(nineEnergy);
    }
}
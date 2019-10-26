package tokyoking.cards;
import tokyoking.effects.Effect;

public class Card {

    private String name;
    private int cost;
    private boolean discard;
    private Effect effect;
    private String description;

    public Card(String name, int cost, boolean discard, Effect effect, String description) {
        this.name = name;
        this.cost = cost;
        this.discard = discard;
        this.effect = effect;
        this.description = description;
    }       
    public String toString() {
        return name + ", Cost " + cost + ", " + (discard?"DISCARD":"KEEP") + ", Effect " + description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public int getCost() {
        return cost;
    }
    
    public void setCost(int cost) {
        this.cost = cost;
    }

    public boolean getDiscard(){
        return discard;
    }
    
    public void setDiscard(boolean discard) {
        this.discard = discard;
    }
    
    public Effect getEffect() {
        return effect;
    }

    public void setEffect(Effect effect) {
        this.effect = effect;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    
}
package tokyoking.effects;
/**
 * @author Tom Hammarkvist
 */
public class Effect {
    /**
     * Class for all the different card effects.
     */
    private int moreDamage = 0; //Acid Attack
    private int cardsCostLess = 0; //Alien Metabolism
    private int starsWhenAttacking = 0; //Alpha monster
    private int stars = 0; //Apartment Building, Commuter Train, Corner Stone
    private int armor = 0; //Armor Plating
    private int energy = 0;


    public int getMoreDamage() {
        return moreDamage;
    }

    public void setMoreDamage(int moreDamage) {
        this.moreDamage = moreDamage;
    }
 
    public int getCardsCostLess() {
        return cardsCostLess;
    }
 
    public void setCardsCostLess(int cardsCostLess) {
        this.cardsCostLess = cardsCostLess;
    }

    public int getStarsWhenAttacking() {
        return starsWhenAttacking;
    }

    public void setStarsWhenAttacking(int starsWhenAttacking) {
        this.starsWhenAttacking = starsWhenAttacking;
    }

    public int getStars() {
        return stars;
    }

    public void setStars(int stars) {
        this.stars = stars;
    }

    public int getArmor() {
        return armor;
    }

    public void setArmor(int armor) {
        this.armor = armor;
    }


    public int getEnergy() {
        return energy;
    }


    public void setEnergy(int energy) {
        this.energy = energy;
    }
}
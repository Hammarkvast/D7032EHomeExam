package tokyoking.monsters;
import java.io.BufferedReader;
import java.io.DataOutputStream;
//import java.io.InputStreamReader;
//import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import tokyoking.cards.*;
import tokyoking.effects.*;

public class Monster {
    private int maxHealth = 10;
    private int currentHealth = 10;
    private String name;
    private int energy = 0;
    private int stars = 0;
    private boolean inTokyo = false;
    public ArrayList<Card> cards = new ArrayList<Card>();
    public Socket connection = null;
    public BufferedReader inFromClient = null;
    public DataOutputStream outToClient = null;
   
    public Monster(String name) {
        this.name = name;
    }
   
    // search all available cards and return the effect value of an effect
    public int cardEffect(String effectName) {
        for(int i=0; i<cards.size(); i++) {
            try {
                //Find variable by "name"
                 if(Effect.class.getField(effectName).getInt(cards.get(i).effect) > 0) {
                     return Effect.class.getField(effectName).getInt(cards.get(i).effect);
                 }
            } catch (Exception e) {}
        }
        return 0;
    }

    public String cardsToString() {
        String returnString = "";
        if (cards.size()==0)
            return "[NO CARDS]:";
        for(int i=0; i<cards.size(); i++) {returnString += "\t["+i+"] " + cards.get(i) + ":";}
        return returnString;           
    }

    public String getName(){
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getStars(){
        return stars;
    }

    public void setStars(int stars) {
        this.stars = stars;
    }

    public int getCurrentHealth(){
        return currentHealth;
    }


    public void setCurrentHealth(int currentHealth) {
        this.currentHealth = currentHealth;
    }

    public int getEnergy() {
        return energy;
    }

    public void setEnergy(int energy) {
        this.energy = energy;
    }

    public boolean getInTokyo(){
        return inTokyo;
    }

    public void setInTokyo(boolean inTokyo) {
        this.inTokyo = inTokyo;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public void setMaxHealth(int maxHealth) {
        this.maxHealth = maxHealth;
    }

}
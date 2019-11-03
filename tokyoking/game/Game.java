package tokyoking.game;

import tokyoking.monsters.Alienoid;
import tokyoking.monsters.Gigazaur;
import tokyoking.monsters.Kong;
import tokyoking.monsters.Monster;
import tokyoking.SendMessage;
import tokyoking.deck.Deck;
import tokyoking.dice.Dice;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;

/**
 * @author Tom Hammarkvist
 * In this class you will find all the logic for the actual game.
 * 
 */
public class Game{

    private SendMessage message;
    private String rolledDice;
    private String[] reroll;

    public Game(SendMessage msg){
        this.message = msg;
    }
    public void buyWithEnergy(Deck deck, int buyer, Monster monster, ArrayList<Monster>monsterList){
        String msg = "PURCHASE:Do you want to buy any of the cards from the store? (you have " + monster.getEnergy() + " energy) (reset: 3) [-1 to leave store]:" + deck + "\n";
        String answer = message.sendMessage(buyer, msg, monsterList);
        int buy = Integer.parseInt(answer);
        if(buy ==3 && monster.getEnergy() >= 2){
            monster.setEnergy(monster.getEnergy()-2);
            deck.resetStore(deck);
            buyWithEnergy(deck, buyer, monster, monsterList);
        }else if(buy == 3 && monster.getEnergy() < 2){
            System.out.println("Not enough Energy to reset store!");
        }
        else if(buy>=0 && (monster.getEnergy() >= (deck.getStoreCard(buy).getCost() - monster.cardEffect("cardsCostLess")) && buy < 3)) { //Alien Metabolism
            if(deck.getStoreCard(buy).getDiscard()) {
                //7a. Play "DISCARD" cards immediately
                monster.setStars(monster.getStars() + deck.getStoreCard(buy).getEffect().getStars());
                monster.setEnergy(monster.getEnergy() + deck.getStoreCard(buy).getEffect().getEnergy());
            } else
                monster.cards.add(deck.getStoreCard(buy));
            //Deduct the cost of the card from energy
            monster.setEnergy(monster.getEnergy()-(deck.getStoreCard(buy).getCost() - monster.cardEffect("cardsCostLess")));
            //Draw a new card from the deck to replace the card that was bought
            deck.setStoreCard(buy,deck.getDeck().remove(0));
        }        
    }
    
    public boolean checkWinByStars(ArrayList<Monster>monsterList){
        for(int mon=0; mon<monsterList.size(); mon++) {
            if(monsterList.get(mon).getStars() >= 20) {
                for(int victory=0; victory<monsterList.size(); victory++) {
                    message.sendMessage(victory, "Victory: " + monsterList.get(mon).getName() + " has won by stars\n", monsterList);
                    return true;
                }
            }
        }
        return false;
    }

    public boolean checkWinByLoneSurvivor(ArrayList<Monster>monsterList){
        int alive = 0;
        String aliveMonster = "";
        for(int mon=0; mon<monsterList.size(); mon++){
            if(monsterList.get(mon).getCurrentHealth() > 0) {
                alive++; 
                aliveMonster = monsterList.get(mon).getName();
            }
        }
        if(alive==1) {
            for(int victory=0; victory<monsterList.size(); victory++) {
                message.sendMessage(victory, "Victory: " + aliveMonster + " has won by being the only one alive\n", monsterList);
                return true;
            } 
            
        }
        return false; 
    }

    /**
     * Check if any monsters has reached a victory condition, and if thats true, quit the game.
     * @param monsterList
     */

    public void isGameOver(ArrayList<Monster>monsterList){
        if(checkWinByLoneSurvivor(monsterList) || checkWinByStars(monsterList)){
            System.exit(0);
        }
    }
    

    public void wannaLeaveTokyo(int monsterIndex, boolean isMonsterInTokyo, ArrayList<Monster>monsterList){
        String answer = message.sendMessage(monsterIndex, "ATTACKED:You have " + 
        monsterList.get(monsterIndex).getCurrentHealth() + " health left. Do you wish to leave Tokyo? [YES/NO]\n", monsterList);
    if(answer.equalsIgnoreCase("YES")) {
        monsterList.get(monsterIndex).setInTokyo(false);
        isMonsterInTokyo = false;
    }       
    }
    /**
     * Attacks a monster in tokyo if you are outside tokyo.
     * @param monsterList
     * @param monster
     * @param res
     * @param clawDice
     * @param theGame
     * @param monInTokyo
     */
    public void attackInTokyo(ArrayList<Monster>monsterList, Monster monster, HashMap<Dice, Integer> res, Dice clawDice, Game theGame, boolean monInTokyo){
        monInTokyo = false;
        for(int mon=0; mon<monsterList.size(); mon++) {
            if(monsterList.get(mon).getInTokyo()){
                monInTokyo = true;
                int moreDamage = monster.cardEffect("moreDamage"); //Acid Attack
                int totalDamage = res.get(clawDice).intValue()+moreDamage;
                if(totalDamage > monsterList.get(mon).cardEffect("armor")){ //Armor Plating
                    monsterList.get(mon).setCurrentHealth(monsterList.get(mon).getCurrentHealth()-totalDamage);
                }
                // 6e. If you were outside, then the monster inside tokyo may decide to leave Tokyo
                theGame.wannaLeaveTokyo(mon,monInTokyo, monsterList);
            }
        }        
    }


    /**
     * Check if you rolled three dices with the same value of either ONE, TWO or THREE
     * @param res
     * @param monster
     */
    public void threeOfANumber(HashMap<Dice, Integer> res, Monster monster){
        for(int num = 1; num < 4; num++) {
            if(res.containsKey(new Dice(num)))
                if(res.get(new Dice(num)).intValue() >= 3)                      
                    monster.setStars(monster.getStars()+num+(res.get(new Dice(num)).intValue()-3));                 
        }        
    }

    public void attackEveryone(int index, ArrayList<Monster>monsterList, Monster monster, HashMap<Dice, Integer>res, Dice clawDice){
        for(int mon=0; mon<monsterList.size(); mon++) {
            int moreDamage = monster.cardEffect("moreDamage"); //Acid Attack
            int totalDamage = res.get(clawDice).intValue()+moreDamage;
            if(mon!=index && totalDamage > monsterList.get(mon).cardEffect("armor")) { //Armor Plating
                monsterList.get(mon).setCurrentHealth(monsterList.get(mon).getCurrentHealth()-totalDamage);
            }
        }        
    }

    /**
     * If you rolled three or more hearts, check which monster did and give it a evolution powerup.
     * @param monsterList
     * @param monster
     * @param mKong
     * @param mGiga
     * @param mAlien
     * @param index
     */
    public void moreThanThreeHeartsNameChecker(ArrayList<Monster>monsterList, Monster monster,Kong mKong, Gigazaur mGiga,  Alienoid mAlien,int index){
        if(monster.getName().equals("Kong")) {

            mKong.randomizeKongEvo(mKong, index, message, monsterList);
        }
        if(monster.getName().equals("Gigazaur")) {

            mGiga.randomizeGigaEvo(mGiga, index, monsterList, message);    
        }
        if(monster.getName().equals("Alienoid")) {

            mAlien.randomizeAlienEvo(mAlien, index, message, monsterList);
        }
    }

    public void increaseHealth(HashMap<Dice, Integer> res, Dice heartDice, Monster monster){
        if(monster.getCurrentHealth() + res.get(heartDice).intValue() >= monster.getMaxHealth()) {
            monster.setCurrentHealth(monster.getMaxHealth());
        } else {
            monster.setCurrentHealth(monster.getCurrentHealth() + res.get(heartDice).intValue());
        }
    }

    public void sumUp(ArrayList<Dice>dices, ArrayList<Monster>monsterList,HashMap<Dice, Integer>res, int index){
        Collections.sort(dices);
        for(Dice unique : new HashSet<Dice>(dices)) {
             res.put(unique, Collections.frequency(dices, unique));
        }
        message.sendMessage(index, "ROLLED:You rolled " + res + " Press [ENTER]\n", monsterList);
    }

    public void checkAlphaMonster(Monster monster){
        monster.setStars(monster.getStars() + monster.cardEffect("starsWhenAttacking")); //Alpha Monster
    }

    public void increaseEnergy(HashMap<Dice,Integer> res,Dice energyDice,Monster monster){
        monster.setEnergy(monster.getEnergy()+res.get(energyDice).intValue());        
    }

    public ArrayList<Dice> diceRoll(int nrOfDice, Random ran) {
        ArrayList<Dice> dice = new ArrayList<Dice>();
        for(int i=0; i<nrOfDice; i++) {
            dice.add(new Dice(ran.nextInt(6)));
        }
        return dice;
    }

    public void reRoll(ArrayList<Dice> rolleddice, Game theGame, Random ran){
        rolleddice.addAll(theGame.diceRoll(6-rolleddice.size(),ran));
    }

    public void whichDiceToKeep(int index, ArrayList<Dice>diceList, ArrayList<Monster> monsterList) {
        rolledDice = "ROLLED:You rolled:\t[1]\t[2]\t[3]\t[4]\t[5]\t[6]:";
        for(int allDice=0; allDice<diceList.size(); allDice++) {rolledDice+="\t["+diceList.get(allDice)+"]";}
        rolledDice += ":Choose which dice to reroll, separate with comma and in decending order (e.g. 5,4,1   0 to skip)\n";
        reroll = message.sendMessage(index, rolledDice, monsterList).split(",");
        if(Integer.parseInt(reroll[0]) != 0)
            for(int j=0; j<reroll.length; j++) { 
                diceList.remove(Integer.parseInt(reroll[j])-1); 
            }
    }

    public void increaseStars(Monster monster, int amount){
                monster.setStars(monster.getStars()+amount);
            }    

    public String statusUpdate(ArrayList<Monster>monsterList, Monster monster){
        String statusUpdate = "You are " + monster.getName() + " and it is your turn. Here are the stats";
        for(int count=0; count<3; count++) {
            statusUpdate += ":"+monsterList.get(count).getName() + (monsterList.get(count).getInTokyo()?" is in Tokyo ":" is not in Tokyo ");
            statusUpdate += "with " + monsterList.get(count).getCurrentHealth() + " health, " + monsterList.get(count).getStars() + " stars, ";
            statusUpdate += monsterList.get(count).getEnergy() + " energy, and owns the following cards:";
            statusUpdate += monsterList.get(count).cardsToString();
        }
        return statusUpdate;
    }

}
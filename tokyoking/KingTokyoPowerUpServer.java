package tokyoking;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;
import java.util.Scanner;

import tokyoking.cards.Card;
import tokyoking.effects.Effect;
import tokyoking.monsters.Alienoid;
import tokyoking.monsters.Gigazaur;
import tokyoking.monsters.Kong;
import tokyoking.monsters.Monster;
import tokyoking.dice.Dice;
import tokyoking.deck.Deck;

public class KingTokyoPowerUpServer {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        // https://www.youtube.com/watch?v=HqdOaAzPtek
        // https://boardgamegeek.com/thread/1408893/categorizing-cards
        new KingTokyoPowerUpServer();
    }
    
    
    private ArrayList<Monster> monster = new ArrayList<Monster>();
    private Random ran = new Random();
    private Scanner sc = new Scanner(System.in);
    private ArrayList<Dice> dice = new ArrayList<Dice>();
    private Monster currentMonster;
    private String statusUpdate;
    private boolean monsterInTokyo = false;
    private String rolledDice;
    private HashMap<Dice, Integer> result;

    private Dice aHeart = new Dice(Dice.HEART);
    private Dice aClaw = new Dice(Dice.CLAWS);
    private Dice anEnergy = new Dice(Dice.ENERGY);

    public KingTokyoPowerUpServer() {
        Monster kong = new Kong();
        Monster gigazaur = new Gigazaur();
        Monster alien = new Alienoid();
        monster.add(kong);
        monster.add(gigazaur);
        monster.add(alien);
        
        //Shuffle which player is which monster
        Collections.shuffle(monster);
        Deck deck = new Deck();
       
        //Server stuffs
        try {
            ServerSocket aSocket = new ServerSocket(2048);
            //assume two online clients
            for(int onlineClient=0; onlineClient<2; onlineClient++) {
                Socket connectionSocket = aSocket.accept();
                BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
                DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());
                outToClient.writeBytes("You are the monster: " + monster.get(onlineClient).getName() + "\n");
                monster.get(onlineClient).connection = connectionSocket;
                monster.get(onlineClient).inFromClient = inFromClient;
                monster.get(onlineClient).outToClient = outToClient;
                System.out.println("Connected to " + monster.get(onlineClient).getName());
            }
        } catch (Exception e) {}

        //Shuffle the starting order
        Collections.shuffle(monster);
        /*
            Game loop:
            pre: Award a monster in Tokyo 1 star
            1. Roll 6 dice
            2. Decide which dice to keep
            3. Reroll remaining dice
            4. Decide which dice to keep 
            5. Reroll remaining dice
            6. Sum up totals
              6a. Hearts = health (max 10 unless a cord increases it)
              6b. 3 hearts = power-up
              6c. 3 of a number = victory points
              6d. claws = attack (if in Tokyo attack everyone, else attack monster in Tokyo)
              6e. If you were outside, then the monster inside tokyo may decide to leave Tokyo
              6f. energy = energy tokens
            7. Decide to buy things for energy
              7a. Play "DISCARD" cards immediately
            8. Check victory conditions
        */        
        while(true) {
            for(int i=0; i<monster.size(); i++) {
                currentMonster = monster.get(i);
                if(currentMonster.getCurrentHealth() == 0) {
                    currentMonster.setInTokyo(false);
                    continue;
                }
                // pre: Award a monster in Tokyo 1 star
                if(currentMonster.getInTokyo()){
                    increaseStars(currentMonster);
                }
                statusUpdate = statusUpdate();
                sendMessage(i, statusUpdate+"\n");
                // 1. Roll 6 dice
                //ArrayList<Dice> dice = new ArrayList<Dice>();
                dice = diceRoll(6);
                // 2. Decide which dice to keep
                rolledDice = "ROLLED:You rolled:\t[1]\t[2]\t[3]\t[4]\t[5]\t[6]:";
                for(int allDice=0; allDice<dice.size(); allDice++) {rolledDice+="\t["+dice.get(allDice)+"]";}
                rolledDice += ":Choose which dice to reroll, separate with comma and in decending order (e.g. 5,4,1   0 to skip)\n";
                String[] reroll = sendMessage(i, rolledDice).split(",");
                if(Integer.parseInt(reroll[0]) != 0)
                    for(int j=0; j<reroll.length; j++) { dice.remove(Integer.parseInt(reroll[j])-1); }
                // 3. Reroll remaining dice
                dice.addAll(diceRoll(6-dice.size()));
                // 4. Decide which dice to keep
                rolledDice = "ROLLED:You rolled:\t[1]\t[2]\t[3]\t[4]\t[5]\t[6]:";
                for(int allDice=0; allDice<dice.size(); allDice++) {rolledDice+="\t["+dice.get(allDice)+"]";}
                rolledDice += ":Choose which dice to reroll, separate with comma and in decending order (e.g. 5,4,1   0 to skip)\n";
                reroll = sendMessage(i, rolledDice).split(",");
                if(Integer.parseInt(reroll[0]) != 0)
                    for(int j=0; j<reroll.length; j++) { dice.remove(Integer.parseInt(reroll[j])-1); }
                // 5. Reroll remaining dice
                dice.addAll(diceRoll(6-dice.size()));
                // 6. Sum up totals
                Collections.sort(dice);
                result = new HashMap<Dice, Integer>();
                for(Dice unique : new HashSet<Dice>(dice)) {
                     result.put(unique, Collections.frequency(dice, unique));
                }
                String ok = sendMessage(i, "ROLLED:You rolled " + result + " Press [ENTER]\n");
                // 6a. Hearts = health (max 10 unless a cord increases it)
                //Dice aHeart = new Dice(Dice.HEART);
                if(result.containsKey(aHeart)) { //+1 currentHealth per heart, up to maxHealth
                    if(currentMonster.getCurrentHealth() + result.get(aHeart).intValue() >= currentMonster.getMaxHealth()) {
                        currentMonster.setCurrentHealth(currentMonster.getMaxHealth());
                    } else {
//                        currentMonster.currentHealth += result.get(aHeart).intValue();
                        currentMonster.setCurrentHealth(currentMonster.getCurrentHealth() + result.get(aHeart).intValue());
                    }
                    // 6b. 3 hearts = power-up
                    if(result.get(aHeart).intValue() >= 3) {
                        // Deal a power-up card to the currentMonster
                        if(currentMonster.getName().equals("Kong")) {
                            //Todo: Add support for more cards.
                            //Current support is only for the Red Dawn card
                            //Add support for keeping it secret until played
                            redDawn(i);
                        }
                        if(currentMonster.getName().equals("Gigazaur")) {
                            //Todo: Add support for more cards.
                            //Current support is only for the Radioactive Waste
                            //Add support for keeping it secret until played
                            radioactiveWaste(i);    
                        }
                        if(currentMonster.getName().equals("Alienoid")) {
                            //Todo: Add support for more cards.
                            //Current support is only for the Alien Scourge
                            //Add support for keeping it secret until played
                            theAlienScourge(i);
                        }
                    }
                }
                // 6c. 3 of a number = victory points
                for(int num = 1; num < 4; num++) {
                    if(result.containsKey(new Dice(num)))
                        if(result.get(new Dice(num)).intValue() >= 3)
                            currentMonster.setStars(currentMonster.getStars()+num+(result.get(new Dice(num)).intValue()-3));                 
                }
                // 6d. claws = attack (if in Tokyo attack everyone, else attack monster in Tokyo)
                //Dice aClaw = new Dice(Dice.CLAWS);
                if(result.containsKey(aClaw)) {
                //    currentMonster.stars += currentMonster.cardEffect("starsWhenAttacking"); //Alpha Monster
                    currentMonster.setStars(currentMonster.getStars() + currentMonster.cardEffect("starsWhenAttacking")); //Alpha Monster
                    if(currentMonster.getInTokyo()) {
                        for(int mon=0; mon<monster.size(); mon++) {
                            int moreDamage = currentMonster.cardEffect("moreDamage"); //Acid Attack
                            int totalDamage = result.get(aClaw).intValue()+moreDamage;
                            if(mon!=i && totalDamage > monster.get(mon).cardEffect("armor")) { //Armor Plating
                               // monster.get(mon).currentHealth+=-totalDamage;
                                monster.get(mon).setCurrentHealth(monster.get(mon).getCurrentHealth()-totalDamage);
                            }
                        }
                    }
                    else {
                        for(int mon=0; mon<monster.size(); mon++) {
                            if(monster.get(mon).getInTokyo()){
                                System.out.println("hej");
                                monsterInTokyo = true;
                                int moreDamage = currentMonster.cardEffect("moreDamage"); //Acid Attack
                                int totalDamage = result.get(aClaw).intValue()+moreDamage;
                                if(totalDamage > monster.get(mon).cardEffect("armor")) //Armor Plating
                                    //monster.get(mon).currentHealth+=-totalDamage;
                                    monster.get(mon).setCurrentHealth(monster.get(mon).getCurrentHealth()-totalDamage);
                                // 6e. If you were outside, then the monster inside tokyo may decide to leave Tokyo
                                String answer = sendMessage(mon, "ATTACKED:You have " + 
                                    //monster.get(mon).currentHealth + " health left. Do you wish to leave Tokyo? [YES/NO]\n");
                                    monster.get(mon).getCurrentHealth() + " health left. Do you wish to leave Tokyo? [YES/NO]\n");
                                if(answer.equalsIgnoreCase("YES")) {
                                    monster.get(mon).setInTokyo(false);
                                    monsterInTokyo = false;
                                }
                            }
                        }
                        if(!monsterInTokyo) {
                            System.out.println("you get a star!");
                            currentMonster.setInTokyo(true);
                            increaseStars(currentMonster);
                        }
                    }
                }
                // 6f. energy = energy tokens
                //Dice anEnergy = new Dice(Dice.ENERGY);
                if(result.containsKey(anEnergy))
                    //currentMonster.energy += result.get(anEnergy).intValue();
                    currentMonster.setEnergy(currentMonster.getEnergy()+result.get(anEnergy).intValue());
                // 7. Decide to buy things for energy
                String msg = "PURCHASE:Do you want to buy any of the cards from the store? (you have " + currentMonster.getEnergy() + " energy) [#/-1]:" + deck + "\n";
                String answer = sendMessage(i, msg);
                int buy = Integer.parseInt(answer);
                if(buy>=0 && (currentMonster.getEnergy() >= (deck.getCard(buy).getCost() - currentMonster.cardEffect("cardsCostLess")))) { //Alien Metabolism
                    if(deck.getCard(buy).getDiscard()) {
                        //7a. Play "DISCARD" cards immediately
                        //currentMonster.stars += deck.store[buy].effect.stars;
                        currentMonster.setStars(currentMonster.getStars() + deck.getCard(buy).getEffect().getStars());
                    } else
                        currentMonster.cards.add(deck.getCard(buy));
                    //Deduct the cost of the card from energy
                   // currentMonster.energy += -(deck.store[buy].cost-currentMonster.cardEffect("cardsCostLess")); //Alient Metabolism
                    currentMonster.setEnergy(currentMonster.getEnergy()-(deck.getCard(buy).getCost() - currentMonster.cardEffect("cardsCostLess")));
                    //Draw a new card from the deck to replace the card that was bought
                    deck.setCard(buy,deck.getDeck().remove(0));
                }
                //8. Check victory conditions
                int alive=0; String aliveMonster = "";
                for(int mon=0; mon<monster.size(); mon++) {
                    if(monster.get(mon).getStars() >= 20) {
                        for(int victory=0; victory<monster.size(); victory++) {
                            String victoryByStars = sendMessage(victory, "Victory: " + monster.get(mon).getName() + " has won by stars\n");
                        }
                        System.exit(0);
                    }
                    if(monster.get(mon).getCurrentHealth() > 0) {
                        alive++; aliveMonster = monster.get(mon).getName();
                    }
                }
                if(alive==1) {
                    for(int victory=0; victory<monster.size(); victory++) {
                        String victoryByKills = sendMessage(victory, "Victory: " + aliveMonster + " has won by being the only one alive\n");
                    } 
                    System.exit(0);
                }
            }
        }      
    }
    
    private String sendMessage(int recipient, String message) {
        Monster aMonster = monster.get(recipient);
        String response = "";
        if(aMonster.connection != null) {
            try {
                aMonster.outToClient.writeBytes(message);
                response = aMonster.inFromClient.readLine();
            } catch (Exception e) {}
        } else {
            String[] theMessage = message.split(":");
            for(int i=0; i<theMessage.length; i++) {System.out.println(theMessage[i].toString());}
            if(!(theMessage[0].equals("ATTACKED") || theMessage[0].equals("ROLLED") || theMessage[0].equals("PURCHASE")))
                System.out.println("Press [ENTER]");
            response = sc.nextLine();
        }
        return response;
    }
    
    private ArrayList<Dice> diceRoll(int nrOfDice) {
        ArrayList<Dice> dice = new ArrayList<Dice>();
        for(int i=0; i<nrOfDice; i++) {
            dice.add(new Dice(ran.nextInt(6)));
        }
        return dice;
    }
    
    private void redDawn(int index){
        String power = sendMessage(index, "POWERUP:Deal 2 damage to all others\n");
        for(int mon=0; mon<monster.size(); mon++) {
            if(mon!=index) {
                monster.get(mon).setCurrentHealth(monster.get(mon).getCurrentHealth()-2);
            }
        }        
    }

    private void theAlienScourge(int index){
        sendMessage(index, "POWERUP:Receive 2 stars\n");
        currentMonster.setStars(currentMonster.getStars()+2);        
    }

    private void radioactiveWaste(int index){
        sendMessage(index, "POWERUP:Receive 2 energy and 1 health\n");
        currentMonster.setEnergy(currentMonster.getEnergy() + 2);
        if(currentMonster.getCurrentHealth() + 1 >= currentMonster.getMaxHealth()) {
            currentMonster.setCurrentHealth(currentMonster.getMaxHealth());
        } else {
            currentMonster.setCurrentHealth(currentMonster.getCurrentHealth()+1);
        }          
    }

    private void increaseStars(Monster monster){
//    monster.stars +=1;
        monster.setStars(monster.getStars()+1);
    }

    private String statusUpdate(){
        String statusUpdate = "You are " + currentMonster.getName() + " and it is your turn. Here are the stats";
        for(int count=0; count<3; count++) {
            statusUpdate += ":"+monster.get(count).getName() + (monster.get(count).getInTokyo()?" is in Tokyo ":" is not in Tokyo ");
            statusUpdate += "with " + monster.get(count).getCurrentHealth() + " health, " + monster.get(count).getStars() + " stars, ";
            statusUpdate += monster.get(count).getEnergy() + " energy, and owns the following cards:";
            statusUpdate += monster.get(count).cardsToString();
        }
        return statusUpdate;
    }
}

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

import tokyoking.monsters.Alienoid;
import tokyoking.monsters.Gigazaur;
import tokyoking.monsters.Kong;
import tokyoking.monsters.Monster;
import tokyoking.dice.Dice;
import tokyoking.deck.Deck;
import tokyoking.cards.Card;
import tokyoking.game.Game;
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
    private boolean monsterInTokyo = false;
    private String rolledDice;
    private String[] reroll;
    private HashMap<Dice, Integer> result;

    private Dice aHeart = new Dice(Dice.HEART);
    private Dice aClaw = new Dice(Dice.CLAWS);
    private Dice anEnergy = new Dice(Dice.ENERGY);

    public KingTokyoPowerUpServer() {
        SendMessage msg = new SendMessage();
        Game game = new Game();
        Kong kong = new Kong();
        Gigazaur gigazaur = new Gigazaur();
        Alienoid alien = new Alienoid();
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
                    game.increaseStars(currentMonster, 1);
                }
                //statusUpdate = statusUpdate();
                msg.sendMessage(i, game.statusUpdate(monster, currentMonster)+"\n", monster);
                // 1. Roll 6 dice
                //ArrayList<Dice> dice = new ArrayList<Dice>();
                dice = game.diceRoll(6, ran);
                // 2. Decide which dice to keep
                game.whichDiceToKeep(i,dice,monster);
                // 3. Reroll remaining dice
                game.reRoll(dice, game, ran);
                //dice.addAll(diceRoll(6-dice.size()));
                // 4. Decide which dice to keep
                game.whichDiceToKeep(i,dice,monster);
                // 5. Reroll remaining dice
                //dice.addAll(diceRoll(6-dice.size()));
                game.reRoll(dice, game, ran);
                // 6. Sum up totals
                //game.sumUp(dice, monster, result, i);
                //Collections.sort(dice);
                result = new HashMap<Dice, Integer>();
                game.sumUp(dice, monster, result, i);
                // for(Dice unique : new HashSet<Dice>(dice)) {
                //      result.put(unique, Collections.frequency(dice, unique));
                // }
                // sendMessage(i, "ROLLED:You rolled " + result + " Press [ENTER]\n");
                // 6a. Hearts = health (max 10 unless a cord increases it)
                if(result.containsKey(aHeart)) { //+1 currentHealth per heart, up to maxHealth
                    game.increaseHealth(result, aHeart, currentMonster);
                    // 6b. 3 hearts = power-up
                    if(result.get(aHeart).intValue() >= 3) {
                        // Deal a power-up card to the currentMonster
                        if(currentMonster.getName().equals("Kong")) {
                            //Todo: Add support for more cards.
                            //Current support is only for the Red Dawn card
                            //Add support for keeping it secret until played
                            //redDawn(i);
                            kong.randomizeKongEvo(kong, i, msg, monster);
                        }
                        if(currentMonster.getName().equals("Gigazaur")) {
                            //Todo: Add support for more cards.
                            //Current support is only for the Radioactive Waste
                            //Add support for keeping it secret until played
                            //radioactiveWaste(i);
                            gigazaur.randomizeGigaEvo(gigazaur, i, monster, msg);    
                        }
                        if(currentMonster.getName().equals("Alienoid")) {
                            //Todo: Add support for more cards.
                            //Current support is only for the Alien Scourge
                            //Add support for keeping it secret until played
                            //theAlienScourge(i);
                            alien.randomizeAlienEvo(alien, i, msg, monster);
                        }
                    }
                }
                // 6c. 3 of a number = victory points
                game.threeOfANumber(result, currentMonster);
                // 6d. claws = attack (if in Tokyo attack everyone, else attack monster in Tokyo)
                if(result.containsKey(aClaw)) {
                    game.checkAlphaMonster(currentMonster);
                    if(currentMonster.getInTokyo()) {
                        game.attackEveryone(i, monster, currentMonster,result, aClaw);
                    }
                    else {
                        game.attackInTokyo(monster, currentMonster,result, aClaw, game, monsterInTokyo);
                        if(!monsterInTokyo) {
                            currentMonster.setInTokyo(true);;
                            game.increaseStars(currentMonster,1);
                        }
                    }
                }
                // 6f. energy = energy tokens
                //Dice anEnergy = new Dice(Dice.ENERGY);
                if(result.containsKey(anEnergy))
                    //currentMonster.energy += result.get(anEnergy).intValue();
                    game.increaseEnergy(result, anEnergy, currentMonster);
                // 7. Decide to buy things for energy
                game.buyWithEnergy(deck, i, currentMonster, monster);
                //8. Check victory conditions
                game.checkVictory(monster, currentMonster);
            }
        }      
    }
    


}

package tokyoking;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;

import tokyoking.monsters.Alienoid;
import tokyoking.monsters.Gigazaur;
import tokyoking.monsters.Kong;
import tokyoking.monsters.Monster;
import tokyoking.dice.Dice;
import tokyoking.deck.Deck;
import tokyoking.game.Game;

/**
 * @author Tom Hammarkvist
 */
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
    
    
    private ArrayList<Monster> players = new ArrayList<Monster>();
    private Random ran = new Random();
    private ArrayList<Dice> dice = new ArrayList<Dice>();
    private Monster currentPlayer;
    private boolean monsterInTokyo = false;
    private HashMap<Dice, Integer> result;

    private Dice aHeart = new Dice(Dice.HEART);
    private Dice aClaw = new Dice(Dice.CLAWS);
    private Dice anEnergy = new Dice(Dice.ENERGY);

    /**
     * The constructor now only runs the game with the functions from the Game class.
     */

    public KingTokyoPowerUpServer() {
        SendMessage msg = new SendMessage();
        Game game = new Game(msg);
        Kong kong = new Kong();
        Gigazaur gigazaur = new Gigazaur();
        Alienoid alien = new Alienoid();
        players.add(kong);
        players.add(gigazaur);
        players.add(alien);
        
        //Shuffle which player is which monster
        Collections.shuffle(players);
        Deck deck = new Deck();
       
        //Server stuffs
        try {
            ServerSocket aSocket = new ServerSocket(2048);
            //assume two online clients
            for(int onlineClient=0; onlineClient<2; onlineClient++) {
                Socket connectionSocket = aSocket.accept();
                BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
                DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());
                outToClient.writeBytes("You are the monster: " + players.get(onlineClient).getName() + "\n");
                players.get(onlineClient).connection = connectionSocket;
                players.get(onlineClient).inFromClient = inFromClient;
                players.get(onlineClient).outToClient = outToClient;
                System.out.println("Connected to " + players.get(onlineClient).getName());
            }
        } catch (Exception e) {}

        //Shuffle the starting order 
        Collections.shuffle(players);
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
            for(int i=0; i<players.size(); i++) {
                currentPlayer = players.get(i);
                if(currentPlayer.getCurrentHealth() == 0) {
                    currentPlayer.setInTokyo(false);
                    continue;
                }
                // pre: Award a monster in Tokyo 1 star
                if(currentPlayer.getInTokyo()){
                    game.increaseStars(currentPlayer, 1);
                }
                msg.sendMessage(i, game.statusUpdate(players, currentPlayer)+"\n", players);
                // 1. Roll 6 dice
                dice = game.diceRoll(6, ran);
                // 2. Decide which dice to keep
                game.whichDiceToKeep(i,dice,players);
                // 3. Reroll remaining dice
                game.reRoll(dice, game, ran);
                // 4. Decide which dice to keep
                game.whichDiceToKeep(i,dice,players);
                // 5. Reroll remaining dice
                game.reRoll(dice, game, ran);
                // 6. Sum up totals
                result = new HashMap<Dice, Integer>();
                game.sumUp(dice, players, result, i);

                // 6a. Hearts = health (max 10 unless a cord increases it)
                if(result.containsKey(aHeart) && !currentPlayer.getInTokyo()) { //+1 currentHealth per heart, up to maxHealth
                    game.increaseHealth(result, aHeart, currentPlayer);

                    // 6b. 3 hearts = power-up
                    if(result.get(aHeart).intValue() >= 3) {
                        game.moreThanThreeHeartsNameChecker(players, currentPlayer, kong, gigazaur, alien, i);
                    }
                // if the current player is in tokyo, hearts only gives you powerup, no health.
                }else if(result.containsKey(aHeart) && currentPlayer.getInTokyo() && result.get(aHeart).intValue() >= 3){
                    game.moreThanThreeHeartsNameChecker(players, currentPlayer, kong, gigazaur, alien, i);
                }
                // 6c. 3 of a number, increases stars by either one two or three
                game.threeOfANumber(result, currentPlayer);
                // 6d. claws = attack (if in Tokyo attack everyone, else attack monster in Tokyo)
                if(result.containsKey(aClaw)) {
                    game.checkAlphaMonster(currentPlayer);
                    if(currentPlayer.getInTokyo()) {
                        game.attackEveryone(i, players, currentPlayer,result, aClaw);
                    }
                    else {
                        game.attackInTokyo(players, currentPlayer,result, aClaw, game, monsterInTokyo);
                        if(!monsterInTokyo) {
                            currentPlayer.setInTokyo(true);
                            game.increaseStars(currentPlayer,1);
                        }
                    }
                }
                // 6f. energy = energy tokens
                if(result.containsKey(anEnergy))
                    game.increaseEnergy(result, anEnergy, currentPlayer);
                // 7. Decide to buy things for energy
                game.buyWithEnergy(deck, i, currentPlayer, players);
                //8. Check victory conditions
                game.isGameOver(players);
            }
        }      
    }
    


}

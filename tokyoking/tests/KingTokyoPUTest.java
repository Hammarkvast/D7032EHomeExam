package tokyoking.tests;

import static org.junit.Assert.*;

import tokyoking.KingTokyoPowerUpServer;
import tokyoking.monsters.Alienoid;
import tokyoking.monsters.Gigazaur;
import tokyoking.monsters.Kong;
import tokyoking.monsters.Monster;
import tokyoking.game.Game;
import tokyoking.dice.Dice;
import tokyoking.tests.SendMessageTest;
import tokyoking.cards.Card;
import tokyoking.cards.storecards.*;
import tokyoking.deck.Deck;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;
import java.util.HashMap;
import java.util.HashSet;
import java.beans.Transient;
import org.junit.Test;


public class KingTokyoPUTest{
    //KingTokyoPowerUpServer server = new KingTokyoPowerUpServer();
    SendMessageTest message = new SendMessageTest("test");
    Game gameTest = new Game(message);
    String[]reroll;
    Kong kong = new Kong();
    Alienoid alien = new Alienoid();
    Gigazaur gigazaur = new Gigazaur();
    Random ran = new Random();
    private HashMap<Dice, Integer> result;
    ArrayList<Dice> Die = new ArrayList<Dice>();
    private boolean monsterInTokyo = false;



    // @Test
    // public void testEachPlayersAssignedMonster(){

    // }


    //Test of requirement 2
    @Test
    public void testVictoryPointsSetZero(){
        assertEquals(0, kong.getStars());
    }

    //Test requirement 3
    @Test
    public void testStartingHP(){
        assertEquals(10, kong.getCurrentHealth());
    }


    //Testing requirement 7
    @Test
    public void testMonsterInsideTokyoGainStar(){
        if(!monsterInTokyo) {
            kong.setInTokyo(true);
            monsterInTokyo = true;
            gameTest.increaseStars(kong,1);
        }
        assertEquals(1, kong.getStars());       
    }
    
    //Testing requirement 8
    @Test
    public void testRollSixDie(){
       Die = gameTest.diceRoll(6, ran);
       int amountOfDices = Die.size();
       assertEquals(6, amountOfDices);
    }

    //Testing requirement 12.1
    @Test
    public void testTripleOnes(){
        Dice dice1 = new Dice(1);
        Dice dice2 = new Dice(1);
        Dice dice3 = new Dice(1);
        Dice dice4 = new Dice(2);
        Dice dice5 = new Dice(3);
        Dice dice6 = new Dice(3);
        ArrayList<Dice> dices = new ArrayList<>(Arrays.asList(dice1,dice2,dice3,dice4,dice5,dice6));
        result = new HashMap<Dice, Integer>();
        for(Dice unique : new HashSet<Dice>(dices)) {
            result.put(unique, Collections.frequency(dices, unique));
       }
       gameTest.threeOfANumber(result, kong);
       assertEquals(1, kong.getStars());
    }
   //Testing requirement 12.2
    @Test
    public void testTripleTwos(){
        Dice dice1 = new Dice(1);
        Dice dice2 = new Dice(2);
        Dice dice3 = new Dice(2);
        Dice dice4 = new Dice(2);
        Dice dice5 = new Dice(3);
        Dice dice6 = new Dice(3);
        ArrayList<Dice> dices = new ArrayList<>(Arrays.asList(dice1,dice2,dice3,dice4,dice5,dice6));
        result = new HashMap<Dice, Integer>();
        for(Dice unique : new HashSet<Dice>(dices)) {
            result.put(unique, Collections.frequency(dices, unique));
       }
       gameTest.threeOfANumber(result, kong);
       assertEquals(2, kong.getStars());
    }
    //Testing requirement 12.3
    @Test
    public void testTripleThrees(){
        Dice dice1 = new Dice(1);
        Dice dice2 = new Dice(2);
        Dice dice3 = new Dice(2);
        Dice dice4 = new Dice(3);
        Dice dice5 = new Dice(3);
        Dice dice6 = new Dice(3);
        ArrayList<Dice> dices = new ArrayList<>(Arrays.asList(dice1,dice2,dice3,dice4,dice5,dice6));
        result = new HashMap<Dice, Integer>();
        for(Dice unique : new HashSet<Dice>(dices)) {
            result.put(unique, Collections.frequency(dices, unique));
       }
       gameTest.threeOfANumber(result, kong);
       assertEquals(3, kong.getStars());
    }
    //Testing requirement 12.4
    @Test
    public void testEachEnergyOneEnergy(){
        Dice anEnergy = new Dice(Dice.ENERGY);
        Dice dice1 = new Dice(1);
        Dice dice2 = new Dice(2);
        Dice dice3 = new Dice(4);
        Dice dice4 = new Dice(3);
        Dice dice5 = new Dice(3);
        Dice dice6 = new Dice(3);
        ArrayList<Dice> dices = new ArrayList<>(Arrays.asList(dice1,dice2,dice3,dice4,dice5,dice6));
        result = new HashMap<Dice, Integer>();
        for(Dice unique : new HashSet<Dice>(dices)) {
            result.put(unique, Collections.frequency(dices, unique));
       }
        if(result.containsKey(anEnergy)){
            gameTest.increaseEnergy(result, anEnergy, kong);
        }
        assertEquals(1, kong.getEnergy());

    }

    //Testing requirement 12.5.i
    @Test
    public void testInsideTokyoNoHealth(){
        kong.setCurrentHealth(8);
        kong.setInTokyo(true);
        Dice aHeart = new Dice(Dice.HEART);
        Dice dice1 = new Dice(1);
        Dice dice2 = new Dice(0);
        Dice dice3 = new Dice(4);
        Dice dice4 = new Dice(3);
        Dice dice5 = new Dice(3);
        Dice dice6 = new Dice(3);
        ArrayList<Dice> dices = new ArrayList<>(Arrays.asList(dice1,dice2,dice3,dice4,dice5,dice6));
        result = new HashMap<Dice, Integer>();
        for(Dice unique : new HashSet<Dice>(dices)) {
            result.put(unique, Collections.frequency(dices, unique));
       }
        if(result.containsKey(aHeart) && !kong.getInTokyo()) { //+1 currentHealth per heart, up to maxHealth
            gameTest.increaseHealth(result, aHeart, kong);

            // 6b. 3 hearts = power-up
            if(result.get(aHeart).intValue() >= 3) {
                //game.moreThanThreeHeartsNameChecker(monster, currentMonster, kong, gigazaur, alien, i);
            }
        }else if(result.containsKey(aHeart) && kong.getInTokyo() && result.get(aHeart).intValue() >= 3){
            //game.moreThanThreeHeartsNameChecker(monster, currentMonster, kong, gigazaur, alien, i);
        }
        assertEquals(8, kong.getCurrentHealth());
    }

    //Testing requirement 12.5.ii
    @Test
    public void testOutsideTokyoIncreaseHealth(){
        kong.setCurrentHealth(8);
        kong.setInTokyo(false);
        Dice aHeart = new Dice(Dice.HEART);
        Dice dice1 = new Dice(1);
        Dice dice2 = new Dice(0);
        Dice dice3 = new Dice(4);
        Dice dice4 = new Dice(3);
        Dice dice5 = new Dice(3);
        Dice dice6 = new Dice(3);
        ArrayList<Dice> dices = new ArrayList<>(Arrays.asList(dice1,dice2,dice3,dice4,dice5,dice6));
        result = new HashMap<Dice, Integer>();
        for(Dice unique : new HashSet<Dice>(dices)) {
            result.put(unique, Collections.frequency(dices, unique));
       }
        if(result.containsKey(aHeart) && !kong.getInTokyo()) { //+1 currentHealth per heart, up to maxHealth
            gameTest.increaseHealth(result, aHeart, kong);

            // 6b. 3 hearts = power-up
            if(result.get(aHeart).intValue() >= 3) {
                //game.moreThanThreeHeartsNameChecker(monster, currentMonster, kong, gigazaur, alien, i);
            }
        }else if(result.containsKey(aHeart) && kong.getInTokyo() && result.get(aHeart).intValue() >= 3){
            //game.moreThanThreeHeartsNameChecker(monster, currentMonster, kong, gigazaur, alien, i);
        }
        assertEquals(9, kong.getCurrentHealth());
    }  

    //Testing requirement 12.6
    @Test
    public void testThreeHeartsGivesAlienScourge(){
        Alienoid monster = new Alienoid();
        int index = 0;
        ArrayList <Monster>monsterList = new ArrayList<>();
        if(monster.getName().equals("Kong")) {
            //Todo: Add support for more cards.
            //Current support is only for the Red Dawn card
            //Add support for keeping it secret until played
            //redDawn(i);
            //monster.randomizeKongEvo(mKong, index, message, monsterList);
        }
        if(monster.getName().equals("Gigazaur")) {
            //Todo: Add support for more cards.
            //Current support is only for the Radioactive Waste
            //Add support for keeping it secret until played
            //radioactiveWaste(i);
            //mGiga.randomizeGigaEvo(mGiga, index, monsterList, message);    
        }
        if(monster.getName().equals("Alienoid")) {
            //Todo: Add support for more cards.
            //Current support is only for the Alien Scourge
            //Add support for keeping it secret until played
            //theAlienScourge(i);
            monster.randomizeAlienEvo(monster, index, message, monsterList);
          
        }
        assertEquals(2, monster.getStars());
    }
    //Test requirement 12.7.i
    @Test
    public void testEachClawOneDmgOutsideTokyo(){
        Dice aClaw = new Dice(Dice.CLAWS);
        Dice dice1 = new Dice(1);
        Dice dice2 = new Dice(5);
        Dice dice3 = new Dice(1);
        Dice dice4 = new Dice(2);
        Dice dice5 = new Dice(3);
        Dice dice6 = new Dice(3);
        ArrayList<Dice> dices = new ArrayList<>(Arrays.asList(dice1,dice2,dice3,dice4,dice5,dice6));
        Alienoid alien = new Alienoid();
        Gigazaur giga = new Gigazaur();
        
        result = new HashMap<Dice, Integer>();
        for(Dice unique : new HashSet<Dice>(dices)) {
            result.put(unique, Collections.frequency(dices, unique));
       }
       if(result.containsKey(aClaw)) {
        //game.checkAlphaMonster(currentMonster);
        int index = 0;
        kong.setInTokyo(true);
        //System.out.println("hallådär");
        ArrayList<Monster>monsterList = new ArrayList<>(Arrays.asList(kong,alien,giga));
        if(kong.getInTokyo()) {
            gameTest.attackEveryone(index, monsterList, kong,result, aClaw);
        }
        else {
            //gameTest.attackInTokyo(monster, currentMonster,result, aClaw, game, monsterInTokyo);
            if(!monsterInTokyo) {
                //currentMonster.setInTokyo(true);;
                gameTest.increaseStars(kong,1);
            }
        }
    }
    assertEquals(9, alien.getCurrentHealth());
    }
   //Testing 12.7.ii.1
    @Test
    public void testEachClawInTokyoGainStar(){
        Dice aClaw = new Dice(Dice.CLAWS);
        Dice dice1 = new Dice(1);
        Dice dice2 = new Dice(5);
        Dice dice3 = new Dice(1);
        Dice dice4 = new Dice(2);
        Dice dice5 = new Dice(3);
        Dice dice6 = new Dice(3);
        ArrayList<Dice> dices = new ArrayList<>(Arrays.asList(dice1,dice2,dice3,dice4,dice5,dice6));
        Alienoid alien = new Alienoid();
        Gigazaur giga = new Gigazaur();
        
        result = new HashMap<Dice, Integer>();
        for(Dice unique : new HashSet<Dice>(dices)) {
            result.put(unique, Collections.frequency(dices, unique));
       }
       if(result.containsKey(aClaw)) {
        //game.checkAlphaMonster(currentMonster);
        int index = 0;
        //System.out.println("hallådär");
        ArrayList<Monster>monsterList = new ArrayList<>(Arrays.asList(kong,alien,giga));
        if(kong.getInTokyo()) {
            gameTest.attackEveryone(index, monsterList, kong,result, aClaw);
        }
        else {
            //gameTest.attackInTokyo(monster, currentMonster,result, aClaw, game, monsterInTokyo);
            if(!monsterInTokyo) {
                //currentMonster.setInTokyo(true);;
                gameTest.increaseStars(kong,1);
            }
        }
    }
    assertEquals(1, kong.getStars());        
    }
    //Testing 12.7.ii.2.a
    @Test
    public void testDealDamageInsideTokyo(){
        Dice aClaw = new Dice(Dice.CLAWS);
        Dice dice1 = new Dice(1);
        Dice dice2 = new Dice(5);
        Dice dice3 = new Dice(1);
        Dice dice4 = new Dice(2);
        Dice dice5 = new Dice(3);
        Dice dice6 = new Dice(3);
        ArrayList<Dice> dices = new ArrayList<>(Arrays.asList(dice1,dice2,dice3,dice4,dice5,dice6));
        Alienoid alien = new Alienoid();
        Gigazaur giga = new Gigazaur();
        
        result = new HashMap<Dice, Integer>();
        for(Dice unique : new HashSet<Dice>(dices)) {
            result.put(unique, Collections.frequency(dices, unique));
       }
       if(result.containsKey(aClaw)) {
        //game.checkAlphaMonster(currentMonster);
        int index = 0;
        //System.out.println("hallådär");
        kong.setInTokyo(false);
        alien.setInTokyo(true);
        ArrayList<Monster>monsterList = new ArrayList<>(Arrays.asList(kong,alien,giga));
        if(kong.getInTokyo()) {
            gameTest.attackEveryone(index, monsterList, kong,result, aClaw);
        }
        else {
            gameTest.attackInTokyo(monsterList, kong,result, aClaw, gameTest, monsterInTokyo);
            if(!monsterInTokyo) {
                kong.setInTokyo(true);;
                gameTest.increaseStars(kong,1);
            }
        }
    }
    assertEquals(9, alien.getCurrentHealth());        
    }
    //Testing 12.7.ii.2.b
    @Test
    public void testMayLeaveTokyo(){
        SendMessageTest sm = new SendMessageTest("YES");
        Game gameNewTest = new Game(sm);
        Dice aClaw = new Dice(Dice.CLAWS);
        Dice dice1 = new Dice(1);
        Dice dice2 = new Dice(5);
        Dice dice3 = new Dice(1);
        Dice dice4 = new Dice(2);
        Dice dice5 = new Dice(3);
        Dice dice6 = new Dice(3);
        ArrayList<Dice> dices = new ArrayList<>(Arrays.asList(dice1,dice2,dice3,dice4,dice5,dice6));
        Alienoid alien = new Alienoid();
        Gigazaur giga = new Gigazaur();
        ArrayList<Monster>monsterList = new ArrayList<>(Arrays.asList(kong,alien,giga));
        alien.setInTokyo(true);
        
        result = new HashMap<Dice, Integer>();
        for(Dice unique : new HashSet<Dice>(dices)) {
            result.put(unique, Collections.frequency(dices, unique));
       }
    gameNewTest.attackInTokyo(monsterList, kong, result, aClaw, gameNewTest, monsterInTokyo);  
       assertEquals(false, alien.getInTokyo());
}
    //Testining requirement 13.1   
    @Test
    public void testBuyingCards(){
        SendMessageTest sm = new SendMessageTest("0");
        Game gameNewTest = new Game(sm);
        Alienoid alien = new Alienoid();
        Gigazaur giga = new Gigazaur();
        ArrayList<Monster>monsterList = new ArrayList<>(Arrays.asList(kong,alien,giga));
        Deck deck = new Deck();
        int costOfCard = deck.getStoreCard(0).getCost();
        kong.setEnergy(10);
        gameNewTest.buyWithEnergy(deck, 0, kong, monsterList);
        assertEquals(10-costOfCard, kong.getEnergy());
    }

    //Testing requirement 13.2
    @Test
    public void testResetStoreCost(){
        SendMessageTest sm = new SendMessageTest("3");
        Game gameNewTest = new Game(sm);
        Alienoid alien = new Alienoid();
        Gigazaur giga = new Gigazaur();
        ArrayList<Monster>monsterList = new ArrayList<>(Arrays.asList(kong,alien,giga));
        Deck deck = new Deck();
        //int costOfCard = deck.getStoreCard(0).getCost();
        kong.setEnergy(3);
        gameNewTest.buyWithEnergy(deck, 0, kong, monsterList);
        assertEquals(1, kong.getEnergy());
    }

    //Testing requirement 14
    @Test
    public void testDiscardEffect(){
        SendMessageTest sm = new SendMessageTest("0");
        Game gameNewTest = new Game(sm);
        Alienoid alien = new Alienoid();
        Gigazaur giga = new Gigazaur();
        ArrayList<Monster>monsterList = new ArrayList<>(Arrays.asList(kong,alien,giga));
        Deck deck = new Deck();
        deck.setStoreCard(0, new ApartmentBuilding());
        kong.setEnergy(7);
        gameNewTest.buyWithEnergy(deck, 0, kong, monsterList);
        assertEquals(3, kong.getStars());
    }
    //Testing requirement 16
    @Test
    public void testFirstToTwentyWins(){
        SendMessageTest sm = new SendMessageTest("Test");
        Game gameNewTest = new Game(sm);
        Alienoid alien = new Alienoid();
        Gigazaur giga = new Gigazaur();
        ArrayList<Monster>monsterList = new ArrayList<>(Arrays.asList(kong,alien,giga));
        kong.setStars(20);
        assertTrue(gameNewTest.checkWinByStars(monsterList));
    }
    //Testing requirement 17
    @Test
    public void testLastOneAliveWins(){
        SendMessageTest sm = new SendMessageTest("Test");
        Game gameNewTest = new Game(sm);
        Alienoid alien = new Alienoid();
        Gigazaur giga = new Gigazaur();
        ArrayList<Monster>monsterList = new ArrayList<>(Arrays.asList(kong,alien,giga));
        alien.setCurrentHealth(0);
        giga.setCurrentHealth(0);
        assertTrue(gameNewTest.checkWinByLoneSurvivor(monsterList));  
    }


}
package tokyoking.tests;
import static org.junit.Assert.*;

import tokyoking.KingTokyoPowerUpServer;
import tokyoking.monsters.Monster;
import tokyoking.game.Game;
import java.beans.Transient;

import org.junit.Test;


public class KingTokyoPUTest{
    //KingTokyoPowerUpServer server = new KingTokyoPowerUpServer();
    Game gameTest = new Game();
    Monster kong = new Monster("Kong");
    private boolean monsterInTokyo = false;
    @Test
    public void testDummy(){
        assertEquals(1, 1);
    }

    // @Test
    // public void testEachPlayersAssignedMonster(){

    // }

    @Test
    public void testVictoryPointsSetZero(){
        assertEquals(0, kong.getStars());
    }

    @Test
    public void testStartingHP(){
        assertEquals(10, kong.getCurrentHealth());
    }



    @Test
    public void testMonsterInsideTokyoGainStar(){
        if(!monsterInTokyo) {
            kong.setInTokyo(true);
            monsterInTokyo = true;
            gameTest.increaseStars(kong,1);
        }
        assertEquals(1, kong.getStars());       
    }

}
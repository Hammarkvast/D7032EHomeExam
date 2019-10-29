package tokyoking.monsters;

import tokyoking.SendMessage;
import tokyoking.monsters.Monster;
import tokyoking.game.Game;
import java.util.ArrayList;
import java.util.Random;
public class Kong extends Monster{

    public Kong(){
        super("Kong");
    }

    public void redDawn(int attacker, SendMessage msg, ArrayList<Monster>monsterList){
        msg.sendMessage(attacker, "POWERUP:Deal 2 damage to all others\n", monsterList);
        for(int mon=0; mon<monsterList.size(); mon++) {
            if(mon!=attacker) {
                monsterList.get(mon).setCurrentHealth(monsterList.get(mon).getCurrentHealth()-2);
            }
        }        
    }
    
    public void randomizeKongEvo(Kong kongPlayer, int attacker, SendMessage msg, ArrayList<Monster>monsterList){
        Random randomNumber = new Random();
        int randNumber = randomNumber.nextInt(1);
        if(randNumber == 0){
            kongPlayer.redDawn(attacker, msg, monsterList);
        }
    }

}
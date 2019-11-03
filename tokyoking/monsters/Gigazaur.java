package tokyoking.monsters;

import tokyoking.monsters.Monster;
import tokyoking.SendMessage;
import java.util.Random;
import java.util.ArrayList;
/**
 * @author Tom Hammarkvist
 */
public class Gigazaur extends Monster{
    public Gigazaur(){
        super("Gigazaur");
    }
    //the evo card "radioactive waste"
    private void radioactiveWaste(int reciever, SendMessage msg, Gigazaur gigaPlayer, ArrayList<Monster>monsterList){
        msg.sendMessage(reciever, "POWERUP:Receive 2 energy and 1 health\n", monsterList);
        gigaPlayer.setEnergy(gigaPlayer.getEnergy() + 2);
        if(gigaPlayer.getCurrentHealth() + 1 >= gigaPlayer.getMaxHealth()) {
            gigaPlayer.setCurrentHealth(gigaPlayer.getMaxHealth());
        } else {
            gigaPlayer.setCurrentHealth(gigaPlayer.getCurrentHealth()+1);
        }          
    }
    //the evo card "primal below"
    private void  primalBelow(int attacker, SendMessage msg, Gigazaur gigaPlayer, ArrayList<Monster>monsterList){
        msg.sendMessage(attacker, "POWERUP: All other monsters lose 2 stars\n", monsterList);
        for(int mon = 0; mon<monsterList.size(); mon++){
            Monster currentMonster = monsterList.get(mon);
            if(mon!= attacker){
                if(currentMonster.getStars() > 1){
                    currentMonster.setStars(currentMonster.getStars()-2);
                }else{
                    currentMonster.setStars(0);
                }

            }
        }
    }
    //randomizez which gigazaur evocard should run.
    public void randomizeGigaEvo(Gigazaur gigaPlayer, int reciever, ArrayList<Monster> monsterList, SendMessage msg){
        Random randomNumber = new Random();
        int randNum = randomNumber.nextInt(2);
        if(randNum == 0){
            gigaPlayer.radioactiveWaste(reciever, msg, gigaPlayer, monsterList);
        }else if(randNum == 1){
            gigaPlayer.primalBelow(reciever, msg, gigaPlayer, monsterList);
        }
    }
}
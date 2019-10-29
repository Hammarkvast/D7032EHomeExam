package tokyoking.monsters;

import tokyoking.monsters.Monster;
import tokyoking.SendMessage;
import java.util.Random;
import java.util.ArrayList;
public class Gigazaur extends Monster{
    public Gigazaur(){
        super("Gigazaur");
    }

    private void radioactiveWaste(int reciever, SendMessage msg, Gigazaur gigaPlayer, ArrayList<Monster>monsterList){
        msg.sendMessage(reciever, "POWERUP:Receive 2 energy and 1 health\n", monsterList);
        gigaPlayer.setEnergy(gigaPlayer.getEnergy() + 2);
        if(gigaPlayer.getCurrentHealth() + 1 >= gigaPlayer.getMaxHealth()) {
            gigaPlayer.setCurrentHealth(gigaPlayer.getMaxHealth());
        } else {
            gigaPlayer.setCurrentHealth(gigaPlayer.getCurrentHealth()+1);
        }          
    }

    public void randomizeGigaEvo(Gigazaur gigaPlayer, int reciever, ArrayList<Monster> monsterList, SendMessage msg){
        Random randomNumber = new Random();
        int randNum = randomNumber.nextInt(1);
        if(randNum == 0){
            gigaPlayer.radioactiveWaste(reciever, msg, gigaPlayer, monsterList);
        }
    }
}
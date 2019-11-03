package tokyoking.monsters;

import tokyoking.monsters.Monster;
import tokyoking.SendMessage;
import java.util.ArrayList;
import java.util.Random;
/**
 * @author Tom Hammarkvist
 */
public class Alienoid extends Monster{
    
    public Alienoid(){
        super("Alienoid");
    }
    //the evolution card "the alien scourge"
    private void theAlienScourge(int reciever, SendMessage msg, Alienoid alienPlayer,ArrayList<Monster>monsterList){
        msg.sendMessage(reciever, "POWERUP:Receive 2 stars\n", monsterList);
        alienPlayer.setStars(alienPlayer.getStars()+2);        
    }


    //randomizes which evo card should be run.
    public void randomizeAlienEvo(Alienoid alienPlayer, int reciever, SendMessage msg, ArrayList<Monster>monsterList){
        Random randomNumber = new Random();
        int randNum = randomNumber.nextInt(1);
        if(randNum == 0){
            alienPlayer.theAlienScourge(reciever, msg, alienPlayer, monsterList);
        }
    }

}
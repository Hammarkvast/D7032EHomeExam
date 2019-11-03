package tokyoking.tests;

import tokyoking.SendMessage;
import tokyoking.monsters.Monster;
import java.util.ArrayList;
public class SendMessageTest extends SendMessage{
    private String messageTest;
    public SendMessageTest(String Message){
        this.messageTest = Message;
    }

    @Override
    public String sendMessage(int recipient, String message, ArrayList<Monster>monsterList){
        return messageTest;

    }
    }

package tokyoking;

import tokyoking.monsters.Monster;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.ServerSocket;
import java.util.ArrayList;
public class SendMessage{
    private Scanner sc = new Scanner(System.in);
    public SendMessage(){
        
    }
    public String sendMessage(int recipient, String message,ArrayList<Monster>monsterList) {
        Monster aMonster = monsterList.get(recipient);
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
}
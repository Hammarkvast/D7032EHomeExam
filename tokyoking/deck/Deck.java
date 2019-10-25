package tokyoking.deck;

import java.util.ArrayList;
import java.util.Collections;
import tokyoking.cards.Card;
import tokyoking.cards.storecards.*;

public class Deck {
        private ArrayList<Card> deck = new ArrayList<Card>();
        private Card[] store = new Card[3];
        
        public Deck() {
            deck.add(new AcidAttack());
            deck.add(new AlienMetabolism());
            deck.add(new AlphaMonster());
            deck.add(new ApartmentBuilding());
            deck.add(new ArmorPlating());
            deck.add(new CommuterTrain());
            deck.add(new CornerStone());
            //Todo: Add more cards
            Collections.shuffle(deck);
            // Start the game with 3 cards face up in the store
            for(int i=0; i<3; i++) {
                store[i] = deck.remove(0);
            }
        }
        // Print the store
        public String toString() {
            String returnString = "";
            for(int i=0; i<3; i++) {returnString += "\t["+i+"] " + store[i] + ":";}
            return returnString;
        }

        public ArrayList<Card> getDeck() {    
            return deck;
        }

        public void setDeck(ArrayList<Card> deck) {
            this.deck = deck;
        }

        public Card[] getStore() {
            return store;
        }

        public void setStore(Card[] store) {
            this.store = store;
        }

        public Card getCard(int index){
            return store[index];
        }

        public void setCard(int index, Card newCard){
            this.store[index] = newCard;
        }
    }
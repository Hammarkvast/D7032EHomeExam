package tokyoking.deck;

import java.util.ArrayList;
import java.util.Collections;
import tokyoking.cards.Card;
import tokyoking.cards.storecards.*;
/**
 * @author Tom Hammarkvist
 */
public class Deck {
        private ArrayList<Card> deck = new ArrayList<Card>();
        private Card[] store = new Card[3];
        /**
         * Creates the deck that is used in the game.
         */
        public Deck() {
            deck.add(new AcidAttack());
            deck.add(new AlienMetabolism());
            deck.add(new AlphaMonster());
            deck.add(new ApartmentBuilding());
            deck.add(new ArmorPlating());
            deck.add(new CommuterTrain());
            deck.add(new CornerStone());
            deck.add(new Energize());
            deck.add(new AmusementPark());
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
        /**
         * Gives the user an option to reset the store.
         * This  grabs every card from the store and puts it back into the deck
         * then reshuffles and adds three new cards to the store.
         * @param deck
         */
        public void resetStore(Deck deck){
            ArrayList<Card>fullDeck = deck.getDeck();
            for(int storeCard = 0; storeCard < 3; storeCard++){
                fullDeck.add(deck.getStoreCard(storeCard));
                deck.setDeck(fullDeck);
            }
            Collections.shuffle(fullDeck);
            for (int i = 0; i < 3; i++){
                store[i] = fullDeck.get(i);
            }
        
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

        public Card getStoreCard(int index){
            return store[index];
        }

        public void setStoreCard(int index, Card newCard){
            this.store[index] = newCard;
        }

    }
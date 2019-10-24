package tokyoking.deck;

import java.util.ArrayList;
import java.util.Collections;
import tokyoking.cards.Card;
import tokyoking.effects.Effect;

public class Deck {
        private ArrayList<Card> deck = new ArrayList<Card>();
        private Card[] store = new Card[3];
        
        public Deck() {
            Effect moreDamage = new Effect(); moreDamage.setMoreDamage(1);
            Effect cardsCostLess = new Effect(); cardsCostLess.setCardsCostLess(1);
            Effect starsWhenAttacking = new Effect(); starsWhenAttacking.setStarsWhenAttacking(1);
            Effect stars3 = new Effect(); stars3.setStars(3);
            Effect armor = new Effect(); armor.setArmor(1);
            Effect stars2 = new Effect(); stars2.setStars(2);
            Effect stars1 = new Effect(); stars1.setStars(1);
            deck.add(new Card("Acid Attack", 6, false, moreDamage, "Deal 1 extra damage each turn"));
            deck.add(new Card("Alien Metabolism", 3, false, cardsCostLess, "Buying cards costs you 1 less"));
            deck.add(new Card("Alpha Monster", 5, false, starsWhenAttacking, "Gain 1 star when you attack"));
            deck.add(new Card("Apartment Building", 5, true, stars3, "+3 stars"));
            deck.add(new Card("Armor Plating", 4, false, armor, "Ignore damage of 1"));
            deck.add(new Card("Commuter Train", 4, true, stars2, "+2 stars"));
            deck.add(new Card("Corner Stone", 3, true, stars1, "+1 stars"));
            //Todo: Add more cards
            Collections.shuffle(deck);
            // Start the game with 3 cards face up in the store
            for(int i=0; i<3; i++) {store[i] = deck.remove(0);}
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
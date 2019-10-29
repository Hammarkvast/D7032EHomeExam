package tokyoking.dice;

import java.util.ArrayList;
import java.util.Random;
    public class Dice implements Comparable<Dice> {
        public static final int HEART = 0;
        public static final int ENERGY = 4;
        public static final int CLAWS = 5;
        public int value = -1;  
        private Random ran = new Random();
        public Dice(int value) {
            this.value = value;
        }

        public ArrayList<Dice> diceRoll(int nrOfDice) {
            ArrayList<Dice> dice = new ArrayList<Dice>();
            for(int i=0; i<nrOfDice; i++) {
                dice.add(new Dice(ran.nextInt(6)));
            }
            return dice;
        }

        public void reRoll(ArrayList<Dice> rolledDice){
            rolledDice.addAll(diceRoll(6-rolledDice.size()));
        }

        public String toString() {
            return (value==HEART?"HEART":value==ENERGY?"ENRGY":value==CLAWS?"CLAWS":value==1?"ONE":value==2?"TWO":"THREE");
        }
        @Override
        public int compareTo(Dice o) {
            return value<o.value?-1:value==o.value?0:1;
        }
        public boolean equals(Object o) {
            return value==((Dice) o).value;
        }
        public int hashCode() {
            return toString().hashCode();
        }

    }
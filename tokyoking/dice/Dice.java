package tokyoking.dice;

 /**
  * @author Tom Hammarkvist
  */
    public class Dice implements Comparable<Dice> {
        public static final int HEART = 0;
        public static final int ENERGY = 4;
        public static final int CLAWS = 5;
        public int value = -1; 
        /**
         * Creates a dice, with a specific value.
         * @param value
         */ 
        public Dice(int value) {
            this.value = value;
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
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

/**
 *
 * @author alex
 */
public class Value implements Comparable<Value> {
    
        private Boolean selected;
        private Boolean value;

        public Value(Boolean selected, Boolean value) {
            this.selected = selected;
            this.value = value;
        }

        @Override
        public int compareTo(Value v) {
            return this.value.compareTo(v.value);
        }

        @Override
        public boolean equals(Object v) {
            return v instanceof Value && this.value.equals(((Value) v).value);
        }

        @Override
        public int hashCode() {
            return this.value.hashCode();
        }        
}

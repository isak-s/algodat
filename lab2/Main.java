package lab2;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Main {
    public static void main (String [] args ){
        HashMap map = new HashMap(10);
        Scanner scan = new Scanner(System.in);
        int i=0;

        int error = 0;
        
        boolean is_present = false;

        while(scan.hasNext()){
            String word = scan.nextLine();
            is_present=map.contains(word);
            boolean remove_it = i % 16==0;

            if (is_present) {
                if(remove_it){
                    map.delete(word);
                    if (map.contains(word)){
                    }
                }
                else {
                    map.put(word, map.getVal(word)+1);
                }
            }
            else if (!remove_it) {
                map.put(word, 1);
            }
            if (word.equals("__FRAME_END__")){
                error++;
            }
            i++;
        }

        Pair [] pairs = map.getPairs();
        Pair out = new Pair("", 0);

        //If pairs[j] has higher value, we replace. If they have the same, and out is "lexographically lesser" (alphabetically first), we replace.
        for (int j=0;j<pairs.length;j++){
            if (pairs[j].biggerThan(out) || (pairs[j].equals(out)&&0<out.getKey().compareTo(pairs[j].getKey()))){
                out=pairs[j];
            }
        }

    
    System.out.println(out.getKey() + " " + out.getValue());
    scan.close();
    }

    public static class HashMap {

        private String[] keys;
        private int[] vals;
        private boolean[] wasDeleted;  // false represents empty or filled. True represents was deleted
        private int capacitance;
        private int size;

        public HashMap(int size) {
            this.size=size;
            capacitance=0; //Is this needed? Does it not get 0 by default?
            keys = new String[size];
            vals = new int[size];
            Arrays.fill(keys, "");
            wasDeleted = new boolean[size];
        }

        private void doubleSize(){
            int oldSize = size;
            size *=2;
            capacitance=0;

            String[] tempKeys = keys;
            int[] tempVals = vals;

            keys = new String[size];
            vals = new int[size];
            Arrays.fill(keys, "");
            wasDeleted = new boolean[size];

            for (int i=0; i<oldSize; i++){
                if (!tempKeys[i].isEmpty()){
                    put(tempKeys[i], tempVals[i]);
                }
            }
        }

        //Nothing increases size, can become overfull
        public void put(String key, int val){
            if (capacitance>size/4){
                doubleSize();
            }

            int idx = hashCode(key)%size;

            while(true){
                if (keys[idx].isEmpty()) {
                    //nbr deleted does not change, neither will wasDeleted
                    keys[idx] = key;
                    vals[idx] = val;
                    capacitance++;
                    wasDeleted[idx]=false;
                    return;
                }
                else if (keys[idx].equals(key)){
                    vals[idx] = val;
                    return;
                }
                idx = (idx + 1)%size;
            }
        }

        public Pair[] getPairs(){
            Pair[] ret = new Pair[size];
            int j = 0;
            for (int i=0;i<size;i++){
                ret[j]=(new Pair(keys[i], vals[i]));
                j++;
            }
            return ret;
        }

        public String[] getKeys(){
            return keys;
        }

        public int[] getValues () {
            return vals;
        }

        public boolean contains(String key){
            if (getVal(key)!=0){
                return true;
            }
            return false;
        }
        
        public int getVal(String key){
            int idx = hashCode(key)%size;

            //forloop because if list is full and we are searching for a key that doesnt exist, it loops
            for (int i=0;i<size;i++){
                String tmp = keys[idx];
                if(tmp.equals(key)){
                        return vals[idx];
                }
                if (tmp.isEmpty()&&!wasDeleted[idx]){
                    return 0;
                }
            idx = (idx+1) % size;
            }
            return 0;
        }

        public void delete(String key){
            int idx = hashCode(key)%size;

            //Cant be a while loop, if list is full of either values or wasdeleted, aka no stopping point, but the value we try to delete
            //is not one of them, it will loop forever.
            for (int i = 0; i<size;i++) {
                String tmp = keys[idx];

                if(tmp.equals(key)){
                    keys[idx]="";
                    vals[idx]=0;
                    wasDeleted[idx] = true;
                    capacitance--;
                    return;
                }
                else {
                    if (tmp.isEmpty()&&!wasDeleted[idx]) {
                        return;
                    }
                }            
            idx = (idx+1) % size;

            }

        }

        private int hashCode(String key) {
            return Math.abs(key.hashCode());
        }
        
    }

    public static class Pair {
        private String key;
        private int val;

        public Pair(String key, int val) {
            this.key=key;
            this.val=val;
        }
        
        public int getValue() {
            return val;
        }
        
        public String getKey() {
            return key;
        }

        public boolean biggerThan(Pair other){
            return val>other.val;
        }
        public boolean equals(Pair other) {
            return val==other.val;
        }
    }
}

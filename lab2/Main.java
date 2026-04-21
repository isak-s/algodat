package lab2;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main (String [] args ){
        HashMap map = new HashMap();
        Scanner scan = new Scanner(System.in);
        int i=0;

        int error = 0;
        
        boolean is_present = false;

        while(scan.hasNext()){
            String word = scan.next().strip();

            if (map.getVal(word)==0){
                is_present=false;
            }
            else {
                is_present=true;
            }
            
            boolean remove_it = i%16==0;
            //System.err.println(is_present +" " + word + " " + map.getVal(word)+ " " + remove_it);


            if (is_present) {
                if(remove_it){
                    map.delete(word);
                    //System.err.println("delete!");
                    //System.err.println(i + "     "+ word + " " + map.getVal(word));
                }
                else {
                    map.put(word, map.getVal(word)+1);
                    //put(word, value+1)
                    //System.err.println("inc! "+ word + " " + map.getVal(word));
                }
            }
            else if (!remove_it) {
                map.put(word, 1);
                //System.err.println(word);
            }

            if (word.equals("__FRAME_END__")){
                error++;
                //System.err.println(error + " " + remove_it);
            }
            //System.err.println(i);
            i++;
        }
        Pair [] pairs = map.getPairs();
        Pair out = new Pair("", 0);

        //If pairs[j] has higher value, we replace. If they have the same, and out is "lexographically lesser" (alphabetically first), we replace.
        for (int j=0;j<pairs.length;j++){
            if (pairs[j]!=null){
                if (pairs[j].biggerThan(out) || (pairs[j].equals(out)&&0<out.getKey().compareToIgnoreCase(pairs[j].getKey()))){
                    //System.err.println(pairs[j].getKey() + " " + pairs[j].getValue() );
                    out=pairs[j];
                }
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

        public HashMap() {
            size = 10;
            capacitance=0; //Is this needed? Does it not get 0 by default?
            keys = new String[size];
            vals = new int[size];
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
            wasDeleted = new boolean[size];

            for (int i=0; i<oldSize; i++){
                if (tempKeys[i]!=null){
                    put(tempKeys[i], tempVals[i]);
                }
            }
        }

        //Nothing increases size, can become overfull
        public void put(String key, int val){
            if (capacitance==size){
                doubleSize();
            }

            int idx = hashCode(key)%size;

            while(true){
                if (keys[idx] == null) {
                    //nbr deleted does not change, neither will wasDeleted
                    keys[idx] = key;
                    vals[idx] = val;
                    capacitance++;
                    if (wasDeleted[idx]){
                        wasDeleted[idx]=false;
                    }
                    return;
                }
                if (keys[idx].equals(key)){
                    keys[idx] = key;
                    vals[idx] = val;
                    return;
                }
                idx = (idx + 1)%size;
            }
        }

        public Pair[] getPairs(){
            Pair[] ret = new Pair[capacitance];
            int j = 0;
            for (int i=0;i<size;i++){
                if (keys[i]!=null){
                    ret[j]=(new Pair(keys[i], vals[i]));
                    j++;
                }
                
            }
            return ret;
        }

        public String[] getKeys(){
            return keys;
        }

        public int[] getValues () {
            return vals;
        }

        public int getVal(String key){
            int idx = hashCode(key)%size;
            boolean loop = false;

            for (int i=0;i<size;i++){
                if (keys[idx]!=null){
                    if(keys[idx].equals(key)){
                        return vals[idx];
                    }
                }
                
            if (idx == size-1){
                if (loop) {
                    return 0;
                }
                loop = true;
            }
            idx = (idx+1) % size;
            }
            return 0;
        }

        public void delete(String key){
            int idx = hashCode(key)%size;
            boolean loop = false;

            while(true) {
                if (keys[idx] != null){
                    if(keys[idx].equals(key)){
                        keys[idx]=null;
                        vals[idx]=0;
                        wasDeleted[idx] = true;
                        capacitance--;
                        return;
                    }
                }
                else {
                    if (!wasDeleted[idx]) {
                        return;
                    }
                }
            //When it is going to floor, mark that we have looped. If we try to loop again, then we
            //know it is a true loop and should be killed (ex when capacitance is full, but the key
            //we are searching for is not there, happens when cap is full, delete a key, add a new one,
            //try to delete the same key again. Would loop forever since there is no empty space that 
            //deleted)
            if (idx == size-1){
                if (loop){
                    return;
                }
                loop = true;
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

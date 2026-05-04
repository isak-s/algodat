/*
No letters in either of the strings can
be changed, moved or removed – the only allowed modification is to insert ”*”
aaaj
**aj
b*aj - 4 and

biggest number possible

test all permutations of where we can insert asterisks.
On these, change letters until they are the same

aaaj
baj

->
*baj
    -> swap * to a      accumilate cost
        -> swap b to a
b*aj
ba*j
ba*j

remainder = baj
remainingTarget = abaj

currString = ba****, currCost = int, remainder: String, remainingTarget: string
    is the lenght of the remainder and the remainingTarget the same length &&
    char at remainder[0] the same as remainingTarget[0]
        -> add the cost to currCost, Append to currstring (update lookup table), remove from remainder and target
    otherwise
        is the target longer string than remainder?
            do 2 cases:
            1. insert an asterisk to *remainder* then recurse with tail remainder and tail remainingTarget. (Add to lookup)
            2. insert an astterisk to *raminderTarget* then recurse with tail remainder and tail remainingTarget (add to lookup)
            3. add cost of the diff to cost. Recurse with tail, and insert the asterisk in a later step 1 (add to lookup)



-> subsolutions may overlap?
if we have two asterisks or more,

We should use a trie i think

*/
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        String inp = scan.nextLine();
        String chars = inp.strip();
        String[] inputLine = inp.split(" "); 
        int nLetters = inputLine.length;
        HashMap<String, Letter> letters = new HashMap<>();

        //Every char is converted to a Letter
        for (String letter: inputLine) {
            letters.put(letter, new Letter(letter));
        }

        //every Letter gets its ascendency matrix filled.
        for (int i=0;i<nLetters;i++) {
            for (int j=0; j<nLetters;j++){
                letters.get(Character.toString(chars.charAt(i)))
                    .addCost(letters.get(Character.toString(chars.charAt(j))), scan.nextInt());
            }
        }

        int nQueries = scan.nextInt();

        for (int i=0; i<nQueries; i++){
            System.out.println(getOutput(scan.next(), scan.next(), letters));
        }

        scan.close();




    }

    public static String getOutput(String first, String second, HashMap<String, Letter> letters){
        
        return "";

    }

    public static class Letter {
        String id;
        HashMap<Letter, Integer> replaceCosts;

        public Letter(String id) {
            this.id=id;
        }

        public void addCost(Letter replacement, int cost) {
            replaceCosts.put(replacement, cost);
        }

        public int getCost(Letter replacement) {
            return replaceCosts.get(replacement);
        }
    }
}

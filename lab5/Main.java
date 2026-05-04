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

        String[] inputLine = scan.nextLine().split(" ");
        ArrayList<Letter> letters = new ArrayList<>();
        int nLetters = inputLine.length;

        //Varje
        for (String letter: inputLine) {
            letters.add(new Letter(letter));
        }

        for (int i=0;i<nLetters;i++) {
            for (int j=i; j<nLetters;j++){
                letters.get(i).addCost(letters.get(j).id, scan.nextInt());
            }
        }

        scan.close();

    }

    public static class Letter {
        String id;
        HashMap<String, Integer> replaceCosts;

        public Letter(String id) {
            this.id=id;
        }

        public void addCost(String replacement, int cost) {
            replaceCosts.put(replacement, cost);
        }

        public int getCost(String replacement) {
            return replaceCosts.get(replacement);
        }
    }
}

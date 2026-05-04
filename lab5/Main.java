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

penispen**
**nispenis


currString = ba****, currCost = int, remainderLeft: String, remainderRight: string, (nbrAsterisksInserted, nbrAsterisksAllowed)
    is the lenght of the remainder and the remainingTarget the same length &&
    char at remainder[0] the same as remainingTarget[0]
        -> add the cost to currCost, Append to currstring (update lookup table), remove from remainder and target
    otherwise
        is the target longer string than remainder?
            do 2 cases:
            1. insert an asterisk to *remainder* then recurse with tail remainder and tail remainingTarget. (Add to lookup)
            2. insert an astterisk to *raminderTarget* then recurse with tail remainder and tail remainingTarget (add to lookup)
            3. add cost of the diff to cost. Recurse with tail, and insert the asterisk in a later step 1 (add to lookup)

when we do a recursive call, first check if there is already a best cost for remainder + remainingTarget in our lookup.
If it is empty, we use that cost
otherwise we compute it and store it.

-> subsolutions may overlap?
if we have two asterisks or more,

We should use a trie i think

*/
import java.util.HashMap;
import java.util.Scanner;

public class Main {


    HashMap<Character, Letter> letters;
    HashMap<Integer, Integer> cache;
    /*
    csl = currstring
    cc = currcost
    rl = remainder left
    rr = remainder right
    nAI = nbr asteriskt inserted
    nAA = nbr asterisk allowed
    */

    public int diffCost(char l, char r) {
        return letters.get(l).getCost(r);
    }
    public int match(String csl, int cc, String rl, String rr, int nAI, int nAA) {
        // base case 2: we have already computed the best cost for this remainderLeft and remainderRight!
        // then we just return the saved value from the lookup.

        int key = rl.hashCode() + rr.hashCode();

        if (cache.containsKey(key)) {
            return cache.get(key);
        }


        // base case: no remainders:
        if (rl.length() == 0) {
            return match(csl, cc, "*"+rl, rr, nAI, nAA);
        }
        if (rr.length() == 0) {
            return match(csl, cc, rl, "*"+rr, nAI, nAA);
        }

        // get best match
        int c1 = diffCost(rl.charAt(0), rr.charAt(0));
        int c2 = -4; // for c2 and c3, there is an asterisk inserted which is -4
        int c3 = -4;

        c1 += match(csl, cc, rl.substring(1), rr.substring(1), nAI, nAA);

        if (nAI < nAA) {
            // insert in remainderLeft
            // recursive call with an asterisk inserted
            c2 += match(csl, cc, "*"+rl, rr, ++nAI, nAA);

            // insert in remainderRight
            c3 += match(csl, cc, rl, "*"+rr, nAI, nAA);
        }
        int cost = Math.max(c1, Math.max(c2, c3));
        // with some hashcode representing the remainders
        // cache.put(cost);
        cache.put(key, cost);

        return cost;
    }

    public Main() {
        this.letters = new HashMap<>();
        this.cache = new HashMap<>();
    }

    public static void main(String[] args) {
        Main mainclass = new Main();
        Scanner scan = new Scanner(System.in);
        String inp = scan.nextLine();
        String chars = inp.strip();
        String[] inputLine = inp.split(" ");
        int nLetters = inputLine.length;

        //Every char is converted to a Letter
        for (int i = 0; i < chars.length(); i++) {
            mainclass.letters.put(chars.charAt(i), new Letter(chars.charAt(i)));
        }

        //every Letter gets its ascendency matrix filled.
        for (int i=0;i<nLetters;i++) {
            for (int j=0; j<nLetters;j++){
                mainclass.letters.get(chars.charAt(i))
                    .addCost(chars.charAt(j), scan.nextInt());
            }
        }

        int nQueries = scan.nextInt();

        for (int i=0; i<nQueries; i++){

            String left = scan.next();
            String right = scan.next();
            mainclass.match("", 0, left, right, 0, right.length());
        }

        scan.close();

    }

    public static class Letter {
        char id;
        HashMap<Character, Integer> replaceCosts;

        public Letter(char id) {
            this.id=id;
        }

        public void addCost(char replacement, int cost) {
            replaceCosts.put(replacement, cost);
        }

        public int getCost(char replacement) {
            return replaceCosts.get(replacement);
        }
    }
}

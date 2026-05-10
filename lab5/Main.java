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

NEW APPROACH

1. prepend the left string with an *, without actually doing it. record cost -4, take the entire left string and the tail of the right one and recurse with i and j++
2. same as 1 but with right. recurse with i++ and j
3. record the diff of the chars at i and j, recurse with i++ and j++

the caller then takes the max from the 3 recursive callees, puts into cache

The path we took to get to indices i and j do not say anyhing about what the
rest of the cost will be

we can therefore cache the subsolutions one time and lookup subsequent times.

We solve the problem in 2 steps. first we construt the cache, then we traverse it
to construct the optimal strings.

*/
import java.util.HashMap;
import java.util.Scanner;

public class Main {

    HashMap<Character, Letter> letters;

    public int diffCost(char l, char r) {
        return letters.get(l).getCost(r);
    }
    /* returns the reconstructed strings. */
    public void match(Tuple<Integer, Integer> indices,
        String left, String right, Integer[][] cache) {
        // base case 0: both are at exact end index. Caught by base case 1 and 2
        // base case 1
        // j is at the end of the right string. No more insertions to do.
        // we have to insert asterisks until the lenght is the same.
        if (indices.left == left.length()) {
            cache[indices.left][indices.right] =  -4 * right.length() - indices.right;
        }
        // base case 2
        // i is at the end of the left string. no more insertions to do.
        // asterisks...
        if (indices.right == right.length()) {
            cache[indices.left][indices.right] = -4 * (left.length() - indices.left);
        }

        // case1.
        // prepend the left string with an *, without actually doing it.
        // record cost -4, take the entire left string and the tail of the right
        // one and recurse with i and j++
        int case1 = 1;
        //case2.
        // same as case1 but with i++ and j
        int case2 = 2;
        // case 3.
        // record the diff of th ecars at i and j, recurse with i++ and j++
        int case3 = 3;

        int best = Math.max(case1, Math.max(case2, case3));

        cache[indices.left][indices.right] = best;

    }

    public Tuple<String, String> retrace(String left, String right, Integer[][] cache) {
        StringBuilder sbLeft = new StringBuilder();
        StringBuilder sbRight = new StringBuilder();

        // we have 3 neighbors. (--i, j) (i, --j) and (--i, --j)


        return new Tuple<>("penis", "penis");
        }


    public Main() {
        this.letters = new HashMap<>();
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
                int c = scan.nextInt();
                mainclass.letters.get(chars.charAt(i))
                    .addCost(chars.charAt(j), c);
                mainclass.letters.get(chars.charAt(j))
                    .addCost(chars.charAt(i), c);
            }
        }

        int nQueries = scan.nextInt();

        for (int i=0; i<nQueries; i++){

            String left = scan.next();
            String right = scan.next();
            Integer[][] cache = new Integer[left.length()][right.length()];
            mainclass.match(
                new Tuple<Integer,Integer>(0, 0),
                left,
                right,
                cache
            );
            Tuple<String, String> res = mainclass.retrace(left, right, cache);

            System.out.println(res.left + " " + res.right);
        }
        scan.close();
    }

    public static class Tuple<X, Y> {
        public final X left;
        public final Y right;

        public Tuple(X left, Y right) {
            this.left = left;
            this.right = right;
        }
    }

    public static class Letter {
        char id;
        HashMap<Character, Integer> replaceCosts;

        public Letter(char id) {
            this.id=id;
            replaceCosts = new HashMap<>();
            //replaceCosts.put('*', 1000);
        }

        public void addCost(char replacement, int cost) {
            replaceCosts.put(replacement, cost);
        }

        public int getCost(char replacement) {
            return replaceCosts.get(replacement);
        }
    }
}

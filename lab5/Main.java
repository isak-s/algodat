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

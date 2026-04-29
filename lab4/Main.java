import java.util.ArrayList;
import java.util.Scanner;

public class Main{
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        int nPlayers = scan.nextInt();
        Coord[] players = new Coord[nPlayers];
        ArrayList<Integer> xs = new ArrayList<>();

        double dist = 0.0;
        for (int i=0; i<nPlayers; i++) {
            int x = scan.nextInt();
            int y = scan.nextInt();
            players[i]= new Coord (x, y);
            xs.add(x);
        }

        xs.sort(Coord::compareTo);
        double median = nPlayers % 2 == 0 ? (double) (xs.get(nPlayers/2) + xs.get(nPlayers/2 + 1))/2 : xs.get(nPlayers/2 + 1);
        Math.round(nPlayers);
        scan.close();
        String res = Double.toString(dist);
        //Index of comma, and then six digits after. Is +7 because substring is [a,b[
        System.out.println(res.substring(0, res.indexOf(",")+7));
    }

    public static class Coord implements Comparable{
        int x;
        int y;

        public Coord(int x, int y) {
            this.x=x;
            this.y=y;
        }

        @Override
        public int compareTo(Object o) {
            if (o instanceof Coord) {
            throw new UnsupportedOperationException("Unimplemented method 'compareTo'");        
            }
            Coord other = (Coord) o;
            return this.x - other.x;
            // TODO Auto-generated method stub
        }
    }
}
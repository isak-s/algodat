import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Main{
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        int nPlayers = scan.nextInt();
        ArrayList<Coord> players = new ArrayList<>(nPlayers);
        // ArrayList<Integer> xs = new ArrayList<>();

        double dist = 0.0;
        for (int i=0; i<nPlayers; i++) {
            int x = scan.nextInt();
            int y = scan.nextInt();
            players[i]= new Coord (x, y);
            xs.add(x);
        }

        Math.round(nPlayers);
        scan.close();
        String res = Double.toString(dist);
        //Index of comma, and then six digits after. Is +7 because substring is [a,b[
        System.out.println(res.substring(0, res.indexOf(",")+7));
    }

    public double closestPair(ArrayList<Coord> coords) {
        // sort all by x
        coords.sort(null);
        // start the algorithm
        return _closestPair(coords);
    }

    private double _closestPair(ArrayList<Coord> coords) {
        // double m = getMedian(coords);
        // case 1: only one in coords, INF - no closest
        // case 2: only 2 in coords, return distance between the 2
        // case 3: more: divide into 2 subsets and recurse. Get closest pair in the divided pairs, then look for closer pairs along the dividing line

        switch (coords.size()) {
            case 1:
                return Integer.MAX_VALUE;
            case 2:
                return coords.get(0).dist(coords.get(1));
            default:
                ArrayList<Coord> c1 = (ArrayList<Coord>) coords.subList(0, coords.size() / 2);
                ArrayList<Coord> c2 = (ArrayList<Coord>) coords.subList(coords.size() / 2, coords.size());
                var r1 = _closestPair(c1);
                var r2 = _closestPair(c2);
                var submin = Math.min(r1, r2);
                // get closest pair in the 2 sublists
                // make a bounding box with that delta.
                // if we find a closer pair between the dividing line, use that.
                Double dlMin = Double.MAX_VALUE;
                // filter out all points in coords that are further from the dividing line than subMin
                // check y values
                // --- For each point, check dist to all other points in the bounding box


                return Math.min(dlMin, submin);
        }
    }

    private double getMedian(Coord[] coords) {
        int l = coords.length;
        return l % 2 == 0
            ? (double) coords[l/2].x + coords[l/2 + 1].x/2
            : coords[l/2 + 1].x;
    }

    public static class Coord implements Comparable{
        int x;
        int y;

        public Coord(int x, int y) {
            this.x=x;
            this.y=y;
        }
        // euclidean
        public double dist(Coord c2) {
            return Math.sqrt(Math.pow(x-c2.x, 2) + Math.pow(y - c2.y, 2));
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
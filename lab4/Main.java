import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class Main{
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        int nPlayers = scan.nextInt();
        ArrayList<Coord> players = new ArrayList<>(nPlayers);

        for (int i=0; i<nPlayers; i++) {
            int x = scan.nextInt();
            int y = scan.nextInt();
            players.add(new Coord (x, y));
        }

        scan.close();

        double dist = closestPair(players);

        DecimalFormat formatter = new DecimalFormat("#0.000000");
        System.out.println(formatter.format(dist));

    }

    static public double closestPair(ArrayList<Coord> coords) {
        // sort all by x
        coords.sort(null);
        // start the algorithm
        return _closestPair(coords);
    }

    static private double _closestPair(List<Coord> coords) {
        // double m = getMedian(coords);
        // case 1: only one in coords, INF - no closest
        // case 2: only 2 in coords, return distance between the 2
        // case 3: more: divide into 2 subsets and recurse. Get closest pair in the divided pairs, then look for closer pairs along the dividing line

        switch (coords.size()) {
            case 1:
                return Integer.MAX_VALUE;
            case 2:
                return coords.get(0).euDist(coords.get(1));
            default:
                List<Coord> c1 = coords.subList(0, coords.size() / 2);
                List<Coord> c2 = coords.subList(coords.size() / 2, coords.size());
                var r1 = _closestPair(c1);
                var r2 = _closestPair(c2);
                var min = Math.min(r1, r2);
                // get closest pair in the 2 sublists
                // make a bounding box with that delta.
                // if we find a closer pair between the dividing line, use that.
                // filter out all points in coords that are further from the dividing line than min
                var med = getMedian(coords); // this is the dividing line

                List<Coord> withinBoundingBox = new ArrayList<>();
                for (int i = coords.size()/2; i < coords.size() && xdist(coords.get(i).x, med) < min; i--) {
                    withinBoundingBox.add(coords.get(i));
                }
                for (int i = coords.size()/2; i < coords.size() && xdist(coords.get(i).x, med) < min; i++) {
                    withinBoundingBox.add(coords.get(i));
                }
                withinBoundingBox.sort(Comparator.comparingInt(Coord::getY));
                // check y values
                // --- For each point, check dist to next 7 points in the bounding box
                for (int i = 0; i < withinBoundingBox.size(); i++) {
                    for (int j = i+1; j < i + 8 && j < withinBoundingBox.size(); j++) {
                        // if (j > withinBoundingBox.size()) {
                            // break;
                        // }
                        var dist = withinBoundingBox.get(i)
                            .euDist(withinBoundingBox.get(j));
                        if (dist < min) {
                            min = dist;
                        }
                    }
                }
                return min;
        }
    }

    static private double getMedian(List<Coord> coords) {
        int l = coords.size();
        return l % 2 == 0
            ? (double) coords.get(l/2).x + coords.get(l/2 + 1).x/2
            : coords.get(l/2 + 1).x;
    }

    static public double xdist(double a, double b) {
        return Math.abs(a-b);
    }

    public static class Coord implements Comparable{
        int x;
        int y;

        public int getY() {
            return y;
        }

        public Coord(int x, int y) {
            this.x=x;
            this.y=y;
        }
        // euclidean
        public double euDist(Coord c2) {
            return Math.sqrt(Math.pow(x-c2.x, 2) + Math.pow(y - c2.y, 2));
        }

        public int xdist(Coord c2) {
            return Math.abs(x - c2.x);
        }

        @Override
        public int compareTo(Object o) {
            if (!(o instanceof Coord)) {
                throw new UnsupportedOperationException("penis");
            }
            Coord other = (Coord) o;
            return this.x - other.x;
        }
    }
}
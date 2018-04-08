import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;

public class Outcast {
    // constructor takes a WordNet object
    private final WordNet wordnet;

    public Outcast(WordNet wordnet) {
        this.wordnet = wordnet;
    }

    // given an array of WordNet nouns, return an outcast
    public String outcast(String[] nouns) {
        String maxNoun = null;
        int maxDist = 0;

        for (String noun : nouns) {
            int currDist = 0;
            System.out.println("Max dist: " + maxDist);
            for (String otherNoun : nouns) {
                currDist += this.wordnet.distance(noun, otherNoun);
                System.out.println(noun + " to " + otherNoun + ": " + currDist);
            }
            if (currDist > maxDist) {
                maxDist = currDist;
                maxNoun = noun;
            }
            System.out.println("\n");
        }
        return maxNoun;
    }

    // see test client below
    public static void main(String[] args) {

        WordNet wordnet = new WordNet(args[0], args[1]);
        Outcast outcast = new Outcast(wordnet);

        for (int t = 2; t < args.length; t++) {
            In in = new In(args[t]);
            String[] nouns = in.readAllStrings();
            System.out.println(Arrays.toString(nouns));
            StdOut.println(args[t] + ": " + outcast.outcast(nouns));
        }
    }
}

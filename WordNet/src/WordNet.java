/**
 * Corner cases.
 * All methods and the constructor should throw a java.lang.IllegalArgumentException if any argument is null.
 * The constructor should throw a java.lang.IllegalArgumentException if the input does not correspond to a rooted DAG.
 * The distance() and sap() methods should throw a java.lang.IllegalArgumentException
 * unless both of the noun arguments are WordNet nouns.
 **/

import edu.princeton.cs.algs4.*;

import java.util.*;

public class WordNet {

    private final Hashtable<Integer, String[]> intToWord = new Hashtable<>();
    private final TreeMap<String, ArrayList<Integer>> wordToInt = new TreeMap<>();
    private final Digraph wordNet;

    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        if (synsets == null || hypernyms == null) throw new IllegalArgumentException();

        In synsetsFile = new In(synsets);
        In hypernymsFile = new In(hypernyms);
        int n = 0; // n will be used to get the last index basically, to create the Digraph.

        while (synsetsFile.hasNextLine()) {
            String line = synsetsFile.readLine();
            String[] result = line.split(",");

            int index = Integer.parseInt(result[0]);
            String[] synset = result[1].split(" ");

            intToWord.put(index, synset);
            for (String syn : synset) {
                if (wordToInt.containsKey(syn)) wordToInt.get(syn).add(index);
                else {
                    ArrayList<Integer> indexes = new ArrayList<>();
                    indexes.add(index);
                    wordToInt.put(syn, indexes);
                }
            }
            n++;
        }

        this.wordNet = new Digraph(n);
        while (hypernymsFile.hasNextLine()) {
            String line = hypernymsFile.readLine();
            String[] indexes = line.split(",");
            int sourceIndex = Integer.parseInt(indexes[0]);

            for (int i = 1; i < indexes.length; i++) {
                wordNet.addEdge(sourceIndex, Integer.parseInt(indexes[i]));
            }
        }
        DirectedCycle cycle = new DirectedCycle(wordNet);
        if (cycle.hasCycle()) throw new IllegalArgumentException();
    }


    //    // returns all WordNet nouns
    public Iterable<String> nouns() {
        return this.wordToInt.keySet();
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        if (word == null) throw new IllegalArgumentException();
        return this.wordToInt.containsKey(word);
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        if (!isNoun(nounA) || !isNoun(nounB)) throw new IllegalArgumentException();
        ArrayList<Integer> indexesA = this.wordToInt.get(nounA);
        ArrayList<Integer> indexesB = this.wordToInt.get(nounB);
//        System.out.println(nounA + " bag: " + indexesA.toString());
//        System.out.println(nounB + " bag: " + indexesB.toString());
        SAP sap = new SAP(this.wordNet);
        return sap.length(indexesA, indexesB);
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        if (!isNoun(nounA) || !isNoun(nounB)) throw new IllegalArgumentException();
        ArrayList<Integer> indexesA = this.wordToInt.get(nounA);
        ArrayList<Integer> indexesB = this.wordToInt.get(nounB);
        SAP sap = new SAP(this.wordNet);
        int sapIndex = sap.ancestor(indexesA, indexesB);
        return this.intToWord.get(sapIndex)[0];
    }


    // do unit testing of this class
    public static void main(String[] args) {
        String synsets = args[0];
        String hypernyms = args[1];

        In synsetsFile = new In(synsets);
        In hypernymsFile = new In(hypernyms);

        WordNet wordNet = new WordNet(synsets, hypernyms);
        while (synsetsFile.hasNextLine()) {
            String line = synsetsFile.readLine();
            String[] result = line.split(",");
            String[] synset = result[1].split(" ");
            for (String syn : synset) assert wordNet.isNoun(syn);
        }
        System.out.println("[PASSED] isNoun");
        while (hypernymsFile.hasNextLine()) {
            String line = hypernymsFile.readLine();
            ArrayList<String> indexes = new ArrayList<>(Arrays.asList(line.split(",")));
            int sourceIndex = Integer.parseInt(indexes.get(0));

            List<String> hypernymIndexes = indexes.subList(1, indexes.size());
            assert wordNet.wordNet.outdegree(sourceIndex) == hypernymIndexes.size();
        }
        System.out.println("[PASSED] adjacentIndexes");
    }
}

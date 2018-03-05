import edu.princeton.cs.algs4.StdIn;

/*
* takes an integer k as a command-line argument;
* reads in a sequence of strings from standard input using StdIn.readString();
* prints exactly k of them, uniformly at random. Print each item from the sequence at most once.

% more distinct.txt                        % more duplicates.txt
A B C D E F G H I                          AA BB BB BB BB BB CC CC

% java-algs4 Permutation 3 < distinct.txt   % java-algs4 Permutation 8 < duplicates.txt
C                                               BB
G                                               AA
A                                               BB
                                                CC
% java-algs4 Permutation 3 < distinct.txt       BB
E                                               BB
F                                               CC
G                                               BB

* */
public class Permutation {
    public static void main(String[] args) {
        int k = Integer.parseInt(args[0]);
        RandomizedQueue<String> queue = new RandomizedQueue<String>();
        while (!StdIn.isEmpty()) {
            String s = StdIn.readString();
            queue.enqueue(s);
        }
        while (k > 0) {
            String s = queue.dequeue();
            System.out.println(s);
            k--;
        }
    }
}

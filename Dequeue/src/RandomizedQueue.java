/*
* Throw a java.lang.IllegalArgumentException if the client calls enqueue() with a null argument.
Throw a java.util.NoSuchElementException if the client calls either sample() or dequeue() when the randomized queue is empty.
Throw a java.util.NoSuchElementException if the client calls the next() method in the iterator when there are no more items to return.
Throw a java.lang.UnsupportedOperationException if the client calls the remove() method in the iterator.

Your randomized queue implementation must support each randomized queue operation
(besides creating an iterator) in constant amortized time.
That is, any sequence of m randomized queue operations (starting from an empty queue) must take at
most cm steps in the worst case, for some constant c. A randomized queue containing n items must use at most
48n + 192 bytes of memory. Additionally, your iterator implementation must support operations next() and
hasNext() in constant worst-case time; and construction in linear time;
you may (and will need to) use a linear amount of extra memory per iterator.
*
* */

import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private Item[] queue;
    private int N;
    private int tail;

    // construct an empty randomized queue
    public RandomizedQueue() {
        this.queue = (Item[]) new Object[1];
        this.N = 0;
        this.tail = 0;
    }

    // is the randomized queue empty?
    public boolean isEmpty() {
        return this.N == 0;
    }

    // return the number of items on the randomized queue
    public int size() {
        return this.N;
    }

    // add the item
    public void enqueue(Item item) {
        // exception handling
        if (item == null) throw new IllegalArgumentException();

        // add item
        this.queue[this.N++] = item;
        if (this.N > 1) this.tail++;

        // resizing array logic
        if (this.queue.length == this.N) resize(this.queue.length * 2);
    }

    // remove and return a random item
    public Item dequeue() {
        // exception handling
        if (this.N == 0) throw new NoSuchElementException();

        // remove random item
        int randIndex = StdRandom.uniform(this.N);
        Item toRemove = this.queue[randIndex];
        this.queue[randIndex] = this.queue[this.tail];
        this.queue[this.tail] = null;
        this.N--;
        if (this.tail > 0) this.tail--;

        // resizing array logic
        if (this.N > 0 && this.N == this.queue.length / 4) resize(this.queue.length / 2);

        return toRemove;
    }

    private void resize(int N) {
        Item[] smallerQueue = (Item[]) new Object[N];
        for (int i = 0; i < this.queue.length; i++) {
            if (this.queue[i] != null) smallerQueue[i] = this.queue[i];
        }
        this.queue = smallerQueue;
    }

    // return a random item (but do not remove it)
    public Item sample() {
        // exception handling
        if (this.N == 0) throw new NoSuchElementException();

        int randIndex = StdRandom.uniform(this.N);
        return this.queue[randIndex];

    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        return new RandomizedQueueIterator();
    }

    private class RandomizedQueueIterator implements Iterator<Item> {
        RandomizedQueue<Item> iteratorQueue;

        private RandomizedQueueIterator() {
            this.iteratorQueue = new RandomizedQueue<Item>();
            for (Item item : queue) {
                if (item != null) iteratorQueue.enqueue(item);
            }
        }

        public boolean hasNext() {
            return 0 < this.iteratorQueue.size();
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

        public Item next() {
            if (!hasNext()) throw new NoSuchElementException();
            return this.iteratorQueue.dequeue();
        }
    }

    // unit testing (optional)
    private static void testInit(RandomizedQueue<Integer> queue) {
        assert queue.size() == 0;
        assert queue.isEmpty();
        assert queue.N == 0;
    }

    private static void testEnqueue(RandomizedQueue<Integer> queue) {
        for (int i = 0; i < 10; i++) {
            queue.enqueue(i);
        }
        assert !queue.isEmpty();
        assert queue.size() == 10;
        assert queue.tail == 9;
        assert queue.queue.length == 20;
    }

    private static void testSample(RandomizedQueue<Integer> queue) {
        queue.sample();
        assert queue.size() == 10;
        assert queue.tail == 9;
        assert queue.queue.length == 20;
    }

    private static void testDequeue(RandomizedQueue<Integer> queue) {
        int size = queue.size();
        int N = size;
        int tail = queue.tail;
        for (int i = 0; i < N; i++) {
            int item = queue.dequeue();
            assert item >= 0 && item <= 9;
            assert queue.size() == size - 1;
            assert queue.tail == tail - 1;
            size--;
            tail--;
        }
        assert queue.size() == 0;
        assert queue.tail == 0;
    }

    private static void testIterator() {
        RandomizedQueue<Integer> queue = new RandomizedQueue<Integer>();
        for (int i = 0; i < 10; i++) {
            queue.enqueue(i);
        }
        for (int i : queue) {
            System.out.println(i);
        }
    }

    public static void main(String[] args) {
        RandomizedQueue<Integer> queue = new RandomizedQueue<Integer>();
        testInit(queue);
        testEnqueue(queue);
        testSample(queue);
        testDequeue(queue);
        testIterator();

    }
}

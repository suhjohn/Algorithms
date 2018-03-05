import java.util.Iterator;
import java.util.NoSuchElementException;

/*
* Throw a java.lang.IllegalArgumentException if the client calls either addFirst() or addLast() with a null argument.
Throw a java.util.NoSuchElementException if the client calls either removeFirst() or removeLast when the deque is empty.
Throw a java.util.NoSuchElementException if the client calls the next() method in the iterator when there are no more items to return.
Throw a java.lang.UnsupportedOperationException if the client calls the remove() method in the iterator.

Performance requirements.  Your deque implementation must support each deque operation (including construction)
in constant worst-case time. A deque containing n items must use at most 48n + 192 bytes of memory and
use space proportional to the number of items currently in the deque.
Additionally, your iterator implementation must support each operation
(including construction) in constant worst-case time.

* */
public class Deque<Item> implements Iterable<Item> {
    private Node first;
    private Node last;
    private int N;

    private class Node {
        private Item item;
        private Node prev;
        private Node next;
    }

    // construct an empty deque
    public Deque() {
        this.first = null;
        this.last = null;
        this.N = 0;
    }

    // is the deque empty?
    public boolean isEmpty() {
        return first == null;
    }

    // return the number of items on the deque
    public int size() {
        return this.N;
    }

    // add the item to the front
    public void addFirst(Item item) {
        if (item == null) throw new IllegalArgumentException();
        Node first = new Node();
        first.item = item;

        if (this.first == null) {
            this.first = first;
            this.last = first;
        } else {
            first.next = this.first;
            this.first.prev = first;
            this.first = first;
        }
        this.N++;
    }

    // add the item to the end
    public void addLast(Item item) {
        if (item == null) throw new IllegalArgumentException();
        Node last = new Node();
        last.item = item;

        if (this.last == null) {
            this.first = last;
            this.last = last;
        } else {
            this.last.next = last;
            last.prev = this.last;
            this.last = this.last.next;
        }
        this.N++;
    }

    //   remove and return the item from the front
    public Item removeFirst() {
        if (this.N == 0) throw new NoSuchElementException();
        Item toRemove = this.first.item;
        this.first.item = null;
        // NullPointer exception occurs when this.first.next is null, because null would not have attribute .prev
        if (this.first.next != null) this.first.next.prev = null;
        this.first = this.first.next;
        // Ensure that if all items are deleted, the deque is not referencing any dangling Nodes.
        if (this.first == null) this.last = null;
        this.N--;
        return toRemove;
    }

    // remove and return the item from the end
    public Item removeLast() {
        if (this.N == 0) throw new NoSuchElementException();
        Item toRemove = this.last.item;
        this.last.item = null;
        // NullPointer exception occurs when this.first.next is null, because null would not have attribute .prev
        if (this.last.prev != null) this.last.prev.next = null;
        this.last = this.last.prev;
        // Ensure that if all items are deleted, the deque is not referencing any dangling Nodes.
        if (this.last == null) this.first = null;
        this.N--;
        return toRemove;
    }

    // return an iterator over items in order from front to end
    public Iterator<Item> iterator() {
        return new DequeIterator();
    }

    private class DequeIterator implements Iterator<Item> {
        private Node curr = first;

        public boolean hasNext() {
            return curr != null;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

        public Item next() {
            if (!hasNext()) throw new NoSuchElementException();
            Item item = curr.item;
            curr = curr.next;
            return item;
        }

    }

    // unit testing (optional)
    public static void main(String[] args) {
        // RemoveFirst Unittest
        Deque<String> deque3 = new Deque<String>();
        String[] testarr3 = {"A", "B", "C"};
        deque3.addFirst(testarr3[2]);
        deque3.addFirst(testarr3[1]);
        deque3.addFirst(testarr3[0]);
        assert deque3.size() == 3;
        assert deque3.first.item.equals("A");
        assert deque3.last.item.equals("C");

        String rf1 = deque3.removeFirst();
        assert rf1.equals("A");
        assert deque3.size() == 2;
        assert deque3.first.item.equals("B");
        assert deque3.first.prev == null;
        assert deque3.first.next.item.equals("C");

        String rf2 = deque3.removeFirst();
        assert rf2.equals("B");
        assert deque3.size() == 1;
        assert deque3.first.item.equals("C");
        assert deque3.last.item.equals("C");
        assert deque3.first.prev == null;
        assert deque3.first.next == null;

        String rf3 = deque3.removeFirst();
        assert rf3.equals("C");
        assert deque3.size() == 0;
        assert deque3.first == null;
        assert deque3.last == null;


        // RemoveLast Unittest
        Deque<String> deque4 = new Deque<String>();
        String[] testarr4 = {"A", "B", "C"};
        deque4.addLast(testarr4[0]);
        deque4.addLast(testarr4[1]);
        deque4.addLast(testarr4[2]);
        assert deque4.size() == 3;

        String rl1 = deque4.removeLast();
        assert rl1.equals("C");
        assert deque4.size() == 2;
        assert deque4.last.item.equals("B");
        assert deque4.last.next == null;
        assert deque4.last.prev.item.equals("A");

        deque4.removeLast();
        String rl3 = deque4.removeLast();
        assert rl3.equals("A");
        assert deque4.size() == 0;
        assert deque4.first == null;
        assert deque4.last == null;

    }
}



package priorityqueues;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.NoSuchElementException;

/**
 * @see ExtrinsicMinPQ
 */
public class ArrayHeapMinPQ<T> implements ExtrinsicMinPQ<T> {
    // IMPORTANT: Do not rename these fields or change their visibility.
    // We access these during grading to test your code.
    static final int START_INDEX = 1;
    ArrayList<PriorityNode<T>> items;
    private int size;
    private HashMap<T, Integer> indexTracker = new HashMap<>();

    public ArrayHeapMinPQ() {
        items = new ArrayList<>(10);
        this.size = 0;
        items.add(null);
    }

    // Here's a method stub that may be useful. Feel free to change or remove it, if you wish.
    // You'll probably want to add more helper methods like this one to make your code easier to read.
    /**
     * A helper method for swapping the items at two indices of the array heap.
     */
    private void swap(int a, int b) {
        PriorityNode<T> aNode = items.get(a);
        PriorityNode<T> bNode = items.get(b);
        indexTracker.put(aNode.getItem(), b);
        indexTracker.put(bNode.getItem(), a);

        items.set(a, bNode);
        items.set(b, aNode);
    }

    // returns parent of the node at index i
    private int parent(int i) { return (i/2); }

    // returns left child of the node at index i
    private int leftChild(int i) {
        return (2*i);
    }

    // returns right child of the node at index i
    private int rightChild(int i) {
        return (2*i) + 1;
    }


    @Override
    public void add(T item, double priority) {
        if (indexTracker.containsKey(item)) {
            throw new IllegalArgumentException("Duplicate item value");
        }
        PriorityNode<T> pqnode = new PriorityNode<>(item, priority);
        items.add(size + 1, pqnode);
        size++;
        indexTracker.put(item, size);
        if (size > 1) {
            percolateUp(size);
        }
    }

    private void percolateUp(int i) {
        if (i != 1) {
            while (items.get(parent(i)).getPriority() > items.get(i).getPriority()) {
                swap(parent(i), i);
                i = parent(i);
                if (i == 1) {
                    break;
                }
            }
        }
    }



    @Override
    public boolean contains(T item) {
        return indexTracker.containsKey(item);
    }

    @Override
    public T peekMin() {
        if (size == 0) {
            throw new NoSuchElementException();
        }
        return items.get(START_INDEX).getItem();
    }

    @Override
    public T removeMin() {
        if (size == 0) {
            throw new NoSuchElementException();
        }
        // saves the minimum value
        T removedItem = items.get(START_INDEX).getItem();
        indexTracker.remove(removedItem);

        // moves last item to the top of heap
        items.set(START_INDEX, items.get(size));

        // removes last item
        items.remove(size);
        size--;

        // goes to helper method in order to maintain heap invariance after removing
        percolateDown(START_INDEX);
        return removedItem;
    }

    //maintain heap invariance for remove Min using recursion
    private void percolateDown(int i) {
        int min = i;
        int left = leftChild(i);
        int right = rightChild(i);
        if (left <= size && items.get(left).getPriority() < items.get(min).getPriority()) {
            min = left;
        }
        if (right <= size && items.get(right).getPriority() < items.get(min).getPriority()) {
            min = right;
        }
        if (i != min) {
            swap(i, min);
            percolateDown(min);
        }
    }

    @Override
    public void changePriority(T item, double priority) {
        if (size <= 1) {
            throw new NoSuchElementException();
        }
        if (indexTracker.containsKey(item)) {
            // not sure if this is a pointer or a new node?
            PriorityNode<T> changed = items.get(indexTracker.get(item));
            double oldPriority = changed.getPriority();
            changed.setPriority((priority));
            if (oldPriority >= priority) {
                percolateUp(indexTracker.get(item));
            } else {
                percolateDown(indexTracker.get(item));
            }
        }

    }

    @Override
    public int size() {
        return size;
    }
}

import java.util.Arrays;

public class SPQ<K extends Comparable<K>, V> {

    public static void main(String[] args) {
        SPQ<Integer, String> spq = new SPQ<>();

        spq.insert(10, "A");
        spq.insert(20, "B");
        spq.insert(5, "C");
        spq.insert(15, "D");
        spq.insert(25, "E");
        System.out.println("Heap after insertions: " + spq);

        System.out.println("Removed top: " + spq.removeTop());
        System.out.println("Heap after removing top: " + spq);

        SPQ.Entry<Integer, String> e = spq.insert(30, "F");
        System.out.println("Heap after inserting (30, F): " + spq);
        System.out.println("Replaced key: " + spq.replaceKey(e, 8));
        System.out.println("Heap after replacing key: " + spq);

        spq.toggle();
        System.out.println("Heap after toggle to Max-heap: " + spq);

        System.out.println("Replaced value: " + spq.replaceValue(e, "Z"));
        System.out.println("Heap after replacing value: " + spq);

        System.out.println("Removed entry: " + spq.remove(e));
        System.out.println("Heap after removing entry: " + spq);

        System.out.println("Current state: " + spq.state());
        System.out.println("Current size: " + spq.size());
    }


    private static class Entry<K, V> {
        K key;
        V value;

        Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public String toString() {
            return "(" + key + ", " + value + ")";
        }
    }

    private Entry<K, V>[] heap;
    private int size;
    private boolean isMinHeap;

    public SPQ() {
        heap = new Entry[2];
        size = 0;
        isMinHeap = true; // We will keep minHeap as default option
    }

    /**
     * Toggle between min-heap and max-heap
     */
    public void toggle() {
        isMinHeap = !isMinHeap;
        for (int i = size / 2 - 1; i >= 0; i--) {
            downHeap(i);
        }
    }

    /**
     * Insert a key-value pair
     */
    public Entry<K, V> insert(K key, V value) {
        if (size == heap.length) extendArray();
        Entry<K, V> newEntry = new Entry<>(key, value);
        heap[size] = newEntry;
        upHeap(size);
        size++;
        return newEntry;
    }

    /**
     * Remove the top element (smallest or largest depending on state)
     */
    public Entry<K, V> removeTop() {
        if (isEmpty()) return null;
        Entry<K, V> top = heap[0];
        heap[0] = heap[--size];
        downHeap(0);
        return top;
    }

    /**
     * Return the top element without removing it
     */
    public Entry<K, V> top() {
        return isEmpty() ? null : heap[0];
    }

    /**
     * Remove a specific entry
     */
    public Entry<K, V> remove(Entry<K, V> e) {
        for (int i = 0; i < size; i++) {
            if (heap[i] == e) {
                Entry<K, V> removed = heap[i];
                heap[i] = heap[--size];
                reheapify(i);
                return removed;
            }
        }
        return null;
    }

    /**
     * Replace the key of a specific entry
     */
    public K replaceKey(Entry<K, V> e, K newKey) {
        for (int i = 0; i < size; i++) {
            if (heap[i] == e) {
                K oldKey = heap[i].key;
                heap[i].key = newKey;
                reheapify(i);
                return oldKey;
            }
        }
        return null;
    }

    /**
     * Replace the value of a specific entry
     */
    public V replaceValue(Entry<K, V> e, V newValue) {
        for (int i = 0; i < size; i++) {
            if (heap[i] == e) {
                V oldValue = heap[i].value;
                heap[i].value = newValue;
                return oldValue;
            }
        }
        return null;
    }

    /**
     * Get the current state (min-heap or max-heap)
     */
    public String state() {
        return isMinHeap ? "Min-heap" : "Max-heap";
    }

    /**
     * Check if the heap is empty
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Get the size of the heap
     */
    public int size() {
        return size;
    }

    // --- Helper Methods ---
    private void extendArray() {
        heap = Arrays.copyOf(heap, heap.length * 2);

        Entry[] temp = new Entry[heap.length * 2];
        for (int i = 0; i < heap.length; i++) {
            temp[i] = heap[i];
        }
        heap = temp;
    }

    private void upHeap(int index) {
        while (index > 0) {
            int parentIndex = (index - 1) / 2;
            if (compare(heap[index], heap[parentIndex])) {
                swap(index, parentIndex);
                index = parentIndex;
            } else {
                break;
            }
        }
    }

    private void downHeap(int index) {
        while (true) {
            int left = 2 * index + 1;
            int right = 2 * index + 2;
            int target = index;

            if (left < size && compare(heap[left], heap[target])) {
                target = left;
            }
            if (right < size && compare(heap[right], heap[target])) {
                target = right;
            }
            if (target == index) break;

            swap(index, target);
            index = target;
        }
    }

    private void reheapify(int index) {
        if (index > 0 && compare(heap[index], heap[(index - 1) / 2])) {
            upHeap(index);
        } else {
            downHeap(index);
        }
    }

    private boolean compare(Entry<K, V> a, Entry<K, V> b) {
        if (isMinHeap) {
            return a.key.compareTo(b.key) < 0;
        } else {
            return a.key.compareTo(b.key) > 0;
        }
    }

    private void swap(int i, int j) {
        Entry<K, V> temp = heap[i];
        heap[i] = heap[j];
        heap[j] = temp;
    }

    @Override
    public String toString() {
        return Arrays.toString(Arrays.copyOf(heap, size));
    }


}

package ug.ucu.algorithmexplorer.datastructures;

/**
 * A fixed-capacity circular queue implemented using a plain array.
 *
 * The queue follows the First In, First Out policy. Front and rear
 * indices wrap around the array so that positions freed by dequeue
 * operations can be reused.
 *
 * Enqueue, dequeue, front, size, isEmpty and isFull run in O(1).
 * Display and array-copy operations run in O(n).
 *
 * @param <T> type of element stored in the queue
 */
public class CircularQueue<T> {

    private final Object[] elements;
    private int frontIndex;
    private int rearIndex;
    private int elementCount;

    /**
     * Creates an empty circular queue with a fixed capacity.
     *
     * @param capacity maximum number of elements
     */
    public CircularQueue(int capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException(
                    "Queue capacity must be greater than zero."
            );
        }

        elements = new Object[capacity];
        frontIndex = 0;
        rearIndex = -1;
        elementCount = 0;
    }

    /**
     * Adds an element at the rear of the circular queue.
     *
     * Time complexity: O(1)
     *
     * @param value element to add
     */
    public void enqueue(T value) {
        if (value == null) {
            throw new IllegalArgumentException(
                    "The queue cannot store a null value."
            );
        }

        if (isFull()) {
            throw new IllegalStateException(
                    "Queue is full: no free circular-array "
                            + "position is available."
            );
        }

        rearIndex = nextIndex(rearIndex);
        elements[rearIndex] = value;
        elementCount++;
    }

    /**
     * Removes and returns the element at the front.
     *
     * Time complexity: O(1)
     *
     * @return removed front element
     */
    public T dequeue() {
        ensureNotEmpty(
                "Queue underflow: cannot dequeue from an empty queue."
        );

        T removedValue = elementAt(frontIndex);

        elements[frontIndex] = null;
        frontIndex = nextIndex(frontIndex);
        elementCount--;

        return removedValue;
    }

    /**
     * Returns the front element without removing it.
     *
     * Time complexity: O(1)
     *
     * @return current front element
     */
    public T front() {
        ensureNotEmpty(
                "Queue underflow: an empty queue has no front element."
        );

        return elementAt(frontIndex);
    }

    /**
     * Returns the number of active elements.
     *
     * Time complexity: O(1)
     *
     * @return number of queued elements
     */
    public int size() {
        return elementCount;
    }

    /**
     * Returns the fixed queue capacity.
     *
     * Time complexity: O(1)
     *
     * @return maximum number of elements
     */
    public int capacity() {
        return elements.length;
    }

    /**
     * Checks whether the queue contains no elements.
     *
     * Time complexity: O(1)
     *
     * @return true when empty
     */
    public boolean isEmpty() {
        return elementCount == 0;
    }

    /**
     * Checks whether every array position is occupied.
     *
     * Time complexity: O(1)
     *
     * @return true when the queue has reached capacity
     */
    public boolean isFull() {
        return elementCount == elements.length;
    }

    /**
     * Returns the current front array index.
     *
     * The index remains available for visualisation even when the
     * queue becomes empty.
     *
     * @return current front index
     */
    public int frontIndex() {
        return frontIndex;
    }

    /**
     * Returns the current rear array index.
     *
     * @return rear index, or -1 before the first enqueue
     */
    public int rearIndex() {
        return rearIndex;
    }

    /**
     * Checks whether the rear has wrapped behind the front.
     *
     * This helps the interface explain circular behaviour.
     *
     * @return true when active data crosses the end of the array
     */
    public boolean hasWrapped() {
        return !isEmpty() && rearIndex < frontIndex;
    }

    /**
     * Clears every array position and restores the initial indices.
     *
     * Time complexity: O(n)
     */
    public void clear() {
        for (int index = 0; index < elements.length; index++) {
            elements[index] = null;
        }

        frontIndex = 0;
        rearIndex = -1;
        elementCount = 0;
    }

    /**
     * Returns active elements in FIFO order from front to rear.
     *
     * The returned array is independent from internal storage.
     *
     * Time complexity: O(n)
     *
     * @return active queue elements
     */
    public Object[] toArray() {
        Object[] result = new Object[elementCount];

        for (int offset = 0; offset < elementCount; offset++) {
            int physicalIndex =
                    (frontIndex + offset) % elements.length;

            result[offset] = elements[physicalIndex];
        }

        return result;
    }

    /**
     * Returns a copy of every physical array position.
     *
     * Empty positions contain null. This is useful for drawing the
     * fixed array and displaying front and rear indices.
     *
     * Time complexity: O(n)
     *
     * @return copy of the complete internal array
     */
    public Object[] storageSnapshot() {
        Object[] snapshot = new Object[elements.length];

        for (int index = 0; index < elements.length; index++) {
            snapshot[index] = elements[index];
        }

        return snapshot;
    }

    /**
     * Displays active elements in FIFO order.
     *
     * Time complexity: O(n)
     *
     * @return readable queue contents
     */
    public String display() {
        if (isEmpty()) {
            return "[]";
        }

        StringBuilder text = new StringBuilder("[ ");

        for (int offset = 0; offset < elementCount; offset++) {
            int physicalIndex =
                    (frontIndex + offset) % elements.length;

            text.append(elements[physicalIndex]);

            if (offset < elementCount - 1) {
                text.append(", ");
            }
        }

        text.append(" ]");

        return text.toString();
    }

    private int nextIndex(int currentIndex) {
        return (currentIndex + 1) % elements.length;
    }

    private void ensureNotEmpty(String message) {
        if (isEmpty()) {
            throw new IllegalStateException(message);
        }
    }

    @SuppressWarnings("unchecked")
    private T elementAt(int index) {
        return (T) elements[index];
    }
}
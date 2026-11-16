package ug.ucu.algorithmexplorer.datastructures;

/**
 * A fixed-capacity linear queue implemented using a plain array.
 *
 * The queue follows the First In, First Out policy.
 *
 * Enqueue, dequeue, front, size, isEmpty and isFull run in O(1).
 * Display and array-copy operations run in O(n).
 *
 * Dequeued positions are not reused. This deliberately demonstrates
 * the false-full limitation of a linear array queue.
 *
 * @param <T> type of element stored in the queue
 */
public class LinearQueue<T> {

    private final Object[] elements;
    private int frontIndex;
    private int rearIndex;
    private int elementCount;

    /**
     * Creates an empty linear queue with a fixed capacity.
     *
     * @param capacity maximum array capacity
     */
    public LinearQueue(int capacity) {
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
     * Adds an element at the rear of the queue.
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
                    "Queue is full: the rear has reached the end "
                            + "of the linear array."
            );
        }

        rearIndex++;
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
        frontIndex++;
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
     * Returns the fixed array capacity.
     *
     * Time complexity: O(1)
     *
     * @return queue capacity
     */
    public int capacity() {
        return elements.length;
    }

    /**
     * Checks whether the queue contains no active elements.
     *
     * Time complexity: O(1)
     *
     * @return true when empty
     */
    public boolean isEmpty() {
        return elementCount == 0;
    }

    /**
     * Checks whether the rear has reached the final array position.
     *
     * In a linear queue, this can be true even when positions before
     * the front index are empty. That situation is false-full.
     *
     * Time complexity: O(1)
     *
     * @return true when enqueue cannot move the rear farther right
     */
    public boolean isFull() {
        return rearIndex == elements.length - 1;
    }

    /**
     * Checks whether the queue is reporting full while containing
     * fewer active elements than its capacity.
     *
     * Time complexity: O(1)
     *
     * @return true when the linear queue has a false-full condition
     */
    public boolean hasFalseFullCondition() {
        return isFull() && elementCount < elements.length;
    }

    /**
     * Returns the number of unused positions before the front.
     *
     * These positions cannot be reused by this linear queue until
     * the queue is cleared.
     *
     * Time complexity: O(1)
     *
     * @return number of wasted array positions
     */
    public int wastedPositions() {
        return frontIndex;
    }

    /**
     * Returns the current front array index.
     *
     * @return front index
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
     * Clears all array positions and restores the queue indices.
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
     * Returns active queue elements from front to rear.
     *
     * The returned array is independent of the internal storage.
     *
     * Time complexity: O(n)
     *
     * @return active elements in queue order
     */
    public Object[] toArray() {
        Object[] result = new Object[elementCount];

        for (int offset = 0; offset < elementCount; offset++) {
            result[offset] = elements[frontIndex + offset];
        }

        return result;
    }

    /**
     * Returns a snapshot of every physical array position.
     *
     * Empty positions contain null. This is useful for showing the
     * false-full problem in the interface.
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
     * Displays active values from front to rear.
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

        for (int index = frontIndex;
                index <= rearIndex;
                index++) {

            text.append(elements[index]);

            if (index < rearIndex) {
                text.append(", ");
            }
        }

        text.append(" ]");

        return text.toString();
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
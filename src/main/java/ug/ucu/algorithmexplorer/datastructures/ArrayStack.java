package ug.ucu.algorithmexplorer.datastructures;

/**
 * A fixed-capacity array-based stack implemented from first principles.
 *
 * The stack follows the Last In, First Out policy.
 * Push, pop, peek, size, isEmpty and isFull run in O(1) time.
 * Display and array-copy operations run in O(n) time.
 *
 * @param <T> type of element stored in the stack
 */
public class ArrayStack<T> {

    private final Object[] elements;
    private int top;

    /**
     * Creates an empty stack with a fixed capacity.
     *
     * @param capacity maximum number of elements
     */
    public ArrayStack(int capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException(
                    "Stack capacity must be greater than zero."
            );
        }

        elements = new Object[capacity];
        top = -1;
    }

    /**
     * Adds an element to the top of the stack.
     *
     * Time complexity: O(1)
     *
     * @param value element to add
     */
    public void push(T value) {
        if (value == null) {
            throw new IllegalArgumentException(
                    "The stack cannot store a null value."
            );
        }

        if (isFull()) {
            throw new IllegalStateException(
                    "Stack overflow: the stack is full."
            );
        }

        top++;
        elements[top] = value;
    }

    /**
     * Removes and returns the top element.
     *
     * Time complexity: O(1)
     *
     * @return removed top element
     */
    public T pop() {
        ensureNotEmpty(
                "Stack underflow: cannot pop from an empty stack."
        );

        T removedValue = elementAt(top);

        elements[top] = null;
        top--;

        return removedValue;
    }

    /**
     * Returns the top element without removing it.
     *
     * Time complexity: O(1)
     *
     * @return current top element
     */
    public T peek() {
        ensureNotEmpty(
                "Stack underflow: cannot peek at an empty stack."
        );

        return elementAt(top);
    }

    /**
     * Returns the number of stored elements.
     *
     * Time complexity: O(1)
     *
     * @return stack size
     */
    public int size() {
        return top + 1;
    }

    /**
     * Returns the fixed capacity of the stack.
     *
     * Time complexity: O(1)
     *
     * @return maximum number of elements
     */
    public int capacity() {
        return elements.length;
    }

    /**
     * Checks whether the stack contains no elements.
     *
     * Time complexity: O(1)
     *
     * @return true when the stack is empty
     */
    public boolean isEmpty() {
        return top == -1;
    }

    /**
     * Checks whether the stack has reached its capacity.
     *
     * Time complexity: O(1)
     *
     * @return true when the stack is full
     */
    public boolean isFull() {
        return top == elements.length - 1;
    }

    /**
     * Removes every element without changing the stack capacity.
     *
     * Time complexity: O(n)
     */
    public void clear() {
        while (top >= 0) {
            elements[top] = null;
            top--;
        }
    }

    /**
     * Returns the current elements from bottom to top.
     *
     * The returned array is independent from the internal storage.
     *
     * Time complexity: O(n)
     *
     * @return stack contents from bottom to top
     */
    public Object[] toArray() {
        Object[] result = new Object[size()];

        for (int index = 0; index <= top; index++) {
            result[index] = elements[index];
        }

        return result;
    }

    /**
     * Returns a readable representation from bottom to top.
     *
     * Time complexity: O(n)
     *
     * @return stack contents
     */
    public String display() {
        if (isEmpty()) {
            return "[]";
        }

        StringBuilder displayText = new StringBuilder("[ ");

        for (int index = 0; index <= top; index++) {
            displayText.append(elements[index]);

            if (index < top) {
                displayText.append(", ");
            }
        }

        displayText.append(" ]");

        return displayText.toString();
    }

    /**
     * Returns the zero-based index of the current top position.
     *
     * This will help the visual interface show the array position.
     *
     * @return top index, or -1 when empty
     */
    public int topIndex() {
        return top;
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
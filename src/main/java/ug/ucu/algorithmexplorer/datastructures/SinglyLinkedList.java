package ug.ucu.algorithmexplorer.datastructures;

import java.util.Objects;

/**
 * A singly linked list implemented using custom node objects.
 *
 * The class does not use Java's built-in linked-list collections.
 * Each node contains a value and an explicit reference to the next node.
 *
 * @param <T> type of value stored in the list
 */
public class SinglyLinkedList<T> {

    private Node<T> head;
    private Node<T> tail;
    private int elementCount;

    /**
     * Inserts a value at the beginning.
     *
     * Time complexity: O(1)
     *
     * @param value value to insert
     */
    public void insertAtBeginning(T value) {
        validateValue(value);

        Node<T> newNode = new Node<>(value);
        newNode.next = head;
        head = newNode;

        if (tail == null) {
            tail = newNode;
        }

        elementCount++;
    }

    /**
     * Inserts a value at the end.
     *
     * Time complexity: O(1)
     *
     * @param value value to insert
     */
    public void insertAtEnd(T value) {
        validateValue(value);

        Node<T> newNode = new Node<>(value);

        if (isEmpty()) {
            head = newNode;
            tail = newNode;
        } else {
            tail.next = newNode;
            tail = newNode;
        }

        elementCount++;
    }

    /**
     * Inserts a value before the first occurrence of a target.
     *
     * Time complexity: O(n)
     *
     * @param target existing value to locate
     * @param value new value to insert
     */
    public void insertBefore(T target, T value) {
        validateTarget(target);
        validateValue(value);

        if (isEmpty()) {
            throw targetNotFound(target);
        }

        if (valuesEqual(head.value, target)) {
            insertAtBeginning(value);
            return;
        }

        Node<T> previous = head;
        Node<T> current = head.next;

        while (current != null
                && !valuesEqual(current.value, target)) {
            previous = current;
            current = current.next;
        }

        if (current == null) {
            throw targetNotFound(target);
        }

        Node<T> newNode = new Node<>(value);
        newNode.next = current;
        previous.next = newNode;

        elementCount++;
    }

    /**
     * Inserts a value after the first occurrence of a target.
     *
     * Time complexity: O(n)
     *
     * @param target existing value to locate
     * @param value new value to insert
     */
    public void insertAfter(T target, T value) {
        validateTarget(target);
        validateValue(value);

        Node<T> targetNode = findNode(target);

        if (targetNode == null) {
            throw targetNotFound(target);
        }

        Node<T> newNode = new Node<>(value);
        newNode.next = targetNode.next;
        targetNode.next = newNode;

        if (tail == targetNode) {
            tail = newNode;
        }

        elementCount++;
    }

    /**
     * Deletes and returns the first value.
     *
     * Time complexity: O(1)
     *
     * @return deleted value
     */
    public T deleteAtBeginning() {
        ensureNotEmpty(
                "Cannot delete from the beginning of an empty list."
        );

        T removedValue = head.value;
        head = head.next;
        elementCount--;

        if (head == null) {
            tail = null;
        }

        return removedValue;
    }

    /**
     * Deletes and returns the last value.
     *
     * Time complexity: O(n), because a singly linked list must locate
     * the node immediately before the tail.
     *
     * @return deleted value
     */
    public T deleteAtEnd() {
        ensureNotEmpty(
                "Cannot delete from the end of an empty list."
        );

        if (head == tail) {
            return deleteAtBeginning();
        }

        Node<T> current = head;

        while (current.next != tail) {
            current = current.next;
        }

        T removedValue = tail.value;

        current.next = null;
        tail = current;
        elementCount--;

        return removedValue;
    }

    /**
     * Deletes and returns the node before the first target occurrence.
     *
     * Time complexity: O(n)
     *
     * @param target value whose preceding node should be deleted
     * @return deleted value
     */
    public T deleteBefore(T target) {
        validateTarget(target);
        ensureNotEmpty(
                "Cannot delete before a value in an empty list."
        );

        if (valuesEqual(head.value, target)) {
            throw new IllegalStateException(
                    "Cannot delete before the head element."
            );
        }

        if (head.next != null
                && valuesEqual(head.next.value, target)) {
            return deleteAtBeginning();
        }

        Node<T> beforePrevious = head;
        Node<T> previous = head.next;
        Node<T> current = previous == null
                ? null
                : previous.next;

        while (current != null
                && !valuesEqual(current.value, target)) {
            beforePrevious = previous;
            previous = current;
            current = current.next;
        }

        if (current == null) {
            throw targetNotFound(target);
        }

        beforePrevious.next = current;
        elementCount--;

        return previous.value;
    }

    /**
     * Deletes and returns the node after the first target occurrence.
     *
     * Time complexity: O(n)
     *
     * @param target value whose following node should be deleted
     * @return deleted value
     */
    public T deleteAfter(T target) {
        validateTarget(target);
        ensureNotEmpty(
                "Cannot delete after a value in an empty list."
        );

        Node<T> targetNode = findNode(target);

        if (targetNode == null) {
            throw targetNotFound(target);
        }

        if (targetNode.next == null) {
            throw new IllegalStateException(
                    "Cannot delete after the tail element."
            );
        }

        Node<T> removedNode = targetNode.next;
        targetNode.next = removedNode.next;

        if (tail == removedNode) {
            tail = targetNode;
        }

        elementCount--;

        return removedNode.value;
    }

    /**
     * Deletes the first occurrence of a specified value.
     *
     * Time complexity: O(n)
     *
     * @param value value to delete
     * @return deleted value
     */
    public T deleteValue(T value) {
        validateTarget(value);
        ensureNotEmpty(
                "Cannot delete a value from an empty list."
        );

        if (valuesEqual(head.value, value)) {
            return deleteAtBeginning();
        }

        Node<T> previous = head;
        Node<T> current = head.next;

        while (current != null
                && !valuesEqual(current.value, value)) {
            previous = current;
            current = current.next;
        }

        if (current == null) {
            throw targetNotFound(value);
        }

        previous.next = current.next;

        if (tail == current) {
            tail = previous;
        }

        elementCount--;

        return current.value;
    }

    /**
     * Returns the zero-based index of the first matching value.
     *
     * Time complexity: O(n)
     *
     * @param value value to locate
     * @return index, or -1 when not found
     */
    public int search(T value) {
        validateTarget(value);

        Node<T> current = head;
        int index = 0;

        while (current != null) {
            if (valuesEqual(current.value, value)) {
                return index;
            }

            current = current.next;
            index++;
        }

        return -1;
    }

    /**
     * Checks whether the list contains a value.
     *
     * Time complexity: O(n)
     *
     * @param value value to locate
     * @return true when found
     */
    public boolean contains(T value) {
        return search(value) >= 0;
    }

    /**
     * Returns the first value.
     *
     * Time complexity: O(1)
     *
     * @return first value
     */
    public T first() {
        ensureNotEmpty("The list has no first element.");
        return head.value;
    }

    /**
     * Returns the last value.
     *
     * Time complexity: O(1)
     *
     * @return last value
     */
    public T last() {
        ensureNotEmpty("The list has no last element.");
        return tail.value;
    }

    /**
     * Returns the number of stored values.
     *
     * Time complexity: O(1)
     *
     * @return list size
     */
    public int size() {
        return elementCount;
    }

    /**
     * Checks whether the list contains no nodes.
     *
     * Time complexity: O(1)
     *
     * @return true when empty
     */
    public boolean isEmpty() {
        return elementCount == 0;
    }

    /**
     * Removes all nodes.
     *
     * Time complexity: O(1) for unlinking the structure.
     */
    public void clear() {
        head = null;
        tail = null;
        elementCount = 0;
    }

    /**
     * Returns the values in forward order.
     *
     * Time complexity: O(n)
     *
     * @return independent array snapshot
     */
    public Object[] toArray() {
        Object[] result = new Object[elementCount];

        Node<T> current = head;
        int index = 0;

        while (current != null) {
            result[index] = current.value;
            current = current.next;
            index++;
        }

        return result;
    }

    /**
     * Displays the linked structure in forward order.
     *
     * Time complexity: O(n)
     *
     * @return readable node-and-arrow representation
     */
    public String displayForward() {
        if (isEmpty()) {
            return "HEAD -> null";
        }

        StringBuilder text = new StringBuilder("HEAD -> ");

        Node<T> current = head;

        while (current != null) {
            text.append("[ ")
                    .append(current.value)
                    .append(" ] -> ");

            current = current.next;
        }

        text.append("null");

        return text.toString();
    }

    private Node<T> findNode(T value) {
        Node<T> current = head;

        while (current != null) {
            if (valuesEqual(current.value, value)) {
                return current;
            }

            current = current.next;
        }

        return null;
    }

    private boolean valuesEqual(T firstValue, T secondValue) {
        return Objects.equals(firstValue, secondValue);
    }

    private void validateValue(T value) {
        if (value == null) {
            throw new IllegalArgumentException(
                    "The list cannot store a null value."
            );
        }
    }

    private void validateTarget(T target) {
        if (target == null) {
            throw new IllegalArgumentException(
                    "The target value cannot be null."
            );
        }
    }

    private void ensureNotEmpty(String message) {
        if (isEmpty()) {
            throw new IllegalStateException(message);
        }
    }

    private IllegalArgumentException targetNotFound(T target) {
        return new IllegalArgumentException(
                "Target value not found: " + target
        );
    }

    /**
     * A node stores one value and a reference to the next node.
     *
     * @param <T> node value type
     */
    private static final class Node<T> {

        private final T value;
        private Node<T> next;

        Node(T value) {
            this.value = value;
        }
    }
}
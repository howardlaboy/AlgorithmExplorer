package ug.ucu.algorithmexplorer.datastructures;

import java.util.Objects;

/**
 * A doubly linked list implemented using custom node objects.
 *
 * Each node has explicit previous and next references. The class does
 * not use Java's built-in linked-list collections.
 *
 * @param <T> type of value stored in the list
 */
public class DoublyLinkedList<T> {

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

        if (head == null) {
            head = newNode;
            tail = newNode;
        } else {
            head.previous = newNode;
            head = newNode;
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
        newNode.previous = tail;

        if (tail == null) {
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

        Node<T> targetNode = findNode(target);

        if (targetNode == null) {
            throw targetNotFound(target);
        }

        if (targetNode == head) {
            insertAtBeginning(value);
            return;
        }

        Node<T> newNode = new Node<>(value);
        Node<T> previousNode = targetNode.previous;

        newNode.previous = previousNode;
        newNode.next = targetNode;

        previousNode.next = newNode;
        targetNode.previous = newNode;

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

        if (targetNode == tail) {
            insertAtEnd(value);
            return;
        }

        Node<T> newNode = new Node<>(value);
        Node<T> nextNode = targetNode.next;

        newNode.previous = targetNode;
        newNode.next = nextNode;

        targetNode.next = newNode;
        nextNode.previous = newNode;

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

        if (head == tail) {
            head = null;
            tail = null;
        } else {
            head = head.next;
            head.previous = null;
        }

        elementCount--;

        return removedValue;
    }

    /**
     * Deletes and returns the final value.
     *
     * Time complexity: O(1)
     *
     * @return deleted value
     */
    public T deleteAtEnd() {
        ensureNotEmpty(
                "Cannot delete from the end of an empty list."
        );

        T removedValue = tail.value;

        if (head == tail) {
            head = null;
            tail = null;
        } else {
            tail = tail.previous;
            tail.next = null;
        }

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

        Node<T> targetNode = findNode(target);

        if (targetNode == null) {
            throw targetNotFound(target);
        }

        if (targetNode == head) {
            throw new IllegalStateException(
                    "Cannot delete before the head element."
            );
        }

        Node<T> nodeToDelete = targetNode.previous;

        if (nodeToDelete == head) {
            return deleteAtBeginning();
        }

        unlinkNode(nodeToDelete);

        return nodeToDelete.value;
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

        if (targetNode == tail) {
            throw new IllegalStateException(
                    "Cannot delete after the tail element."
            );
        }

        Node<T> nodeToDelete = targetNode.next;

        if (nodeToDelete == tail) {
            return deleteAtEnd();
        }

        unlinkNode(nodeToDelete);

        return nodeToDelete.value;
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

        Node<T> nodeToDelete = findNode(value);

        if (nodeToDelete == null) {
            throw targetNotFound(value);
        }

        if (nodeToDelete == head) {
            return deleteAtBeginning();
        }

        if (nodeToDelete == tail) {
            return deleteAtEnd();
        }

        unlinkNode(nodeToDelete);

        return nodeToDelete.value;
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
     * Unlinks the complete list.
     *
     * Time complexity: O(1)
     */
    public void clear() {
        head = null;
        tail = null;
        elementCount = 0;
    }

    /**
     * Returns values from head to tail.
     *
     * Time complexity: O(n)
     *
     * @return independent forward array snapshot
     */
    public Object[] toArrayForward() {
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
     * Returns values from tail to head.
     *
     * Time complexity: O(n)
     *
     * @return independent backward array snapshot
     */
    public Object[] toArrayBackward() {
        Object[] result = new Object[elementCount];

        Node<T> current = tail;
        int index = 0;

        while (current != null) {
            result[index] = current.value;
            current = current.previous;
            index++;
        }

        return result;
    }

    /**
     * Displays the linked structure from head to tail.
     *
     * Time complexity: O(n)
     *
     * @return readable forward representation
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
                    .append(" ]");

            if (current.next != null) {
                text.append(" <-> ");
            }

            current = current.next;
        }

        text.append(" -> null");

        return text.toString();
    }

    /**
     * Displays the linked structure from tail to head.
     *
     * Time complexity: O(n)
     *
     * @return readable backward representation
     */
    public String displayBackward() {
        if (isEmpty()) {
            return "TAIL -> null";
        }

        StringBuilder text = new StringBuilder("TAIL -> ");

        Node<T> current = tail;

        while (current != null) {
            text.append("[ ")
                    .append(current.value)
                    .append(" ]");

            if (current.previous != null) {
                text.append(" <-> ");
            }

            current = current.previous;
        }

        text.append(" -> null");

        return text.toString();
    }

    /**
     * Checks the internal previous and next links.
     *
     * This helper supports testing and later diagnostic display.
     *
     * @return true when all links and stored state are consistent
     */
    public boolean hasConsistentLinks() {
        if (elementCount == 0) {
            return head == null && tail == null;
        }

        if (head == null || tail == null) {
            return false;
        }

        if (head.previous != null || tail.next != null) {
            return false;
        }

        int forwardCount = 0;
        Node<T> current = head;
        Node<T> expectedPrevious = null;
        Node<T> finalForwardNode = null;

        while (current != null) {
            if (current.previous != expectedPrevious) {
                return false;
            }

            expectedPrevious = current;
            finalForwardNode = current;
            current = current.next;
            forwardCount++;
        }

        if (finalForwardNode != tail) {
            return false;
        }

        int backwardCount = 0;
        current = tail;
        Node<T> expectedNext = null;
        Node<T> finalBackwardNode = null;

        while (current != null) {
            if (current.next != expectedNext) {
                return false;
            }

            expectedNext = current;
            finalBackwardNode = current;
            current = current.previous;
            backwardCount++;
        }

        return finalBackwardNode == head
                && forwardCount == elementCount
                && backwardCount == elementCount;
    }

    private void unlinkNode(Node<T> node) {
        Node<T> previousNode = node.previous;
        Node<T> nextNode = node.next;

        previousNode.next = nextNode;
        nextNode.previous = previousNode;

        node.previous = null;
        node.next = null;

        elementCount--;
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
     * Stores one value with explicit previous and next references.
     *
     * @param <T> node value type
     */
    private static final class Node<T> {

        private final T value;
        private Node<T> previous;
        private Node<T> next;

        Node(T value) {
            this.value = value;
        }
    }
}
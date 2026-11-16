package ug.ucu.algorithmexplorer.controllers;

import ug.ucu.algorithmexplorer.datastructures.ArrayStack;
import ug.ucu.algorithmexplorer.datastructures.BalancedBracketChecker;
import ug.ucu.algorithmexplorer.datastructures.CircularQueue;
import ug.ucu.algorithmexplorer.datastructures.DoublyLinkedList;
import ug.ucu.algorithmexplorer.datastructures.LinearQueue;
import ug.ucu.algorithmexplorer.datastructures.SinglyLinkedList;
import ug.ucu.algorithmexplorer.models.BracketCheckResult;

/**
 * Connects Data Structures Lab interface actions to the custom
 * data-structure implementations.
 *
 * The controller stores the current structures but contains no Swing
 * code and does not implement the core data-structure operations.
 */
public class DataStructuresController {

    private ArrayStack<Integer> stack;
    private LinearQueue<Integer> linearQueue;
    private CircularQueue<Integer> circularQueue;

    private final SinglyLinkedList<Integer> singlyLinkedList;
    private final DoublyLinkedList<Integer> doublyLinkedList;
    private final BalancedBracketChecker bracketChecker;

    /**
     * Creates a controller with empty linked lists and no capacity-based
     * structures. The stack and queues are created when the user selects
     * their capacities.
     */
    public DataStructuresController() {
        singlyLinkedList = new SinglyLinkedList<>();
        doublyLinkedList = new DoublyLinkedList<>();
        bracketChecker = new BalancedBracketChecker();
    }

    // ------------------------------------------------------------
    // Stack operations
    // ------------------------------------------------------------

    /**
     * Creates or replaces the current fixed-capacity stack.
     *
     * @param capacity user-selected capacity
     */
    public void createStack(int capacity) {
        stack = new ArrayStack<>(capacity);
    }

    public void pushStack(int value) {
        requireStack();
        stack.push(value);
    }

    public int popStack() {
        requireStack();
        return stack.pop();
    }

    public int peekStack() {
        requireStack();
        return stack.peek();
    }

    public int stackSize() {
        requireStack();
        return stack.size();
    }

    public int stackCapacity() {
        requireStack();
        return stack.capacity();
    }

    public boolean isStackEmpty() {
        requireStack();
        return stack.isEmpty();
    }

    public boolean isStackFull() {
        requireStack();
        return stack.isFull();
    }

    public int stackTopIndex() {
        requireStack();
        return stack.topIndex();
    }

    public String displayStack() {
        requireStack();
        return stack.display();
    }

    public Object[] stackSnapshot() {
        requireStack();
        return stack.toArray();
    }

    public void clearStack() {
        requireStack();
        stack.clear();
    }

    public boolean hasStack() {
        return stack != null;
    }

    // ------------------------------------------------------------
    // Balanced-bracket application
    // ------------------------------------------------------------

    public BracketCheckResult checkBrackets(String expression) {
        return bracketChecker.check(expression);
    }

    // ------------------------------------------------------------
    // Linear queue operations
    // ------------------------------------------------------------

    /**
     * Creates or replaces the current fixed-capacity linear queue.
     *
     * @param capacity user-selected capacity
     */
    public void createLinearQueue(int capacity) {
        linearQueue = new LinearQueue<>(capacity);
    }

    public void enqueueLinear(int value) {
        requireLinearQueue();
        linearQueue.enqueue(value);
    }

    public int dequeueLinear() {
        requireLinearQueue();
        return linearQueue.dequeue();
    }

    public int frontLinear() {
        requireLinearQueue();
        return linearQueue.front();
    }

    public int linearQueueSize() {
        requireLinearQueue();
        return linearQueue.size();
    }

    public int linearQueueCapacity() {
        requireLinearQueue();
        return linearQueue.capacity();
    }

    public boolean isLinearQueueEmpty() {
        requireLinearQueue();
        return linearQueue.isEmpty();
    }

    public boolean isLinearQueueFull() {
        requireLinearQueue();
        return linearQueue.isFull();
    }

    public boolean hasLinearFalseFullCondition() {
        requireLinearQueue();
        return linearQueue.hasFalseFullCondition();
    }

    public int linearWastedPositions() {
        requireLinearQueue();
        return linearQueue.wastedPositions();
    }

    public int linearFrontIndex() {
        requireLinearQueue();
        return linearQueue.frontIndex();
    }

    public int linearRearIndex() {
        requireLinearQueue();
        return linearQueue.rearIndex();
    }

    public String displayLinearQueue() {
        requireLinearQueue();
        return linearQueue.display();
    }

    public Object[] linearQueueSnapshot() {
        requireLinearQueue();
        return linearQueue.toArray();
    }

    public Object[] linearQueueStorageSnapshot() {
        requireLinearQueue();
        return linearQueue.storageSnapshot();
    }

    public void clearLinearQueue() {
        requireLinearQueue();
        linearQueue.clear();
    }

    public boolean hasLinearQueue() {
        return linearQueue != null;
    }

    // ------------------------------------------------------------
    // Circular queue operations
    // ------------------------------------------------------------

    /**
     * Creates or replaces the current fixed-capacity circular queue.
     *
     * @param capacity user-selected capacity
     */
    public void createCircularQueue(int capacity) {
        circularQueue = new CircularQueue<>(capacity);
    }

    public void enqueueCircular(int value) {
        requireCircularQueue();
        circularQueue.enqueue(value);
    }

    public int dequeueCircular() {
        requireCircularQueue();
        return circularQueue.dequeue();
    }

    public int frontCircular() {
        requireCircularQueue();
        return circularQueue.front();
    }

    public int circularQueueSize() {
        requireCircularQueue();
        return circularQueue.size();
    }

    public int circularQueueCapacity() {
        requireCircularQueue();
        return circularQueue.capacity();
    }

    public boolean isCircularQueueEmpty() {
        requireCircularQueue();
        return circularQueue.isEmpty();
    }

    public boolean isCircularQueueFull() {
        requireCircularQueue();
        return circularQueue.isFull();
    }

    public boolean hasCircularQueueWrapped() {
        requireCircularQueue();
        return circularQueue.hasWrapped();
    }

    public int circularFrontIndex() {
        requireCircularQueue();
        return circularQueue.frontIndex();
    }

    public int circularRearIndex() {
        requireCircularQueue();
        return circularQueue.rearIndex();
    }

    public String displayCircularQueue() {
        requireCircularQueue();
        return circularQueue.display();
    }

    public Object[] circularQueueSnapshot() {
        requireCircularQueue();
        return circularQueue.toArray();
    }

    public Object[] circularQueueStorageSnapshot() {
        requireCircularQueue();
        return circularQueue.storageSnapshot();
    }

    public void clearCircularQueue() {
        requireCircularQueue();
        circularQueue.clear();
    }

    public boolean hasCircularQueue() {
        return circularQueue != null;
    }

    // ------------------------------------------------------------
    // Singly linked-list operations
    // ------------------------------------------------------------

    public void insertSinglyAtBeginning(int value) {
        singlyLinkedList.insertAtBeginning(value);
    }

    public void insertSinglyAtEnd(int value) {
        singlyLinkedList.insertAtEnd(value);
    }

    public void insertSinglyBefore(int target, int value) {
        singlyLinkedList.insertBefore(target, value);
    }

    public void insertSinglyAfter(int target, int value) {
        singlyLinkedList.insertAfter(target, value);
    }

    public int deleteSinglyAtBeginning() {
        return singlyLinkedList.deleteAtBeginning();
    }

    public int deleteSinglyAtEnd() {
        return singlyLinkedList.deleteAtEnd();
    }

    public int deleteSinglyBefore(int target) {
        return singlyLinkedList.deleteBefore(target);
    }

    public int deleteSinglyAfter(int target) {
        return singlyLinkedList.deleteAfter(target);
    }

    public int deleteSinglyValue(int value) {
        return singlyLinkedList.deleteValue(value);
    }

    public int searchSingly(int value) {
        return singlyLinkedList.search(value);
    }

    public int singlyListSize() {
        return singlyLinkedList.size();
    }

    public boolean isSinglyListEmpty() {
        return singlyLinkedList.isEmpty();
    }

    public String displaySinglyForward() {
        return singlyLinkedList.displayForward();
    }

    public Object[] singlyListSnapshot() {
        return singlyLinkedList.toArray();
    }

    public void clearSinglyList() {
        singlyLinkedList.clear();
    }

    // ------------------------------------------------------------
    // Doubly linked-list operations
    // ------------------------------------------------------------

    public void insertDoublyAtBeginning(int value) {
        doublyLinkedList.insertAtBeginning(value);
    }

    public void insertDoublyAtEnd(int value) {
        doublyLinkedList.insertAtEnd(value);
    }

    public void insertDoublyBefore(int target, int value) {
        doublyLinkedList.insertBefore(target, value);
    }

    public void insertDoublyAfter(int target, int value) {
        doublyLinkedList.insertAfter(target, value);
    }

    public int deleteDoublyAtBeginning() {
        return doublyLinkedList.deleteAtBeginning();
    }

    public int deleteDoublyAtEnd() {
        return doublyLinkedList.deleteAtEnd();
    }

    public int deleteDoublyBefore(int target) {
        return doublyLinkedList.deleteBefore(target);
    }

    public int deleteDoublyAfter(int target) {
        return doublyLinkedList.deleteAfter(target);
    }

    public int deleteDoublyValue(int value) {
        return doublyLinkedList.deleteValue(value);
    }

    public int searchDoubly(int value) {
        return doublyLinkedList.search(value);
    }

    public int doublyListSize() {
        return doublyLinkedList.size();
    }

    public boolean isDoublyListEmpty() {
        return doublyLinkedList.isEmpty();
    }

    public String displayDoublyForward() {
        return doublyLinkedList.displayForward();
    }

    public String displayDoublyBackward() {
        return doublyLinkedList.displayBackward();
    }

    public Object[] doublyListForwardSnapshot() {
        return doublyLinkedList.toArrayForward();
    }

    public Object[] doublyListBackwardSnapshot() {
        return doublyLinkedList.toArrayBackward();
    }

    public boolean hasConsistentDoublyLinks() {
        return doublyLinkedList.hasConsistentLinks();
    }

    public void clearDoublyList() {
        doublyLinkedList.clear();
    }

    // ------------------------------------------------------------
    // Required structure checks
    // ------------------------------------------------------------

    private void requireStack() {
        if (stack == null) {
            throw new IllegalStateException(
                    "Create the stack before performing an operation."
            );
        }
    }

    private void requireLinearQueue() {
        if (linearQueue == null) {
            throw new IllegalStateException(
                    "Create the linear queue before performing "
                            + "an operation."
            );
        }
    }

    private void requireCircularQueue() {
        if (circularQueue == null) {
            throw new IllegalStateException(
                    "Create the circular queue before performing "
                            + "an operation."
            );
        }
    }
}
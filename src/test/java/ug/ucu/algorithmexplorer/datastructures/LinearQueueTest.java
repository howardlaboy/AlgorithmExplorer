package ug.ucu.algorithmexplorer.datastructures;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LinearQueueTest {

    private LinearQueue<Integer> queue;

    @BeforeEach
    void setUp() {
        queue = new LinearQueue<>(5);
    }

    @Test
    void newQueueHasCorrectInitialState() {
        assertTrue(queue.isEmpty());
        assertFalse(queue.isFull());
        assertFalse(queue.hasFalseFullCondition());

        assertEquals(0, queue.size());
        assertEquals(5, queue.capacity());
        assertEquals(0, queue.frontIndex());
        assertEquals(-1, queue.rearIndex());
        assertEquals(0, queue.wastedPositions());
        assertEquals("[]", queue.display());
    }

    @Test
    void enqueueAddsElementsAtRear() {
        queue.enqueue(10);
        queue.enqueue(20);
        queue.enqueue(30);

        assertEquals(3, queue.size());
        assertEquals(10, queue.front());
        assertEquals(0, queue.frontIndex());
        assertEquals(2, queue.rearIndex());
        assertEquals("[ 10, 20, 30 ]", queue.display());
    }

    @Test
    void dequeueReturnsElementsInFirstInFirstOutOrder() {
        queue.enqueue(10);
        queue.enqueue(20);
        queue.enqueue(30);

        assertEquals(10, queue.dequeue());
        assertEquals(20, queue.dequeue());
        assertEquals(30, queue.dequeue());

        assertTrue(queue.isEmpty());
    }

    @Test
    void frontDoesNotRemoveElement() {
        queue.enqueue(42);

        assertEquals(42, queue.front());
        assertEquals(42, queue.front());
        assertEquals(1, queue.size());
    }

    @Test
    void queueReportsFullWhenRearReachesArrayEnd() {
        queue.enqueue(10);
        queue.enqueue(20);
        queue.enqueue(30);
        queue.enqueue(40);
        queue.enqueue(50);

        assertTrue(queue.isFull());
        assertFalse(queue.hasFalseFullCondition());
        assertEquals(5, queue.size());
        assertEquals(4, queue.rearIndex());
    }

    @Test
    void enqueueOnFullQueueReportsClearMessage() {
        queue.enqueue(10);
        queue.enqueue(20);
        queue.enqueue(30);
        queue.enqueue(40);
        queue.enqueue(50);

        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> queue.enqueue(60)
        );

        assertTrue(
                exception.getMessage().contains("Queue is full")
        );

        assertEquals(5, queue.size());
    }

    @Test
    void demonstratesFalseFullCondition() {
        queue.enqueue(10);
        queue.enqueue(20);
        queue.enqueue(30);
        queue.enqueue(40);
        queue.enqueue(50);

        assertEquals(10, queue.dequeue());
        assertEquals(20, queue.dequeue());
        assertEquals(30, queue.dequeue());

        assertEquals(2, queue.size());
        assertEquals(3, queue.frontIndex());
        assertEquals(4, queue.rearIndex());

        assertTrue(queue.isFull());
        assertTrue(queue.hasFalseFullCondition());
        assertEquals(3, queue.wastedPositions());

        assertArrayEquals(
                new Object[]{null, null, null, 40, 50},
                queue.storageSnapshot()
        );

        assertThrows(
                IllegalStateException.class,
                () -> queue.enqueue(60)
        );
    }

    @Test
    void falseFullCanRemainAfterQueueBecomesEmpty() {
        queue.enqueue(10);
        queue.enqueue(20);
        queue.enqueue(30);
        queue.enqueue(40);
        queue.enqueue(50);

        queue.dequeue();
        queue.dequeue();
        queue.dequeue();
        queue.dequeue();
        queue.dequeue();

        assertTrue(queue.isEmpty());
        assertTrue(queue.isFull());
        assertTrue(queue.hasFalseFullCondition());

        assertEquals(0, queue.size());
        assertEquals(5, queue.frontIndex());
        assertEquals(4, queue.rearIndex());
        assertEquals(5, queue.wastedPositions());
    }

    @Test
    void dequeueOnEmptyQueueReportsUnderflow() {
        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> queue.dequeue()
        );

        assertTrue(
                exception.getMessage().contains("underflow")
        );
    }

    @Test
    void frontOnEmptyQueueReportsUnderflow() {
        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> queue.front()
        );

        assertTrue(
                exception.getMessage().contains("underflow")
        );
    }

    @Test
    void toArrayReturnsActiveElementsOnly() {
        queue.enqueue(10);
        queue.enqueue(20);
        queue.enqueue(30);
        queue.enqueue(40);

        queue.dequeue();
        queue.dequeue();

        assertArrayEquals(
                new Object[]{30, 40},
                queue.toArray()
        );
    }

    @Test
    void returnedArraysAreIndependentSnapshots() {
        queue.enqueue(10);
        queue.enqueue(20);

        Object[] activeSnapshot = queue.toArray();
        Object[] storageSnapshot = queue.storageSnapshot();

        activeSnapshot[0] = 999;
        storageSnapshot[0] = 999;

        assertEquals(10, queue.front());

        assertArrayEquals(
                new Object[]{10, 20},
                queue.toArray()
        );

        assertArrayEquals(
                new Object[]{10, 20, null, null, null},
                queue.storageSnapshot()
        );
    }

    @Test
    void clearRestoresQueueForReuse() {
        queue.enqueue(10);
        queue.enqueue(20);
        queue.enqueue(30);
        queue.enqueue(40);
        queue.enqueue(50);

        queue.dequeue();
        queue.dequeue();

        queue.clear();

        assertTrue(queue.isEmpty());
        assertFalse(queue.isFull());
        assertFalse(queue.hasFalseFullCondition());

        assertEquals(0, queue.frontIndex());
        assertEquals(-1, queue.rearIndex());
        assertEquals(0, queue.wastedPositions());

        assertArrayEquals(
                new Object[]{null, null, null, null, null},
                queue.storageSnapshot()
        );

        queue.enqueue(99);

        assertEquals(99, queue.front());
        assertEquals(1, queue.size());
    }

    @Test
    void constructorRejectsInvalidCapacity() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new LinearQueue<Integer>(0)
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> new LinearQueue<Integer>(-3)
        );
    }

    @Test
    void queueRejectsNullValues() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> queue.enqueue(null)
        );

        assertTrue(exception.getMessage().contains("null"));
        assertTrue(queue.isEmpty());
    }

    @Test
    void sameQueueClassCanStoreStrings() {
        LinearQueue<String> textQueue =
                new LinearQueue<>(3);

        textQueue.enqueue("first");
        textQueue.enqueue("second");

        assertEquals("first", textQueue.dequeue());
        assertEquals("second", textQueue.front());
    }
}
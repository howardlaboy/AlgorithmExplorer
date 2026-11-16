package ug.ucu.algorithmexplorer.datastructures;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CircularQueueTest {

    private CircularQueue<Integer> queue;

    @BeforeEach
    void setUp() {
        queue = new CircularQueue<>(5);
    }

    @Test
    void newQueueHasCorrectInitialState() {
        assertTrue(queue.isEmpty());
        assertFalse(queue.isFull());
        assertFalse(queue.hasWrapped());

        assertEquals(0, queue.size());
        assertEquals(5, queue.capacity());
        assertEquals(0, queue.frontIndex());
        assertEquals(-1, queue.rearIndex());
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
    void queueBecomesFullAtCapacity() {
        queue.enqueue(10);
        queue.enqueue(20);
        queue.enqueue(30);
        queue.enqueue(40);
        queue.enqueue(50);

        assertTrue(queue.isFull());
        assertFalse(queue.isEmpty());
        assertEquals(5, queue.size());
        assertEquals(0, queue.frontIndex());
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
    void rearWrapsToBeginningOfArray() {
        queue.enqueue(10);
        queue.enqueue(20);
        queue.enqueue(30);
        queue.enqueue(40);
        queue.enqueue(50);

        assertEquals(10, queue.dequeue());
        assertEquals(20, queue.dequeue());
        assertEquals(30, queue.dequeue());

        queue.enqueue(60);

        assertEquals(3, queue.frontIndex());
        assertEquals(0, queue.rearIndex());
        assertTrue(queue.hasWrapped());

        assertArrayEquals(
                new Object[]{60, null, null, 40, 50},
                queue.storageSnapshot()
        );

        assertArrayEquals(
                new Object[]{40, 50, 60},
                queue.toArray()
        );

        assertEquals("[ 40, 50, 60 ]", queue.display());
    }

    @Test
    void circularQueueReusesAllFreedPositions() {
        queue.enqueue(10);
        queue.enqueue(20);
        queue.enqueue(30);
        queue.enqueue(40);
        queue.enqueue(50);

        queue.dequeue();
        queue.dequeue();
        queue.dequeue();

        queue.enqueue(60);
        queue.enqueue(70);
        queue.enqueue(80);

        assertTrue(queue.isFull());
        assertTrue(queue.hasWrapped());
        assertEquals(5, queue.size());

        assertEquals(3, queue.frontIndex());
        assertEquals(2, queue.rearIndex());

        assertArrayEquals(
                new Object[]{60, 70, 80, 40, 50},
                queue.storageSnapshot()
        );

        assertArrayEquals(
                new Object[]{40, 50, 60, 70, 80},
                queue.toArray()
        );
    }

    @Test
    void wrappedQueueMaintainsFirstInFirstOutOrder() {
        queue.enqueue(10);
        queue.enqueue(20);
        queue.enqueue(30);
        queue.enqueue(40);
        queue.enqueue(50);

        assertEquals(10, queue.dequeue());
        assertEquals(20, queue.dequeue());
        assertEquals(30, queue.dequeue());

        queue.enqueue(60);
        queue.enqueue(70);
        queue.enqueue(80);

        assertEquals(40, queue.dequeue());
        assertEquals(50, queue.dequeue());
        assertEquals(60, queue.dequeue());
        assertEquals(70, queue.dequeue());
        assertEquals(80, queue.dequeue());

        assertTrue(queue.isEmpty());
    }

    @Test
    void queueCanBeReusedAfterBecomingEmpty() {
        queue.enqueue(10);
        queue.enqueue(20);

        assertEquals(10, queue.dequeue());
        assertEquals(20, queue.dequeue());

        assertTrue(queue.isEmpty());

        queue.enqueue(30);
        queue.enqueue(40);

        assertEquals(30, queue.front());
        assertEquals("[ 30, 40 ]", queue.display());
        assertEquals(2, queue.size());
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
    void clearRestoresInitialState() {
        queue.enqueue(10);
        queue.enqueue(20);
        queue.enqueue(30);

        queue.dequeue();
        queue.enqueue(40);

        queue.clear();

        assertTrue(queue.isEmpty());
        assertFalse(queue.isFull());
        assertFalse(queue.hasWrapped());

        assertEquals(0, queue.size());
        assertEquals(0, queue.frontIndex());
        assertEquals(-1, queue.rearIndex());

        assertArrayEquals(
                new Object[]{null, null, null, null, null},
                queue.storageSnapshot()
        );

        queue.enqueue(99);

        assertEquals(99, queue.front());
        assertEquals(0, queue.frontIndex());
        assertEquals(0, queue.rearIndex());
    }

    @Test
    void constructorRejectsInvalidCapacity() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new CircularQueue<Integer>(0)
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> new CircularQueue<Integer>(-4)
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
    void capacityOneQueueWorksCorrectly() {
        CircularQueue<Integer> smallQueue =
                new CircularQueue<>(1);

        smallQueue.enqueue(10);

        assertTrue(smallQueue.isFull());
        assertEquals(0, smallQueue.frontIndex());
        assertEquals(0, smallQueue.rearIndex());
        assertEquals(10, smallQueue.front());

        assertEquals(10, smallQueue.dequeue());
        assertTrue(smallQueue.isEmpty());

        smallQueue.enqueue(20);

        assertTrue(smallQueue.isFull());
        assertEquals(20, smallQueue.front());
    }

    @Test
    void sameQueueClassCanStoreStrings() {
        CircularQueue<String> textQueue =
                new CircularQueue<>(3);

        textQueue.enqueue("first");
        textQueue.enqueue("second");

        assertEquals("first", textQueue.dequeue());

        textQueue.enqueue("third");
        textQueue.enqueue("fourth");

        assertEquals("second", textQueue.front());

        assertArrayEquals(
                new Object[]{"second", "third", "fourth"},
                textQueue.toArray()
        );
    }
}
package ug.ucu.algorithmexplorer.controllers;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ug.ucu.algorithmexplorer.models.BracketCheckResult;

class DataStructuresControllerTest {

    private DataStructuresController controller;

    @BeforeEach
    void setUp() {
        controller = new DataStructuresController();
    }

    @Test
    void capacityBasedStructuresDoNotExistInitially() {
        assertFalse(controller.hasStack());
        assertFalse(controller.hasLinearQueue());
        assertFalse(controller.hasCircularQueue());
    }

    @Test
    void stackOperationsRequireStackCreation() {
        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> controller.pushStack(10)
        );

        assertTrue(exception.getMessage().contains("Create the stack"));
    }

    @Test
    void createsAndOperatesStack() {
        controller.createStack(3);

        controller.pushStack(10);
        controller.pushStack(20);

        assertTrue(controller.hasStack());
        assertEquals(2, controller.stackSize());
        assertEquals(20, controller.peekStack());
        assertEquals(1, controller.stackTopIndex());

        assertArrayEquals(
                new Object[]{10, 20},
                controller.stackSnapshot()
        );

        assertEquals(20, controller.popStack());
        assertFalse(controller.isStackFull());
    }

    @Test
    void replacingStackCreatesFreshState() {
        controller.createStack(3);
        controller.pushStack(10);
        controller.pushStack(20);

        controller.createStack(5);

        assertEquals(5, controller.stackCapacity());
        assertEquals(0, controller.stackSize());
        assertTrue(controller.isStackEmpty());
    }

    @Test
    void delegatesBalancedBracketCheck() {
        BracketCheckResult result =
                controller.checkBrackets("{ ( [ ] ) }");

        assertTrue(result.balanced());
        assertEquals(11, result.steps().size());
        assertEquals("[]", result.steps().get(10).stackState());
    }

    @Test
    void linearQueueMustBeCreatedBeforeUse() {
        assertThrows(
                IllegalStateException.class,
                () -> controller.enqueueLinear(10)
        );
    }

    @Test
    void operatesLinearQueueAndExposesFalseFull() {
        controller.createLinearQueue(3);

        controller.enqueueLinear(10);
        controller.enqueueLinear(20);
        controller.enqueueLinear(30);

        assertEquals(10, controller.dequeueLinear());
        assertEquals(20, controller.dequeueLinear());

        assertEquals(1, controller.linearQueueSize());
        assertTrue(controller.isLinearQueueFull());
        assertTrue(controller.hasLinearFalseFullCondition());
        assertEquals(2, controller.linearWastedPositions());

        assertArrayEquals(
                new Object[]{null, null, 30},
                controller.linearQueueStorageSnapshot()
        );
    }

    @Test
    void operatesCircularQueueAndShowsWraparound() {
        controller.createCircularQueue(3);

        controller.enqueueCircular(10);
        controller.enqueueCircular(20);
        controller.enqueueCircular(30);

        assertEquals(10, controller.dequeueCircular());
        assertEquals(20, controller.dequeueCircular());

        controller.enqueueCircular(40);
        controller.enqueueCircular(50);

        assertTrue(controller.isCircularQueueFull());
        assertTrue(controller.hasCircularQueueWrapped());

        assertEquals(2, controller.circularFrontIndex());
        assertEquals(1, controller.circularRearIndex());

        assertArrayEquals(
                new Object[]{30, 40, 50},
                controller.circularQueueSnapshot()
        );

        assertArrayEquals(
                new Object[]{40, 50, 30},
                controller.circularQueueStorageSnapshot()
        );
    }

    @Test
    void singlyLinkedListOperationsAreDelegated() {
        controller.insertSinglyAtEnd(10);
        controller.insertSinglyAtEnd(30);
        controller.insertSinglyBefore(30, 20);
        controller.insertSinglyAfter(30, 40);

        assertArrayEquals(
                new Object[]{10, 20, 30, 40},
                controller.singlyListSnapshot()
        );

        assertEquals(1, controller.searchSingly(20));
        assertEquals(4, controller.singlyListSize());

        assertEquals(20, controller.deleteSinglyValue(20));

        assertArrayEquals(
                new Object[]{10, 30, 40},
                controller.singlyListSnapshot()
        );
    }

    @Test
    void everySinglyDeletionVariantIsAvailable() {
        controller.insertSinglyAtEnd(10);
        controller.insertSinglyAtEnd(20);
        controller.insertSinglyAtEnd(30);
        controller.insertSinglyAtEnd(40);
        controller.insertSinglyAtEnd(50);

        assertEquals(10, controller.deleteSinglyAtBeginning());
        assertEquals(50, controller.deleteSinglyAtEnd());
        assertEquals(20, controller.deleteSinglyBefore(30));
        assertEquals(40, controller.deleteSinglyAfter(30));

        assertArrayEquals(
                new Object[]{30},
                controller.singlyListSnapshot()
        );
    }

    @Test
    void doublyLinkedListOperationsAreDelegated() {
        controller.insertDoublyAtEnd(10);
        controller.insertDoublyAtEnd(30);
        controller.insertDoublyBefore(30, 20);
        controller.insertDoublyAfter(30, 40);

        assertArrayEquals(
                new Object[]{10, 20, 30, 40},
                controller.doublyListForwardSnapshot()
        );

        assertArrayEquals(
                new Object[]{40, 30, 20, 10},
                controller.doublyListBackwardSnapshot()
        );

        assertEquals(2, controller.searchDoubly(30));
        assertTrue(controller.hasConsistentDoublyLinks());
    }

    @Test
    void everyDoublyDeletionVariantIsAvailable() {
        controller.insertDoublyAtEnd(10);
        controller.insertDoublyAtEnd(20);
        controller.insertDoublyAtEnd(30);
        controller.insertDoublyAtEnd(40);
        controller.insertDoublyAtEnd(50);

        assertEquals(10, controller.deleteDoublyAtBeginning());
        assertEquals(50, controller.deleteDoublyAtEnd());
        assertEquals(20, controller.deleteDoublyBefore(30));
        assertEquals(40, controller.deleteDoublyAfter(30));

        assertArrayEquals(
                new Object[]{30},
                controller.doublyListForwardSnapshot()
        );

        assertTrue(controller.hasConsistentDoublyLinks());
    }

    @Test
    void clearOperationsResetAllExistingStructures() {
        controller.createStack(3);
        controller.pushStack(10);

        controller.createLinearQueue(3);
        controller.enqueueLinear(20);

        controller.createCircularQueue(3);
        controller.enqueueCircular(30);

        controller.insertSinglyAtEnd(40);
        controller.insertDoublyAtEnd(50);

        controller.clearStack();
        controller.clearLinearQueue();
        controller.clearCircularQueue();
        controller.clearSinglyList();
        controller.clearDoublyList();

        assertTrue(controller.isStackEmpty());
        assertTrue(controller.isLinearQueueEmpty());
        assertTrue(controller.isCircularQueueEmpty());
        assertTrue(controller.isSinglyListEmpty());
        assertTrue(controller.isDoublyListEmpty());
    }

    @Test
    void backendErrorsReachControllerCaller() {
        controller.createStack(1);
        controller.pushStack(10);

        assertThrows(
                IllegalStateException.class,
                () -> controller.pushStack(20)
        );

        controller.createLinearQueue(1);

        assertThrows(
                IllegalStateException.class,
                () -> controller.dequeueLinear()
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> controller.insertSinglyAfter(99, 10)
        );
    }
}

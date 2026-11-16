package ug.ucu.algorithmexplorer.datastructures;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DoublyLinkedListTest {

    private DoublyLinkedList<Integer> list;

    @BeforeEach
    void setUp() {
        list = new DoublyLinkedList<>();
    }

    @Test
    void newListHasCorrectInitialState() {
        assertTrue(list.isEmpty());
        assertEquals(0, list.size());
        assertEquals("HEAD -> null", list.displayForward());
        assertEquals("TAIL -> null", list.displayBackward());
        assertTrue(list.hasConsistentLinks());
    }

    @Test
    void insertsAtBeginning() {
        list.insertAtBeginning(20);
        list.insertAtBeginning(10);

        assertArrayEquals(
                new Object[]{10, 20},
                list.toArrayForward()
        );

        assertArrayEquals(
                new Object[]{20, 10},
                list.toArrayBackward()
        );

        assertEquals(10, list.first());
        assertEquals(20, list.last());
        assertTrue(list.hasConsistentLinks());
    }

    @Test
    void insertsAtEnd() {
        list.insertAtEnd(10);
        list.insertAtEnd(20);
        list.insertAtEnd(30);

        assertArrayEquals(
                new Object[]{10, 20, 30},
                list.toArrayForward()
        );

        assertArrayEquals(
                new Object[]{30, 20, 10},
                list.toArrayBackward()
        );

        assertTrue(list.hasConsistentLinks());
    }

    @Test
    void insertsBeforeMiddleTarget() {
        list.insertAtEnd(10);
        list.insertAtEnd(30);

        list.insertBefore(30, 20);

        assertArrayEquals(
                new Object[]{10, 20, 30},
                list.toArrayForward()
        );

        assertArrayEquals(
                new Object[]{30, 20, 10},
                list.toArrayBackward()
        );

        assertTrue(list.hasConsistentLinks());
    }

    @Test
    void insertsBeforeHead() {
        list.insertAtEnd(20);
        list.insertAtEnd(30);

        list.insertBefore(20, 10);

        assertEquals(10, list.first());

        assertArrayEquals(
                new Object[]{10, 20, 30},
                list.toArrayForward()
        );

        assertTrue(list.hasConsistentLinks());
    }

    @Test
    void insertsAfterMiddleTarget() {
        list.insertAtEnd(10);
        list.insertAtEnd(30);

        list.insertAfter(10, 20);

        assertArrayEquals(
                new Object[]{10, 20, 30},
                list.toArrayForward()
        );

        assertTrue(list.hasConsistentLinks());
    }

    @Test
    void insertsAfterTail() {
        list.insertAtEnd(10);
        list.insertAfter(10, 20);

        assertEquals(20, list.last());

        assertArrayEquals(
                new Object[]{10, 20},
                list.toArrayForward()
        );

        assertTrue(list.hasConsistentLinks());
    }

    @Test
    void deletesAtBeginning() {
        populateList();

        assertEquals(10, list.deleteAtBeginning());

        assertArrayEquals(
                new Object[]{20, 30, 40},
                list.toArrayForward()
        );

        assertArrayEquals(
                new Object[]{40, 30, 20},
                list.toArrayBackward()
        );

        assertEquals(20, list.first());
        assertTrue(list.hasConsistentLinks());
    }

    @Test
    void deletesAtEnd() {
        populateList();

        assertEquals(40, list.deleteAtEnd());

        assertArrayEquals(
                new Object[]{10, 20, 30},
                list.toArrayForward()
        );

        assertArrayEquals(
                new Object[]{30, 20, 10},
                list.toArrayBackward()
        );

        assertEquals(30, list.last());
        assertTrue(list.hasConsistentLinks());
    }

    @Test
    void deletesOnlyNodeAtBeginning() {
        list.insertAtEnd(10);

        assertEquals(10, list.deleteAtBeginning());
        assertTrue(list.isEmpty());
        assertTrue(list.hasConsistentLinks());

        list.insertAtBeginning(20);

        assertEquals(20, list.first());
        assertEquals(20, list.last());
    }

    @Test
    void deletesOnlyNodeAtEnd() {
        list.insertAtEnd(10);

        assertEquals(10, list.deleteAtEnd());
        assertTrue(list.isEmpty());
        assertTrue(list.hasConsistentLinks());

        list.insertAtEnd(20);

        assertEquals(20, list.first());
        assertEquals(20, list.last());
    }

    @Test
    void deletesBeforeMiddleTarget() {
        populateList();

        assertEquals(20, list.deleteBefore(30));

        assertArrayEquals(
                new Object[]{10, 30, 40},
                list.toArrayForward()
        );

        assertArrayEquals(
                new Object[]{40, 30, 10},
                list.toArrayBackward()
        );

        assertTrue(list.hasConsistentLinks());
    }

    @Test
    void deletesHeadBeforeSecondNode() {
        list.insertAtEnd(10);
        list.insertAtEnd(20);
        list.insertAtEnd(30);

        assertEquals(10, list.deleteBefore(20));

        assertEquals(20, list.first());
        assertArrayEquals(
                new Object[]{20, 30},
                list.toArrayForward()
        );

        assertTrue(list.hasConsistentLinks());
    }

    @Test
    void cannotDeleteBeforeHead() {
        list.insertAtEnd(10);
        list.insertAtEnd(20);

        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> list.deleteBefore(10)
        );

        assertTrue(exception.getMessage().contains("head"));
    }

    @Test
    void deletesAfterMiddleTarget() {
        populateList();

        assertEquals(30, list.deleteAfter(20));

        assertArrayEquals(
                new Object[]{10, 20, 40},
                list.toArrayForward()
        );

        assertArrayEquals(
                new Object[]{40, 20, 10},
                list.toArrayBackward()
        );

        assertTrue(list.hasConsistentLinks());
    }

    @Test
    void deletesTailAfterPreviousNode() {
        list.insertAtEnd(10);
        list.insertAtEnd(20);
        list.insertAtEnd(30);

        assertEquals(30, list.deleteAfter(20));

        assertEquals(20, list.last());

        assertArrayEquals(
                new Object[]{10, 20},
                list.toArrayForward()
        );

        assertTrue(list.hasConsistentLinks());
    }

    @Test
    void cannotDeleteAfterTail() {
        list.insertAtEnd(10);
        list.insertAtEnd(20);

        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> list.deleteAfter(20)
        );

        assertTrue(exception.getMessage().contains("tail"));
    }

    @Test
    void deletesSpecifiedMiddleValue() {
        populateList();

        assertEquals(20, list.deleteValue(20));

        assertArrayEquals(
                new Object[]{10, 30, 40},
                list.toArrayForward()
        );

        assertTrue(list.hasConsistentLinks());
    }

    @Test
    void deletesHeadByValue() {
        list.insertAtEnd(10);
        list.insertAtEnd(20);

        assertEquals(10, list.deleteValue(10));

        assertEquals(20, list.first());
        assertEquals(20, list.last());
        assertTrue(list.hasConsistentLinks());
    }

    @Test
    void deletesTailByValue() {
        list.insertAtEnd(10);
        list.insertAtEnd(20);
        list.insertAtEnd(30);

        assertEquals(30, list.deleteValue(30));

        assertEquals(20, list.last());
        assertTrue(list.hasConsistentLinks());
    }

    @Test
    void searchReturnsFirstMatchingIndex() {
        list.insertAtEnd(10);
        list.insertAtEnd(20);
        list.insertAtEnd(20);
        list.insertAtEnd(30);

        assertEquals(1, list.search(20));
        assertEquals(3, list.search(30));
        assertEquals(-1, list.search(99));

        assertTrue(list.contains(20));
        assertFalse(list.contains(99));
    }

    @Test
    void operationsUseFirstDuplicateOccurrence() {
        list.insertAtEnd(10);
        list.insertAtEnd(20);
        list.insertAtEnd(20);
        list.insertAtEnd(30);

        list.insertAfter(20, 25);

        assertArrayEquals(
                new Object[]{10, 20, 25, 20, 30},
                list.toArrayForward()
        );

        assertEquals(20, list.deleteValue(20));

        assertArrayEquals(
                new Object[]{10, 25, 20, 30},
                list.toArrayForward()
        );

        assertTrue(list.hasConsistentLinks());
    }

    @Test
    void displaysForwardAndBackward() {
        list.insertAtEnd(10);
        list.insertAtEnd(20);
        list.insertAtEnd(30);

        assertEquals(
                "HEAD -> [ 10 ] <-> [ 20 ] <-> [ 30 ] -> null",
                list.displayForward()
        );

        assertEquals(
                "TAIL -> [ 30 ] <-> [ 20 ] <-> [ 10 ] -> null",
                list.displayBackward()
        );
    }

    @Test
    void clearRemovesAllNodesAndAllowsReuse() {
        populateList();

        list.clear();

        assertTrue(list.isEmpty());
        assertEquals(0, list.size());
        assertTrue(list.hasConsistentLinks());

        list.insertAtEnd(50);

        assertEquals(50, list.first());
        assertEquals(50, list.last());
        assertTrue(list.hasConsistentLinks());
    }

    @Test
    void missingTargetsAreReported() {
        list.insertAtEnd(10);
        list.insertAtEnd(20);

        assertThrows(
                IllegalArgumentException.class,
                () -> list.insertBefore(99, 30)
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> list.insertAfter(99, 30)
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> list.deleteBefore(99)
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> list.deleteAfter(99)
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> list.deleteValue(99)
        );
    }

    @Test
    void deletionOperationsRejectEmptyList() {
        assertThrows(
                IllegalStateException.class,
                () -> list.deleteAtBeginning()
        );

        assertThrows(
                IllegalStateException.class,
                () -> list.deleteAtEnd()
        );

        assertThrows(
                IllegalStateException.class,
                () -> list.deleteBefore(10)
        );

        assertThrows(
                IllegalStateException.class,
                () -> list.deleteAfter(10)
        );

        assertThrows(
                IllegalStateException.class,
                () -> list.deleteValue(10)
        );
    }

    @Test
    void firstAndLastRejectEmptyList() {
        assertThrows(
                IllegalStateException.class,
                () -> list.first()
        );

        assertThrows(
                IllegalStateException.class,
                () -> list.last()
        );
    }

    @Test
    void nullValuesAndTargetsAreRejected() {
        assertThrows(
                IllegalArgumentException.class,
                () -> list.insertAtBeginning(null)
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> list.insertAtEnd(null)
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> list.insertBefore(10, null)
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> list.insertAfter(null, 20)
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> list.search(null)
        );
    }

    @Test
    void returnedArraysAreIndependentSnapshots() {
        list.insertAtEnd(10);
        list.insertAtEnd(20);

        Object[] forward = list.toArrayForward();
        Object[] backward = list.toArrayBackward();

        forward[0] = 999;
        backward[0] = 999;

        assertArrayEquals(
                new Object[]{10, 20},
                list.toArrayForward()
        );

        assertArrayEquals(
                new Object[]{20, 10},
                list.toArrayBackward()
        );
    }

    @Test
    void genericListCanStoreStrings() {
        DoublyLinkedList<String> textList =
                new DoublyLinkedList<>();

        textList.insertAtEnd("A");
        textList.insertAtEnd("C");
        textList.insertBefore("C", "B");

        assertArrayEquals(
                new Object[]{"A", "B", "C"},
                textList.toArrayForward()
        );

        assertArrayEquals(
                new Object[]{"C", "B", "A"},
                textList.toArrayBackward()
        );

        assertTrue(textList.hasConsistentLinks());
    }

    private void populateList() {
        list.insertAtEnd(10);
        list.insertAtEnd(20);
        list.insertAtEnd(30);
        list.insertAtEnd(40);
    }
}
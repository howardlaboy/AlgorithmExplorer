package ug.ucu.algorithmexplorer.datastructures;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SinglyLinkedListTest {

    private SinglyLinkedList<Integer> list;

    @BeforeEach
    void setUp() {
        list = new SinglyLinkedList<>();
    }

    @Test
    void newListHasCorrectInitialState() {
        assertTrue(list.isEmpty());
        assertEquals(0, list.size());
        assertEquals("HEAD -> null", list.displayForward());
        assertArrayEquals(new Object[0], list.toArray());
    }

    @Test
    void insertsAtBeginning() {
        list.insertAtBeginning(20);
        list.insertAtBeginning(10);

        assertArrayEquals(
                new Object[]{10, 20},
                list.toArray()
        );

        assertEquals(10, list.first());
        assertEquals(20, list.last());
        assertEquals(2, list.size());
    }

    @Test
    void insertsAtEnd() {
        list.insertAtEnd(10);
        list.insertAtEnd(20);
        list.insertAtEnd(30);

        assertArrayEquals(
                new Object[]{10, 20, 30},
                list.toArray()
        );

        assertEquals(10, list.first());
        assertEquals(30, list.last());
    }

    @Test
    void insertsBeforeTarget() {
        list.insertAtEnd(10);
        list.insertAtEnd(30);

        list.insertBefore(30, 20);

        assertArrayEquals(
                new Object[]{10, 20, 30},
                list.toArray()
        );
    }

    @Test
    void insertsBeforeHead() {
        list.insertAtEnd(20);
        list.insertAtEnd(30);

        list.insertBefore(20, 10);

        assertArrayEquals(
                new Object[]{10, 20, 30},
                list.toArray()
        );
        assertEquals(10, list.first());
    }

    @Test
    void insertsAfterTarget() {
        list.insertAtEnd(10);
        list.insertAtEnd(30);

        list.insertAfter(10, 20);

        assertArrayEquals(
                new Object[]{10, 20, 30},
                list.toArray()
        );
    }

    @Test
    void insertsAfterTailAndUpdatesTail() {
        list.insertAtEnd(10);
        list.insertAfter(10, 20);

        assertArrayEquals(
                new Object[]{10, 20},
                list.toArray()
        );
        assertEquals(20, list.last());
    }

    @Test
    void deletesAtBeginning() {
        list.insertAtEnd(10);
        list.insertAtEnd(20);
        list.insertAtEnd(30);

        assertEquals(10, list.deleteAtBeginning());

        assertArrayEquals(
                new Object[]{20, 30},
                list.toArray()
        );
        assertEquals(20, list.first());
    }

    @Test
    void deletesAtEnd() {
        list.insertAtEnd(10);
        list.insertAtEnd(20);
        list.insertAtEnd(30);

        assertEquals(30, list.deleteAtEnd());

        assertArrayEquals(
                new Object[]{10, 20},
                list.toArray()
        );
        assertEquals(20, list.last());
    }

    @Test
    void deletesOnlyNodeAtBeginning() {
        list.insertAtEnd(10);

        assertEquals(10, list.deleteAtBeginning());
        assertTrue(list.isEmpty());

        list.insertAtEnd(20);

        assertEquals(20, list.first());
        assertEquals(20, list.last());
    }

    @Test
    void deletesOnlyNodeAtEnd() {
        list.insertAtEnd(10);

        assertEquals(10, list.deleteAtEnd());
        assertTrue(list.isEmpty());

        list.insertAtBeginning(20);

        assertEquals(20, list.first());
        assertEquals(20, list.last());
    }

    @Test
    void deletesBeforeTarget() {
        list.insertAtEnd(10);
        list.insertAtEnd(20);
        list.insertAtEnd(30);
        list.insertAtEnd(40);

        assertEquals(20, list.deleteBefore(30));

        assertArrayEquals(
                new Object[]{10, 30, 40},
                list.toArray()
        );
    }

    @Test
    void deletesHeadWhenHeadIsBeforeTarget() {
        list.insertAtEnd(10);
        list.insertAtEnd(20);
        list.insertAtEnd(30);

        assertEquals(10, list.deleteBefore(20));

        assertArrayEquals(
                new Object[]{20, 30},
                list.toArray()
        );
    }

    @Test
    void cannotDeleteBeforeHead() {
        list.insertAtEnd(10);
        list.insertAtEnd(20);

        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> list.deleteBefore(10)
        );

        assertTrue(
                exception.getMessage().contains("head")
        );
    }

    @Test
    void deletesAfterTarget() {
        list.insertAtEnd(10);
        list.insertAtEnd(20);
        list.insertAtEnd(30);
        list.insertAtEnd(40);

        assertEquals(30, list.deleteAfter(20));

        assertArrayEquals(
                new Object[]{10, 20, 40},
                list.toArray()
        );
    }

    @Test
    void deletesTailWhenTailIsAfterTarget() {
        list.insertAtEnd(10);
        list.insertAtEnd(20);
        list.insertAtEnd(30);

        assertEquals(30, list.deleteAfter(20));

        assertArrayEquals(
                new Object[]{10, 20},
                list.toArray()
        );
        assertEquals(20, list.last());
    }

    @Test
    void cannotDeleteAfterTail() {
        list.insertAtEnd(10);
        list.insertAtEnd(20);

        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> list.deleteAfter(20)
        );

        assertTrue(
                exception.getMessage().contains("tail")
        );
    }

    @Test
    void deletesSpecifiedValue() {
        list.insertAtEnd(10);
        list.insertAtEnd(20);
        list.insertAtEnd(30);

        assertEquals(20, list.deleteValue(20));

        assertArrayEquals(
                new Object[]{10, 30},
                list.toArray()
        );
    }

    @Test
    void deletesHeadByValue() {
        list.insertAtEnd(10);
        list.insertAtEnd(20);

        assertEquals(10, list.deleteValue(10));

        assertArrayEquals(
                new Object[]{20},
                list.toArray()
        );
        assertEquals(20, list.first());
        assertEquals(20, list.last());
    }

    @Test
    void deletesTailByValueAndUpdatesTail() {
        list.insertAtEnd(10);
        list.insertAtEnd(20);
        list.insertAtEnd(30);

        assertEquals(30, list.deleteValue(30));

        assertEquals(20, list.last());
        assertArrayEquals(
                new Object[]{10, 20},
                list.toArray()
        );
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
    void operationsUseFirstOccurrenceOfDuplicateTarget() {
        list.insertAtEnd(10);
        list.insertAtEnd(20);
        list.insertAtEnd(20);
        list.insertAtEnd(30);

        list.insertAfter(20, 25);

        assertArrayEquals(
                new Object[]{10, 20, 25, 20, 30},
                list.toArray()
        );

        assertEquals(20, list.deleteValue(20));

        assertArrayEquals(
                new Object[]{10, 25, 20, 30},
                list.toArray()
        );
    }

    @Test
    void displayShowsNodesAndArrows() {
        list.insertAtEnd(10);
        list.insertAtEnd(20);
        list.insertAtEnd(30);

        assertEquals(
                "HEAD -> [ 10 ] -> [ 20 ] -> [ 30 ] -> null",
                list.displayForward()
        );
    }

    @Test
    void clearRemovesAllNodesAndAllowsReuse() {
        list.insertAtEnd(10);
        list.insertAtEnd(20);
        list.insertAtEnd(30);

        list.clear();

        assertTrue(list.isEmpty());
        assertEquals(0, list.size());
        assertEquals("HEAD -> null", list.displayForward());

        list.insertAtEnd(40);

        assertEquals(40, list.first());
        assertEquals(40, list.last());
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
    void returnedArrayIsIndependentSnapshot() {
        list.insertAtEnd(10);
        list.insertAtEnd(20);

        Object[] snapshot = list.toArray();
        snapshot[0] = 999;

        assertArrayEquals(
                new Object[]{10, 20},
                list.toArray()
        );
    }

    @Test
    void genericListCanStoreStrings() {
        SinglyLinkedList<String> textList =
                new SinglyLinkedList<>();

        textList.insertAtEnd("A");
        textList.insertAtEnd("C");
        textList.insertBefore("C", "B");

        assertArrayEquals(
                new Object[]{"A", "B", "C"},
                textList.toArray()
        );

        assertEquals(1, textList.search("B"));
    }
}
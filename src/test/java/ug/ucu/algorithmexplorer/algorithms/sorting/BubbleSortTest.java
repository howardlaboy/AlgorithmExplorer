package ug.ucu.algorithmexplorer.algorithms.sorting;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ug.ucu.algorithmexplorer.models.sorting.SortResult;
import ug.ucu.algorithmexplorer.models.sorting.SortStep;

class BubbleSortTest {

    private BubbleSort bubbleSort;

    @BeforeEach
    void setUp() {
        bubbleSort = new BubbleSort();
    }

    @Test
    void sortsUnorderedValues() {
        SortResult result = bubbleSort.sort(
                new int[]{5, 1, 4, 2, 8}
        );

        assertArrayEquals(
                new int[]{1, 2, 4, 5, 8},
                result.sortedArray()
        );

        assertEquals("Bubble Sort", result.algorithmName());
        assertTrue(result.executionTimeNanos() >= 0);
    }

    @Test
    void doesNotModifyOriginalInput() {
        int[] input = {5, 1, 4, 2, 8};

        SortResult result = bubbleSort.sort(input);

        assertArrayEquals(
                new int[]{5, 1, 4, 2, 8},
                input
        );

        assertArrayEquals(
                new int[]{5, 1, 4, 2, 8},
                result.originalArray()
        );
    }

    @Test
    void sortsReverseOrderedValues() {
        SortResult result = bubbleSort.sort(
                new int[]{5, 4, 3, 2, 1}
        );

        assertArrayEquals(
                new int[]{1, 2, 3, 4, 5},
                result.sortedArray()
        );

        assertEquals(10, result.comparisonCount());
        assertEquals(10, result.swapCount());
        assertEquals(30, result.moveCount());
        assertEquals(4, result.stepCount());
    }

    @Test
    void stopsEarlyForAlreadySortedArray() {
        SortResult result = bubbleSort.sort(
                new int[]{1, 2, 3, 4, 5}
        );

        assertArrayEquals(
                new int[]{1, 2, 3, 4, 5},
                result.sortedArray()
        );

        assertEquals(4, result.comparisonCount());
        assertEquals(0, result.swapCount());
        assertEquals(0, result.moveCount());
        assertEquals(1, result.stepCount());

        assertTrue(
                result.steps()
                        .get(0)
                        .description()
                        .contains("No swaps occurred")
        );
    }

    @Test
    void handlesSingleValue() {
        SortResult result = bubbleSort.sort(
                new int[]{42}
        );

        assertArrayEquals(
                new int[]{42},
                result.sortedArray()
        );

        assertEquals(0, result.comparisonCount());
        assertEquals(0, result.swapCount());
        assertEquals(0, result.moveCount());
        assertEquals(0, result.stepCount());
    }

    @Test
    void handlesDuplicateAndNegativeValues() {
        SortResult result = bubbleSort.sort(
                new int[]{3, -1, 3, 0, -1}
        );

        assertArrayEquals(
                new int[]{-1, -1, 0, 3, 3},
                result.sortedArray()
        );
    }

    @Test
    void recordsArrayAfterEveryCompletedPass() {
        SortResult result = bubbleSort.sort(
                new int[]{4, 3, 2, 1}
        );

        assertEquals(3, result.stepCount());

        assertArrayEquals(
                new int[]{3, 2, 1, 4},
                result.steps().get(0).arrayState()
        );

        assertArrayEquals(
                new int[]{2, 1, 3, 4},
                result.steps().get(1).arrayState()
        );

        assertArrayEquals(
                new int[]{1, 2, 3, 4},
                result.steps().get(2).arrayState()
        );
    }

    @Test
    void resultSnapshotsCannotChangeStoredResult() {
        SortResult result = bubbleSort.sort(
                new int[]{3, 2, 1}
        );

        int[] sortedSnapshot = result.sortedArray();
        sortedSnapshot[0] = 999;

        assertArrayEquals(
                new int[]{1, 2, 3},
                result.sortedArray()
        );

        SortStep firstStep = result.steps().get(0);
        int[] stepSnapshot = firstStep.arrayState();

        stepSnapshot[0] = 999;

        assertArrayEquals(
                new int[]{2, 1, 3},
                firstStep.arrayState()
        );
    }

    @Test
    void stepListCannotBeModified() {
        SortResult result = bubbleSort.sort(
                new int[]{3, 2, 1}
        );

        assertThrows(
                UnsupportedOperationException.class,
                () -> result.steps().clear()
        );
    }

    @Test
    void rejectsNullAndEmptyInput() {
        assertThrows(
                IllegalArgumentException.class,
                () -> bubbleSort.sort(null)
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> bubbleSort.sort(new int[0])
        );
    }

    @Test
    void publishesCorrectComplexityInformation() {
        assertEquals("O(n)", bubbleSort.getBestCase());
        assertEquals(
                "O(n^2)",
                bubbleSort.getAverageCase()
        );
        assertEquals(
                "O(n^2)",
                bubbleSort.getWorstCase()
        );
        assertEquals(
                "O(1)",
                bubbleSort.getSpaceComplexity()
        );
        assertTrue(bubbleSort.isStable());
        assertFalse(bubbleSort.getName().isBlank());
    }
}
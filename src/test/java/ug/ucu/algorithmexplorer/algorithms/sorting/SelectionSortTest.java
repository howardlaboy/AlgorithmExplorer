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

class SelectionSortTest {

    private SelectionSort selectionSort;

    @BeforeEach
    void setUp() {
        selectionSort = new SelectionSort();
    }

    @Test
    void sortsUnorderedValues() {
        SortResult result = selectionSort.sort(
                new int[]{64, 25, 12, 22, 11}
        );

        assertArrayEquals(
                new int[]{11, 12, 22, 25, 64},
                result.sortedArray()
        );

        assertEquals(
                "Selection Sort",
                result.algorithmName()
        );

        assertTrue(result.executionTimeNanos() >= 0);
    }

    @Test
    void doesNotModifyOriginalInput() {
        int[] input = {64, 25, 12, 22, 11};

        SortResult result = selectionSort.sort(input);

        assertArrayEquals(
                new int[]{64, 25, 12, 22, 11},
                input
        );

        assertArrayEquals(
                new int[]{64, 25, 12, 22, 11},
                result.originalArray()
        );
    }

    @Test
    void recordsEveryCompletedPass() {
        SortResult result = selectionSort.sort(
                new int[]{64, 25, 12, 22, 11}
        );

        assertEquals(4, result.stepCount());

        assertArrayEquals(
                new int[]{11, 25, 12, 22, 64},
                result.steps().get(0).arrayState()
        );

        assertArrayEquals(
                new int[]{11, 12, 25, 22, 64},
                result.steps().get(1).arrayState()
        );

        assertArrayEquals(
                new int[]{11, 12, 22, 25, 64},
                result.steps().get(2).arrayState()
        );

        assertArrayEquals(
                new int[]{11, 12, 22, 25, 64},
                result.steps().get(3).arrayState()
        );
    }

    @Test
    void countsComparisonsAndSwapsCorrectly() {
        SortResult result = selectionSort.sort(
                new int[]{64, 25, 12, 22, 11}
        );

        assertEquals(10, result.comparisonCount());
        assertEquals(3, result.swapCount());
        assertEquals(9, result.moveCount());
    }

    @Test
    void alreadySortedInputStillPerformsAllComparisons() {
        SortResult result = selectionSort.sort(
                new int[]{1, 2, 3, 4, 5}
        );

        assertArrayEquals(
                new int[]{1, 2, 3, 4, 5},
                result.sortedArray()
        );

        assertEquals(10, result.comparisonCount());
        assertEquals(0, result.swapCount());
        assertEquals(0, result.moveCount());
        assertEquals(4, result.stepCount());

        assertTrue(
                result.steps()
                        .get(0)
                        .description()
                        .contains("already the minimum")
        );
    }

    @Test
    void sortsReverseOrderedValues() {
        SortResult result = selectionSort.sort(
                new int[]{5, 4, 3, 2, 1}
        );

        assertArrayEquals(
                new int[]{1, 2, 3, 4, 5},
                result.sortedArray()
        );

        assertEquals(10, result.comparisonCount());
        assertEquals(2, result.swapCount());
        assertEquals(6, result.moveCount());
        assertEquals(4, result.stepCount());
    }

    @Test
    void handlesSingleValue() {
        SortResult result = selectionSort.sort(
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
        SortResult result = selectionSort.sort(
                new int[]{3, -1, 3, 0, -1}
        );

        assertArrayEquals(
                new int[]{-1, -1, 0, 3, 3},
                result.sortedArray()
        );
    }

    @Test
    void highlightsCurrentAndSelectedMinimumIndices() {
        SortResult result = selectionSort.sort(
                new int[]{4, 2, 3, 1}
        );

        SortStep firstStep = result.steps().get(0);

        assertArrayEquals(
                new int[]{0, 3},
                firstStep.highlightedIndices()
        );

        SortStep secondStep = result.steps().get(1);

        assertArrayEquals(
                new int[]{1, 1},
                secondStep.highlightedIndices()
        );
    }

    @Test
    void resultSnapshotsAreIndependent() {
        SortResult result = selectionSort.sort(
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
                new int[]{1, 2, 3},
                firstStep.arrayState()
        );
    }

    @Test
    void stepListCannotBeModified() {
        SortResult result = selectionSort.sort(
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
                () -> selectionSort.sort(null)
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> selectionSort.sort(new int[0])
        );
    }

    @Test
    void publishesCorrectComplexityInformation() {
        assertEquals(
                "O(n^2)",
                selectionSort.getBestCase()
        );

        assertEquals(
                "O(n^2)",
                selectionSort.getAverageCase()
        );

        assertEquals(
                "O(n^2)",
                selectionSort.getWorstCase()
        );

        assertEquals(
                "O(1)",
                selectionSort.getSpaceComplexity()
        );

        assertFalse(selectionSort.isStable());
        assertFalse(selectionSort.getName().isBlank());
    }
}
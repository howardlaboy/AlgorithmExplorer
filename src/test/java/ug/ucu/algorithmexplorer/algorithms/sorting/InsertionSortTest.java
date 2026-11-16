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

class InsertionSortTest {

    private InsertionSort insertionSort;

    @BeforeEach
    void setUp() {
        insertionSort = new InsertionSort();
    }

    @Test
    void sortsUnorderedValues() {
        SortResult result = insertionSort.sort(
                new int[]{5, 2, 4, 6, 1, 3}
        );

        assertArrayEquals(
                new int[]{1, 2, 3, 4, 5, 6},
                result.sortedArray()
        );

        assertEquals(
                "Insertion Sort",
                result.algorithmName()
        );

        assertTrue(result.executionTimeNanos() >= 0);
    }

    @Test
    void doesNotModifyOriginalInput() {
        int[] input = {5, 2, 4, 6, 1, 3};

        SortResult result = insertionSort.sort(input);

        assertArrayEquals(
                new int[]{5, 2, 4, 6, 1, 3},
                input
        );

        assertArrayEquals(
                new int[]{5, 2, 4, 6, 1, 3},
                result.originalArray()
        );
    }

    @Test
    void recordsEveryInsertionPass() {
        SortResult result = insertionSort.sort(
                new int[]{5, 2, 4, 6, 1, 3}
        );

        assertEquals(5, result.stepCount());

        assertArrayEquals(
                new int[]{2, 5, 4, 6, 1, 3},
                result.steps().get(0).arrayState()
        );

        assertArrayEquals(
                new int[]{2, 4, 5, 6, 1, 3},
                result.steps().get(1).arrayState()
        );

        assertArrayEquals(
                new int[]{2, 4, 5, 6, 1, 3},
                result.steps().get(2).arrayState()
        );

        assertArrayEquals(
                new int[]{1, 2, 4, 5, 6, 3},
                result.steps().get(3).arrayState()
        );

        assertArrayEquals(
                new int[]{1, 2, 3, 4, 5, 6},
                result.steps().get(4).arrayState()
        );
    }

    @Test
    void countsOperationsForTypicalInput() {
        SortResult result = insertionSort.sort(
                new int[]{5, 2, 4, 6, 1, 3}
        );

        assertEquals(12, result.comparisonCount());
        assertEquals(0, result.swapCount());
        assertEquals(14, result.moveCount());
        assertEquals(5, result.stepCount());
    }

    @Test
    void alreadySortedInputUsesLinearComparisons() {
        SortResult result = insertionSort.sort(
                new int[]{1, 2, 3, 4, 5}
        );

        assertArrayEquals(
                new int[]{1, 2, 3, 4, 5},
                result.sortedArray()
        );

        assertEquals(4, result.comparisonCount());
        assertEquals(0, result.swapCount());
        assertEquals(4, result.moveCount());
        assertEquals(4, result.stepCount());

        assertTrue(
                result.steps()
                        .get(0)
                        .description()
                        .contains("already in the correct position")
        );
    }

    @Test
    void reverseOrderedInputUsesMaximumShifts() {
        SortResult result = insertionSort.sort(
                new int[]{5, 4, 3, 2, 1}
        );

        assertArrayEquals(
                new int[]{1, 2, 3, 4, 5},
                result.sortedArray()
        );

        assertEquals(10, result.comparisonCount());
        assertEquals(0, result.swapCount());
        assertEquals(14, result.moveCount());
        assertEquals(4, result.stepCount());
    }

    @Test
    void handlesSingleValue() {
        SortResult result = insertionSort.sort(
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
        SortResult result = insertionSort.sort(
                new int[]{3, -1, 3, 0, -1}
        );

        assertArrayEquals(
                new int[]{-1, -1, 0, 3, 3},
                result.sortedArray()
        );
    }

    @Test
    void equalValuesAreNotShiftedPastEachOther() {
        SortResult result = insertionSort.sort(
                new int[]{2, 2, 1}
        );

        assertArrayEquals(
                new int[]{1, 2, 2},
                result.sortedArray()
        );

        assertEquals(3, result.comparisonCount());
        assertEquals(0, result.swapCount());
        assertEquals(4, result.moveCount());
        assertTrue(insertionSort.isStable());
    }

    @Test
    void highlightsInsertionAndOriginalIndices() {
        SortResult result = insertionSort.sort(
                new int[]{4, 2, 3}
        );

        SortStep firstStep = result.steps().get(0);

        assertArrayEquals(
                new int[]{0, 1},
                firstStep.highlightedIndices()
        );

        SortStep secondStep = result.steps().get(1);

        assertArrayEquals(
                new int[]{1, 2},
                secondStep.highlightedIndices()
        );
    }

    @Test
    void resultSnapshotsAreIndependent() {
        SortResult result = insertionSort.sort(
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
                new int[]{2, 3, 1},
                firstStep.arrayState()
        );
    }

    @Test
    void stepListCannotBeModified() {
        SortResult result = insertionSort.sort(
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
                () -> insertionSort.sort(null)
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> insertionSort.sort(new int[0])
        );
    }

    @Test
    void publishesCorrectComplexityInformation() {
        assertEquals(
                "O(n)",
                insertionSort.getBestCase()
        );

        assertEquals(
                "O(n^2)",
                insertionSort.getAverageCase()
        );

        assertEquals(
                "O(n^2)",
                insertionSort.getWorstCase()
        );

        assertEquals(
                "O(1)",
                insertionSort.getSpaceComplexity()
        );

        assertTrue(insertionSort.isStable());
        assertFalse(insertionSort.getName().isBlank());
    }
}

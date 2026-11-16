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

class MergeSortTest {

    private MergeSort mergeSort;

    @BeforeEach
    void setUp() {
        mergeSort = new MergeSort();
    }

    @Test
    void sortsUnorderedValues() {
        SortResult result = mergeSort.sort(
                new int[]{5, 2, 4, 6, 1, 3}
        );

        assertArrayEquals(
                new int[]{1, 2, 3, 4, 5, 6},
                result.sortedArray()
        );

        assertEquals(
                "Merge Sort",
                result.algorithmName()
        );

        assertTrue(result.executionTimeNanos() >= 0);
    }

    @Test
    void doesNotModifyOriginalInput() {
        int[] input = {5, 2, 4, 6, 1, 3};

        SortResult result = mergeSort.sort(input);

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
    void recordsEveryMergeStep() {
        SortResult result = mergeSort.sort(
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
                new int[]{2, 4, 5, 1, 6, 3},
                result.steps().get(2).arrayState()
        );

        assertArrayEquals(
                new int[]{2, 4, 5, 1, 3, 6},
                result.steps().get(3).arrayState()
        );

        assertArrayEquals(
                new int[]{1, 2, 3, 4, 5, 6},
                result.steps().get(4).arrayState()
        );
    }

    @Test
    void countsOperationsForTypicalInput() {
        SortResult result = mergeSort.sort(
                new int[]{5, 2, 4, 6, 1, 3}
        );

        assertEquals(11, result.comparisonCount());
        assertEquals(0, result.swapCount());
        assertEquals(32, result.moveCount());
        assertEquals(5, result.stepCount());
    }

    @Test
    void sortsReverseOrderedValues() {
        SortResult result = mergeSort.sort(
                new int[]{4, 3, 2, 1}
        );

        assertArrayEquals(
                new int[]{1, 2, 3, 4},
                result.sortedArray()
        );

        assertEquals(4, result.comparisonCount());
        assertEquals(0, result.swapCount());
        assertEquals(16, result.moveCount());
        assertEquals(3, result.stepCount());
    }

    @Test
    void alreadySortedInputStillUsesMergeProcess() {
        SortResult result = mergeSort.sort(
                new int[]{1, 2, 3, 4}
        );

        assertArrayEquals(
                new int[]{1, 2, 3, 4},
                result.sortedArray()
        );

        assertEquals(4, result.comparisonCount());
        assertEquals(0, result.swapCount());
        assertEquals(16, result.moveCount());
        assertEquals(3, result.stepCount());
    }

    @Test
    void handlesSingleValue() {
        SortResult result = mergeSort.sort(
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
        SortResult result = mergeSort.sort(
                new int[]{3, -1, 3, 0, -1}
        );

        assertArrayEquals(
                new int[]{-1, -1, 0, 3, 3},
                result.sortedArray()
        );

        assertTrue(mergeSort.isStable());
    }

    @Test
    void recordsMergedRangeForHighlighting() {
        SortResult result = mergeSort.sort(
                new int[]{4, 3, 2, 1}
        );

        SortStep firstStep = result.steps().get(0);

        assertArrayEquals(
                new int[]{0, 1},
                firstStep.highlightedIndices()
        );

        SortStep secondStep = result.steps().get(1);

        assertArrayEquals(
                new int[]{2, 3},
                secondStep.highlightedIndices()
        );

        SortStep finalStep = result.steps().get(2);

        assertArrayEquals(
                new int[]{0, 1, 2, 3},
                finalStep.highlightedIndices()
        );
    }

    @Test
    void stepDescriptionsIdentifyMergedRanges() {
        SortResult result = mergeSort.sort(
                new int[]{4, 3, 2, 1}
        );

        assertTrue(
                result.steps()
                        .get(0)
                        .description()
                        .contains("[0, 0]")
        );

        assertTrue(
                result.steps()
                        .get(0)
                        .description()
                        .contains("[1, 1]")
        );

        assertTrue(
                result.steps()
                        .get(2)
                        .description()
                        .contains("[0, 3]")
        );
    }

    @Test
    void resultSnapshotsAreIndependent() {
        SortResult result = mergeSort.sort(
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
        SortResult result = mergeSort.sort(
                new int[]{3, 2, 1}
        );

        assertThrows(
                UnsupportedOperationException.class,
                () -> result.steps().clear()
        );
    }

    
    @Test
    void sameInstanceCanSortMoreThanOnce() {
    SortResult firstResult = mergeSort.sort(
            new int[]{3, 2, 1}
    );

    SortResult secondResult = mergeSort.sort(
            new int[]{6, 5, 4}
    );

    assertArrayEquals(
            new int[]{1, 2, 3},
            firstResult.sortedArray()
    );

    assertArrayEquals(
            new int[]{4, 5, 6},
            secondResult.sortedArray()
    );

    assertEquals(2, firstResult.stepCount());
    assertEquals(2, secondResult.stepCount());

    assertEquals(
            2,
            firstResult.comparisonCount()
    );

    assertEquals(
            2,
            secondResult.comparisonCount()
    );
}

    @Test
    void rejectsNullAndEmptyInput() {
        assertThrows(
                IllegalArgumentException.class,
                () -> mergeSort.sort(null)
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> mergeSort.sort(new int[0])
        );
    }

    @Test
    void publishesCorrectComplexityInformation() {
        assertEquals(
                "O(n log n)",
                mergeSort.getBestCase()
        );

        assertEquals(
                "O(n log n)",
                mergeSort.getAverageCase()
        );

        assertEquals(
                "O(n log n)",
                mergeSort.getWorstCase()
        );

        assertEquals(
                "O(n)",
                mergeSort.getSpaceComplexity()
        );

        assertTrue(mergeSort.isStable());
        assertFalse(mergeSort.getName().isBlank());
    }
}
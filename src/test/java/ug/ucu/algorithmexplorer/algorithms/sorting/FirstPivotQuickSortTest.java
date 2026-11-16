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

class FirstPivotQuickSortTest {

    private FirstPivotQuickSort quickSort;

    @BeforeEach
    void setUp() {
        quickSort = new FirstPivotQuickSort();
    }

    @Test
    void sortsUnorderedValues() {
        SortResult result = quickSort.sort(
                new int[]{4, 2, 7, 3, 6, 1, 5}
        );

        assertArrayEquals(
                new int[]{1, 2, 3, 4, 5, 6, 7},
                result.sortedArray()
        );

        assertEquals(
                "Quick Sort - First Pivot",
                result.algorithmName()
        );

        assertTrue(result.executionTimeNanos() >= 0);
    }

    @Test
    void doesNotModifyOriginalInput() {
        int[] input = {4, 2, 7, 3, 6, 1, 5};

        SortResult result = quickSort.sort(input);

        assertArrayEquals(
                new int[]{4, 2, 7, 3, 6, 1, 5},
                input
        );

        assertArrayEquals(
                new int[]{4, 2, 7, 3, 6, 1, 5},
                result.originalArray()
        );
    }

    @Test
    void firstPartitionUsesFirstElementAsPivot() {
    SortResult result = quickSort.sort(
            new int[]{4, 2, 7, 3, 6, 1, 5}
    );

    SortStep firstPartition =
            result.steps().get(0);

    assertArrayEquals(
            new int[]{1, 2, 3, 4, 6, 7, 5},
            firstPartition.arrayState()
    );

    assertArrayEquals(
            new int[]{3},
            firstPartition.highlightedIndices()
    );

    assertTrue(
            firstPartition.description()
                    .contains("first-element pivot 4")
    );

    assertTrue(
            firstPartition.description()
                    .contains("pivot finished at index 3")
    );
}

    @Test
    void recordsStepAfterEveryPartition() {
        SortResult result = quickSort.sort(
                new int[]{4, 2, 7, 3, 6, 1, 5}
        );

        assertEquals(4, result.stepCount());

        for (int stepIndex = 0;
                stepIndex < result.steps().size();
                stepIndex++) {

            assertEquals(
                    stepIndex + 1,
                    result.steps()
                            .get(stepIndex)
                            .stepNumber()
            );

            assertTrue(
                    result.steps()
                            .get(stepIndex)
                            .description()
                            .contains("Partition")
            );
        }
    }

    @Test
    void countsOperationsForTypicalInput() {
    SortResult result = quickSort.sort(
            new int[]{4, 2, 7, 3, 6, 1, 5}
    );

    assertEquals(
            11,
            result.comparisonCount()
    );

    assertEquals(
            5,
            result.swapCount()
    );

    assertEquals(
            15,
            result.moveCount()
    );

    assertEquals(
            4,
            result.stepCount()
    );
}

    @Test
    void sortsAlreadySortedInput() {
        SortResult result = quickSort.sort(
                new int[]{1, 2, 3, 4, 5}
        );

        assertArrayEquals(
                new int[]{1, 2, 3, 4, 5},
                result.sortedArray()
        );

        assertEquals(
                10,
                result.comparisonCount()
        );

        assertEquals(
                0,
                result.swapCount()
        );

        assertEquals(
                0,
                result.moveCount()
        );

        assertEquals(
                4,
                result.stepCount()
        );
    }

    @Test
    void sortedInputShowsWorstCaseComparisons() {
        SortResult result = quickSort.sort(
                new int[]{1, 2, 3, 4, 5, 6}
        );

        assertEquals(
                15,
                result.comparisonCount()
        );

        assertEquals(
                5,
                result.stepCount()
        );
    }

    @Test
    void sortsReverseOrderedInput() {
        SortResult result = quickSort.sort(
                new int[]{5, 4, 3, 2, 1}
        );

        assertArrayEquals(
                new int[]{1, 2, 3, 4, 5},
                result.sortedArray()
        );

        assertEquals(
                10,
                result.comparisonCount()
        );

        assertEquals(
                2,
                result.swapCount()
        );

        assertEquals(
                6,
                result.moveCount()
        );

        assertEquals(
                4,
                result.stepCount()
        );
    }

    @Test
    void handlesSingleValue() {
        SortResult result = quickSort.sort(
                new int[]{42}
        );

        assertArrayEquals(
                new int[]{42},
                result.sortedArray()
        );

        assertEquals(
                0,
                result.comparisonCount()
        );

        assertEquals(
                0,
                result.swapCount()
        );

        assertEquals(
                0,
                result.moveCount()
        );

        assertEquals(
                0,
                result.stepCount()
        );
    }

    @Test
    void handlesDuplicateAndNegativeValues() {
        SortResult result = quickSort.sort(
                new int[]{3, -1, 3, 0, -1}
        );

        assertArrayEquals(
                new int[]{-1, -1, 0, 3, 3},
                result.sortedArray()
        );
    }

    @Test
    void handlesAllEqualValues() {
        SortResult result = quickSort.sort(
                new int[]{5, 5, 5, 5}
        );

        assertArrayEquals(
                new int[]{5, 5, 5, 5},
                result.sortedArray()
        );

        assertEquals(
                6,
                result.comparisonCount()
        );

        assertEquals(
                3,
                result.swapCount()
        );

        assertEquals(
                9,
                result.moveCount()
        );

        assertEquals(
                3,
                result.stepCount()
        );
    }

    @Test
    void sameInstanceCanSortMoreThanOnce() {
        SortResult firstResult = quickSort.sort(
                new int[]{3, 2, 1}
        );

        SortResult secondResult = quickSort.sort(
                new int[]{6, 4, 5}
        );

        assertArrayEquals(
                new int[]{1, 2, 3},
                firstResult.sortedArray()
        );

        assertArrayEquals(
                new int[]{4, 5, 6},
                secondResult.sortedArray()
        );

        assertEquals(
                2,
                firstResult.stepCount()
        );

        assertEquals(
                2,
                secondResult.stepCount()
        );
    }

    @Test
    void resultSnapshotsAreIndependent() {
        SortResult result = quickSort.sort(
                new int[]{3, 2, 1}
        );

        int[] sortedSnapshot =
                result.sortedArray();

        sortedSnapshot[0] = 999;

        assertArrayEquals(
                new int[]{1, 2, 3},
                result.sortedArray()
        );

        SortStep firstStep =
                result.steps().get(0);

        int[] stepSnapshot =
                firstStep.arrayState();

        stepSnapshot[0] = 999;

        assertArrayEquals(
                new int[]{1, 2, 3},
                firstStep.arrayState()
        );
    }

    @Test
    void stepListCannotBeModified() {
        SortResult result = quickSort.sort(
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
                () -> quickSort.sort(null)
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> quickSort.sort(new int[0])
        );
    }

    @Test
    void publishesCorrectComplexityInformation() {
        assertEquals(
                "O(n log n)",
                quickSort.getBestCase()
        );

        assertEquals(
                "O(n log n)",
                quickSort.getAverageCase()
        );

        assertEquals(
                "O(n^2)",
                quickSort.getWorstCase()
        );

        assertEquals(
                "O(log n) average, O(n) worst",
                quickSort.getSpaceComplexity()
        );

        assertFalse(quickSort.isStable());
        assertFalse(quickSort.getName().isBlank());
    }
}
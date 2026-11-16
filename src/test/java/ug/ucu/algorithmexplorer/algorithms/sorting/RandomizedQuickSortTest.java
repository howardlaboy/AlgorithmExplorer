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

class RandomizedQuickSortTest {

    private RandomizedQuickSort quickSort;

    @BeforeEach
    void setUp() {
        /*
         * A fixed seed makes every test run repeatable.
         */
        quickSort = new RandomizedQuickSort(42L);
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
                "Quick Sort - Randomized Pivot",
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
    void recordsStepAfterEveryPartition() {
        SortResult result = quickSort.sort(
                new int[]{4, 2, 7, 3, 6, 1, 5}
        );

        assertFalse(result.steps().isEmpty());

        for (int index = 0;
                index < result.steps().size();
                index++) {

            SortStep step = result.steps().get(index);

            assertEquals(
                    index + 1,
                    step.stepNumber()
            );

            assertTrue(
                    step.description()
                            .contains("randomly selected value")
            );

            assertTrue(
                    step.description()
                            .contains(
                                    "yellow/green partitioning"
                            )
            );

            assertEquals(
                    1,
                    step.highlightedIndices().length
            );
        }
    }

    @Test
    void finalPartitionStepProducesSortedArray() {
        SortResult result = quickSort.sort(
                new int[]{8, 3, 7, 4, 9, 2, 6, 5, 1}
        );

        assertArrayEquals(
                new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9},
                result.sortedArray()
        );

        SortStep finalStep = result.steps().get(
                result.steps().size() - 1
        );

        assertArrayEquals(
                result.sortedArray(),
                finalStep.arrayState()
        );
    }

    @Test
    void recordsValidPivotIndexAfterEachPartition() {
        SortResult result = quickSort.sort(
                new int[]{9, 4, 7, 3, 8, 2, 6, 1, 5}
        );

        for (SortStep step : result.steps()) {
            int[] highlighted =
                    step.highlightedIndices();

            assertEquals(1, highlighted.length);

            int pivotIndex = highlighted[0];

            assertTrue(pivotIndex >= 0);
            assertTrue(
                    pivotIndex
                            < result.sortedArray().length
            );
        }
    }

    @Test
    void recordsRandomPivotInformation() {
        SortResult result = quickSort.sort(
                new int[]{4, 2, 7, 3, 6, 1, 5}
        );

        SortStep firstStep = result.steps().get(0);

        assertTrue(
                firstStep.description()
                        .contains("from index")
        );

        assertTrue(
                firstStep.description()
                        .contains("as pivot for range")
        );

        assertTrue(
                firstStep.description()
                        .contains(
                                "pivot finished at index"
                        )
        );
    }

    @Test
    void operationCountsAreInternallyConsistent() {
        SortResult result = quickSort.sort(
                new int[]{4, 2, 7, 3, 6, 1, 5}
        );

        assertTrue(result.comparisonCount() > 0);
        assertTrue(result.swapCount() >= 0);

        assertEquals(
                result.swapCount() * 3,
                result.moveCount()
        );

        assertTrue(result.stepCount() > 0);
    }

    @Test
    void sortsAlreadySortedInput() {
        SortResult result = quickSort.sort(
                new int[]{1, 2, 3, 4, 5, 6}
        );

        assertArrayEquals(
                new int[]{1, 2, 3, 4, 5, 6},
                result.sortedArray()
        );

        assertTrue(result.comparisonCount() > 0);
        assertTrue(result.stepCount() > 0);
    }

    @Test
    void sortsReverseOrderedInput() {
        SortResult result = quickSort.sort(
                new int[]{6, 5, 4, 3, 2, 1}
        );

        assertArrayEquals(
                new int[]{1, 2, 3, 4, 5, 6},
                result.sortedArray()
        );

        assertTrue(result.comparisonCount() > 0);
        assertTrue(result.stepCount() > 0);
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
    void sameSeedProducesSamePartitionSequence() {
        RandomizedQuickSort firstSorter =
                new RandomizedQuickSort(100L);

        RandomizedQuickSort secondSorter =
                new RandomizedQuickSort(100L);

        int[] input = {8, 3, 7, 4, 9, 2, 6, 5, 1};

        SortResult firstResult =
                firstSorter.sort(input);

        SortResult secondResult =
                secondSorter.sort(input);

        assertArrayEquals(
                firstResult.sortedArray(),
                secondResult.sortedArray()
        );

        assertEquals(
                firstResult.comparisonCount(),
                secondResult.comparisonCount()
        );

        assertEquals(
                firstResult.swapCount(),
                secondResult.swapCount()
        );

        assertEquals(
                firstResult.moveCount(),
                secondResult.moveCount()
        );

        assertEquals(
                firstResult.stepCount(),
                secondResult.stepCount()
        );

        for (int index = 0;
                index < firstResult.stepCount();
                index++) {

            assertArrayEquals(
                    firstResult.steps()
                            .get(index)
                            .arrayState(),
                    secondResult.steps()
                            .get(index)
                            .arrayState()
            );

            assertEquals(
                    firstResult.steps()
                            .get(index)
                            .description(),
                    secondResult.steps()
                            .get(index)
                            .description()
            );
        }
    }

    @Test
    void differentSeedsCanProduceDifferentPartitionSequences() {
        RandomizedQuickSort firstSorter =
                new RandomizedQuickSort(10L);

        RandomizedQuickSort secondSorter =
                new RandomizedQuickSort(20L);

        int[] input = {
                9, 4, 7, 3, 8, 2, 6, 1, 5
        };

        SortResult firstResult =
                firstSorter.sort(input);

        SortResult secondResult =
                secondSorter.sort(input);

        assertArrayEquals(
                new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9},
                firstResult.sortedArray()
        );

        assertArrayEquals(
                new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9},
                secondResult.sortedArray()
        );

        boolean differentDescriptions =
                !firstResult.steps()
                        .get(0)
                        .description()
                        .equals(
                                secondResult.steps()
                                        .get(0)
                                        .description()
                        );

        boolean differentFirstStates =
                !arraysEqual(
                        firstResult.steps()
                                .get(0)
                                .arrayState(),
                        secondResult.steps()
                                .get(0)
                                .arrayState()
                );

        assertTrue(
                differentDescriptions
                        || differentFirstStates
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

        assertTrue(firstResult.stepCount() > 0);
        assertTrue(secondResult.stepCount() > 0);
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

        assertFalse(
                firstStep.arrayState()[0] == 999
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
                "O(n log n) expected",
                quickSort.getAverageCase()
        );

        assertEquals(
                "O(n^2)",
                quickSort.getWorstCase()
        );

        assertEquals(
                "O(log n) expected, O(n) worst",
                quickSort.getSpaceComplexity()
        );

        assertFalse(quickSort.isStable());
        assertFalse(quickSort.getName().isBlank());
    }

    private boolean arraysEqual(
            int[] first,
            int[] second
    ) {
        if (first.length != second.length) {
            return false;
        }

        for (int index = 0;
                index < first.length;
                index++) {

            if (first[index] != second[index]) {
                return false;
            }
        }

        return true;
    }
}
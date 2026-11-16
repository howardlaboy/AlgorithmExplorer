package ug.ucu.algorithmexplorer.analysis.benchmark;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import ug.ucu.algorithmexplorer.analysis.BenchmarkInputGenerator;
import ug.ucu.algorithmexplorer.analysis.BenchmarkRunner;
import ug.ucu.algorithmexplorer.analysis.BenchmarkRunner.BenchmarkMeasurement;
import ug.ucu.algorithmexplorer.models.analysis.BenchmarkInputType;
import ug.ucu.algorithmexplorer.models.analysis.BenchmarkResult;

class EfficientBenchmarkAlgorithmsTest {

    @Test
    void mergeSortSortsTypicalInput() {
        BenchmarkMergeSort algorithm =
                new BenchmarkMergeSort();

        int[] values =
                {5, 2, 4, 6, 1, 3};

        BenchmarkMeasurement measurement =
                algorithm.sortAndMeasure(values);

        assertArrayEquals(
                new int[]{1, 2, 3, 4, 5, 6},
                values
        );

        assertEquals(
                "Merge Sort",
                algorithm.getName()
        );

        assertEquals(
                11,
                measurement.comparisonCount()
        );

        assertEquals(
                0,
                measurement.swapCount()
        );

        assertEquals(
                32,
                measurement.moveCount()
        );

        assertTrue(
                measurement.executionTimeNanos() >= 0
        );
    }

    @Test
    void mergeSortHandlesOrderedInputs() {
        BenchmarkMergeSort algorithm =
                new BenchmarkMergeSort();

        int[] sorted = {1, 2, 3, 4};

        BenchmarkMeasurement sortedMeasurement =
                algorithm.sortAndMeasure(sorted);

        assertArrayEquals(
                new int[]{1, 2, 3, 4},
                sorted
        );

        assertEquals(
                4,
                sortedMeasurement.comparisonCount()
        );

        assertEquals(
                16,
                sortedMeasurement.moveCount()
        );

        int[] reverse = {4, 3, 2, 1};

        BenchmarkMeasurement reverseMeasurement =
                algorithm.sortAndMeasure(reverse);

        assertArrayEquals(
                new int[]{1, 2, 3, 4},
                reverse
        );

        assertEquals(
                4,
                reverseMeasurement.comparisonCount()
        );

        assertEquals(
                16,
                reverseMeasurement.moveCount()
        );
    }

    @Test
    void firstPivotQuickSortSortsTypicalInput() {
        BenchmarkFirstPivotQuickSort algorithm =
                new BenchmarkFirstPivotQuickSort();

        int[] values =
                {4, 2, 7, 3, 6, 1, 5};

        BenchmarkMeasurement measurement =
                algorithm.sortAndMeasure(values);

        assertArrayEquals(
                new int[]{1, 2, 3, 4, 5, 6, 7},
                values
        );

        assertEquals(
                "Quick Sort - First Pivot",
                algorithm.getName()
        );

        assertEquals(
                11,
                measurement.comparisonCount()
        );

        assertEquals(
                5,
                measurement.swapCount()
        );

        assertEquals(
                15,
                measurement.moveCount()
        );
    }

    @Test
    void firstPivotQuickSortCountsSortedWorstCase() {
        BenchmarkFirstPivotQuickSort algorithm =
                new BenchmarkFirstPivotQuickSort();

        int[] values =
                {1, 2, 3, 4, 5, 6};

        BenchmarkMeasurement measurement =
                algorithm.sortAndMeasure(values);

        assertArrayEquals(
                new int[]{1, 2, 3, 4, 5, 6},
                values
        );

        assertEquals(
                15,
                measurement.comparisonCount()
        );

        assertEquals(
                0,
                measurement.swapCount()
        );

        assertEquals(
                0,
                measurement.moveCount()
        );
    }

    @Test
    void firstPivotQuickSortHandlesReverseInput() {
        BenchmarkFirstPivotQuickSort algorithm =
                new BenchmarkFirstPivotQuickSort();

        int[] values =
                {5, 4, 3, 2, 1};

        BenchmarkMeasurement measurement =
                algorithm.sortAndMeasure(values);

        assertArrayEquals(
                new int[]{1, 2, 3, 4, 5},
                values
        );

        assertEquals(
                10,
                measurement.comparisonCount()
        );

        assertEquals(
                2,
                measurement.swapCount()
        );

        assertEquals(
                6,
                measurement.moveCount()
        );
    }

    @Test
    void randomizedQuickSortSortsTypicalInput() {
        BenchmarkRandomizedQuickSort algorithm =
                new BenchmarkRandomizedQuickSort(42L);

        int[] values =
                {4, 2, 7, 3, 6, 1, 5};

        BenchmarkMeasurement measurement =
                algorithm.sortAndMeasure(values);

        assertArrayEquals(
                new int[]{1, 2, 3, 4, 5, 6, 7},
                values
        );

        assertEquals(
                "Quick Sort - Randomized Pivot",
                algorithm.getName()
        );

        assertTrue(
                measurement.comparisonCount() > 0
        );

        assertEquals(
                measurement.swapCount() * 3,
                measurement.moveCount()
        );
    }

    @Test
    void randomizedQuickSortSortsOrderedInputs() {
        BenchmarkRandomizedQuickSort sortedAlgorithm =
                new BenchmarkRandomizedQuickSort(42L);

        int[] sorted =
                {1, 2, 3, 4, 5, 6, 7, 8};

        sortedAlgorithm.sortAndMeasure(sorted);

        assertArrayEquals(
                new int[]{1, 2, 3, 4, 5, 6, 7, 8},
                sorted
        );

        BenchmarkRandomizedQuickSort reverseAlgorithm =
                new BenchmarkRandomizedQuickSort(42L);

        int[] reverse =
                {8, 7, 6, 5, 4, 3, 2, 1};

        reverseAlgorithm.sortAndMeasure(reverse);

        assertArrayEquals(
                new int[]{1, 2, 3, 4, 5, 6, 7, 8},
                reverse
        );
    }

    @Test
    void fixedSeedProducesRepeatableMeasurements() {
        int[] firstValues =
                {9, 4, 7, 3, 8, 2, 6, 1, 5};

        int[] secondValues =
                {9, 4, 7, 3, 8, 2, 6, 1, 5};

        BenchmarkRandomizedQuickSort first =
                new BenchmarkRandomizedQuickSort(100L);

        BenchmarkRandomizedQuickSort second =
                new BenchmarkRandomizedQuickSort(100L);

        BenchmarkMeasurement firstMeasurement =
                first.sortAndMeasure(firstValues);

        BenchmarkMeasurement secondMeasurement =
                second.sortAndMeasure(secondValues);

        assertArrayEquals(
                firstValues,
                secondValues
        );

        assertEquals(
                firstMeasurement.comparisonCount(),
                secondMeasurement.comparisonCount()
        );

        assertEquals(
                firstMeasurement.swapCount(),
                secondMeasurement.swapCount()
        );

        assertEquals(
                firstMeasurement.moveCount(),
                secondMeasurement.moveCount()
        );
    }

    @Test
    void efficientAlgorithmsHandleDuplicatesAndNegatives() {
        int[] expected =
                {-1, -1, 0, 3, 3};

        int[] mergeValues =
                {3, -1, 3, 0, -1};

        int[] firstPivotValues =
                {3, -1, 3, 0, -1};

        int[] randomizedValues =
                {3, -1, 3, 0, -1};

        new BenchmarkMergeSort()
                .sortAndMeasure(mergeValues);

        new BenchmarkFirstPivotQuickSort()
                .sortAndMeasure(firstPivotValues);

        new BenchmarkRandomizedQuickSort(42L)
                .sortAndMeasure(randomizedValues);

        assertArrayEquals(
                expected,
                mergeValues
        );

        assertArrayEquals(
                expected,
                firstPivotValues
        );

        assertArrayEquals(
                expected,
                randomizedValues
        );
    }

    @Test
    void algorithmsRejectNullAndEmptyInput() {
        BenchmarkMergeSort merge =
                new BenchmarkMergeSort();

        BenchmarkFirstPivotQuickSort firstPivot =
                new BenchmarkFirstPivotQuickSort();

        BenchmarkRandomizedQuickSort randomized =
                new BenchmarkRandomizedQuickSort(42L);

        assertThrows(
                IllegalArgumentException.class,
                () -> merge.sortAndMeasure(null)
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> merge.sortAndMeasure(new int[0])
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> firstPivot.sortAndMeasure(null)
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> firstPivot.sortAndMeasure(new int[0])
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> randomized.sortAndMeasure(null)
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> randomized.sortAndMeasure(new int[0])
        );
    }

    @Test
    void runnerBenchmarksMergeSort() {
        BenchmarkRunner runner =
                new BenchmarkRunner();

        BenchmarkInputGenerator generator =
                new BenchmarkInputGenerator(42L);

        BenchmarkResult result = runner.run(
                new BenchmarkMergeSort(),
                BenchmarkInputType.RANDOM,
                1_000,
                3,
                generator
        );

        assertEquals(
                "Merge Sort",
                result.algorithmName()
        );

        assertEquals(
                1_000,
                result.inputSize()
        );

        assertEquals(
                3,
                result.repetitions()
        );

        assertTrue(
                result.averageComparisons() > 0
        );

        assertTrue(
                result.averageMoves() > 0
        );

        assertTrue(
                result.minimumTimeNanos()
                        <= result.maximumTimeNanos()
        );
    }

    @Test
    void iterativeQuickSortHandlesLargeSortedArray() {
        BenchmarkFirstPivotQuickSort algorithm =
                new BenchmarkFirstPivotQuickSort();

        int size = 2_000;
        int[] values = new int[size];

        for (int index = 0;
                index < values.length;
                index++) {

            values[index] = index;
        }

        BenchmarkMeasurement measurement =
                algorithm.sortAndMeasure(values);

        for (int index = 0;
                index < values.length;
                index++) {

            assertEquals(index, values[index]);
        }

        long expectedComparisons =
                (long) size
                        * (size - 1)
                        / 2;

        assertEquals(
                expectedComparisons,
                measurement.comparisonCount()
        );
    }
}
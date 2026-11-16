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

class QuadraticBenchmarkAlgorithmsTest {

    @Test
    void benchmarkBubbleSortSortsInPlace() {
        BenchmarkBubbleSort algorithm =
                new BenchmarkBubbleSort();

        int[] values = {5, 1, 4, 2, 3};

        BenchmarkMeasurement measurement =
                algorithm.sortAndMeasure(values);

        assertArrayEquals(
                new int[]{1, 2, 3, 4, 5},
                values
        );

        assertEquals(
                "Bubble Sort",
                algorithm.getName()
        );

        assertTrue(
                measurement.comparisonCount() > 0
        );

        assertEquals(
                measurement.swapCount() * 3,
                measurement.moveCount()
        );

        assertTrue(
                measurement.executionTimeNanos() >= 0
        );
    }

    @Test
    void benchmarkBubbleSortStopsEarlyOnSortedInput() {
        BenchmarkBubbleSort algorithm =
                new BenchmarkBubbleSort();

        int[] values = {1, 2, 3, 4, 5};

        BenchmarkMeasurement measurement =
                algorithm.sortAndMeasure(values);

        assertArrayEquals(
                new int[]{1, 2, 3, 4, 5},
                values
        );

        assertEquals(
                4,
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
    void benchmarkBubbleSortCountsReverseInput() {
        BenchmarkBubbleSort algorithm =
                new BenchmarkBubbleSort();

        int[] values = {5, 4, 3, 2, 1};

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
                10,
                measurement.swapCount()
        );

        assertEquals(
                30,
                measurement.moveCount()
        );
    }

    @Test
    void benchmarkSelectionSortSortsInPlace() {
        BenchmarkSelectionSort algorithm =
                new BenchmarkSelectionSort();

        int[] values = {64, 25, 12, 22, 11};

        BenchmarkMeasurement measurement =
                algorithm.sortAndMeasure(values);

        assertArrayEquals(
                new int[]{11, 12, 22, 25, 64},
                values
        );

        assertEquals(
                "Selection Sort",
                algorithm.getName()
        );

        assertEquals(
                10,
                measurement.comparisonCount()
        );

        assertEquals(
                3,
                measurement.swapCount()
        );

        assertEquals(
                9,
                measurement.moveCount()
        );
    }

    @Test
    void selectionSortAlwaysPerformsQuadraticComparisons() {
        BenchmarkSelectionSort algorithm =
                new BenchmarkSelectionSort();

        int[] sorted = {1, 2, 3, 4, 5};

        BenchmarkMeasurement measurement =
                algorithm.sortAndMeasure(sorted);

        assertEquals(
                10,
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
    void benchmarkInsertionSortSortsInPlace() {
        BenchmarkInsertionSort algorithm =
                new BenchmarkInsertionSort();

        int[] values = {5, 2, 4, 6, 1, 3};

        BenchmarkMeasurement measurement =
                algorithm.sortAndMeasure(values);

        assertArrayEquals(
                new int[]{1, 2, 3, 4, 5, 6},
                values
        );

        assertEquals(
                "Insertion Sort",
                algorithm.getName()
        );

        assertEquals(
                12,
                measurement.comparisonCount()
        );

        assertEquals(
                0,
                measurement.swapCount()
        );

        assertEquals(
                14,
                measurement.moveCount()
        );
    }

    @Test
    void insertionSortUsesLinearWorkOnSortedInput() {
        BenchmarkInsertionSort algorithm =
                new BenchmarkInsertionSort();

        int[] values = {1, 2, 3, 4, 5};

        BenchmarkMeasurement measurement =
                algorithm.sortAndMeasure(values);

        assertArrayEquals(
                new int[]{1, 2, 3, 4, 5},
                values
        );

        assertEquals(
                4,
                measurement.comparisonCount()
        );

        assertEquals(
                4,
                measurement.moveCount()
        );
    }

    @Test
    void algorithmsHandleDuplicatesAndNegativeValues() {
        int[] expected = {-1, -1, 0, 3, 3};

        int[] bubbleValues =
                {3, -1, 3, 0, -1};

        int[] selectionValues =
                {3, -1, 3, 0, -1};

        int[] insertionValues =
                {3, -1, 3, 0, -1};

        new BenchmarkBubbleSort()
                .sortAndMeasure(bubbleValues);

        new BenchmarkSelectionSort()
                .sortAndMeasure(selectionValues);

        new BenchmarkInsertionSort()
                .sortAndMeasure(insertionValues);

        assertArrayEquals(
                expected,
                bubbleValues
        );

        assertArrayEquals(
                expected,
                selectionValues
        );

        assertArrayEquals(
                expected,
                insertionValues
        );
    }

    @Test
    void algorithmsRejectNullAndEmptyInput() {
        BenchmarkBubbleSort bubble =
                new BenchmarkBubbleSort();

        BenchmarkSelectionSort selection =
                new BenchmarkSelectionSort();

        BenchmarkInsertionSort insertion =
                new BenchmarkInsertionSort();

        assertThrows(
                IllegalArgumentException.class,
                () -> bubble.sortAndMeasure(null)
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> bubble.sortAndMeasure(new int[0])
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> selection.sortAndMeasure(null)
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> selection.sortAndMeasure(new int[0])
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> insertion.sortAndMeasure(null)
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> insertion.sortAndMeasure(new int[0])
        );
    }

    @Test
    void runnerAveragesBubbleSortOverThreeRuns() {
        BenchmarkRunner runner =
                new BenchmarkRunner();

        BenchmarkInputGenerator generator =
                new BenchmarkInputGenerator(42L);

        BenchmarkResult result = runner.run(
                new BenchmarkBubbleSort(),
                BenchmarkInputType.RANDOM,
                100,
                3,
                generator
        );

        assertEquals(
                "Bubble Sort",
                result.algorithmName()
        );

        assertEquals(
                BenchmarkInputType.RANDOM,
                result.inputType()
        );

        assertEquals(
                100,
                result.inputSize()
        );

        assertEquals(
                3,
                result.repetitions()
        );

        assertTrue(
                result.averageTimeNanos() >= 0
        );

        assertTrue(
                result.minimumTimeNanos()
                        <= result.maximumTimeNanos()
        );

        assertTrue(
                result.averageComparisons() > 0
        );
    }

    @Test
    void runnerRejectsFewerThanThreeRuns() {
        BenchmarkRunner runner =
                new BenchmarkRunner();

        BenchmarkInputGenerator generator =
                new BenchmarkInputGenerator(42L);

        assertThrows(
                IllegalArgumentException.class,
                () -> runner.run(
                        new BenchmarkBubbleSort(),
                        BenchmarkInputType.RANDOM,
                        100,
                        2,
                        generator
                )
        );
    }
}
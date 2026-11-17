package ug.ucu.algorithmexplorer.controllers;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ug.ucu.algorithmexplorer.controllers.SortingController.AlgorithmInformation;
import ug.ucu.algorithmexplorer.models.sorting.SortResult;

class SortingControllerTest {

    private SortingController controller;

    @BeforeEach
    void setUp() {
        controller = new SortingController(42L);
    }

    @Test
    void parsesCommaSeparatedInput() {
        assertArrayEquals(
                new int[]{5, 2, 8, 1},
                controller.parseInput("5, 2, 8, 1")
        );
    }

    @Test
    void parsesSpaceSeparatedInput() {
        assertArrayEquals(
                new int[]{5, 2, 8, 1},
                controller.parseInput("5 2 8 1")
        );
    }

    @Test
    void parsesSemicolonSeparatedInput() {
        assertArrayEquals(
                new int[]{5, 2, 8, 1},
                controller.parseInput("5; 2; 8; 1")
        );
    }

    @Test
    void parsesMixedSeparatorsAndNegativeValues() {
        assertArrayEquals(
                new int[]{5, -2, 8, 0, -11},
                controller.parseInput(
                        "5, -2; 8 0, -11"
                )
        );
    }

    @Test
    void rejectsEmptyInput() {
        assertThrows(
                IllegalArgumentException.class,
                () -> controller.parseInput(null)
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> controller.parseInput("")
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> controller.parseInput("   ")
        );
    }

    @Test
    void reportsInvalidTokenPosition() {
        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> controller.parseInput(
                                "5, 2, hello, 1"
                        )
                );

        assertTrue(
                exception.getMessage()
                        .contains("position 3")
        );

        assertTrue(
                exception.getMessage()
                        .contains("hello")
        );
    }

    @Test
    void generatesRequestedNumberOfRandomValues() {
        int[] values =
                controller.generateRandomInput(20);

        assertEquals(20, values.length);

        for (int value : values) {
            assertTrue(value >= -99);
            assertTrue(value <= 99);
        }
    }

    @Test
    void seededRandomGenerationIsRepeatable() {
        SortingController first =
                new SortingController(100L);

        SortingController second =
                new SortingController(100L);

        assertArrayEquals(
                first.generateRandomInput(10),
                second.generateRandomInput(10)
        );
    }

    @Test
    void rejectsInvalidRandomSize() {
        assertThrows(
                IllegalArgumentException.class,
                () -> controller.generateRandomInput(0)
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> controller.generateRandomInput(-5)
        );
    }

    @Test
    void exposesAllSixAlgorithmChoices() {
        assertArrayEquals(
                new String[]{
                        SortingController.BUBBLE_SORT,
                        SortingController.SELECTION_SORT,
                        SortingController.INSERTION_SORT,
                        SortingController.MERGE_SORT,
                        SortingController.FIRST_PIVOT_QUICK_SORT,
                        SortingController.RANDOMIZED_QUICK_SORT
                },
                controller.getAlgorithmNames()
        );
    }

    @Test
    void runsEveryAlgorithmChoice() {
        int[] input = {5, 1, 4, 2, 3};
        int[] expected = {1, 2, 3, 4, 5};

        for (String algorithmName
                : controller.getAlgorithmNames()) {

            SortResult result =
                    controller.runAlgorithm(
                            algorithmName,
                            input
                    );

            assertArrayEquals(
                    expected,
                    result.sortedArray()
            );

            assertEquals(
                    algorithmName,
                    result.algorithmName()
            );
        }
    }

    @Test
    void individualSortDoesNotModifyInput() {
        int[] input = {5, 1, 4, 2, 3};

        controller.runAlgorithm(
                SortingController.MERGE_SORT,
                input
        );

        assertArrayEquals(
                new int[]{5, 1, 4, 2, 3},
                input
        );
    }

    @Test
    void compareAllRunsFiveRequiredAlgorithms() {
        int[] input = {5, 1, 4, 2, 3};

        List<SortResult> results =
                controller.compareAll(input);

        assertEquals(5, results.size());

        assertEquals(
                "Bubble Sort",
                results.get(0).algorithmName()
        );

        assertEquals(
                "Selection Sort",
                results.get(1).algorithmName()
        );

        assertEquals(
                "Insertion Sort",
                results.get(2).algorithmName()
        );

        assertEquals(
                "Merge Sort",
                results.get(3).algorithmName()
        );

        assertEquals(
                "Quick Sort - First Pivot",
                results.get(4).algorithmName()
        );
    }

    @Test
    void compareAllUsesSameInputForEveryAlgorithm() {
        int[] input = {5, 1, 4, 2, 3};
        int[] expected = {1, 2, 3, 4, 5};

        List<SortResult> results =
                controller.compareAll(input);

        for (SortResult result : results) {
            assertArrayEquals(
                    input,
                    result.originalArray()
            );

            assertArrayEquals(
                    expected,
                    result.sortedArray()
            );
        }

        assertArrayEquals(
                new int[]{5, 1, 4, 2, 3},
                input
        );
    }

    @Test
    void compareAllReturnsUnmodifiableList() {
        List<SortResult> results =
                controller.compareAll(
                        new int[]{3, 2, 1}
                );

        assertThrows(
                UnsupportedOperationException.class,
                () -> results.clear()
        );
    }

    @Test
    void rejectsUnknownAlgorithm() {
        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> controller.runAlgorithm(
                                "Unknown Sort",
                                new int[]{3, 2, 1}
                        )
                );

        assertTrue(
                exception.getMessage()
                        .contains("Unknown sorting algorithm")
        );
    }

    @Test
    void rejectsMissingAlgorithmSelection() {
        assertThrows(
                IllegalArgumentException.class,
                () -> controller.runAlgorithm(
                        null,
                        new int[]{3, 2, 1}
                )
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> controller.runAlgorithm(
                        " ",
                        new int[]{3, 2, 1}
                )
        );
    }

    @Test
    void rejectsInvalidCompareAllInput() {
        assertThrows(
                IllegalArgumentException.class,
                () -> controller.compareAll(null)
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> controller.compareAll(new int[0])
        );
    }

    @Test
    void returnsBubbleSortInformation() {
        AlgorithmInformation information =
                controller.getAlgorithmInformation(
                        SortingController.BUBBLE_SORT
                );

        assertEquals(
                "Bubble Sort",
                information.name()
        );

        assertEquals(
                "O(n)",
                information.bestCase()
        );

        assertEquals(
                "O(n^2)",
                information.averageCase()
        );

        assertEquals(
                "O(n^2)",
                information.worstCase()
        );

        assertEquals(
                "O(1)",
                information.spaceComplexity()
        );

        assertTrue(information.stable());
    }

    @Test
    void returnsRandomizedQuickSortInformation() {
        AlgorithmInformation information =
                controller.getAlgorithmInformation(
                        SortingController.RANDOMIZED_QUICK_SORT
                );

        assertEquals(
                "O(n log n) expected",
                information.averageCase()
        );

        assertEquals(
                "O(n^2)",
                information.worstCase()
        );

        assertFalse(information.stable());
    }

    @Test
    void formatsArrayForDisplay() {
        assertEquals(
                "[ 5, -2, 8, 1 ]",
                controller.formatArray(
                        new int[]{5, -2, 8, 1}
                )
        );

        assertEquals(
                "[  ]",
                controller.formatArray(new int[0])
        );
    }

    @Test
    void formatArrayRejectsNull() {
        assertThrows(
                IllegalArgumentException.class,
                () -> controller.formatArray(null)
        );
    }
}
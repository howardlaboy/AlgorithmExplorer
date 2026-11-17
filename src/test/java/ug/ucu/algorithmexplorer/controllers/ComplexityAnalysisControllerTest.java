package ug.ucu.algorithmexplorer.controllers;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ug.ucu.algorithmexplorer.controllers.ComplexityAnalysisController.CancellationToken;
import ug.ucu.algorithmexplorer.models.analysis.BenchmarkInputType;
import ug.ucu.algorithmexplorer.models.analysis.ExperimentConfiguration;
import ug.ucu.algorithmexplorer.models.analysis.ExperimentProgress;
import ug.ucu.algorithmexplorer.models.analysis.ExperimentReport;

class ComplexityAnalysisControllerTest {

    private ComplexityAnalysisController controller;

    @BeforeEach
    void setUp() {
        controller =
                new ComplexityAnalysisController(
                        42L,
                        100L
                );
    }

    @Test
    void exposesRequiredStandardSizes() {
        assertArrayEquals(
                new int[]{
                        100,
                        500,
                        1_000,
                        2_000,
                        5_000,
                        10_000
                },
                controller.getStandardSizes()
        );
    }

    @Test
    void exposesLargeEfficientAlgorithmSizes() {
        assertArrayEquals(
                new int[]{
                        50_000,
                        100_000
                },
                controller.getLargeSizes()
        );
    }

    @Test
    void returnedSizeArraysAreIndependent() {
        int[] standard =
                controller.getStandardSizes();

        standard[0] = 999;

        assertArrayEquals(
                new int[]{
                        100,
                        500,
                        1_000,
                        2_000,
                        5_000,
                        10_000
                },
                controller.getStandardSizes()
        );
    }

    @Test
    void exposesSixBenchmarkAlgorithms() {
        assertArrayEquals(
                new String[]{
                        ComplexityAnalysisController.BUBBLE_SORT,
                        ComplexityAnalysisController.SELECTION_SORT,
                        ComplexityAnalysisController.INSERTION_SORT,
                        ComplexityAnalysisController.MERGE_SORT,
                        ComplexityAnalysisController.FIRST_PIVOT_QUICK_SORT,
                        ComplexityAnalysisController.RANDOMIZED_QUICK_SORT
                },
                controller.getAlgorithmNames()
        );
    }

    @Test
    void exposesThreeInputTypes() {
        assertArrayEquals(
                BenchmarkInputType.values(),
                controller.getInputTypes()
        );
    }

    @Test
    void standardPlanContainsAllCombinations() {
        List<ExperimentConfiguration> plan =
                controller.createStandardPlan(3);

        /*
         * 6 algorithms x 3 input types x 6 sizes.
         */
        assertEquals(108, plan.size());

        ExperimentConfiguration first =
                plan.get(0);

        assertEquals(
                ComplexityAnalysisController.BUBBLE_SORT,
                first.algorithmName()
        );

        assertEquals(
                BenchmarkInputType.RANDOM,
                first.inputType()
        );

        assertEquals(100, first.inputSize());
        assertEquals(3, first.repetitions());
    }

    @Test
    void largePlanContainsEfficientAlgorithmsOnly() {
        List<ExperimentConfiguration> plan =
                controller.createLargeInputPlan(3);

        /*
         * 3 algorithms x 3 input types x 2 sizes.
         */
        assertEquals(18, plan.size());

        for (ExperimentConfiguration configuration
                : plan) {

            assertFalse(
                    configuration.algorithmName()
                            .equals(
                                    ComplexityAnalysisController.BUBBLE_SORT
                            )
            );

            assertFalse(
                    configuration.algorithmName()
                            .equals(
                                    ComplexityAnalysisController.SELECTION_SORT
                            )
            );

            assertFalse(
                    configuration.algorithmName()
                            .equals(
                                    ComplexityAnalysisController.INSERTION_SORT
                            )
            );

            assertTrue(
                    configuration.inputSize() == 50_000
                            || configuration.inputSize()
                            == 100_000
            );
        }
    }

    @Test
    void createsCustomPlan() {
        List<ExperimentConfiguration> plan =
                controller.createCustomPlan(
                        new String[]{
                                ComplexityAnalysisController.MERGE_SORT,
                                ComplexityAnalysisController.RANDOMIZED_QUICK_SORT
                        },
                        new BenchmarkInputType[]{
                                BenchmarkInputType.RANDOM,
                                BenchmarkInputType.SORTED
                        },
                        new int[]{
                                100,
                                500
                        },
                        3
                );

        assertEquals(8, plan.size());
    }

    @Test
    void rejectsInvalidPlansAndRepetitions() {
        assertThrows(
                IllegalArgumentException.class,
                () -> controller.createStandardPlan(2)
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> controller.createCustomPlan(
                        new String[0],
                        BenchmarkInputType.values(),
                        new int[]{100},
                        3
                )
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> controller.createCustomPlan(
                        new String[]{
                                ComplexityAnalysisController.MERGE_SORT
                        },
                        new BenchmarkInputType[0],
                        new int[]{100},
                        3
                )
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> controller.createCustomPlan(
                        new String[]{
                                ComplexityAnalysisController.MERGE_SORT
                        },
                        BenchmarkInputType.values(),
                        new int[]{0},
                        3
                )
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> controller.runPlan(
                        List.of(),
                        false
                )
        );
    }

    @Test
    void skipsLargeOrderedFirstPivotByDefault() {
        ExperimentConfiguration configuration =
                new ExperimentConfiguration(
                        ComplexityAnalysisController.FIRST_PIVOT_QUICK_SORT,
                        BenchmarkInputType.SORTED,
                        50_000,
                        3
                );

        assertTrue(
                controller.shouldSkip(
                        configuration,
                        false
                )
        );

        assertFalse(
                controller.shouldSkip(
                        configuration,
                        true
                )
        );
    }

    @Test
    void doesNotSkipLargeRandomFirstPivot() {
        ExperimentConfiguration configuration =
                new ExperimentConfiguration(
                        ComplexityAnalysisController.FIRST_PIVOT_QUICK_SORT,
                        BenchmarkInputType.RANDOM,
                        50_000,
                        3
                );

        assertFalse(
                controller.shouldSkip(
                        configuration,
                        false
                )
        );
    }

    @Test
    void skipsQuadraticAlgorithmsBeyondStandardLimit() {
        ExperimentConfiguration configuration =
                new ExperimentConfiguration(
                        ComplexityAnalysisController.BUBBLE_SORT,
                        BenchmarkInputType.RANDOM,
                        50_000,
                        3
                );

        assertTrue(
                controller.shouldSkip(
                        configuration,
                        false
                )
        );

        /*
         * The extended flag only overrides first-pivot ordered-input
         * protection. It does not enable enormous quadratic runs.
         */
        assertTrue(
                controller.shouldSkip(
                        configuration,
                        true
                )
        );
    }

    @Test
    void executesSmallCustomPlanAndReportsProgress() {
        List<ExperimentConfiguration> plan =
                controller.createCustomPlan(
                        new String[]{
                                ComplexityAnalysisController.BUBBLE_SORT,
                                ComplexityAnalysisController.MERGE_SORT
                        },
                        new BenchmarkInputType[]{
                                BenchmarkInputType.RANDOM
                        },
                        new int[]{
                                100
                        },
                        3
                );

        List<ExperimentProgress> progressUpdates =
                new ArrayList<>();

        ExperimentReport report =
                controller.runPlan(
                        plan,
                        false,
                        progressUpdates::add,
                        new CancellationToken()
                );

        assertFalse(report.cancelled());
        assertEquals(2, report.completedCount());
        assertEquals(0, report.skippedCount());
        assertEquals(2, report.requestedConfigurations());

        assertEquals(2, progressUpdates.size());

        assertEquals(
                50,
                progressUpdates.get(0).percentage()
        );

        assertEquals(
                100,
                progressUpdates.get(1).percentage()
        );

        assertFalse(
                progressUpdates.get(0).skipped()
        );
    }

    @Test
    void reportsSkippedConfigurationProgress() {
        List<ExperimentConfiguration> plan =
                List.of(
                        new ExperimentConfiguration(
                                ComplexityAnalysisController.MERGE_SORT,
                                BenchmarkInputType.RANDOM,
                                100,
                                3
                        ),
                        new ExperimentConfiguration(
                                ComplexityAnalysisController.FIRST_PIVOT_QUICK_SORT,
                                BenchmarkInputType.SORTED,
                                50_000,
                                3
                        )
                );

        List<ExperimentProgress> updates =
                new ArrayList<>();

        ExperimentReport report =
                controller.runPlan(
                        plan,
                        false,
                        updates::add,
                        new CancellationToken()
                );

        assertFalse(report.cancelled());
        assertEquals(1, report.completedCount());
        assertEquals(1, report.skippedCount());

        assertEquals(2, updates.size());
        assertFalse(updates.get(0).skipped());
        assertTrue(updates.get(1).skipped());

        assertTrue(
                updates.get(1)
                        .message()
                        .contains("Skipped")
        );

        assertEquals(
                100,
                updates.get(1).percentage()
        );
    }

    @Test
    void supportsCancellationBetweenConfigurations() {
        List<ExperimentConfiguration> plan =
                controller.createCustomPlan(
                        new String[]{
                                ComplexityAnalysisController.MERGE_SORT
                        },
                        new BenchmarkInputType[]{
                                BenchmarkInputType.RANDOM
                        },
                        new int[]{
                                100,
                                500,
                                1_000
                        },
                        3
                );

        CancellationToken token =
                new CancellationToken();

        List<ExperimentProgress> updates =
                new ArrayList<>();

        ExperimentReport report =
                controller.runPlan(
                        plan,
                        false,
                        progress -> {
                            updates.add(progress);

                            if (progress.completedConfigurations()
                                    == 1) {

                                token.cancel();
                            }
                        },
                        token
                );

        assertTrue(report.cancelled());
        assertEquals(1, report.completedCount());
        assertEquals(0, report.skippedCount());
        assertEquals(1, updates.size());
        assertTrue(token.isCancellationRequested());
    }

    @Test
    void reportListsAreUnmodifiable() {
        List<ExperimentConfiguration> plan =
                List.of(
                        new ExperimentConfiguration(
                                ComplexityAnalysisController.MERGE_SORT,
                                BenchmarkInputType.RANDOM,
                                100,
                                3
                        )
                );

        ExperimentReport report =
                controller.runPlan(
                        plan,
                        false
                );

        assertThrows(
                UnsupportedOperationException.class,
                () -> report.results().clear()
        );

        assertThrows(
                UnsupportedOperationException.class,
                () -> report.skippedConfigurations()
                        .clear()
        );
    }

    @Test
    void configurationProvidesReadableDescription() {
        ExperimentConfiguration configuration =
                new ExperimentConfiguration(
                        ComplexityAnalysisController.MERGE_SORT,
                        BenchmarkInputType.REVERSE_SORTED,
                        1_000,
                        3
                );

        assertEquals(
                "Merge Sort | Reverse Sorted "
                        + "| n = 1000 | repetitions = 3",
                configuration.description()
        );
    }

    @Test
    void modelValidationRejectsInvalidValues() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new ExperimentConfiguration(
                        "",
                        BenchmarkInputType.RANDOM,
                        100,
                        3
                )
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> new ExperimentConfiguration(
                        "Example",
                        null,
                        100,
                        3
                )
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> new ExperimentConfiguration(
                        "Example",
                        BenchmarkInputType.RANDOM,
                        0,
                        3
                )
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> new ExperimentConfiguration(
                        "Example",
                        BenchmarkInputType.RANDOM,
                        100,
                        2
                )
        );
    }
}
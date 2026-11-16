package ug.ucu.algorithmexplorer.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import ug.ucu.algorithmexplorer.analysis.BenchmarkInputGenerator;
import ug.ucu.algorithmexplorer.analysis.BenchmarkRunner;
import ug.ucu.algorithmexplorer.analysis.BenchmarkRunner.BenchmarkSortingAlgorithm;
import ug.ucu.algorithmexplorer.analysis.benchmark.BenchmarkBubbleSort;
import ug.ucu.algorithmexplorer.analysis.benchmark.BenchmarkFirstPivotQuickSort;
import ug.ucu.algorithmexplorer.analysis.benchmark.BenchmarkInsertionSort;
import ug.ucu.algorithmexplorer.analysis.benchmark.BenchmarkMergeSort;
import ug.ucu.algorithmexplorer.analysis.benchmark.BenchmarkRandomizedQuickSort;
import ug.ucu.algorithmexplorer.analysis.benchmark.BenchmarkSelectionSort;
import ug.ucu.algorithmexplorer.models.analysis.BenchmarkInputType;
import ug.ucu.algorithmexplorer.models.analysis.BenchmarkResult;
import ug.ucu.algorithmexplorer.models.analysis.ExperimentConfiguration;
import ug.ucu.algorithmexplorer.models.analysis.ExperimentProgress;
import ug.ucu.algorithmexplorer.models.analysis.ExperimentReport;

/**
 * Coordinates Module D sorting experiments.
 *
 * The controller builds experiment plans, creates benchmark-only
 * sorting implementations, executes repeated experiments, reports
 * progress and supports cooperative cancellation.
 */
public class ComplexityAnalysisController {

    public static final String BUBBLE_SORT =
            "Bubble Sort";

    public static final String SELECTION_SORT =
            "Selection Sort";

    public static final String INSERTION_SORT =
            "Insertion Sort";

    public static final String MERGE_SORT =
            "Merge Sort";

    public static final String FIRST_PIVOT_QUICK_SORT =
            "Quick Sort - First Pivot";

    public static final String RANDOMIZED_QUICK_SORT =
            "Quick Sort - Randomized Pivot";

    private static final int[] STANDARD_SIZES = {
            100,
            500,
            1_000,
            2_000,
            5_000,
            10_000
    };

    private static final int[] LARGE_SIZES = {
            50_000,
            100_000
    };

    /*
     * First-pivot Quick Sort becomes quadratic on ordered input.
     * Configurations beyond this size are skipped unless extended
     * worst-case experiments are explicitly enabled.
     */
    private static final int SAFE_FIRST_PIVOT_ORDERED_LIMIT =
            10_000;

    /*
     * Bubble, Selection and Insertion Sort are quadratic. The default
     * complete plan keeps these algorithms at the required standard
     * maximum of 10,000.
     */
    private static final int QUADRATIC_ALGORITHM_LIMIT =
            10_000;

    private final BenchmarkRunner runner;
    private final long generatorSeed;
    private final long randomizedQuickSortSeed;

    /**
     * Creates a controller with repeatable benchmark seeds.
     */
    public ComplexityAnalysisController() {
        this(
                new BenchmarkRunner(),
                42L,
                100L
        );
    }

    /**
     * Creates a repeatable controller for automated tests.
     *
     * @param generatorSeed benchmark-input seed
     * @param randomizedQuickSortSeed randomized Quick Sort seed
     */
    public ComplexityAnalysisController(
            long generatorSeed,
            long randomizedQuickSortSeed
    ) {
        this(
                new BenchmarkRunner(),
                generatorSeed,
                randomizedQuickSortSeed
        );
    }

    private ComplexityAnalysisController(
            BenchmarkRunner runner,
            long generatorSeed,
            long randomizedQuickSortSeed
    ) {
        if (runner == null) {
            throw new IllegalArgumentException(
                    "Benchmark runner cannot be null."
            );
        }

        this.runner = runner;
        this.generatorSeed = generatorSeed;

        this.randomizedQuickSortSeed =
                randomizedQuickSortSeed;
    }

    /**
     * Returns a defensive copy of the standard project sizes.
     *
     * @return standard benchmark sizes
     */
    public int[] getStandardSizes() {
        return copyArray(STANDARD_SIZES);
    }

    /**
     * Returns a defensive copy of the larger efficient-sort sizes.
     *
     * @return large benchmark sizes
     */
    public int[] getLargeSizes() {
        return copyArray(LARGE_SIZES);
    }

    /**
     * Returns every algorithm supported by Module D.
     *
     * @return benchmark algorithm names
     */
    public String[] getAlgorithmNames() {
        return new String[]{
                BUBBLE_SORT,
                SELECTION_SORT,
                INSERTION_SORT,
                MERGE_SORT,
                FIRST_PIVOT_QUICK_SORT,
                RANDOMIZED_QUICK_SORT
        };
    }

    /**
     * Returns all required input arrangements.
     *
     * @return benchmark input types
     */
    public BenchmarkInputType[] getInputTypes() {
        return BenchmarkInputType.values().clone();
    }

    /**
     * Builds the standard project experiment plan.
     *
     * The five required algorithms are tested at all standard sizes
     * for all three input arrangements. The additional randomized
     * Quick Sort is included as a sixth algorithm.
     *
     * @param repetitions measured repetitions per configuration
     * @return unmodifiable standard plan
     */
    public List<ExperimentConfiguration> createStandardPlan(
            int repetitions
    ) {
        validateRepetitions(repetitions);

        List<ExperimentConfiguration> plan =
                new ArrayList<>();

        for (String algorithmName : getAlgorithmNames()) {
            for (BenchmarkInputType inputType
                    : BenchmarkInputType.values()) {

                for (int size : STANDARD_SIZES) {
                    plan.add(
                            new ExperimentConfiguration(
                                    algorithmName,
                                    inputType,
                                    size,
                                    repetitions
                            )
                    );
                }
            }
        }

        return List.copyOf(plan);
    }

    /**
     * Builds the larger-input plan for efficient algorithms.
     *
     * The plan includes Merge Sort, first-pivot Quick Sort and
     * randomized-pivot Quick Sort at 50,000 and 100,000 for every
     * input arrangement. Unsafe ordered first-pivot configurations
     * are included in the plan but may be skipped during execution.
     *
     * @param repetitions measured repetitions per configuration
     * @return unmodifiable large-input plan
     */
    public List<ExperimentConfiguration> createLargeInputPlan(
            int repetitions
    ) {
        validateRepetitions(repetitions);

        List<ExperimentConfiguration> plan =
                new ArrayList<>();

        String[] efficientAlgorithms = {
                MERGE_SORT,
                FIRST_PIVOT_QUICK_SORT,
                RANDOMIZED_QUICK_SORT
        };

        for (String algorithmName
                : efficientAlgorithms) {

            for (BenchmarkInputType inputType
                    : BenchmarkInputType.values()) {

                for (int size : LARGE_SIZES) {
                    plan.add(
                            new ExperimentConfiguration(
                                    algorithmName,
                                    inputType,
                                    size,
                                    repetitions
                            )
                    );
                }
            }
        }

        return List.copyOf(plan);
    }

    /**
     * Builds one configurable experiment plan.
     *
     * @param algorithms selected algorithm names
     * @param inputTypes selected input arrangements
     * @param sizes selected sizes
     * @param repetitions measured repetitions
     * @return unmodifiable experiment plan
     */
    public List<ExperimentConfiguration> createCustomPlan(
            String[] algorithms,
            BenchmarkInputType[] inputTypes,
            int[] sizes,
            int repetitions
    ) {
        validateRepetitions(repetitions);

        if (algorithms == null
                || algorithms.length == 0) {

            throw new IllegalArgumentException(
                    "Select at least one algorithm."
            );
        }

        if (inputTypes == null
                || inputTypes.length == 0) {

            throw new IllegalArgumentException(
                    "Select at least one input type."
            );
        }

        if (sizes == null || sizes.length == 0) {
            throw new IllegalArgumentException(
                    "Select at least one input size."
            );
        }

        List<ExperimentConfiguration> plan =
                new ArrayList<>();

        for (String algorithmName : algorithms) {
            validateAlgorithmName(algorithmName);

            for (BenchmarkInputType inputType
                    : inputTypes) {

                if (inputType == null) {
                    throw new IllegalArgumentException(
                            "Input type cannot be null."
                    );
                }

                for (int size : sizes) {
                    if (size < 1) {
                        throw new IllegalArgumentException(
                                "Input sizes must be at least 1."
                        );
                    }

                    plan.add(
                            new ExperimentConfiguration(
                                    algorithmName,
                                    inputType,
                                    size,
                                    repetitions
                            )
                    );
                }
            }
        }

        return List.copyOf(plan);
    }

    /**
     * Executes an experiment plan.
     *
     * @param plan configurations to execute
     * @param allowExtendedWorstCase whether unsafe first-pivot
     * ordered configurations may run
     * @param progressListener optional progress receiver
     * @param cancellationToken optional cooperative cancellation token
     * @return completed experiment report
     */
    public ExperimentReport runPlan(
            List<ExperimentConfiguration> plan,
            boolean allowExtendedWorstCase,
            ProgressListener progressListener,
            CancellationToken cancellationToken
    ) {
        validatePlan(plan);

        List<BenchmarkResult> results =
                new ArrayList<>();

        List<ExperimentConfiguration> skipped =
                new ArrayList<>();

        CancellationToken token =
                cancellationToken == null
                        ? new CancellationToken()
                        : cancellationToken;

        int completedConfigurations = 0;

        for (int index = 0;
                index < plan.size();
                index++) {

            if (token.isCancellationRequested()) {
                return new ExperimentReport(
                        results,
                        skipped,
                        true,
                        plan.size()
                );
            }

            ExperimentConfiguration configuration =
                    plan.get(index);

            if (shouldSkip(
                    configuration,
                    allowExtendedWorstCase
            )) {
                skipped.add(configuration);
                completedConfigurations++;

                notifyProgress(
                        progressListener,
                        new ExperimentProgress(
                                completedConfigurations,
                                plan.size(),
                                calculatePercentage(
                                        completedConfigurations,
                                        plan.size()
                                ),
                                configuration,
                                createSkipMessage(configuration),
                                true
                        )
                );

                continue;
            }

            BenchmarkSortingAlgorithm algorithm =
                    createAlgorithm(
                            configuration.algorithmName(),
                            index
                    );

            /*
             * A different deterministic generator stream is used for
             * each configuration.
             */
            BenchmarkInputGenerator generator =
                    new BenchmarkInputGenerator(
                            generatorSeed + index
                    );

            BenchmarkResult result = runner.run(
                    algorithm,
                    configuration.inputType(),
                    configuration.inputSize(),
                    configuration.repetitions(),
                    generator
            );

            results.add(result);
            completedConfigurations++;

            notifyProgress(
                    progressListener,
                    new ExperimentProgress(
                            completedConfigurations,
                            plan.size(),
                            calculatePercentage(
                                    completedConfigurations,
                                    plan.size()
                            ),
                            configuration,
                            "Completed "
                                    + configuration.description(),
                            false
                    )
            );
        }

        return new ExperimentReport(
                results,
                skipped,
                false,
                plan.size()
        );
    }

    /**
     * Convenience overload for a non-cancellable run with no progress
     * receiver.
     *
     * @param plan configurations to execute
     * @param allowExtendedWorstCase extended worst-case flag
     * @return experiment report
     */
    public ExperimentReport runPlan(
            List<ExperimentConfiguration> plan,
            boolean allowExtendedWorstCase
    ) {
        return runPlan(
                plan,
                allowExtendedWorstCase,
                null,
                null
        );
    }

    /**
     * Indicates whether a configuration is skipped by default.
     *
     * @param configuration configuration to evaluate
     * @param allowExtendedWorstCase override flag
     * @return true when the configuration should not run
     */
    public boolean shouldSkip(
            ExperimentConfiguration configuration,
            boolean allowExtendedWorstCase
    ) {
        if (configuration == null) {
            throw new IllegalArgumentException(
                    "Experiment configuration cannot be null."
            );
        }

        if (isQuadraticAlgorithm(
                configuration.algorithmName()
        )
                && configuration.inputSize()
                > QUADRATIC_ALGORITHM_LIMIT) {

            return true;
        }

        boolean firstPivot =
                FIRST_PIVOT_QUICK_SORT.equals(
                        configuration.algorithmName()
                );

        boolean ordered =
                configuration.inputType()
                        == BenchmarkInputType.SORTED
                        || configuration.inputType()
                        == BenchmarkInputType.REVERSE_SORTED;

        boolean beyondSafeLimit =
                configuration.inputSize()
                        > SAFE_FIRST_PIVOT_ORDERED_LIMIT;

        return firstPivot
                && ordered
                && beyondSafeLimit
                && !allowExtendedWorstCase;
    }

    /**
     * Returns the reason a default safety rule skipped a configuration.
     *
     * @param configuration skipped configuration
     * @return readable skip message
     */
    public String createSkipMessage(
            ExperimentConfiguration configuration
    ) {
        if (configuration == null) {
            throw new IllegalArgumentException(
                    "Experiment configuration cannot be null."
            );
        }

        if (isQuadraticAlgorithm(
                configuration.algorithmName()
        )
                && configuration.inputSize()
                > QUADRATIC_ALGORITHM_LIMIT) {

            return "Skipped "
                    + configuration.description()
                    + ": quadratic algorithms are limited to n = "
                    + QUADRATIC_ALGORITHM_LIMIT
                    + " in the default experiment plan.";
        }

        return "Skipped "
                + configuration.description()
                + ": first-pivot Quick Sort on large ordered input "
                + "performs quadratic work. Enable extended "
                + "worst-case experiments to run it.";
    }

    private BenchmarkSortingAlgorithm createAlgorithm(
            String algorithmName,
            int configurationIndex
    ) {
        return switch (algorithmName) {
            case BUBBLE_SORT ->
                    new BenchmarkBubbleSort();

            case SELECTION_SORT ->
                    new BenchmarkSelectionSort();

            case INSERTION_SORT ->
                    new BenchmarkInsertionSort();

            case MERGE_SORT ->
                    new BenchmarkMergeSort();

            case FIRST_PIVOT_QUICK_SORT ->
                    new BenchmarkFirstPivotQuickSort();

            case RANDOMIZED_QUICK_SORT ->
                    new BenchmarkRandomizedQuickSort(
                            randomizedQuickSortSeed
                                    + configurationIndex
                    );

            default ->
                    throw new IllegalArgumentException(
                            "Unknown benchmark algorithm: "
                                    + algorithmName
                    );
        };
    }

    private boolean isQuadraticAlgorithm(
            String algorithmName
    ) {
        return BUBBLE_SORT.equals(algorithmName)
                || SELECTION_SORT.equals(algorithmName)
                || INSERTION_SORT.equals(algorithmName);
    }

    private void validatePlan(
            List<ExperimentConfiguration> plan
    ) {
        if (plan == null || plan.isEmpty()) {
            throw new IllegalArgumentException(
                    "Experiment plan cannot be empty."
            );
        }

        for (ExperimentConfiguration configuration
                : plan) {

            if (configuration == null) {
                throw new IllegalArgumentException(
                        "Experiment plan cannot contain null."
                );
            }

            validateAlgorithmName(
                    configuration.algorithmName()
            );
        }
    }

    private void validateAlgorithmName(
            String algorithmName
    ) {
        if (algorithmName == null
                || algorithmName.isBlank()) {

            throw new IllegalArgumentException(
                    "Algorithm name cannot be empty."
            );
        }

        for (String supported : getAlgorithmNames()) {
            if (supported.equals(algorithmName)) {
                return;
            }
        }

        throw new IllegalArgumentException(
                "Unknown benchmark algorithm: "
                        + algorithmName
        );
    }

    private void validateRepetitions(
            int repetitions
    ) {
        if (repetitions < 3) {
            throw new IllegalArgumentException(
                    "Run every experiment at least 3 times."
            );
        }
    }

    private void notifyProgress(
            ProgressListener listener,
            ExperimentProgress progress
    ) {
        if (listener != null) {
            listener.onProgress(progress);
        }
    }

    private int calculatePercentage(
            int completed,
            int total
    ) {
        return (int) Math.round(
                completed * 100.0 / total
        );
    }

    private int[] copyArray(int[] source) {
        int[] copy = new int[source.length];

        for (int index = 0;
                index < source.length;
                index++) {

            copy[index] = source[index];
        }

        return copy;
    }

    /**
     * Receives progress updates from the experiment controller.
     */
    @FunctionalInterface
    public interface ProgressListener {

        /**
         * Handles one progress update.
         *
         * @param progress progress information
         */
        void onProgress(ExperimentProgress progress);
    }

    /**
     * Thread-safe cooperative cancellation token.
     */
    public static final class CancellationToken {

        private final AtomicBoolean cancellationRequested =
                new AtomicBoolean(false);

        /**
         * Requests cancellation before the next configuration.
         */
        public void cancel() {
            cancellationRequested.set(true);
        }

        /**
         * Indicates whether cancellation was requested.
         *
         * @return true when cancellation has been requested
         */
        public boolean isCancellationRequested() {
            return cancellationRequested.get();
        }
    }
}
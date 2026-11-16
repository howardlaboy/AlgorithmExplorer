package ug.ucu.algorithmexplorer.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import ug.ucu.algorithmexplorer.algorithms.sorting.BubbleSort;
import ug.ucu.algorithmexplorer.algorithms.sorting.FirstPivotQuickSort;
import ug.ucu.algorithmexplorer.algorithms.sorting.InsertionSort;
import ug.ucu.algorithmexplorer.algorithms.sorting.MergeSort;
import ug.ucu.algorithmexplorer.algorithms.sorting.RandomizedQuickSort;
import ug.ucu.algorithmexplorer.algorithms.sorting.SelectionSort;
import ug.ucu.algorithmexplorer.algorithms.sorting.SortingAlgorithm;
import ug.ucu.algorithmexplorer.models.sorting.SortResult;

/**
 * Connects the Sorting Lab interface to the sorting algorithms.
 *
 * The controller parses user input, generates random data, selects
 * algorithms, runs individual sorts and compares all required
 * algorithms using independent copies of the same input.
 */
public class SortingController {

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

    private static final int MIN_RANDOM_VALUE = -99;
    private static final int MAX_RANDOM_VALUE = 99;

    private final Random random;

    /**
     * Creates a controller with normal runtime randomness.
     */
    public SortingController() {
        this(new Random());
    }

    /**
     * Creates a controller with repeatable randomness.
     *
     * This constructor is useful for automated testing.
     *
     * @param seed random-number seed
     */
    public SortingController(long seed) {
        this(new Random(seed));
    }

    private SortingController(Random random) {
        if (random == null) {
            throw new IllegalArgumentException(
                    "Random-number generator cannot be null."
            );
        }

        this.random = random;
    }

    /**
     * Parses integers separated by commas, spaces or semicolons.
     *
     * Accepted examples:
     *
     * 5, 2, 8, 1
     * 5 2 8 1
     * 5; 2; 8; 1
     *
     * @param text user-entered number list
     * @return parsed integer array
     */
    public int[] parseInput(String text) {
        if (text == null || text.isBlank()) {
            throw new IllegalArgumentException(
                    "Enter at least one integer."
            );
        }

        String normalized = text
                .trim()
                .replace(',', ' ')
                .replace(';', ' ');

        String[] tokens = normalized.split("\\s+");

        int[] values = new int[tokens.length];

        for (int index = 0;
                index < tokens.length;
                index++) {

            try {
                values[index] =
                        Integer.parseInt(tokens[index]);

            } catch (NumberFormatException exception) {
                throw new IllegalArgumentException(
                        "Invalid integer at position "
                                + (index + 1)
                                + ": "
                                + tokens[index]
                );
            }
        }

        return values;
    }

    /**
     * Generates random integers from -99 through 99.
     *
     * @param size number of values to generate
     * @return random integer array
     */
    public int[] generateRandomInput(int size) {
        if (size < 1) {
            throw new IllegalArgumentException(
                    "Random input size must be at least 1."
            );
        }

        int[] values = new int[size];

        int range =
                MAX_RANDOM_VALUE - MIN_RANDOM_VALUE + 1;

        for (int index = 0;
                index < values.length;
                index++) {

            values[index] =
                    random.nextInt(range)
                            + MIN_RANDOM_VALUE;
        }

        return values;
    }

    /**
     * Runs the selected sorting algorithm.
     *
     * @param algorithmName selected algorithm name
     * @param input values to sort
     * @return complete sort result
     */
    public SortResult runAlgorithm(
            String algorithmName,
            int[] input
    ) {
        return createAlgorithm(algorithmName)
                .sort(input);
    }

    /**
     * Runs the five required sorting algorithms on the same input.
     *
     * The first-pivot Quick Sort is used as the required Quick Sort.
     * Randomized Quick Sort remains available as the additional option
     * requested by the project brief.
     *
     * @param input values to sort
     * @return results in display order
     */
    public List<SortResult> compareAll(int[] input) {
        validateInputArray(input);

        List<SortResult> results = new ArrayList<>();

        results.add(
                new BubbleSort().sort(input)
        );

        results.add(
                new SelectionSort().sort(input)
        );

        results.add(
                new InsertionSort().sort(input)
        );

        results.add(
                new MergeSort().sort(input)
        );

        results.add(
                new FirstPivotQuickSort().sort(input)
        );

        return List.copyOf(results);
    }

    /**
     * Returns all algorithm choices displayed by the interface.
     *
     * @return algorithm names
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
     * Returns complexity and stability information for an algorithm.
     *
     * @param algorithmName selected algorithm name
     * @return algorithm information
     */
    public AlgorithmInformation getAlgorithmInformation(
            String algorithmName
    ) {
        SortingAlgorithm algorithm =
                createAlgorithm(algorithmName);

        return new AlgorithmInformation(
                algorithm.getName(),
                algorithm.getBestCase(),
                algorithm.getAverageCase(),
                algorithm.getWorstCase(),
                algorithm.getSpaceComplexity(),
                algorithm.isStable()
        );
    }

    /**
     * Formats an integer array for display.
     *
     * @param values values to format
     * @return readable array representation
     */
    public String formatArray(int[] values) {
        if (values == null) {
            throw new IllegalArgumentException(
                    "Array cannot be null."
            );
        }

        StringBuilder text =
                new StringBuilder("[ ");

        for (int index = 0;
                index < values.length;
                index++) {

            text.append(values[index]);

            if (index < values.length - 1) {
                text.append(", ");
            }
        }

        text.append(" ]");

        return text.toString();
    }

    private SortingAlgorithm createAlgorithm(
            String algorithmName
    ) {
        if (algorithmName == null
                || algorithmName.isBlank()) {

            throw new IllegalArgumentException(
                    "Select a sorting algorithm."
            );
        }

        return switch (algorithmName) {
            case BUBBLE_SORT ->
                    new BubbleSort();

            case SELECTION_SORT ->
                    new SelectionSort();

            case INSERTION_SORT ->
                    new InsertionSort();

            case MERGE_SORT ->
                    new MergeSort();

            case FIRST_PIVOT_QUICK_SORT ->
                    new FirstPivotQuickSort();

            case RANDOMIZED_QUICK_SORT ->
                    new RandomizedQuickSort();

            default ->
                    throw new IllegalArgumentException(
                            "Unknown sorting algorithm: "
                                    + algorithmName
                    );
        };
    }

    private void validateInputArray(int[] input) {
        if (input == null) {
            throw new IllegalArgumentException(
                    "Sorting input cannot be null."
            );
        }

        if (input.length == 0) {
            throw new IllegalArgumentException(
                    "Enter at least one number to sort."
            );
        }
    }

    /**
     * Stores complexity and stability information for the interface.
     *
     * @param name algorithm name
     * @param bestCase best-case time complexity
     * @param averageCase average-case time complexity
     * @param worstCase worst-case time complexity
     * @param spaceComplexity extra-space complexity
     * @param stable whether the algorithm is stable
     */
    public record AlgorithmInformation(
            String name,
            String bestCase,
            String averageCase,
            String worstCase,
            String spaceComplexity,
            boolean stable
    ) {
    }
}
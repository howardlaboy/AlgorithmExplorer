package ug.ucu.algorithmexplorer.algorithms.sorting;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import ug.ucu.algorithmexplorer.models.sorting.SortResult;
import ug.ucu.algorithmexplorer.models.sorting.SortStep;

/**
 * Sorts integer arrays using randomized-pivot Quick Sort.
 *
 * A random value from the current range is moved to the first
 * position. The lecturer-required yellow/green partition is then
 * applied using that value as the pivot.
 *
 * Best-case time: O(n log n)
 * Expected time: O(n log n)
 * Worst-case time: O(n^2)
 * Extra space: O(log n) expected, O(n) worst
 * Stable: no
 */
public class RandomizedQuickSort
        implements SortingAlgorithm {

    private final Random random;

    private long comparisonCount;
    private long swapCount;
    private long moveCount;
    private int stepNumber;

    private List<SortStep> steps;

    /**
     * Creates a sorter using normal runtime randomness.
     */
    public RandomizedQuickSort() {
        this(new Random());
    }

    /**
     * Creates a sorter with a fixed random seed.
     *
     * This constructor makes unit tests repeatable.
     *
     * @param seed random-number seed
     */
    public RandomizedQuickSort(long seed) {
        this(new Random(seed));
    }

    private RandomizedQuickSort(Random random) {
        if (random == null) {
            throw new IllegalArgumentException(
                    "Random-number generator cannot be null."
            );
        }

        this.random = random;
    }

    @Override
    public SortResult sort(int[] input) {
        validateInput(input);

        int[] values = copyArray(input);
        int[] original = copyArray(input);

        comparisonCount = 0;
        swapCount = 0;
        moveCount = 0;
        stepNumber = 0;
        steps = new ArrayList<>();

        long startTime = System.nanoTime();

        if (values.length > 1) {
            quickSort(
                    values,
                    0,
                    values.length - 1
            );
        }

        long executionTime =
                System.nanoTime() - startTime;

        return new SortResult(
                getName(),
                original,
                values,
                steps,
                comparisonCount,
                swapCount,
                moveCount,
                executionTime
        );
    }

    /**
     * Recursively sorts the inclusive range [low, high].
     *
     * @param values working array
     * @param low first index in the range
     * @param high final index in the range
     */
    private void quickSort(
            int[] values,
            int low,
            int high
    ) {
        if (low >= high) {
            return;
        }

        int pivotIndex = randomizedPartition(
                values,
                low,
                high
        );

        quickSort(
                values,
                low,
                pivotIndex - 1
        );

        quickSort(
                values,
                pivotIndex + 1,
                high
        );
    }

    /**
     * Selects a random pivot from the current range and moves that
     * value to the first position before partitioning.
     *
     * @param values working array
     * @param low first index in the range
     * @param high final index in the range
     * @return final pivot index
     */
    private int randomizedPartition(
            int[] values,
            int low,
            int high
    ) {
        int selectedPivotIndex =
                low + random.nextInt(high - low + 1);

        int selectedPivotValue =
                values[selectedPivotIndex];

        if (selectedPivotIndex != low) {
            swap(
                    values,
                    low,
                    selectedPivotIndex
            );
        }

        return yellowGreenPartition(
                values,
                low,
                high,
                selectedPivotIndex,
                selectedPivotValue
        );
    }

    /**
     * Applies the yellow/green partition using the value at the first
     * position as the pivot.
     *
     * Yellow scans the range from left to right. Green marks the end
     * of the values that are less than or equal to the pivot.
     *
     * @param values working array
     * @param low first index in the range
     * @param high final index in the range
     * @param selectedPivotIndex original random-pivot index
     * @param pivotValue selected pivot value
     * @return final pivot index
     */
    private int yellowGreenPartition(
            int[] values,
            int low,
            int high,
            int selectedPivotIndex,
            int pivotValue
    ) {
        int greenIndex = low;

        for (int yellowIndex = low + 1;
                yellowIndex <= high;
                yellowIndex++) {

            comparisonCount++;

            if (values[yellowIndex] <= pivotValue) {
                greenIndex++;

                /*
                 * A self-swap does not physically move values,
                 * so it is not counted.
                 */
                if (greenIndex != yellowIndex) {
                    swap(
                            values,
                            greenIndex,
                            yellowIndex
                    );
                }
            }
        }

        /*
         * Place the pivot at the green boundary.
         */
        if (greenIndex != low) {
            swap(
                    values,
                    low,
                    greenIndex
            );
        }

        stepNumber++;

        steps.add(
                new SortStep(
                        stepNumber,
                        createStepDescription(
                                stepNumber,
                                selectedPivotIndex,
                                pivotValue,
                                low,
                                high,
                                greenIndex
                        ),
                        values,
                        new int[]{
                                greenIndex
                        }
                )
        );

        return greenIndex;
    }

    /**
     * Exchanges two distinct array positions.
     *
     * One swap counts as three moves.
     *
     * @param values working array
     * @param firstIndex first position
     * @param secondIndex second position
     */
    private void swap(
            int[] values,
            int firstIndex,
            int secondIndex
    ) {
        int temporary = values[firstIndex];
        values[firstIndex] = values[secondIndex];
        values[secondIndex] = temporary;

        swapCount++;
        moveCount += 3;
    }

    private String createStepDescription(
            int currentStep,
            int selectedPivotIndex,
            int pivotValue,
            int low,
            int high,
            int finalPivotIndex
    ) {
        return "Partition "
                + currentStep
                + ": randomly selected value "
                + pivotValue
                + " from index "
                + selectedPivotIndex
                + " as pivot for range ["
                + low
                + ", "
                + high
                + "]. After yellow/green partitioning, "
                + "the pivot finished at index "
                + finalPivotIndex
                + ".";
    }

    @Override
    public String getName() {
        return "Quick Sort - Randomized Pivot";
    }

    @Override
    public String getBestCase() {
        return "O(n log n)";
    }

    @Override
    public String getAverageCase() {
        return "O(n log n) expected";
    }

    @Override
    public String getWorstCase() {
        return "O(n^2)";
    }

    @Override
    public String getSpaceComplexity() {
        return "O(log n) expected, O(n) worst";
    }

    @Override
    public boolean isStable() {
        return false;
    }

    private void validateInput(int[] input) {
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

    private int[] copyArray(int[] source) {
        int[] copy = new int[source.length];

        for (int index = 0;
                index < source.length;
                index++) {

            copy[index] = source[index];
        }

        return copy;
    }
}
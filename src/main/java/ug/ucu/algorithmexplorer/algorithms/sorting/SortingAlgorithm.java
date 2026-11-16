package ug.ucu.algorithmexplorer.algorithms.sorting;

import ug.ucu.algorithmexplorer.models.sorting.SortResult;

/**
 * Common contract implemented by every sorting algorithm.
 */
public interface SortingAlgorithm {

    /**
     * Sorts the supplied values in ascending order.
     *
     * The supplied array must not be modified. Each implementation
     * creates and sorts its own independent copy.
     *
     * @param input values to sort
     * @return complete sorting result and measurements
     */
    SortResult sort(int[] input);

    /**
     * Returns the readable algorithm name.
     *
     * @return algorithm name
     */
    String getName();

    /**
     * Returns the best-case time complexity.
     *
     * @return best-case complexity
     */
    String getBestCase();

    /**
     * Returns the average-case time complexity.
     *
     * @return average-case complexity
     */
    String getAverageCase();

    /**
     * Returns the worst-case time complexity.
     *
     * @return worst-case complexity
     */
    String getWorstCase();

    /**
     * Returns the extra-space complexity.
     *
     * @return extra-space complexity
     */
    String getSpaceComplexity();

    /**
     * Indicates whether equal elements retain their original order.
     *
     * @return true when stable
     */
    boolean isStable();
}
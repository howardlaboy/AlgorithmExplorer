package ug.ucu.algorithmexplorer.analysis;

import java.util.Random;

import ug.ucu.algorithmexplorer.models.analysis.BenchmarkInputType;

/**
 * Generates the random, sorted and reverse-sorted arrays required by
 * Module D.
 *
 * The implementation does not use a built-in sorting method. Sorted
 * and reverse-sorted inputs are constructed directly.
 */
public class BenchmarkInputGenerator {

    private static final int MINIMUM_VALUE = -1_000_000;
    private static final int MAXIMUM_VALUE = 1_000_000;

    private final Random random;

    /**
     * Creates a generator with normal runtime randomness.
     */
    public BenchmarkInputGenerator() {
        this(new Random());
    }

    /**
     * Creates a repeatable generator for tests and experiments.
     *
     * @param seed random-number seed
     */
    public BenchmarkInputGenerator(long seed) {
        this(new Random(seed));
    }

    private BenchmarkInputGenerator(Random random) {
        if (random == null) {
            throw new IllegalArgumentException(
                    "Random-number generator cannot be null."
            );
        }

        this.random = random;
    }

    /**
     * Generates an array using the selected arrangement.
     *
     * @param size number of values
     * @param inputType required arrangement
     * @return generated array
     */
    public int[] generate(
            int size,
            BenchmarkInputType inputType
    ) {
        validate(size, inputType);

        return switch (inputType) {
            case RANDOM -> generateRandom(size);
            case SORTED -> generateSorted(size);
            case REVERSE_SORTED ->
                    generateReverseSorted(size);
        };
    }

    /**
     * Produces random integers in the configured range.
     *
     * @param size number of values
     * @return random array
     */
    private int[] generateRandom(int size) {
        int[] values = new int[size];

        long range =
                (long) MAXIMUM_VALUE
                        - MINIMUM_VALUE
                        + 1L;

        for (int index = 0;
                index < values.length;
                index++) {

            /*
             * nextDouble is used because the configured inclusive
             * range is larger than a small classroom array range.
             */
            values[index] =
                    MINIMUM_VALUE
                            + (int) (random.nextDouble() * range);
        }

        return values;
    }

    /**
     * Constructs an already sorted array directly.
     *
     * @param size number of values
     * @return ascending array
     */
    private int[] generateSorted(int size) {
        int[] values = new int[size];

        for (int index = 0;
                index < values.length;
                index++) {

            values[index] = index;
        }

        return values;
    }

    /**
     * Constructs a reverse-sorted array directly.
     *
     * @param size number of values
     * @return descending array
     */
    private int[] generateReverseSorted(int size) {
        int[] values = new int[size];

        for (int index = 0;
                index < values.length;
                index++) {

            values[index] =
                    size - 1 - index;
        }

        return values;
    }

    private void validate(
            int size,
            BenchmarkInputType inputType
    ) {
        if (size < 1) {
            throw new IllegalArgumentException(
                    "Benchmark input size must be at least 1."
            );
        }

        if (inputType == null) {
            throw new IllegalArgumentException(
                    "Select a benchmark input type."
            );
        }
    }
}
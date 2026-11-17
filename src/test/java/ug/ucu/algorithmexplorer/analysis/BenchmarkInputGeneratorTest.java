package ug.ucu.algorithmexplorer.analysis;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ug.ucu.algorithmexplorer.models.analysis.BenchmarkInputType;
import ug.ucu.algorithmexplorer.models.analysis.BenchmarkResult;

class BenchmarkInputGeneratorTest {

    private BenchmarkInputGenerator generator;

    @BeforeEach
    void setUp() {
        generator =
                new BenchmarkInputGenerator(42L);
    }

    @Test
    void generatesRequestedRandomSize() {
        int[] values = generator.generate(
                100,
                BenchmarkInputType.RANDOM
        );

        assertEquals(100, values.length);

        for (int value : values) {
            assertTrue(value >= -1_000_000);
            assertTrue(value <= 1_000_000);
        }
    }

    @Test
    void seededRandomGenerationIsRepeatable() {
        BenchmarkInputGenerator first =
                new BenchmarkInputGenerator(100L);

        BenchmarkInputGenerator second =
                new BenchmarkInputGenerator(100L);

        assertArrayEquals(
                first.generate(
                        50,
                        BenchmarkInputType.RANDOM
                ),
                second.generate(
                        50,
                        BenchmarkInputType.RANDOM
                )
        );
    }

    @Test
    void generatesAscendingInput() {
        assertArrayEquals(
                new int[]{0, 1, 2, 3, 4},
                generator.generate(
                        5,
                        BenchmarkInputType.SORTED
                )
        );
    }

    @Test
    void generatesReverseSortedInput() {
        assertArrayEquals(
                new int[]{4, 3, 2, 1, 0},
                generator.generate(
                        5,
                        BenchmarkInputType.REVERSE_SORTED
                )
        );
    }

    @Test
    void rejectsInvalidSizeAndType() {
        assertThrows(
                IllegalArgumentException.class,
                () -> generator.generate(
                        0,
                        BenchmarkInputType.RANDOM
                )
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> generator.generate(
                        10,
                        null
                )
        );
    }

    @Test
    void enumProvidesReadableNames() {
        assertEquals(
                "Random",
                BenchmarkInputType.RANDOM
                        .getDisplayName()
        );

        assertEquals(
                "Already Sorted",
                BenchmarkInputType.SORTED
                        .getDisplayName()
        );

        assertEquals(
                "Reverse Sorted",
                BenchmarkInputType.REVERSE_SORTED
                        .getDisplayName()
        );
    }

    @Test
    void benchmarkResultConvertsTimesToMilliseconds() {
        BenchmarkResult result =
                new BenchmarkResult(
                        "Example Sort",
                        BenchmarkInputType.RANDOM,
                        100,
                        3,
                        2_000_000.0,
                        1_000_000,
                        3_000_000,
                        100.0,
                        20.0,
                        60.0
                );

        assertEquals(
                2.0,
                result.averageTimeMillis()
        );

        assertEquals(
                1.0,
                result.minimumTimeMillis()
        );

        assertEquals(
                3.0,
                result.maximumTimeMillis()
        );
    }

    @Test
    void rejectsInvalidBenchmarkResult() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new BenchmarkResult(
                        "",
                        BenchmarkInputType.RANDOM,
                        100,
                        3,
                        1.0,
                        1,
                        1,
                        0,
                        0,
                        0
                )
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> new BenchmarkResult(
                        "Example",
                        BenchmarkInputType.RANDOM,
                        0,
                        3,
                        1.0,
                        1,
                        1,
                        0,
                        0,
                        0
                )
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> new BenchmarkResult(
                        "Example",
                        BenchmarkInputType.RANDOM,
                        100,
                        3,
                        1.0,
                        5,
                        2,
                        0,
                        0,
                        0
                )
        );
    }

    @Test
    void randomInputIsNotForcedToSortedOrder() {
        int[] values = generator.generate(
                100,
                BenchmarkInputType.RANDOM
        );

        boolean sawDecrease = false;

        for (int index = 1;
                index < values.length;
                index++) {

            if (values[index - 1] > values[index]) {
                sawDecrease = true;
                break;
            }
        }

        assertTrue(sawDecrease);
    }

    @Test
    void generatedArraysAreIndependent() {
        int[] first = generator.generate(
                5,
                BenchmarkInputType.SORTED
        );

        int[] second = generator.generate(
                5,
                BenchmarkInputType.SORTED
        );

        first[0] = 999;

        assertFalse(first[0] == second[0]);

        assertArrayEquals(
                new int[]{0, 1, 2, 3, 4},
                second
        );
    }
}
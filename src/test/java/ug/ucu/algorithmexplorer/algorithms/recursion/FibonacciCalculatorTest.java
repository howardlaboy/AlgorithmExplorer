package ug.ucu.algorithmexplorer.algorithms.recursion;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigInteger;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ug.ucu.algorithmexplorer.models.FibonacciMethod;
import ug.ucu.algorithmexplorer.models.FibonacciResult;

class FibonacciCalculatorTest {

    private FibonacciCalculator calculator;

    @BeforeEach
    void setUp() {
        calculator = new FibonacciCalculator();
    }

    @Test
    void naiveReturnsCorrectValueForNormalInput() {
        FibonacciResult result = calculator.calculateNaive(10);

        assertEquals(BigInteger.valueOf(55), result.value());
        assertEquals(10, result.input());
        assertEquals(
                FibonacciMethod.NAIVE_RECURSIVE,
                result.method()
        );
        assertEquals(177, result.operationCount());
        assertTrue(result.executionTimeNanos() >= 0);
    }

    @Test
    void memoizedReturnsCorrectValueForNormalInput() {
        FibonacciResult result = calculator.calculateMemoized(10);

        assertEquals(BigInteger.valueOf(55), result.value());
        assertEquals(
                FibonacciMethod.MEMOIZED_RECURSIVE,
                result.method()
        );
        assertEquals(19, result.operationCount());
    }

    @Test
    void bottomUpReturnsCorrectValueForNormalInput() {
        FibonacciResult result = calculator.calculateBottomUp(10);

        assertEquals(BigInteger.valueOf(55), result.value());
        assertEquals(
                FibonacciMethod.BOTTOM_UP,
                result.method()
        );
        assertEquals(9, result.operationCount());
    }

    @Test
    void allMethodsHandleZero() {
        assertEquals(
                BigInteger.ZERO,
                calculator.calculateNaive(0).value()
        );
        assertEquals(
                BigInteger.ZERO,
                calculator.calculateMemoized(0).value()
        );
        assertEquals(
                BigInteger.ZERO,
                calculator.calculateBottomUp(0).value()
        );
    }

    @Test
    void allMethodsHandleOne() {
        assertEquals(
                BigInteger.ONE,
                calculator.calculateNaive(1).value()
        );
        assertEquals(
                BigInteger.ONE,
                calculator.calculateMemoized(1).value()
        );
        assertEquals(
                BigInteger.ONE,
                calculator.calculateBottomUp(1).value()
        );
    }

    @Test
    void methodsAgreeForInputsFromZeroToTwenty() {
        for (int n = 0; n <= 20; n++) {
            BigInteger naive = calculator.calculateNaive(n).value();
            BigInteger memoized =
                    calculator.calculateMemoized(n).value();
            BigInteger bottomUp =
                    calculator.calculateBottomUp(n).value();

            assertEquals(naive, memoized);
            assertEquals(memoized, bottomUp);
        }
    }

    @Test
    void rejectsNegativeInput() {
        assertThrows(
                IllegalArgumentException.class,
                () -> calculator.calculateNaive(-1)
        );
        assertThrows(
                IllegalArgumentException.class,
                () -> calculator.calculateMemoized(-1)
        );
        assertThrows(
                IllegalArgumentException.class,
                () -> calculator.calculateBottomUp(-1)
        );
    }

    @Test
    void naiveRejectsInputAboveSafeLimit() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> calculator.calculateNaive(31)
        );

        assertTrue(exception.getMessage().contains("n <= 30"));
    }

    @Test
    void efficientMethodsProduceLargeFibonacciValue() {
        BigInteger expected = new BigInteger(
                "354224848179261915075"
        );

        assertEquals(
                expected,
                calculator.calculateMemoized(100).value()
        );
        assertEquals(
                expected,
                calculator.calculateBottomUp(100).value()
        );
    }
}
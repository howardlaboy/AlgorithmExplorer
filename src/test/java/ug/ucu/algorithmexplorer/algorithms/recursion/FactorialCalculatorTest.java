package ug.ucu.algorithmexplorer.algorithms.recursion;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigInteger;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ug.ucu.algorithmexplorer.models.FactorialMethod;
import ug.ucu.algorithmexplorer.models.FactorialResult;

class FactorialCalculatorTest {

    private FactorialCalculator calculator;

    @BeforeEach
    void setUp() {
        calculator = new FactorialCalculator();
    }

    @Test
    void recursiveReturnsCorrectValueForFive() {
        FactorialResult result = calculator.calculateRecursive(5);

        assertEquals(BigInteger.valueOf(120), result.value());
        assertEquals(5, result.input());
        assertEquals(
                FactorialMethod.RECURSIVE,
                result.method()
        );
        assertEquals(5, result.operationCount());
        assertTrue(result.executionTimeNanos() >= 0);
    }

    @Test
    void iterativeReturnsCorrectValueForFive() {
        FactorialResult result = calculator.calculateIterative(5);

        assertEquals(BigInteger.valueOf(120), result.value());
        assertEquals(5, result.input());
        assertEquals(
                FactorialMethod.ITERATIVE,
                result.method()
        );
        assertEquals(4, result.operationCount());
        assertTrue(result.executionTimeNanos() >= 0);
    }

    @Test
    void bothMethodsHandleZero() {
        FactorialResult recursive =
                calculator.calculateRecursive(0);
        FactorialResult iterative =
                calculator.calculateIterative(0);

        assertEquals(BigInteger.ONE, recursive.value());
        assertEquals(BigInteger.ONE, iterative.value());

        assertEquals(1, recursive.operationCount());
        assertEquals(0, iterative.operationCount());
    }

    @Test
    void bothMethodsHandleOne() {
        FactorialResult recursive =
                calculator.calculateRecursive(1);
        FactorialResult iterative =
                calculator.calculateIterative(1);

        assertEquals(BigInteger.ONE, recursive.value());
        assertEquals(BigInteger.ONE, iterative.value());

        assertEquals(1, recursive.operationCount());
        assertEquals(0, iterative.operationCount());
    }

    @Test
    void bothMethodsAgreeForInputsZeroToTwenty() {
        for (int n = 0; n <= 20; n++) {
            BigInteger recursive =
                    calculator.calculateRecursive(n).value();
            BigInteger iterative =
                    calculator.calculateIterative(n).value();

            assertEquals(recursive, iterative);
        }
    }

    @Test
    void calculatesTenFactorialCorrectly() {
        BigInteger expected = BigInteger.valueOf(3_628_800);

        assertEquals(
                expected,
                calculator.calculateRecursive(10).value()
        );
        assertEquals(
                expected,
                calculator.calculateIterative(10).value()
        );
    }

    @Test
    void calculatesLargeFactorialUsingBigInteger() {
        BigInteger expected = new BigInteger(
                "30414093201713378043612608166064768844377641568960512000000000000"
        );

        assertEquals(
                expected,
                calculator.calculateIterative(50).value()
        );
    }

    @Test
    void recursiveRejectsNegativeInput() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> calculator.calculateRecursive(-1)
        );

        assertTrue(
                exception.getMessage().contains("zero or greater")
        );
    }

    @Test
    void iterativeRejectsNegativeInput() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> calculator.calculateIterative(-1)
        );

        assertTrue(
                exception.getMessage().contains("zero or greater")
        );
    }
}
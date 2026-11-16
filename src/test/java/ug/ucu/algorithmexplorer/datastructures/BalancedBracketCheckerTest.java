package ug.ucu.algorithmexplorer.datastructures;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ug.ucu.algorithmexplorer.models.BracketCheckResult;
import ug.ucu.algorithmexplorer.models.BracketCheckStep;

class BalancedBracketCheckerTest {

    private BalancedBracketChecker checker;

    @BeforeEach
    void setUp() {
        checker = new BalancedBracketChecker();
    }

    @Test
    void acceptsBalancedNestedExpression() {
        BracketCheckResult result = checker.check("{([])}");

        assertTrue(result.balanced());
        assertEquals(
                "The expression is balanced.",
                result.message()
        );
        assertEquals(6, result.steps().size());
        assertEquals("[]", result.steps().get(5).stackState());
        assertEquals(9, result.stackOperationCount());
        assertTrue(result.executionTimeNanos() >= 0);
    }

    @Test
    void acceptsProjectBriefExampleWithSpaces() {
        BracketCheckResult result = checker.check("{ ( [ ] ) }");

        assertTrue(result.balanced());
        assertEquals(11, result.steps().size());
        assertEquals("[]", result.steps().get(10).stackState());
    }

    @Test
    void acceptsExpressionContainingOrdinaryCharacters() {
        BracketCheckResult result =
                checker.check("total = values[(index + 1)]");

        assertTrue(result.balanced());
        assertEquals(
                "The expression is balanced.",
                result.message()
        );
        assertEquals(
                "Ignored non-bracket symbol",
                result.steps().get(0).action()
        );
    }

    @Test
    void rejectsMismatchedClosingBracket() {
        BracketCheckResult result = checker.check("([)]");

        assertFalse(result.balanced());
        assertEquals(3, result.steps().size());
        assertTrue(result.message().contains("matching pair"));
        assertFalse(result.steps().get(2).validSoFar());
        assertEquals("[ (, [ ]", result.steps().get(2).stackState());
    }

    @Test
    void rejectsClosingBracketWithoutOpeningBracket() {
        BracketCheckResult result = checker.check(")");

        assertFalse(result.balanced());
        assertEquals(1, result.steps().size());
        assertEquals("[]", result.steps().get(0).stackState());
        assertTrue(
                result.message().contains(
                        "no matching opening bracket"
                )
        );
    }

    @Test
    void rejectsUnclosedOpeningBrackets() {
        BracketCheckResult result = checker.check("{{[(");

        assertFalse(result.balanced());
        assertEquals(4, result.steps().size());
        assertTrue(
                result.message().contains(
                        "opening brackets remain"
                )
        );
        assertEquals(
                "[ {, {, [, ( ]",
                result.steps().get(3).stackState()
        );
    }

    @Test
    void recordsStackAfterEverySymbol() {
        BracketCheckResult result = checker.check("(a)");

        assertEquals(3, result.steps().size());

        BracketCheckStep first = result.steps().get(0);
        BracketCheckStep second = result.steps().get(1);
        BracketCheckStep third = result.steps().get(2);

        assertEquals('(', first.symbol());
        assertEquals("[ ( ]", first.stackState());

        assertEquals('a', second.symbol());
        assertEquals("[ ( ]", second.stackState());

        assertEquals(')', third.symbol());
        assertEquals("[]", third.stackState());
    }

    @Test
    void rejectsEmptyExpression() {
        assertThrows(
                IllegalArgumentException.class,
                () -> checker.check("")
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> checker.check("   ")
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> checker.check(null)
        );
    }

    @Test
    void resultStepListCannotBeModified() {
        BracketCheckResult result = checker.check("[]");

        assertThrows(
                UnsupportedOperationException.class,
                () -> result.steps().clear()
        );
    }

    @Test
    void descriptionIncludesPositionActionAndStack() {
        BracketCheckResult result = checker.check("{}");

        String description =
                result.steps().get(0).description();

        assertTrue(description.contains("Position 0"));
        assertTrue(description.contains("Pushed"));
        assertTrue(description.contains("[ { ]"));
    }
}
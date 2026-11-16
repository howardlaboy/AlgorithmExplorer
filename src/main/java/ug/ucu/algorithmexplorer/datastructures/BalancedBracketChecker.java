package ug.ucu.algorithmexplorer.datastructures;

import java.util.ArrayList;
import java.util.List;

import ug.ucu.algorithmexplorer.models.BracketCheckResult;
import ug.ucu.algorithmexplorer.models.BracketCheckStep;

/**
 * Checks whether round, square and curly brackets are balanced.
 *
 * This implementation deliberately uses the project's own ArrayStack.
 *
 * Time complexity: O(n)
 * Extra space: O(n)
 */
public class BalancedBracketChecker {

    /**
     * Checks an expression and records the stack after every symbol.
     *
     * Non-bracket characters are ignored, but a step is still recorded
     * so that the interface can show what happened to every symbol.
     *
     * @param expression expression to inspect
     * @return complete bracket-check result
     */
    public BracketCheckResult check(String expression) {
        validateExpression(expression);

        ArrayStack<Character> stack =
                new ArrayStack<>(Math.max(1, expression.length()));

        List<BracketCheckStep> steps = new ArrayList<>();
        long stackOperations = 0;
        boolean valid = true;
        String message = "The expression is balanced.";

        long startTime = System.nanoTime();

        for (int position = 0;
                position < expression.length();
                position++) {

            char symbol = expression.charAt(position);
            String action;

            if (isOpeningBracket(symbol)) {
                stack.push(symbol);
                stackOperations++;

                action = "Pushed opening bracket " + symbol;

            } else if (isClosingBracket(symbol)) {
                if (stack.isEmpty()) {
                    valid = false;

                    action = "No opening bracket is available "
                            + "for " + symbol;

                    message = "The expression is not balanced: "
                            + "closing bracket "
                            + symbol
                            + " at position "
                            + position
                            + " has no matching opening bracket.";

                } else {
                    char openingBracket = stack.peek();
                    stackOperations++;

                    if (isMatchingPair(openingBracket, symbol)) {
                        stack.pop();
                        stackOperations++;

                        action = "Matched "
                                + openingBracket
                                + " with "
                                + symbol
                                + " and popped "
                                + openingBracket;

                    } else {
                        valid = false;

                        action = "Expected the closing bracket for "
                                + openingBracket
                                + " but found "
                                + symbol;

                        message = "The expression is not balanced: "
                                + openingBracket
                                + " and "
                                + symbol
                                + " do not form a matching pair "
                                + "at position "
                                + position
                                + ".";
                    }
                }

            } else {
                action = "Ignored non-bracket symbol";
            }

            steps.add(
                    new BracketCheckStep(
                            position,
                            symbol,
                            action,
                            stack.display(),
                            valid
                    )
            );

            if (!valid) {
                break;
            }
        }

        if (valid && !stack.isEmpty()) {
            valid = false;

            message = "The expression is not balanced: "
                    + "one or more opening brackets remain "
                    + "on the stack.";
        }

        long executionTime = System.nanoTime() - startTime;

        return new BracketCheckResult(
                expression,
                valid,
                message,
                steps,
                stackOperations,
                executionTime
        );
    }

    private void validateExpression(String expression) {
        if (expression == null || expression.isBlank()) {
            throw new IllegalArgumentException(
                    "Enter a bracket expression to check."
            );
        }
    }

    private boolean isOpeningBracket(char symbol) {
        return symbol == '('
                || symbol == '['
                || symbol == '{';
    }

    private boolean isClosingBracket(char symbol) {
        return symbol == ')'
                || symbol == ']'
                || symbol == '}';
    }

    private boolean isMatchingPair(
            char openingBracket,
            char closingBracket
    ) {
        return openingBracket == '(' && closingBracket == ')'
                || openingBracket == '[' && closingBracket == ']'
                || openingBracket == '{' && closingBracket == '}';
    }
}
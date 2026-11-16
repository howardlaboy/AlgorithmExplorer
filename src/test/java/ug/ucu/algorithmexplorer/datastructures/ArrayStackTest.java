package ug.ucu.algorithmexplorer.datastructures;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ArrayStackTest {

    private ArrayStack<Integer> stack;

    @BeforeEach
    void setUp() {
        stack = new ArrayStack<>(3);
    }

    @Test
    void newStackHasCorrectInitialState() {
        assertTrue(stack.isEmpty());
        assertFalse(stack.isFull());
        assertEquals(0, stack.size());
        assertEquals(3, stack.capacity());
        assertEquals(-1, stack.topIndex());
        assertEquals("[]", stack.display());
    }

    @Test
    void pushAddsElementToTop() {
        stack.push(10);
        stack.push(20);

        assertEquals(2, stack.size());
        assertEquals(20, stack.peek());
        assertEquals(1, stack.topIndex());
        assertFalse(stack.isEmpty());
    }

    @Test
    void stackBecomesFullAtCapacity() {
        stack.push(10);
        stack.push(20);
        stack.push(30);

        assertTrue(stack.isFull());
        assertEquals(3, stack.size());
        assertEquals(30, stack.peek());
    }

    @Test
    void popRemovesElementsInLastInFirstOutOrder() {
        stack.push(10);
        stack.push(20);
        stack.push(30);

        assertEquals(30, stack.pop());
        assertEquals(20, stack.pop());
        assertEquals(10, stack.pop());

        assertTrue(stack.isEmpty());
    }

    @Test
    void peekDoesNotRemoveTopElement() {
        stack.push(42);

        assertEquals(42, stack.peek());
        assertEquals(42, stack.peek());
        assertEquals(1, stack.size());
    }

    @Test
    void pushOnFullStackReportsOverflow() {
        stack.push(10);
        stack.push(20);
        stack.push(30);

        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> stack.push(40)
        );

        assertTrue(
                exception.getMessage().contains("overflow")
        );

        assertEquals(3, stack.size());
        assertEquals(30, stack.peek());
    }

    @Test
    void popOnEmptyStackReportsUnderflow() {
        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> stack.pop()
        );

        assertTrue(
                exception.getMessage().contains("underflow")
        );
    }

    @Test
    void peekOnEmptyStackReportsUnderflow() {
        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> stack.peek()
        );

        assertTrue(
                exception.getMessage().contains("underflow")
        );
    }

    @Test
    void displayShowsElementsFromBottomToTop() {
        stack.push(10);
        stack.push(20);
        stack.push(30);

        assertEquals("[ 10, 20, 30 ]", stack.display());
    }

    @Test
    void toArrayReturnsIndependentSnapshot() {
        stack.push(10);
        stack.push(20);

        Object[] snapshot = stack.toArray();

        assertArrayEquals(
                new Object[]{10, 20},
                snapshot
        );

        snapshot[0] = 999;

        assertEquals(10, stack.toArray()[0]);
        assertEquals(20, stack.pop());
    }

    @Test
    void clearRemovesAllElementsAndKeepsCapacity() {
        stack.push(10);
        stack.push(20);
        stack.push(30);

        stack.clear();

        assertTrue(stack.isEmpty());
        assertFalse(stack.isFull());
        assertEquals(0, stack.size());
        assertEquals(3, stack.capacity());
        assertEquals(-1, stack.topIndex());
    }

    @Test
    void constructorRejectsInvalidCapacity() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new ArrayStack<Integer>(0)
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> new ArrayStack<Integer>(-5)
        );
    }

    @Test
    void stackRejectsNullValues() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> stack.push(null)
        );

        assertTrue(
                exception.getMessage().contains("null")
        );
    }

    @Test
    void sameStackClassCanStoreCharacters() {
        ArrayStack<Character> characterStack =
                new ArrayStack<>(4);

        characterStack.push('{');
        characterStack.push('(');
        characterStack.push('[');

        assertEquals('[', characterStack.pop());
        assertEquals('(', characterStack.pop());
        assertEquals('{', characterStack.pop());
        assertTrue(characterStack.isEmpty());
    }
}
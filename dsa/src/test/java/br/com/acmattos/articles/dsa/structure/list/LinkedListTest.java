package br.com.acmattos.articles.dsa.structure.list;

import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LinkedListTest {

    @Test
    void givenANegativeIndexWhenGettingAValueWithTheIndexThenAnExceptionIsThrown() {
        // Given
        LinkedList list = createPopulatedLinkedList(1);
        int index = -1;
        // When
        Throwable throwable =
            assertThrows(IndexOutOfBoundsException.class,
                () -> list.get(index));
        // Then
        assertNotNull(throwable);
        assertEquals("Index out of bounds: -1, size: 1",
            throwable.getMessage());
    }

    @Test
    void givenAInvalidIndexWhenGettingAValueWithTheIndexThenAnExceptionIsThrown() {
        // Given
        LinkedList list = createPopulatedLinkedList(1);
        int index = 1;
        // When
        Throwable throwable =
            assertThrows(IndexOutOfBoundsException.class,
                () -> list.get(index));
        // Then
        assertNotNull(throwable);
        assertEquals("Index out of bounds: 1, size: 1",
            throwable.getMessage());
    }

    @Test
    void givenAValidIndexWhenGettingAValueWithTheIndexThenAValueIsReturned() {
        // Given
        LinkedList list = createPopulatedLinkedList(1);
        int index = 0;
        // When
        int value = list.get(0);
        // Then
        assertEquals(0, value);
    }

    @Test
    void givenAnEmptyListWhenGettingFirstValueThenAnExceptionIsThrown() {
        // Given
        LinkedList list = createPopulatedLinkedList(0);
        // When
        Throwable throwable =
            assertThrows(IndexOutOfBoundsException.class,
                () -> list.getFirst());
        // Then
        assertNotNull(throwable);
        assertEquals("Index out of bounds: 0, size: 0",
            throwable.getMessage());
    }

    @Test
    void givenANomEmptyListWhenGettingFirstValueThenAValueIsReturned() {
        // Given
        LinkedList list = createPopulatedLinkedList(1);
        // When
        int value = list.getFirst();
        // Then
        assertEquals(0, value);
    }

    @Test
    void givenAnEmptyListWhenGettingLastValueThenAnExceptionIsThrown() {
        // Given
        LinkedList list = createPopulatedLinkedList(0);
        // When
        Throwable throwable =
            assertThrows(IndexOutOfBoundsException.class,
                () -> list.getLast());
        // Then
        assertNotNull(throwable);
        assertEquals("Index out of bounds: -1, size: 0",
            throwable.getMessage());
    }

    @Test
    void givenANomEmptyListWhenGettingLastValueThenAValueIsReturned() {
        // Given
        LinkedList list = createPopulatedLinkedList(1);
        // When
        int value = list.getLast();
        // Then
        assertEquals(0, value);
    }

    @Test
    void givenANegativeIndexWhenSettingAValueWithTheIndexThenAnExceptionIsThrown() {
        // Given
        LinkedList list = createPopulatedLinkedList(1);
        int index = -1;
        // When
        Throwable throwable =
            assertThrows(IndexOutOfBoundsException.class,
                () -> list.set(index, 10));
        // Then
        assertNotNull(throwable);
        assertEquals("Index out of bounds: -1, size: 1",
            throwable.getMessage());
    }

    @Test
    void givenAInvalidIndexWhenSettingAValueWithTheIndexThenAnExceptionIsThrown() {
        // Given
        LinkedList list = createPopulatedLinkedList(1);
        int index = 1;
        // When
        Throwable throwable =
            assertThrows(IndexOutOfBoundsException.class,
                () -> list.set(index, 10));
        // Then
        assertNotNull(throwable);
        assertEquals("Index out of bounds: 1, size: 1",
            throwable.getMessage());
    }

    @Test
    void givenAValidIndexWhenSettingAValueWithTheIndexThenAValueIsReturned() {
        // Given
        LinkedList list = createPopulatedLinkedList(1);
        int index = 0;
        // When
        list.set(index, 10);
        // Then
        assertEquals(10, list.get(index));
    }

    @Test
    void givenANegativeIndexWhenAddingAValueWithTheIndexThenAnExceptionIsThrown() {
        // Given
        LinkedList list = createPopulatedLinkedList(1);
        int index = -1;
        // When
        Throwable throwable =
            assertThrows(IndexOutOfBoundsException.class,
                () -> list.add(index, 10));
        // Then
        assertNotNull(throwable);
        assertEquals("Index out of bounds: -1, size: 1",
            throwable.getMessage());
    }

    @Test
    void givenAInvalidIndexWhenAddingAValueWithTheIndexThenAnExceptionIsThrown() {
        // Given
        LinkedList list = createPopulatedLinkedList(1);
        int index = 2;
        // When
        Throwable throwable =
            assertThrows(IndexOutOfBoundsException.class,
                () -> list.add(index, 10));
        // Then
        assertNotNull(throwable);
        assertEquals("Index out of bounds: 2, size: 1",
            throwable.getMessage());
    }

    @Test
    void givenAIndexEqualEndOfTheListWhenAddingAValueWithTheIndexThenAValueIsReturned() {
        // Given
        LinkedList list = createPopulatedLinkedList(1);
        int index = 1;
        // When
        list.add(index, 10);
        // Then
        assertEquals(0, list.get(0));
        assertEquals(10, list.get(index));
        assertEquals(2, list.size());
    }

    @Test
    void givenANegativeIndexWhenAddingAValueAtIndexThenAnExceptionIsThrown() {
        // Given
        LinkedList list = createPopulatedLinkedList(1);
        int index = -1;
        // When
        Throwable throwable =
            assertThrows(IndexOutOfBoundsException.class,
                () -> list.add(index, 10));
        // Then
        assertNotNull(throwable);
        assertEquals("Index out of bounds: -1, size: 1",
            throwable.getMessage());
    }

    @Test
    void givenAnInvalidIndexWhenAddingAValueAtIndexThenAnExceptionIsThrown() {
        // Given
        LinkedList list = createPopulatedLinkedList(1);
        int index = 2;
        // When
        Throwable throwable =
            assertThrows(IndexOutOfBoundsException.class,
                () -> list.add(index, 10));
        // Then
        assertNotNull(throwable);
        assertEquals("Index out of bounds: 2, size: 1",
            throwable.getMessage());
    }

    @Test
    void givenAValidIndexWhenAddingAValueAtIndexThenAAValueIsAdded() {
        // Given
        LinkedList list = createPopulatedLinkedList(1);
        int index = 1;
        // When
        list.add(index,10);
        // Then
        assertEquals(0, list.get(0));
        assertEquals(10, list.get(1));
        assertEquals(2, list.size());
    }

    @Test
    void givenAnEmptyListWhenAddingAValueAsFirstThenListSizeIsOne() {
        // Given
        LinkedList list = createPopulatedLinkedList(0);
        int index = 0;
        // When
        list.addFirst(10);
        // Then
        assertEquals(10, list.get(index));
        assertEquals(1, list.size());
    }

    @Test
    void givenANonEmptyListWhenAddingAValueAsFirstThenListSizeIsTwo() {
        // Given
        LinkedList list = createPopulatedLinkedList(1);
        // When
        list.addFirst(10);
        // Then
        assertEquals(10, list.get(0));
        assertEquals(0, list.get(1));
        assertEquals(2, list.size());
    }

    @Test
    void givenAnEmptyListWhenAddingAValueAsLastThenListSizeIsOne() {
        // Given
        LinkedList list = createPopulatedLinkedList(0);
        int index = 0;
        // When
        list.add(10);
        // Then
        assertEquals(10, list.get(index));
        assertEquals(1, list.size());
    }

    @Test
    void givenANonEmptyListWhenAddingAValueAsLastThenListSizeIsTwo() {
        // Given
        LinkedList list = createPopulatedLinkedList(1);
        // When
        list.add(10);
        // Then
        assertEquals(0, list.get(0));
        assertEquals(10, list.get(1));
        assertEquals(2, list.size());
    }

    @Test
    void givenAValidIndexWhenAddingAValueWithTheIndexThenAValueIsInsertedAtIndexPosition() {
        // Given
        LinkedList list = createPopulatedLinkedList(3);
        int index = 2;
        // When
        list.add(index, 10);
        // Then
        assertEquals(0, list.get(0));
        assertEquals(1, list.get(1));
        assertEquals(10, list.get(index));
        assertEquals(2, list.get(3));
        assertEquals(4, list.size());
    }

    @Test
    void givenANegativeIndexWhenRemovingAValueWithTheIndexThenAnExceptionIsThrown() {
        // Given
        LinkedList list = createPopulatedLinkedList(1);
        int index = -1;
        // When
        Throwable throwable =
            assertThrows(IndexOutOfBoundsException.class,
                () -> list.remove(index));
        // Then
        assertNotNull(throwable);
        assertEquals("Index out of bounds: -1, size: 1",
            throwable.getMessage());
    }

    @Test
    void givenAInvalidIndexWhenRemovingAValueWithTheIndexThenAnExceptionIsThrown() {
        // Given
        LinkedList list = createPopulatedLinkedList(1);
        int index = 1;
        // When
        Throwable throwable =
            assertThrows(IndexOutOfBoundsException.class,
                () -> list.remove(index));
        // Then
        assertNotNull(throwable);
        assertEquals("Index out of bounds: 1, size: 1",
            throwable.getMessage());
    }

    @Test
    void givenAValidIndexWhenRemovingTheFirstValueWithTheIndexThenTheSizeIsZero() {
        // Given
        LinkedList list = createPopulatedLinkedList(1);
        int index = 0;
        // When
        int value = list.remove(index);
        // Then
        assertEquals(0, value);
        assertEquals(0, list.size());
        assertTrue(list.isEmpty());
    }

    @Test
    void givenAValidIndexWhenRemovingAValueWithTheIndexThenTheSizeIsTwo() {
        // Given
        LinkedList list = createPopulatedLinkedList(3);
        int index = 1;
        // When
        int value = list.remove(index);
        // Then
        assertEquals(1, value);
        assertEquals(0, list.get(0));
        assertEquals(2, list.get(1));
        assertEquals(2, list.size());
        assertFalse(list.isEmpty());
    }

    @Test
    void givenAnEmptyListWhenRemovingFirstValueThenAnExceptionIsThrown() {
        // Given
        LinkedList list = createPopulatedLinkedList(0);
        // When
        Throwable throwable =
            assertThrows(NoSuchElementException.class,
                () -> list.removeFirst());
        // Then
        assertNotNull(throwable);
        assertEquals("The list is empty!",
            throwable.getMessage());
    }

    @Test
    void givenANonEmptyListWhenRemovingFirstValueThenTheSizeIsZero() {
        // Given
        LinkedList list = createPopulatedLinkedList(1);
        // When
        int value = list.removeFirst();
        // Then
        assertEquals(0, value);
        assertEquals(0, list.size());
        assertTrue(list.isEmpty());
    }

    @Test
    void givenANonEmptyListWhenRemovingFirstValueThenTheSizeIsOne() {
        // Given
        LinkedList list = createPopulatedLinkedList(2);
        // When
        int value = list.removeFirst();
        // Then
        assertEquals(0, value);
        assertEquals(1, list.get(0));
        assertEquals(1, list.size());
        assertFalse(list.isEmpty());
    }

    @Test
    void givenAnEmptyListWhenRemovingLastValueThenAnExceptionIsThrown() {
        // Given
        LinkedList list = createPopulatedLinkedList(0);
        // When
        Throwable throwable =
            assertThrows(NoSuchElementException.class,
                () -> list.remove());
        // Then
        assertNotNull(throwable);
        assertEquals("The list is empty!",
            throwable.getMessage());
    }

    @Test
    void givenANonEmptyListWhenRemovingLastValueThenTheSizeIsZero() {
        // Given
        LinkedList list = createPopulatedLinkedList(1);
        // When
        int value = list.remove();
        // Then
        assertEquals(0, value);
        assertEquals(0, list.size());
        assertTrue(list.isEmpty());
    }

    @Test
    void givenANonEmptyListWhenRemovingLastValueThenTheSizeIsOne() {
        // Given
        LinkedList list = createPopulatedLinkedList(2);
        // When
        int value = list.remove();
        // Then
        assertEquals(1, value);
        assertEquals(0, list.get(0));
        assertEquals(1, list.size());
        assertFalse(list.isEmpty());
    }

    @Test
    void givenANonEmptyListWhenClearingTheListThenTheSizeIsZero() {
        // Given
        LinkedList list = createPopulatedLinkedList(1);
        // When
        list.clear();
        // Then
        assertEquals(0, list.size());
        assertTrue(list.isEmpty());
    }

    @Test
    void givenAEmptyListWhenContainsValueThenFalseIsReturned() {
        // Given
        LinkedList list = createPopulatedLinkedList(0);
        // When
        boolean contains = list.contains(10);
        // Then
        assertFalse(contains);
        assertTrue(list.isEmpty());
    }

    @Test
    void givenAnInvalidValueWhenContainsValueThenFalseIsReturned() {
        // Given
        LinkedList list = createPopulatedLinkedList(4);
        int value = 4;
        // When
        boolean contains = list.contains(value);
        // Then
        assertFalse(contains);
        assertFalse(list.isEmpty());
    }

    @Test
    void givenAnValidValueWhenContainsValueThenTrueIsReturned() {
        // Given
        LinkedList list = createPopulatedLinkedList(4);
        int value = 3;
        // When
        boolean contains = list.contains(value);
        // Then
        assertTrue(contains);
        assertFalse(list.isEmpty());
    }

    private LinkedList createPopulatedLinkedList(int size) {
        LinkedList list = new LinkedList();
        for (int i = 0; i < size; i++) {
            list.add(i);
        }
        return list;
    }
}
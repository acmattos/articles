package br.com.acmattos.articles.dsa.structure.list;

import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DoublyLinkedListTest {

    @Test
    void givenANegativeIndexWhenGettingAValueWithTheIndexThenAnExceptionIsThrown() {
        // Given
        DoublyLinkedList list = createPopulatedDoublyLinkedList(1);
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
        DoublyLinkedList list = createPopulatedDoublyLinkedList(1);
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
        DoublyLinkedList list = createPopulatedDoublyLinkedList(1);
        int index = 0;
        // When
        int value = list.get(index);
        // Then
        assertEquals(0, value);
    }

    @Test
    void givenAnEmptyListWhenGettingFirstValueThenAnExceptionIsThrown() {
        // Given
        DoublyLinkedList list = createPopulatedDoublyLinkedList(0);
        // When
        Throwable throwable =
            assertThrows(IndexOutOfBoundsException.class, list::getFirst);
        // Then
        assertNotNull(throwable);
        assertEquals("Index out of bounds: 0, size: 0",
            throwable.getMessage());
    }

    @Test
    void givenANomEmptyListWhenGettingFirstValueThenAValueIsReturned() {
        // Given
        DoublyLinkedList list = createPopulatedDoublyLinkedList(1);
        // When
        int value = list.getFirst();
        // Then
        assertEquals(0, value);
    }

    @Test
    void givenAnEmptyListWhenGettingLastValueThenAnExceptionIsThrown() {
        // Given
        DoublyLinkedList list = createPopulatedDoublyLinkedList(0);
        // When
        Throwable throwable =
            assertThrows(IndexOutOfBoundsException.class, list::getLast);
        // Then
        assertNotNull(throwable);
        assertEquals("Index out of bounds: -1, size: 0",
            throwable.getMessage());
    }

    @Test
    void givenANomEmptyListWhenGettingLastValueThenAValueIsReturned() {
        // Given
        DoublyLinkedList list = createPopulatedDoublyLinkedList(1);
        // When
        int value = list.getLast();
        // Then
        assertEquals(0, value);
    }

    @Test
    void givenANegativeIndexWhenSettingAValueWithTheIndexThenAnExceptionIsThrown() {
        // Given
        DoublyLinkedList list = createPopulatedDoublyLinkedList(1);
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
        DoublyLinkedList list = createPopulatedDoublyLinkedList(1);
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
        DoublyLinkedList list = createPopulatedDoublyLinkedList(1);
        int index = 0;
        // When
        list.set(index, 10);
        // Then
        assertEquals(10, list.get(index));
    }

    @Test
    void givenANegativeIndexWhenAddingAValueWithTheIndexThenAnExceptionIsThrown() {
        // Given
        DoublyLinkedList list = createPopulatedDoublyLinkedList(1);
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
    void givenAnInvalidIndexWhenAddingAValueWithTheIndexThenAnExceptionIsThrown() {
        // Given
        DoublyLinkedList list = createPopulatedDoublyLinkedList(1);
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
    void givenAnEmptyListWhenAddingAValueWithTheIndexThenAValueIsAdded() {
        // Given
        DoublyLinkedList list = createPopulatedDoublyLinkedList(0);
        int index = 0;
        // When
        list.add(index, 10);
        // Then
        assertEquals(10, list.get(index));
        assertEquals(1, list.size());
    }

    @Test
    void givenAnIndexEqualEndOfTheListWhenAddingAValueWithTheIndexThenAValueIsAdded() {
        // Given
        DoublyLinkedList list = createPopulatedDoublyLinkedList(1);
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
        DoublyLinkedList list = createPopulatedDoublyLinkedList(1);
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
        DoublyLinkedList list = createPopulatedDoublyLinkedList(1);
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
        DoublyLinkedList list = createPopulatedDoublyLinkedList(1);
        int index = 1;
        // When
        list.add(index,10);
        // Then
        assertEquals(0, list.get(0));
        assertEquals(10, list.get(1));
        assertEquals(2, list.size());
    }

    @Test
    void givenAValidIndexWhenAddingAValueWithTheIndexThenAValueIsInsertedAtIndexPosition() {
        // Given
        DoublyLinkedList list = createPopulatedDoublyLinkedList(3);
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
    void givenAnEmptyListWhenAddingAValueAsFirstThenListSizeIsOne() {
        // Given
        DoublyLinkedList list = createPopulatedDoublyLinkedList(0);
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
        DoublyLinkedList list = createPopulatedDoublyLinkedList(1);
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
        DoublyLinkedList list = createPopulatedDoublyLinkedList(0);
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
        DoublyLinkedList list = createPopulatedDoublyLinkedList(1);
        // When
        list.add(10);
        // Then
        assertEquals(0, list.get(0));
        assertEquals(10, list.get(1));
        assertEquals(2, list.size());
    }

    @Test
    void givenANegativeIndexWhenRemovingAValueWithTheIndexThenAnExceptionIsThrown() {
        // Given
        DoublyLinkedList list = createPopulatedDoublyLinkedList(1);
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
        DoublyLinkedList list = createPopulatedDoublyLinkedList(1);
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
        DoublyLinkedList list = createPopulatedDoublyLinkedList(1);
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
        DoublyLinkedList list = createPopulatedDoublyLinkedList(3);
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
    void givenAValidLastIndexWhenRemovingAValueWithTheIndexThenTheSizeIsTwo() {
        // Given
        DoublyLinkedList list = createPopulatedDoublyLinkedList(4);
        int index = 2;
        // When
        int value = list.remove(index);
        // Then
        assertEquals(2, value);
        assertEquals(0, list.get(0));
        assertEquals(1, list.get(1));
        assertEquals(3, list.get(2));
        assertEquals(3, list.size());
        assertFalse(list.isEmpty());
    }

    @Test
    void givenAnEmptyListWhenRemovingFirstValueThenAnExceptionIsThrown() {
        // Given
        DoublyLinkedList list = createPopulatedDoublyLinkedList(0);
        // When
        Throwable throwable =
            assertThrows(NoSuchElementException.class, list::removeFirst);
        // Then
        assertNotNull(throwable);
        assertEquals("The list is empty!",
            throwable.getMessage());
    }

    @Test
    void givenANonEmptyListWhenRemovingFirstValueThenTheSizeIsZero() {
        // Given
        DoublyLinkedList list = createPopulatedDoublyLinkedList(1);
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
        DoublyLinkedList list = createPopulatedDoublyLinkedList(2);
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
        DoublyLinkedList list = createPopulatedDoublyLinkedList(0);
        // When
        Throwable throwable =
            assertThrows(NoSuchElementException.class, list::remove);
        // Then
        assertNotNull(throwable);
        assertEquals("The list is empty!",
            throwable.getMessage());
    }

    @Test
    void givenANonEmptyListWhenRemovingLastValueThenTheSizeIsZero() {
        // Given
        DoublyLinkedList list = createPopulatedDoublyLinkedList(1);
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
        DoublyLinkedList list = createPopulatedDoublyLinkedList(2);
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
        DoublyLinkedList list = createPopulatedDoublyLinkedList(1);
        // When
        list.clear();
        // Then
        assertEquals(0, list.size());
        assertTrue(list.isEmpty());
    }

    @Test
    void givenAEmptyListWhenContainsValueThenFalseIsReturned() {
        // Given
        DoublyLinkedList list = createPopulatedDoublyLinkedList(0);
        // When
        boolean contains = list.contains(10);
        // Then
        assertFalse(contains);
        assertTrue(list.isEmpty());
    }

    @Test
    void givenAnInvalidValueWhenContainsValueThenFalseIsReturned() {
        // Given
        DoublyLinkedList list = createPopulatedDoublyLinkedList(4);
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
        DoublyLinkedList list = createPopulatedDoublyLinkedList(4);
        int value = 3;
        // When
        boolean contains = list.contains(value);
        // Then
        assertTrue(contains);
        assertFalse(list.isEmpty());
    }

    @Test
    void givenAnEmptyListWhenStringifyTheListThenNoElementsIsShown() {
        // Given
        DoublyLinkedList list = createPopulatedDoublyLinkedList(0);
        // When
        String toString = list.toString();
        // Then
        assertEquals("DoublyLinkedList=[]", toString);
    }

    @Test
    void givenANomEmptyListWhenStringifyTheListThenElementsAreShown() {
        // Given
        DoublyLinkedList list = createPopulatedDoublyLinkedList(4);
        // When
        String toString = list.toString();
        // Then
        assertEquals("DoublyLinkedList=[0, 1, 2, 3]", toString);
    }

    private DoublyLinkedList createPopulatedDoublyLinkedList(int capacity) {
        DoublyLinkedList list = new DoublyLinkedList();
        for (int i = 0; i < capacity; i++) {
            list.add(i);
        }
        return list;
    }
}
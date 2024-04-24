package br.com.acmattos.articles.dsa.structure.list;

import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ArrayListTest {
    @Test
    void givenDefaultCapacityWhenAnArrayListIsCreatedThenAnArrayListCapacityIsTheDefault() {
        // Given the default capacity usage
        int defaultCapacity = 10;
        // When
        ArrayList list = new ArrayList();
        // Then
        assertEquals(defaultCapacity, list.getCapacity());
    }

    @Test
    void givenAnNegativeCapacityWhenAnArrayListIsCreatedThenAnExceptionIsThrown() {
        // Given
        int capacity = -1;
        // When
        Throwable throwable =
            assertThrows(IllegalStateException.class,
                () -> new ArrayList(capacity));
        // Then
        assertNotNull(throwable);
        assertEquals("The array capacity can't be less than zero!",
            throwable.getMessage());
    }

    @Test
    void givenAPositiveCapacityWhenAnArrayListIsCreatedThenAnArrayListCapacityIsTheSameProvided() {
        // Given
        int capacity = 10;
        // When
        ArrayList list = new ArrayList(capacity);
        // Then
        assertEquals(capacity, list.getCapacity());
    }

    @Test
    void givenANegativeIndexWhenGettingAValueWithTheIndexThenAnExceptionIsThrown() {
        // Given
        ArrayList list = createPopulatedArrayList(1);
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
        ArrayList list = createPopulatedArrayList(1);
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
        ArrayList list = createPopulatedArrayList(1);
        int index = 0;
        // When
        int value = list.get(0);
        // Then
        assertEquals(0, value);
    }

    @Test
    void givenAnEmptyListWhenGettingFirstValueThenAnExceptionIsThrown() {
        // Given
        ArrayList list = createPopulatedArrayList(0);
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
        ArrayList list = createPopulatedArrayList(1);
        // When
        int value = list.getFirst();
        // Then
        assertEquals(0, value);
    }

    @Test
    void givenAnEmptyListWhenGettingLastValueThenAnExceptionIsThrown() {
        // Given
        ArrayList list = createPopulatedArrayList(0);
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
        ArrayList list = createPopulatedArrayList(1);
        // When
        int value = list.getLast();
        // Then
        assertEquals(0, value);
    }

    @Test
    void givenANegativeIndexWhenSettingAValueWithTheIndexThenAnExceptionIsThrown() {
        // Given
        ArrayList list = createPopulatedArrayList(1);
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
        ArrayList list = createPopulatedArrayList(1);
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
        ArrayList list = createPopulatedArrayList(1);
        int index = 0;
        // When
        list.set(index, 10);
        // Then
        assertEquals(10, list.get(index));
    }

    @Test
    void givenANegativeIndexWhenAddingAValueWithTheIndexThenAnExceptionIsThrown() {
        // Given
        ArrayList list = createPopulatedArrayList(1);
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
        ArrayList list = createPopulatedArrayList(1);
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
        ArrayList list = createPopulatedArrayList(1);
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
        ArrayList list = createPopulatedArrayList(1);
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
        ArrayList list = createPopulatedArrayList(1);
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
        ArrayList list = createPopulatedArrayList(1);
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
        ArrayList list = createPopulatedArrayList(0);
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
        ArrayList list = createPopulatedArrayList(1);
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
        ArrayList list = createPopulatedArrayList(0);
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
        ArrayList list = createPopulatedArrayList(1);
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
        ArrayList list = createPopulatedArrayList(3);
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
        ArrayList list = createPopulatedArrayList(1);
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
        ArrayList list = createPopulatedArrayList(1);
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
        ArrayList list = createPopulatedArrayList(1);
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
        ArrayList list = createPopulatedArrayList(3);
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
        ArrayList list = createPopulatedArrayList(0);
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
        ArrayList list = createPopulatedArrayList(1);
        // When
        int value = list.removeFirst();
        // Then
        assertEquals(0, value);
        assertEquals(0, list.size());
        assertTrue(list.isEmpty());
    }

    @Test
    void givenAnEmptyListWhenRemovingLastValueThenAnExceptionIsThrown() {
        // Given
        ArrayList list = createPopulatedArrayList(0);
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
        ArrayList list = createPopulatedArrayList(1);
        // When
        int value = list.remove();
        // Then
        assertEquals(0, value);
        assertEquals(0, list.size());
        assertTrue(list.isEmpty());
    }

    @Test
    void givenANonEmptyListWhenClearingTheListThenTheSizeIsZero() {
        // Given
        ArrayList list = createPopulatedArrayList(1);
        // When
        list.clear();
        // Then
        assertEquals(0, list.size());
        assertTrue(list.isEmpty());
    }

    @Test
    void givenAListWithCapacityOneWhenAddingAValueThenTheCapacityIsTwo() {
        // Given
        ArrayList list = createPopulatedArrayList(1);
        // When
        list.add(10);
        // Then
        assertEquals(2, list.size());
        assertEquals(2, list.getCapacity());
    }

    @Test
    void givenAEmptyListWhenContainsValueThenFalseIsReturned() {
        // Given
        ArrayList list = createPopulatedArrayList(0);
        // When
        boolean contains = list.contains(10);
        // Then
        assertFalse(contains);
        assertTrue(list.isEmpty());
    }

    @Test
    void givenAnInvalidValueWhenContainsValueThenFalseIsReturned() {
        // Given
        ArrayList list = createPopulatedArrayList(4);
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
        ArrayList list = createPopulatedArrayList(4);
        int value = 3;
        // When
        boolean contains = list.contains(value);
        // Then
        assertTrue(contains);
        assertFalse(list.isEmpty());
    }

    @Test
    void givenAnEmptyListWhenStringfyingTheListThenNoElementsIsShown() {
        // Given
        ArrayList list = createPopulatedArrayList(0);
        // When
        String toString = list.toString();
        // Then
        assertEquals("ArrayList=[]", toString);
    }

    @Test
    void givenANomEmptyListWhenStringfyingTheListThenElementsAreShown() {
        // Given
        ArrayList list = createPopulatedArrayList(4);
        // When
        String toString = list.toString();
        // Then
        assertEquals("ArrayList=[0, 1, 2, 3]", toString);
    }

    private ArrayList createPopulatedArrayList(int capacity) {
        ArrayList list = new ArrayList(capacity);
        for (int i = 0; i < capacity; i++) {
            list.add(i);
        }
        return list;
    }
}
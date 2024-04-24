package br.com.acmattos.articles.dsa.structure.list;

import java.util.NoSuchElementException;

/**
 * Resizable-array implementation of the {@code List} interface.
 */
public class ArrayList implements List {
    private static final int DEFAULT_CAPACITY = 10;
    private int[] array;
    private int size;
    private int capacity = DEFAULT_CAPACITY;

    /**
     * Construct an array list.
     * Initial {@code capacity} of this array list is 10.
     */
    public ArrayList() {
        this.array = new int[capacity];
    }

    /**
     * Construct an array list.
     * @param capacity the initial capacity of this array list.
     * @throws IllegalStateException if the capacity is bellow zero.
     */
    public ArrayList(int capacity) {
        if(capacity < 0) {
            throw new IllegalStateException(
                "The array capacity can't be less than zero!");
        }
        this.capacity = capacity;
        this.array = new int[capacity];
    }

    // vvvvvvvvvvvvvvvvvvvvv Positional Access Operations vvvvvvvvvvvvvvvvvvvvvv
    /**
     * Returns the value at the specified position in this list.
     * Time Complexity: O(1).
     *
     * @param index index of the value to return.
     * @return the value at the specified position in this list.
     * @throws IndexOutOfBoundsException if the index is out of range:
     *         ({@code index < 0 || index >= size()})
     */
    @Override
    public int get(int index) {
        if(index < 0 || index >= size) {
            throwIndexOutOfBoundsException(index);
        }
        return array[index];
    }

    /**
     * Gets the first value on this list.
     * Time Complexity: O(1).
     *
     * @return the first value.
     * @throws IndexOutOfBoundsException if this list is empty.
     */
    @Override
    public int getFirst() {
        return get(0);
    }

    /**
     * Gets the last value on this list.
     * Time Complexity: O(1).
     *
     * @return the last value.
     * @throws IndexOutOfBoundsException if this list is empty.
     */
    @Override
    public int getLast() {
        return get(size - 1);
    }

    /**
     * Replaces the current value at the specified position in this list with
     * the given value.
     * Time Complexity: O(1).
     *
     * @param index index of the value to replace.
     * @param value value to be stored at the {@code index}ed position.
     * @return the value previously stored at the {@code index}ed  position.
     * @throws IndexOutOfBoundsException if the index is out of range
     *         ({@code index < 0 || index >= size()})
     */
    @Override
    public int set(int index, int value) {
        if(index < 0 || index >= size) {
            throwIndexOutOfBoundsException(index);
        }
        int temp = array[index];
        array[index] = value;
        return temp;
    }

    private void throwIndexOutOfBoundsException(int index) {
        throw new IndexOutOfBoundsException(
            "Index out of bounds: " + index + ", size: " + size);
    }
    // ^^^^^^^^^^^^^^^^^^^^^ Positional Access Operations ^^^^^^^^^^^^^^^^^^^^^^

    // vvvvvvvvvvvvvvvvvvvvvvvv Modification Operations vvvvvvvvvvvvvvvvvvvvvvvv
    /**
     * Adds the specified value at the specified position in this list.
     * Shifts the value currently at that position (if any) and any subsequent
     * values to the right (adds one to their indices).
     * Time Complexity: O(n).
     *
     * @param index index at which the specified value is to be inserted.
     * @param value the value to be added.
     * @throws IndexOutOfBoundsException if the index is out of range
     *         ({@code index < 0 || index > size()})
     */
    @Override
    public void add(int index, int value) {
        if(index < 0 || index > size) {
            throwIndexOutOfBoundsException(index);
        }
        if(size == capacity) {
            resize();
        }
        for(int i = size; i > index; i--) {
            array[i] = array[i - 1];
        }
        array[index] = value;
        size++;
    }

    /**
     * Adds a value as the first value of this list.
     * After this operation completes normally, the given value will be a member
     * of this list, and it will be the first value in encounter order.
     * Time Complexity: O(n).
     *
     * @param value the value to be added.
     */
    @Override
    public void addFirst(int value) {
        add(0, value);
    }

    /**
     * Adds a value as the last value of this collection.
     * After this operation completes normally, the given value will be a member
     * of this list, and it will be the last value in encounter order.
     * Time Complexity: O(1).
     *
     * @param value the value to be added.
     */
    @Override
    public void add(int value) {
        if(size == capacity) {
            resize();
        }
        array[size] = value;
        size++;
    }

    /**
     * Resize the current list.
     * Time Complexity: O(n).
     * Space Complexity: O(2n + 1) => O(n).
     *
     * @throws IllegalStateException if the array exceeds its max capacity.
     */
    private void resize() {
        if(capacity + 1 < 0) {
            throw new IllegalStateException(
                "The array exceeds its max capacity!");
        }
        capacity = capacity + 1;
        int[] copy = new int[capacity];
        System.arraycopy(array, 0, copy, 0, size);
        this.array = copy;
    }

    /**
     * Removes the value at the specified position in this list.
     * Shifts any subsequent values to the left (subtracts one
     * from their indices). Returns the value that was removed from the list.
     * Time Complexity: O(n).
     *
     * @param index the index of the value to be removed
     * @return the value previously at the specified position
     * @throws IndexOutOfBoundsException if the index is out of range
     *         ({@code index < 0 || index >= size()})
     */
    @Override
    public int remove(int index) {
        if(index < 0 || index >= size) {
            throwIndexOutOfBoundsException(index);
        }
        int temp = array[index];
        for (int i = index; i < size - 1; i++) {
            array[i] = array[i + 1];
        }
        size--;
        return temp;
    }

    /**
     * Removes and returns the first value of this list (optional operation).
     * Time Complexity: O(n).
     *
     * @return the removed value.
     * @throws NoSuchElementException if the list is empty.
     */
    @Override
    public int removeFirst() {
        if(size == 0) {
            throw new NoSuchElementException("The list is empty!");
        }
        return remove(0);
    }

    /**
     * Removes and returns the last value of this list (optional operation).
     * Time Complexity: O(1).
     *
     * @return the removed value.
     * @throws NoSuchElementException if this list is empty
     */
    @Override
    public int remove() {
        if(size == 0) {
            throw new NoSuchElementException("The list is empty!");
        }
        size--;
        return array[size];
    }

    /**
     * Removes all the values from this list by resetting the size of the list
     * to zero.
     * The list will be considered empty after this call returns.
     * Time Complexity: O(1).
     */
    public void clear() {
        size = 0;
    }
    // ^^^^^^^^^^^^^^^^^^^^^^^^ Modification Operations ^^^^^^^^^^^^^^^^^^^^^^^^

    // vvvvvvvvvvvvvvvvvvvvvvvvvvv Query Operations vvvvvvvvvvvvvvvvvvvvvvvvvvvv

    /**
     * Gets the capacity of this list.
     * @return The capacity of this list.
     */
    public int getCapacity() {
        return capacity;
    }

    /**
     * Returns the number of values in this list.
     *
     * @return the number of values in this list
     */
    @Override
    public int size() {
        return size;
    }

    /**
     * Returns {@code true} if this list contains no values.
     *
     * @return {@code true} if this list contains no values
     */
    @Override
    public boolean isEmpty(){
        return size == 0;
    }

    /**
     * Returns {@code true} if this list contains the specified value.
     * More formally, returns {@code true} if and only if this list contains
     * at least one value.
     * Time Complexity: O(n).
     *
     * @param value the value whose presence in this list is to be tested.
     * @return {@code true} if this list contains the specified value.
     */
    @Override
    public boolean contains(int value) {
        if(size == 0) {
            return false;
        }
        for (int i = 0; i < size; i++) {
            if(array[i] == value) {
                return true;
            }
        }
        return false;
    }
    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^ Query Operations ^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("ArrayList=[");
        for(int i = 0; i < size; i++){
            sb.append(array[i]).append(", ");
        }
        if(size > 0) {
            return sb.substring(0, sb.lastIndexOf(", ")) + "]";
        }
        return sb + "]";
    }
}

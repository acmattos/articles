package br.com.acmattos.articles.dsa.structure.list

import java.util.NoSuchElementException

/**
 * In the context of data structures, a list typically refers to a linear data
 * structure that stores a list of values in a specific order. Here are
 * some common characteristics associated with lists in data structures:
 * <ul>
 *  <li>{@code Ordered Collection}: Lists maintain the order of values as they
 *      are inserted, allowing for sequential access based on their position or
 *      index within the list.</li>
 *  <li>{@code Indexed Access}: Elements in a list can be accessed using their
 *      index or position within the list. This allows for efficient retrieval
 *      and manipulation of individual values.</li>
 *  <li>{@code Dynamic Size}: Lists can dynamically grow or shrink in size as
 *      values are added or removed. This dynamic resizing capability makes
 *      lists flexible and adaptable to changing data requirements.</li>
 *  <li>{@code Element Duplication}: Lists may allow duplicate values, meaning
 *      the same value can appear multiple times within the list.</li>
 *  <li>{@code Mutable}: Lists are typically mutable data structures, meaning
 *      that values can be modified, added, or removed after the list is
 *      created.</li>
 *  <li>{@code Linear Traversal}: Lists support linear traversal, allowing you
 *       to iterate through all values in the list sequentially, usually using
 *       loops or iterators.</li>
 *  <li>{@code Random Access}: Some list implementations, such as arrays or
 *       array-based lists, support constant-time random access to values
 *       based on their index. This means that accessing an value at a
 *       specific index takes the same amount of time, regardless of the size of
 *       the list.</li>
 *  <li>{@code Insertion and Deletion}: Lists provide efficient operations for
 *       inserting and deleting values at different positions within the list.
 *       The time complexity of these operations may vary depending on the
 *       implementation (e.g., linked lists vs. array lists).</li>
 * </ul>
 * These characteristics make lists a versatile and widely used data structure
 * for storing and managing lists of values in computer programming and
 * software development. The specific characteristics and behavior of a list may
 * vary based on the programming language and the type of list implementation
 * used (e.g., linked list, array list, doubly linked list, etc.).
 */
interface KList {
    // vvvvvvvvvvvvvvvvvvvvv Positional Access Operations vvvvvvvvvvvvvvvvvvvvvv
    /**
     * Returns the value at the specified position in this list.
     *
     * @param index index of the value to return.
     * @return the value at the specified position in this list.
     * @throws IndexOutOfBoundsException if the index is out of range:
     *         ({@code index < 0 || index >= size()})
     */
    fun get(index: Int): Int

    /**
     * Gets the first value on this list.
     *
     * @return the first value.
     * @throws IndexOutOfBoundsException if this list is empty.
     */
    fun getFirst(): Int

    /**
     * Gets the last value on this list.
     *
     * @return the last value.
     * @throws IndexOutOfBoundsException if this list is empty.
     */
    fun getLast(): Int

    /**
     * Replaces the current value at the specified position in this list with
     * the given value.
     *
     * @param index index of the value to replace.
     * @param value value to be stored at the {@code index}ed position.
     * @return the value previously stored at the {@code index}ed position.
     * @throws IndexOutOfBoundsException if the index is out of range
     *         ({@code index < 0 || index >= size()})
     */
    fun set(index: Int, value: Int): Int
    // ^^^^^^^^^^^^^^^^^^^^^ Positional Access Operations ^^^^^^^^^^^^^^^^^^^^^^

    // vvvvvvvvvvvvvvvvvvvvvvvv Modification Operations vvvvvvvvvvvvvvvvvvvvvvvv
    /**
     * Adds the specified value at the specified position in this list.
     * Shifts the value currently at that position (if any) and any subsequent
     * values to the right (adds one to their indices).
     *
     * @param index index at which the specified value is to be inserted.
     * @param value the value to be added.
     * @throws IndexOutOfBoundsException if the index is out of range
     *         ({@code index < 0 || index > size()})
     */
    fun add(index: Int, value: Int)

    /**
     * Adds a value as the first value of this list.
     * After this operation completes normally, the given value will be a member
     * of this list, and it will be the first value in encounter order.
     *
     * @param value the value to be added.
     */
    fun addFirst(value: Int)

    /**
     * Adds a value as the last value of this list.
     * After this operation completes normally, the given value will be a member
     * of this list, and it will be the last value in encounter order.
     *
     * @param value the value to be added.
     */
    fun add(value: Int)

    /**
     * Removes the value at the specified position in this list.
     * Shifts any subsequent values to the left (subtracts one
     * from their indices). Returns the value that was removed from the list.
     *
     * @param index the index of the value to be removed
     * @return the value previously at the specified position
     * @throws IndexOutOfBoundsException if the index is out of range
     *         ({@code index < 0 || index >= size()})
     */
    fun remove(index: Int): Int

    /**
     * Removes and returns the first value of this list.
     *
     * @return the removed value
     * @throws NoSuchElementException if the list is empty.
     */
    fun removeFirst(): Int

    /**
     * Removes and returns the last value of this list.
     *
     * @return the removed value.
     * @throws NoSuchElementException if this list is empty.
     */
    fun remove(): Int

    /**
     * Removes all of the values from this list.
     * The list will be empty after this call returns.
     */
    fun clear()
    // ^^^^^^^^^^^^^^^^^^^^^^^^ Modification Operations ^^^^^^^^^^^^^^^^^^^^^^^^

    // vvvvvvvvvvvvvvvvvvvvvvvvvvv Query Operations vvvvvvvvvvvvvvvvvvvvvvvvvvvv
    /**
     * Returns the number of values in this list.
     *
     * @return the number of values in this list
     */
    fun size(): Int

    /**
     * Returns {@code true} if this list contains no values.
     *
     * @return {@code true} if this list contains no values
     */
    fun isEmpty(): Boolean

    /**
     * Returns {@code true} if this list contains the specified value.
     * More formally, returns {@code true} if and only if this list contains
     * at least one value.
     *
     * @param value the value whose presence in this list is to be tested.
     * @return {@code true} if this list contains the specified value.
     */
    fun contains(value: Int): Boolean
    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^ Query Operations ^^^^^^^^^^^^^^^^^^^^^^^^^^^^
}

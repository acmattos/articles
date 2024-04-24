package br.com.acmattos.articles.dsa.structure.list;

import java.util.NoSuchElementException;

/**
 * Linked List implementation of the {@code List} interface.
 */
public class LinkedList implements List {
    private Node head;
    private Node tail;
    private int size;

    private static class Node {
        int value;
        Node next;
        Node(int value) {
            this.value = value;
        }
    }

    /**
     * Construct a linked list.
     */
    public LinkedList() {
    }

    // vvvvvvvvvvvvvvvvvvvvv Positional Access Operations vvvvvvvvvvvvvvvvvvvvvv
    /**
     * Returns the value at the specified position in this list.
     * Time Complexity: O(n).
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
        return getNode(index).value;
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
        if(size == 0) {
            throwIndexOutOfBoundsException(0);
        }
        return head.value;
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
        if(size == 0) {
            throwIndexOutOfBoundsException(size - 1);
        }
        return tail.value;
    }

    /**
     * Replaces the current value at the specified position in this list with
     * the given value.
     * Time Complexity: O(n).
     *
     * @param index index of the value to replace.
     * @param value value to be stored at the {@code index}ed position.
     * @return the value previously stored at the {@code index}ed position.
     * @throws IndexOutOfBoundsException if the index is out of range
     *         ({@code index < 0 || index >= size()})
     */
    @Override
    public int set(int index, int value) {
        if(index < 0 || index >= size) {
            throwIndexOutOfBoundsException(index);
        }
        Node temp = getNode(index);
        int oldValue = temp.value;
        temp.value = value;
        return oldValue;
    }

    private Node getNode(int index) {
        Node temp = head;
        for (int i = 1; i <= index; i++) {
            temp = temp.next;
        }
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
        Node added = new Node(value);
        if (index == 0) {
            if (size != 0) {
                added.next = head;
            }
            head = added;
            if (size == 0) {
                tail = head;
            }
        } else if (index == size) {
            tail.next = added;
            tail = added;
        } else {
            Node prev = head;
            for (int i = 1; i < index; i++) {
                prev = prev.next;
            }
            added.next = prev.next;
            prev.next = added;
        }
        size++;
    }

    /**
     * Adds a value as the first value of this list.
     * After this operation completes normally, the given value will be a member
     * of this list, and it will be the first value in encounter order.
     * Time Complexity: O(1).
     *
     * @param value the value to be added.
     */
    @Override
    public void addFirst(int value) {
        add(0, value);
    }

    /**
     * Adds a value as the last value of this list.
     * After this operation completes normally, the given value will be a member
     * of this list, and it will be the last value in encounter order.
     * Time Complexity: O(1).
     *
     * @param value the value to be added.
     */
    @Override
    public void add(int value) {
        add(size, value);
    }

    /**
     * Removes the value at the specified position in this list.
     * Shifts any subsequent values to the left (subtracts one
     * from their indices). Returns the value that was removed from the list.
     * Time Complexity: O(n) (O(1) -> head).
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
        Node removed;
        if(index == 0) {
            removed = head;
            head = head.next;
            removed.next = null;
            if (size - 1 == 0) {
                tail = head;
            }
        } else {
            Node prev = getNode(index - 1);
            removed = prev.next;
            if(removed == tail) {
                tail = prev;
            }
            prev.next = removed.next;
        }
        size--;
        return removed.value;
    }

    /**
     * Removes and returns the first value of this list (optional operation).
     * Time Complexity: O(1).
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
     * Time Complexity: O(n).
     *
     * @return the removed value.
     * @throws NoSuchElementException if this list is empty
     */
    @Override
    public int remove() {
        if(size == 0) {
            throw new NoSuchElementException("The list is empty!");
        }
        return remove(size - 1);
    }

    /**
     * Removes all the values from this list by resetting the size of the list
     * to zero.
     * The list will be considered empty after this call returns.
     * Time Complexity: O(1).
     */
    public void clear() {
        head = null;
        tail = null;
        size = 0;
    }
    // ^^^^^^^^^^^^^^^^^^^^^^^^ Modification Operations ^^^^^^^^^^^^^^^^^^^^^^^^

    // vvvvvvvvvvvvvvvvvvvvvvvvvvv Query Operations vvvvvvvvvvvvvvvvvvvvvvvvvvvv
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
        Node temp = head;
        while (temp != null) {
            if(temp.value == value) {
                return true;
            }
            temp = temp.next;
        }
        return false;
    }
    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^ Query Operations ^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    @Override
    public String toString() {
        return "LinkedList=" +
            stringify();
    }

    private String stringify() {
        Node temp = head;
        StringBuilder sb = new StringBuilder("[");
        while (temp != null){
            sb.append(temp.value).append(", ");
            temp = temp.next;
        }
        if(size > 0) {
            return sb.substring(0, sb.lastIndexOf(", ")) + "]";
        }
        return sb + "]";
    }
}

package com.snek.framework.data_types;







/**
 * An ArrayDequeue that allows indexing in O(1) time.
 */
public class IndexedArrayDeque<E> extends AccessibleArrayDeque<E> {
    public IndexedArrayDeque() {
        super();
    }



    /**
     * Returns the element at index <index>
     * @param index The index.
     * @return The element.
     */
    @SuppressWarnings("unchecked")
    public E get(int index) {
        if (index < 0 || index >= size()) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size());
        }

        return (E)elements[(head + index) & (elements.length - 1)];
    }
}
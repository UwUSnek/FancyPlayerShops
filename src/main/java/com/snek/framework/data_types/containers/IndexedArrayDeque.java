package com.snek.framework.data_types.containers;

import java.util.function.Supplier;








/**
 * An ArrayDequeue that allows indexing in O(1) time.
 */
public class IndexedArrayDeque<E> extends AccessibleArrayDeque<E> {
    public IndexedArrayDeque() {
        super();
    }



    /**
     * Returns the element at index <index>.
     * @param index The index.
     * @return The element.
     */
    @SuppressWarnings("unchecked")
    public E get(int index) {
        if(index < 0 || index >= size()) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size());
        }

        return (E)elements[(head + index) % elements.length];
    }



    /**
     * Returns the element at index <index>.
     * If no element is present at the specified index, new elements are added to the queue until the desired size is reached.
     * @param index The index.
     * @param f The supplier function used to create new elements.
     * @return The element.
     */
    public E getOrAdd(int index, Supplier<E> f) {
        while(index >= size()) add(f.get());
        return get(index);
    }



    /**
     * Sets the element at index <index>.
     * @param index The index.
     * @param value The value.
     */
    public void set(int index, E value) {
        if(index < 0 || index >= size()) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size());
        }

        elements[(head + index) % elements.length] = value;
    }
}
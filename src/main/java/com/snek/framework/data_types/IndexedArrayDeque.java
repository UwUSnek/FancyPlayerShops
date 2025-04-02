package com.snek.framework.data_types;

import java.lang.reflect.Field;
import java.util.ArrayDeque;






//FIXME make index access O(1)
//FIXME make index access O(1)
//FIXME make index access O(1)
//FIXME make index access O(1)

/**
 * An ArrayDequeue that allows indexing in O(n) time instead of the O(1) it should have been because Java is stupid.
 */
public class IndexedArrayDeque<E> extends ArrayDeque<E> {
    public IndexedArrayDeque() {
        super();
    }



    /**
     * Returns the element at index <index>
     * @param index The index.
     * @return The element.
     */
    public E get(int index) {
        if (index < 0 || index >= size()) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size());
        }

        int i = 0;
        for (E element : this) {
            if (i == index) {
                return element;
            }
            i++;
        }

        throw new IllegalStateException("Element not found, this should never happen.");
    }
}
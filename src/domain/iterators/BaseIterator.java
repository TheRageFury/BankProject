package domain.iterators;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * This ADT represents an immutable iterator over a collection
 *
 * @param <T> The type of the collection whose iterator is needed on
 */
public class BaseIterator<T> implements Iterator<T> {
    private final List<T> container;
    private int i;

    /**
     * Creates a new immutable iterator with the given collecton.
     *
     * @param container The collection of elements whose iterator is needed on
     */
    public BaseIterator(Collection<T> container) {
        this.container = new ArrayList<>(container);
        this.i = 0;
    }

    @Override
    public boolean hasNext() {
        return this.container.size() > 0;
    }

    @Override
    public T next() {
        return this.container.get(i++);
    }
}

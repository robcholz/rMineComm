package robcholz.manager;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public abstract class AbstractCommManager<E> {
    private final Set<E> markedElements;

    public AbstractCommManager () {
        this.markedElements = new HashSet<>();
    }

    public Set<E> getMarkedElements () {
        return Collections.unmodifiableSet(markedElements);
    }

    public void update (){}

    public boolean add (E element) {
        if (markedElements.contains(element))
            return false;
        return markedElements.add(element);
    }

    public boolean remove (E element) {
        if (!markedElements.contains(element))
            return false;
        return markedElements.remove(element);
    }
}

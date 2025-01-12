package bloomfilter;

import java.util.Collection;
import java.util.LinkedList;

public class BloomList<E> extends LinkedList<E> {
    private final BloomFilter<E> bf;

    public BloomList(BloomFilter<E> bf) {
        this.bf = bf;
    }

    public void resetBloomFilter() {
        bf.reset();
        // Re-add all current elements
        for (E element : this) {
            bf.add(element);
        }
    }

    @Override
    public boolean add(E e) {
        bf.add(e);
        return super.add(e);
    }

    @Override
    public void add(int index, E element) {
        bf.add(element);
        super.add(index, element);
    }

    @Override
    public E set(int i, E e) {
        bf.add(e);
        return super.set(i, e);
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        for (E element : c) {
            bf.add(element);
        }
        return super.addAll(index, c);
    }

    @Override
    public boolean contains(Object e) {
        // First check bloom filter for quick rejection
        if (!bf.containsMaybe((E)e)) {
            return false;
        }
        // If possibly contained, check actual list
        return super.contains(e);
    }

    @Override
    public void clear() {
        bf.reset();
        super.clear();
    }
}
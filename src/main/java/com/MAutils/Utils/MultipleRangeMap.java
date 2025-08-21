package com.MAutils.Utils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map.Entry;
import java.util.NavigableMap;
import java.util.Objects;
import java.util.TreeMap;

/** Index of possibly-overlapping numeric ranges [start,end) -> value. */
public final class MultipleRangeMap<N extends Comparable<N>, V> {
    private static final class Interval<N extends Comparable<N>, V> {
        final N start, end; final V value; final long seq;
        Interval(N s, N e, V v, long q) { start = s; end = e; value = v; seq = q; }
    }

    private final NavigableMap<N, List<Interval<N,V>>> byStart = new TreeMap<>();
    private long counter = 0;

    /** Add an interval [start,end) for value. Overlaps allowed. */
    public void put(N start, N end, V value) {
        if (start.compareTo(end) >= 0) throw new IllegalArgumentException("start must be < end");
        byStart.computeIfAbsent(start, k -> new ArrayList<>())
               .add(new Interval<>(start, end, value, counter++));
    }

    /** Return all values whose intervals intersect [qStart, qEnd). */
    public List<V> getRangeOverlapping(N qStart, N qEnd) {
        if (qStart.compareTo(qEnd) >= 0) throw new IllegalArgumentException("qStart must be < qEnd");
        List<Interval<N,V>> hits = new ArrayList<>();
        // Only intervals with start < qEnd can possibly overlap
        for (Entry<N, List<Interval<N,V>>> e : byStart.headMap(qEnd, false).entrySet()) {
            for (Interval<N,V> iv : e.getValue()) {
                // Overlap test: [iv.start, iv.end) ∩ [qStart, qEnd) ≠ ∅  ⇔  iv.end > qStart && iv.start < qEnd
                if (iv.end.compareTo(qStart) > 0) hits.add(iv);
            }
        }
        // Stable, readable order: by start then insertion
        hits.sort(Comparator.<Interval<N,V>, N>comparing(iv -> iv.start).thenComparingLong(iv -> iv.seq));

        List<V> out = new ArrayList<>(hits.size());
        for (Interval<N,V> iv : hits) out.add(iv.value);
        return out;
    }

    /** Optional: remove exactly one stored interval for a value. */
    public boolean remove(N start, N end, V value) {
        List<Interval<N,V>> list = byStart.get(start);
        if (list == null) return false;
        boolean removed = list.removeIf(iv -> iv.end.compareTo(end) == 0 && Objects.equals(iv.value, value));
        if (list.isEmpty()) byStart.remove(start);
        return removed;
    }
}

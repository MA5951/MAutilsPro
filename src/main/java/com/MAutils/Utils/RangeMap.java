package com.MAutils.Utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map.Entry;
import java.util.NavigableMap;
import java.util.TreeMap;

/** Maps non-overlapping numeric ranges [start, end) to values. */
public final class RangeMap<N extends Comparable<N>, V> {
    private static final class Interval<N extends Comparable<N>, V> {
        final N start, end;
        final V value;
        Interval(N start, N end, V value) { this.start = start; this.end = end; this.value = value; }
        @Override public String toString() { return "[" + start + "," + end + ") -> " + value; }
    }

    // Keyed by interval start
    private final NavigableMap<N, Interval<N, V>> byStart = new TreeMap<>();

    /** Put a new non-overlapping interval [start, end) -> value. */
    public void put(N start, N end, V value) {
        if (start.compareTo(end) >= 0) {
            throw new IllegalArgumentException("start must be < end");
        }

        // Check overlap with the interval that starts at or before 'start'
        Entry<N, Interval<N, V>> floor = byStart.floorEntry(start);
        if (floor != null) {
            Interval<N, V> prev = floor.getValue();
            // overlap if prev.end > start
            if (prev.end.compareTo(start) > 0) {
                throw new IllegalArgumentException("Overlaps previous interval: " + prev);
            }
        }

        // Check overlap with following intervals whose start < end
        Entry<N, Interval<N, V>> next = byStart.ceilingEntry(start);
        while (next != null && next.getKey().compareTo(end) < 0) {
            throw new IllegalArgumentException("Overlaps next interval: " + next.getValue());
        }

        byStart.put(start, new Interval<>(start, end, value));
    }

    /** Return the value whose interval contains point, or null if none. */
    public V get(N point) {
        Entry<N, Interval<N, V>> floor = byStart.floorEntry(point);
        if (floor == null) return null;
        Interval<N, V> iv = floor.getValue();
        // Contains if point in [start, end)
        if (point.compareTo(iv.start) >= 0 && point.compareTo(iv.end) < 0) {
            return iv.value;
        }
        return null;
    }

    /** Remove exactly the interval [start, end); returns true if removed. */
    public boolean remove(N start, N end) {
        Interval<N, V> iv = byStart.get(start);
        if (iv != null && iv.end.compareTo(end) == 0) {
            byStart.remove(start);
            return true;
        }
        return false;
    }

    /** For debugging/inspection. */
    public List<String> intervals() {
        List<String> out = new ArrayList<>();
        for (Interval<N, V> iv : byStart.values()) out.add(iv.toString());
        return Collections.unmodifiableList(out);
    }
}

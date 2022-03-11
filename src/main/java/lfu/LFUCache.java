package lfu;

import com.google.common.cache.RemovalCause;
import com.google.common.cache.RemovalListener;
import com.google.common.cache.RemovalNotification;
import com.google.common.collect.MapMaker;
import shared.Cache;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Eviction policy
 * LFU (Least Frequently Used) — counts how often an item is needed and discards rare elements.
 */
public class LFUCache<K, V> implements Cache<K, V> {

    private ConcurrentMap<K, V> values;//cache K and V
    private ConcurrentMap<K, Integer> counts;//K and counters
    private ConcurrentMap<Integer, LinkedHashSet<V>> lists;//Counter and item list
    private ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    private int capacity;
    private int minimum = -1;
    private int evictionNumber;
    private long start;
    private long end;
    private List<Long> times = new ArrayList();
    private RemovalListener removalListener;

    public LFUCache(int capacity, RemovalListener<Object, Object> removalListener) {
        this.capacity = capacity;
        this.values = new MapMaker().makeMap();
        this.counts = new MapMaker().makeMap();
        this.lists = new MapMaker().makeMap();
        this.lists.put(1, new LinkedHashSet<>());
        this.removalListener = removalListener;
    }

    @Override
    public Optional<V> get(K key) {
        if (!values.containsKey(key)) {
            return Optional.empty();
        }
        int count = counts.get(key);
        counts.put(key, count + 1);
        lists.get(count).remove(key);

        if (count == minimum && lists.get(count).size() == 0) {
            minimum++;
        }
        if (!lists.containsKey(count + 1)) {
            lists.put(count + 1, new LinkedHashSet<>());
        }
        lists.get(count + 1).add(values.get(key));
        return Optional.of(values.get(key));
    }

    private boolean set(K key, V value) {
        start = System.currentTimeMillis();
        if (capacity <= 0) {
            end = System.currentTimeMillis();
            times.add(end - start);
            return false;
        }
        if (values.containsKey(key)) {
            values.put(key, value);
            get(key);
            end = System.currentTimeMillis();
            times.add(end - start);
            return true;
        }
        if (values.size() >= capacity) {
            V evit = lists.get(minimum).iterator().next();
            lists.get(minimum).remove(evit);
            values.remove(evit);
            counts.remove(evit);
            evictionNumber++;
            System.out.println("Value removed - " + evit);
            removalListener.onRemoval(RemovalNotification.create(key, evit, RemovalCause.EXPIRED));
        }
        values.put(key, value);
        counts.put(key, 1);
        minimum = 1;
        lists.get(1).add(value);
        end = System.currentTimeMillis();
        times.add(end - start);
        return true;
    }

    @Override
    public boolean put(K key, V value) {
        return set(key, value);
    }

    @Override
    public int size() {
        return values.size();
    }

    @Override
    public boolean isEmpty() {
        return values.isEmpty();
    }

    @Override
    public void clear() {
        values.clear();
        lists.clear();
        counts.clear();
    }

    public void displayCache() {
        System.out.println(values);
    }

    @Override
    public void displayStatistics() {
        System.out.println("eviction count - " + evictionNumber);
        System.out.println("average time - " + getAverageTime());
    }

    private Long getAverageTime() {
        Long totalTime = 0L;
        for (Long time : times) {
            totalTime += time;
        }
        return (totalTime / times.size());
    }
}
package lfu;

import com.google.common.collect.MapMaker;
import shared.Cache;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutionException;
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
    private int cap;
    private int min = -1;
    private int evictionNumber;
    private long start;
    private long end;
    private List<Long> times = new ArrayList();

    public LFUCache(int capacity) {
        cap = capacity;
        values = new MapMaker().makeMap();
        counts = new MapMaker().makeMap();
        lists = new MapMaker().makeMap();
        lists.put(1, new LinkedHashSet<>());
    }

    @Override
    public Optional<V> get(K key) throws ExecutionException {
        if (!values.containsKey(key))
            return Optional.empty();
        int count = counts.get(key);
        counts.put(key, count + 1);
        lists.get(count).remove(key);

        if (count == min && lists.get(count).size() == 0)
            min++;
        if (!lists.containsKey(count + 1))
            lists.put(count + 1, new LinkedHashSet<>());
        lists.get(count + 1).add(values.get(key));
        return (Optional<V>) values.get(key);
    }

    public boolean set(K key, V value) throws ExecutionException {
        start = System.currentTimeMillis();
        if (cap <= 0) {
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
        if (values.size() >= cap) {
            V evit = lists.get(min).iterator().next();
            lists.get(min).remove(evit);
            values.remove(evit);
            counts.remove(evit);
            evictionNumber++;
            System.out.println("Value removed - " + evit);
        }
        values.put(key, value);
        counts.put(key, 1);
        min = 1;
        lists.get(1).add(value);
        end = System.currentTimeMillis();
        times.add(end - start);
        return true;
    }

    @Override
    public boolean put(K key, V value) {
        try {
            return set(key, value);
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return false;
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
        Long total = 0L;
        for (Long l : times)
            total += l;
        return (total / times.size());
    }
}
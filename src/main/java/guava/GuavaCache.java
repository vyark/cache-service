package guava;

import com.google.common.cache.*;
import shared.Cache;

import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class GuavaCache<K, V> implements Cache<K, V> {
    private static final int MAX_CACHE_SIZE = 10000;
    private static final int CONCURRENCY_LEVEL = 1;

    private LoadingCache<K, V> cache =
            CacheBuilder.newBuilder().expireAfterAccess(5, TimeUnit.SECONDS)
                    .maximumSize(MAX_CACHE_SIZE)
                    .recordStats()
                    .concurrencyLevel(CONCURRENCY_LEVEL)
                    .removalListener(new RemovalListener<K, V>() {
                        @Override
                        public void onRemoval(RemovalNotification<K, V> notification) {
                            System.out.println("Key - " + notification.getKey() + " removed due to " + notification.getCause());
                        }
                    })
                    .build(new CacheLoader<K, V>() {
                        @Override
                        public V load(K key) throws ExecutionException {
                            return get(key).get();
                        }
                    });

    public void displayStatistics() {
        System.out.println("average load penalty - " + cache.stats().averageLoadPenalty());
        System.out.println("eviction count - " + cache.stats().evictionCount());
        System.out.println("cache hit count - " + cache.stats().hitCount());
        System.out.println("cache hit rate - " + cache.stats().hitRate());
        System.out.println("cache load count - " + cache.stats().loadCount());
        System.out.println("cache miss rate - " + cache.stats().missRate());
        System.out.println("cache total load time - " + cache.stats().totalLoadTime());
    }

    @Override
    public boolean put(K key, V value) {
        cache.put(key, value);
        return true;
    }

    @Override
    public Optional<V> get(K key) throws ExecutionException {
        return Optional.of(cache.get(key));
    }

    @Override
    public int size() {
        return (int) cache.size();
    }

    @Override
    public boolean isEmpty() {
        return cache.size() == 0;
    }

    @Override
    public void clear() {
        cache.invalidateAll();
    }

    public void displayCache() {
        cache.asMap().values().stream().forEach(value -> System.out.print(value));
    }
}

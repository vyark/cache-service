package guava;

import com.google.common.cache.*;
import shared.CacheService;

import java.util.concurrent.TimeUnit;

/**
 * LRU (Least Recently Used) — discards items that have not been used for a long time
 * (more than other elements in the cache).
 */
public class GuavaCacheService implements CacheService {
    private static int MAX_CACHE_SIZE = 100000;

    private LoadingCache<String, Object> cache =
            CacheBuilder.newBuilder().expireAfterAccess(5, TimeUnit.SECONDS)
                    .maximumSize(MAX_CACHE_SIZE)
                    .recordStats()
                    .concurrencyLevel(1)
                    .removalListener(new RemovalListener<String, Object>() {
                        @Override
                        public void onRemoval(RemovalNotification<String, Object> notification) {
                            System.out.println("Key - " + notification.getKey() + " removed due to " + notification.getCause());
                        }
                    })
                    .build(new CacheLoader<String, Object>() {
                        @Override
                        public Integer load(String key) {
                            return key.length();
                        }
                    });

    @Override
    public Object get(String key) {
        return cache.getIfPresent(key);
    }

    @Override
    public void put(String key, Object value) {
        if (cache.size() == MAX_CACHE_SIZE) {
            Object firstKey = (Object) cache.asMap().entrySet().iterator().next();
            cache.invalidate(firstKey);
        }
        cache.put(key, value);
    }

    @Override
    public void displayStatistics() {
        System.out.println("average load penalty - " + cache.stats().averageLoadPenalty());
        System.out.println("eviction count - " + cache.stats().evictionCount());
        System.out.println("cache hit count - " + cache.stats().hitCount());
        System.out.println("cache hit rate - " + cache.stats().hitRate());
        System.out.println("cache load count - " + cache.stats().loadCount());
        System.out.println("cache miss rate - " + cache.stats().missRate());
        System.out.println("cache total load time - " + cache.stats().totalLoadTime());
    }
}

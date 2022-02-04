package lru;

import com.google.common.cache.RemovalListener;
import com.google.common.cache.RemovalNotification;
import shared.CacheService;
import shared.Item;

import java.util.Optional;

import static shared.Cache.MAX_CACHE_SIZE;

public class LRUCacheService implements CacheService {

    LRUCache<String, Item> cache = new LRUCache<>(MAX_CACHE_SIZE, new RemovalListener<>() {
        @Override
        public void onRemoval(RemovalNotification<Object, Object> removalNotification) {
            System.out.println("Removal");
        }
    });

    @Override
    public Optional<Item> get(String key) {
        return cache.get(key);
    }

    @Override
    public void put(String key, Item value) {
        cache.put(key, value);
    }

    @Override
    public void displayStatistics() {
        cache.displayStatistics();
    }

    @Override
    public void displayCache() {
        cache.displayCache();
    }
}

package lfu;

import shared.CacheService;
import shared.Item;

import java.util.Optional;
import java.util.concurrent.ExecutionException;

import static shared.Cache.MAX_CACHE_SIZE;

public class LFUCacheService implements CacheService {

    LFUCache<String, Item> cache = new LFUCache<>(MAX_CACHE_SIZE);

    @Override
    public Optional<Item> get(String key) throws ExecutionException {
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

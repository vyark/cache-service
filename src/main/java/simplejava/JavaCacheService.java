package simplejava;

import shared.CacheService;

/**
 * Eviction policy
 * LFU (Least Frequently Used) — counts how often an item is needed and discards rare elements.
 */
public class JavaCacheService implements CacheService {
    @Override
    public Object get(String id) {
        return null;
    }

    @Override
    public void put(String key, Object value) {
    }

    @Override
    public void displayStatistics() {
    }
}

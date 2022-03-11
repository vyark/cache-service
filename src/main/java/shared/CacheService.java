package shared;

import java.util.Optional;
import java.util.concurrent.ExecutionException;

public interface CacheService<K, V> {

    Optional<Item> get(String key) throws ExecutionException;

    void put(String key, Item value);

    void displayStatistics();

    void displayCache();

    Cache<K, V> getCache();
}

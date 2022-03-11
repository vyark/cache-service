package shared;

import java.util.Optional;
import java.util.concurrent.ExecutionException;

public interface Cache<K, V> {
    boolean put(K key, V value);

    Optional<V> get(K key) throws ExecutionException;

    int size();

    boolean isEmpty();

    void clear();

    void displayCache();

    void displayStatistics();
}
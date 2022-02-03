package shared;

import java.util.Optional;
import java.util.concurrent.ExecutionException;

public interface CacheService {

    Optional<Item> get(String key) throws ExecutionException;

    void put(String key, Item value);

    void displayStatistics();

    void displayCache();
}

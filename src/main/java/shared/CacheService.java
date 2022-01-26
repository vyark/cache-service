package shared;

public interface CacheService {

    Object get(String key);

    void put(String key, Object value);

    void displayStatistics();
}

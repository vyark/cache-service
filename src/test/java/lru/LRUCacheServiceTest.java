package lru;

import org.junit.jupiter.api.Test;
import shared.CacheService;
import shared.Item;

import java.util.Optional;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LRUCacheServiceTest {
    private static final String KEY = "1";
    private static final Item ITEM = new Item(KEY);

    private CacheService cacheService = new LRUCacheService();

    @Test
    public void testGetFromCache() throws ExecutionException {
        cacheService.put(KEY, ITEM);
        cacheService.put("2", new Item("2"));
        cacheService.put("3", new Item("3"));
        cacheService.put("4", new Item("4"));

        cacheService.displayCache();

        assertEquals(Optional.of(ITEM), cacheService.get(KEY));
        assertEquals(4, cacheService.getCache().size());

        cacheService.getCache().clear();

        assertEquals(0, cacheService.getCache().size());
    }

    @Test
    public void testPutIntoCache() throws ExecutionException {
        cacheService.put(KEY, ITEM);

        assertEquals(Optional.of(ITEM), cacheService.get(KEY));
    }
}

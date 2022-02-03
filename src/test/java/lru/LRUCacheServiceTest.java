package lru;

import org.junit.Test;
import shared.CacheService;
import shared.Item;

import java.util.concurrent.ExecutionException;

import static org.junit.Assert.assertEquals;

public class LRUCacheServiceTest {
    private static final String KEY = "1";
    private static final Item ITEM = new Item(KEY);

    private CacheService cacheService = new LRUCacheService();

    @Test
    public void get() throws ExecutionException {
        cacheService.put(KEY, ITEM);

        assertEquals(cacheService.get(KEY), ITEM);
    }

    @Test
    public void put() throws ExecutionException {
        cacheService.put(KEY, ITEM);

        assertEquals(cacheService.get(KEY), ITEM);
    }

}

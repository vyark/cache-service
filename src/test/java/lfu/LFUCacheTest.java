package lfu;

import com.google.common.cache.RemovalListener;
import com.google.common.cache.RemovalNotification;
import org.junit.jupiter.api.Test;
import shared.Cache;
import shared.Item;

import java.util.Optional;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.*;

public class LFUCacheTest {
    private static final Item ITEM = new Item("value");
    private static final String KEY = "key";
    private Cache<String, Item> cache = new LFUCache<>(1, new RemovalListener<>() {
        @Override
        public void onRemoval(RemovalNotification<Object, Object> removalNotification) {
            System.out.println("Removal");
        }
    });

    @Test
    public void testGetDoNotContain() throws Exception {
        cache.put(KEY, ITEM);

        assertEquals(Optional.empty(), cache.get(""));
    }

    @Test
    public void testGet() throws ExecutionException {
        cache.put(KEY, ITEM);

        assertEquals(Optional.of(ITEM), cache.get(KEY));
    }

    @Test
    public void testPutExistingValue() {
        cache.put(KEY, ITEM);

        assertTrue(cache.put(KEY, ITEM));
    }

    @Test
    public void testPutWithBiggerCapacity() {
        cache.put(KEY, ITEM);

        assertTrue(cache.put("key1", new Item("value1")));
    }

    @Test
    public void testCleanUp() {
        cache.put(KEY, ITEM);

        assertEquals(1, cache.size());

        cache.clear();

        assertTrue(cache.isEmpty());
    }
}

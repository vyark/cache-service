package guava;

import org.junit.jupiter.api.Test;
import shared.Item;

import java.util.Optional;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GuavaCacheTest {
    private static final String KEY = "1";
    private static final Item ITEM = new Item(KEY);

    private GuavaCache guavaCache = new GuavaCache();

    @Test
    public void testGetFromCache() throws ExecutionException {
        guavaCache.put(KEY, ITEM);
        guavaCache.put("2", new Item("2"));
        guavaCache.put("3", new Item("3"));
        guavaCache.put("4", new Item("4"));

        guavaCache.displayCache();

        assertEquals(Optional.of(ITEM), guavaCache.get(KEY));
        assertEquals(4, guavaCache.size());

        guavaCache.clear();

        assertEquals(0, guavaCache.size());
    }

    @Test
    public void testPutIntoCache() throws ExecutionException {
        guavaCache.put(KEY, ITEM);

        assertEquals(Optional.of(ITEM), guavaCache.get(KEY));
    }

}

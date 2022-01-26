package guava;

import org.junit.Test;
import shared.CacheService;
import shared.Item;

public class GuavaCacheServiceTest {
    CacheService cacheService = new GuavaCacheService();

    @Test
    public void get() {
        cacheService.get("1");
    }

    @Test
    public void put() {
        cacheService.put("1", new Item("1"));
    }

}

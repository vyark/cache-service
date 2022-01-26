package simplejava;

import guava.GuavaCacheService;
import org.junit.Test;
import shared.CacheService;
import shared.Item;

public class JavaCacheServiceTest {
    CacheService cacheService = new JavaCacheService();

    @Test
    public void get() {
        cacheService.get("1");
    }

    @Test
    public void put() {
        cacheService.put("1", new Item("1"));
    }
}

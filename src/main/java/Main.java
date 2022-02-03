import lru.LRUCacheService;
import shared.CacheService;
import shared.Item;
import lfu.LFUCacheService;

import java.util.concurrent.ExecutionException;

public class Main {

    public static void main(String[] args) throws ExecutionException {
        // lru cache service
        CacheService lruCacheService = new LRUCacheService();
        System.out.println("LRU Cache Service");
        callCache(lruCacheService);

        System.out.println("\n\n");

        // lfu cache service
        CacheService lfuCacheService = new LFUCacheService();
        System.out.println("LFU Cache Service");
        callCache(lfuCacheService);
    }

    private static void callCache(CacheService cacheService) throws ExecutionException {
        cacheService.put("1", new Item("1"));
        cacheService.put("2", new Item("2"));
        cacheService.put("3", new Item("3"));
        cacheService.put("4", new Item("4"));

        cacheService.get("1");

        cacheService.displayCache();
        cacheService.displayStatistics();
    }
}

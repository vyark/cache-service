import guava.GuavaCacheService;
import shared.CacheService;
import shared.Item;
import simplejava.JavaCacheService;

public class Main {

    public static void main(String[] args) {
        // guava cache service
        CacheService guavaCacheService = new GuavaCacheService();
        System.out.println("Guava Cache Service");
        callCache(guavaCacheService);

        System.out.println("\n\n");

        // java cache service
        CacheService javaCacheService = new JavaCacheService();
        System.out.println("Java Cache Service");
        callCache(javaCacheService);
    }

    private static void callCache(CacheService cacheService) {
        cacheService.put("1", new Item("1"));
        cacheService.put("2", new Item("2"));
        cacheService.put("3", new Item("3"));
        cacheService.put("4", new Item("4"));

        cacheService.get("1");

        cacheService.displayStatistics();
    }
}

package simplejava;

import java.util.*;

public class Cache<String, Object> {

    private Map<String, Object> cache;
    private int capacity;

    public Cache(int capacity)
    {
        this.cache = new LinkedHashMap<String, Object>(capacity);
        this.capacity = capacity;
    }

    public boolean get(String key)
    {
        if (!cache.containsKey(key))
            return false;
        cache.remove(key);
        cache.get(key);
        return true;
    }

    public void getValue(String key)
    {
        if (get(key) == false)
            put(key);
    }

    public void display()
    {
        LinkedList<Object> list = new LinkedList<Object>((Collection<? extends Object>) cache);
        Iterator<Object> itr = list.descendingIterator();

        while (itr.hasNext())
            System.out.print(itr.next() + " ");
    }

    public void put(String key)
    {
        if (cache.size() == capacity) {
            Object firstKey = (Object) cache.entrySet().iterator().next();
            cache.remove(firstKey);
        }
        cache.get(key);
    }

    public int size() {
        return cache.size();
    }
}